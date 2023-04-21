package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * A very simple implementation of separator renderer.
 * <p>
 * All it does is render a custom string which should be easy to check for
 * in a unit test.  
 */
public class TestSimpleSeparatorRenderer implements SeparatorRenderer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The custom content of the separator to render.
     */
    private String content;

    /**
     * Construct an instance of this class.
     * 
     * @param content the custom content of the separator to render.
     */
    public TestSimpleSeparatorRenderer(String content) {
        this.content = content;
    }

    public void render(OutputBuffer buffer) throws RendererException {
        buffer.writeText(content);
    }
}
