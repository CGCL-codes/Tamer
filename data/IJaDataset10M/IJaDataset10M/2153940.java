package org.rob.confjsflistener;

import org.rob.confjsflistener.simplelistener.configuration.SimpleListenerConfigurationImpl;

/**
 * @author Roberto
 *
 */
public class TestSimpleConfiguration extends SimpleListenerConfigurationImpl {

    /**
	 * Default constructor
	 */
    public TestSimpleConfiguration() {
    }

    /**
	 * Test additional property
	 */
    private String testProp = null;

    /**
	 * @return the testProp
	 */
    public String getTestProp() {
        return this.testProp;
    }

    /**
	 * @param testProp the testProp to set
	 */
    public void setTestProp(String testProp) {
        this.testProp = testProp;
    }
}
