package org.eclipse.dltk.launching;

import java.io.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.ILaunch;

/**
 * {@link RunnableProcess} with DLTK launching data.
 * 
 * This class could interest DLTK project?
 */
public abstract class DLTKRunnableProcess extends RunnableProcess {

    protected final IInterpreterInstall install;

    protected final InterpreterConfig config;

    protected final ILaunch launch;

    public DLTKRunnableProcess(IInterpreterInstall install, ILaunch launch, InterpreterConfig config) {
        super(null, false);
        this.install = install;
        this.config = config;
        this.launch = launch;
        super.start();
    }

    /**
	 * Returns the file where user has done Run As->Freemarker.
	 * 
	 * @return
	 */
    protected File getFile() {
        return config.getScriptFilePath().toFile();
    }

    protected IFile getResourcesFile() {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        return root.getFile(config.getScriptFilePath().makeRelativeTo(root.getLocation()));
    }
}
