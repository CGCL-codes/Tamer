package de.beas.explicanto.client.rcp.actions;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datasource.types.WSTask;
import de.bea.services.vidya.client.datastructures.CCustomer;
import de.bea.services.vidya.client.datastructures.TreeNode;
import de.beas.explicanto.client.ExplicantoClientPlugin;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.dialogs.TaskDialog;
import de.beas.explicanto.client.rcp.views.TasksView;

public class EditTaskAction implements IViewActionDelegate {

    private static final Logger log = Logger.getLogger(EditTaskAction.class);

    private WSTask task;

    private TasksView tv;

    public void run(IAction action) {
        TreeNode node = tv.getSelectedNode();
        try {
            if (node instanceof CCustomer) VidyaDataTree.getDefault().mayEditNode(task.getNodeUid()); else VidyaDataTree.getDefault().mayEditNode(node);
        } catch (Exception e) {
            ExplicantoClientPlugin.handleException(e, node);
            return;
        }
        TaskDialog nad = new TaskDialog(tv.getSite().getWorkbenchWindow().getShell(), task);
        nad.open();
        if (nad.isOk()) try {
            VidyaDataTree.getDefault().updateTask(task);
            tv.refresh(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(IViewPart view) {
        log.debug("init edit task action");
        tv = (TasksView) view;
    }

    public void selectionChanged(IAction action, ISelection selection) {
        action.setText(I18N.translate("viewActions.label.editTask"));
        action.setToolTipText(I18N.translate("viewActions.tooltip.editTask"));
        IStructuredSelection iss = (IStructuredSelection) selection;
        if (iss.isEmpty()) {
            action.setEnabled(false);
            task = null;
        } else {
            action.setEnabled(true);
            task = (WSTask) iss.getFirstElement();
        }
    }
}
