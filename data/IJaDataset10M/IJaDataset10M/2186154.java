package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.view.XPontusWindow;

/**
 * Action to copy text
 * @author Yves Zoundi
 */
public class CopyAction extends BaseAction {

    /** Creates a new instance of CopyAction */
    public CopyAction() {
    }

    public void execute() {
        XPontusWindow.getInstance().getCurrentEditor().copy();
    }
}
