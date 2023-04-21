package ren.util;

import java.awt.Color;

/**
 *
 * @author  Rene Wooller
 */
public class Colors {

    public static Color brighterBy(int amount, Color c) {
        int red = c.getRed() + amount;
        int green = c.getGreen() + amount;
        int blue = c.getBlue() + amount;
        red = setInBounds(red);
        green = setInBounds(green);
        blue = setInBounds(blue);
        return new Color(red, green, blue);
    }

    public static int setInBounds(int i) {
        return i > 255 ? 255 : (i = i < 0 ? 0 : i);
    }
}
