package com.jcorporate.expresso.kernel;

import com.jcorporate.expresso.kernel.exception.InstallationException;

/**
 * This interface represents a component that needs to perform install/uninstall
 * work other than recieve straight configuration values. An example of this is a
 * Schema.  All Schemas are installable, for example, because tables need to be created,
 * default security set up, etc.  And these tables may need to be removed upon
 * uninstallation
 * @author Michael Rimov
 * @version $Revision: 3 $ on  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 * @since Expresso 5.1
 */
public interface Installable {

    /**
     * Called when the Service Manager installs this component
     * @param log A 'log4j-like' interface that allows you to output progress
     * in the form of warnings, errors, info messages etc.  The underlying implementation
     * is often not log4j.
     */
    public void install(InstallationOptions installOptions, InstallLog log) throws InstallationException;

    /**
     * Called when the Service Manager uninstalls this component
     * @param uninstallLog A 'log4j-like' interface that allows you to output progress
     * in the form of warnings, errors, info messages etc.  The underlying implementation
     * is often not log4j.
     */
    public void uninstall(InstallationOptions installOptions, InstallLog uninstallLog) throws InstallationException;

    /**
     * This method is used to query the object for any Installation Options.
     * The implementing component may return null if there are no special options
     * for the job.
     * @return Created and filled out installation objects or null;
     */
    public InstallationOptions getInstallationOptions();

    /**
     * This method is used to query the Installable object for any uninstallation
     * options.  The implementing component may return null if there are no
     * special options for the job.  Although the 'type' returned is the same, as
     * getInstallOptions(), obviously, their contents are not the same. :)
     * @return Created and filled out InstallationOptions object or null;
     */
    public InstallationOptions getUninstallOptions();
}
