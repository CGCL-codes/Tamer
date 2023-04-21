package com.volantis.mcs.protocols.css.emulator;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.css.renderer.RuntimeRendererContext;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;
import java.io.IOException;

/**
 * The context in which the rendering of style sheet and its component
 * parts takes place.
 */
public class EmulatorRendererContext extends RuntimeRendererContext {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(EmulatorRendererContext.class);

    /**
     * Construct a RendererContext that will write the renderer style sheet
     * to a given Writer utilizing the given StyleSheetRenderer.
     *
     * @param requestContext
     * @param renderer       the StyleSheetRenderer
     * @param cssVersion
     */
    public EmulatorRendererContext(MarinerRequestContext requestContext, StyleSheetRenderer renderer, VolantisProtocol protocol, CSSVersion cssVersion) {
        super(null, renderer, protocol, cssVersion);
        this.valuesRenderer = new EmulatorValuesRenderer(this.valuesRenderer);
    }

    /**
     * Write out the contents of the writer that is used to write values into
     * the RendererContext to the writer that the RendererContext writes out
     * to(i.e. write the proxy writer to the real writer).
     *
     * @throws IOException if a problem is encountered in the write operation
     */
    public void flushStyleSheet() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Render a Selector.
     *
     * @param selector the Selector to render
     * @throws IOException if there is a problem rendering to the writer.
     */
    public void renderSelector(Selector selector) throws IOException {
        throw new UnsupportedOperationException();
    }

    public ReusableStringBuffer getRenderValue(StyleValue value) {
        if (value == null || value.getStyleValueType() == StyleValueType.INHERIT) {
            return null;
        }
        try {
            ReusableStringWriter stringWriter = (ReusableStringWriter) getWriter();
            ReusableStringBuffer buffer = stringWriter.getBuffer();
            buffer.setLength(0);
            renderValue(value);
            if (buffer.length() > 0) {
                return buffer;
            } else {
                return null;
            }
        } catch (IOException ioe) {
            logger.warn("style-value-retrieval-error");
            return null;
        }
    }

    public void renderValue(StyleValue value, Element element, String attributeName) {
        ReusableStringBuffer buffer = getRenderValue(value);
        if (buffer != null) {
            element.setAttribute(attributeName, buffer.toString());
        }
    }
}
