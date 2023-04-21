package net.sf.beatrix.launch;

import net.sf.beatrix.launch.util.Environment;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

public class BeatrixLaunchDelegate implements ILaunchConfigurationDelegate {

    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        String parameters = Environment.replaceVariables(configuration.getAttribute(BeatrixConfigurationConstants.BEATRIX_LAUNCH_ARGUMENTS, ""));
        String config = Environment.replaceVariables(configuration.getAttribute(BeatrixConfigurationConstants.BEATRIX_LAUNCH_CONFIGURATION, ""));
        IPath path = new Path(config);
        StringBuilder sb = new StringBuilder(parameters);
        sb.append(" --config ");
        sb.append(config);
        LaunchTarget target = new LaunchTarget(path.lastSegment(), sb.toString());
        if (ILaunchManager.DEBUG_MODE.equals(mode)) {
            LaunchManager.getInstance("ide").addDebugTarget(target);
        } else {
            LaunchManager.getInstance("ide").addRunTarget(target);
        }
    }
}
