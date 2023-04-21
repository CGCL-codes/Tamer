package org.pixory.pxapplication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pixory.pxapplication.pages.composer.AdvancedConfiguration;
import org.pixory.pxapplication.pages.composer.BasicConfiguration;
import org.pixory.pxfoundation.PXFilePath;
import org.pixory.pxfoundation.PXStringUtility;
import org.pixory.pxfoundation.SZNotification;
import org.pixory.pxmodel.PXFileFilters;
import org.pixory.pxmodel.PXSettings;
import org.pixory.pxmodel.PXUserAccount;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PXComposerVisit extends PXApplicationVisit implements SZNotification.Observer {

    private static final Log LOG = LogFactory.getLog(PXComposerVisit.class);

    private static final String IGNORE_DIRS_SEPARATOR = ",";

    /**
	 * Map of currently logged in, active sessions/users, where key is
	 * (String)userName and value is (String)sessionId. This Map is
	 * *synchronized*
	 */
    private static Map _authenticatedSessionMap;

    private PXNavigator _navigator;

    private int _lightboxSelectionIndex;

    private String[] _ignoreDirectoryNames;

    public PXComposerVisit() {
        this(null, null);
    }

    public PXComposerVisit(PXUserAccount userAccount, PXApplicationVisit originalVisit_) {
        super(userAccount, originalVisit_);
        LOG.info("creating visit");
        SZNotification.Center.defaultCenter().addObserver(this, BasicConfiguration.DID_CHANGE_NOTIFICATION_NAME, null);
        SZNotification.Center.defaultCenter().addObserver(this, AdvancedConfiguration.DID_CHANGE_NOTIFICATION_NAME, null);
        this.configurationChanged();
    }

    public static String getAuthenticatedSessionId(String username) {
        String getAuthenticatedSessionId = null;
        if (username != null) {
            Map aSessionMap = getAuthenticatedSessionMap();
            getAuthenticatedSessionId = (String) aSessionMap.get(username);
        }
        return getAuthenticatedSessionId;
    }

    public static void setAuthenticatedSessionId(String username, String sessionId) {
        if ((username != null) && (sessionId != null)) {
            Map aSessionMap = getAuthenticatedSessionMap();
            aSessionMap.put(username, sessionId);
            LOG.info("set username: " + username + " sessionId: " + sessionId);
        } else {
            String aMessage = "setAuthenticatedUsername() does not accept null args";
            throw new IllegalArgumentException(aMessage);
        }
    }

    public static void removeAuthenticatedSessionId(String username) {
        if (username != null) {
            Map aSessionMap = getAuthenticatedSessionMap();
            aSessionMap.remove(username);
        }
    }

    private static Map getAuthenticatedSessionMap() {
        if (_authenticatedSessionMap == null) {
            _authenticatedSessionMap = Collections.synchronizedMap(new HashMap());
        }
        return _authenticatedSessionMap;
    }

    public int getLightboxSelectionIndex() {
        return _lightboxSelectionIndex;
    }

    public void setLightboxSelectionIndex(int lightboxSelectionIndex) {
        _lightboxSelectionIndex = lightboxSelectionIndex;
    }

    /**
     * Previously this method fetched ignoreDirectories from each user
     * I think this was just used for the adminUser. So now fetch it from Settings.
     * So it isn't tied to any user anymore, but to global settings.
     *
     */
    private String[] getIgnoreDirectoryNames() {
        if (_ignoreDirectoryNames == null) {
            PXSettings settings = PXSettings.getSettings();
            String anIgnoreString = settings.getIgnoreDirectories();
            LOG.debug("anIgnoreString: " + anIgnoreString);
            if (anIgnoreString != null) {
                _ignoreDirectoryNames = PXStringUtility.tokens(anIgnoreString, IGNORE_DIRS_SEPARATOR);
                if (_ignoreDirectoryNames != null) {
                    for (int i = 0; i < _ignoreDirectoryNames.length; i++) {
                        _ignoreDirectoryNames[i] = _ignoreDirectoryNames[i].trim();
                    }
                }
            }
        }
        if (_ignoreDirectoryNames == null) {
            _ignoreDirectoryNames = new String[0];
        }
        if (_ignoreDirectoryNames.length > 0) {
            return _ignoreDirectoryNames;
        } else {
            return null;
        }
    }

    public PXNavigator getNavigator() {
        if (_navigator == null) {
            _navigator = new PXNavigator();
        }
        return _navigator;
    }

    /**
	 * a convenience method that extracts the targetPath from the Navigator and
	 * tries to interpret it as an album file path
	 */
    public PXFilePath getCurrentPathUnderAlbums() {
        return this.getNavigator().getPathUnderImageRoot();
    }

    /**
	 * extend the super implementation
	 */
    public void cleanupVisit(PXApplicationEngine engine) {
        LOG.debug("cleaning up...");
        SZNotification.Center.defaultCenter().removeObserver(this, BasicConfiguration.DID_CHANGE_NOTIFICATION_NAME, null);
        SZNotification.Center.defaultCenter().removeObserver(this, AdvancedConfiguration.DID_CHANGE_NOTIFICATION_NAME, null);
        super.cleanupVisit(engine);
    }

    /**
	 * @see org.pixory.pxfoundation.SZNotification.Observer#notify(SZNotification)
	 */
    public void notify(SZNotification aNotification) {
        String aName = aNotification.name();
        if (aName.equals(BasicConfiguration.DID_CHANGE_NOTIFICATION_NAME) || aName.equals(AdvancedConfiguration.DID_CHANGE_NOTIFICATION_NAME)) {
            this.configurationChanged();
        }
    }

    private void configurationChanged() {
        _ignoreDirectoryNames = null;
        String[] anIgnoreDirNames = this.getIgnoreDirectoryNames();
        LOG.debug("anIgnoreDirNames: " + anIgnoreDirNames);
        PXFileFilters.setIgnoreDirNames(anIgnoreDirNames);
    }
}
