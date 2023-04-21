package org.icepdf.core.util;

import android.graphics.Color;

/**
 * <p>The <code>GraphicsRenderingHints</code> class provides a central place for
 * storing Java2D rendering hints settings.  The
 * <code>GraphicsRenderingHints</code> object is used to apply different rendering
 * hints for printing and screen presentation when rending a Page's content.</p>
 * <p/>
 * <p>The "screen" and "print" configuration are configurable with system properties.
 * See the <i>ICEpdf Developer's Guide</i>  for more information about configuring
 * these properites.</p>
 *
 * @author Mark Collette
 * @since 2.0
 */
public class GraphicsRenderingHints {

    /**
     * Constant used to specify rendering hint specific to screen rendering.
     */
    public static final int SCREEN = 1;

    /**
     * Constant used to specify rendering hint specific to print rendering.
     */
    public static final int PRINT = 2;

    /**
     * Gets the singleton representation of this object.
     *
     * @return a reference to the singleton GraphicsRenderingHints object.
     */
    public static synchronized GraphicsRenderingHints getDefault() {
        if (singleton == null) {
            singleton = new GraphicsRenderingHints();
        }
        return singleton;
    }

    private static GraphicsRenderingHints singleton;

    /**
     * Load values from the system properties if any and assign defaults.
     */
    private GraphicsRenderingHints() {
        setFromProperties();
    }

    public int getPageBackgroundColor(final int hintType) {
        if (hintType == SCREEN) return screenBackground; else return printBackground;
    }

    /**
     * Rereads the system properties responsible for setting the rendering hints
     * for both the PRINT and SCREEN modes.
     */
    public synchronized void reset() {
        setFromProperties();
    }

    /**
     * Utility method for reading the system properties.
     */
    private void setFromProperties() {
    }

    /**
     * This hints controls if the Page will paint a white background before drawing itself.
     * The default value is Color.white
     */
    int printBackground = Color.WHITE;

    ;

    /**
     * This hints controls if the Page will paint a white background before drawing itself.
     * The default value is Color.white
     */
    int screenBackground = Color.WHITE;

    ;
}
