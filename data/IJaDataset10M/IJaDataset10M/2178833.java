package com.loribel.commons.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.loribel.commons.swing.GB_MenuItem;

/**
 * Menu Item Update.
 *
 * @author Gregory Borelli
 */
public class GB_MenuItemUpdate extends GB_MenuItem implements ActionListener {

    /**
     * The listener of this menuItem.
     */
    private GB_ActionListenerUpdate listener;

    /**
     * Constructor of GB_MenuItemUpdate with parameter(s).
     *
     * @param a_listener GB_ActionListenerUpdate - the listener of this menuItem
     */
    public GB_MenuItemUpdate(GB_ActionListenerUpdate a_listener) {
        super(AA.MENU_ITEM_UPDATE, AA.MENU_ITEM_UPDATE_ICON);
        listener = a_listener;
        this.addActionListener(this);
    }

    /**
     * Method actionPerformed.
     * <br />
     *
     * @param e ActionEvent -
     */
    public void actionPerformed(ActionEvent e) {
        listener.actionUpdate(e);
    }
}
