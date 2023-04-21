package easyaccept.app.business;

import static easyaccept.app.business.BusinessConstants.APPLICATION_CONTEXT_FILE;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Lookups the Services available to business Layer
 * 
 * @author Fabrício Silva Epaminondas
 * 
 */
public class BusinessServiceLocator {

    /**
	 * Using the Spring implementation
	 */
    private static ApplicationContext springContext = null;

    /**
	 * Lookups the instances of Services available to application Layer
	 * 
	 * @author Fabrício Silva Epaminondas
	 * 
	 */
    public static Object lookup(String name) {
        return getSpringContext().getBean(name);
    }

    public static void setSpringContext(ApplicationContext springContext) {
        BusinessServiceLocator.springContext = springContext;
    }

    public static ApplicationContext getSpringContext() {
        if (springContext == null) springContext = createContext();
        return springContext;
    }

    private static ApplicationContext createContext() {
        return new ClassPathXmlApplicationContext(new String[] { APPLICATION_CONTEXT_FILE });
    }

    public static synchronized void destroy() {
        if (springContext != null) springContext = null;
    }
}
