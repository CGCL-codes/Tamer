package com.entelience.test.test99bugzilla;

import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.entelience.util.DateHelper;

public class testBug501 extends junit.framework.TestCase {

    protected boolean done_something;

    /** JUnit 2.0 */
    public static Test suite() {
        return new TestSuite(testBug501.class);
    }

    /** test simple date calculations. */
    public void test01_ad_date() throws Exception {
        Date d = DateHelper.fromActiveDirectoryOrNull("126932203963860503");
        System.out.println("GOT: [" + d + "]");
    }
}
