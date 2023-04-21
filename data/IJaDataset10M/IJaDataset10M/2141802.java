package de.beas.explicanto.client.rcp.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.dialogs.TaskFilterDialog;
import de.beas.explicanto.client.rcp.views.TaskFilter;
import de.beas.explicanto.client.rcp.views.TasksView;

public class SetupTaskFilterAction implements IViewActionDelegate {

    private TasksView tv;

    public void init(IViewPart view) {
        tv = (TasksView) view;
    }

    public void run(IAction action) {
        TaskFilterDialog tfd = new TaskFilterDialog(tv.getViewSite().getShell());
        tfd.open();
        if (!tfd.isOk()) {
            action.setChecked(false);
            tv.removeFilter();
            tv.refresh(null);
        } else {
            TaskFilter filter = new TaskFilter(tfd.getType(), tfd.getSearchString());
            tv.setFilter(filter);
            tv.refresh(null);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        action.setText(I18N.translate("viewActions.label.setupFilter"));
        action.setToolTipText(I18N.translate("viewActions.tooltip.setupFilter"));
    }
}
