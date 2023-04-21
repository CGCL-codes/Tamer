package net.sourceforge.unitth.test.junititems;

import static org.junit.Assert.assertEquals;
import java.util.Locale;
import net.sourceforge.unitth.test.UnitTHUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import unitth.junit.TestCase;
import unitth.junit.TestCaseSummary;
import unitth.junit.TestCaseVerdict;

public class TestCaseSummaryTester {

    @BeforeClass
    public static void preamble() {
        Locale.setDefault(Locale.US);
    }

    @Test
    public void test_Ctor_001() throws Exception {
        System.out.println(UnitTHUtils.getCallingMethod());
        TestCaseVerdict[] v1 = { TestCaseVerdict.e_PASS, TestCaseVerdict.e_PASS, TestCaseVerdict.e_PASS, TestCaseVerdict.e_ERROR, TestCaseVerdict.e_FAIL };
        TestCaseSummary tcs = null;
        for (int i = 0; i < v1.length; i++) {
            TestCase tc = new TestCase();
            tc.setClassName("Module");
            tc.setName("tc");
            tc.setExecutionTime("1.0");
            tc.setVerdict(v1[i]);
            if (0 == i) {
                tcs = new TestCaseSummary(tc, i + 1);
            } else {
                tcs.increment(tc, i + 1);
            }
        }
        assertEquals(null, tcs.getSpreadAt(0));
        assertEquals(TestCaseVerdict.e_PASS, tcs.getSpreadAt(1));
        assertEquals(TestCaseVerdict.e_PASS, tcs.getSpreadAt(2));
        assertEquals(TestCaseVerdict.e_PASS, tcs.getSpreadAt(3));
        assertEquals(TestCaseVerdict.e_ERROR, tcs.getSpreadAt(4));
        assertEquals(TestCaseVerdict.e_FAIL, tcs.getSpreadAt(5));
        assertEquals(null, tcs.getSpreadAt(6));
    }

    @Test
    public void test_getClassPlusTestCaseName_001() throws Exception {
        System.out.println(UnitTHUtils.getCallingMethod());
        TestCase tc = new TestCase();
        tc.setClassName("moda.modb.modc");
        tc.setName("tc");
        TestCaseSummary tcs = new TestCaseSummary(tc, 0);
        assertEquals("modb.modc.tc", tcs.getClassPlusTestCaseName("moda"));
        assertEquals("modc.tc", tcs.getClassPlusTestCaseName("moda.modb"));
        assertEquals("tc", tcs.getClassPlusTestCaseName("moda.modb.modc"));
    }

    @Test
    public void test_getClassPlusTestCaseName_002() throws Exception {
        System.out.println(UnitTHUtils.getCallingMethod());
        TestCase tc = new TestCase();
        tc.setClassName("moda.modb.modc");
        tc.setName("tc");
        TestCaseSummary tcs = new TestCaseSummary(tc, 0);
        assertEquals("tc", tcs.getClassPlusTestCaseName("xxxx"));
        assertEquals("tc", tcs.getClassPlusTestCaseName("UNDEF"));
        assertEquals("tc", tcs.getClassPlusTestCaseName(""));
    }
}
