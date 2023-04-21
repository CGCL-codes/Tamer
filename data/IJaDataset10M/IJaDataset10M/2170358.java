package org.w3c.domts.level3.xpath;

import java.lang.reflect.Constructor;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestSuite;
import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.DOMTestSuite;
import org.w3c.domts.JUnitTestSuiteAdapter;
import org.w3c.domts.XalanDOMTestDocumentBuilderFactory;

public class TestXalanAltConfig extends TestSuite {

    public static TestSuite suite() throws Exception {
        Class testClass = ClassLoader.getSystemClassLoader().loadClass("org.w3c.domts.level3.xpath.alltests");
        Constructor testConstructor = testClass.getConstructor(new Class[] { DOMTestDocumentBuilderFactory.class });
        DocumentBuilderFactory jaxpFactory = DocumentBuilderFactory.newInstance();
        DOMTestDocumentBuilderFactory factory = new XalanDOMTestDocumentBuilderFactory(jaxpFactory, XalanDOMTestDocumentBuilderFactory.getConfiguration2());
        Object test = testConstructor.newInstance(new Object[] { factory });
        return new JUnitTestSuiteAdapter((DOMTestSuite) test);
    }
}
