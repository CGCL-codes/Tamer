package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;
import java.io.IOException;

/**
 * Simple test protocol that generates easily testable output.
 */
public class RendererTestProtocol extends VolantisProtocol {

    /**
     * Duration output buffer.
     */
    private StringBuffer durationBuffer;

    /**
     * Default output buffer factory
     */
    private OutputBufferFactory defaultOutputBufferFactory;

    /**
     * Create a new RendererTestProtocol with the specified Mariner page
     * context and a default duration buffer.
     * <p>Used for testing renderers that do not require access to the
     * duration buffer (in general, anything other than tests specific to
     * temporal iteration).
     * @param pageContext The Mariner page context
     */
    public RendererTestProtocol(MarinerPageContext pageContext) {
        this(pageContext, new StringBuffer());
    }

    /**
     * Create a new RendererTestProtocol with the specified duration buffer
     * and MarinerPageContext.
     * @param pageContext The Mariner page context
     * @param durationBuffer The duration buffer
     */
    public RendererTestProtocol(MarinerPageContext pageContext, StringBuffer durationBuffer) {
        super(null);
        this.durationBuffer = durationBuffer;
        setMarinerPageContext(pageContext);
        this.defaultOutputBufferFactory = new DOMOutputBufferFactory(DOMFactory.getDefaultInstance());
    }

    public void writeOpenGrid(GridAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("grid", attributes.getStyles()));
    }

    public void writeOpenGridChild(GridChildAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("gridChild", attributes.getStyles()));
    }

    public void writeOpenGridRow(GridRowAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("gridRow", attributes.getStyles()));
    }

    public void writeCloseGrid(GridAttributes ga) {
        getBuffer().popElement();
    }

    public void writeCloseGridChild(GridChildAttributes ga) {
        getBuffer().popElement();
    }

    public void writeCloseGridRow(GridRowAttributes ga) {
        getBuffer().popElement();
    }

    public void writeOpenPane(PaneAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("pane", attributes.getStyles()));
    }

    public void writeClosePane(PaneAttributes attr) {
        getBuffer().popElement();
    }

    public void writeOpenSlide(SlideAttributes attributes) {
        durationBuffer.append(attributes.getDuration()).append(',');
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("slide", attributes.getStyles()));
    }

    public void writeCloseSlide(SlideAttributes attr) {
        getBuffer().popElement();
    }

    public void writeOpenForm(FormAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("form", attributes.getStyles()));
        appendAttribute("title", attributes.getTitle());
    }

    public void writeCloseForm(FormAttributes attributes) throws IOException {
        getBuffer().popElement();
    }

    public void writeFormPreamble(OutputBuffer buf) throws IOException {
        getBuffer().transferContentsFrom(buf);
    }

    public void writeFormPostamble(OutputBuffer buf) throws IOException {
        getBuffer().transferContentsFrom(buf);
    }

    public void writeLayout(DeviceLayoutContext dlc) throws IOException {
    }

    public OutputBufferFactory getOutputBufferFactory() {
        return defaultOutputBufferFactory;
    }

    public void openCanvasPage(CanvasAttributes attributes) throws IOException {
    }

    public void openInclusionPage(CanvasAttributes attributes) throws IOException {
    }

    public void closeInclusionPage(CanvasAttributes attributes) throws IOException, ProtocolException {
    }

    public void openMontagePage(MontageAttributes attributes) throws IOException {
    }

    protected void writeCanvasContent(PackageBodyOutput output, CanvasAttributes attributes) throws IOException, ProtocolException {
    }

    protected void writeMontageContent(PackageBodyOutput output, MontageAttributes attributes) throws IOException, ProtocolException {
    }

    public String defaultMimeType() {
        return "";
    }

    public void writeCloseSpatialFormatIterator(SpatialFormatIteratorAttributes attributes) {
        getBuffer().popElement();
    }

    public void writeCloseSpatialFormatIteratorChild(SpatialFormatIteratorAttributes attributes) {
        getBuffer().popElement();
    }

    public void writeCloseSpatialFormatIteratorRow(SpatialFormatIteratorAttributes attributes) {
        getBuffer().popElement();
    }

    public void writeOpenSpatialFormatIterator(SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("spatial", attributes.getStyles()));
        appendAttribute("title", attributes.getTitle());
    }

    public void writeOpenSpatialFormatIteratorChild(SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("spatialChild", attributes.getStyles()));
    }

    public void writeOpenSpatialFormatIteratorRow(SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("spatialRow", attributes.getStyles()));
    }

    public void writeFragmentLink(FraglinkAttributes attributes) throws IOException, ProtocolException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("fragmentLink", attributes.getStyles()));
    }

    public void writeOpenDissectingPane(DissectingPaneAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("dissectingPane", attributes.getStyles()));
        appendAttribute("backLinkText", attributes.getBackLinkText());
        appendAttribute("inclusionPath", attributes.getInclusionPath());
        appendAttribute("linkText", attributes.getLinkText());
        appendAttribute("title", attributes.getTitle());
    }

    public void writeCloseDissectingPane(DissectingPaneAttributes attributes) throws IOException {
        getBuffer().popElement();
    }

    public void writeOpenRowIteratorPane(RowIteratorPaneAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("rowIteratorPane", attributes.getStyles()));
    }

    public void writeCloseRowIteratorPane(RowIteratorPaneAttributes attrs) throws IOException {
        getBuffer().popElement();
    }

    public void writeOpenColumnIteratorPane(ColumnIteratorPaneAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("columnIteratorPane", attributes.getStyles()));
    }

    public void writeCloseColumnIteratorPane(ColumnIteratorPaneAttributes attrs) throws IOException {
        getBuffer().popElement();
    }

    public void writeOpenSegment(SegmentAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("segment", attributes.getStyles()));
        appendAttribute("borderColor", attributes.getBorderColor());
        appendAttribute("frameBorder", String.valueOf(attributes.getFrameBorder()));
        appendAttribute("marginHeight", String.valueOf(attributes.getMarginHeight()));
        appendAttribute("marginWidth", String.valueOf(attributes.getMarginWidth()));
        appendAttribute("name", attributes.getName());
        appendAttribute("scrolling", String.valueOf(attributes.getScrolling()));
        appendAttribute("title", attributes.getTitle());
    }

    public void writeOpenSegmentGrid(SegmentGridAttributes attributes) throws IOException {
        DOMOutputBuffer buffer = getBuffer();
        buffer.pushElement(buffer.allocateStyledElement("segmentGrid", attributes.getStyles()));
        appendAttribute("borderColor", attributes.getBorderColor());
        appendAttribute("borderWidth", String.valueOf(attributes.getBorderWidth()));
        appendAttribute("frameSpacing", String.valueOf(attributes.getFrameSpacing()));
        appendAttribute("title", attributes.getTitle());
    }

    public void writeCloseSegmentGrid(SegmentGridAttributes attrs) throws IOException {
        getBuffer().popElement();
    }

    public void writeCloseSegment(SegmentAttributes attrs) throws IOException {
        getBuffer().popElement();
    }

    /**
     * Appends an attribute to the output buffer if its value is not null.
     * @param attrName The name of the attribute
     * @param attrValue The value of the attribute
     */
    private void appendAttribute(String attrName, String attrValue) {
        if (attrValue != null) {
            DOMOutputBuffer buffer = getBuffer();
            Element ele = buffer.getCurrentElement();
            ele.setAttribute(attrName, attrValue);
        }
    }

    private DOMOutputBuffer getBuffer() {
        return (DOMOutputBuffer) getMarinerPageContext().getCurrentOutputBuffer();
    }

    public ValidationHelper getValidationHelper() {
        return null;
    }
}
