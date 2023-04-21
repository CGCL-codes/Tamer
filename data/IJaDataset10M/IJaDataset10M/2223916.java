package com.ibm.celldt.managedbuilder.xl.ui;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ManagedOptionValueHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import com.ibm.celldt.managedbuilder.xl.ui.internal.XlManagedMakeMessages;

/**
 * @author laggarcia
 * @since 3.0.0
 */
public class VmxValueHandler extends ManagedOptionValueHandler {

    public VmxValueHandler() {
    }

    /**
	 * Handles transfer between values between UI element and back-end in
	 * different circumstances. extraArgument must be the id of the other option
	 * in which this option depends on.
	 * 
	 * @param configuration
	 *            build configuration of option (may be IConfiguration or
	 *            IResourceConfiguration)
	 * @param holder
	 *            contains the holder of the option
	 * @param option
	 *            the option that is handled
	 * @param extraArgument
	 *            extra argument for handler
	 * @param event
	 *            event to be handled
	 * 
	 * @return True when the event was handled, false otherwise. This enables
	 *         default event handling can take place.
	 */
    public boolean handleValue(IBuildObject configuration, IHoldsOptions holder, IOption option, String extraArgument, int event) {
        if (event == EVENT_APPLY) {
            try {
                if (option.getBooleanValue() == false) {
                    IOption otherOption = holder.getOptionBySuperClassId(extraArgument);
                    if (otherOption.getBooleanValue() == true) {
                        MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), XlManagedMakeMessages.VmxWarningDialogTitle, NLS.bind(XlManagedMakeMessages.VmxWarningDialogMessage, option.getCommand(), otherOption.getCommand()));
                    }
                }
            } catch (BuildException be) {
                be.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
