package net.sourceforge.steelme;

import javax.swing.*;
import java.awt.*;

/**
 *  <code>ThemeAwareTextArea</code> forces the text area's foreground
 * and background colors to conform with the root windows during an updateUI
 * event.
 * @author <a href="mailto:tj_willis@users.sourceforge.net">T.J. Willis</a>
 * @version $Revision: 1.1.1.1 $
 */
public class ThemeAwareTextArea extends JTextArea {

    /**
     * Sets background and foreground color to the theme's colors and
     * delegates up to super.updateUI().
     *
     */
    public void updateUI() {
        super.updateUI();
        Component rt = javax.swing.SwingUtilities.getRoot(this);
        if (rt != null) {
            Color bg = rt.getBackground();
            Color fg = rt.getForeground();
            setBackground(bg);
            setForeground(fg);
        }
    }
}
