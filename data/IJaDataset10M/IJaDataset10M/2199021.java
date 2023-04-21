package tutor.mina.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringJMXTimeServer {

    public static void main(final String[] args) {
        new ClassPathXmlApplicationContext("applicationContext.xml").registerShutdownHook();
    }
}
