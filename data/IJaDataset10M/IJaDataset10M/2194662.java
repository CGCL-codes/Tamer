package org.rubypeople.rdt.internal.debug.ui.tests.launcher;

import junit.framework.TestCase;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.rubypeople.eclipse.shams.debug.core.ShamLaunchConfigurationWorkingCopy;
import org.rubypeople.rdt.internal.debug.ui.RdtDebugUiMessages;
import org.rubypeople.rdt.internal.debug.ui.launcher.RubyEntryPointTab;
import org.rubypeople.rdt.internal.launching.RubyLaunchConfigurationAttribute;

public class TC_RubyEntryPointTab extends TestCase {

    public TC_RubyEntryPointTab(String name) {
        super(name);
    }

    public void testIsValid() {
        RubyEntryPointTab tab = new RubyEntryPointTab();
        ILaunchConfigurationWorkingCopy configuration = new ShamLaunchConfigurationWorkingCopy();
        String errorMessage = tab.getErrorMessage();
        assertNull("There should be no error message.", errorMessage);
        assertTrue("The tab is not valid when the configuration is completely empty.", !tab.isValid(configuration));
        errorMessage = RdtDebugUiMessages.getString("LaunchConfigurationTab.RubyEntryPoint.invalidProjectSelectionMessage");
        assertEquals("The tab should set the error message for no project.", errorMessage, tab.getErrorMessage());
        configuration.setAttribute(RubyLaunchConfigurationAttribute.PROJECT_NAME, "myProjectName");
        assertTrue("The tab is not valid when the configuration has only a projectname.", !tab.isValid(configuration));
        errorMessage = RdtDebugUiMessages.getString("LaunchConfigurationTab.RubyEntryPoint.invalidFileSelectionMessage");
        assertEquals("The tab should set the error message for no file.", errorMessage, tab.getErrorMessage());
        configuration.setAttribute(RubyLaunchConfigurationAttribute.FILE_NAME, "myFileName");
        assertTrue("The tab is valid when the configuration has a filename and projectName.", tab.isValid(configuration));
        assertNull("The tab should set the error message to null when there is a filename and projectname.", tab.getErrorMessage());
    }
}
