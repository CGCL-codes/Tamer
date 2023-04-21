package xvrengine.launching;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.debug.core.model.IProcess;
import org.xvr.xvrengine.preferences.PreferenceConstants;
import org.xvr.xvrengine.process.IProcessExitListener;
import org.xvr.xvrengine.process.ProcessExitHandler;
import org.xvr.xvrengine.process.RunningVMs;
import org.xvr.xvrengine.util.XVRConstants;
import org.xvr.xvrengine.util.XVRUtils;
import xvrengine.builders.XVRBuilder;
import xvrengine.debug.XVRDebugTarget;
import xvrengine.ui.XVRProjectSupport;

public class ApplicationLaunchConfigurationDelegate implements ILaunchConfigurationDelegate, ILaunchConfigurationDelegate2 {

    private LaunchDispatcher dispatcher;

    private boolean stream;

    private boolean remote_application_running;

    private XVRConsoleStreamer c_streamer;

    public ApplicationLaunchConfigurationDelegate() {
        this.dispatcher = new LaunchDispatcher();
        this.remote_application_running = false;
        this.stream = false;
        this.c_streamer = new XVRConsoleStreamer();
    }

    public void addListener(ILaunchListener listener) {
        this.dispatcher.addLaunchListener(listener);
    }

    public void removeListener(ILaunchListener listener) {
        this.dispatcher.removeLaunchListener(listener);
    }

    public void setRemoteApplicationRunning(boolean running, String remote_user) {
        this.remote_application_running = running;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    /** The ID of the XVR launch configuration*/
    public static final String XVR_LAUNCH_CONFIGURATION_ID = "xvrengine.launching.XVR";

    private IProject active_prj = null;

    @Override
    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        String name = configuration.getAttribute(XVRConstants.XVR_LAUNCH_CONFIG_PROJECT_NAME_KEY, (String) null);
        IProject prj = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
        this.dispatcher.firePreLaunch(prj);
        IProjectDescription description = prj.getDescription();
        IProject[] referencedProjects = description.getReferencedProjects();
        for (IProject ref_prj : referencedProjects) XVRUtils.getXVRLaunchConfiguration(ref_prj).launch(mode, monitor);
        launchVirtualMachine(configuration, mode, launch, prj);
        this.dispatcher.fireLaunched(prj);
    }

    /**
	 * Updates the active project.
	 */
    private void updateActiveProject() {
        this.active_prj = XVRUtils.getActiveProject();
    }

    private void launchVirtualMachine(ILaunchConfiguration configuration, String mode, ILaunch launch, final IProject prj) throws CoreException {
        String vm_type = XVRUtils.getStringPreference(PreferenceConstants.RD_VM_USED);
        ArrayList<String> vmArgs;
        int consoleJobPort = findFreePort();
        int commandPort = findFreePort();
        int eventPort = findFreePort();
        if (vm_type.equals(PreferenceConstants.RD_GLUT)) vmArgs = this.launchGLUT(prj, mode, consoleJobPort, commandPort, eventPort); else if (vm_type.equals(PreferenceConstants.RD_IE)) vmArgs = this.launchIE(prj, mode, consoleJobPort, commandPort, eventPort); else vmArgs = this.launchFS(prj, mode, consoleJobPort, commandPort, eventPort);
        String[] commandLine = vmArgs.toArray(new String[vmArgs.size()]);
        Process process = DebugPlugin.exec(commandLine, new File(prj.getLocation().toString()));
        RunningVMs.getInstance().addProcess(process);
        Map<String, String> processAttributes = new HashMap<String, String>();
        processAttributes.put(IProcess.ATTR_PROCESS_TYPE, "XVR");
        IProcess p = DebugPlugin.newProcess(launch, process, "XVR Virtual Machine", processAttributes);
        if (this.stream) this.c_streamer.setStream(p.getStreamsProxy().getOutputStreamMonitor());
        ProcessExitHandler ph = new ProcessExitHandler(process);
        ph.addListener(new IProcessExitListener() {

            @Override
            public void processFinished() {
                if (stream) c_streamer.removeStreamMonitor();
                dispatcher.fireLaunchEnd(prj);
                XVRUtils.setDebugSession(false);
            }
        });
        ph.start();
        if (mode.equals(org.eclipse.debug.core.ILaunchManager.DEBUG_MODE)) {
            IDebugTarget target = new XVRDebugTarget(launch, p, commandPort, eventPort, ph);
            launch.addDebugTarget(target);
        }
    }

    public static int findFreePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            return socket.getLocalPort();
        } catch (IOException e) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        return -1;
    }

    @Override
    public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
        return null;
    }

    @Override
    public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
        try {
            buildProject(this.active_prj, monitor, mode);
        } catch (CoreException e) {
            if (mode.equals(ILaunchManager.DEBUG_MODE)) XVRUtils.setDebugSession(false);
            XVRUtils.displayWarning("The project compilation failed. Check the console for error(s).");
            throw e;
        }
        return false;
    }

    private void buildProject(IProject p, IProgressMonitor monitor, String mode) throws CoreException {
        int work = 100;
        SubMonitor subM = SubMonitor.convert(monitor, 100);
        subM.setTaskName("Building project " + p.getName());
        IProjectDescription description = p.getDescription();
        IProject[] referencedProjects = description.getReferencedProjects();
        int len = referencedProjects.length;
        if (len > 0) {
            int sub_work = (work / 2) / len;
            for (int i = 0; i < referencedProjects.length; i++) {
                buildProject(referencedProjects[i], new SubProgressMonitor(subM, sub_work), mode);
                subM.worked(sub_work);
            }
            work /= 2;
        }
        ICommand xvrBuilder = XVRUtils.getXVRBuilderCommand(p);
        Map<String, String> args = XVRUtils.getBuilderArgs(p);
        args.put(XVRConstants.BUILD_MODE, mode);
        xvrBuilder.setArguments(args);
        p.build(IncrementalProjectBuilder.FULL_BUILD, xvrBuilder.getBuilderName(), xvrBuilder.getArguments(), subM.newChild(work));
        subM.worked(work);
    }

    @Override
    public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
        return true;
    }

    @Override
    public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
        int work = 100;
        SubMonitor subM = SubMonitor.convert(monitor, work);
        subM.setTaskName("Validating precondition");
        subM.subTask("Checking active project");
        updateActiveProject();
        if (this.active_prj == null || !this.active_prj.exists()) {
            XVRUtils.displayError("No project selected to be launched.");
            if (mode.equals(ILaunchManager.DEBUG_MODE)) XVRUtils.setDebugSession(false);
            subM.worked(work);
            return false;
        }
        subM.worked(work / 2);
        return this.checkProject(this.active_prj, subM.newChild(work / 2));
    }

    private boolean checkProject(IProject p, IProgressMonitor monitor) throws CoreException {
        int work = 100;
        SubMonitor subM = SubMonitor.convert(monitor, work);
        subM.setTaskName("Checking project " + p.getName());
        IProjectDescription description = p.getDescription();
        IProject[] referencedProjects = description.getReferencedProjects();
        int len = referencedProjects.length;
        if (len > 0) {
            int sub_work = (work / 2) / len;
            for (int i = 0; i < referencedProjects.length; i++) {
                if (!checkProject(referencedProjects[i], new SubProgressMonitor(subM, sub_work))) {
                    subM.worked(work);
                    return false;
                }
                subM.worked(sub_work);
            }
            work /= 2;
        }
        subM.worked(work);
        String main = XVRUtils.getBuilderPreferece(XVRProjectSupport.ARG_MAIN_KEY, p);
        if (main == null) {
            XVRUtils.displayError("Main program not specified in project : " + p.getName());
            return false;
        }
        return true;
    }

    private ArrayList<String> launchGLUT(IProject prj, String mode, int consoleJobPort, int commandPort, int eventPort) throws CoreException {
        String main = (String) prj.getSessionProperty(XVRBuilder.KEY_MAIN_FILE);
        ArrayList<String> vmArgs = new ArrayList<String>();
        File vm = new File((String) prj.getSessionProperty(XVRBuilder.KEY_GLUT));
        String params = (String) prj.getSessionProperty(XVRBuilder.KEY_GLUT_PARAMS);
        if (!vm.exists()) XVRUtils.displayError(MessageFormat.format("Specified virtual machine {0} does not exist.", vm));
        vmArgs.add(vm.getPath());
        vmArgs.add(main + ".bin");
        if (params != null) {
            String[] plist = params.split(" ");
            for (int i = 0; i < plist.length; i++) vmArgs.add(plist[i]);
        }
        if (mode.equals(ILaunchManager.DEBUG_MODE)) {
            vmArgs.add("-EngineParam");
            vmArgs.add("DBGOUT=" + consoleJobPort + ";DBGCMD=" + commandPort + ";DBGEVT=" + eventPort);
        }
        if (!this.remote_application_running && this.stream) vmArgs.add("-stream");
        return vmArgs;
    }

    private ArrayList<String> launchFS(IProject prj, String mode, int consoleJobPort, int commandPort, int eventPort) throws CoreException {
        ArrayList<String> vmArgs = new ArrayList<String>();
        File vm = new File((String) prj.getSessionProperty(XVRBuilder.KEY_FS));
        String params = (String) prj.getSessionProperty(XVRBuilder.KEY_FS_PARAMS);
        if (!vm.exists()) XVRUtils.displayError(MessageFormat.format("Specified virtual machine {0} does not exist.", vm));
        vmArgs.add(vm.getPath());
        String htmlDefined = null;
        if (params != null) {
            String[] plist = params.split(" ");
            for (String temp : plist) {
                if (temp.startsWith("-h") && htmlDefined == null) {
                    htmlDefined = temp.substring(3);
                    File test = null;
                    test = XVRUtils.getFile(htmlDefined, prj);
                    if (test != null) temp = "-h=" + test.getAbsolutePath();
                }
                vmArgs.add(temp);
            }
        }
        if (htmlDefined == null) vmArgs.add("-h=" + prj.getLocation() + "/" + ((String) prj.getSessionProperty(XVRBuilder.KEY_MAIN_HTML)));
        return vmArgs;
    }

    private ArrayList<String> launchIE(IProject prj, String mode, int consoleJobPort, int commandPort, int eventPort) throws CoreException {
        String main = (String) prj.getSessionProperty(XVRBuilder.KEY_MAIN_FILE);
        ArrayList<String> vmArgs = new ArrayList<String>();
        File vm = new File(XVRUtils.getStringPreference(PreferenceConstants.RD_IE_PATH));
        if (!vm.exists()) XVRUtils.displayError(MessageFormat.format("Specified virtual machine {0} does not exist.", vm));
        vmArgs.add(vm.getAbsolutePath());
        String html = (String) prj.getSessionProperty(XVRBuilder.KEY_MAIN_HTML);
        IFile main_file = XVRUtils.getPrjFile(main + ".bin");
        String abs_path = main_file.getLocation().toString();
        String parameters = "file:///" + prj.getLocation() + "/" + html + "?scriptName=" + abs_path + "&";
        if (mode.equals(ILaunchManager.DEBUG_MODE)) parameters += "engineParam=DBGOUT=" + consoleJobPort + ";DBGCMD=" + commandPort + ";DBGEVT=" + eventPort;
        vmArgs.add(parameters);
        return vmArgs;
    }
}
