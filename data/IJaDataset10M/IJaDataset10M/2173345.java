package net.sf.brightside.gymcalendar.core.spring;

public class ApplicationContextProviderSingletonTest extends ApplicationContextProviderTest {

    @Override
    protected ApplicationContextProvider createUnderTest() {
        return new ApplicationContextProviderSingleton();
    }
}
