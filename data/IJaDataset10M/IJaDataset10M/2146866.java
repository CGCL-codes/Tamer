package net.sourceforge.mlf.metouia;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;

/**
 * This class represents the UI delegate for the JMenu component.
 *
 * @author Taoufik Romdhane
 */
public class MetouiaMenuUI extends BasicMenuUI {

    /**
   * Creates the UI delegate for the given component.
   *
   * @param c The component to create its UI delegate.
   * @return The UI delegate for the given component.
   */
    public static ComponentUI createUI(JComponent c) {
        return new MetouiaMenuUI();
    }

    /**
   * Paints the given component.
   *
   * @param g The graphics context to use.
   * @param c The component to paint.
   */
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        Container parent = menuItem.getParent();
        MetouiaGradients.drawHighlight(g, new Rectangle(0, -1, parent.getWidth(), parent.getHeight() / 2), true, true);
        MetouiaGradients.drawShadow(g, new Rectangle(0, parent.getHeight() / 2 - 1, parent.getWidth(), parent.getHeight() / 2), true, false);
    }
}
