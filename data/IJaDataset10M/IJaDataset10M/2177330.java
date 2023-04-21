package org.easiermock.invocationhandlers;

import java.lang.reflect.Proxy;
import org.junit.Assert;
import org.junit.Test;

public class UnsupportedOperationInvocationHandlerTest {

    @Test
    public void testInvocation() {
        TestInterface testObject = (TestInterface) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { TestInterface.class }, UnsupportedOperationInvocationHandler.INSTANCE);
        try {
            testObject.testMethod();
            Assert.fail("Expected exception");
        } catch (UnsupportedOperationException e) {
        }
    }

    public static interface TestInterface {

        void testMethod();
    }
}
