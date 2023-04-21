package com.amd.javalabs.tools.caplugin.launching;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaJRETab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaMainTab;

/**
 * The various tabs that make up a CodeSleuth launch configuration.
 * 
 * Note that we basically 'extend' the default set foe a Java launch.
 * 
 * @author gfrost
 * 
 * http://www.eclipse.org/articles/Article-Launch-Framework/launch.html
 */
public class TabGroup extends AbstractLaunchConfigurationTabGroup {

    /**
    * Create/Set the various Tab pages used by this configuration UI.
    * 
    * Had to comment out the SourceLookupTab. Kept getting 'no source locator error'
    * 
    * @param dialog The dialog we are building/configuring
    * @param mode Either DEBUG or RUN
    */
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        setTabs(new ILaunchConfigurationTab[] { new Tab(), new JavaMainTab(), new JavaArgumentsTab(), new JavaJRETab(), new JavaClasspathTab(), new EnvironmentTab(), new CommonTab() });
    }
}
