package org.jazzteam.example.jms.testjmsspring_v2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author 
 */
public class StarterServer {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("org/jazzteam/example/jms/testjmsspring_v2/spring-activemq-server.xml");
    }
}
