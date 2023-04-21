package org.w3c.domts.level2.core;

import java.lang.reflect.Constructor;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestSuite;
import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DOMTestSuite;
import org.w3c.domts.JAXPDOMTestDocumentBuilderFactory;
import org.w3c.domts.JUnitTestSuiteAdapter;

public class TestCrimson extends TestSuite {

    public static TestSuite suite() throws Exception {
        Class testClass = ClassLoader.getSystemClassLoader().loadClass("org.w3c.domts.level2.core.alltests");
        Constructor testConstructor = testClass.getConstructor(new Class[] { DOMTestDocumentBuilderFactory.class });
        DocumentBuilderFactory crimsonFactory = (DocumentBuilderFactory) ClassLoader.getSystemClassLoader().loadClass("org.apache.crimson.jaxp.DocumentBuilderFactoryImpl").newInstance();
        DOMTestDocumentBuilderFactory factory = new JAXPDOMTestDocumentBuilderFactory(crimsonFactory, JAXPDOMTestDocumentBuilderFactory.getConfiguration1());
        Object test = testConstructor.newInstance(new Object[] { factory });
        return new JUnitTestSuiteAdapter((DOMTestSuite) test);
    }
}
