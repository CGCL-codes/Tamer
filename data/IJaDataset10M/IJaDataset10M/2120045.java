package net.sf.brightside.simpark.core.spring;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.assertNotNull;

public abstract class ApplicationContextProviderTest {

    private ApplicationContextProvider providerUnderTest;

    protected abstract ApplicationContextProvider createUnderTest();

    @BeforeMethod
    public void setUp() {
        providerUnderTest = createUnderTest();
    }

    @Test
    public void testCreateContext() {
        assertNotNull(providerUnderTest.createContext());
    }
}
