package org.opensourcephysics.tools;

import java.beans.*;
import java.util.*;
import java.awt.*;
import javax.swing.JMenu;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.border.*;

/**
 * A class for setting relative font sizes by level.
 *
 * @author dobrown
 * @version 1.0
 */
public class FontSizer {

    static Object levelObj = new FontSizer();

    static PropertyChangeSupport support = new SwingPropertyChangeSupport(levelObj);

    static int level;

    static double levelFactor = 1.35;

    static Map fontMap = new HashMap();

    /**
   * Private constructor to prevent instantiation.
   */
    private FontSizer() {
    }

    /**
   * Sets the font level and informs all listeners.
   *
   * @param n a non-negative integer level
   */
    public static void setLevel(int n) {
        level = Math.abs(n);
        support.firePropertyChange("level", null, new Integer(level));
    }

    /**
   * Gets the current font level.
   *
   * @return the level
   */
    public static int getLevel() {
        return level;
    }

    /**
   * Increments the font level and informs all listeners.
   */
    public static void levelUp() {
        level++;
        support.firePropertyChange("level", null, new Integer(level));
    }

    /**
   * Decrements the font level and informs all listeners.
   */
    public static void levelDown() {
        level--;
        level = Math.max(level, 0);
        support.firePropertyChange("level", null, new Integer(level));
    }

    /**
   * Sets the fonts of an object to a specified level.
   *
   * @param obj the object
   * @param level the level
   */
    public static void setFonts(Object obj, int level) {
        double factor = getFactor(level);
        if (obj instanceof Container) setFontFactor((Container) obj, factor); else if (obj instanceof TitledBorder) setFontFactor((TitledBorder) obj, factor); else if (obj instanceof Component) setFontFactor((Component) obj, factor);
    }

    /**
   * Resizes a font to a specified level.
   *
   * @param font the font
   * @param level the level
   */
    public static Font getResizedFont(Font font, int level) {
        return getResizedFont(font, getFactor(level));
    }

    /**
   * Resizes a font by a specified factor.
   *
   * @param font the font
   * @param factor the factor
   */
    public static Font getResizedFont(Font font, double factor) {
        if (font == null) return null;
        Font base = (Font) fontMap.get(font);
        if (base == null) {
            base = font;
            fontMap.put(font, base);
        }
        float size = (float) (base.getSize() * factor);
        font = base.deriveFont(size);
        fontMap.put(font, base);
        return font;
    }

    /**
   * Gets the factor corresponding to a specified level.
   *
   * @param level the level
   * @return the factor
   */
    public static double getFactor(int level) {
        double factor = 1.0;
        for (int i = 0; i < level; i++) {
            factor *= levelFactor;
        }
        return factor;
    }

    /**
    * Adds a PropertyChangeListener.
    *
    * @param property the name of the property (only "level" accepted)
    * @param listener the object requesting property change notification
    */
    public static void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        if (property.equals("level")) support.addPropertyChangeListener(property, listener);
    }

    /**
   * Removes a PropertyChangeListener.
   *
   * @param property the name of the property (only "level" accepted)
   * @param listener the listener requesting removal
   */
    public static void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }

    /**
   * Increases a container's normal font sizes by the specified factor.
   *
   * @param c a container
   * @param factor the factor
   */
    private static void setFontFactor(Container c, double factor) {
        Font font = getResizedFont(c.getFont(), factor);
        if (c instanceof javax.swing.JComponent) {
            Border border = ((javax.swing.JComponent) c).getBorder();
            if (border instanceof TitledBorder) setFontFactor((TitledBorder) border, factor);
        }
        if (c instanceof JMenu) {
            setMenuFont((JMenu) c, font);
        } else {
            c.setFont(font);
            for (int i = 0; i < c.getComponentCount(); i++) {
                Component co = c.getComponent(i);
                if ((co instanceof Container)) {
                    setFontFactor((Container) co, factor);
                } else setFontFactor(co, factor);
            }
        }
        if (c != null) {
            c.repaint();
        }
    }

    /**
   * Increases a component's normal font sizes by the specified factor.
   *
   * @param c a component
   * @param factor the factor
   */
    private static void setFontFactor(Component c, double factor) {
        Font font = getResizedFont(c.getFont(), factor);
        c.setFont(font);
        if (c instanceof javax.swing.JComponent) {
            Border border = ((javax.swing.JComponent) c).getBorder();
            if (border instanceof TitledBorder) setFontFactor((TitledBorder) border, factor);
        }
    }

    /**
   * Increases a titled border's normal font size by the specified factor.
   *
   * @param b a titled border
   * @param factor the factor
   */
    private static void setFontFactor(TitledBorder b, double factor) {
        Font font = getResizedFont(b.getTitleFont(), factor);
        b.setTitleFont(font);
    }

    /**
   * Sets the menu font.
   *
   * @param m a menu
   * @param font the font
   */
    private static void setMenuFont(JMenu m, Font font) {
        m.setFont(font);
        for (int i = 0; i < m.getMenuComponentCount(); i++) {
            m.getMenuComponent(i).setFont(font);
            if (m.getMenuComponent(i) instanceof JMenu) setMenuFont((JMenu) m.getMenuComponent(i), font);
        }
    }
}
