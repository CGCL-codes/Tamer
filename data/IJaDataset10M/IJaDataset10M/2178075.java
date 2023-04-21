package org.lastfm.helper;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author josdem (joseluis.delacruz@gmail.com)
 * @understands a class who knows how to get the application context from spring
 */
public class ApplicationContextSingleton {

    private static ConfigurableApplicationContext applicationContext;

    public static ConfigurableApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            applicationContext = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        }
        return applicationContext;
    }
}
