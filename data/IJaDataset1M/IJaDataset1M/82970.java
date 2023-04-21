package xqts.catalog;

import junit.framework.TestCase;
import xqts.XQTSTestBase;

public class CatalogTest extends TestCase {

    private static final String TEST_PATH = "((//ns:test-group[count(child::ns:test-group)=0])[296]//ns:test-case)";

    private static final String TARGET_XQTS_VERSION = "1.0.2";

    private final XQTSTestBase xqts;

    public CatalogTest() {
        super(CatalogTest.class.getName());
        this.xqts = new XQTSTestBase(CatalogTest.class.getName(), TARGET_XQTS_VERSION);
    }

    @org.junit.Test(timeout = 300000)
    public void testCatalog001() throws Exception {
        xqts.invokeTest(TEST_PATH + "[1]");
    }

    @org.junit.Test(timeout = 300000)
    public void testCatalog002() throws Exception {
        xqts.invokeTest(TEST_PATH + "[2]");
    }

    @org.junit.Test(timeout = 300000)
    public void testCatalog003() throws Exception {
        xqts.invokeTest(TEST_PATH + "[3]");
    }
}
