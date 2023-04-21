package exercise2.xml;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ParserWrapperTestNoParameters {

    private static String xmlFile = "<?xml version='1.0'?>" + "<!DOCTYPE poem [ <!ELEMENT poem (line+)> " + "<!ELEMENT line (#PCDATA)><!ATTLIST line num CDATA #IMPLIED> " + "]><poem><line>aaa</line><line num='2'>bbb</line>" + "<line>ccc</line><line num='4'>ddd</line></poem>";

    private static Document document;

    @BeforeClass
    public static void onlyOnce() throws Exception {
        ParserWrapper parserWrapper = ParserWrapper.getParser();
        document = parserWrapper.parseString(xmlFile);
    }

    /**
	 * Document itself is a DOM node
	 */
    @Test
    public void getDocument() {
        Node node = document;
        assertEquals(Node.DOCUMENT_NODE, node.getNodeType());
        assertEquals("#document", node.getNodeName());
        assertEquals(null, node.getTextContent());
    }

    /**
	 * Document's first child is the DOCTYPE declaration
	 */
    @Test
    public void getDoctype() {
        Node node = document.getFirstChild();
        assertEquals(Node.DOCUMENT_TYPE_NODE, node.getNodeType());
        assertEquals("poem", node.getNodeName());
        assertEquals(null, node.getTextContent());
    }

    /**
	 * Document's second child is the root element (poem);
	 * Its exposed text is "aaabbbcccddd" - concatenation 
	 * of the text contents of all children.
	 */
    @Test
    public void getRootNode() {
        Node node = document.getChildNodes().item(1);
        assertEquals(Node.ELEMENT_NODE, node.getNodeType());
        assertEquals("poem", node.getNodeName());
        assertEquals("aaabbbcccddd", node.getTextContent());
    }

    /**
	 * Root has the 2nd child - XML element with name "line" and content "bbb"
	 */
    @Test
    public void getSecondLine() {
        Node node = document.getChildNodes().item(1).getChildNodes().item(1);
        assertEquals(Node.ELEMENT_NODE, node.getNodeType());
        assertEquals("line", node.getNodeName());
        assertEquals("bbb", node.getTextContent());
    }

    /**
	 * Root's 2nd child also has attribute with name "num" and value "2"
	 */
    @Test
    public void getAttribute() {
        Node node = document.getChildNodes().item(1).getChildNodes().item(1).getAttributes().getNamedItem("num");
        assertEquals(Node.ATTRIBUTE_NODE, node.getNodeType());
        assertEquals("num", node.getNodeName());
        assertEquals("2", node.getTextContent());
    }
}
