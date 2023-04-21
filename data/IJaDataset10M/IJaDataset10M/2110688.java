package org.liris.schemerger.test.testcase;

import junit.framework.TestCase;
import org.liris.schemerger.chronicle.ChrTimeConstraint;
import org.liris.schemerger.core.event.EDate;

public class ChronicleTimeConstraintTestCase extends TestCase {

    ChrTimeConstraint ctc1;

    ChrTimeConstraint ctc2;

    ChrTimeConstraint ctc3;

    ChrTimeConstraint ctc4;

    protected void setUp() throws Exception {
        super.setUp();
        ctc1 = new ChrTimeConstraint(0, 1, EDate.n(2), EDate.n(5));
        ctc2 = new ChrTimeConstraint(0, 1, EDate.n(2), EDate.n(3));
        ctc3 = new ChrTimeConstraint(0, 1, EDate.n(0), EDate.n(4));
        ctc4 = new ChrTimeConstraint(0, 1, EDate.MIN, EDate.MAX);
    }

    public void testIsLessContrainedThan() {
        System.out.println(ctc1);
        System.out.println(ctc2);
        System.out.println(ctc3);
        System.out.println(ctc4);
        assertTrue(ctc1.isMoreContrainedThan(ctc1));
        assertTrue(ctc2.isMoreContrainedThan(ctc2));
        assertTrue(ctc3.isMoreContrainedThan(ctc3));
        assertTrue(ctc4.isMoreContrainedThan(ctc4));
        assertTrue(ctc2.isMoreContrainedThan(ctc1));
        assertFalse(ctc4.isMoreContrainedThan(ctc1));
        assertFalse(ctc4.isMoreContrainedThan(ctc2));
        assertFalse(ctc4.isMoreContrainedThan(ctc3));
        assertFalse(ctc3.isMoreContrainedThan(ctc2));
        assertTrue(ctc1.isMoreContrainedThan(ctc4));
        assertTrue(ctc2.isMoreContrainedThan(ctc4));
        assertTrue(ctc3.isMoreContrainedThan(ctc4));
        assertFalse(ctc3.isMoreContrainedThan(ctc1));
        assertFalse(ctc1.isMoreContrainedThan(ctc3));
        assertTrue(ctc2.isMoreContrainedThan(ctc1));
        assertTrue(ctc2.isMoreContrainedThan(ctc3));
    }
}
