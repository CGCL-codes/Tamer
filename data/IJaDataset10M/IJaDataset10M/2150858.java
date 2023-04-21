package tests.security.permissions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(java.security.Permissions.class)
public class AllTests extends TestCase {

    public static final Test suite() {
        TestSuite suite = new TestSuite("Tests for security permissions");
        suite.addTestSuite(JavaIoFileInputStreamTest.class);
        suite.addTestSuite(JavaIoFileOutputStreamTest.class);
        suite.addTestSuite(JavaIoFileTest.class);
        suite.addTestSuite(JavaIoObjectInputStreamTest.class);
        suite.addTestSuite(JavaIoObjectOutputStreamTest.class);
        suite.addTestSuite(JavaIoRandomAccessFileTest.class);
        suite.addTestSuite(JavaLangClassLoaderTest.class);
        suite.addTestSuite(JavaLangClassTest.class);
        suite.addTestSuite(JavaLangRuntimeTest.class);
        suite.addTestSuite(JavaLangSystemTest.class);
        suite.addTestSuite(JavaLangThreadTest.class);
        suite.addTestSuite(JavaNetDatagramSocketTest.class);
        suite.addTestSuite(JavaNetMulticastSocketTest.class);
        suite.addTestSuite(JavaNetServerSocketTest.class);
        suite.addTestSuite(JavaNetSocketTest.class);
        suite.addTestSuite(JavaSecurityPolicyTest.class);
        suite.addTestSuite(JavaSecuritySecurityTest.class);
        suite.addTestSuite(JavaUtilLocale.class);
        suite.addTestSuite(JavaUtilZipZipFile.class);
        suite.addTestSuite(JavaxSecurityAuthSubjectDomainCombiner.class);
        suite.addTestSuite(JavaxSecurityAuthSubject.class);
        suite.addTestSuite(JavaLangReflectAccessibleObjectTest.class);
        return suite;
    }
}
