package com.sun.midp.appmanager;

import com.sun.midp.midletsuite.*;

/**
 * This is a common interface for Application managers.
 * Application manager controls midlet life cycle:
 *    - installs and updates midlet suites
 *    - launches, moves to foreground and exits midlets
 *    - shuts down the AMS system
 * It is used by the AppSelector.
 */
interface ApplicationManager {

    /** Discover and install a suite. */
    void installSuite();

    /** Launch the CA manager. */
    void launchCaManager();

    /**
     * Launches a suite.
     *
     * @param suiteInfo information for suite to launch
     * @param midletToRun class name of the MIDlet to launch
     */
    void launchSuite(RunningMIDletSuiteInfo suiteInfo, String midletToRun);

    /**
     * Update a suite.
     *
     * @param suiteInfo information for suite to update
     */
    void updateSuite(RunningMIDletSuiteInfo suiteInfo);

    /**
     * Shut downt the system
     */
    void shutDown();

    /**
     * Bring the midlet with the passed in midlet suite info to the 
     * foreground.
     * 
     * @param suiteInfo information for the midlet to be put to foreground
     */
    void moveToForeground(RunningMIDletSuiteInfo suiteInfo);

    /**
     * Exit the midlet with the passed in midlet suite info.
     * 
     * @param suiteInfo information for the midlet to be terminated
     */
    void exitMidlet(RunningMIDletSuiteInfo suiteInfo);
}
