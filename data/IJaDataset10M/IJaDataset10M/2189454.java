package net.sf.brightside.beautyshop.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public interface ApplicationContextProvider {

    ApplicationContext getContext();

    ApplicationContext createContext();

    String[] getContextLocations();
}
