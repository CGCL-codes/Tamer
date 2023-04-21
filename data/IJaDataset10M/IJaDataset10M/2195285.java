package hambo.mobiledirectory;

import hambo.app.core.HamboAbstractApplication;
import hambo.app.core.HamboApplicationManager;
import hambo.config.Config;

/**
 * Mobile Directory Application object. Currently there is no configuration to
 * initialize for Mobile Directory. This class is empty.
 */
public class MobileDirectoryApplication extends HamboAbstractApplication {

    public static boolean IS_SOURCE = false;

    /**
     * Do the initialization stuff. Like reading config and
     * adding event listeners.
     * @param config configuration object for the application.
     */
    public boolean doInit(Config config) {
        IS_SOURCE = config.getPropertyAsBoolean("source");
        HamboApplicationManager man = HamboApplicationManager.getApplicationManager();
        man.addUserLoginListener(new DirectoryLoginListener());
        return true;
    }
}
