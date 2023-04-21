package org.custommonkey.xmlunit;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import junit.framework.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Collection of static methods so that XML assertion facilities are available
 * in any class, not just test suites. Thanks to Andrew McCormick and others for
 * suggesting this refactoring.<br/>
 * Available assertion methods are:
 * <ul>
 * <li><strong><code>assertXMLEqual</code></strong><br/>
 *  assert that two pieces of XML markup are <i>similar</i></li>
 * <li><strong><code>assertXMLNotEqual</code></strong><br/>
 *  assert that two pieces of XML markup are <i>different</i></li>
 * <li><strong><code>assertXMLIdentical</code></strong><br/>
 *  assert that two pieces of XML markup are <i>identical</i>. In most cases
 *  this assertion is too strong and <code>assertXMLEqual</code> is sufficient</li>
 * <li><strong><code>assertXpathExists</code></strong><br/> 
 * assert that an XPath expression matches at least one node</li>
 * <li><strong><code>assertXpathNotExists</code></strong><br/> 
 * assert that an XPath expression does not match any nodes</li>
 * <li><strong><code>assertXpathsEqual</code></strong><br/>
 *  assert that the nodes obtained by executing two Xpaths
 *  are <i>similar</i></li>
 * <li><strong><code>assertXpathsNotEqual</code></strong><br/>
 *  assert that the nodes obtained by executing two Xpaths
 *  are <i>different</i></li>
 * <li><strong><code>assertXpathValuesEqual</code></strong><br/>
 *  assert that the flattened String obtained by executing two Xpaths
 *  are <i>similar</i></li>
 * <li><strong><code>assertXpathValuesNotEqual</code></strong><br/>
 *  assert that the flattened String obtained by executing two Xpaths
 *  are <i>different</i></li>
 * <li><strong><code>assertXpathEvaluatesTo</code></strong><br/>
 *  assert that the flattened String obtained by executing an Xpath
 *  is a particular value</li>
 * <li><strong><code>assertXMLValid</code></strong><br/>
 *  assert that a piece of XML markup is valid with respect to a DTD: either
 *  by using the markup's own DTD or a different DTD</li>
 * <li><strong><code>assertNodeTestPasses</code></strong><br/>
 *  assert that a piece of XML markup passes a {@link NodeTest NodeTest}</li>
 * </ul>
 * All underlying similarity and difference testing is done using 
 * {@link Diff Diff} instances which can be instantiated and evaluated
 * independently of this class.
 * @see Diff#similar()
 * @see Diff#identical()
 * <br />Examples and more at <a href="http://xmlunit.sourceforge.net"/>xmlunit.sourceforge.net</a>
 */
public class XMLAssert extends Assert implements XSLTConstants {

    protected XMLAssert() {
        super();
    }

    /**
     * Assert that the result of an XML comparison is or is not similar.
     * @param diff the result of an XML comparison
     * @param assertion true if asserting that result is similar
     */
    public static void assertXMLEqual(Diff diff, boolean assertion) {
        if (assertion != diff.similar()) {
            fail(diff.toString());
        }
    }

    /**
     * Assert that the result of an XML comparison is or is not similar.
     * @param msg additional message to display if assertion fails
     * @param diff the result of an XML comparison
     * @param assertion true if asserting that result is similar
     */
    public static void assertXMLEqual(String msg, Diff diff, boolean assertion) {
        if (assertion != diff.similar()) {
            fail(msg + ", " + diff.toString());
        }
    }

    /**
     * Assert that the result of an XML comparison is or is not identical
     * @param diff the result of an XML comparison
     * @param assertion true if asserting that result is identical
     */
    public static void assertXMLIdentical(Diff diff, boolean assertion) {
        if (assertion != diff.identical()) {
            fail(diff.toString());
        }
    }

    /**
     * Assert that the result of an XML comparison is or is not identical
     * @param diff the result of an XML comparison
     * @param assertion true if asserting that result is identical
     * @param msg additional message to display if assertion fails
     * @deprecated Use XMLTestCase#assertXMLIdentical(String, Diff, boolean) instead
     */
    public static void assertXMLIdentical(Diff diff, boolean assertion, String msg) {
        assertXMLIdentical(msg, diff, assertion);
    }

    /**
     * Assert that the result of an XML comparison is or is not identical
     * @param msg Message to display if assertion fails
     * @param diff the result of an XML comparison
     * @param assertion true if asserting that result is identical
     */
    public static void assertXMLIdentical(String msg, Diff diff, boolean assertion) {
        if (assertion != diff.identical()) {
            fail(msg + ", " + diff.toString());
        }
    }

    /**
     * Assert that two XML documents are similar
     * @param control XML to be compared against
     * @param test XML to be tested
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void assertXMLEqual(String control, String test) throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(control, test);
        assertXMLEqual(diff, true);
    }

    /**
     * Assert that two XML documents are similar
     * @param control XML to be compared against
     * @param test XML to be tested
     */
    public static void assertXMLEqual(Document control, Document test) {
        Diff diff = new Diff(control, test);
        assertXMLEqual(diff, true);
    }

    /**
     * Assert that two XML documents are similar
     * @param control XML to be compared against
     * @param test XML to be tested
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void assertXMLEqual(Reader control, Reader test) throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(control, test);
        assertXMLEqual(diff, true);
    }

    /**
     * Assert that two XML documents are similar
     * @param err Message to be displayed on assertion failure
     * @param control XML to be compared against
     * @param test XML to be tested
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void assertXMLEqual(String err, String control, String test) throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(control, test);
        assertXMLEqual(err, diff, true);
    }

    /**
     * Assert that two XML documents are similar
     * @param err Message to be displayed on assertion failure
     * @param control XML to be compared against
     * @param test XML to be tested
     */
    public static void assertXMLEqual(String err, Document control, Document test) {
        Diff diff = new Diff(control, test);
        assertXMLEqual(err, diff, true);
    }

    /**
     * Assert that two XML documents are similar
     * @param err Message to be displayed on assertion failure
     * @param control XML to be compared against
     * @param test XML to be tested
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void assertXMLEqual(String err, Reader control, Reader test) throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(control, test);
        assertXMLEqual(err, diff, true);
    }

    /**
     * Assert that two XML documents are NOT similar
     * @param control XML to be compared against
     * @param test XML to be tested
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void assertXMLNotEqual(String control, String test) throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(control, test);
        assertXMLEqual(diff, false);
    }

    /**
     * Assert that two XML documents are NOT similar
     * @param err Message to be displayed on assertion failure
     * @param control XML to be compared against
     * @param test XML to be tested
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void assertXMLNotEqual(String err, String control, String test) throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(control, test);
        assertXMLEqual(err, diff, false);
    }

    /**
     * Assert that two XML documents are NOT similar
     * @param control XML to be compared against
     * @param test XML to be tested
     */
    public static void assertXMLNotEqual(Document control, Document test) {
        Diff diff = new Diff(control, test);
        assertXMLEqual(diff, false);
    }

    /**
     * Assert that two XML documents are NOT similar
     * @param err Message to be displayed on assertion failure
     * @param control XML to be compared against
     * @param test XML to be tested
     */
    public static void assertXMLNotEqual(String err, Document control, Document test) {
        Diff diff = new Diff(control, test);
        assertXMLEqual(err, diff, false);
    }

    /**
     * Assert that two XML documents are NOT similar
     * @param control XML to be compared against
     * @param test XML to be tested
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void assertXMLNotEqual(Reader control, Reader test) throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(control, test);
        assertXMLEqual(diff, false);
    }

    /**
     * Assert that two XML documents are NOT similar
     * @param err Message to be displayed on assertion failure
     * @param control XML to be compared against
     * @param test XML to be tested
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public static void assertXMLNotEqual(String err, Reader control, Reader test) throws SAXException, IOException, ParserConfigurationException {
        Diff diff = new Diff(control, test);
        assertXMLEqual(err, diff, false);
    }

    /**
     * Assert that the node lists of two Xpaths in the same document are equal
     * @param xpathOne
     * @param xpathTwo
     * @param document
     * @throws TransformerException
     * @see SimpleXpathEngine
     */
    public static void assertXpathsEqual(String controlXpath, String testXpath, Document document) throws TransformerException {
        assertXpathsEqual(controlXpath, document, testXpath, document);
    }

    /**
     * Assert that the node lists of two Xpaths in the same XML string are
     * equal
     * @param xpathOne
     * @param xpathTwo
     * @param inXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void assertXpathsEqual(String controlXpath, String testXpath, String inXMLString) throws SAXException, ParserConfigurationException, TransformerException, IOException {
        assertXpathsEqual(controlXpath, testXpath, XMLUnit.buildControlDocument(inXMLString));
    }

    /**
     * Assert that the node lists of two Xpaths in two XML strings are equal
     * @param xpathOne
     * @param inControlXMLString
     * @param xpathTwo
     * @param inTestXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void assertXpathsEqual(String controlXpath, String inControlXMLString, String testXpath, String inTestXMLString) throws SAXException, ParserConfigurationException, IOException, TransformerException {
        assertXpathsEqual(controlXpath, XMLUnit.buildControlDocument(inControlXMLString), testXpath, XMLUnit.buildTestDocument(inTestXMLString));
    }

    /**
     * Assert that the node lists of two Xpaths in two documents are equal
     * @param xpathOne
     * @param xpathTwo
     * @param document
     * @see SimpleXpathEngine
     * @throws TransformerException
     */
    public static void assertXpathsEqual(String controlXpath, Document controlDocument, String testXpath, Document testDocument) throws TransformerException {
        SimpleXpathEngine xpath = new SimpleXpathEngine();
        Diff diff = new Diff(xpath.getXPathResultAsDocument(controlXpath, controlDocument), xpath.getXPathResultAsDocument(testXpath, testDocument));
        assertXMLEqual(diff, true);
    }

    /**
     * Assert that the node lists of two Xpaths in the same document are NOT equal
     * @param xpathOne
     * @param xpathTwo
     * @param document
     * @throws TransformerException
     * @see SimpleXpathEngine
     */
    public static void assertXpathsNotEqual(String controlXpath, String testXpath, Document document) throws TransformerException {
        assertXpathsNotEqual(controlXpath, document, testXpath, document);
    }

    /**
     * Assert that the node lists of two Xpaths in the same XML string are NOT
     * equal
     * @param xpathOne
     * @param xpathTwo
     * @param inXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void assertXpathsNotEqual(String controlXpath, String testXpath, String inXMLString) throws SAXException, ParserConfigurationException, TransformerException, IOException {
        assertXpathsNotEqual(controlXpath, testXpath, XMLUnit.buildControlDocument(inXMLString));
    }

    /**
     * Assert that the node lists of two Xpaths in two XML strings are NOT equal
     * @param xpathOne
     * @param inControlXMLString
     * @param xpathTwo
     * @param inTestXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void assertXpathsNotEqual(String controlXpath, String inControlXMLString, String testXpath, String inTestXMLString) throws SAXException, ParserConfigurationException, IOException, TransformerException {
        assertXpathsNotEqual(controlXpath, XMLUnit.buildControlDocument(inControlXMLString), testXpath, XMLUnit.buildTestDocument(inTestXMLString));
    }

    /**
     * Assert that the node lists of two Xpaths in two documents are NOT equal
     * @param xpathOne
     * @param xpathTwo
     * @param document
     * @see SimpleXpathEngine
     * @throws TransformerException
     */
    public static void assertXpathsNotEqual(String controlXpath, Document controlDocument, String testXpath, Document testDocument) throws TransformerException {
        SimpleXpathEngine xpath = new SimpleXpathEngine();
        Diff diff = new Diff(xpath.getXPathResultAsDocument(controlXpath, controlDocument), xpath.getXPathResultAsDocument(testXpath, testDocument));
        assertXMLEqual(diff, false);
    }

    /**
     * Assert that the evaluation of two Xpaths in the same document are equal
     * @param xpathOne
     * @param xpathTwo
     * @param document
     * @throws TransformerException
     * @throws TransformerConfigurationException
     * @see SimpleXpathEngine
     */
    public static void assertXpathValuesEqual(String controlXpath, String testXpath, Document document) throws TransformerException, TransformerConfigurationException {
        assertXpathValuesEqual(controlXpath, document, testXpath, document);
    }

    /**
     * Assert that the evaluation of two Xpaths in the same XML string are
     *  equal
     * @param xpathOne
     * @param xpathTwo
     * @param inXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public static void assertXpathValuesEqual(String controlXpath, String testXpath, String inXMLString) throws SAXException, ParserConfigurationException, IOException, TransformerException, TransformerConfigurationException {
        assertXpathValuesEqual(controlXpath, testXpath, XMLUnit.buildControlDocument(inXMLString));
    }

    /**
     * Assert that the evaluation of two Xpaths in two XML strings are equal
     * @param xpathOne
     * @param inControlXMLString
     * @param xpathTwo
     * @param inTestXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public static void assertXpathValuesEqual(String controlXpath, String inControlXMLString, String testXpath, String inTestXMLString) throws SAXException, ParserConfigurationException, IOException, TransformerException, TransformerConfigurationException {
        assertXpathValuesEqual(controlXpath, XMLUnit.buildControlDocument(inControlXMLString), testXpath, XMLUnit.buildTestDocument(inTestXMLString));
    }

    /**
     * Assert that the evaluation of two Xpaths in two documents are equal
     * @param xpathOne
     * @param xpathTwo
     * @param document
     * @see SimpleXpathEngine
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public static void assertXpathValuesEqual(String controlXpath, Document controlDocument, String testXpath, Document testDocument) throws TransformerException, TransformerConfigurationException {
        SimpleXpathEngine xpath = new SimpleXpathEngine();
        assertEquals(xpath.evaluate(controlXpath, controlDocument), xpath.evaluate(testXpath, testDocument));
    }

    /**
     * Assert that the evaluation of two Xpaths in the same XML string are
     * NOT equal
     * @param xpathOne
     * @param xpathTwo
     * @param inXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public static void assertXpathValuesNotEqual(String controlXpath, String testXpath, String inXMLString) throws SAXException, ParserConfigurationException, IOException, TransformerException, TransformerConfigurationException {
        assertXpathValuesNotEqual(controlXpath, testXpath, XMLUnit.buildControlDocument(inXMLString));
    }

    /**
     * Assert that the evaluation of two Xpaths in the same document are
     * NOT equal
     * @param xpathOne
     * @param xpathTwo
     * @param document
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public static void assertXpathValuesNotEqual(String controlXpath, String testXpath, Document document) throws TransformerException, TransformerConfigurationException {
        assertXpathValuesNotEqual(controlXpath, document, testXpath, document);
    }

    /**
     * Assert that the evaluation of two Xpaths in two XML strings are
     * NOT equal
     * @param xpathOne
     * @param inControlXMLString
     * @param xpathTwo
     * @param inTestXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public static void assertXpathValuesNotEqual(String controlXpath, String inControlXMLString, String testXpath, String inTestXMLString) throws SAXException, ParserConfigurationException, IOException, TransformerException, TransformerConfigurationException {
        assertXpathValuesNotEqual(controlXpath, XMLUnit.buildControlDocument(inControlXMLString), testXpath, XMLUnit.buildTestDocument(inTestXMLString));
    }

    /**
     * Assert that the evaluation of two Xpaths in two documents are
     * NOT equal
     * @param xpathOne
     * @param xpathTwo
     * @param document
     * @throws TransformerException
     * @throws TransformerConfigurationException
     */
    public static void assertXpathValuesNotEqual(String controlXpath, Document controlDocument, String testXpath, Document testDocument) throws TransformerException, TransformerConfigurationException {
        SimpleXpathEngine xpath = new SimpleXpathEngine();
        String control = xpath.evaluate(controlXpath, controlDocument);
        String test = xpath.evaluate(testXpath, testDocument);
        if (control != null) {
            if (control.equals(test)) {
                fail("Expected test value NOT to be equal to control but both were " + test);
            }
        } else if (test != null) {
            fail("control xPath evaluated to empty node set, " + "but test xPath evaluated to " + test);
        }
    }

    /**
     * Assert the value of an Xpath expression in an XML String
     * @param expectedValue
     * @param xpathExpression
     * @param inXMLString
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws TransformerConfigurationException
     * @see SimpleXpathEngine which provides the underlying evaluation mechanism
     */
    public static void assertXpathEvaluatesTo(String expectedValue, String xpathExpression, String inXMLString) throws SAXException, IOException, ParserConfigurationException, TransformerException, TransformerConfigurationException {
        Document document = XMLUnit.buildControlDocument(inXMLString);
        assertXpathEvaluatesTo(expectedValue, xpathExpression, document);
    }

    /**
     * Assert the value of an Xpath expression in an DOM Document
     * @param expectedValue
     * @param xpathExpression
     * @param inDocument
     * @throws TransformerException
     * @throws TransformerConfigurationException
     * @see SimpleXpathEngine which provides the underlying evaluation mechanism
     */
    public static void assertXpathEvaluatesTo(String expectedValue, String xpathExpression, Document inDocument) throws TransformerException {
        SimpleXpathEngine simpleXpathEngine = new SimpleXpathEngine();
        assertEquals(expectedValue, simpleXpathEngine.evaluate(xpathExpression, inDocument));
    }

    /**
     * Assert that a specific XPath exists in some given XML
     * @param inXpathExpression
     * @param inXMLString
     * @see SimpleXpathEngine which provides the underlying evaluation mechanism
     */
    public static void assertXpathExists(String xPathExpression, String inXMLString) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        Document inDocument = XMLUnit.buildControlDocument(inXMLString);
        assertXpathExists(xPathExpression, inDocument);
    }

    /**
     * Assert that a specific XPath exists in some given XML
     * @param inXpathExpression
     * @param inDocument
     * @see SimpleXpathEngine which provides the underlying evaluation mechanism
     */
    public static void assertXpathExists(String xPathExpression, Document inDocument) throws TransformerException {
        SimpleXpathEngine simpleXpathEngine = new SimpleXpathEngine();
        NodeList nodeList = simpleXpathEngine.getMatchingNodes(xPathExpression, inDocument);
        int matches = nodeList.getLength();
        assertTrue("Expecting to find matches for Xpath " + xPathExpression, matches > 0);
    }

    /**
     * Assert that a specific XPath does NOT exist in some given XML
     * @param inXpathExpression
     * @param inXMLString
     * @see SimpleXpathEngine which provides the underlying evaluation mechanism
     */
    public static void assertXpathNotExists(String xPathExpression, String inXMLString) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        Document inDocument = XMLUnit.buildControlDocument(inXMLString);
        assertXpathNotExists(xPathExpression, inDocument);
    }

    /**
     * Assert that a specific XPath does NOT exist in some given XML
     * @param inXpathExpression
     * @param inDocument
     * @see SimpleXpathEngine which provides the underlying evaluation mechanism
     */
    public static void assertXpathNotExists(String xPathExpression, Document inDocument) throws TransformerException {
        SimpleXpathEngine simpleXpathEngine = new SimpleXpathEngine();
        NodeList nodeList = simpleXpathEngine.getMatchingNodes(xPathExpression, inDocument);
        int matches = nodeList.getLength();
        assertEquals("Should be zero matches for Xpath " + xPathExpression, 0, matches);
    }

    /**
     * Assert that a String containing XML contains valid XML: the String must
     * contain a DOCTYPE declaration to be validated
     * @param xmlString
     * @throws SAXException
     * @throws ParserConfigurationException
     * @see Validator
     */
    public static void assertXMLValid(String xmlString) throws SAXException, ParserConfigurationException {
        assertXMLValid(new Validator(new StringReader(xmlString)));
    }

    /**
     * Assert that a String containing XML contains valid XML: the String must
     * contain a DOCTYPE to be validated, but the validation will use the
     * systemId to obtain the DTD
     * @param xmlString
     * @param systemId
     * @throws SAXException
     * @throws ParserConfigurationException
     * @see Validator
     */
    public static void assertXMLValid(String xmlString, String systemId) throws SAXException, ParserConfigurationException {
        assertXMLValid(new Validator(new StringReader(xmlString), systemId));
    }

    /**
     * Assert that a String containing XML contains valid XML: the String will
     * be given a DOCTYPE to be validated with the name and systemId specified
     * regardless of whether it already contains a doctype declaration.
     * @param xmlString
     * @param systemId
     * @param doctype
     * @throws SAXException
     * @throws ParserConfigurationException
     * @see Validator
     */
    public static void assertXMLValid(String xmlString, String systemId, String doctype) throws SAXException, ParserConfigurationException {
        assertXMLValid(new Validator(new StringReader(xmlString), systemId, doctype));
    }

    /**
     * Assert that a Validator instance returns <code>isValid() == true</code>
     * @param validator
     */
    public static void assertXMLValid(Validator validator) {
        assertEquals(validator.toString(), true, validator.isValid());
    }

    /**
     * Execute a <code>NodeTest<code> for a single node type
     * and assert that it passes
     * @param xmlString XML to be tested
     * @param tester The test strategy
     * @param nodeType The node type to be tested: constants defined
     *  in {@link Node org.w3c.dom.Node} e.g. <code>Node.ELEMENT_NODE</code>
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * @see AbstractNodeTester
     * @see CountingNodeTester
     */
    public static void assertNodeTestPasses(String xmlString, NodeTester tester, short nodeType) throws SAXException, ParserConfigurationException, IOException {
        NodeTest test = new NodeTest(xmlString);
        assertNodeTestPasses(test, tester, new short[] { nodeType }, true);
    }

    /**
     * Execute a <code>NodeTest<code> for multiple node types and make an
     * assertion about it whether it is expected to pass
     * @param test a NodeTest instance containing the XML source to be tested
     * @param tester The test strategy
     * @param nodeTypes The node types to be tested: constants defined
     *  in {@link Node org.w3c.dom.Node} e.g. <code>Node.ELEMENT_NODE</code>
     * @param assertion true if the test is expected to pass, false otherwise
     * @see AbstractNodeTester
     * @see CountingNodeTester
     */
    public static void assertNodeTestPasses(NodeTest test, NodeTester tester, short[] nodeTypes, boolean assertion) {
        try {
            test.performTest(tester, nodeTypes);
            if (!assertion) {
                fail("Expected node test to fail, but it passed!");
            }
        } catch (NodeTestException e) {
            if (assertion) {
                fail("Expected node test to pass, but it failed! " + e.getMessage());
            }
        }
    }
}
