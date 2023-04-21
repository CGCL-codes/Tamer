package com.google.gdt.eclipse.suite.launch.ui;

import com.google.gdt.eclipse.core.CorePluginLog;
import com.google.gdt.eclipse.core.ResourceUtils;
import com.google.gdt.eclipse.suite.launch.WebAppLaunchUtil;
import com.google.gdt.eclipse.suite.propertytesters.LaunchTargetTester;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;

/**
 * Launch shortcut for -noserver launches of Web Applications.
 */
public class WebAppNoServerLaunchShortcut extends WebAppLaunchShortcut {

    @Override
    protected void launch(IResource resource, String mode) {
        assert (new LaunchTargetTester().test(resource, null, new Object[0], null));
        resource = ResourceUtils.resolveTargetResource(resource);
        try {
            String startupUrl = WebAppLaunchUtil.determineStartupURL(resource, true);
            if (startupUrl != null) {
                ILaunchConfiguration config = findOrCreateLaunchConfiguration(resource, startupUrl, true);
                assert (config != null);
                DebugUITools.launch(config, mode);
            }
        } catch (CoreException e) {
            CorePluginLog.logError(e);
        } catch (OperationCanceledException e) {
        }
    }
}
