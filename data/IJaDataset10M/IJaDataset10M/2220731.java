package apple.eclipse.debug;

import java.io.File;
import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;

public class LaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        try {
            String command = "java -cp bin;lib/log4j-1.2.14.jar apple.ui.awt.AppleII";
            String[] envp = new String[0];
            File dir = new File("C:/eclipse-workspace/javapple2");
            Process p = Runtime.getRuntime().exec(command, envp, dir);
            launch.addDebugTarget(new DebugTarget(launch, p));
        } catch (IOException e) {
            throw new CoreException(new Status(IStatus.ERROR, "com.ckelly.plugin", e.getLocalizedMessage(), e));
        }
    }
}
