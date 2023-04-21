package org.mcisb.ui.util;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * 
 * @author Neil Swainston
 */
public class JTextComponentMenu extends JPopupMenu implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.mcisb.ui.util.messages");

    /**
	 * 
	 */
    final JMenuItem cutMenuItem = new JMenuItem(resourceBundle.getString("JTextFieldMenu.cut"));

    /**
	 * 
	 */
    final JMenuItem copyMenuItem = new JMenuItem(resourceBundle.getString("JTextFieldMenu.copy"));

    /**
	 * 
	 */
    final JMenuItem pasteMenuItem = new JMenuItem(resourceBundle.getString("JTextFieldMenu.paste"));

    /**
     * 
     * @param editable
     */
    public JTextComponentMenu(final boolean editable) {
        cutMenuItem.addActionListener(this);
        copyMenuItem.addActionListener(this);
        pasteMenuItem.addActionListener(this);
        if (editable) {
            add(cutMenuItem);
        }
        add(copyMenuItem);
        if (editable) {
            add(pasteMenuItem);
        }
    }

    /**
	 * 
	 */
    public JTextComponentMenu() {
        this(true);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        final Object source = evt.getSource();
        final Component component = getInvoker();
        if (component instanceof JTextComponent) {
            final JTextComponent textComponent = (JTextComponent) component;
            if (source == cutMenuItem) {
                textComponent.cut();
            }
            if (source == copyMenuItem) {
                textComponent.copy();
            }
            if (source == pasteMenuItem) {
                textComponent.paste();
            }
        }
    }
}
