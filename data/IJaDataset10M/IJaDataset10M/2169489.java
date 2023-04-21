package org.uk2005.util.test;

import junit.framework.*;
import org.w3c.dom.*;
import org.uk2005.util.XMLUtil;

/**
 * @author	<a href="mailto:niklas@saers.com">Niklas Saers</a>
 * @version	$Id: TestXMLUtil.java,v 1.2 2002/10/18 09:29:35 niklasjs Exp $
 */
public class TestXMLUtil extends TestCase {

    /**
	 * Our test-subject
	 */
    String doc = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" + "<!-- This is a test document for doing testing -->\n" + "<familly id=\"41842\">\n" + "  <surname>Simpsons</surname>\n" + "  <member id=\"1\" baby=\"false\">\n" + "    <name>Homer</name>\n" + "  </member>\n" + "  <member id=\"2\" baby=\"false\">\n" + "    <name>Marge</name>\n" + "  </member>\n" + "  <member id=\"3\" baby=\"false\">\n" + "    <name>Bartonomy</name>\n" + "  </member>\n" + "  <member id=\"4\" baby=\"false\">\n" + "    <name>Lisa</name>\n" + "  </member>\n" + "  <member id=\"5\" baby=\"true\">\n" + "    <name>Margareth</name>\n" + "  </member>\n" + "</familly>";

    /**
	 * Default constructor.
	 */
    public TestXMLUtil() {
        super("TestXMLUtil");
    }

    /**
	 * Constructs a test case with the specified name.
	 *
	 * @param	name the name of the test
	 */
    public TestXMLUtil(String name) {
        super(name);
    }

    public void testGetTextContents() {
        Node node = XMLUtil.loadString(doc);
        String s = XMLUtil.getTextContents("/familly/surname", node);
        assertTrue(s.equals("Simpsons"));
        s = XMLUtil.getTextContents("/familly/babyfactor", node);
        assertTrue(s.equals(""));
    }

    public void testGetIntContents() {
        Node node = XMLUtil.loadString(doc);
        int i = XMLUtil.getIntContents("/familly/@id", node);
        assertTrue(i == 41842);
        i = XMLUtil.getIntContents("/familly/babyfactor", node);
        assertTrue(i == 0);
    }

    public void testGetBoolContents() {
        Node node = XMLUtil.loadString(doc);
        boolean b = XMLUtil.getBoolContents("/familly/member[@id=5]/@baby", node);
        assertTrue(b);
        b = XMLUtil.getBoolContents("/familly/member[@id=4]/@baby", node);
        assertFalse(b);
        b = XMLUtil.getBoolContents("/familly/member", node);
        assertFalse(b);
    }

    public void testSelectNodeList() {
        Node node = XMLUtil.loadString(doc);
        NodeList list = XMLUtil.selectNodeList(node, "/familly/member");
        assertTrue(list.getLength() == 5);
        list = XMLUtil.selectNodeList(node, "/familly/member[@baby='false']");
        assertTrue(list.getLength() == 4);
        list = XMLUtil.selectNodeList(node, "/familly/babyfactor");
        assertTrue(list.getLength() == 0);
    }

    public void testBackTrace() {
        Node node = XMLUtil.loadString(doc);
        NodeList list = XMLUtil.selectNodeList(node, "/familly/member");
        Node item = list.item(0);
        assertFalse(XMLUtil.backTrace(item).equals(""));
    }

    /**
	 * Tests whether loadString works and fails correctly
	 */
    public void testLoadString() {
        Node node = XMLUtil.loadString(doc);
        assertNotNull(node);
        try {
            node = XMLUtil.loadString(doc + "</familly>");
            fail("This should have failed, so this point should never be reached");
        } catch (Error e) {
        }
    }
}
