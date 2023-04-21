package gov.sns.apps.jeri.tools.swing.autocompletecombobox;

import javax.swing.plaf.basic.*;
import javax.swing.*;

/**
 * Creates a <code>WideComboPopup</code> for a <code>JComboBox</code>. This
 * class is used to create a <code>JComboBox</code> with a popup that is wider
 * than the oringinal combo box.
 *
 * @author Chris Fowlkes
 * @version 1.0
 */
public class WideComboUI extends BasicComboBoxUI {

    /**
     * Creates an implementation of the ComboPopup interface.
     * Returns an instance of BasicComboPopup.
     */
    @Override
    protected ComboPopup createPopup() {
        WideComboPopup popup = new WideComboPopup(comboBox);
        popup.getAccessibleContext().setAccessibleParent(comboBox);
        return popup;
    }

    @Override
    protected void installKeyboardActions() {
        UIManager.put("ComboBox.actionMap", null);
        super.installKeyboardActions();
    }
}
