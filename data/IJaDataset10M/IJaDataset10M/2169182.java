package lslplus.launching;

import lslplus.LslPlusPlugin;
import lslplus.LslProjectNature;
import lslplus.SimManager;
import lslplus.debug.LslDebugTarget;
import lslplus.debug.LslSimProcess;
import lslplus.debug.LslSourceLocator;
import lslplus.sim.SimKeyManager;
import lslplus.sim.SimProject;
import lslplus.sim.SimWorldDef;
import lslplus.util.Util;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.jface.dialogs.MessageDialog;

public class SimLaunchDelegate extends LaunchConfigurationDelegate {

    private static final String BLANK = "";

    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
        Util.log("launch!!!");
        String fullPath = configuration.getAttribute(LaunchLslTestShortcut.LC_RESOURCE_NAME, BLANK);
        Path path = new Path(fullPath);
        IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
        if (resource == null) {
            MessageDialog.openError(null, "Resource no longer exists", "Referenced resource no longer exists!");
            return;
        }
        SimWorldDef def = null;
        SimProject.WorldNode world = (SimProject.WorldNode) resource.getAdapter(SimProject.WorldNode.class);
        if (world == null) {
            String name = LslProjectNature.resourceToLslPlusName(resource);
            def = SimWorldDef.mkSimpleWorld(new SimKeyManager(), name);
        } else {
            def = SimProject.toSimWorldDef(world);
        }
        LslProjectNature nature = (LslProjectNature) resource.getProject().getNature(LslProjectNature.ID);
        String sourceDescriptor = nature.projectSourceList();
        String testDescriptor = "<sim-descriptor>" + sourceDescriptor + SimWorldDef.toXML(def) + "</sim-descriptor>";
        if (LslPlusPlugin.DEBUG) Util.log(testDescriptor);
        LslSimProcess p = new LslSimProcess(testDescriptor, launch);
        LslDebugTarget target = new LslDebugTarget("lslplus-test", launch, p);
        p.go();
        simManager().simLaunched(p);
        launch.addDebugTarget(target);
        launch.addProcess(p);
        launch.setSourceLocator(new LslSourceLocator());
    }

    private SimManager simManager() {
        return LslPlusPlugin.getDefault().getSimManager();
    }
}
