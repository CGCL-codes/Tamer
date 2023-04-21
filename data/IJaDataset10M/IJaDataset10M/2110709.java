package com.cosmos.acacia.beans;

import com.cosmos.acacia.annotation.Property;

/**
 *
 * @author Miro
 */
public class TestBeanA {

    @Property(parentContainerName = "primaryInfo")
    private TestBeanB testBeanB;

    public TestBeanB getTestBeanB() {
        return testBeanB;
    }

    public void setTestBeanB(TestBeanB testBeanB) {
        this.testBeanB = testBeanB;
    }
}
