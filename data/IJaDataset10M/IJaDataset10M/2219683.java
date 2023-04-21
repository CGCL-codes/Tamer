package com.jgoodies.looks.plastic;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import com.jgoodies.looks.common.ExtButtonAreaLayout;

/**
 * The JGoodies Plastic Look&amp;Feel implementation of
 * <code>OptionPaneUI</code>. Honors the screen resolution and
 * uses a minimum button with that complies better with the Mac and Windows
 * UI style guides.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class PlasticOptionPaneUI extends BasicOptionPaneUI {

    public static ComponentUI createUI(JComponent b) {
        return new PlasticOptionPaneUI();
    }

    /**
     * Creates and returns a Container containin the buttons. The buttons
     * are created by calling <code>getButtons</code>.
     */
    protected Container createButtonArea() {
        JPanel bottom = new JPanel(new ExtButtonAreaLayout(true, 6));
        bottom.setBorder(UIManager.getBorder("OptionPane.buttonAreaBorder"));
        addButtonComponents(bottom, getButtons(), getInitialValueIndex());
        return bottom;
    }
}
