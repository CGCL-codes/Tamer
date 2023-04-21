package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.dom.NodeSequence;
import com.volantis.mcs.dom.TextMock;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.styling.StylesMock;

/**
 * Test the DOMOutputBuffer.
 *
 */
public class DOMOutputBufferTestCase extends OutputBufferTestAbstract {

    DOMOutputBuffer buffer;

    DOMFactoryMock factoryMock;

    ElementMock element;

    ElementMock emptyElement;

    MCSAttributesMock attributes;

    com.volantis.styling.StylesMock styles;

    public void setUp() throws Exception {
        super.setUp();
        factoryMock = new DOMFactoryMock("factoryMock", expectations);
        element = new ElementMock("element", expectations);
        emptyElement = new ElementMock("emptyElement", expectations);
        styles = new StylesMock("styles", expectations);
    }

    /**
     * This method sets up the expectations that are required to run the
     * {@link #testAddContentsEmptiesSource} test.
     *
     * @param textNode mock of the textNode that will be used during the test
     * @param textToWrite the text that should be written to the buffer
     */
    void setUpAddContentsEmptiesSourceExpectations(TextMock textNode, String textToWrite) {
        factoryMock.expects.createElement().returns(emptyElement);
        emptyElement.expects.getHead().returns(null);
        factoryMock.expects.createElement().returns(element);
        element.expects.getHead().returns(null);
        element.expects.getTail().returns(null);
        factoryMock.expects.createText().returns(textNode);
        textNode.expects.setEncoded(false);
        element.expects.addTail(textNode);
        for (int i = 0; i < textToWrite.length(); i++) {
            textNode.expects.append(textToWrite.charAt(i));
        }
        element.expects.getHead().returns(textNode);
        element.expects.getTail().returns(textNode);
        element.expects.addChildrenToTail(emptyElement);
        element.expects.getHead().returns(null);
        emptyElement.expects.getHead().returns(textNode);
    }

    /**
     * Create a DOMOutputBuffer to test.
     * @return The new DOMOutputBuffer to test.
     */
    public OutputBuffer createOutputBuffer() {
        buffer = new DOMOutputBuffer(factoryMock);
        buffer.initialise();
        return buffer;
    }

    /**
     * Tests that the addStyledElement method returns an element with the
     * ClassifiedStylePropertiesContainer set to that of the MCSAttributes
     * passed in.
     */
    public void testAddElementWithStyle() {
        factoryMock.expects.createElement().returns(emptyElement);
        factoryMock.fuzzy.createStyledElement(attributes).returns(element);
        element.expects.setName("test");
        emptyElement.expects.addTail(element);
        element.expects.getStyles().returns(styles);
        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.addStyledElement("test", attributes);
        assertEquals(element.getStyles(), styles);
    }

    /**
     * Tests that the addElement method returns an element with a null
     * ClassifiedStylePropertiesContainer.
     */
    public void testAddElementWithoutStyle() {
        factoryMock.expects.createElement().returns(emptyElement);
        factoryMock.expects.createElement("test").returns(element);
        emptyElement.expects.addTail(element);
        element.expects.getStyles().returns(null);
        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.addElement("test");
        assertNull(element.getStyles());
    }

    /**
     * Tests that the openStyledElement method returns an element with the
     * ClassifiedStylePropertiesContainer set to that of the MCSAttributes
     * passed in.
     */
    public void testOpenElementWithStyle() {
        factoryMock.expects.createElement().returns(emptyElement);
        factoryMock.fuzzy.createStyledElement(attributes).returns(element);
        element.expects.setName("test");
        emptyElement.expects.addTail(element);
        element.expects.getStyles().returns(styles);
        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.openStyledElement("test", attributes);
        assertEquals(element.getStyles(), styles);
    }

    /**
     * Tests that the openElement method returns an element with a null
     * ClassifiedStylePropertiesContainer.
     */
    public void testOpenElementWithoutStyle() {
        factoryMock.expects.createElement().returns(emptyElement);
        factoryMock.expects.createElement("test").returns(element);
        emptyElement.expects.addTail(element);
        element.expects.getStyles().returns(null);
        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.openElement("test");
        assertNull(element.getStyles());
    }

    /**
     * Tests that the allocateStyledElement method returns an element with the
     * ClassifiedStylePropertiesContainer set to that of the MCSAttributes
     * passed in.
     *
     * todo XDIME-CP fix this
     */
    public void notestAllocateElementWithStyle() {
        factoryMock.expects.createElement().returns(emptyElement);
        emptyElement.expects.setName(null);
        factoryMock.fuzzy.createStyledElement(attributes).returns(element);
        element.expects.setName("test");
        element.expects.getStyles().returns(styles);
        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.allocateStyledElement("test", styles);
        assertEquals(element.getStyles(), styles);
    }

    /**
     * Tests that the allocateElement method returns an element with a null
     * ClassifiedStylePropertiesContainer.
     */
    public void testAllocateElementWithoutStyle() {
        factoryMock.expects.createElement().returns(emptyElement);
        factoryMock.expects.createElement("test").returns(element);
        element.expects.getStyles().returns(null);
        buffer = new DOMOutputBuffer(factoryMock);
        Element element = buffer.allocateElement("test");
        assertNull(element.getStyles());
    }

    /**
     * Ensure that adding white space does not create an empty text node.
     */
    public void testAddWhitespaceDoesNotCreateEmptyTextNode() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        assertTrue(buffer.isEmpty());
        buffer.writeText("    ");
        assertTrue(buffer.isEmpty());
    }

    /**
     * Ensure that adding text makes an empty buffer not empty.
     */
    public void addTextMakesNonEmpty() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        assertTrue(buffer.isEmpty());
        buffer.writeText("some text");
        assertFalse(buffer.isEmpty());
    }

    /**
     * Test that adding some preformatted white space makes an empty buffer
     * not empty.
     */
    public void testAddingPreformattedWhitespaceMakesNonEmpty() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.setElementIsPreFormatted(true);
        assertTrue(buffer.isEmpty());
        buffer.writeText("   ");
        assertFalse(buffer.isEmpty());
    }

    /**
     * Test that adding an element makes an empty buffer not empty.
     */
    public void testAddingElementMakesNonEmpty() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        assertTrue(buffer.isEmpty());
        buffer.addElement("element");
        assertFalse(buffer.isEmpty());
    }

    /**
     * Ensure that adding a node sequence updates the position correctly.
     */
    public void testAddNodeSequence() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.addElement("a");
        NodeSequence nodes = DOMUtilities.readSequence("<b/><c/>");
        buffer.addNodeSequence(nodes);
        buffer.addElement("d");
        String actual = DOMUtilities.toString(buffer.getRoot());
        assertEquals("<a/><b/><c/><d/>", actual);
    }

    /**
     * Ensure that adding the contents from one buffer to another empties the
     * source buffer.
     */
    public void testAddContentsEmptiesSource() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.addNodeSequence(DOMUtilities.readSequence("<a><b>B</b>C<d>D</d></a><e/>"));
        DOMOutputBuffer target = new DOMOutputBuffer();
        assertFalse(buffer.isEmpty());
        assertTrue(target.isEmpty());
        target.openElement("r");
        target.transferContentsFrom(buffer);
        target.closeElement("r");
        assertTrue(buffer.isEmpty());
        assertFalse(target.isEmpty());
        String actual;
        actual = DOMUtilities.toString(buffer.getRoot());
        assertEquals("Source buffer", "", actual);
        actual = DOMUtilities.toString(target.getRoot());
        assertEquals("Target buffer", "<r><a><b>B</b>C<d>D</d></a><e/></r>", actual);
    }
}
