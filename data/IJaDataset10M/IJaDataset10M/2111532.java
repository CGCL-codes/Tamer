package org.nakedobjects.ide.core.launches;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class OtherArgumentsBlock extends AbstractArgumentBlock {

    private Text argumentsText;

    public OtherArgumentsBlock(NakedObjectsArgumentsTab nakedObjectsArgumentsTab) {
        super(nakedObjectsArgumentsTab);
    }

    public void createControl(Composite parent) {
        Font font = parent.getFont();
        Group group = new Group(parent, SWT.NONE);
        group.setFont(font);
        GridLayout layout = new GridLayout();
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        String controlName = (LauncherMessages.JavaArgumentsTab__Program_arguments__5);
        group.setText(controlName);
        argumentsText = new Text(group, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 40;
        gd.widthHint = 100;
        argumentsText.setLayoutData(gd);
        argumentsText.setFont(font);
        argumentsText.addModifyListener(new MakeDirtyModifyListener(getTab()));
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration, StringBuilder argumentsBuffer) {
        LaunchUtils.performApply(argumentsBuffer, "", argumentsText, configuration, LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_ADDITIONAL);
    }

    public void initializeFrom(ILaunchConfiguration configuration) throws CoreException {
        String additionalArgs = configuration.getAttribute(LaunchConstants.LAUNCH_CONFIGURATION_ATTR_ARG_ADDITIONAL, "");
        argumentsText.setText(additionalArgs);
    }
}
