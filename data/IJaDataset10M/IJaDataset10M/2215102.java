package org.blindmandesign;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;

/**
 * Base test case that provides utility methods for
 * the rest of the tests.
 *
 * @author Nathan Bubna
 * @version $Id: BaseTestCase.java 747119 2009-02-23 20:10:04Z nbubna $
 */
public abstract class TestBase extends TestCase {

    protected boolean DEBUG = false;

    public TestBase(String name) {
        super(name);
        String testcase = System.getProperty("case");
        if (testcase != null) {
            DEBUG = testcase.equals(getClass().getName());
        }
    }

    protected void setUp() throws Exception {
    }

    public void tearDown() {
    }

    protected final void info(String msg, Object... args) {
        if (DEBUG) {
            info(String.format(msg, args), (Throwable) null);
        }
    }

    protected void info(String msg, Throwable t) {
        if (DEBUG) {
            System.out.println(msg);
            if (t != null) {
                t.printStackTrace();
            }
        }
    }
}
