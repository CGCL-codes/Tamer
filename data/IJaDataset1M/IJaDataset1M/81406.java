package com.seaglasslookandfeel.state;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 * Is the text field a search field variant with a popup menu?
 */
public class SearchFieldHasPopupState extends State {

    /**
     * Creates a new SearchFieldHasPopupState object.
     */
    public SearchFieldHasPopupState() {
        super("HasPopup");
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInState(JComponent c) {
        if (c instanceof JButton && c.getParent() != null && c.getParent() instanceof JTextComponent) {
            c = (JComponent) c.getParent();
        } else if (!(c instanceof JTextComponent)) {
            return false;
        }
        if (!"search".equals(c.getClientProperty("JTextField.variant"))) {
            return false;
        }
        Object o = c.getClientProperty("JTextField.Search.FindPopup");
        return (o != null && o instanceof JPopupMenu);
    }
}
