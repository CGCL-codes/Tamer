package org.punit.builder;

import java.io.*;

/**
 * Interface for test suite builder.
 * @see TestSuiteLabel
 */
public interface TestSuiteBuilder extends Serializable {

    /**
	 * Builds the test suite. Returns a list of test classes. TestSuite will be
	 * presented as TestSuiteLabel object.
	 * 
	 * @param clazz
	 * @return
	 */
    public Object[] buildTestClasses(Class testSutie);
}
