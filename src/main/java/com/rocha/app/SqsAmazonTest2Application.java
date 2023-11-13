package com.rocha.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;

@RestController
@SpringBootApplication
public class SqsAmazonTest2Application {

	Logger logger = LoggerFactory.getLogger(SqsAmazonTest2Application.class);

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	@Value("${cloud.aws.end-point.uri}")
	private String endpoint;

	@GetMapping("/send/{message}")
	public void sendMessageToQueue(@PathVariable String message) {
		queueMessagingTemplate.send(endpoint, MessageBuilder.withPayload(message).build());
	}

	@SqsListener(value = "testQueue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void loadMessageFromSQS(String message) {
		logger.info("message from SQS Queue {}", message);
	}

	public static void main(String[] args) {
		SpringApplication.run(SqsAmazonTest2Application.class, args);
	}

}
