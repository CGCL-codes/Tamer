package org.dishevelled.text.java2d;

import java.awt.Graphics2D;
import static java.awt.RenderingHints.*;

/**
 * Java2D with KEY_TEXT_ANTIALIASING set to VALUE_TEXT_ANTIALIAS_LCD_HRGB.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class Java2DAntialiasingLcdHrgb extends AbstractJava2DTextRenderingBenchmark {

    /** {@inheritDoc} */
    public String getName() {
        return "java2d-lcd-hrgb";
    }

    /** {@inheritDoc} */
    protected void prepareGraphics(final Graphics2D graphics) {
        graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }
}
