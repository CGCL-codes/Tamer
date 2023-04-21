package com.nhncorp.usf.core.result.helper;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Web Platform Development Team
 */
public class FreemarkerInitializeExceptionTest {

    @Test
    public void testException() {
        FreemarkerInitializeException e = new FreemarkerInitializeException("message");
        Assert.assertTrue(e instanceof Exception);
        Assert.assertEquals("message", e.getMessage());
        FreemarkerInitializeException e1 = new FreemarkerInitializeException();
        Assert.assertTrue(e1 instanceof Exception);
        FreemarkerInitializeException e3 = new FreemarkerInitializeException(e1);
        Assert.assertEquals(e1, e3.getCause());
    }
}
