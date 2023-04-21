package com.goodcodeisbeautiful.archtea.util.factory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author hata
 *
 */
public class PackageTestCase extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite ts = new TestSuite(PackageTestCase.class.getPackage().getName() + " Package TestCase");
        ts.addTest(AbstractFileFactoryElementTestCase.suite());
        ts.addTest(EqualFileFactoryElementTestCase.suite());
        ts.addTest(ExtensionFileFactoryElementTestCase.suite());
        ts.addTest(FileFactoryExceptionTestCase.suite());
        ts.addTest(RegexFileFactoryElementTestCase.suite());
        ts.addTest(SimpleFileFactoryContainerTestCase.suite());
        ts.addTest(SimpleFileFactoryConfigTestCase.suite());
        return ts;
    }
}
