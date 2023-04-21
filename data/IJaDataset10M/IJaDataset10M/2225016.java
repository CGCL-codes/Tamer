package org.apache.commons.logging.tccl.logfactory;

import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.PathableClassLoader;
import org.apache.commons.logging.PathableTestSuite;

/**
 * Verify that by default a custom LogFactoryImpl is loaded from the
 * tccl classloader.
 */
public class TcclEnabledTestCase extends TestCase {

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() throws Exception {
        Class thisClass = TcclEnabledTestCase.class;
        PathableClassLoader dummy = new PathableClassLoader(null);
        dummy.useExplicitLoader("junit.", Test.class.getClassLoader());
        dummy.addLogicalLib("testclasses");
        dummy.addLogicalLib("commons-logging");
        String thisClassPath = thisClass.getName().replace('.', '/') + ".class";
        URL baseUrl = dummy.findResource(thisClassPath);
        PathableClassLoader emptyLoader = new PathableClassLoader(null);
        PathableClassLoader parentLoader = new PathableClassLoader(null);
        parentLoader.useExplicitLoader("junit.", Test.class.getClassLoader());
        parentLoader.addLogicalLib("commons-logging");
        parentLoader.addLogicalLib("testclasses");
        parentLoader.useExplicitLoader("org.apache.commons.logging.tccl.custom.", emptyLoader);
        URL propsEnableUrl = new URL(baseUrl, "props_enable_tccl/");
        parentLoader.addURL(propsEnableUrl);
        PathableClassLoader tcclLoader = new PathableClassLoader(parentLoader);
        tcclLoader.addLogicalLib("testclasses");
        Class testClass = parentLoader.loadClass(thisClass.getName());
        return new PathableTestSuite(testClass, tcclLoader);
    }

    /**
     * Set up instance variables required by this test case.
     */
    public void setUp() throws Exception {
        LogFactory.releaseAll();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    public void tearDown() {
        LogFactory.releaseAll();
    }

    /**
     * Verify that MyLogFactoryImpl is only loadable via the tccl.
     */
    public void testLoader() throws Exception {
        ClassLoader thisClassLoader = this.getClass().getClassLoader();
        ClassLoader tcclLoader = Thread.currentThread().getContextClassLoader();
        assertNotSame("tccl not same as test classloader", thisClassLoader, tcclLoader);
        try {
            Class clazz = thisClassLoader.loadClass("org.apache.commons.logging.tccl.custom.MyLogFactoryImpl");
            fail("Unexpectedly able to load MyLogFactoryImpl via test class classloader");
            assertNotNull(clazz);
        } catch (ClassNotFoundException ex) {
        }
        try {
            Class clazz = tcclLoader.loadClass("org.apache.commons.logging.tccl.custom.MyLogFactoryImpl");
            assertNotNull(clazz);
        } catch (ClassNotFoundException ex) {
            fail("Unexpectedly unable to load MyLogFactoryImpl via tccl classloader");
        }
    }

    /**
     * Verify that the custom LogFactory implementation which is only accessable
     * via the TCCL has successfully been loaded as specified in the config file.
     * This proves that the TCCL was used to load that class.
     */
    public void testTcclLoading() throws Exception {
        LogFactory instance = LogFactory.getFactory();
        assertEquals("Correct LogFactory loaded", "org.apache.commons.logging.tccl.custom.MyLogFactoryImpl", instance.getClass().getName());
    }
}
