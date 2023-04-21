package info.repo.didl.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Patrick Hochstenbach <patrick.hochstenbach@ugent.be>
 */
public class DIDLInfoTest extends TestCase {

    public DIDLInfoTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DIDLInfoTest.class);
        return suite;
    }

    /**
     * Test of getContent method, of class info.repo.didl.impl.DIDLInfo.
     */
    public void testSetGetContent() {
        System.out.println("getContent");
        DIDLInfo instance = new DIDLInfo();
        Object expResult = new String("Hello, World");
        instance.setContent(expResult);
        assertTrue(instance.getContent() == expResult);
    }
}
