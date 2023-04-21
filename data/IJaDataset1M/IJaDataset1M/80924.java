package fr.emn.easymol.gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/** The class to register the action "Load".<br>
*   It uses some methods in the <code>LibGUI</code> to perform its operations.
**/
public class ActionLoad extends AbstractAction {

    private ToolBarWindow tb;

    /** Constructs an <code>AbstractAction</code> and sets additional fields. 
   *   @param toolB the <code>ToolBarWindow</code> in which this action will be created.
   *   @param name the label that will be put in menus.
   *   @param toolTip the tooltip which will appear on the buttons or in the menus.
   *   @param accelerator the keyboard shortcut.
   *   @param icon the beautiful icon for the button and the menu item.
   **/
    public ActionLoad(ToolBarWindow toolB, String name, String toolTip, KeyStroke accelerator, Icon icon) {
        super(name, icon);
        super.putValue(Action.SHORT_DESCRIPTION, toolTip);
        super.putValue(Action.ACCELERATOR_KEY, accelerator);
        tb = toolB;
    }

    /** The action connected to item "Load" in menus or toolbars.
   *   @param e the <code>ActionEvent</code> fired by the listener that will be registered by the action.
   **/
    public void actionPerformed(ActionEvent e) {
        LibGUI.loadFromFile(tb);
    }
}
