package org.rubypeople.rdt.internal.ui.viewsupport;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.rubypeople.rdt.ui.actions.*;

/**
 * Action used to enable / disable method filter properties
 */
public class MemberFilterAction extends Action {

    private int fFilterProperty;

    private MemberFilterActionGroup fFilterActionGroup;

    public MemberFilterAction(MemberFilterActionGroup actionGroup, String title, int property, String contextHelpId, boolean initValue) {
        super(title);
        fFilterActionGroup = actionGroup;
        fFilterProperty = property;
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, contextHelpId);
        setChecked(initValue);
    }

    /**
     * Returns this action's filter property.
     */
    public int getFilterProperty() {
        return fFilterProperty;
    }

    public void run() {
        fFilterActionGroup.setMemberFilter(fFilterProperty, isChecked());
    }
}
