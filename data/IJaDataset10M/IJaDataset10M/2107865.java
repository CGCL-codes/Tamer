package com.apress.prospringintegration.transform;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.support.MessageBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: wenwei
 * Date: 4/15/11
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transformer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:transformer.xml");
        MessageChannel input = context.getBean("input", MessageChannel.class);
        PollableChannel output = context.getBean("output", PollableChannel.class);
        Map<String, String> customerMap = new HashMap<String, String>();
        customerMap.put("firstName", "John");
        customerMap.put("lastName", "Smith");
        customerMap.put("address", "100 State Street");
        customerMap.put("city", "Los Angeles");
        customerMap.put("state", "CA");
        customerMap.put("zip", "90064");
        Message<Map<String, String>> message = MessageBuilder.withPayload(customerMap).build();
        input.send(message);
        Message<?> reply = output.receive();
        System.out.println("received: " + reply.getPayload());
    }
}
