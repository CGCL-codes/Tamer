package org.yawlfoundation.yawl.editor.actions;

import org.yawlfoundation.yawl.editor.actions.specification.YAWLActiveOpenSpecificationAction;
import org.yawlfoundation.yawl.editor.specification.SpecificationUndoManager;
import org.yawlfoundation.yawl.editor.swing.menu.MenuUtilities;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class RedoAction extends YAWLActiveOpenSpecificationAction {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    private static final RedoAction INSTANCE = new RedoAction();

    {
        putValue(Action.SHORT_DESCRIPTION, " Redo the last undone action ");
        putValue(Action.NAME, "Redo");
        putValue(Action.LONG_DESCRIPTION, "Redo last undone action");
        putValue(Action.SMALL_ICON, getPNGIcon("arrow_redo"));
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_R));
        putValue(Action.ACCELERATOR_KEY, MenuUtilities.getAcceleratorKeyStroke("Y"));
    }

    private RedoAction() {
    }

    ;

    public static RedoAction getInstance() {
        return INSTANCE;
    }

    public void actionPerformed(ActionEvent event) {
        SpecificationUndoManager.getInstance().redo();
    }
}
