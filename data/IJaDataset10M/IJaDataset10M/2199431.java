package org.dishevelled.text.pswing;

import java.awt.Graphics2D;
import static java.awt.RenderingHints.*;

/**
 * PSwing with KEY_TEXT_ANTIALIASING set to VALUE_TEXT_ANTIALIAS_LCD_VBGR.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class PSwingAntialiasingLcdVbgr extends AbstractPSwingTextRenderingBenchmark {

    /** {@inheritDoc} */
    public String getName() {
        return "pswing-lcd-vbgr";
    }

    /** {@inheritDoc} */
    protected void prepareGraphics(final Graphics2D graphics) {
        graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_LCD_VBGR);
    }
}
