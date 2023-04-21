package net.sf.brightside.travelsystem.core.spring;

import org.springframework.context.ApplicationContext;

public interface ApplicationContextFactory {

    public String[] getLocations();

    public ApplicationContext getApplicationContextInstance();
}
