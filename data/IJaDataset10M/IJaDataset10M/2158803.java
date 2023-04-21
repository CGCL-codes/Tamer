package org.apache.batik.css.engine;

import java.net.URL;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueConstants;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.engine.value.svg.SVGColorManager;
import org.apache.batik.css.engine.value.svg.OpacityManager;
import org.apache.batik.css.engine.value.svg12.LineHeightManager;
import org.apache.batik.css.engine.value.svg12.MarginLengthManager;
import org.apache.batik.css.engine.value.svg12.MarginShorthandManager;
import org.apache.batik.css.engine.value.svg12.TextAlignManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.util.SVG12CSSConstants;
import org.w3c.dom.Document;

/**
 * This class provides a CSS engine initialized for SVG.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVG12CSSEngine.java,v 1.1 2005/11/21 09:51:34 dev Exp $
 */
public class SVG12CSSEngine extends SVGCSSEngine {

    /**
     * Creates a new SVGCSSEngine.
     * @param doc The associated document.
     * @param uri The document URI.
     * @param p The CSS parser to use.
     * @param ctx The CSS context.
     */
    public SVG12CSSEngine(Document doc, URL uri, ExtendedParser p, CSSContext ctx) {
        super(doc, uri, p, SVG_VALUE_MANAGERS, SVG_SHORTHAND_MANAGERS, ctx);
        lineHeightIndex = LINE_HEIGHT_INDEX;
    }

    /**
     * Creates a new SVGCSSEngine.
     * @param doc The associated document.
     * @param uri The document URI.
     * @param p The CSS parser to use.
     * @param vms Extension value managers.
     * @param sms Extension shorthand managers.
     * @param ctx The CSS context.
     */
    public SVG12CSSEngine(Document doc, URL uri, ExtendedParser p, ValueManager[] vms, ShorthandManager[] sms, CSSContext ctx) {
        super(doc, uri, p, mergeArrays(SVG_VALUE_MANAGERS, vms), mergeArrays(SVG_SHORTHAND_MANAGERS, sms), ctx);
        lineHeightIndex = LINE_HEIGHT_INDEX;
    }

    /**
     * The value managers for SVG.
     */
    public static final ValueManager[] SVG_VALUE_MANAGERS = { new LineHeightManager(), new MarginLengthManager(SVG12CSSConstants.CSS_INDENT_PROPERTY), new MarginLengthManager(SVG12CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY), new MarginLengthManager(SVG12CSSConstants.CSS_MARGIN_LEFT_PROPERTY), new MarginLengthManager(SVG12CSSConstants.CSS_MARGIN_RIGHT_PROPERTY), new MarginLengthManager(SVG12CSSConstants.CSS_MARGIN_TOP_PROPERTY), new SVGColorManager(SVG12CSSConstants.CSS_SOLID_COLOR_PROPERTY), new OpacityManager(SVG12CSSConstants.CSS_SOLID_OPACITY_PROPERTY, true), new TextAlignManager() };

    /**
     * The shorthand managers for SVG.
     */
    public static final ShorthandManager[] SVG_SHORTHAND_MANAGERS = { new MarginShorthandManager() };

    public static final int LINE_HEIGHT_INDEX = SVGCSSEngine.FINAL_INDEX + 1;

    public static final int INDENT_INDEX = LINE_HEIGHT_INDEX + 1;

    public static final int MARGIN_BOTTOM_INDEX = INDENT_INDEX + 1;

    public static final int MARGIN_LEFT_INDEX = MARGIN_BOTTOM_INDEX + 1;

    public static final int MARGIN_RIGHT_INDEX = MARGIN_LEFT_INDEX + 1;

    public static final int MARGIN_TOP_INDEX = MARGIN_RIGHT_INDEX + 1;

    public static final int SOLID_COLOR_INDEX = MARGIN_TOP_INDEX + 1;

    public static final int SOLID_OPACITY_INDEX = SOLID_COLOR_INDEX + 1;

    public static final int TEXT_ALIGN_INDEX = SOLID_OPACITY_INDEX + 1;

    public static final int FINAL_INDEX = TEXT_ALIGN_INDEX;
}
