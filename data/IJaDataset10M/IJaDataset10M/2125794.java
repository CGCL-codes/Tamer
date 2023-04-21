package net.sf.brightside.qualifications.core.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextProviderSingleton implements ApplicationContextProvider {

    private static ApplicationContext applicationContext;

    @Override
    public ApplicationContext createContext() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(getContextLocations());
        context.registerShutdownHook();
        return context;
    }

    @Override
    public ApplicationContext getContext() {
        if (applicationContext == null) applicationContext = createContext();
        return applicationContext;
    }

    @Override
    public String[] getContextLocations() {
        String[] locations = new String[] { "META-INF/net/sf/brightside/qualifications/core/spring/context.xml", "META-INF/net/sf/brightside/qualifications/metamodel/context.xml", "META-INF/net/sf/brightside/qualifications/core/hibernate/context.xml", "META-INF/net/sf/brightside/qualifications/core/hsqldb/context.xml", "META-INF/net/sf/brightside/qualifications/service/context.xml" };
        return locations;
    }
}
