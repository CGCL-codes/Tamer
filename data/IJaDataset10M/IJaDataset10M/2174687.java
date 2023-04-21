package org.makagiga.tools.highcontrast;

import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * @since 3.0
 */
public final class HighContrastLookAndFeel extends MetalLookAndFeel {

    public HighContrastLookAndFeel() {
        setCurrentTheme(new HighContrastTheme());
    }
}
