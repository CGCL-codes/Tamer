package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.LineBreakAttributes;

/**
 * A test implementation of {@link DeprecatedLineBreakOutput} which writes out
 * the line break attributes as simply as possible.
 */
public class TestDeprecatedLineBreakOutput implements DeprecatedLineBreakOutput {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    public void outputLineBreak(DOMOutputBuffer dom, LineBreakAttributes attributes) throws ProtocolException {
        dom.addStyledElement("br", attributes);
    }
}
