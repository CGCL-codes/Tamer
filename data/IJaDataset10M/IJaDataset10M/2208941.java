package com.nokia.ats4.appmodel.util.execution;

import com.nokia.ats4.appmodel.plugin.testengine.event.TestResultBean;

/**
 * TestSetExecutedEvent, notifies when one test set pach has
 * been executed
 * 
 * @author Hannu-Pekka Hakam&auml;ki.
 * @version $Revision: 4 $
 */
public class TestSetExecutedEvent {

    private TestResultBean results;

    private String testSetName;

    /**
     * Creates a new instance of TestSetExecutedEvent
     */
    public TestSetExecutedEvent(String testSetName, TestResultBean results) {
        this.testSetName = testSetName;
        this.results = results;
    }

    public String getName() {
        return testSetName;
    }

    public TestResultBean getResults() {
        return results;
    }

    @Override
    public String toString() {
        String retval = "Test Set [" + getName() + "] \n";
        for (String id : getResults().getTestSetItems()) {
            retval += "Item: " + getResults().getItemName(id) + " [P: " + getResults().getPassedCases(id, -1) + " F: " + getResults().getFailedCases(id, -1) + " S: " + getResults().getSkippedCases(id, -1) + "]\n";
        }
        return retval;
    }
}
