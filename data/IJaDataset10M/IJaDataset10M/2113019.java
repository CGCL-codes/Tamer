package org.custommonkey.xmlunit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * JUnit test for DifferenceEngine
 */
public class test_DifferenceEngine extends TestCase implements DifferenceConstants {

    private CollectingDifferenceListener listener;

    private DifferenceEngine engine;

    private Document document;

    private final ComparisonController PSEUDO_DIFF = new SimpleComparisonController();

    private final ComparisonController PSEUDO_DETAILED_DIFF = new NeverHaltingComparisonController();

    private static final ElementQualifier DEFAULT_ELEMENT_QUALIFIER = new ElementNameQualifier();

    private static final String TEXT_A = "the pack on my back is aching";

    private static final String TEXT_B = "the straps seem to cut me like a knife";

    private static final String COMMENT_A = "Im no clown I wont back down";

    private static final String COMMENT_B = "dont need you to tell me whats going down";

    private static final String[] PROC_A = { "down", "down down" };

    private static final String[] PROC_B = { "dadada", "down" };

    private static final String CDATA_A = "I'm standing alone, you're weighing the gold";

    private static final String CDATA_B = "I'm watching you sinking... Fools Gold";

    private static final String ATTR_A = "These boots were made for walking";

    private static final String ATTR_B = "The marquis de sade never wore no boots like these";

    private void assertDifferentText(Text control, Text test, Difference difference) {
        try {
            engine.compareText(control, test, listener);
        } catch (DifferenceEngine.DifferenceFoundException e) {
        }
        assertEquals(difference.getId(), listener.comparingWhat);
        assertEquals(true, listener.different);
        resetListener();
    }

    public void testCompareText() throws Exception {
        String expected = TEXT_A;
        String actual = TEXT_B;
        Text control = document.createTextNode(expected);
        Text test = document.createTextNode(actual);
        assertDifferentText(control, test, TEXT_VALUE);
    }

    private void assertDifferentProcessingInstructions(ProcessingInstruction control, ProcessingInstruction test, Difference difference) {
        try {
            engine.compareProcessingInstruction(control, test, listener);
        } catch (DifferenceEngine.DifferenceFoundException e) {
        }
        assertEquals(difference.getId(), listener.comparingWhat);
        assertEquals(true, listener.different);
        resetListener();
    }

    public void testCompareProcessingInstruction() throws Exception {
        String[] expected = PROC_A;
        String[] actual = PROC_B;
        ProcessingInstruction control = document.createProcessingInstruction(expected[0], expected[1]);
        ProcessingInstruction test = document.createProcessingInstruction(actual[0], actual[1]);
        assertDifferentProcessingInstructions(control, test, PROCESSING_INSTRUCTION_TARGET);
        ProcessingInstruction control2 = document.createProcessingInstruction(expected[0], expected[1]);
        ProcessingInstruction test2 = document.createProcessingInstruction(expected[0], actual[1]);
        assertDifferentProcessingInstructions(control2, test2, PROCESSING_INSTRUCTION_DATA);
    }

    private void assertDifferentComments(Comment control, Comment test, Difference difference) {
        try {
            engine.compareComment(control, test, listener);
        } catch (DifferenceEngine.DifferenceFoundException e) {
        }
        assertEquals(difference.getId(), listener.comparingWhat);
        assertEquals(true, listener.different);
        resetListener();
    }

    public void testCompareComment() throws Exception {
        String expected = COMMENT_A;
        String actual = COMMENT_B;
        Comment control = document.createComment(expected);
        Comment test = document.createComment(actual);
        assertDifferentComments(control, test, COMMENT_VALUE);
    }

    private void assertDifferentCDATA(CDATASection control, CDATASection test, Difference difference) {
        try {
            engine.compareCDataSection(control, test, listener);
        } catch (DifferenceEngine.DifferenceFoundException e) {
        }
        assertEquals(difference.getId(), listener.comparingWhat);
        assertEquals(true, listener.different);
        resetListener();
    }

    public void testCompareCDATA() throws Exception {
        String expected = CDATA_A;
        String actual = CDATA_B;
        CDATASection control = document.createCDATASection(expected);
        CDATASection test = document.createCDATASection(actual);
        assertDifferentCDATA(control, test, CDATA_VALUE);
    }

    private void assertDifferentDocumentTypes(DocumentType control, DocumentType test, Difference difference, boolean fatal) {
        try {
            engine.compareDocumentType(control, test, listener);
            if (fatal) {
                fail("Expected fatal difference!");
            }
        } catch (DifferenceEngine.DifferenceFoundException e) {
            if (!fatal) {
                fail("Expected similarity not fatal difference!");
            }
        }
        assertEquals(difference.getId(), listener.comparingWhat);
        assertEquals(fatal, listener.different);
        resetListener();
    }

    public void testCompareDocumentType() throws Exception {
        File tmpFile = File.createTempFile("Roses", "dtd");
        tmpFile.deleteOnExit();
        String tmpDTD = "<!ELEMENT leaf (#PCDATA)><!ELEMENT root (leaf)>";
        new FileWriter(tmpFile).write(tmpDTD);
        String rosesDTD = tmpFile.toURL().toExternalForm();
        File altTmpFile = File.createTempFile("TheCrows", "dtd");
        altTmpFile.deleteOnExit();
        new FileWriter(altTmpFile).write(tmpDTD);
        String theCrowsDTD = altTmpFile.toURL().toExternalForm();
        Document controlDoc = XMLUnit.buildControlDocument("<!DOCTYPE root PUBLIC 'Stone' '" + rosesDTD + "'>" + "<root><leaf/></root>");
        Document testDoc = XMLUnit.buildTestDocument("<!DOCTYPE tree PUBLIC 'Stone' '" + rosesDTD + "'>" + "<tree><leaf/></tree>");
        DocumentType control = controlDoc.getDoctype();
        DocumentType test = testDoc.getDoctype();
        assertDifferentDocumentTypes(control, test, DOCTYPE_NAME, true);
        test = XMLUnit.buildTestDocument("<!DOCTYPE root PUBLIC 'id' '" + rosesDTD + "'>" + "<root><leaf/></root>").getDoctype();
        assertDifferentDocumentTypes(control, test, DOCTYPE_PUBLIC_ID, true);
        test = XMLUnit.buildTestDocument("<!DOCTYPE root SYSTEM '" + rosesDTD + "'>" + "<root><leaf/></root>").getDoctype();
        assertDifferentDocumentTypes(control, test, DOCTYPE_PUBLIC_ID, true);
        test = XMLUnit.buildTestDocument("<!DOCTYPE root PUBLIC 'Stone' '" + theCrowsDTD + "'>" + "<root><leaf/></root>").getDoctype();
        assertDifferentDocumentTypes(control, test, DOCTYPE_SYSTEM_ID, false);
        test = XMLUnit.buildTestDocument("<!DOCTYPE root SYSTEM '" + theCrowsDTD + "'>" + "<root><leaf/></root>").getDoctype();
        assertDifferentDocumentTypes(control, test, DOCTYPE_PUBLIC_ID, true);
        control = XMLUnit.buildTestDocument("<!DOCTYPE root SYSTEM '" + rosesDTD + "'>" + "<root><leaf/></root>").getDoctype();
        assertDifferentDocumentTypes(control, test, DOCTYPE_SYSTEM_ID, false);
    }

    private void assertDifferentAttributes(Attr control, Attr test, Difference difference, boolean fatal) {
        try {
            engine.compareAttribute(control, test, listener);
            if (fatal) {
                fail("Expecting fatal difference!");
            }
        } catch (DifferenceEngine.DifferenceFoundException e) {
            if (!fatal) {
                fail("Expecting similarity not fatal difference!");
            }
        }
        assertEquals(difference.getId(), listener.comparingWhat);
        assertEquals(fatal, listener.different);
        resetListener();
    }

    public void testCompareAttribute() throws Exception {
        String expected = ATTR_A;
        String actual = ATTR_B;
        Attr control = document.createAttribute(getName());
        control.setValue(expected);
        Attr test = document.createAttribute(getName());
        test.setValue(actual);
        assertDifferentAttributes(control, test, ATTR_VALUE, true);
        String doctypeDeclaration = "<!DOCTYPE manchester [" + "<!ELEMENT sound EMPTY><!ATTLIST sound sorted (true|false) \"true\">" + "<!ELEMENT manchester (sound)>]>";
        Document controlDoc = XMLUnit.buildControlDocument(doctypeDeclaration + "<manchester><sound sorted=\"true\"/></manchester>");
        control = (Attr) controlDoc.getDocumentElement().getFirstChild().getAttributes().getNamedItem("sorted");
        Document testDoc = XMLUnit.buildTestDocument(doctypeDeclaration + "<manchester><sound/></manchester>");
        test = (Attr) testDoc.getDocumentElement().getFirstChild().getAttributes().getNamedItem("sorted");
        assertDifferentAttributes(control, test, ATTR_VALUE_EXPLICITLY_SPECIFIED, false);
    }

    private void assertDifferentElements(Element control, Element test, Difference difference) {
        try {
            engine.compareElement(control, test, listener);
        } catch (DifferenceEngine.DifferenceFoundException e) {
        }
        assertEquals(difference.getId(), listener.comparingWhat);
        assertEquals(true, listener.different);
        resetListener();
    }

    public void testCompareElements() throws Exception {
        Document document = XMLUnit.buildControlDocument("<down><im standing=\"alone\"/><im watching=\"you\" all=\"\"/>" + "<im watching=\"you all\"/><im watching=\"you sinking\"/></down>");
        Element control = (Element) document.getDocumentElement();
        Element test = (Element) control.getFirstChild();
        assertDifferentElements(control, test, ELEMENT_TAG_NAME);
        control = test;
        test = (Element) control.getNextSibling();
        assertDifferentElements(control, test, ELEMENT_NUM_ATTRIBUTES);
        test = (Element) test.getNextSibling();
        assertDifferentElements(control, test, ATTR_NAME_NOT_FOUND);
        control = test;
        test = (Element) control.getNextSibling();
        assertDifferentElements(control, test, ATTR_VALUE);
    }

    public void testCompareNode() throws Exception {
        Document controlDocument = XMLUnit.buildControlDocument("<root>" + "<!-- " + COMMENT_A + " -->" + "<?" + PROC_A[0] + " " + PROC_A[1] + " ?>" + "<elem attr=\"" + ATTR_A + "\">" + TEXT_A + "</elem></root>");
        Document testDocument = XMLUnit.buildTestDocument("<root>" + "<!-- " + COMMENT_B + " -->" + "<?" + PROC_B[0] + " " + PROC_B[1] + " ?>" + "<elem attr=\"" + ATTR_B + "\">" + TEXT_B + "</elem></root>");
        engine.compare(controlDocument, testDocument, listener, null);
        Node control = controlDocument.getDocumentElement().getFirstChild();
        Node test = testDocument.getDocumentElement().getFirstChild();
        do {
            resetListener();
            engine.compare(control, test, listener, null);
            assertEquals(true, -1 != listener.comparingWhat);
            assertEquals(false, listener.nodesSkipped);
            resetListener();
            engine.compare(control, control, listener, null);
            assertEquals(-1, listener.comparingWhat);
            control = control.getNextSibling();
            test = test.getNextSibling();
        } while (control != null);
    }

    private void assertDifferentNamespaceDetails(Node control, Node test, Difference expectedDifference, boolean fatal) {
        try {
            engine.compareNodeBasics(control, test, listener);
            if (fatal) {
                fail("Expected fatal difference");
            }
        } catch (DifferenceEngine.DifferenceFoundException e) {
            if (!fatal) {
                fail("Not expecting fatal difference!");
            }
        }
        assertEquals(expectedDifference.getId(), listener.comparingWhat);
        assertEquals(fatal, listener.different);
        resetListener();
    }

    public void testCompareNodeBasics() throws Exception {
        String namespaceA = "http://example.org/StoneRoses";
        String namespaceB = "http://example.org/Stone/Roses";
        String prefixA = "music";
        String prefixB = "cd";
        String elemName = "nowPlaying";
        Element control = document.createElementNS(namespaceA, prefixA + ':' + elemName);
        engine.compareNodeBasics(control, control, listener);
        Element test = document.createElementNS(namespaceB, prefixA + ':' + elemName);
        assertDifferentNamespaceDetails(control, test, NAMESPACE_URI, true);
        test = document.createElementNS(namespaceA, prefixB + ':' + elemName);
        assertDifferentNamespaceDetails(control, test, NAMESPACE_PREFIX, false);
    }

    private void assertDifferentChildren(Node control, Node test, Difference expectedDifference, boolean fatal) {
        try {
            engine.compareHasChildNodes(control, test, listener);
            engine.compareNodeChildren(control, test, listener, DEFAULT_ELEMENT_QUALIFIER);
            if (fatal) {
                fail("Expected fatal difference");
            }
        } catch (DifferenceEngine.DifferenceFoundException e) {
            if (!fatal) {
                fail("Not expecting fatal difference " + listener.comparingWhat + ": expected " + listener.expected + " but was " + listener.actual);
            }
        }
        assertEquals(expectedDifference == null ? -1 : expectedDifference.getId(), listener.comparingWhat);
        assertEquals(fatal, listener.different);
        resetListener();
    }

    public void testCompareNodeChildren() throws Exception {
        document = XMLUnit.buildControlDocument("<down><im standing=\"alone\"/><im><watching/>you all</im>" + "<im watching=\"you\">sinking</im></down>");
        Node control = document.getDocumentElement().getFirstChild();
        Node test = control;
        assertDifferentChildren(control, control, null, false);
        test = control.getNextSibling();
        assertDifferentChildren(control, test, HAS_CHILD_NODES, true);
        control = test;
        test = control.getNextSibling();
        assertDifferentChildren(control, test, CHILD_NODELIST_LENGTH, true);
    }

    private void assertDifferentNodeLists(Node control, Node test, Difference expectedDifference, boolean fatal) {
        try {
            engine.compareNodeList(control.getChildNodes(), test.getChildNodes(), control.getChildNodes().getLength(), listener, DEFAULT_ELEMENT_QUALIFIER);
            if (fatal) {
                fail("Expected fatal difference");
            }
        } catch (DifferenceEngine.DifferenceFoundException e) {
            if (!fatal) {
                fail("Not expecting fatal difference!");
            }
        }
        assertEquals(expectedDifference == null ? -1 : expectedDifference.getId(), listener.comparingWhat);
        assertEquals(fatal, listener.different);
        resetListener();
    }

    public void testCompareNodeList() throws Exception {
        document = XMLUnit.buildControlDocument("<down><im><standing/>alone</im><im><watching/>you all</im>" + "<im><watching/>you sinking</im></down>");
        Node control = document.getDocumentElement().getFirstChild();
        Node test = control;
        assertDifferentNodeLists(control, test, null, false);
        test = control.getNextSibling();
        assertDifferentChildren(control, test, ELEMENT_TAG_NAME, true);
        control = test;
        test = control.getNextSibling();
        assertDifferentChildren(control, test, TEXT_VALUE, true);
    }

    public void testCompareNodeListElements() throws Exception {
        Element control = document.createElement("root");
        control.appendChild(document.createElement("leafElemA"));
        control.appendChild(document.createElement("leafElemB"));
        Element test = document.createElement("root");
        test.appendChild(document.createElement("leafElemB"));
        test.appendChild(document.createElement("leafElemA"));
        assertDifferentChildren(control, test, CHILD_NODELIST_SEQUENCE, false);
        assertDifferentChildren(test, control, CHILD_NODELIST_SEQUENCE, false);
    }

    public void testCompareNodeListMixedContent() throws Exception {
        Element control = document.createElement("root");
        control.appendChild(document.createTextNode("text leaf"));
        control.appendChild(document.createElement("leafElem"));
        Element test = document.createElement("root");
        test.appendChild(document.createElement("leafElem"));
        test.appendChild(document.createTextNode("text leaf"));
        assertDifferentChildren(control, test, CHILD_NODELIST_SEQUENCE, false);
        assertDifferentChildren(test, control, CHILD_NODELIST_SEQUENCE, false);
    }

    public void testBasicCompare() throws Exception {
        try {
            engine.compare("black", "white", null, null, listener, ATTR_NAME_NOT_FOUND);
            fail("Expected difference found exception");
        } catch (DifferenceEngine.DifferenceFoundException e) {
            assertEquals(true, listener.different);
            assertEquals(ATTR_NAME_NOT_FOUND.getId(), listener.comparingWhat);
        }
        resetListener();
        try {
            engine.compare("black", "white", null, null, listener, NAMESPACE_PREFIX);
            assertEquals(false, listener.different);
            assertEquals(NAMESPACE_PREFIX.getId(), listener.comparingWhat);
        } catch (Exception e) {
            fail("Not expecting difference found exception");
        }
    }

    public void testXpathLocation1() throws Exception {
        String control = "<dvorak><keyboard/><composer/></dvorak>";
        String test = "<qwerty><keyboard/></qwerty>";
        listenToDifferences(control, test);
        assertEquals("1st control xpath", "/dvorak[1]", listener.controlXpath);
        assertEquals("1st test xpath", "/qwerty[1]", listener.testXpath);
    }

    public void testXpathLocation2() throws Exception {
        String control = "<dvorak><keyboard/><composer/></dvorak>";
        String test = "<qwerty><keyboard/></qwerty>";
        String start = "<a>", end = "</a>";
        listenToDifferences(start + control + end, start + test + end);
        assertEquals("2nd control xpath", "/a[1]/dvorak[1]", listener.controlXpath);
        assertEquals("2nd test xpath", "/a[1]/qwerty[1]", listener.testXpath);
    }

    public void testXpathLocation3() throws Exception {
        String control = "<stuff><wood type=\"rough\"/></stuff>";
        String test = "<stuff><wood type=\"smooth\"/></stuff>";
        listenToDifferences(control, test);
        assertEquals("3rd control xpath", "/stuff[1]/wood[1]/@type", listener.controlXpath);
        assertEquals("3rd test xpath", "/stuff[1]/wood[1]/@type", listener.testXpath);
    }

    public void testXpathLocation4() throws Exception {
        String control = "<stuff><glass colour=\"clear\"/><glass colour=\"green\"/></stuff>";
        String test = "<stuff><glass colour=\"clear\"/><glass colour=\"blue\"/></stuff>";
        ;
        listenToDifferences(control, test);
        assertEquals("4th control xpath", "/stuff[1]/glass[2]/@colour", listener.controlXpath);
        assertEquals("4th test xpath", "/stuff[1]/glass[2]/@colour", listener.testXpath);
    }

    public void testXpathLocation5() throws Exception {
        String control = "<stuff><wood>maple</wood><wood>oak</wood></stuff>";
        String test = "<stuff><wood>maple</wood><wood>ash</wood></stuff>";
        listenToDifferences(control, test);
        assertEquals("5th control xpath", "/stuff[1]/wood[2]/text()[1]", listener.controlXpath);
        assertEquals("5th test xpath", "/stuff[1]/wood[2]/text()[1]", listener.testXpath);
    }

    public void testXpathLocation6() throws Exception {
        String control = "<stuff><list><wood/><glass/></list><item/></stuff>";
        String test = "<stuff><list><wood/><glass/></list><item>description</item></stuff>";
        listenToDifferences(control, test);
        assertEquals("6th control xpath", "/stuff[1]/item[1]", listener.controlXpath);
        assertEquals("6th test xpath", "/stuff[1]/item[1]", listener.testXpath);
    }

    public void testXpathLocation7() throws Exception {
        String control = "<stuff><list><wood/></list></stuff>";
        String test = "<stuff><list><glass/></list></stuff>";
        listenToDifferences(control, test);
        assertEquals("7th control xpath", "/stuff[1]/list[1]/wood[1]", listener.controlXpath);
        assertEquals("7th test xpath", "/stuff[1]/list[1]/glass[1]", listener.testXpath);
    }

    public void testXpathLocation8() throws Exception {
        String control = "<stuff><list><!--wood--></list></stuff>";
        String test = "<stuff><list><!--glass--></list></stuff>";
        listenToDifferences(control, test);
        assertEquals("8th control xpath", "/stuff[1]/list[1]/comment()[1]", listener.controlXpath);
        assertEquals("8th test xpath", "/stuff[1]/list[1]/comment()[1]", listener.testXpath);
    }

    public void testXpathLocation9() throws Exception {
        String control = "<stuff><list/><?wood rough?><list/></stuff>";
        String test = "<stuff><list/><?glass clear?><list/></stuff>";
        listenToDifferences(control, test);
        assertEquals("9th control xpath", "/stuff[1]/processing-instruction()[1]", listener.controlXpath);
        assertEquals("9th test xpath", "/stuff[1]/processing-instruction()[1]", listener.testXpath);
    }

    public void testXpathLocation10() throws Exception {
        String control = "<stuff><list/>list<![CDATA[wood]]></stuff>";
        String test = "<stuff><list/>list<![CDATA[glass]]></stuff>";
        listenToDifferences(control, test);
        assertEquals("10th control xpath", "/stuff[1]/text()[2]", listener.controlXpath);
        assertEquals("10th test xpath", "/stuff[1]/text()[2]", listener.testXpath);
    }

    public void testXpathLocation11() throws Exception {
        String control = "<stuff><list><item/></list></stuff>";
        String test = "<stuff><list>item text</list></stuff>";
        listenToDifferences(control, test);
        assertEquals("11th control xpath", "/stuff[1]/list[1]/item[1]", listener.controlXpath);
        assertEquals("11th test xpath", "/stuff[1]/list[1]/text()[1]", listener.testXpath);
    }

    public void testXpathLocation12() throws Exception {
        engine = new DifferenceEngine(PSEUDO_DETAILED_DIFF);
        String control = "<stuff><item id=\"1\"/><item id=\"2\"/></stuff>";
        String test = "<stuff><item id=\"1\"/></stuff>";
        listenToDifferences(control, test);
        assertEquals("12th control xpath", "/stuff[1]/item[2]", listener.controlXpath);
        assertEquals("12th test xpath", "/stuff[1]/item[1]", listener.testXpath);
    }

    public void testXpathLocation13() throws Exception {
        engine = new DifferenceEngine(PSEUDO_DETAILED_DIFF);
        String control = "<stuff><item id=\"1\"/><item id=\"2\"/></stuff>";
        String test = "<stuff><?item data?></stuff>";
        listenToDifferences(control, test);
        assertEquals("13th control xpath", "/stuff[1]/item[2]", listener.controlXpath);
        assertEquals("13th test xpath", "/stuff[1]/processing-instruction()[1]", listener.testXpath);
    }

    public void testXpathLocation14() throws Exception {
        engine = new DifferenceEngine(PSEUDO_DETAILED_DIFF);
        String control = "<stuff><thing id=\"1\"/><item id=\"2\"/></stuff>";
        String test = "<stuff><item id=\"2\"/><item id=\"1\"/></stuff>";
        listenToDifferences(control, test);
        assertEquals("14th control xpath", "/stuff[1]/item[1]/@id", listener.controlXpath);
        assertEquals("14th test xpath", "/stuff[1]/item[2]/@id", listener.testXpath);
    }

    private void listenToDifferences(String control, String test) throws SAXException, IOException, ParserConfigurationException {
        Document controlDoc = XMLUnit.buildControlDocument(control);
        Document testDoc = XMLUnit.buildTestDocument(test);
        engine.compare(controlDoc, testDoc, listener, DEFAULT_ELEMENT_QUALIFIER);
    }

    private void resetListener() {
        listener = new CollectingDifferenceListener();
    }

    public void setUp() throws Exception {
        resetListener();
        engine = new DifferenceEngine(PSEUDO_DIFF);
        DocumentBuilder documentBuilder = XMLUnit.getControlParser();
        document = documentBuilder.newDocument();
    }

    public test_DifferenceEngine(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(test_DifferenceEngine.class);
    }

    private class SimpleComparisonController implements ComparisonController {

        public boolean haltComparison(Difference afterDifference) {
            return !afterDifference.isRecoverable();
        }
    }

    private class NeverHaltingComparisonController implements ComparisonController {

        public boolean haltComparison(Difference afterDifference) {
            return false;
        }
    }

    private class CollectingDifferenceListener implements DifferenceListener {

        public String expected;

        public String actual;

        public Node control;

        public Node test;

        public int comparingWhat = -1;

        public boolean different = false;

        public boolean nodesSkipped = false;

        public String controlXpath;

        public String testXpath;

        private boolean tracing = false;

        public int differenceFound(Difference difference) {
            if (tracing) {
                System.out.println(difference.toString());
            }
            assertNotNull("difference not null", difference);
            assertNotNull("control node detail not null", difference.getControlNodeDetail());
            assertNotNull("test node detail not null", difference.getTestNodeDetail());
            this.expected = difference.getControlNodeDetail().getValue();
            this.actual = difference.getTestNodeDetail().getValue();
            this.control = difference.getControlNodeDetail().getNode();
            this.test = difference.getTestNodeDetail().getNode();
            this.comparingWhat = difference.getId();
            this.different = !difference.isRecoverable();
            this.controlXpath = difference.getControlNodeDetail().getXpathLocation();
            this.testXpath = difference.getTestNodeDetail().getXpathLocation();
            return RETURN_ACCEPT_DIFFERENCE;
        }

        public void skippedComparison(Node control, Node test) {
            nodesSkipped = true;
        }

        public void setTrace(boolean active) {
            tracing = active;
        }
    }
}
