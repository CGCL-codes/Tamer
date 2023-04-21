package org.wtc.eclipse.platform.reset;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.wtc.eclipse.core.reset.IResetDaemon;
import org.wtc.eclipse.platform.util.ExceptionHandler;
import com.windowtester.runtime.IUIContext;

/**
 * Reset daemon that makes sure that workspace auto build is turned on.
 */
public class AutoBuildResetDaemon implements IResetDaemon {

    /**
     * @see  org.wtc.eclipse.core.reset.IResetDaemon#resetWorkspace(com.windowtester.runtime.IUIContext,
     *       org.wtc.eclipse.core.reset.IResetDaemon.ResetContext)
     */
    public void resetWorkspace(IUIContext ui, ResetContext context) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceDescription description = workspace.getDescription();
        if (!description.isAutoBuilding()) {
            description.setAutoBuilding(true);
            try {
                workspace.setDescription(description);
            } catch (CoreException e) {
                ExceptionHandler.handle(e);
            }
        }
    }
}
