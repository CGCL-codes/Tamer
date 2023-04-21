package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneRendering;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.StylesBuilder;

/**
 * This class unit test the XHTMLBasic_MIB2_1 protocol.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class XHTMLBasic_MIB2_1TestCase extends XHTMLBasicTestCase {

    protected static final String PANE_CLASS = "VF-0";

    private XHTMLBasic_MIB2_1 protocol;

    private XHTMLBasicTestable testable;

    private TestMarinerPageContext context;

    public XHTMLBasic_MIB2_1TestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(new TestProtocolRegistry.TestXHTMLBasic_MIB2_1Factory(), internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol, VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);
        this.protocol = (XHTMLBasic_MIB2_1) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }

    /**
     * Private setup.
     */
    private void privateSetUp() {
        canvasLayout = new CanvasLayout();
        pane = new Pane(canvasLayout);
        pane.setName(PANE_NAME);
        pane.setInstance(0);
        RuntimeDeviceLayout runtimeDeviceLayout = RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        context.setDeviceLayout(runtimeDeviceLayout);
        DeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        deviceContext.setDeviceLayout(runtimeDeviceLayout);
        context.pushDeviceLayoutContext(deviceContext);
        ContextInternals.setMarinerPageContext(requestContext, context);
        protocol.setMarinerPageContext(context);
        PaneInstance paneInstance = new TestPaneInstance();
        paneInstance.setStyleClass("fred");
        context.setFormatInstance(paneInstance);
        PageHead head = new PageHead();
        testable.setPageHead(head);
        testable.setStyleSheetRenderer(CSSStyleSheetRenderer.getSingleton());
        protocol.setMarinerPageContext(context);
        context.setDeviceName(DEVICE_NAME);
    }

    protected String getExpectedProtocolString() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML Basic 1.0//EN\" " + "\"http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd\">";
    }

    protected String getExpectedHorizontalRuleMarkup() {
        return "<hr/>";
    }

    public void testOpenPane() {
    }

    public void testOpenPaneWithContainerCell() throws Exception {
    }

    /**
     * To use a div, there must be style attributes on the pane and no td
     * around the pane's markup.
     *
     * todo XDIME-CP
     */
    public void notestOpenPaneUseDiv() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);
        dom.initialise();
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles("background-color: #ff0000"));
        attributes.setPane(pane);
        context.setFormatInstance(new TestPaneInstance());
        protocol.setMarinerPageContext(context);
        protocol.openPane(dom, attributes);
        Element element = dom.getCurrentElement();
        assertEquals("Element name", "div", element.getName());
    }

    /**
     * To use the surrounding td cell, there may be stylistic values on the
     * pane, but these must not clash with the cell's own attributes.
     */
    public void testOpenPaneUseCell() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);
        dom.initialise();
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles("background-color: #ff0000"));
        attributes.setPane(pane);
        context.setFormatInstance(new TestPaneInstance());
        protocol.setMarinerPageContext(context);
        Element parent = dom.openStyledElement("td", attributes);
        protocol.openPane(dom, attributes);
        Element element = dom.getCurrentElement();
        assertEquals("Element added", parent, element);
    }

    /**
     * To be unable to use the td cell, the surrounding td must have attributes
     * that clash with those of the pane. In this case a div will be output
     * to surround the pane's content.
     */
    public void testOpenPaneCantUseCell() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);
        Element parent = null;
        dom.initialise();
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles("background-color: #ff0000"));
        attributes.setPane(pane);
        context.setFormatInstance(new TestPaneInstance());
        protocol.setMarinerPageContext(context);
        parent = dom.openStyledElement("td", attributes);
        parent.setAttribute("id", "parent");
        attributes.setId("child");
        protocol.openPane(dom, attributes);
        Element element = dom.getCurrentElement();
        assertEquals("Element name", "div", element.getName());
    }

    /**
     * The pane's content doesn't need to be surrounded by anything if there
     * are no stylistic attributes on the pane.
     */
    public void notestOpenPaneDoNothing() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);
        PaneAttributes attributes = new PaneAttributes();
        Element parent = null;
        Element element = null;
        dom.initialise();
        attributes.setPane(pane);
        pane.setBackgroundColour(null);
        pane.setBorderWidth(null);
        pane.setCellPadding(null);
        pane.setCellSpacing(null);
        pane.setHeight(null);
        pane.setWidth(null);
        pane.setHorizontalAlignment(null);
        pane.setVerticalAlignment(null);
        context.setFormatInstance(new TestPaneInstance());
        protocol.setMarinerPageContext(context);
        testable.setPageHead(new PageHead());
        parent = dom.openStyledElement("body", attributes);
        protocol.openPane(dom, attributes);
        try {
            element = dom.closeElement("body");
        } catch (IllegalStateException e) {
            fail("Expected body element not found");
        }
        assertSame("The parent was not as expected", parent, element);
        assertEquals("Pane class attribute not as expected", null, element.getAttributeValue("class"));
    }

    /**
     * Helper method.
     */
    private DOMOutputBuffer setupForPaneTests(PaneRendering rendering, PaneAttributes attributes) {
        context = new TestMarinerPageContext();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        TestDeviceLayoutContext deviceLayoutContext = new TestDeviceLayoutContext();
        dom.initialise();
        context.pushDeviceLayoutContext(deviceLayoutContext);
        protocol.setMarinerPageContext(context);
        attributes.setPane(new Pane(null));
        PaneInstance paneInstance = (PaneInstance) context.getDeviceLayoutContext().getCurrentFormatInstance(attributes.getPane());
        paneInstance.setRendering(rendering);
        return dom;
    }

    public void testClosePane() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        DOMOutputBuffer dom = setupForPaneTests(PaneRendering.USE_TABLE, attributes);
        final Element expected;
        dom.openStyledElement("body", attributes);
        expected = dom.openElement("div");
        protocol.closePane(dom, attributes);
        assertSame("The DOM's current element isn't the body element", dom.getCurrentElement(), expected);
    }

    /**
     * Test closing a pane where a div has been used to surround the pane's
     * content.
     */
    public void testClosePaneUseDiv() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        DOMOutputBuffer dom = setupForPaneTests(PaneRendering.CREATE_ENCLOSING_ELEMENT, attributes);
        final Element expected;
        expected = dom.openStyledElement("body", attributes);
        dom.openElement("div");
        dom.appendEncoded("Example");
        protocol.closePane(dom, attributes);
        assertSame("The DOM's current element isn't the body element", dom.getCurrentElement(), expected);
    }

    /**
     * Test closing a pane where the surrounding td has been used as the
     * pane's content container.
     */
    public void testClosePaneUseCell() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        DOMOutputBuffer dom = setupForPaneTests(PaneRendering.USE_ENCLOSING_TABLE_CELL, attributes);
        final Element expected;
        expected = dom.openStyledElement("td", attributes);
        dom.appendEncoded("Example");
        protocol.closePane(dom, attributes);
        assertSame("The DOM's current element isn't the td element", dom.getCurrentElement(), expected);
    }

    /**
     * Test closing a pane where nothing needed to be done to surround the
     * pane's content.
     *
     * @throws Exception
     */
    public void testClosePaneDoNothing() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        DOMOutputBuffer dom = setupForPaneTests(PaneRendering.DO_NOTHING, attributes);
        final Element expected;
        expected = dom.openStyledElement("body", attributes);
        dom.appendEncoded("Example");
        protocol.closePane(dom, attributes);
        assertSame("The DOM's current element isn't the body element", dom.getCurrentElement(), expected);
    }

    protected String expectedDefaultMimeType() {
        return "text/html";
    }
}
