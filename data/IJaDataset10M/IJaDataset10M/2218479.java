package org.w3c.domts.level3.validation;

import java.lang.reflect.Constructor;
import junit.framework.TestSuite;
import org.w3c.domts.BatikTestDocumentBuilderFactory;
import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DOMTestSuite;
import org.w3c.domts.DocumentBuilderSetting;
import org.w3c.domts.JUnitTestSuiteAdapter;

/**
 * 
 * Runs test suite using Batik SVG.
 * 
 */
public class TestBatik extends TestSuite {

    /**
     * Factory method for suite.
     * 
     * @return suite
     * @throws Exception if Batik is not available or could not be instantiated
     */
    public static TestSuite suite() throws Exception {
        Class testClass = ClassLoader.getSystemClassLoader().loadClass("org.w3c.domts.level3.validation.alltests");
        Constructor testConstructor = testClass.getConstructor(new Class[] { DOMTestDocumentBuilderFactory.class });
        DOMTestDocumentBuilderFactory factory = new BatikTestDocumentBuilderFactory(new DocumentBuilderSetting[0]);
        Object test = testConstructor.newInstance(new Object[] { factory });
        return new JUnitTestSuiteAdapter((DOMTestSuite) test);
    }
}
