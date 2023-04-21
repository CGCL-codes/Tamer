package com.ivis.xprocess.app;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import com.ivis.xprocess.ui.UIPlugin;

/**
 * The Release Notes action that appears under the Helper menu.
 * Displays the changes.txt found under com.ivis.xprocess, that
 * displays all the changes since v2 up to the current release.
 *
 */
public class ReleaseNotesAction implements IWorkbenchWindowActionDelegate {

    public boolean isEnabled() {
        return true;
    }

    public void run(IAction action) {
        String pluginLocation = UIPlugin.getDefault().getPluginLocation();
        String pluginName = pluginLocation.substring(pluginLocation.indexOf("com.ivis.xprocess.ui"), pluginLocation.length() - 1);
        String numberToAdd = "";
        int index = pluginName.indexOf("_");
        if (index > 0) {
            numberToAdd = pluginName.substring(index);
        }
        String path = pluginLocation;
        path = path.substring(0, path.indexOf("com.ivis.xprocess.ui"));
        path = path + "com.ivis.xprocess.app" + numberToAdd + "/changes.txt";
        Program p = Program.findProgram(".txt");
        if (p != null) {
            p.execute(path);
        } else {
            UIPlugin.log("Unable to find default application to open - " + path, IStatus.ERROR, null);
        }
    }

    public boolean isHandled() {
        return true;
    }

    public String getActionName() {
        return "Release Notes";
    }

    public boolean canShow() {
        return true;
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow arg0) {
    }

    public void selectionChanged(IAction arg0, ISelection arg1) {
    }
}
