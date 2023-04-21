package net.sf.excompcel.poi.sheet.comparator.compareobject;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This Class executes all Unit Tests in this Package.
 * 
 * @author Detlev Struebig
 * @since v0.2
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ CellCompareObjectTest.class, RowCompareObjectTest.class, SheetCompareObjectTest.class, WorkbookCompareObjectTest.class })
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.sf.excompcel.poi.comparator");
        return suite;
    }
}
