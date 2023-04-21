package org.eoti.awt;

import java.awt.*;

public class ColorUtil {

    public static WebColor getWebColor(Color source) {
        for (WebColor wc : WebColor.values()) {
            if (wc.getColor().equals(source)) return wc;
        }
        return null;
    }

    public static WebColor getWebColor(String name) throws IllegalArgumentException {
        try {
            return Enum.valueOf(WebColor.class, name);
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException("No Such Color: " + name, iae);
        }
    }

    public static Color makeTransparent(Color source, int alpha) {
        return new Color(source.getRed(), source.getGreen(), source.getBlue(), alpha);
    }

    public static Color getColor(String name) throws IllegalArgumentException {
        WebColor webColor = getWebColor(name);
        return webColor.getColor();
    }
}
