package es.gavab.eclipse.pascalfc.launching;

import java.io.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import es.gavab.eclipse.pascalfc.PascalFCCorePlugin;
import es.gavab.eclipse.pascalfc.compiler.PFCCompInterpreter;

public class PascalfcLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

    /**
	 * constructor PascalLaunchConfigurationDelegate
	 */
    public PascalfcLaunchConfigurationDelegate() {
    }

    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        String EMPTY_STRING = "";
        String pascalProject = configuration.getAttribute(PascalfcLaunchConfigurationConstants.ATTR_PROJECT_NAME, EMPTY_STRING);
        String pascalProgram = configuration.getAttribute(PascalfcLaunchConfigurationConstants.ATTR_PASCALFC_PROGRAM_NAME, EMPTY_STRING);
        String arguments = configuration.getAttribute(PascalfcLaunchConfigurationConstants.ATTR_PASCALFC_ARGUMENTS, EMPTY_STRING);
        IFile file = (IFile) ResourcesPlugin.getWorkspace().getRoot().getProject(pascalProject).getFile(pascalProgram);
        File interPath = PascalFCCorePlugin.getDefault().getStateLocation().toFile();
        try {
            PFCCompInterpreter pfcCompInt = new PFCCompInterpreter(interPath);
            File srcSource = file.getRawLocation().toFile();
            pfcCompInt.execute(srcSource, srcSource.getParentFile(), arguments);
        } catch (Exception e) {
            PascalFCCorePlugin.log(e.getMessage(), e);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            PascalFCCorePlugin.log(e1.getMessage(), e1);
        }
        try {
            file.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (CoreException e) {
            PascalFCCorePlugin.log(e.getMessage(), e);
        }
    }
}
