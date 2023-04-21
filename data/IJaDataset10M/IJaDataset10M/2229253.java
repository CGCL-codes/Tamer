package org.smartcrawler.extractor;

import junit.framework.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.digester.Digester;
import org.smartcrawler.extractor.pattern.AbstractPattern;
import org.smartcrawler.extractor.pattern.ConcretePattern;

/**
 *
 *
 * @author <a href="mailto:pozzad@alice.it">Davide Pozza</a>
 * @version <tt>$Revision: 1.3 $</tt>
 */
public class PatternProviderTest extends TestCase {

    /**
     * 
     * @param testName 
     */
    public PatternProviderTest(String testName) {
        super(testName);
    }

    /**
     * 
     * @throws java.lang.Exception 
     */
    protected void setUp() throws Exception {
    }

    /**
     * 
     * @throws java.lang.Exception 
     */
    protected void tearDown() throws Exception {
    }

    /**
     * 
     * @return 
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(PatternProviderTest.class);
        return suite;
    }

    /**
     * Test of getPatterns method, of class org.smartcrawler.extractor.PatternProvider.
     */
    public void testGetPatterns() {
        System.out.println("testGetPatterns");
        AbstractPattern expected = new ConcretePattern();
        expected.setStringPattern("(<\\s*[A]\\s[^>]*[\\n\\s]*)(href\\s*=\\s*([^>|\\s]*))[^>]*>");
        expected.setGroup("3");
        AbstractPattern actual = (AbstractPattern) PatternProvider.instance().getPatterns()[0];
        System.out.println("exp = " + expected);
        System.out.println("act = " + actual);
        assertEquals("PatternProvider.getPatterns()", expected, actual);
    }
}
