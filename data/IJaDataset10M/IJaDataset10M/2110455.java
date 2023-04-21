package com.volantis.mcs.protocols.renderer.shared.layouts.unit;

import com.volantis.mcs.layouts.IteratorPaneMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.AttributesTestHelper;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneAttributesMock;
import com.volantis.mcs.protocols.RowIteratorPaneAttributesMock;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.layouts.IteratorPaneInstanceMock;
import com.volantis.mcs.protocols.layouts.RowIteratorPaneInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.unit.IteratorPaneRendererTestAbstract;
import com.volantis.mcs.protocols.renderer.shared.layouts.RowIteratorPaneRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.method.CallUpdaterReturnsVoid;

/**
 * Tests rendering functionality of default row iterator renderer.
 */
public class RowIteratorPaneRendererTestCase extends IteratorPaneRendererTestAbstract {

    protected IteratorPaneMock.Expects createIteratorPaneMockExpects() {
        return LayoutTestHelper.createRowIteratorPaneMock("rowIteratorPane", expectations, canvasLayoutMock).expects;
    }

    protected IteratorPaneInstanceMock.Expects createIteratorPaneInstanceExpects(NDimensionalIndex index) {
        RowIteratorPaneInstanceMock paneInstanceMock = new RowIteratorPaneInstanceMock("paneInstanceMock", expectations, index);
        return paneInstanceMock.expects;
    }

    protected PaneAttributesMock.Expects createPaneAttributeExpects() throws Exception {
        final RowIteratorPaneAttributesMock paneAttributesMock = (RowIteratorPaneAttributesMock) AttributesTestHelper.createMockAttributes(RowIteratorPaneAttributesMock.class, "pane", "paneAttributesMock", expectations);
        return paneAttributesMock.expects;
    }

    protected FormatRenderer createFormatRenderer() {
        return new RowIteratorPaneRenderer();
    }

    protected void expectsWriteOpenPane(final PaneAttributes paneAttributesMock) {
        layoutModuleMock.expects.writeOpenRowIteratorPane((RowIteratorPaneAttributes) paneAttributesMock);
    }

    protected void expectsWriteClosePane(final PaneAttributes paneAttributesMock) {
        layoutModuleMock.expects.writeCloseRowIteratorPane((RowIteratorPaneAttributes) paneAttributesMock);
    }

    protected void expectsWriteOpenPaneElement(final PaneAttributes paneAttributesMock) {
        layoutModuleMock.fuzzy.writeOpenRowIteratorPaneElement(mockFactory.expectsInstanceOf(RowIteratorPaneAttributes.class));
    }

    protected void expectsWritePaneElementContents(final OutputBufferMock buffer1) {
        layoutModuleMock.expects.writeRowIteratorPaneElementContents(buffer1);
    }

    protected void expectsWriteClosePaneElement(final PaneAttributes paneAttributesMock) {
        layoutModuleMock.fuzzy.writeCloseRowIteratorPaneElement(mockFactory.expectsInstanceOf(RowIteratorPaneAttributes.class));
    }
}
