package org.adempiere.plaf;

import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/**
 * 	Button Listener
 *	
 *  @author Jorg Janke
 *  @version $Id: CompiereButtonListener.java,v 1.1 2005/12/05 02:38:28 jjanke Exp $
 */
public class AdempiereButtonListener extends BasicButtonListener {

    /**
	 * 	Adempiere Button Listener
	 *	@param b button
	 */
    public AdempiereButtonListener(AbstractButton b) {
        super(b);
    }

    /**
	 * 	Install Keyboard Actions
	 *	@param c component
	 */
    public void installKeyboardActions(JComponent c) {
        super.installKeyboardActions(c);
        updateMnemonicBindingX((AbstractButton) c);
    }

    /**
	 * 	Property Change
	 *	@param e event
	 */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(AbstractButton.MNEMONIC_CHANGED_PROPERTY)) updateMnemonicBindingX((AbstractButton) e.getSource()); else super.propertyChange(e);
    }

    /**
	 * 	Update Mnemonic Binding
	 *	@param b button
	 */
    void updateMnemonicBindingX(AbstractButton b) {
        int m = b.getMnemonic();
        if (m != 0) {
            InputMap map = SwingUtilities.getUIInputMap(b, JComponent.WHEN_IN_FOCUSED_WINDOW);
            if (map == null) {
                map = new ComponentInputMapUIResource(b);
                SwingUtilities.replaceUIInputMap(b, JComponent.WHEN_IN_FOCUSED_WINDOW, map);
            }
            map.clear();
            String className = b.getClass().getName();
            int mask = InputEvent.ALT_MASK;
            if (b instanceof JCheckBox || className.indexOf("VButton") != -1) mask = InputEvent.SHIFT_MASK + InputEvent.CTRL_MASK;
            map.put(KeyStroke.getKeyStroke(m, mask, false), "pressed");
            map.put(KeyStroke.getKeyStroke(m, mask, true), "released");
            map.put(KeyStroke.getKeyStroke(m, 0, true), "released");
        } else {
            InputMap map = SwingUtilities.getUIInputMap(b, JComponent.WHEN_IN_FOCUSED_WINDOW);
            if (map != null) map.clear();
        }
    }
}
