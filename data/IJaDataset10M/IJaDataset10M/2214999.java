package org.mss.quartzjobs.actions;

import java.text.ParseException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.mss.quartzjobs.CorePlugin;
import org.osgi.framework.Bundle;
import org.quartz.SchedulerException;

public class StartQuartzAction implements IWorkbenchWindowActionDelegate, IPropertyChangeListener {

    private IAction action;

    public StartQuartzAction() {
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        try {
            CorePlugin.getDefault().getQuartzUtil().startscheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void propertyChange(PropertyChangeEvent event) {
    }
}
