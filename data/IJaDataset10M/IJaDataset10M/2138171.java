package org.plazmaforge.studio.dbdesigner.actions;

import java.util.Iterator;
import java.util.List;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.plazmaforge.studio.dbdesigner.parts.ERTableNodeEditPart;

public class ToggleTableDetailAction extends SelectionAction {

    public ToggleTableDetailAction(IWorkbenchPart iworkbenchpart) {
        super(iworkbenchpart);
        setId(TOGGLE_TABLE_DETAIL);
        setText(TOGGLE_TABLE_DETAIL);
    }

    protected boolean calculateEnabled() {
        List list = getSelectedObjects();
        if (list.size() == 0) return false;
        for (int i = 0; i < list.size(); i++) if (list.get(i) instanceof ERTableNodeEditPart) {
            if (list.size() == 1) {
                boolean flag = ((ERTableNodeEditPart) list.get(i)).isExpanded();
                if (flag) setText("Hide detail"); else setText("Show detail");
            } else {
                setText(TOGGLE_TABLE_DETAIL);
            }
            return true;
        }
        return false;
    }

    public void run() {
        for (Iterator iterator = getSelectedObjects().iterator(); iterator.hasNext(); ) {
            Object obj = iterator.next();
            if (obj instanceof ERTableNodeEditPart) ((ERTableNodeEditPart) obj).toggleDetail();
        }
    }

    public static final String TOGGLE_TABLE_DETAIL;

    static {
        TOGGLE_TABLE_DETAIL = "Toggle table detail";
    }
}
