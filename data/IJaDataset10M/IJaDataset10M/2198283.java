package xqts.appendices.appendix_a4;

import junit.framework.TestCase;
import xqts.XQTSTestBase;

public class AppendixA4Test extends TestCase {

    private static final String TEST_PATH = "((//ns:test-group[count(child::ns:test-group)=0])[286]//ns:test-case)";

    private static final String TARGET_XQTS_VERSION = "1.0.2";

    private final XQTSTestBase xqts;

    public AppendixA4Test() {
        super(AppendixA4Test.class.getName());
        this.xqts = new XQTSTestBase(AppendixA4Test.class.getName(), TARGET_XQTS_VERSION);
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_1() throws Exception {
        xqts.invokeTest(TEST_PATH + "[1]");
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_2() throws Exception {
        xqts.invokeTest(TEST_PATH + "[2]");
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_3() throws Exception {
        xqts.invokeTest(TEST_PATH + "[3]");
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_4() throws Exception {
        xqts.invokeTest(TEST_PATH + "[4]");
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_5() throws Exception {
        xqts.invokeTest(TEST_PATH + "[5]");
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_6() throws Exception {
        xqts.invokeTest(TEST_PATH + "[6]");
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_7() throws Exception {
        xqts.invokeTest(TEST_PATH + "[7]");
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_8() throws Exception {
        xqts.invokeTest(TEST_PATH + "[8]");
    }

    @org.junit.Test(timeout = 300000)
    public void testAppendixA4_9() throws Exception {
        xqts.invokeTest(TEST_PATH + "[9]");
    }
}
