package org.apache.commons.logging.config;

import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.PathableClassLoader;
import org.apache.commons.logging.PathableTestSuite;

/**
 * Tests that verify that the process of configuring logging on startup
 * works correctly by selecting the file with the highest priority.
 * <p>
 * This test sets up a classpath where:
 * <ul>
 * <li> first file found has priority=20
 * <li> second file found has priority=10
 * </ul>
 * The result should be that the first file is used.
 */
public class FirstPriorityConfigTestCase extends TestCase {

    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() throws Exception {
        Class thisClass = FirstPriorityConfigTestCase.class;
        PathableClassLoader dummy = new PathableClassLoader(null);
        dummy.useExplicitLoader("junit.", Test.class.getClassLoader());
        dummy.addLogicalLib("testclasses");
        dummy.addLogicalLib("commons-logging");
        String thisClassPath = thisClass.getName().replace('.', '/') + ".class";
        URL baseUrl = dummy.findResource(thisClassPath);
        PathableClassLoader containerLoader = new PathableClassLoader(null);
        containerLoader.useExplicitLoader("junit.", Test.class.getClassLoader());
        containerLoader.addLogicalLib("commons-logging");
        PathableClassLoader webappLoader = new PathableClassLoader(containerLoader);
        webappLoader.addLogicalLib("testclasses");
        URL pri20URL = new URL(baseUrl, "priority20/");
        webappLoader.addURL(pri20URL);
        URL pri10URL = new URL(baseUrl, "priority10/");
        webappLoader.addURL(pri10URL);
        Class testClass = webappLoader.loadClass(thisClass.getName());
        return new PathableTestSuite(testClass, webappLoader);
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
     * Verify that the config file being used is the one containing
     * the desired configId value.
     */
    public void testPriority() throws Exception {
        LogFactory instance = LogFactory.getFactory();
        ClassLoader thisClassLoader = this.getClass().getClassLoader();
        ClassLoader lfClassLoader = instance.getClass().getClassLoader();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        assertEquals(thisClassLoader, contextClassLoader);
        assertEquals(lfClassLoader, thisClassLoader.getParent());
        assertEquals(PathableClassLoader.class.getName(), lfClassLoader.getClass().getName());
        String id = (String) instance.getAttribute("configId");
        assertEquals("Correct config file loaded", "priority20", id);
    }
}
