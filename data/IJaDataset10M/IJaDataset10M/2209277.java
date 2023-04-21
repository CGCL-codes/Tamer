package com.android.ide.eclipse.adt;

import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchController;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.ddms.IDebuggerConnector;
import org.eclipse.core.resources.IProject;

/**
 * Implementation of the com.android.ide.ddms.debuggerConnector extension point.
 */
public class DebuggerConnector implements IDebuggerConnector {

    public boolean connectDebugger(String appName, int appPort, int selectedPort) {
        IProject project = ProjectHelper.findAndroidProjectByAppName(appName);
        if (project != null) {
            AndroidLaunchController.debugRunningApp(project, appPort);
            return true;
        }
        return false;
    }
}
