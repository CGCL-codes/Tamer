package org.testtoolinterfaces.testsuite;

import java.util.ArrayList;

public interface TestCaseFactory {

    public abstract TestCase create(String anId, String aType, int aSequence, String aDescription, ArrayList<String> aRequirementIds, TestStepArrayList anInitializationSteps, TestStepArrayList anExecutionSteps, TestScript anExecutionScript, TestStepArrayList aRestoreSteps);

    public abstract TestStepFactory getTestStepFactory();
}
