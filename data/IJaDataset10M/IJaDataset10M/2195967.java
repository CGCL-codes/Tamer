package edu.columbia.concerns.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import edu.columbia.concerns.ConcernTagger;
import edu.columbia.concerns.repository.Concern;
import edu.columbia.concerns.ui.concerntree.ConcernTreeViewer;
import edu.columbia.concerns.util.ARFFFile;

/**
 * An action to rename a concern to the model.
 */
public class RenameConcernAction extends Action {

    private ConcernTreeViewer viewer;

    private Concern concern;

    /**
	 * Creates the action.
	 * 
	 * @param concern
	 *            The view from where the action is triggered
	 * @param viewer
	 *            The viewer controlling this action.
	 */
    public RenameConcernAction(ConcernTreeViewer viewer, Concern concern) {
        this.viewer = viewer;
        this.concern = concern;
        setText(ConcernTagger.getResourceString("actions.RenameConcernAction.Label"));
        setToolTipText(ConcernTagger.getResourceString("actions.RenameConcernAction.ToolTip"));
    }

    /**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
    @Override
    public void run() {
        InputDialog lDialog = new InputDialog(viewer.getTree().getShell(), ConcernTagger.getResourceString("actions.RenameConcernAction.DialogTitle"), ConcernTagger.getResourceString("actions.RenameConcernAction.DialogLabel"), concern.getDisplayName(), new IInputValidator() {

            public String isValid(String concernName) {
                if (concernName.equals(concern.getDisplayName())) return ConcernTagger.getResourceString("SameName"); else return concern.getParent().isChildNameValid(concernName);
            }
        });
        if (lDialog.open() == Window.OK) {
            String escapedName = ARFFFile.escape(lDialog.getValue());
            concern.rename(escapedName);
        }
    }
}
