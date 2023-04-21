package org.timepedia.chronoscope.client.render;

import org.timepedia.chronoscope.client.canvas.Layer;
import org.timepedia.chronoscope.client.gss.GssProperties;

/**
 * Provides string pixel dimensions to a client.
 * @author chad takahashi
 */
public class StringSizer {

    public static int getRotatedHeight(Layer layer, String s, GssProperties gss, double rotationAngle) {
        return layer.rotatedStringHeight(s, rotationAngle, gss.fontFamily, gss.fontWeight, gss.fontSize);
    }

    public static int getHeight(Layer layer, String s, GssProperties gss) {
        return layer.stringHeight(s, gss.fontFamily, gss.fontWeight, gss.fontSize);
    }

    public static int getRotatedWidth(Layer layer, String s, GssProperties gss, double rotationAngle) {
        return layer.rotatedStringWidth(s, rotationAngle, gss.fontFamily, gss.fontWeight, gss.fontSize);
    }

    public static int getWidth(Layer layer, String s, GssProperties gss) {
        return layer.stringWidth(s, gss.fontFamily, gss.fontWeight, gss.fontSize);
    }

    /**
   * Remove characters at the end of the string which overflow the given width.
   */
    public static String wrapText(Layer layer, String s, GssProperties gss, double maxWidth) {
        String r = "";
        for (int i = 0; i <= s.length() && getWidth(layer, r, gss) < maxWidth; i++) {
            r = s.substring(0, i);
        }
        return r;
    }
}
