package com.metanology.mde.ui.pimEditor.actions;

import java.util.*;
import org.eclipse.ui.IEditorPart;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import com.metanology.mde.ui.pimEditor.edit.LinkableNodeEditPart;
import com.metanology.mde.ui.pimEditor.edit.RefreshRelationRequest;
import com.metanology.mde.utils.Messages;

/**
 * An action to delete selected objects from the diagram.
 */
public class PIMRefreshRelationAction extends SelectionAction {

    public static final String ID = "$pim refresh relation";

    /**
	 * Creates a <code>PIMRefreshRelationAction</code> with a default label.
	 *
	 * @param editor The editor this action will be associated with.
	 */
    public PIMRefreshRelationAction(IEditorPart editor) {
        super(editor);
    }

    /**
	 * Initializes this action's text and images.
	 */
    protected void init() {
        super.init();
        setText(Messages.UI_PIMRefreshRelationAction_refresh);
        setToolTipText(Messages.UI_PIMRefreshRelationAction_refresh_tooltip);
        setId(ID);
        setEnabled(false);
    }

    /**
	 * Creates a <code>PIMRefreshRelationAction</code> with the given label.
	 *
	 * @param editor The editor this action will be associated with.
	 * @param label  The label to be displayed for this action.
	 */
    public PIMRefreshRelationAction(IEditorPart editor, String label) {
        super(editor);
        setText(label);
    }

    /**
	 * Create a command to remove the selected objects.
	 *
	 * @param objects The objects to be deleted.
	 *
	 * @return The command to remove the selected objects.
	 */
    protected Command createRefreshCommand(List objects) {
        if (objects.isEmpty()) return null;
        if (!(objects.get(0) instanceof EditPart)) return null;
        RefreshRelationRequest refreshReq = this.createRefreshRequest();
        CompoundCommand compoundCmd = new CompoundCommand(Messages.UI_PIMRefreshRelationAction_refresh);
        for (int i = 0; i < objects.size(); i++) {
            EditPart object = (EditPart) objects.get(i);
            Command cmd = object.getCommand(refreshReq);
            if (cmd != null && cmd.canExecute()) compoundCmd.add(cmd);
        }
        return compoundCmd;
    }

    protected RefreshRelationRequest createRefreshRequest() {
        return new RefreshRelationRequest(RefreshRelationRequest.ID);
    }

    /**
	 * Returns <code>true</code> if the selected objects can
	 * be deleted.  Returns <code>false</code> if there are
	 * no objects selected or the selected objects are not
	 * {@link EditPart}s.
	 */
    protected boolean calculateEnabled() {
        List objs = getSelectedObjects();
        if (objs == null || objs.size() == 0) {
            return false;
        } else if (objs.size() == 1) {
            Command cmd = createRefreshCommand(getSelectedObjects());
            if (cmd == null) return false;
            return cmd.canExecute();
        } else {
            for (Iterator i = objs.iterator(); i.hasNext(); ) {
                Object o = i.next();
                if (o instanceof LinkableNodeEditPart) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Performs the delete action on the selected objects.
	 */
    public void run() {
        execute(createRefreshCommand(getSelectedObjects()));
    }
}
