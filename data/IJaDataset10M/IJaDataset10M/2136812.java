package org.nakedobjects.ide.core.launches;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;

@SuppressWarnings("restriction")
public class LaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

    public LaunchConfigurationTabGroup() {
    }

    /**
     * @see ILaunchConfigurationTabGroup#createTabs(ILaunchConfigurationDialog, String)
     */
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] { new NakedObjectsMainTab(), new NakedObjectsArgumentsTab(), new JavaJRETab(), new JavaClasspathTab(), new EnvironmentTab(), new CommonTab() };
        setTabs(tabs);
    }
}
