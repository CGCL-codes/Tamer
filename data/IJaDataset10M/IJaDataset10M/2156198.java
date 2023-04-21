package com.volantis.mcs.eclipse.common;

import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.values.converters.ColorConverter;
import org.eclipse.swt.graphics.RGB;
import java.awt.SystemColor;
import java.util.HashMap;
import java.util.Map;

/**
 * Typesafe enumeration for stylesheet colours. Each color has a
 * name and a hex value.
 *
 * The supported colours are the CSS-defined standard and
 * system colors.
 *
 * @todo Stop returning effing arrays.
 */
public class NamedColor {

    private static final Map NAME_2_SYSTEM_COLOR;

    static {
        Map map = new HashMap();
        map.put(StyleColorNames.ACTIVE_BORDER, SystemColor.activeCaptionBorder);
        map.put(StyleColorNames.ACTIVE_CAPTION, SystemColor.activeCaption);
        map.put(StyleColorNames.APP_WORKSPACE, SystemColor.window);
        map.put(StyleColorNames.BACKGROUND, SystemColor.desktop);
        map.put(StyleColorNames.BUTTON_FACE, SystemColor.control);
        map.put(StyleColorNames.BUTTON_HIGHLIGHT, SystemColor.controlLtHighlight);
        map.put(StyleColorNames.BUTTON_SHADOW, SystemColor.controlShadow);
        map.put(StyleColorNames.BUTTON_TEXT, SystemColor.controlText);
        map.put(StyleColorNames.CAPTION_TEXT, SystemColor.activeCaptionText);
        map.put(StyleColorNames.GRAY_TEXT, SystemColor.textInactiveText);
        map.put(StyleColorNames.HIGHLIGHT, SystemColor.textHighlight);
        map.put(StyleColorNames.HIGHLIGHT_TEXT, SystemColor.textHighlightText);
        map.put(StyleColorNames.INACTIVE_BORDER, SystemColor.inactiveCaptionBorder);
        map.put(StyleColorNames.INACTIVE_CAPTION, SystemColor.inactiveCaption);
        map.put(StyleColorNames.INACTIVE_CAPTION_TEXT, SystemColor.inactiveCaptionText);
        map.put(StyleColorNames.INFO_BACKGROUND, SystemColor.info);
        map.put(StyleColorNames.INFO_TEXT, SystemColor.infoText);
        map.put(StyleColorNames.MENU, SystemColor.menu);
        map.put(StyleColorNames.MENU_TEXT, SystemColor.menuText);
        map.put(StyleColorNames.SCROLLBAR, SystemColor.scrollbar);
        map.put(StyleColorNames.THREE_DDARK_SHADOW, SystemColor.controlDkShadow);
        map.put(StyleColorNames.THREE_DFACE, SystemColor.control);
        map.put(StyleColorNames.THREE_DHIGHLIGHT, SystemColor.controlHighlight);
        map.put(StyleColorNames.THREE_DLIGHT_SHADOW, SystemColor.controlShadow);
        map.put(StyleColorNames.THREE_DSHADOW, SystemColor.controlShadow);
        map.put(StyleColorNames.WINDOW, SystemColor.window);
        map.put(StyleColorNames.WINDOW_FRAME, SystemColor.windowBorder);
        map.put(StyleColorNames.WINDOW_TEXT, SystemColor.windowText);
        NAME_2_SYSTEM_COLOR = map;
    }

    /**
     * The color name.
     */
    private String colorName;

    /**
     * The hex value: a three or six digit hex
     * beginning with a hash (#)
     */
    private String hex;

    /**
     * Private constructor for typesafe enum.
     * @param name the StyleColorName that this supports.
     */
    public NamedColor(StyleColorName name) {
        this.colorName = name.getName();
        StyleColorRGB standardRGB = name.getRGB();
        if (standardRGB == null) {
            SystemColor systemColor = (SystemColor) NAME_2_SYSTEM_COLOR.get(name);
            hex = awtColorToHex(systemColor);
        } else {
            hex = ColorConverter.convertColorRGBToText(standardRGB.getRGB(), false).toUpperCase();
        }
    }

    /**
     * Return the name for this color
     *
     * @return standard css name of this color
     */
    public String getName() {
        return colorName;
    }

    /**
     * Get the hex value for the color name. The named color
     * must be a member of all colors.
     * @param name the color name
     * @return the hex of the color called name, or null if
     * the color name does not exist
     */
    public static String getHex(String name) {
        boolean foundColor = false;
        String hex = null;
        for (int i = 0; i < allColors.length && !foundColor; i++) {
            if (allColors[i].colorName.equals(name)) {
                foundColor = true;
                hex = allColors[i].hex;
            }
        }
        return hex;
    }

    /**
     * The hex value of the color.
     * @return the hex value
     */
    public String getHex() {
        return this.hex;
    }

    /**
     * The standard colors. The colors intentionally begin with a lower case
     * letter as the will be used by to create a CSS style sheet
     */
    public static final NamedColor AQUA = new NamedColor(StyleColorNames.AQUA);

    public static final NamedColor BLACK = new NamedColor(StyleColorNames.BLACK);

    public static final NamedColor BLUE = new NamedColor(StyleColorNames.BLUE);

    public static final NamedColor FUCHSIA = new NamedColor(StyleColorNames.FUCHSIA);

    public static final NamedColor GRAY = new NamedColor(StyleColorNames.GRAY);

    public static final NamedColor GREEN = new NamedColor(StyleColorNames.GREEN);

    public static final NamedColor LIME = new NamedColor(StyleColorNames.LIME);

    public static final NamedColor MAROON = new NamedColor(StyleColorNames.MAROON);

    public static final NamedColor NAVY = new NamedColor(StyleColorNames.NAVY);

    public static final NamedColor OLIVE = new NamedColor(StyleColorNames.OLIVE);

    public static final NamedColor PURPLE = new NamedColor(StyleColorNames.PURPLE);

    public static final NamedColor RED = new NamedColor(StyleColorNames.RED);

    public static final NamedColor SILVER = new NamedColor(StyleColorNames.SILVER);

    public static final NamedColor TEAL = new NamedColor(StyleColorNames.TEAL);

    public static final NamedColor WHITE = new NamedColor(StyleColorNames.WHITE);

    public static final NamedColor YELLOW = new NamedColor(StyleColorNames.YELLOW);

    public static final NamedColor ORANGE = new NamedColor(StyleColorNames.ORANGE);

    /**
     * The system colors.  Again case sensitive
     */
    public static final NamedColor ACTIVEBORDER = new NamedColor(StyleColorNames.ACTIVE_BORDER);

    public static final NamedColor ACTIVECAPTION = new NamedColor(StyleColorNames.ACTIVE_CAPTION);

    public static final NamedColor APPWORKSPACE = new NamedColor(StyleColorNames.APP_WORKSPACE);

    public static final NamedColor BACKGROUND = new NamedColor(StyleColorNames.BACKGROUND);

    public static final NamedColor BUTTONFACE = new NamedColor(StyleColorNames.BUTTON_FACE);

    public static final NamedColor BUTTONHIGHLIGHT = new NamedColor(StyleColorNames.BUTTON_HIGHLIGHT);

    public static final NamedColor BUTTONSHADOW = new NamedColor(StyleColorNames.BUTTON_SHADOW);

    public static final NamedColor BUTTONTEXT = new NamedColor(StyleColorNames.BUTTON_TEXT);

    public static final NamedColor CAPTIONTEXT = new NamedColor(StyleColorNames.CAPTION_TEXT);

    public static final NamedColor GRAYTEXT = new NamedColor(StyleColorNames.GRAY_TEXT);

    public static final NamedColor HIGHLIGHT = new NamedColor(StyleColorNames.HIGHLIGHT);

    public static final NamedColor HIGHLIGHTTEXT = new NamedColor(StyleColorNames.HIGHLIGHT_TEXT);

    public static final NamedColor INACTIVEBORDER = new NamedColor(StyleColorNames.INACTIVE_BORDER);

    public static final NamedColor INACTIVECAPTION = new NamedColor(StyleColorNames.INACTIVE_CAPTION);

    public static final NamedColor INACTIVECAPTIONTEXT = new NamedColor(StyleColorNames.INACTIVE_CAPTION_TEXT);

    public static final NamedColor INFOBACKGROUND = new NamedColor(StyleColorNames.INFO_BACKGROUND);

    public static final NamedColor INFOTEXT = new NamedColor(StyleColorNames.INFO_TEXT);

    public static final NamedColor MENU = new NamedColor(StyleColorNames.MENU);

    public static final NamedColor MENUTEXT = new NamedColor(StyleColorNames.MENU_TEXT);

    public static final NamedColor SCROLLBAR = new NamedColor(StyleColorNames.SCROLLBAR);

    public static final NamedColor THREEDDARKSHADOW = new NamedColor(StyleColorNames.THREE_DDARK_SHADOW);

    public static final NamedColor THREEDFACE = new NamedColor(StyleColorNames.THREE_DFACE);

    public static final NamedColor THREEDHIGHLIGHT = new NamedColor(StyleColorNames.THREE_DHIGHLIGHT);

    public static final NamedColor THREEDLIGHTSHADOW = new NamedColor(StyleColorNames.THREE_DLIGHT_SHADOW);

    public static final NamedColor THREEDSHADOW = new NamedColor(StyleColorNames.THREE_DSHADOW);

    public static final NamedColor WINDOW = new NamedColor(StyleColorNames.WINDOW);

    public static final NamedColor WINDOWFRAME = new NamedColor(StyleColorNames.WINDOW_FRAME);

    public static final NamedColor WINDOWTEXT = new NamedColor(StyleColorNames.WINDOW_TEXT);

    private static final NamedColor[] standardColors = { AQUA, BLACK, BLUE, FUCHSIA, GRAY, GREEN, LIME, MAROON, NAVY, OLIVE, PURPLE, RED, SILVER, TEAL, WHITE, YELLOW, ORANGE };

    private static final NamedColor[] systemColors = { ACTIVEBORDER, ACTIVECAPTION, APPWORKSPACE, BACKGROUND, BUTTONFACE, BUTTONHIGHLIGHT, BUTTONSHADOW, BUTTONTEXT, CAPTIONTEXT, GRAYTEXT, HIGHLIGHT, HIGHLIGHTTEXT, INACTIVEBORDER, INACTIVECAPTION, INACTIVECAPTIONTEXT, INFOBACKGROUND, INFOTEXT, MENU, MENUTEXT, SCROLLBAR, THREEDDARKSHADOW, THREEDFACE, THREEDHIGHLIGHT, THREEDLIGHTSHADOW, THREEDSHADOW, WINDOW, WINDOWFRAME, WINDOWTEXT };

    private static final NamedColor[] allColors;

    static {
        allColors = new NamedColor[standardColors.length + systemColors.length];
        System.arraycopy(standardColors, 0, allColors, 0, standardColors.length);
        System.arraycopy(systemColors, 0, allColors, standardColors.length, systemColors.length);
    }

    /**
     * Gets the standard colors.
     * @return the standard colors
     */
    public static NamedColor[] getStandardColors() {
        return standardColors;
    }

    /**
     * Gets the system colors.
     * @return the system colors
     */
    public static NamedColor[] getSystemColors() {
        return systemColors;
    }

    /**
     * Gets the standard and system colors.
     * @return the standard colors followed by the system colors
     */
    public static NamedColor[] getAllColors() {
        return allColors;
    }

    /**
     * Helper method which uses AWT to get the system colors.
     * @param color the AWT SystemColor
     * @return the hex value for the color
     */
    private static String awtColorToHex(SystemColor color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        String hex = Convertors.RGBToHex(new RGB(red, green, blue));
        return hex;
    }
}
