package org.testtoolinterfaces.testsuite;

import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TestStepImpl extends TestEntryImpl implements TestStep {

    private ActionType myActionType;

    private String myCommand;

    private ParameterTable myParameters;

    /**
	 * @param anActionType one of initialize, action, check, restore
	 * @param aSequenceNr
	 * @param aDescription
	 * @param aCommand
	 */
    public TestStepImpl(ActionType anActionType, int aSequenceNr, String aDescription, String aCommand, ParameterTable aParameters) {
        super("TestStep_" + aSequenceNr, TestEntry.TYPE.Step, aDescription, aSequenceNr);
        Trace.println(Trace.LEVEL.CONSTRUCTOR, "TestStepImpl( " + anActionType + ", " + aSequenceNr + ", " + aDescription + ", " + aCommand + " )", true);
        myActionType = anActionType;
        myCommand = aCommand;
        myParameters = aParameters;
    }

    public String getCommand() {
        return myCommand;
    }

    public ParameterTable getParameters() {
        return myParameters;
    }

    public ActionType getActionType() {
        return myActionType;
    }

    public String toString() {
        return myActionType.toString() + ": " + myCommand;
    }

    public String getScript() {
        return "";
    }
}
