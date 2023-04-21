package com.ibm.celldt.managedbuilder.core;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IResourceConfiguration;
import org.eclipse.cdt.managedbuilder.core.ManagedOptionValueHandler;

/**
 * @author laggarcia
 * @since 3.0.0
 */
public class EspecialLibraryShortcutValueHandler extends ManagedOptionValueHandler {

    protected static final String EMPTY_STRING = "";

    public EspecialLibraryShortcutValueHandler() {
    }

    /**
	 * Handles transfer between values between UI element and back-end in
	 * different circumstances. extraArgument must be the id of the
	 * option that holds all the libraries that are used by the project.
	 * 
	 * If the especial library option that references this ManagedOptionValueHandler is selected,the corresponding library should be included in the StringList option that holds all the libraries used by this project.
	 * If the especial library option that references this ManagedOptionValueHandler is deselected, the corresponding library should be removed in the StringList option that holds all the libraries used by this project.
	 * 
	 * For more information about the relationship between these especial library options and the StringList option that holds all the libraries used by this project, see com.ibm.celldt.managedbuilder.core.LibraryListValueHandler. 
	 * 
	 *  @param configuration  build configuration of option 
	 *                        (may be IConfiguration or IResourceConfiguration)
	 *  @param holder         contains the holder of the option
	 *  @param option         the option that is handled
	 *  @param extraArgument  extra argument for handler
	 *  @param event          event to be handled 
	 * 
	 *  @return  True when the event was handled, false otherwise.
	 *  This enables default event handling can take place.
	 * 
	 */
    public boolean handleValue(IBuildObject configuration, IHoldsOptions holder, IOption option, String extraArgument, int event) {
        if (event == EVENT_APPLY) {
            IOption librariesOption = holder.getOptionBySuperClassId(extraArgument);
            String especialLibrary = option.getCommand().replaceFirst(librariesOption.getCommand(), EMPTY_STRING);
            String[] newLibraries;
            try {
                String[] libraries = librariesOption.getLibraries();
                if (option.getBooleanValue() == true) {
                    if (getNumberOfEspecialLibraryInList(libraries, especialLibrary) == 0) {
                        newLibraries = new String[libraries.length + 1];
                        int i;
                        for (i = 0; i < libraries.length; i++) {
                            newLibraries[i] = libraries[i];
                        }
                        newLibraries[i] = especialLibrary;
                        setValue(configuration, holder, librariesOption, newLibraries);
                    }
                } else {
                    newLibraries = new String[libraries.length - getNumberOfEspecialLibraryInList(libraries, especialLibrary)];
                    for (int i = 0, j = 0; i < libraries.length; i++) {
                        if (!libraries[i].equals(especialLibrary)) {
                            newLibraries[j] = libraries[i];
                            j++;
                        }
                    }
                    setValue(configuration, holder, librariesOption, newLibraries);
                }
            } catch (BuildException be) {
                be.printStackTrace();
            }
            return true;
        }
        return false;
    }

    protected int getNumberOfEspecialLibraryInList(String[] libraries, String especialLibrary) {
        int numberOfEspecialLibrary = 0;
        for (int i = 0; i < libraries.length; i++) {
            if (libraries[i].equals(especialLibrary)) {
                numberOfEspecialLibrary++;
            }
        }
        return numberOfEspecialLibrary;
    }

    /**
	 * 
	 * @param configuration
	 *            build configuration of option (may be IConfiguration or
	 *            IResourceConfiguration)
	 * @param holder
	 *            contains the holder of the option
	 * @param option
	 *            the option that is handled
	 * @param value
	 *            value to be set in to the option
	 */
    protected void setValue(IBuildObject configuration, IHoldsOptions holder, IOption option, String[] value) throws BuildException {
        if (configuration instanceof IConfiguration) {
            ((IConfiguration) configuration).setOption(holder, option, value);
        } else if (configuration instanceof IResourceConfiguration) {
            ((IResourceConfiguration) configuration).setOption(holder, option, value);
        }
    }
}
