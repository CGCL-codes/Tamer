package xqts.expressions.operators.arithexpr.durationarith.yearmonthdurationadd;

import junit.framework.TestCase;
import xqts.XQTSTestBase;

public class YearMonthDurationAddTest extends TestCase {

    private static final String TEST_PATH = "((//ns:test-group[count(child::ns:test-group)=0])[28]//ns:test-case)";

    private static final String TARGET_XQTS_VERSION = "1.0.2";

    private final XQTSTestBase xqts;

    public YearMonthDurationAddTest() {
        super(YearMonthDurationAddTest.class.getName());
        this.xqts = new XQTSTestBase(YearMonthDurationAddTest.class.getName(), TARGET_XQTS_VERSION);
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations2args1() throws Exception {
        xqts.invokeTest(TEST_PATH + "[1]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations2args2() throws Exception {
        xqts.invokeTest(TEST_PATH + "[2]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations2args3() throws Exception {
        xqts.invokeTest(TEST_PATH + "[3]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations2args4() throws Exception {
        xqts.invokeTest(TEST_PATH + "[4]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations2args5() throws Exception {
        xqts.invokeTest(TEST_PATH + "[5]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations1() throws Exception {
        xqts.invokeTest(TEST_PATH + "[6]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations2() throws Exception {
        xqts.invokeTest(TEST_PATH + "[7]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations3() throws Exception {
        xqts.invokeTest(TEST_PATH + "[8]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations4() throws Exception {
        xqts.invokeTest(TEST_PATH + "[9]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations5() throws Exception {
        xqts.invokeTest(TEST_PATH + "[10]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations6() throws Exception {
        xqts.invokeTest(TEST_PATH + "[11]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations7() throws Exception {
        xqts.invokeTest(TEST_PATH + "[12]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations8() throws Exception {
        xqts.invokeTest(TEST_PATH + "[13]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations9() throws Exception {
        xqts.invokeTest(TEST_PATH + "[14]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations10() throws Exception {
        xqts.invokeTest(TEST_PATH + "[15]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations11() throws Exception {
        xqts.invokeTest(TEST_PATH + "[16]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations12() throws Exception {
        xqts.invokeTest(TEST_PATH + "[17]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations13() throws Exception {
        xqts.invokeTest(TEST_PATH + "[18]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations14() throws Exception {
        xqts.invokeTest(TEST_PATH + "[19]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations15() throws Exception {
        xqts.invokeTest(TEST_PATH + "[20]");
    }

    @org.junit.Test(timeout = 300000)
    public void testOpAddYearMonthDurations16() throws Exception {
        xqts.invokeTest(TEST_PATH + "[21]");
    }

    @org.junit.Test(timeout = 300000)
    public void testKYearMonthDurationAdd1() throws Exception {
        xqts.invokeTest(TEST_PATH + "[22]");
    }

    @org.junit.Test(timeout = 300000)
    public void testKYearMonthDurationAdd2() throws Exception {
        xqts.invokeTest(TEST_PATH + "[23]");
    }

    @org.junit.Test(timeout = 300000)
    public void testKYearMonthDurationAdd3() throws Exception {
        xqts.invokeTest(TEST_PATH + "[24]");
    }
}
