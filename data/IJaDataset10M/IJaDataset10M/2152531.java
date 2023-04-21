package xqts.functions.nodeseqfunc.seqdocfunc;

import junit.framework.TestCase;
import xqts.XQTSTestBase;

public class SeqDocFuncTest extends TestCase {

    private static final String TEST_PATH = "((//ns:test-group[count(child::ns:test-group)=0])[255]//ns:test-case)";

    private static final String TARGET_XQTS_VERSION = "1.0.2";

    private final XQTSTestBase xqts;

    public SeqDocFuncTest() {
        super(SeqDocFuncTest.class.getName());
        this.xqts = new XQTSTestBase(SeqDocFuncTest.class.getName(), TARGET_XQTS_VERSION);
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc1() throws Exception {
        xqts.invokeTest(TEST_PATH + "[1]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc2() throws Exception {
        xqts.invokeTest(TEST_PATH + "[2]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc3() throws Exception {
        xqts.invokeTest(TEST_PATH + "[3]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc4() throws Exception {
        xqts.invokeTest(TEST_PATH + "[4]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc5() throws Exception {
        xqts.invokeTest(TEST_PATH + "[5]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc6() throws Exception {
        xqts.invokeTest(TEST_PATH + "[6]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc7() throws Exception {
        xqts.invokeTest(TEST_PATH + "[7]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc15() throws Exception {
        xqts.invokeTest(TEST_PATH + "[8]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc16() throws Exception {
        xqts.invokeTest(TEST_PATH + "[9]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc17() throws Exception {
        xqts.invokeTest(TEST_PATH + "[10]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc18() throws Exception {
        xqts.invokeTest(TEST_PATH + "[11]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc19() throws Exception {
        xqts.invokeTest(TEST_PATH + "[12]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc20() throws Exception {
        xqts.invokeTest(TEST_PATH + "[13]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc21() throws Exception {
        xqts.invokeTest(TEST_PATH + "[14]");
    }

    @org.junit.Test(timeout = 300000)
    public void testFnDoc22() throws Exception {
        xqts.invokeTest(TEST_PATH + "[15]");
    }
}
