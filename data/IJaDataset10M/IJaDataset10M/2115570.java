package net.sf.cybowmodeller.simplevarexp.converter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import junit.framework.TestCase;
import net.sf.cybowmodeller.simplevarexp.SimpleVariableExpression;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author SHIMAYOSHI Takao
 * @version $Revision: 9 $
 */
public class MathMLEntRefDomConvTest extends TestCase {

    private static final String TEST_STRING = "A&alpha;b[Xa^12_34]_i";

    private static DocumentBuilder documentBuilder;

    private SimpleVariableExpression varexp;

    @Override
    protected void setUp() throws Exception {
        varexp = SimpleVariableExpression.parse(TEST_STRING);
        super.setUp();
    }

    /**
     * Test method for {@link org.dynabios.cell.simplevarexp.converter.MathMLDomConverter#buildMathMLDom(org.dynabios.cell.simplevarexp.SimpleVariableExpression)}.
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     * @throws TransformerFactoryConfigurationError 
     */
    public void testBuildMathMLDom() throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
        final Document document = buildDocument();
        final Element element = SimpleVariableExpression.buildMathMLTree(varexp, document);
        document.appendChild(element);
        final Node result = convertDocument(document);
        dumpDocument(document, "");
        dumpDocument(result, "");
    }

    private Node convertDocument(final Document document) throws TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final Document transdoc;
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        final DOMResult result;
        factory.setNamespaceAware(true);
        factory.setExpandEntityReferences(false);
        transdoc = factory.newDocumentBuilder().newDocument();
        result = new DOMResult(transdoc);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), result);
        return transdoc;
    }

    private Document buildDocument() throws ParserConfigurationException {
        if (documentBuilder == null) {
            initDocumentBuilder();
        }
        return documentBuilder.newDocument();
    }

    private void dumpDocument(final Node node, final String indent) {
        System.out.println();
        System.out.print(indent);
        System.out.print("Type: ");
        System.out.println(node.getNodeType());
        System.out.print(indent);
        System.out.print("Name: ");
        System.out.println(node.getNodeName());
        System.out.print(indent);
        System.out.print("Value: ");
        System.out.println(node.getNodeValue());
        if (node.hasChildNodes()) {
            System.out.print(indent);
            System.out.print("Children: ");
            dumpDocument(node.getFirstChild(), indent + "  ");
        }
        if (node.getNextSibling() != null) {
            dumpDocument(node.getNextSibling(), indent);
        }
    }

    private static void initDocumentBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        documentBuilder = factory.newDocumentBuilder();
    }
}
