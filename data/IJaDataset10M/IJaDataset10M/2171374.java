package com.unitedinternet.portal.selenium.utils.logging;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SeleniumLoggingBeanTest {

    LoggingBean beanUnderTest;

    @Before
    public void setUp() {
        beanUnderTest = new LoggingBean();
    }

    @Test
    public void getSelResultMoreThanOneComma() {
        beanUnderTest.setResult("OK,This, should,be, in, the,output");
        assertEquals("This, should,be, in, the,output", beanUnderTest.getSelResult());
    }

    @Test
    public void getSelResultOnlyOneComma() {
        beanUnderTest.setResult("OK,This should be  in  the output");
        assertEquals("This should be  in  the output", beanUnderTest.getSelResult());
    }

    @Test
    public void getSrcResultWithComma() {
        beanUnderTest.setResult("OK,does not matter in this test");
        assertEquals("OK", beanUnderTest.getSrcResult());
    }

    @Test
    public void getSrcResultWithoutComma() {
        beanUnderTest.setResult("OK");
        assertEquals("OK", beanUnderTest.getSrcResult());
    }
}
