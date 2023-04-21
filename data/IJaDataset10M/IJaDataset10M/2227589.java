package org.pushingpixels.substance.internal.utils.icon;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.*;
import javax.swing.plaf.UIResource;

/**
 * Icon for the cascading {@link JMenu}s.
 * 
 * @author Kirill Grouchnikov
 */
public class MenuArrowIcon implements Icon, UIResource {

    /**
	 * Icon for left-to-right {@link JMenu}s.
	 */
    private Icon ltrIcon;

    /**
	 * Icon for right-to-left {@link JMenu}s.
	 */
    private Icon rtlIcon;

    /**
	 * Creates the arrow icon for the specified menu.
	 * 
	 * @param menu
	 *            Menu.
	 */
    public MenuArrowIcon(JMenu menu) {
        this.ltrIcon = new ArrowButtonTransitionAwareIcon(menu, SwingConstants.EAST);
        this.rtlIcon = new ArrowButtonTransitionAwareIcon(menu, SwingConstants.WEST);
    }

    public int getIconHeight() {
        return this.ltrIcon.getIconHeight();
    }

    public int getIconWidth() {
        return this.ltrIcon.getIconWidth();
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (c.getComponentOrientation().isLeftToRight()) {
            this.ltrIcon.paintIcon(c, g, x, y);
        } else {
            this.rtlIcon.paintIcon(c, g, x, y);
        }
    }
}
