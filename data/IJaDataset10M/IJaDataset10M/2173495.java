package de.uni_leipzig.lots.server.persist.hibernate.type;

import junit.framework.TestCase;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.util.NodeComparator;

/**
 * @author Alexander Kiel
 * @version $Id: WhiteSpacePreservingTextTypeTest.java,v 1.1 2007/07/15 21:43:10 mai99bxd Exp $
 */
public class WhiteSpacePreservingTextTypeTest extends TestCase {

    public void testNormal() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("normal");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "normal", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithNewline() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("first line\nsecond line");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "first line\nsecond line", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithInnerCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("a]]");
        expected.addCDATA(">b");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "a]]>b", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithStartCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("]]");
        expected.addCDATA(">b");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "]]>b", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithEndCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("a]]");
        expected.addCDATA(">");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "a]]>", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithTwoInnerCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("a]]");
        expected.addCDATA(">b]]");
        expected.addCDATA(">c");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "a]]>b]]>c", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithOneStartAndOneInnerCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("]]");
        expected.addCDATA(">b]]");
        expected.addCDATA(">c");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "]]>b]]>c", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithOneInnerAndOneEndCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("a]]");
        expected.addCDATA(">b]]");
        expected.addCDATA(">");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "a]]>b]]>", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithOnlyOneCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("]]");
        expected.addCDATA(">");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "]]>", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithOnlyTwoCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("]]");
        expected.addCDATA(">]]");
        expected.addCDATA(">");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "]]>]]>", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }

    public void testWithTwoOuterCDATA() {
        DocumentFactory factory = DocumentFactory.getInstance();
        Element expected = factory.createElement("test");
        expected.addCDATA("]]");
        expected.addCDATA(">a]]");
        expected.addCDATA(">");
        Element actual = factory.createElement("test");
        WhiteSpacePreservingTextType type = new WhiteSpacePreservingTextType();
        type.setToXMLNode(actual, "]]>a]]>", null);
        NodeComparator comparator = new NodeComparator();
        assertEquals(0, comparator.compareContent(expected, actual));
    }
}
