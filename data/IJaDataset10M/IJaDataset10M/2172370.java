package org.limaloa.spring;

import junit.framework.Assert;

/**
 * Used by <code>AdapterBeanDefinitionParserTest</code>.
 * 
 * @author Chris Nappin
 */
public class Target1 {

    public void targetMethod1() {
        AdapterBeanDefinitionParserTest.methodInvoked = true;
    }

    public boolean targetMethod2(long i) {
        Assert.assertEquals("incorrect input parameter", 42, i);
        AdapterBeanDefinitionParserTest.methodInvoked = true;
        return true;
    }
}
