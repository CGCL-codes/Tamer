package net.sourceforge.rcontrol.swing.base;

import javax.swing.JTextField;

/**
 * @author christian
 * Derived from JTextfield to
 * implement a basic textfield with
 * a popup menu
 */
public class PopupTextField extends JTextField {

    /**
     * Standard Contructor
     * calling super constructor
     * and create popup
     */
    public PopupTextField() {
        super();
        createPopup();
    }

    /**
     * Standard Contructor
     * calling super constructor
     * and create popup
     * @param arg0 Standard string
     */
    public PopupTextField(String arg0) {
        super(arg0);
        createPopup();
    }

    /**
     * Create the popupmenu
     * by registering a MyPopupListener
     * that holds the basic popup
     */
    private void createPopup() {
        this.addMouseListener(new JTextComponentPopup(this));
    }
}
