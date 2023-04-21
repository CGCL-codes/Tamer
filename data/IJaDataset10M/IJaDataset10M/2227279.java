package archmapper.main.codegenerator;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class CodeGeneratorLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        ILaunchConfigurationTab commonTab = new CommonTab();
        ILaunchConfigurationTab settingsTab = new CodeGeneratorLaunchConfigurationTab(dialog);
        setTabs(new ILaunchConfigurationTab[] { settingsTab, commonTab });
    }
}
