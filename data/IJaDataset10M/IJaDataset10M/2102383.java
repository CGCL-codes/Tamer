package net.sf.fc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import net.sf.fc.io.FileCopierTest;

/**
 *
 * @author david
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ FileCopierTest.class })
public class AllTestsSuite {

    @BeforeClass
    public static void setUp() {
        System.out.println("createTestDirectory");
        TestUtil.createTestDirectory();
        TestUtil.createFileCopyEventListener();
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("deleteTestDirectory");
        TestUtil.deleteTestDirectory();
    }
}
