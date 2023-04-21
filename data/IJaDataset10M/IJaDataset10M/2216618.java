package org.custommonkey.xmlunit;

import javax.xml.transform.OutputKeys;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * JUnit test for SimpleXpathEngine
 */
public class test_SimpleXpathEngine extends TestCase {

    private String[] testAttrNames = { "attrOne", "attrTwo" };

    private String testString = "<test><nodeWithoutAttributes>intellectual property rights </nodeWithoutAttributes>" + "<nodeWithoutAttributes>make us all poorer </nodeWithoutAttributes>" + "<nodeWithAttributes " + testAttrNames[0] + "=\"open source \" " + testAttrNames[1] + "=\"is the answer \">free your code from its chains" + "</nodeWithAttributes></test>";

    private Document testDocument;

    private SimpleXpathEngine simpleXpathEngine = new SimpleXpathEngine();

    public void testGetXPathResultNode() throws Exception {
        Node result = simpleXpathEngine.getXPathResultNode("test", testDocument);
        SimpleSerializer serializer = new SimpleSerializer();
        serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        assertEquals(testString, serializer.serialize(result.getFirstChild()));
    }

    public void testGetMatchingNodesNoMatches() throws Exception {
        NodeList nodeList = simpleXpathEngine.getMatchingNodes("toast", testDocument);
        assertEquals(0, nodeList.getLength());
    }

    public void testGetMatchingNodesMatchRootElement() throws Exception {
        NodeList nodeList = simpleXpathEngine.getMatchingNodes("test", testDocument);
        assertEquals(1, nodeList.getLength());
        assertEquals(Node.ELEMENT_NODE, nodeList.item(0).getNodeType());
    }

    public void testGetMatchingNodesMatchElement() throws Exception {
        NodeList nodeList = simpleXpathEngine.getMatchingNodes("test/nodeWithoutAttributes", testDocument);
        assertEquals(2, nodeList.getLength());
        assertEquals(Node.ELEMENT_NODE, nodeList.item(0).getNodeType());
    }

    public void testGetMatchingNodesMatchText() throws Exception {
        NodeList nodeList = simpleXpathEngine.getMatchingNodes("test//text()", testDocument);
        assertEquals(3, nodeList.getLength());
        assertEquals(Node.TEXT_NODE, nodeList.item(0).getNodeType());
    }

    public void testGetMatchingNodesCheckSubNodes() throws Exception {
        NodeList nodeList = simpleXpathEngine.getMatchingNodes("test/nodeWithAttributes", testDocument);
        assertEquals(1, nodeList.getLength());
        Node aNode;
        aNode = nodeList.item(0);
        assertEquals(Node.ELEMENT_NODE, aNode.getNodeType());
        assertEquals(true, aNode.hasAttributes());
        assertEquals(true, aNode.hasChildNodes());
        NodeList children = aNode.getChildNodes();
        int length = children.getLength();
        assertEquals(1, length);
        for (int i = 0; i < length; ++i) {
            assertEquals(Node.TEXT_NODE, children.item(i).getNodeType());
        }
        NamedNodeMap attributes = aNode.getAttributes();
        int numAttrs = attributes.getLength();
        assertEquals(testAttrNames.length, numAttrs);
        for (int i = 0; i < testAttrNames.length; ++i) {
            Node attrNode = attributes.getNamedItem(testAttrNames[i]);
            assertNotNull(attrNode);
            assertEquals(Node.ATTRIBUTE_NODE, attrNode.getNodeType());
        }
    }

    public void testEvaluate() throws Exception {
        String result = simpleXpathEngine.evaluate("count(test//node())", testDocument);
        assertEquals("3 elements and 3 text nodes", "6", result);
    }

    public void setUp() throws Exception {
        testDocument = XMLUnit.buildControlDocument(testString);
    }

    public test_SimpleXpathEngine(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(test_SimpleXpathEngine.class);
    }
}
