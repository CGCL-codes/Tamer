package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.context.OutputBufferStack;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.RegionContent;
import com.volantis.mcs.protocols.RendererTestProtocol;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;

/**
 * Tests rendering functionality of default region renderer.
 *
 * @todo Remove when code coverage indicates that unit tests have same coverage.
 */
public class RegionRendererTestCase extends AbstractFormatRendererTestAbstract {

    /**
     * Tests that attributes are passed through correctly.
     * @throws Exception if an error occurs
     */
    public void testAttributesPassed() throws Exception {
        RendererTestProtocol protocol = new RendererTestProtocol(pageContext);
        pageContext.setProtocol(protocol);
        Region region = new Region((CanvasLayout) layout);
        RegionInstance instance = (RegionInstance) dlContext.getFormatInstance(region, NDimensionalIndex.ZERO_DIMENSIONS);
        RegionContent content = new RegionContent() {

            public void render(FormatRendererContext ctx) {
                OutputBufferStack outputBufferStack = ctx.getOutputBufferStack();
                DOMOutputBuffer buffer = (DOMOutputBuffer) outputBufferStack.getCurrentOutputBuffer();
                buffer.openElement(buffer.allocateElement("regionContent"));
                buffer.closeElement("regionContent");
            }
        };
        RegionContent content2 = new RegionContent() {

            public void render(FormatRendererContext ctx) {
                OutputBufferStack outputBufferStack = ctx.getOutputBufferStack();
                DOMOutputBuffer buffer = (DOMOutputBuffer) outputBufferStack.getCurrentOutputBuffer();
                buffer.openElement(buffer.allocateElement("regionContent2"));
                buffer.closeElement("regionContent2");
            }
        };
        instance.addRegionContent(content);
        instance.addRegionContent(content2);
        FormatRenderer renderer = new RegionRenderer();
        renderer.render(formatRendererContext, instance);
        String expected = "<regionContent/><regionContent2/>";
        compareStringToBuffer(expected, pageContext.getCurrentOutputBuffer());
    }

    /**
     * Test that blank lines added to a region are discarded.
     *
     * @throws Exception
     */
    public void testEmptyBuffersSkipped() throws Exception {
        RendererTestProtocol protocol = new RendererTestProtocol(pageContext);
        pageContext.setProtocol(protocol);
        Region region = new Region((CanvasLayout) layout);
        RegionInstance instance = (RegionInstance) dlContext.getFormatInstance(region, NDimensionalIndex.ZERO_DIMENSIONS);
        RegionContent content = new RegionContent() {

            public void render(FormatRendererContext ctx) {
                OutputBufferStack outputBufferStack = ctx.getOutputBufferStack();
                DOMOutputBuffer buffer = (DOMOutputBuffer) outputBufferStack.getCurrentOutputBuffer();
                buffer.openElement(buffer.allocateElement("regionContent"));
                buffer.closeElement("regionContent");
            }
        };
        instance.getCurrentBuffer().writeText("   \n");
        instance.addRegionContent(content);
        instance.getCurrentBuffer().writeText("   \n");
        FormatRenderer renderer = new RegionRenderer();
        renderer.render(formatRendererContext, instance);
        String expected = "<regionContent/>";
        compareStringToBuffer(expected, pageContext.getCurrentOutputBuffer());
    }
}
