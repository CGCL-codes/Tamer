package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.layouts.ContainerInstance;

/**
 * Implement the testable methods to create a testable Protocol.
 * <p>
 * We need to ensure that all implementations of these methods are the
 * same so the use of cut & paste here is recommended!
 * <p>
 * All the javadoc for these methods is defined in the interfaces.
 */
public class TestWMLVersion1_1 extends WMLVersion1_1 implements WMLRootTestable {

    public TestWMLVersion1_1(ProtocolSupportFactory supportFactory, ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
        createStyleEmulationRenderer();
    }

    public void setPageHead(PageHead value) {
        this.pageHead = value;
    }

    public void setSupportsAccessKeyAttribute(boolean value) {
        this.supportsAccessKeyAttribute = value;
    }

    public void setPageBuffer(DOMOutputBuffer pageBuffer) {
        this.pageBuffer = pageBuffer;
    }

    public void setStyleSheetRenderer(StyleSheetRenderer renderer) {
        this.styleSheetRenderer = renderer;
    }

    private DOMOutputBuffer fakeCurrentContainerBuffer;

    public void setCurrentBuffer(ContainerInstance containerInstance, DOMOutputBuffer buffer) {
        this.fakeCurrentContainerBuffer = buffer;
    }

    protected DOMOutputBuffer getCurrentBuffer(ContainerInstance containerInstance) {
        return this.fakeCurrentContainerBuffer;
    }

    public OutputBufferFactory getOutputBufferFactory() {
        return new TestDOMOutputBufferFactory();
    }
}
