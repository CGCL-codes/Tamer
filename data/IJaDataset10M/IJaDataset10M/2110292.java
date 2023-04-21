package mobilestock.server.metamodel.abstracttest;

import org.springframework.context.ApplicationContext;
import mobilestock.server.core.spring.ApplicationContextProvider;
import mobilestock.server.core.spring.ApplicationContextProviderSingleton;

public abstract class AbstractTest {

    protected ApplicationContextProvider contextProvider;

    protected ApplicationContext context;

    public abstract Object createUnderTest();

    public abstract void setUp();

    private void createContextProvider() {
        this.contextProvider = new ApplicationContextProviderSingleton();
    }

    protected ApplicationContext getApplicationContext() {
        if (contextProvider == null) {
            this.createContextProvider();
        }
        return contextProvider.getContext();
    }
}
