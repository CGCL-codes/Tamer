package com.aragost.araspect;

import junit.framework.TestSuite;
import com.aragost.junit.Util;

/**
 *
 * @author  aragost
 */
public class TestAll extends TestSuite {

    /** 
   * Holds a a refernce to this class.
   */
    private static final Class THIS_CLASS = TestAll.class;

    /** 
   * Execute all TestCases in the same source tree as this class
   * itself.
   */
    public static void main(String cmdArgs[]) {
        com.aragost.junit.Util.run(THIS_CLASS, cmdArgs);
    }

    /**
   * Used by JUnit when executing the Suite.
   * 
   * @return a TestSuite will test cases in the same library as this
   *         class.
   */
    public static TestSuite suite() {
        return Util.createLibraryTest(THIS_CLASS);
    }
}
