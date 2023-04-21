package uk.org.sith.sproing.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * Provides access to the spring bean factory
 * 
 * @author Antony Lees
 */
public class SpringBeanFactoryProvider {

    private static final Log LOGGER = LogFactory.getLog(SpringBeanFactoryProvider.class);

    /**
    * Returns the bean factory created by the application context. This allows access to spring beans
    * 
    * @return the bean factory
    */
    public static AutowireCapableBeanFactory getBeanFactory() {
        return ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory();
    }

    /**
    * Autowires the given object by name into the existing application context
    * 
    * @param object the object to autowire
    */
    public static void autowireByName(Object object) {
        LOGGER.debug("Wiring bean of type: " + object.getClass().getName());
        ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory().autowireBeanProperties(object, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
    }
}
