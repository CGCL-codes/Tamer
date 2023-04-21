package SEAlib;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.awt.*;
import java.awt.geom.*;

/**
 * Contains a hierarchy of themes.
 */
public class SEATheme {

    private HashMap themeMap;

    public Color backgroundColor;

    public Color foregroundColor;

    public Color borderColor;

    public int borderWidth;

    public Font font;

    /**
   * Constructs the theme with a default config.
   */
    public SEATheme() {
        backgroundColor = Color.WHITE;
        foregroundColor = Color.BLACK;
        borderColor = Color.BLACK;
        borderWidth = 2;
        font = null;
    }

    /**
   * Gets the named sub-theme.
   * @param themeName Name of the theme to be retrieved.
   * @return The requested theme of this object itself if teh named theme doesn't exist.
   */
    public SEATheme getTheme(String themeName) {
        SEATheme rVal;
        if (themeMap == null) {
            rVal = this;
        } else {
            rVal = (SEATheme) themeMap.get(themeName);
            if (rVal == null) {
                rVal = this;
            }
        }
        return rVal;
    }

    /**
   * Adds a subtheme under the supplied name.
   * @param themeName Name under which to store the theme.
   * @param theme Theme to be added.
   */
    public void addTheme(String themeName, SEATheme theme) {
        if (themeMap == null) {
            themeMap = new HashMap();
        }
        themeMap.put(themeName, theme);
    }

    /**
   * Draw the background as defined by the current theme.
   * @param g Graphics2D object for the current destination.
   * @param x X coord of top-left of rect.
   * @param y Y coord of top-left of rect.
   * @param w Width of rect.
   * @param h Height of rect.
   */
    public void drawBackground(Graphics2D g, int x, int y, int w, int h) {
        Rectangle2D.Float region = new Rectangle.Float(borderWidth / 2, borderWidth / 2, w - ((borderWidth)), h - ((borderWidth)));
        g.setColor(backgroundColor);
        g.fill(region);
        if (borderWidth != 0) {
            g.setStroke(new BasicStroke(borderWidth));
            g.setColor(borderColor);
            g.draw(region);
        }
    }

    /**
  	 * @author Michael Clear
  	 * Loads the theme from a user preference node
  	 * @param theme The preference tree that contains the theme data
  	 */
    public void load(SEAUser.Preference theme) {
        Enumeration properties;
        SEAUser.Preference property;
        Field[] fields = getClass().getDeclaredFields();
        int i;
        if (theme == null) throw new IllegalArgumentException("Preference cannot be null");
        properties = theme.getChildren();
        try {
            while (properties.hasMoreElements()) {
                property = (SEAUser.Preference) properties.nextElement();
                for (i = 0; i < fields.length; i++) {
                    if (fields[i].getName().equals(property.getName())) {
                        Object obj = fields[i].get(this);
                        if (obj instanceof Color) obj = new Color(Integer.parseInt(property.getStringValue())); else if (obj instanceof Integer) obj = new Integer(Integer.parseInt(property.getStringValue())); else if (obj instanceof Font || fields[i].getType().getName().equals("java.awt.Font")) {
                            String[] elements = property.getStringValue().split(",");
                            obj = (elements.length == 3) ? new Font(elements[0], Integer.parseInt(elements[1]), Integer.parseInt(elements[2])) : null;
                        } else obj = null;
                        fields[i].set(this, obj);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
