package org.testtoolinterfaces.testresult;

import java.util.ArrayList;
import java.util.Hashtable;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestCaseResult extends TestResult {

    private TestCase myTestCase;

    private Hashtable<Integer, TestStepResult> myInitializationResults;

    private Hashtable<Integer, TestStepResult> myExecutionResults;

    private Hashtable<Integer, TestStepResult> myRestoreResults;

    /**
	 * @param aTestCase
	 */
    public TestCaseResult(TestCase aTestCase) {
        super();
        Trace.println(Trace.LEVEL.CONSTRUCTOR, "TestCaseResultXmlWriter( " + aTestCase + " )");
        myTestCase = aTestCase;
        myInitializationResults = new Hashtable<Integer, TestStepResult>();
        myExecutionResults = new Hashtable<Integer, TestStepResult>();
        myRestoreResults = new Hashtable<Integer, TestStepResult>();
    }

    /**
	 * @param anInitializationResult
	 */
    public void addInitialization(TestStepResult anInitializationResult) {
        Trace.println(Trace.LEVEL.SETTER);
        myInitializationResults.put(myInitializationResults.size(), anInitializationResult);
    }

    /**
	 * @param anInitializationResult
	 */
    public void addExecution(TestStepResult anExecutionResult) {
        Trace.println(Trace.LEVEL.SETTER);
        myExecutionResults.put(myExecutionResults.size(), anExecutionResult);
    }

    /**
	 * @param anInitializationResult
	 */
    public void addRestore(TestStepResult aRestoreResult) {
        Trace.println(Trace.LEVEL.SETTER);
        myRestoreResults.put(myRestoreResults.size(), aRestoreResult);
    }

    /**
	 * @return the myTestCaseName
	 */
    public String getId() {
        Trace.println(Trace.LEVEL.GETTER);
        return myTestCase.getId();
    }

    public int getSequenceNr() {
        Trace.println(Trace.LEVEL.GETTER);
        return myTestCase.getSequenceNr();
    }

    public String getDescription() {
        Trace.println(Trace.LEVEL.GETTER);
        return myTestCase.getDescription();
    }

    public ArrayList<String> getRequirements() {
        Trace.println(Trace.LEVEL.GETTER);
        return myTestCase.getRequirements();
    }

    public Hashtable<Integer, TestStepResult> getInitializationResults() {
        Trace.println(Trace.LEVEL.GETTER);
        return myInitializationResults;
    }

    public Hashtable<Integer, TestStepResult> getExecutionResults() {
        Trace.println(Trace.LEVEL.GETTER);
        return myExecutionResults;
    }

    public Hashtable<Integer, TestStepResult> getRestoreResults() {
        Trace.println(Trace.LEVEL.GETTER);
        return myRestoreResults;
    }
}
