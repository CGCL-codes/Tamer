package com.sun.java.swing.plaf.gtk;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.plaf.FontUIResource;
import java.util.StringTokenizer;
import sun.font.FontManager;

/**
 * @author Shannon Hickey
 * @author Leif Samuelsson
 * @version 1.17 03/15/07
 */
class PangoFonts {

    public static final String CHARS_DIGITS = "0123456789";

    /**
     * Calculate a default scale factor for fonts in this L&F to match
     * the reported resolution of the screen.
     * Java 2D specified a default user-space scale of 72dpi.
     * This is unlikely to correspond to that of the real screen.
     * The Xserver reports a value which may be used to adjust for this.
     * and Java 2D exposes it via a normalizing transform.
     * However many Xservers report a hard-coded 90dpi whilst others report a
     * calculated value based on possibly incorrect data.
     * That is something that must be solved at the X11 level
     * Note that in an X11 multi-screen environment, the default screen
     * is the one used by the JRE so it is safe to use it here.
     */
    private static double fontScale;

    static {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        AffineTransform at = gc.getNormalizingTransform();
        fontScale = at.getScaleY();
    }

    /**
     * Parses a String containing a pango font description and returns
     * a Font object.
     *
     * @param pangoName a String describing a pango font
     *                  e.g. "Sans Italic 10"
     * @return a Font object as a FontUIResource
     *         or null if no suitable font could be created.
     */
    static Font lookupFont(String pangoName) {
        String family = "";
        int style = Font.PLAIN;
        int size = 10;
        StringTokenizer tok = new StringTokenizer(pangoName);
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();
            if (word.equalsIgnoreCase("italic")) {
                style |= Font.ITALIC;
            } else if (word.equalsIgnoreCase("bold")) {
                style |= Font.BOLD;
            } else if (CHARS_DIGITS.indexOf(word.charAt(0)) != -1) {
                try {
                    size = Integer.parseInt(word);
                } catch (NumberFormatException ex) {
                }
            } else {
                if (family.length() > 0) {
                    family += " ";
                }
                family += word;
            }
        }
        double dsize = size;
        int dpi = 96;
        Object value = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Xft/DPI");
        if (value instanceof Integer) {
            dpi = ((Integer) value).intValue() / 1024;
            if (dpi == -1) {
                dpi = 96;
            }
            if (dpi < 50) {
                dpi = 50;
            }
            dsize = ((double) (dpi * size) / 72.0);
        } else {
            dsize = size * fontScale;
        }
        size = (int) (dsize + 0.5);
        if (size < 1) {
            size = 1;
        }
        String fcFamilyLC = family.toLowerCase();
        if (FontManager.mapFcName(fcFamilyLC) != null) {
            return FontManager.getFontConfigFUIR(fcFamilyLC, style, size);
        } else {
            Font font = new FontUIResource(family, style, size);
            return FontManager.getCompositeFontUIResource(font);
        }
    }

    /**
     * Parses a String containing a pango font description and returns
     * the (unscaled) font size as an integer.
     *
     * @param pangoName a String describing a pango font
     * @return the size of the font described by pangoName (e.g. if
     *         pangoName is "Sans Italic 10", then this method returns 10)
     */
    static int getFontSize(String pangoName) {
        int size = 10;
        StringTokenizer tok = new StringTokenizer(pangoName);
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();
            if (CHARS_DIGITS.indexOf(word.charAt(0)) != -1) {
                try {
                    size = Integer.parseInt(word);
                } catch (NumberFormatException ex) {
                }
            }
        }
        return size;
    }
}
