package org.jaimz.telex.tests;

import java.util.*;
import org.jaimz.telex.logging.*;
import org.jaimz.telex.logging.LogElement.InvalidLogElementException;

public class TestUtils {

    private static HashMap<String, Object> dummyActionAtts = new HashMap<String, Object>();

    static {
        dummyActionAtts.put(Action.PropertyNames.ApplicationId, "TestUtils");
        dummyActionAtts.put(Action.PropertyNames.CommandId, "DummyCommand");
        dummyActionAtts.put(Action.PropertyNames.Weight, 1L);
        dummyActionAtts.put(LogElement.PropertyNames.Source, "DummySource");
    }

    public static Action makeDummyAction() throws InvalidLogElementException {
        Action result = null;
        result = new Action(dummyActionAtts);
        return result;
    }

    public static Action makeDummyAction(String cmdID) throws InvalidLogElementException {
        Action result = null;
        HashMap<String, Object> actionAtts = new HashMap<String, Object>();
        actionAtts.put(Action.PropertyNames.ApplicationId, "TestUtils");
        actionAtts.put(Action.PropertyNames.CommandId, cmdID);
        actionAtts.put(Action.PropertyNames.Weight, 1L);
        actionAtts.put(LogElement.PropertyNames.Source, "DummySource");
        result = new Action(actionAtts);
        return result;
    }

    public static Log makeDummyLogToLength(int length) throws InvalidLogElementException {
        Log l = new Log();
        for (int ctr = 0; ctr < length; ++ctr) l.append(makeDummyAction());
        return l;
    }
}
