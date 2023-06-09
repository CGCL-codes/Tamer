package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SeparatorUI;

/**
 * The Basic Look and Feel UI delegate for JSeparator.
 */
public class BasicSeparatorUI extends SeparatorUI {

    /** The shadow color. */
    protected Color shadow;

    /** The highlight color. */
    protected Color highlight;

    /**
   * Creates a new UI delegate for the given JComponent.
   *
   * @param c The JComponent to create a delegate for.
   *
   * @return A new BasicSeparatorUI.
   */
    public static ComponentUI createUI(JComponent c) {
        return new BasicSeparatorUI();
    }

    /**
   * This method installs the UI for the given JComponent.
   * This can include installing defaults, listeners, and
   * initializing any instance data.
   *
   * @param c The JComponent that is having this UI installed.
   */
    public void installUI(JComponent c) {
        super.installUI(c);
        if (c instanceof JSeparator) {
            JSeparator s = (JSeparator) c;
            installDefaults(s);
            installListeners(s);
        }
    }

    /**
   * Uninstalls the UI for the given JComponent. This
   * method reverses what was done when installing
   * the UI on the JComponent.
   *
   * @param c The JComponent that is having this UI uninstalled.
   */
    public void uninstallUI(JComponent c) {
        if (c instanceof JSeparator) {
            JSeparator s = (JSeparator) c;
            uninstallListeners(s);
            uninstallDefaults(s);
        }
    }

    /**
   * This method installs the defaults that are given by
   * the Basic Look and Feel.
   *
   * @param s The JSeparator that is being installed.
   */
    protected void installDefaults(JSeparator s) {
        shadow = UIManager.getColor("Separator.shadow");
        highlight = UIManager.getColor("Separator.highlight");
        s.setOpaque(false);
    }

    /**
   * This method removes the defaults that were given
   * by the Basic Look and Feel.
   *
   * @param s The JSeparator that is being uninstalled.
   */
    protected void uninstallDefaults(JSeparator s) {
        shadow = null;
        highlight = null;
    }

    /**
   * This method installs any listeners that need
   * to be attached to the JSeparator or any of its 
   * components.
   *
   * @param s The JSeparator that is being installed.
   */
    protected void installListeners(JSeparator s) {
    }

    /**
   * This method uninstalls any listeners that
   * were installed during the install UI process.
   *
   * @param s The JSeparator that is being uninstalled.
   */
    protected void uninstallListeners(JSeparator s) {
    }

    /**
   * The separator is made of two lines. The top line will be 
   * the shadow color (or left line if it's vertical). The bottom 
   * or right line will be the highlight color. The two lines will 
   * be centered inside the bounds box. If the separator is horizontal, 
   * then it will be vertically centered, or if it's vertical, it will 
   * be horizontally centered.
   *
   * @param g The Graphics object to paint with
   * @param c The JComponent to paint.
   */
    public void paint(Graphics g, JComponent c) {
        Rectangle r = new Rectangle();
        SwingUtilities.calculateInnerArea(c, r);
        Color saved = g.getColor();
        JSeparator s;
        if (c instanceof JSeparator) s = (JSeparator) c; else return;
        if (s.getOrientation() == JSeparator.HORIZONTAL) {
            int midAB = r.height / 2;
            g.setColor(shadow);
            g.drawLine(r.x, r.y + midAB - 1, r.x + r.width, r.y + midAB - 1);
            g.setColor(highlight);
            g.fillRect(r.x, r.y + midAB, r.x + r.width, r.y + midAB);
        } else {
            int midAD = r.height / 2 + r.y;
            g.setColor(shadow);
            g.drawLine(r.x, r.y, r.x, r.y + r.height);
            g.setColor(highlight);
            g.fillRect(r.x + midAD, r.y + r.height, r.x + midAD, r.y + r.height);
        }
        g.setColor(saved);
    }

    /**
   * This method returns the preferred size of the 
   * JComponent.
   *
   * @param c The JComponent to measure.
   *
   * @return The preferred size.
   */
    public Dimension getPreferredSize(JComponent c) {
        Dimension pref = new Dimension(2, 0);
        if (c instanceof JSeparator) {
            JSeparator s = (JSeparator) c;
            if (s.getOrientation() == JSeparator.HORIZONTAL) pref = new Dimension(0, 2);
        }
        return pref;
    }

    /**
   * This method returns the minimum size of the
   * JComponent.
   *
   * @param c The JComponent to measure.
   *
   * @return The minimum size.
   */
    public Dimension getMinimumSize(JComponent c) {
        return new Dimension(0, 0);
    }

    /**
   * This method returns the maximum size of the
   * JComponent.
   *
   * @param c The JComponent to measure.
   *
   * @return The maximum size.
   */
    public Dimension getMaximumSize(JComponent c) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }
}
