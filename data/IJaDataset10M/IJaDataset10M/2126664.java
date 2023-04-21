package net.sourceforge.ondex.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Christian
 */
public class XMLReaderTest extends XMTestBase {

    public XMLReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        small = smallNoWhitespace();
        wide = wide();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void equalsTest() throws ParserConfigurationException {
        Document dom1 = smallNoWhitespace();
        Document dom2 = smallNoWhitespace();
        String result = DomCompare.compareNodes(dom1, dom2);
        assertEquals(DomCompare.NO_DIFFERENCE, result);
    }

    @Test
    public void testReadFile_File() throws ParserConfigurationException, SAXException, IOException {
        File file = new File(RESOURCE_PATH + "smallNoWhiteSpace.xml");
        Document read = XMLReader.readFile(file);
        String result = DomCompare.compareNodes(small, read);
        assertEquals(DomCompare.NO_DIFFERENCE, result);
    }

    @Test
    public void testReadFile_FileNull() throws ParserConfigurationException, SAXException, IOException {
        File file = null;
        try {
            Document read = XMLReader.readFile(file);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testReadFile_FileBadXML() throws ParserConfigurationException, SAXException, IOException {
        File file = new File(RESOURCE_PATH + "HelloWorldBadXML.t2flow");
        try {
            Document read = XMLReader.readFile(file);
            fail("SAXException expected");
        } catch (SAXException ex) {
        }
    }

    @Test
    public void testReadFile_String() throws ParserConfigurationException, SAXException, IOException {
        String file = RESOURCE_PATH + "smallNoWhiteSpace.xml";
        Document read = XMLReader.readFile(file);
        String result = DomCompare.compareNodes(small, read);
        assertEquals(DomCompare.NO_DIFFERENCE, result);
    }

    @Test
    public void testReadFile_StringNull() throws ParserConfigurationException, SAXException, IOException {
        String file = null;
        try {
            Document read = XMLReader.readFile(file);
            fail("IllegalArgumentExceptionn expected");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testGetDirectChildrenByName() throws ParserConfigurationException, SAXException, IOException {
        File file = new File(RESOURCE_PATH + "small2Children.xml");
        Document document = XMLReader.readFile(file);
        String name = "child";
        Node parent = document.getDocumentElement();
        List result = XMLReader.getDirectChildrenByName(parent, name);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetDirectChildrenByWrongName() throws ParserConfigurationException, SAXException, IOException {
        File file = new File(RESOURCE_PATH + "small2Children.xml");
        Document document = XMLReader.readFile(file);
        String name = "wrong";
        Node parent = document.getDocumentElement();
        List result = XMLReader.getDirectChildrenByName(parent, name);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetDirectChildrenTooDeep() throws ParserConfigurationException, SAXException, IOException {
        File file = new File(RESOURCE_PATH + "small2Children.xml");
        String name = "grandChild";
        Node parent = wide.getDocumentElement();
        List result = XMLReader.getDirectChildrenByName(parent, name);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetDirectChildrenByNameNullNode() throws ParserConfigurationException, SAXException, IOException {
        String name = "child";
        try {
            List result = XMLReader.getDirectChildrenByName(null, name);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testGetDirectChildrenByNameNullName() throws ParserConfigurationException, SAXException, IOException {
        File file = new File(RESOURCE_PATH + "small2Children.xml");
        Document document = XMLReader.readFile(file);
        Node parent = document.getDocumentElement();
        try {
            List result = XMLReader.getDirectChildrenByName(parent, null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    public static Document narrow() throws ParserConfigurationException {
        Document doc = newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Element childElement = doc.createElement("child");
        childElement.setTextContent("text");
        root.appendChild(childElement);
        Element anotherchildElement = doc.createElement("anotherchild");
        root.appendChild(anotherchildElement);
        Element grandchildElement = doc.createElement("grandChild");
        grandchildElement.setTextContent("fluff");
        childElement.appendChild(grandchildElement);
        childElement.appendChild(anotherchildElement);
        Element greatGrandChildElement = doc.createElement("GGC");
        greatGrandChildElement.setTextContent("deep text");
        grandchildElement.appendChild(greatGrandChildElement);
        grandchildElement.appendChild(anotherchildElement);
        return doc;
    }

    @Test
    public void testGetTextFromTree() throws SAXException, ParserConfigurationException {
        Document document = narrow();
        String[] tree = { "child", "grandChild", "GGC" };
        String expResult = "deep text";
        String result = XMLReader.getTextFromTree(document, tree);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTextFromTreeTooWide() throws SAXException, ParserConfigurationException {
        String[] tree = { "child", "grandChild", "GGC" };
        try {
            String result = XMLReader.getTextFromTree(wide, tree);
            fail("SAXException expected");
        } catch (SAXException ex) {
        }
    }

    @Test
    public void testGetTextFromTreeNullDoc() throws SAXException, ParserConfigurationException {
        String[] tree = { "child", "grandChild", "GGC" };
        try {
            String result = XMLReader.getTextFromTree(null, tree);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testGetTextFromTreeNullTree() throws SAXException, ParserConfigurationException {
        Document document = narrow();
        String expResult = "deep text";
        try {
            String result = XMLReader.getTextFromTree(document, null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testGetText() throws SAXException {
        Node element = small.getDocumentElement().getFirstChild();
        String expResult = "text";
        String result = XMLReader.getText(element);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTextNoText() throws SAXException {
        Node element = small.createElement("empty");
        String expResult = null;
        String result = XMLReader.getText(element);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTextNull() throws SAXException {
        try {
            String result = XMLReader.getText(null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    private List<String> threeLevelTree() {
        List<String> tree = new ArrayList<String>();
        tree.add("child");
        tree.add("grandChild");
        tree.add("GGC");
        return tree;
    }

    private List twoResults() {
        List expResult = new ArrayList<String>();
        expResult.add("deep text");
        expResult.add("more deep text");
        return expResult;
    }

    @Test
    public void testGetTextsFromTree_List_List() throws SAXException, ParserConfigurationException {
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(wide.getDocumentElement());
        List expResult = twoResults();
        List<String> tree = threeLevelTree();
        List result = XMLReader.getTextsFromTree(nodeList, tree);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTextsFromTree_List_ListNullList() throws SAXException, ParserConfigurationException {
        List<Node> nodeList = null;
        List<String> tree = threeLevelTree();
        try {
            List result = XMLReader.getTextsFromTree(nodeList, tree);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testGetTextsFromTree_List_ListNullTree() throws SAXException, ParserConfigurationException {
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(wide.getDocumentElement());
        try {
            List result = XMLReader.getTextsFromTree(nodeList, null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testGetTextsFromTree_Document_List() throws SAXException {
        List<String> tree = threeLevelTree();
        List expResult = twoResults();
        List result = XMLReader.getTextsFromTree(wide, tree);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetTextsFromTree_Document_ListNullDocument() throws SAXException {
        List<String> tree = threeLevelTree();
        Document nul = null;
        try {
            List result = XMLReader.getTextsFromTree(nul, tree);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    @Test
    public void testGetTextsFromTree_Document_ListNullList() throws SAXException {
        try {
            List result = XMLReader.getTextsFromTree(wide, null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }
}
