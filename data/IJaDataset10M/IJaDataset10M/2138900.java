package net.sf.refactorit.test.netbeans;

import net.sf.refactorit.test.netbeans.projectoptions.PathUtilTest;
import net.sf.refactorit.test.netbeans.vfs.ClassPathReleaseTest;
import net.sf.refactorit.test.netbeans.vfs.FileNotFoundReasonTest;
import net.sf.refactorit.test.netbeans.vfs.NBSourceIdentityTest;
import net.sf.refactorit.test.netbeans.vfs.NBSourcePathTest;
import net.sf.refactorit.test.netbeans.vfs.RenameLoopingTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** @author  risto */
public class AllTests extends TestCase {

    public AllTests(String name) {
        super(name);
    }

    public static Test suite() {
        try {
            TestSuite result = new TestSuite(AllTests.class.getName());
            try {
                Class classPathElementTests = Class.forName("net.sf.refactorit.netbeans.ide4.vfs.ClassPathElementTest");
                result.addTestSuite(classPathElementTests);
            } catch (ClassNotFoundException e) {
            }
            result.addTestSuite(ShortcutTest.class);
            result.addTestSuite(ExceptionCatchTest.class);
            result.addTestSuite(NBSourcePathTest.class);
            result.addTestSuite(ClassPathReleaseTest.class);
            result.addTestSuite(NBSourceIdentityTest.class);
            result.addTestSuite(RenameLoopingTest.class);
            result.addTest(FileNotFoundReasonTest.suite());
            result.addTest(PathItemReferenceTest.suite());
            result.addTestSuite(PathUtilTest.class);
            result.addTestSuite(BootClasspathTest.class);
            result.addTestSuite(NBSourcepathModelTest.class);
            result.addTestSuite(SettingsDialogTest.class);
            result.addTestSuite(RenamePackageTest.class);
            result.addTestSuite(NBControllerTest.class);
            return result;
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e);
        }
    }
}
