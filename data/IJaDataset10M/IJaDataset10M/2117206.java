package org.radrails.rails.internal.ui.actions;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;
import org.radrails.rails.core.RailsLog;
import org.radrails.rails.internal.core.RailsPlugin;
import org.radrails.rails.internal.ui.RailsUIMessages;
import org.rubypeople.rdt.internal.debug.ui.RdtDebugUiPlugin;
import org.rubypeople.rdt.launching.IRubyLaunchConfigurationConstants;
import org.rubypeople.rdt.launching.RubyRuntime;

public class ScriptConsolePulldownDelegate implements IWorkbenchWindowPulldownDelegate {

    private IWorkbenchWindow fWindow;

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        fWindow = window;
    }

    public void run(IAction action) {
        runConsole("development");
    }

    private void runConsole(String env) {
        try {
            IProject project = RailsPlugin.getSelectedOrOnlyRailsProject();
            if (project != null) {
                ILaunchConfiguration config = createConsoleLaunchConfiguration(project, env);
                config.launch(ILaunchManager.RUN_MODE, null);
            } else {
                MessageDialog.openError(fWindow.getShell(), "Error opening console", RailsUIMessages.SelectRailsProject_message);
            }
        } catch (CoreException e) {
            RailsLog.logError("Error running generator", e);
        }
    }

    /**
	 * Grab an IFile reference to the script\console file inside our rails
	 * project.
	 * 
	 * @return
	 */
    private IFile getConsoleScript(IProject iproject) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IPath path = iproject.getLocation().append("script").append("console");
        return workspace.getRoot().getFileForLocation(path);
    }

    private ILaunchManager getLaunchManager() {
        return DebugPlugin.getDefault().getLaunchManager();
    }

    private ILaunchConfigurationType getRubyApplicationConfigType() {
        return getLaunchManager().getLaunchConfigurationType(IRubyLaunchConfigurationConstants.ID_RUBY_APPLICATION);
    }

    private static String getDefaultWorkingDirectory(IProject project) {
        if (project != null && project.exists()) {
            return project.getLocation().toOSString();
        }
        return RdtDebugUiPlugin.getWorkspace().getRoot().getLocation().toOSString();
    }

    private ILaunchConfiguration createConsoleLaunchConfiguration(IProject project, String env) {
        IFile rubyFile = getConsoleScript(project);
        ILaunchConfiguration config = null;
        try {
            String name = project.getName() + " script/console " + env;
            ILaunchConfigurationType configType = getRubyApplicationConfigType();
            ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, getLaunchManager().generateUniqueLaunchConfigurationNameFrom(name));
            wc.setAttribute(IRubyLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getName());
            wc.setAttribute(IRubyLaunchConfigurationConstants.ATTR_FILE_NAME, rubyFile.getProjectRelativePath().toString());
            wc.setAttribute(IRubyLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, getDefaultWorkingDirectory(project));
            wc.setAttribute(IRubyLaunchConfigurationConstants.ATTR_VM_INSTALL_NAME, RubyRuntime.getDefaultVMInstall().getName());
            wc.setAttribute(IRubyLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE, RubyRuntime.getDefaultVMInstall().getVMInstallType().getId());
            wc.setAttribute(ILaunchConfiguration.ATTR_SOURCE_LOCATOR_ID, "org.rubypeople.rdt.debug.ui.rubySourceLocator");
            wc.setAttribute(IRubyLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, getProgramArguments(env));
            wc.setAttribute(IRubyLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-e STDOUT.sync=true -e STDERR.sync=true -e load(ARGV.shift)");
            Map<String, String> map = new HashMap<String, String>();
            map.put(IRubyLaunchConfigurationConstants.ATTR_RUBY_COMMAND, "ruby");
            wc.setAttribute(IRubyLaunchConfigurationConstants.ATTR_VM_INSTALL_TYPE_SPECIFIC_ATTRS_MAP, map);
            config = wc.doSave();
        } catch (CoreException ce) {
        }
        return config;
    }

    private String getProgramArguments(String env) {
        StringBuffer buffer = new StringBuffer(env);
        String irb = RubyRuntime.getIRB().getAbsolutePath();
        if (irb.indexOf(' ') == -1) {
            buffer.append(" --irb=");
            buffer.append(irb);
        }
        return buffer.toString();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public Menu getMenu(Control parent) {
        Menu m = new Menu(parent);
        createMenu(m, "test");
        createMenu(m, "development");
        createMenu(m, "production");
        return m;
    }

    private void createMenu(Menu parent, final String environment) {
        MenuItem test = new MenuItem(parent, SWT.PUSH);
        test.setText("script/console (" + environment + ")");
        test.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                runConsole(environment);
            }
        });
    }
}
