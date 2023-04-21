package org.testium;

import java.util.ArrayList;
import java.util.Hashtable;
import org.testtoolinterfaces.testsuite.TestCase;
import org.testtoolinterfaces.testsuite.TestCaseFactory;
import org.testtoolinterfaces.testsuite.TestScript;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.testsuite.TestStepFactory;
import org.testtoolinterfaces.utils.Trace;

public class TestCaseMetaFactory implements TestCaseFactory {

    private Hashtable<String, TestCaseFactory> myFactories;

    private TestStepFactory myStepFactory;

    public TestCaseMetaFactory(Hashtable<String, TestCaseFactory> aTcFactories, TestStepFactory aStepFactory) {
        Trace.println(Trace.CONSTRUCTOR, "TestCaseMetaFactory( " + aTcFactories.size() + " TestCaseFactory, " + aStepFactory, true);
        myFactories = aTcFactories;
        myStepFactory = aStepFactory;
    }

    public TestCase create(String anId, String aType, int aSequence, String aDescription, ArrayList<String> aRequirementIds, TestStepArrayList anInitializationSteps, TestStepArrayList anExecutionSteps, TestScript aTestCaseScript, TestStepArrayList aRestoreSteps) {
        Trace.println(Trace.SUITE, "create( " + anId + ", " + aType + ", " + aSequence + ", " + aRequirementIds.size() + " Requirements, " + anInitializationSteps + ", " + anExecutionSteps + ", " + aTestCaseScript + ", " + aRestoreSteps + " )", true);
        TestCaseFactory factory;
        if (myFactories.containsKey(aType)) {
            factory = myFactories.get(aType);
        } else {
            factory = myFactories.get("standard");
        }
        return factory.create(anId, aType, aSequence, aDescription, aRequirementIds, anInitializationSteps, anExecutionSteps, aTestCaseScript, aRestoreSteps);
    }

    public TestStepFactory getTestStepFactory() {
        return myStepFactory;
    }
}
