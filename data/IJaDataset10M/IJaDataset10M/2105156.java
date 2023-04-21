package org.openexi.fujitsu.schema;

import java.net.URL;
import junit.framework.TestCase;

/**
 * Base class for test cases.
 */
public abstract class TestBase extends TestCase {

    public TestBase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
   * Converts a relative systemId into its absolute form. Resolution is
   * to be made relative to the class.
   * @param relId relative systemId
   * @return absolute systemId
   */
    protected String resolveSystemId(String relId) {
        return getClass().getResource(relId).toString();
    }

    /**
   * Converts a relative systemId into an URL. Resolution is
   * to be made relative to the class.
   * @param relId relative systemId
   * @return URL
   */
    protected URL resolveSystemIdAsURL(String relId) {
        return getClass().getResource(relId);
    }
}
