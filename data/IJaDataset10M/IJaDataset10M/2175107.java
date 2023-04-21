package org.testtoolinterfaces.testsuite;

import java.util.ArrayList;

public interface TestGroup extends TestEntry {

    /**
	 * @return the Requirement IDs
	 */
    public ArrayList<String> getRequirements();

    /**
	 * @return the Initialization Steps
	 */
    public TestStepArrayList getInitializationSteps();

    /**
	 * @return the Execution Entries
	 */
    public TestEntryArrayList getExecutionEntries();

    /**
	 * @return the Restore Steps
	 */
    public TestStepArrayList getRestoreSteps();
}
