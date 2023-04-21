package net.sf.excompcel.spreadsheet;

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
@SuiteClasses({ ECSpreadSheetFileHandlerFactory_Base.class, ECSpreadSheetFileHandlerFactory_XLSTest.class, ECSpreadSheetFileHandlerFactory_XLSXTest.class, ECSpreadSheetFileHandlerFactory_ODFTest.class, ECTypeEnumTest.class, EnumECCellStyleTest.class, EnumECFontTest.class })
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.sf.excompcel.spreadsheet");
        suite.addTest(AllTests.suite());
        return suite;
    }
}
