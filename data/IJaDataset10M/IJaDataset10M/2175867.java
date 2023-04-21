package org.testtoolinterfaces.testsuite;

import java.util.ArrayList;

public interface TestCase extends TestEntry {

    /**
	 * @return the Requirement IDs
	 */
    public ArrayList<String> getRequirements();

    /**
	 * @return the Initialization Steps
	 */
    public TestStepArrayList getInitializationSteps();

    /**
	 * @return the Execution Steps
	 */
    public TestStepArrayList getExecutionSteps();

    /**
	 * @return the Restore Steps
	 */
    public TestStepArrayList getRestoreSteps();
}
