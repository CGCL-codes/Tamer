package com.amd.javalabs.tools.caplugin.launching;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.JavaRuntime;
import com.amd.javalabs.tools.camodel.ModelConfiguration;
import com.amd.javalabs.tools.camodel.ModelFactory;
import com.amd.javalabs.tools.caplugin.CodeSleuthPlugin;
import com.amd.javalabs.tools.caplugin.Messages;

/**
 * A LaunchDelegate to handle the launching of CodeAnalysts caprofile extensions to the JavaLaunchDelegate.
 * 
 * @author gfrost
 * 
 */
public class LaunchConfigurationDelegate extends JavaLaunchDelegate {

    /**
    * Causes eclipse to resynch the files for this project after we have added/removed files.
    * 
    * @param configuration The launch configuration which contains project information (including working dir).
    * @throws CoreException
    */
    public void refreshWorkingDirectory(ILaunchConfiguration configuration) throws CoreException {
        IPath path = getWorkingDirectoryPath(configuration);
        if (path == null) {
            IJavaProject jp = getJavaProject(configuration);
            if (jp != null) {
                IProject p = jp.getProject();
                if (p != null) {
                    p.refreshLocal(IResource.DEPTH_INFINITE, null);
                }
            }
        }
    }

    public void addProjectInfo(ILaunchConfiguration _launchConfiguration, ModelConfiguration _modelConfiguration) throws CoreException {
        IPath path = getWorkingDirectoryPath(_launchConfiguration);
        if (path == null) {
            IJavaProject jp = getJavaProject(_launchConfiguration);
            _modelConfiguration.setProjectName(jp.getProject().getName());
        }
    }

    /**
    * Actually launches Caprofile from CodeAnalyst.
    * 
    * In truth we first delegate (super.launch()) to the JavaLauncher and then deal with the executiom of cadataanalyse and
    * careport.
    * 
    * @param configuration The configuration as collected by the LaunchConfiguration wizard.
    * @param mode (RUN or DEBUG). We should not get here if this is a DEBUG!
    * @param ILaunch We use this to actually launch the process
    * @param monitor A progress monitor we use to report our progress.
    */
    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch _launch, IProgressMonitor monitor) throws CoreException {
        File workingDir = verifyWorkingDirectory(configuration);
        ModelConfiguration modelConfiguration = new ModelConfiguration();
        CALaunch launch = new CALaunch(_launch, modelConfiguration);
        modelConfiguration.setCreationDate(new Date());
        try {
            File commonJitFormatFile = File.createTempFile(modelConfiguration.getCreationDateString(), ".dat");
            modelConfiguration.setCommonFormatJitFileName(commonJitFormatFile.toString());
            ModelFactory.getInstance().cleanDirs(workingDir);
            if (monitor == null) {
                monitor = new NullProgressMonitor();
            }
            monitor.beginTask("caprofile", 20);
            super.launch(configuration, mode, launch, monitor);
            LaunchSpinHelper.spin(launch);
            monitor.worked(5);
            if (commonJitFormatFile.exists()) {
                LaunchableCommandLine commandLine = new CADataAnalyseCommandLine();
                String analyseOut = commandLine.launch("cadataanalyse", launch, workingDir, monitor);
                String commandLineString = commandLine.toString();
                CodeSleuthPlugin.logErrorMessage("cadataanalyze =" + commandLineString);
                modelConfiguration.setDataAnalyseText(analyseOut.trim());
                modelConfiguration.setDataAnalyseCommandLine(commandLine);
                addProjectInfo(configuration, modelConfiguration);
                String suffix = configuration.getAttribute(CodeSleuthPlugin.PLUGIN_ID + ".USE_EVENT_PROFILING", false) ? "ebp" : "tbp";
                File eventDataFile = new File(new File(workingDir, "data." + suffix + ".dir"), "data." + suffix);
                ModelFactory.getInstance().buildModel(modelConfiguration, commonJitFormatFile, eventDataFile, workingDir);
            } else {
                CodeSleuthPlugin.logErrorMessage("JVMTIA agent failed to create an output file " + commonJitFormatFile);
                abort(Messages.LauncherAgentFailedToCreateOutputFile, null, 0);
            }
            refreshWorkingDirectory(configuration);
            monitor.done();
        } catch (IOException e) {
            abort(Messages.LauncherFailedToCreateTempFileForAgent, e, 0);
        }
    }

    /**
    * We intercept this method to ensure we create an Install implementation.
    * 
    * @see Install
    */
    @Override
    public IVMInstall getVMInstall(ILaunchConfiguration configuration) throws CoreException {
        return new Install(JavaRuntime.computeVMInstall(configuration));
    }
}
