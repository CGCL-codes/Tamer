package com.ecmdeveloper.plugin.diagrams.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

/**
 * @author Ricardo.Belfor
 *
 */
public class ShowPropertiesAction extends SelectionAction {

    public static final String ID = "com.ecmdeveloper.plugin.diagrams.actions.showPropertiesAction";

    private static final String ACTION_NAME = "Show Properties";

    public ShowPropertiesAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText(ACTION_NAME);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    @Override
    public void run() {
        try {
            getWorkbenchPart().getSite().getPage().showView(IPageLayout.ID_PROP_SHEET);
        } catch (PartInitException e) {
            Shell shell = getWorkbenchPart().getSite().getShell();
            MessageDialog.openError(shell, ACTION_NAME, e.getLocalizedMessage());
        }
    }
}
