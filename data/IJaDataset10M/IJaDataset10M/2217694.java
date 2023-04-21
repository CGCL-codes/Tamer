package xqts.expressions.operators.arithexpr.durationarith.datesubtractdtd;

import junit.framework.TestCase;
import xqts.XQTSTestBase;

public class DateSubtractDTDTest extends TestCase {

    private static final String TEST_PATH = "((//ns:test-group[count(child::ns:test-group)=0])[48]//ns:test-case)";

    private static final String TARGET_XQTS_VERSION = "1.0.2";

    private final XQTSTestBase xqts;

    public DateSubtractDTDTest() {
        super(DateSubtractDTDTest.class.getName());
        this.xqts = new XQTSTestBase(DateSubtractDTDTest.class.getName(), TARGET_XQTS_VERSION);
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate2args1() throws Exception {
        xqts.invokeTest(TEST_PATH + "[1]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate2args2() throws Exception {
        xqts.invokeTest(TEST_PATH + "[2]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate2args3() throws Exception {
        xqts.invokeTest(TEST_PATH + "[3]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate2args4() throws Exception {
        xqts.invokeTest(TEST_PATH + "[4]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate2args5() throws Exception {
        xqts.invokeTest(TEST_PATH + "[5]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate1() throws Exception {
        xqts.invokeTest(TEST_PATH + "[6]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate2() throws Exception {
        xqts.invokeTest(TEST_PATH + "[7]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate3() throws Exception {
        xqts.invokeTest(TEST_PATH + "[8]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate4() throws Exception {
        xqts.invokeTest(TEST_PATH + "[9]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate5() throws Exception {
        xqts.invokeTest(TEST_PATH + "[10]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate6() throws Exception {
        xqts.invokeTest(TEST_PATH + "[11]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate7() throws Exception {
        xqts.invokeTest(TEST_PATH + "[12]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate8() throws Exception {
        xqts.invokeTest(TEST_PATH + "[13]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate9() throws Exception {
        xqts.invokeTest(TEST_PATH + "[14]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate10() throws Exception {
        xqts.invokeTest(TEST_PATH + "[15]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate12() throws Exception {
        xqts.invokeTest(TEST_PATH + "[16]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate13() throws Exception {
        xqts.invokeTest(TEST_PATH + "[17]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate14() throws Exception {
        xqts.invokeTest(TEST_PATH + "[18]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate15() throws Exception {
        xqts.invokeTest(TEST_PATH + "[19]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpSubtractDayTimeDurationFromDate16() throws Exception {
        xqts.invokeTest(TEST_PATH + "[20]");
    }

    @org.junit.Test(timeout = 300000)
    public void testKDateSubtractDTD1() throws Exception {
        xqts.invokeTest(TEST_PATH + "[21]");
    }
}
