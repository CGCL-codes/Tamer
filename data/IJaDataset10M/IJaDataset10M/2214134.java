package net.sf.excompcel.spreadsheet.impl;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This Class exceutes all Unit Tests in this and all sub Packages.
 * 
 * @author Detlev Struebig
 * 
 */
@RunWith(value = Suite.class)
@SuiteClasses({ net.sf.excompcel.spreadsheet.impl.odf.AllTests.class, net.sf.excompcel.spreadsheet.impl.odf.comparator.AllTests.class, net.sf.excompcel.spreadsheet.impl.odf.comparator.compareobject.AllTests.class, net.sf.excompcel.spreadsheet.impl.poihssf.AllTests.class, net.sf.excompcel.spreadsheet.impl.poihssf.comparator.AllTests.class, net.sf.excompcel.spreadsheet.impl.poihssf.comparator.compareobject.AllTests.class, net.sf.excompcel.spreadsheet.impl.poixssf.AllTests.class, net.sf.excompcel.spreadsheet.impl.poixssf.comparator.AllTests.class, net.sf.excompcel.spreadsheet.impl.poixssf.comparator.compareobject.AllTests.class })
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.sf.excompcel.spreadsheet");
        suite.addTest(AllTests.suite());
        return suite;
    }
}
