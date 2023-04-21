package com.netprogress.rcp.ui.framework.actionSet;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.forms.editor.FormEditor;
import com.netprogress.rcp.ui.framework.pages.core.DataPage;

public class CancelAction extends ActionDelegate implements IWorkbenchWindowActionDelegate {

    private final Logger logger = Logger.getLogger(CancelAction.class);

    public static final String ID = "CostEditor.Cancel";

    private IWorkbenchWindow window;

    private FormEditor editor;

    private DataPage page;

    public static IAction action;

    public void dispose() {
    }

    @Override
    public void init(IAction action) {
        this.action = action;
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
        FormEditor editor = (FormEditor) window.getActivePage().getActiveEditor();
        page = (DataPage) editor.getActivePageInstance();
        if (MessageDialog.openConfirm(null, "Annuler", "Annuler les modifications apporter sur cette enr�gistrement?")) {
            int pageMode = page.getMode();
            if (pageMode == page.ADD_MODE) {
                page.emptyFieldContent(page.getForm().getBody());
            } else if (pageMode == page.EDIT_MODE) {
                page.initPageData();
            }
        }
        page.setEditionMode(page.READ_MODE);
        page.setEnabledActionSet(page.ID_EDIT, page.getCurrentObject() != null ? true : false);
        page.setEnabledActionSet(page.ID_DELETE, page.getCurrentObject() != null ? true : false);
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
