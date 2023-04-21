package eu.soa4all.wp6.composer.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {

    private static ApplicationContext appContext;

    private static SpringContext instance;

    private static final String CONFIG_FILE = "composerconfiguration.xml";

    public static SpringContext getInstance() {
        if (null == instance) {
            instance = new SpringContext();
            appContext = new ClassPathXmlApplicationContext(CONFIG_FILE);
        }
        return instance;
    }

    public Object getBean(String bean) {
        return appContext.getBean(bean);
    }
}
