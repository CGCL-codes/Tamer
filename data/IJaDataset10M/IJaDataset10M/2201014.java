package proguard.gui.splash;

import java.awt.Color;

/**
 * This interface represents a Color that varies with time.
 *
 * @author Eric Lafortune
 */
interface VariableColor {

    /**
     * Returns the Color for the given time.
     */
    public Color getColor(long time);
}
