package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import java.io.IOException;

/**
 * A simple testing menu item buffer locator which returns the same menu 
 * buffer for all menu items.
 * <p>
 * This creates and manages the underlying menu buffer and output buffer and 
 * allows easy access to any rendered output.
 */
public class TestMenuBufferLocator implements MenuBufferLocator {

    private DOMOutputBuffer outputBuffer;

    private MenuBuffer buffer;

    /**
     * Construct an instance of this class.
     * <p>
     * This will create the underlying menu and output buffer that this test
     * class manage on your behalf. 
     */
    public TestMenuBufferLocator(SeparatorRenderer orientationRenderer) {
        outputBuffer = new TestDOMOutputBuffer();
        buffer = new ConcreteMenuBuffer(outputBuffer, orientationRenderer);
    }

    public MenuBuffer getMenuBuffer(MenuEntry entry) {
        return buffer;
    }

    /**
     * Return the underlying output buffer of the menu buffer this locator 
     * is managing.
     * 
     * @return the contained output buffer.
     */
    public DOMOutputBuffer getOutputBuffer() {
        return outputBuffer;
    }

    /**
     * Return the content of the output buffer of the menu buffer this locator
     * is managing as a string.
     * 
     * @return the contained output buffer's content as text.
     * @throws IOException
     */
    public String getOutput() throws IOException {
        return DOMUtilities.toString(getOutputBuffer().getRoot());
    }
}
