package com.jgoodies.plaf.windows;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.Element;
import javax.swing.text.PasswordView;
import javax.swing.text.View;
import com.sun.java.swing.plaf.windows.WindowsPasswordFieldUI;

/**
 * The JGoodies Windows Look&amp;Feel implementation of a password field UI
 * delegate. It differs from its superclass in that it utilizes a password 
 * view that renders a circle, not a star (&quot;*&quot;) character.
 * 
 * @author Karsten Lentzsch
 * @version $Revision: 1.1.1.1 $
 */
public final class ExtWindowsXPPasswordFieldUI extends WindowsPasswordFieldUI {

    /**
	 * Creates a UI for a {@link JPasswordField}.
	 * 
	 * @param c the password field component
	 * @return the UI
	 */
    public static ComponentUI createUI(JComponent c) {
        return new ExtWindowsXPPasswordFieldUI();
    }

    /**
	 * Creates a view (an <code>ExtWindowsXPPasswordView</code>) for an element.
	 * 
	 * @param elem the element
	 * @return the view
	 */
    public View create(Element elem) {
        return new ExtWindowsXPPasswordView(elem);
    }

    private static class ExtWindowsXPPasswordView extends PasswordView {

        private ExtWindowsXPPasswordView(Element element) {
            super(element);
        }

        protected int drawEchoCharacter(Graphics g, int x, int y, char c) {
            Container container = getContainer();
            if (!(container instanceof JPasswordField)) return super.drawEchoCharacter(g, x, y, c);
            JPasswordField field = (JPasswordField) container;
            int charWidth = getFontMetrics().charWidth(field.getEchoChar());
            int advance = 2;
            int diameter = Math.max(charWidth - 2, 5);
            Graphics2D g2 = (Graphics2D) g;
            Object oldHints = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillOval(x, y - diameter, diameter, diameter);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHints);
            return x + diameter + advance;
        }
    }
}
