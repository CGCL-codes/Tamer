package net.exclaimindustries.tools;

import java.awt.Color;

/**
 * Simply provides a static method to convert a Color object into its HTML
 * representation.
 *
 * @author Nicholas Killewald
 */
public class HTMLColor {

    /**
     * Returns the color contained by a Color object into an HTML-friendly format.
     * That is, it splits the RGB components into three hex values and returns
     * that.  Note this will supply a # prefix.
     *
     * @param color Color to extract data from
     * @return a hex representation of the RGB values of the given Color
     */
    public static String getHTMLColorFrom(Color color) {
        return "#" + Integer.toHexString(color.getRed()) + Integer.toHexString(color.getGreen()) + Integer.toHexString(color.getBlue());
    }

    /**
     * Returns the color and alpha contained by a Color object into an HTML-friendly
     * format.  That is, it splits the RGB components into four hex values (fourth
     * being alpha) and returns that.  Note this will supply a # prefix.
     *
     * @param color Color to extract data from
     * @return a hex representation of the RGBA values of the given Color
     */
    public static String getHTMLColorAndAlphaFrom(Color color) {
        return "#" + Integer.toHexString(color.getRed()) + Integer.toHexString(color.getGreen()) + Integer.toHexString(color.getBlue()) + Integer.toHexString(color.getAlpha());
    }
}
