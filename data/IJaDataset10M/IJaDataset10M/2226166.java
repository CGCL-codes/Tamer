package org.apache.harmony.crypto.tests.javax.crypto.func;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import junit.framework.TestCase;
import targets.KeyAgreement;

public class KeyAgreementFunctionalTest extends TestCase {

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "", clazz = KeyAgreement.DH.class, method = "method", args = {  }) })
    @BrokenTest("Too slow - disabling for now")
    public void test_KeyAgreement() throws Exception {
        String[] algArray = { "DES", "DESede" };
        KeyAgreementThread kat = new KeyAgreementThread(algArray);
        kat.launcher();
        assertEquals(kat.getFailureMessages(), 0, kat.getTotalFailuresNumber());
    }
}
