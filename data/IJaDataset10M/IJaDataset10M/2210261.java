package de.innot.avreclipse.debug.core;

import org.eclipse.cdt.core.settings.model.CConfigurationStatus;
import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.debug.gdbjtag.core.GDBJtagDSFLaunchConfigurationDelegate;
import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.gdb.launching.LaunchMessages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import de.innot.avreclipse.debug.core.service.AVRDebugServicesFactory;
import de.innot.avreclipse.debug.gdbserver.IGDBServerFactory;

/**
 * @author Michael
 * 
 */
public class AVRGDBLaunchDelegate extends GDBJtagDSFLaunchConfigurationDelegate {

    @Override
    public void launch(ILaunchConfiguration config, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        org.eclipse.cdt.launch.LaunchUtils.enableActivity("org.eclipse.cdt.debug.dsfgdbActivity", true);
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (mode.equals(ILaunchManager.DEBUG_MODE)) {
            launchGDBServer(config, launch, monitor);
        }
        super.launch(config, mode, launch, monitor);
    }

    private void launchGDBServer(ILaunchConfiguration config, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        monitor.beginTask(LaunchMessages.getString("GdbLaunchDelegate.0"), 10);
        if (monitor.isCanceled()) {
            return;
        }
        try {
            String serverId = config.getAttribute(IAVRGDBConstants.ATTR_GDBSERVER_ID, "");
            IGDBServerFactory factory = AVRDebugPlugin.getDefault().getGDBServerFactories().get(serverId);
            if (factory == null) {
                abort("Selected GDB Server not available", null, ICDTLaunchConfigurationConstants.ERR_INTERNAL_ERROR);
            }
            factory.launchServer(config, launch, monitor);
        } finally {
            monitor.done();
        }
    }

    protected IDsfDebugServicesFactory newServiceFactory(String version) {
        return new AVRDebugServicesFactory(version);
    }

    @Override
    protected String getPluginID() {
        return AVRDebugPlugin.PLUGIN_ID;
    }
}
