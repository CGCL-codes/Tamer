package net.sf.refactorit.netbeans.common.action;

import net.sf.refactorit.ui.module.type.TypeAction;

public class InfoAction extends HighLevelMenuAction {

    public String getName() {
        return "[R] Info";
    }

    public String getActionKey() {
        return TypeAction.KEY;
    }

    protected String iconResource() {
        return getIconResource("info_action.gif");
    }
}
