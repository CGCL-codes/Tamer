package net.sf.freecol.common.resources;

import java.awt.Color;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.logging.Logger;

/**
 * A <code>Resource</code> wrapping a <code>Color</code>.
 * 
 * @see Resource
 * @see Color
 */
public class ColorResource extends Resource {

    private static final Logger logger = Logger.getLogger(ColorResource.class.getName());

    public static final String SCHEME = "color:";

    private Color color;

    public ColorResource(Color color) {
        this.color = color;
    }

    /**
     * Do not use directly.
     * @param resourceLocator The <code>URI</code> used when loading this
     *      resource.
     * @see ResourceFactory#createResource(URI)
     */
    ColorResource(URI resourceLocator) throws Exception {
        super(resourceLocator);
        String colorName = resourceLocator.getSchemeSpecificPart().substring(SCHEME.length());
        color = getColor(colorName);
    }

    /**
     * Preloading is a noop for this resource type.
     */
    public void preload() {
    }

    private static boolean isHexString(String str) {
        if (str == null || !(str.startsWith("0x") || str.startsWith("0X")) || str.length() <= 2) return false;
        for (int i = 2; i < str.length(); i++) {
            if ("0123456789ABCDEFabcdef".indexOf(str.substring(i, i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the <code>Color</code> identified by the given
     * string. This is either a hexadecimal integer prefixed with
     * "0x", or the name of a field of the Color class.
     *
     * @param colorName a <code>String</code> value
     * @return a <code>Color</code> value
     */
    public static Color getColor(String colorName) {
        if (isHexString(colorName)) {
            try {
                int col = Integer.decode(colorName);
                return new Color(col, colorName.length() > 8);
            } catch (NumberFormatException e) {
                logger.warning("Failed to decode colour string: " + colorName);
            }
        } else {
            try {
                Field field = Color.class.getField(colorName);
                return (Color) field.get(null);
            } catch (Exception e) {
                logger.warning(e.toString());
            }
        }
        return Color.BLACK;
    }

    /**
     * Gets the <code>Color</code> represented by this resource.
     * @return The <code>Color</code> in it's original size.
     */
    public Color getColor() {
        return color;
    }
}
