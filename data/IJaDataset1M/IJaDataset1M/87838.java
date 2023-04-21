package uk.org.beton.css2.selector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * @author Rick Beton
 * @version $Id: AttributeSelectorTest.java 5 2006-07-16 14:49:45Z rickbeton $
 * @see http://www.w3.org/TR/CSS21/selector.html
 */
public final class AttributeSelectorTest extends TestCase {

    private static final Class CLASS = AttributeSelectorTest.class;

    private static Logger LOG = Logger.getLogger(CLASS);

    /**
     * Construct a test with a specified name
     * 
     * @param name the name.
     */
    public AttributeSelectorTest(String name) throws Exception {
        super(name);
    }

    public void testDefined() throws Exception {
        LOG.info(getName());
        final AttributeSelector as1 = new AttributeSelector(" fred ");
        assertEquals(AttributeSelector.DEFINED, as1.type);
        assertEquals("fred", as1.ident);
        assertNull(as1.value);
        assertEquals(0, as1.words.size());
        final Element e1 = new Element("p");
        assertFalse(as1.matches(e1));
        e1.setAttribute("fred", "bloggs");
        assertTrue(as1.matches(e1));
    }

    public void testExact() throws Exception {
        LOG.info(getName());
        final AttributeSelector as1 = new AttributeSelector(" title = Hello ");
        assertEquals(AttributeSelector.EXACT, as1.type);
        assertEquals("title", as1.ident);
        assertEquals("Hello", as1.value);
        assertEquals(0, as1.words.size());
        final Element e1 = new Element("p");
        assertFalse(as1.matches(e1));
        e1.setAttribute("title", "World");
        assertFalse(as1.matches(e1));
        e1.setAttribute("title", "Hello");
        assertTrue(as1.matches(e1));
    }

    public void testIncludes() throws Exception {
        LOG.info(getName());
        final AttributeSelector as1 = new AttributeSelector(" title ~= \"Hello World Today\" ");
        assertEquals(AttributeSelector.INCLUDES, as1.type);
        assertEquals("title", as1.ident);
        assertEquals("Hello World Today", as1.value);
        assertEquals(3, as1.words.size());
        final Element e1 = new Element("p");
        assertFalse(as1.matches(e1));
        e1.setAttribute("title", "Hello");
        assertTrue(as1.matches(e1));
        e1.setAttribute("title", "World");
        assertTrue(as1.matches(e1));
        e1.setAttribute("title", "Today");
        assertTrue(as1.matches(e1));
        e1.setAttribute("title", "Bother");
        assertFalse(as1.matches(e1));
    }

    public void testStarts() throws Exception {
        LOG.info(getName());
        final AttributeSelector as1 = new AttributeSelector(" lang |= en ");
        final AttributeSelector as2 = new AttributeSelector(" lang |= en-gb ");
        assertEquals(AttributeSelector.STARTS, as1.type);
        assertEquals(AttributeSelector.STARTS, as2.type);
        assertEquals("lang", as1.ident);
        assertEquals("lang", as2.ident);
        assertEquals("en", as1.value);
        assertEquals("en-gb", as2.value);
        assertEquals(0, as1.words.size());
        assertEquals(0, as2.words.size());
        final Element e1 = new Element("p");
        assertFalse(as1.matches(e1));
        assertFalse(as2.matches(e1));
        e1.setAttribute("lang", "en-gb");
        assertTrue(as1.matches(e1));
        assertTrue(as2.matches(e1));
        e1.setAttribute("lang", "en");
        assertTrue(as1.matches(e1));
        assertFalse(as2.matches(e1));
        e1.setAttribute("lang", "fr-ca");
        assertFalse(as1.matches(e1));
        assertFalse(as2.matches(e1));
    }

    /**
     * Provides the main JUnit entry point.
     * 
     * @return a new TestSuite consisting of all the tests in this class.
     * @throws Exception if initialisation failed.
     */
    public static Test suite() throws Exception {
        if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
            BasicConfigurator.configure();
        }
        return new TestSuite(CLASS);
    }

    /**
     * Allows testing from the command line.
     * 
     * @param args command line arguments
     * @throws Exception on test failure
     */
    public static void main(String args[]) throws Exception {
        junit.textui.TestRunner.run(suite());
    }
}
