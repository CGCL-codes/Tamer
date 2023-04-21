package de.fu_berlin.inf.dpp;

import org.apache.log4j.Logger;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;
import org.picocontainer.annotations.Inject;
import de.fu_berlin.inf.dpp.accountManagement.XMPPAccountStore;
import de.fu_berlin.inf.dpp.annotations.Component;
import de.fu_berlin.inf.dpp.preferences.PreferenceConstants;
import de.fu_berlin.inf.dpp.preferences.PreferenceUtils;
import de.fu_berlin.inf.dpp.ui.SarosUI;
import de.fu_berlin.inf.dpp.ui.util.WizardUtils;
import de.fu_berlin.inf.dpp.util.Utils;

/**
 * An instance of this class is instantiated when Eclipse starts, after the
 * Saros plugin has been started.
 * 
 * {@link #earlyStartup()} is called after the workbench is initialized. <br>
 * <br>
 * Checks whether the release number changed.
 * 
 * @author Lisa Dohrmann, Sandor Szücs
 */
@Component(module = "integration")
public class StartupSaros implements IStartup {

    private static final Logger log = Logger.getLogger(StartupSaros.class);

    @Inject
    protected Saros saros;

    @Inject
    protected SarosUI sarosUI;

    @Inject
    protected XMPPAccountStore xmppAccountStore;

    @Inject
    protected PreferenceUtils preferenceUtils;

    public StartupSaros() {
        SarosPluginContext.reinject(this);
    }

    public void earlyStartup() {
        String currentVersion = saros.getVersion();
        String portNumber = System.getProperty("de.fu_berlin.inf.dpp.testmode");
        log.debug("de.fu_berlin.inf.dpp.testmode=" + portNumber);
        boolean testmode = portNumber != null;
        saros.getConfigPrefs().put(PreferenceConstants.SAROS_VERSION, currentVersion);
        saros.saveConfigPrefs();
        updateAccounts();
        if (testmode) return;
    }

    protected void showWizards(boolean showConfigurationWizard, boolean showGettingStartedWizard) {
        if (showGettingStartedWizard) WizardUtils.openSarosGettingStartedWizard(showConfigurationWizard);
        if (showConfigurationWizard) WizardUtils.openSarosConfigurationWizard();
    }

    protected void showSarosView() {
        Utils.runSafeSWTSync(log, new Runnable() {

            public void run() {
                IIntroManager m = PlatformUI.getWorkbench().getIntroManager();
                IIntroPart i = m.getIntro();
                if (i != null) return;
                sarosUI.openSarosView();
            }
        });
    }

    /**
     * This method searches for any XMPP accounts in the IPreferenceStore. For
     * each one it:
     * <ul>
     * <li>Copies the details to the ISecureStorage
     * <li>Resets the existing details in the IPreferenceStore to empty string
     * </ul>
     */
    protected void updateAccounts() {
        IPreferenceStore prefStore = saros.getPreferenceStore();
        ISecurePreferences secureStore = saros.getSecurePrefs();
        String username = prefStore.getString(PreferenceConstants.USERNAME);
        String server = prefStore.getString(PreferenceConstants.SERVER);
        String password = prefStore.getString(PreferenceConstants.PASSWORD);
        try {
            if (!username.equals("")) {
                secureStore.put(PreferenceConstants.USERNAME, username, false);
                secureStore.put(PreferenceConstants.SERVER, server, false);
                secureStore.put(PreferenceConstants.PASSWORD, password, false);
                prefStore.setValue(PreferenceConstants.USERNAME, "");
                prefStore.setValue(PreferenceConstants.SERVER, "");
                prefStore.setValue(PreferenceConstants.PASSWORD, "");
            }
            int i = 1;
            username = prefStore.getString(PreferenceConstants.USERNAME + i);
            server = prefStore.getString(PreferenceConstants.SERVER + i);
            password = prefStore.getString(PreferenceConstants.PASSWORD + i);
            while (!username.equals("")) {
                secureStore.put(PreferenceConstants.USERNAME + i, username, false);
                secureStore.put(PreferenceConstants.SERVER + i, server, false);
                secureStore.put(PreferenceConstants.PASSWORD + i, password, false);
                prefStore.setValue(PreferenceConstants.USERNAME + i, "");
                prefStore.setValue(PreferenceConstants.SERVER + i, "");
                prefStore.setValue(PreferenceConstants.PASSWORD + i, "");
                i++;
                username = prefStore.getString(PreferenceConstants.USERNAME + i);
                server = prefStore.getString(PreferenceConstants.SERVER + i);
                password = prefStore.getString(PreferenceConstants.PASSWORD + i);
            }
        } catch (StorageException e) {
            log.error("Exception with secure storage while upgrading: " + e.getMessage());
        }
        xmppAccountStore.loadAccounts();
    }
}
