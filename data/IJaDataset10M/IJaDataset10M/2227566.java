package com.google.gdt.eclipse.suite.launch.ui;

import com.google.gdt.eclipse.core.CorePluginLog;
import com.google.gdt.eclipse.platform.debug.ui.WorkingDirectoryBlock;
import com.google.gdt.eclipse.suite.launch.processors.WarArgumentProcessor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * A WorkingDirectoryBlock to customize the displayed value for the default
 * working directory text box.
 * 
 * The value set on the default working directory text box is a fixed string
 * instead of being up-to-date with the current WAR directory. The reason is
 * when the user changes the WAR directory in the program arguments, there is
 * not a clean (non-reflective) way of updating the text of the default working
 * dir text box without the superclass automatically reseting the launch
 * configuration to use the default working directory. See the superclass's
 * implementation of setDefaultWorkingDirectoryText.
 */
public class WebAppWorkingDirectoryBlock extends WorkingDirectoryBlock {

    private static final String DEFAULT_WAR_DIRECTORY_STRING = "WAR directory";

    @Override
    public boolean isValid(ILaunchConfiguration config) {
        if (DEFAULT_WAR_DIRECTORY_STRING.equals(getWorkingDirectoryText())) {
            setErrorMessage(null);
            setMessage(null);
            return true;
        } else {
            return super.isValid(config);
        }
    }

    @Override
    protected void setDefaultWorkingDir() {
        ILaunchConfiguration config = getLaunchConfiguration();
        try {
            if (WarArgumentProcessor.doesMainTypeTakeWarArgument(config)) {
                setDefaultWorkingDirectoryText(DEFAULT_WAR_DIRECTORY_STRING);
                return;
            }
        } catch (CoreException e) {
            CorePluginLog.logWarning(e, "Could not set default working directory");
        }
        super.setDefaultWorkingDir();
    }
}
