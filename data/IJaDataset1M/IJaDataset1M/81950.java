package org.pushingpixels.substance.internal.fonts;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Method;
import java.util.StringTokenizer;
import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;

/**
 * The default font policy for Gnome desktops.
 * 
 * @author Kirill Grouchnikov
 */
public class DefaultGnomeFontPolicy implements FontPolicy {

    /**
	 * Font scale.
	 */
    private static double fontScale;

    static {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        AffineTransform at = gc.getNormalizingTransform();
        fontScale = at.getScaleY();
    }

    public FontSet getFontSet(String lafName, UIDefaults table) {
        Object defaultGtkFontName = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Gtk/FontName");
        String family = "";
        int style = Font.PLAIN;
        int size = 10;
        if (defaultGtkFontName instanceof String) {
            String pangoName = (String) defaultGtkFontName;
            StringTokenizer tok = new StringTokenizer(pangoName);
            while (tok.hasMoreTokens()) {
                String word = tok.nextToken();
                boolean allDigits = true;
                for (int i = 0; i < word.length(); i++) {
                    if (!Character.isDigit(word.charAt(i))) {
                        allDigits = false;
                        break;
                    }
                }
                if (word.equalsIgnoreCase("italic")) {
                    style |= Font.ITALIC;
                } else if (word.equalsIgnoreCase("bold")) {
                    style |= Font.BOLD;
                } else if (allDigits) {
                    try {
                        size = Integer.parseInt(word);
                    } catch (NumberFormatException nfe) {
                        size = 10;
                    }
                } else {
                    if (family.length() > 0) {
                        family += " ";
                    }
                    family += word;
                }
            }
        }
        double dsize = size * getPointsToPixelsRatio();
        size = (int) (dsize + 0.5);
        if (size < 1) {
            size = 1;
        }
        if (family.length() == 0) family = "sans";
        Font controlFont = null;
        String fcFamilyLC = family.toLowerCase();
        try {
            Class fontManagerClass = Class.forName("sun.font.FontManager");
            Method mapFcMethod = fontManagerClass.getMethod("mapFcName", new Class[] { String.class });
            Object mapFcMethodResult = mapFcMethod.invoke(null, fcFamilyLC);
            if (mapFcMethodResult != null) {
                Method getFontConfigFUIRMethod = fontManagerClass.getMethod("getFontConfigFUIR", new Class[] { String.class, int.class, int.class });
                controlFont = (Font) getFontConfigFUIRMethod.invoke(null, fcFamilyLC, style, size);
            } else {
                Font font = new FontUIResource(family, style, size);
                Method getCompositeFontUIResourceMethod = fontManagerClass.getMethod("getCompositeFontUIResource", new Class[] { Font.class });
                controlFont = (Font) getCompositeFontUIResourceMethod.invoke(null, font);
            }
        } catch (Throwable t) {
            controlFont = new Font(family, style, size);
        }
        return FontSets.createDefaultFontSet(controlFont);
    }

    public static double getPointsToPixelsRatio() {
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
            return dpi / 72.0;
        } else {
            return fontScale;
        }
    }
}
