package com.volantis.mcs.protocols.html;

import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import java.util.HashMap;
import java.util.Map;

/**
 * Implement the testable methods to create a testable Protocol.
 * <p>
 * We need to ensure that all implementations of these methods are the
 * same so the use of cut & paste here is recommended!
 * <p>
 * All the javadoc for these methods is defined in the interfaces.
 */
public class TestHTML_iMode extends HTML_iMode implements XHTMLBasicTestable {

    public TestHTML_iMode(ProtocolSupportFactory supportFactory, ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }

    public void setPageHead(PageHead value) {
        this.pageHead = value;
    }

    public void setStyleSheetRenderer(StyleSheetRenderer renderer) {
        this.styleSheetRenderer = renderer;
    }

    public void setPageBuffer(DOMOutputBuffer pageBuffer) {
        this.pageBuffer = pageBuffer;
    }

    private Map containerInstanceBufferMap = new HashMap();

    public void setCurrentBuffer(ContainerInstance containerInstance, DOMOutputBuffer buffer) {
        containerInstanceBufferMap.put(containerInstance, buffer);
    }

    protected DOMOutputBuffer getCurrentBuffer(ContainerInstance containerInstance) {
        DOMOutputBuffer buffer = (DOMOutputBuffer) containerInstanceBufferMap.get(containerInstance);
        return (null != buffer) ? buffer : super.getCurrentBuffer(containerInstance);
    }

    public int getMaxOptGroupNestingDepth() {
        return maxOptgroupDepth;
    }

    public void renderMenuItemSeparator(DOMOutputBuffer dom, String separatorCharacters, int separatorRepeat) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < separatorRepeat; i++) {
            buf.append(separatorCharacters);
        }
        dom.appendEncoded(buf.toString());
    }

    public void renderMenuItemSeparator(DOMOutputBuffer dom, ImageAttributes attributes) {
        dom.appendEncoded(attributes.getSrc());
    }
}
