package org.eclipseguru.gwt.core.launch;

import org.eclipseguru.gwt.core.GwtCore;
import org.eclipseguru.gwt.core.GwtProject;
import org.eclipseguru.gwt.core.runtimes.GwtRuntime;
import org.eclipseguru.gwt.core.utils.ProgressUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A launch delegate for launching the GWT browser.
 */
public class GwtBrowserLaunchDelegate extends AbstractJavaLaunchConfigurationDelegate implements ILaunchConfigurationDelegate, GwtLaunchConstants {

    /** CLASS_NAME_GWTSHELL */
    private static final String CLASS_NAME_DEVMODE_RUNNER = "com.google.gwt.dev.DevMode";

    /**
	 * Computes the classpath for the specified project and configuration.
	 * 
	 * @param project
	 * @param configuration
	 * @return
	 * @throws CoreException
	 */
    private String[] computeClasspath(final GwtProject project, final ILaunchConfiguration configuration) throws CoreException {
        final List<String> classpath = new ArrayList<String>();
        GwtLaunchUtil.addSourceFolderToClasspath(project, classpath, true);
        final GwtRuntime runtime = GwtCore.getRuntime(project);
        final String[] entries = runtime.getGwtRuntimeClasspath();
        classpath.addAll(Arrays.asList(entries));
        classpath.addAll(Arrays.asList(getClasspath(configuration)));
        return classpath.toArray(new String[classpath.size()]);
    }

    /**
	 * Computes the classpath for the specified project and configuration.
	 * 
	 * @param project
	 * @param configuration
	 * @return
	 * @throws CoreException
	 */
    private String[] computeVmArgs(final GwtProject project, final ILaunchConfiguration configuration) throws CoreException {
        final List<String> vmArgs = new ArrayList<String>();
        vmArgs.addAll(Arrays.asList(DebugPlugin.parseArguments(getVMArguments(configuration))));
        final GwtRuntime runtime = GwtCore.getRuntime(project);
        final String[] runtimeVmArgs = runtime.getGwtRuntimeVmArgs();
        for (final String arg : runtimeVmArgs) {
            if (!vmArgs.contains(arg)) {
                vmArgs.add(arg);
            }
        }
        return vmArgs.toArray(new String[vmArgs.size()]);
    }

    public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch, IProgressMonitor monitor) throws CoreException {
        monitor = ProgressUtil.monitor(monitor);
        try {
            monitor.beginTask(MessageFormat.format("{0}...", configuration.getName()), 3);
            if (monitor.isCanceled()) {
                return;
            }
            monitor.subTask("Verifying launch attributes...");
            final GwtProject project = GwtLaunchUtil.verifyProject(configuration);
            final IVMRunner runner = getVMRunner(configuration, mode);
            final File workingDir = verifyWorkingDirectory(configuration);
            String workingDirName = null;
            if (workingDir != null) {
                workingDirName = workingDir.getAbsolutePath();
            }
            final String[] envp = getEnvironment(configuration);
            final String[] pgmArgs = GwtLaunchUtil.getGwtDevShellArguments(configuration);
            final String[] vmArgs = computeVmArgs(project, configuration);
            final Map vmAttributesMap = getVMSpecificAttributesMap(configuration);
            final String[] classpath = computeClasspath(project, configuration);
            final VMRunnerConfiguration runConfig = new VMRunnerConfiguration(CLASS_NAME_DEVMODE_RUNNER, classpath);
            runConfig.setProgramArguments(pgmArgs);
            runConfig.setEnvironment(envp);
            runConfig.setVMArguments(vmArgs);
            runConfig.setWorkingDirectory(workingDirName);
            runConfig.setVMSpecificAttributesMap(vmAttributesMap);
            runConfig.setBootClassPath(getBootpath(configuration));
            if (monitor.isCanceled()) {
                return;
            }
            prepareStopInMain(configuration);
            monitor.worked(1);
            monitor.subTask("Creating source locator...");
            setDefaultSourceLocator(launch, configuration);
            monitor.worked(1);
            runner.run(runConfig, launch, monitor);
            if (monitor.isCanceled()) {
                return;
            }
        } finally {
            monitor.done();
        }
    }
}
