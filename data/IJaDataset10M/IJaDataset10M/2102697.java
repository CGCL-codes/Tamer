package org.zxframework.web;

import org.zxframework.ZX;
import org.zxframework.util.TestUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests {@link org.zxframework.web.PageBuilder} methods.
 * 
 * @author Michael Brewer
 * @author Bertus Dispa
 * @author David Swann
 * @since 1.5
 * @version 0.0.1
 */
public class PageBuilderTest extends TestCase {

    private ZX zx;

    /**
     * Constructor for PageBuilderTest.
     * @param name The name of the unit test.
     */
    public PageBuilderTest(String name) {
        super(name);
    }

    /**
     * @param args Arguments.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(PageBuilderTest.class);
    }

    /**
     * @return Returns the test to run
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(PageBuilderTest.class);
        suite.setName("PageBuilder Tests");
        return suite;
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        zx = new ZX(TestUtil.getCfgPath());
        super.setUp();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        zx.cleanup();
        super.tearDown();
    }
}
