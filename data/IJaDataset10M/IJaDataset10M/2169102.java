package com.narirelays.ems.jms.sample.server;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class JMSReceiverSync {

    public static void main(String[] args) {
        try {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("app-context.xml");
            JmsTemplate jmsTemplate = (JmsTemplate) ctx.getBean("jmsTemplate");
            int counter = 0;
            while (counter < 10) {
                Object msg = jmsTemplate.receiveAndConvert();
                if (msg instanceof String) {
                    System.out.println("Received: " + msg);
                }
                counter++;
            }
            System.exit(0);
        } catch (Exception up) {
            up.printStackTrace();
        }
    }
}
