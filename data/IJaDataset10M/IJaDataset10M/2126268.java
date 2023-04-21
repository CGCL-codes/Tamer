package org.callbackparams.junit4;

import java.io.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

class SuperClassWithOverriddenTestMethod {

    @Test
    public void callbackTestMethod(Comparable<File> c) {
        fail("This method should have been overridden");
    }
}

/**
 * Overriding a test-method of a super-class should result in only one
 * callback-proxy method.
 *
 * @author Henrik Kaipe
 */
@RunWith(CallbackParamsRunner.class)
public class TestOverridingCallbackTestMethod extends SuperClassWithOverriddenTestMethod {

    private static int numberOfInvocations = 0;

    @Test
    @Override
    public void callbackTestMethod(Comparable<File> c) {
        assertEquals("This test-method is to be executed once only", 1, ++numberOfInvocations);
    }

    enum SingleTestRun {

        SINGLE_TESTRUN
    }
}
