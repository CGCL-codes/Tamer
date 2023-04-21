package net.jforerunning.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.util.Map;

/**
 * As the class name suggests, a factory for fonts.
 * It provides access to the default fonts and a method for deriving fonts of
 * new color / weight from an existing font.
 * 
 * Usually it is not necessary to access this class directly, as e.g.
 * the DefaultLabel class has methods to select various fonts.
 * 
 * @author jens
 *
 */
public class FontFactory {

    public static final float KEEP_WEIGHT = -1;

    public static final Color KEEP_COLOR = null;

    private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);

    private static Font sectionFont = null;

    /**
	 * Get default font (for labels)
	 * @return default font
	 */
    public static Font getDefaultFont() {
        return DEFAULT_FONT;
    }

    /**
	 * Get the default font for section headings
	 * @return default font for section headings
	 */
    public static Font getSectionHeadingFont() {
        if (sectionFont == null) {
            sectionFont = new Font("Dialog", Font.BOLD, 15);
            Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
            map.put(TextAttribute.FOREGROUND, new Color(0, 0, 200));
            sectionFont = sectionFont.deriveFont(map);
        }
        return sectionFont;
    }

    /**
	 * Derive a new font from the given font f
	 * @param f Font to base the new font on
	 * @param color Color of the new font, or KEEP_COLOR constant
	 * @param weight Weight of the new font, or KEEP_WEIGHT constant
	 * @return the derived font
	 */
    public static Font deriveColoredFont(Font f, Color color, float weight) {
        Map<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
        if (color != KEEP_COLOR) map.put(TextAttribute.FOREGROUND, color);
        if (weight != KEEP_WEIGHT) map.put(TextAttribute.WEIGHT, weight);
        return f.deriveFont(map);
    }
}
