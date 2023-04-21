package com.sshtools.sshterm;

import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.KeyStroke;
import com.sshtools.common.ui.StandardAction;

public abstract class PrintPreviewAction extends StandardAction {

    public PrintPreviewAction() {
        putValue(Action.NAME, "Print Preview");
        putValue(Action.SMALL_ICON, getIcon("/com/sshtools/sshterm/printpreview.png"));
        putValue(Action.SHORT_DESCRIPTION, "Print Preview");
        putValue(Action.LONG_DESCRIPTION, "Preview what would be printed");
        putValue(Action.MNEMONIC_KEY, new Integer('r'));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.ALT_MASK));
        putValue(Action.ACTION_COMMAND_KEY, "print-preview-command");
        putValue(StandardAction.ON_MENUBAR, new Boolean(true));
        putValue(StandardAction.MENU_NAME, "File");
        putValue(StandardAction.MENU_ITEM_GROUP, new Integer(80));
        putValue(StandardAction.MENU_ITEM_WEIGHT, new Integer(10));
        putValue(StandardAction.ON_TOOLBAR, new Boolean(false));
    }
}
