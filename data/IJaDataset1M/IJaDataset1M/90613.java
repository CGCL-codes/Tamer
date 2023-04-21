package com.volantis.mcs.xml.validation.sax.xerces;

import com.volantis.mcs.utilities.BooleanObject;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.xml.schema.JarFileEntityResolver;
import com.volantis.xml.schema.W3CSchemata;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Text;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.List;

/**
 * Test case for the {@link XercesBasedDOMValidator} class. These tests only
 * test against XML that does not contain any namespace declarations
 */
public class XercesBasedDOMValidatorTestCase extends AbstractValidationTestAbstract {

    /**
     * The xml that will be used to test the validator
     */
    private static final String noNamespaceTestXML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "<shiporder orderid='889923' xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\" xsi:noNamespaceSchemaLocation=\"http://fred.com\">" + "<orderperson>John Smith</orderperson>" + "<shipto>" + "<name>Ola Nordmann</name>" + "<address>Langgt 23</address>" + "<city>4000 Stavanger</city>" + "<country>Norway</country>" + "</shipto>" + "<item>" + "<title>Empire Burlesque</title>" + "<note>Special Edition</note>" + "<quantity>1</quantity>" + "<price>10.90</price>" + "</item>" + "<item>" + "<title>Hide your heart</title>" + "<quantity>1</quantity>" + "<price>9.90</price>" + "</item>" + "</shiporder>";

    /**
     * This will be an instance of the class that we are testing
     */
    private DOMValidator validator;

    /**
     * Absolute path to the schema that will be used when validating
     */
    private final String schemaLocation;

    /**
     * This will refer to the xml that is being used to perform the test
     */
    private Document document;

    /**
     * Initializes a <code>XercesBasedDOMValidatorTestCase</code>
     */
    public XercesBasedDOMValidatorTestCase() {
        schemaLocation = getAbsoluteSchemaLocation();
    }

    protected void setUp() throws Exception {
        super.setUp();
        document = createDocument();
    }

    protected void tearDown() throws Exception {
        document = null;
        validator = null;
        super.tearDown();
    }

    /**
     * Factory method that factors a XercesBasedDOMValidator
     * @param errorReporter the error reporter that will be informed whenever
     * a validation method is received.
     * @return the DOMValidator
     */
    protected DOMValidator createValidator(ErrorReporter errorReporter) throws SAXException, ParserErrorException {
        JarFileEntityResolver er = new JarFileEntityResolver();
        er.addSystemIdMapping("http://fred.com", getXSDPath());
        DOMValidator xercesBasedValidator = new XercesBasedDOMValidator(er, errorReporter);
        xercesBasedValidator.deriveSchemaLocationFrom(document);
        return xercesBasedValidator;
    }

    /**
     * Factory method that creates the {@link Document} that will be used
     * for testing
     * @return a Document object
     * @throws JDOMException if an error occurs
     * @throws IOException if an error occurs
     */
    protected Document createDocument() throws JDOMException, IOException {
        return createDocumentFromString(noNamespaceTestXML);
    }

    protected Namespace getNamespace() {
        return null;
    }

    /**
     * Test that ensures that no errors are reported when validating a
     * "valid" document
     * @throws Exception if an error occurs
     */
    public void testValidElement() throws Exception {
        doTestValidator(new ExpectedError[] {}, document.getRootElement());
    }

    /**
     * Test that ensures that no errors are reported when validating a sub
     * element of a "valid" root element
     * @throws Exception if an error occurs
     */
    public void testValidSubElement() throws Exception {
        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");
        doTestValidator(new ExpectedError[] {}, shipto);
    }

    /**
     * Tests that the validator reports the correct error when an unexpected
     * element is encountered.
     * @throws Exception if an error occurs
     */
    public void testValidationOfUnknownElement() throws Exception {
        Element root = document.getRootElement();
        addChild(root, "fred");
        String expectedErrorPath = "/" + getQName("shiporder") + "/" + getQName("fred");
        ExpectedError[] error = { new ExpectedError(expectedErrorPath, FaultTypes.INVALID_ELEMENT_LOCATION, getQName("fred")) };
        doTestValidator(error, root);
    }

    /**
     * Tests that the validator reports the correct error when an unexpected
     * element is encountered.
     * @throws Exception if an error occurs
     */
    public void testValidationOfUnknownElementPredicatedPath() throws Exception {
        Element root = document.getRootElement();
        List children = getChildren(root, "item");
        assertEquals("there should be 2 item elements", 2, children.size());
        Element item = (Element) children.get(1);
        addChild(item, "undeclared");
        String expectedErrorPath = "/" + getQName("shiporder") + "/" + getQName("item[2]") + "/" + getQName("undeclared");
        ExpectedError[] error = { new ExpectedError(expectedErrorPath, FaultTypes.INVALID_ELEMENT_LOCATION, getQName("undeclared")) };
        doTestValidator(error, root);
    }

    /**
     * Tests that the validator reports the correct error when an elements
     * content is invalid
     * @throws Exception if an error occurs
     */
    public void testValidationOfInvalidElementContent() throws Exception {
        Element root = document.getRootElement();
        Element item = getChild(root, "item");
        Element quantity = getChild(item, "quantity");
        quantity.addContent(new Text("hello"));
        String expectedErrorPath = "/" + getQName("shiporder") + "/" + getQName("item[1]") + "/" + getQName("quantity");
        ExpectedError[] error = { new ExpectedError(expectedErrorPath, FaultTypes.INVALID_SCHEMA_DATA_TYPE, "1hello"), new ExpectedError(expectedErrorPath, FaultTypes.INVALID_ELEMENT_CONTENT, getQName("quantity")) };
        doTestValidator(error, root);
    }

    /**
     * Tests that the validator reports the correct error when an attributes
     * content is invalid
     * @throws Exception if an error occurs
     */
    public void testValidationOfInvalidAttributeContent() throws Exception {
        Element root = document.getRootElement();
        assertEquals("shiporder element has unexpected value", "889923", root.getAttributeValue("orderid"));
        root.setAttribute("orderid", "abcdefg");
        String expectedErrorPath = "/" + getQName("shiporder") + "/@orderid";
        ExpectedError[] error = { new ExpectedError(expectedErrorPath, FaultTypes.INVALID_SCHEMA_PATTERN_VALUE, "orderid"), new ExpectedError(expectedErrorPath, FaultTypes.INVALID_ATTRIBUTE_CONTENT, "orderid") };
        doTestValidator(error, root);
    }

    /**
     * Tests that the validator reports the correct error when a sub
     * element has an unexpected child element
     * @throws Exception if an error occurs
     */
    public void testValidationOfUnknownSubElement() throws Exception {
        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");
        addChild(shipto, "undeclared");
        String expectedErrorPath = "/" + getQName("shiporder") + "/" + getQName("shipto") + "/" + getQName("undeclared");
        ExpectedError[] error = { new ExpectedError(expectedErrorPath, FaultTypes.INVALID_ELEMENT_LOCATION, getQName("undeclared")) };
        doTestValidator(error, shipto);
    }

    /**
     * Tests that the validator reports the correct error when an unknown
     * attribute is encountered
     * @throws Exception if an error occurs
     */
    public void testValidationOfUnknownAttribute() throws Exception {
        Element root = document.getRootElement();
        root.setAttribute("undeclared", "true");
        String expectedErrorPath = "/" + getQName("shiporder") + "/@undeclared";
        ExpectedError[] error = { new ExpectedError(expectedErrorPath, FaultTypes.INVALID_ATTRIBUTE_LOCATION, "undeclared") };
        doTestValidator(error, root);
    }

    /**
     * Helper method that validates the element passed in and checks that
     * any errors reported match those passed in via the ExpectedError array
     * @param expectedErrors an array of ExpectedErrors. If empty the client
     * does not expect any errors
     * @param element the element that is to be validated
     * @throws SAXException if an error occurs
     * @throws ParserErrorException if an error occurs
     */
    private void doTestValidator(ExpectedError[] expectedErrors, Element element) throws SAXException, ParserErrorException {
        validator = createValidator(new TestErrorReporter(expectedErrors));
        validator.validate(element);
    }

    /**
     * Tests that a registered supplementary validator is invoked when
     * expected.
     * @throws Exception if an error occurs
     */
    public void testSupplementaryValidatorInvoked() throws Exception {
        Element root = document.getRootElement();
        final Element shipto = getChild(root, "shipto");
        final BooleanObject firstBO = new BooleanObject();
        firstBO.setValue(false);
        DOMSupplementaryValidator firstSupplementartyValidator = new DOMSupplementaryValidator() {

            public void validate(Element element, ErrorReporter errorReporter) {
                firstBO.setValue(true);
                assertEquals("Elment should be the 'shipto' element", shipto, element);
            }
        };
        final BooleanObject secondBO = new BooleanObject();
        secondBO.setValue(false);
        DOMSupplementaryValidator secondSupplementartyValidator = new DOMSupplementaryValidator() {

            public void validate(Element element, ErrorReporter errorReporter) {
                secondBO.setValue(true);
                assertEquals("Elment should be the 'shipto' element", shipto, element);
            }
        };
        validator = createValidator(new TestErrorReporter(new ExpectedError[] {}));
        validator.addSupplementaryValidator(shipto.getNamespaceURI(), shipto.getName(), firstSupplementartyValidator);
        validator.addSupplementaryValidator(shipto.getNamespaceURI(), shipto.getName(), secondSupplementartyValidator);
        validator.validate(root);
        assertTrue("The first supplementary validator was not invoked", firstBO.getValue());
        assertTrue("The second supplementary validator was not invoked", secondBO.getValue());
    }

    /**
     * Test that a supplementary validator is still invoked even if a "normal"
     * error has been reported.
     * @throws Exception if an error occurs
     */
    public void testSupplementaryValidatorInvokedWhenNormalError() throws Exception {
        Element root = document.getRootElement();
        final Element shipto = getChild(root, "shipto");
        shipto.setAttribute("undeclared", "true");
        final BooleanObject bo = new BooleanObject();
        bo.setValue(false);
        DOMSupplementaryValidator supplementartyValidator = new DOMSupplementaryValidator() {

            public void validate(Element element, ErrorReporter errorReporter) {
                bo.setValue(true);
                assertEquals("Element should be the 'shipto' element", shipto, element);
            }
        };
        final BooleanObject erBO = new BooleanObject();
        bo.setValue(false);
        ErrorReporter er = new ErrorReporter() {

            public void reportError(ErrorDetails details) {
                assertTrue("Supplementary validator should be executed " + "before standard errors have been reported", bo.getValue());
                erBO.setValue(true);
            }

            public void validationCompleted(XPath xpath) {
            }

            public void validationStarted(Element root, XPath xpath) {
            }
        };
        validator = createValidator(er);
        validator.addSupplementaryValidator(shipto.getNamespaceURI(), shipto.getName(), supplementartyValidator);
        validator.validate(root);
        assertTrue("The error reported was not invoked  ", erBO.getValue());
        assertTrue("The supplementary validator was not invoked", bo.getValue());
    }

    /**
     * Test that a supplementary validator is not invoked for an element in
     * a different namespace but the same name.
     * @throws Exception if an error occurs
     */
    public void testSupplementaryValidatorNotInvokedWhenDifferentNamesapce() throws Exception {
        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");
        final BooleanObject bo = new BooleanObject();
        bo.setValue(false);
        DOMSupplementaryValidator supplementartyValidator = new DOMSupplementaryValidator() {

            public void validate(Element element, ErrorReporter errorReporter) {
                bo.setValue(true);
            }
        };
        validator = createValidator(new TestErrorReporter(new ExpectedError[] {}));
        validator.addSupplementaryValidator("bogus namepsace", shipto.getName(), supplementartyValidator);
        validator.validate(root);
        assertFalse("The supplementary validator was invoked", bo.getValue());
    }

    /**
     * Test that a supplementary validator is removed correctly.
     * @throws Exception if an error occurs
     */
    public void testSupplementaryValidatorRemoval() throws Exception {
        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");
        final BooleanObject bo = new BooleanObject();
        bo.setValue(false);
        DOMSupplementaryValidator supplementartyValidator = new DOMSupplementaryValidator() {

            public void validate(Element element, ErrorReporter errorReporter) {
                bo.setValue(true);
            }
        };
        validator = createValidator(new TestErrorReporter(new ExpectedError[] {}));
        validator.addSupplementaryValidator(shipto.getNamespaceURI(), shipto.getName(), supplementartyValidator);
        validator.validate(root);
        assertTrue("The supplementary validator was not invoked", bo.getValue());
        bo.setValue(false);
        validator.removeSupplementaryValidator(shipto.getNamespaceURI(), shipto.getName(), supplementartyValidator);
        validator.validate(root);
        assertFalse("The supplementary validator was not removed", bo.getValue());
    }
}
