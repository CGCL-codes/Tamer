package com.hifi.core.utils;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The Class ColorUtil.
 */
public class ColorUtils {

    private static final Map<String, Color> colors = new WeakHashMap<String, Color>();

    private ColorUtils() {
    }

    /**
	 * Gets the color.
	 * 
	 * @param aColor the a color
	 * 
	 * @return the color
	 */
    public static Color getColor(String aColor) {
        String vKey = aColor;
        Color vColor = (Color) colors.get(vKey);
        if (vColor == null) {
            try {
                Field vField = Color.class.getField(aColor);
                vColor = (Color) vField.get(null);
            } catch (Throwable e) {
                vColor = parseHtmlColor(aColor);
            }
            colors.put(vKey, vColor);
        }
        return vColor;
    }

    /**
	 * Parses the html color.
	 * 
	 * @param aColor the a color
	 * 
	 * @return the color
	 */
    public static Color parseHtmlColor(String aColor) {
        if (aColor.startsWith("#")) {
            aColor = aColor.substring(1);
        }
        aColor = aColor.toLowerCase();
        if (aColor.length() > 6) {
            throw new RuntimeException("[" + aColor + "] is not a 24 bit representation of the color");
        }
        Color vColor = new Color(Integer.parseInt(aColor, 16));
        return vColor;
    }
}
