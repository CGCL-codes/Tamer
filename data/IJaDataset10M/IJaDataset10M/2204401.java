package android.security.tests;

import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

/**
 * Instrumentation Test Runner for all KeyStore unit tests.
 *
 * Running all tests:
 *
 *   runtest keystore-unit
 * or
 *   adb shell am instrument -w android.security.tests/.KeyStoreTestRunner
 */
public class KeyStoreTestRunner extends InstrumentationTestRunner {

    @Override
    public TestSuite getAllTests() {
        TestSuite suite = new InstrumentationTestSuite(this);
        suite.addTestSuite(android.security.tests.KeyStoreTest.class);
        return suite;
    }

    @Override
    public ClassLoader getLoader() {
        return KeyStoreTestRunner.class.getClassLoader();
    }
}
