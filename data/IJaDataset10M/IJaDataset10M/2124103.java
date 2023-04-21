package org.gudy.azureus2.platform;

import java.util.Properties;
import org.gudy.azureus2.platform.unix.PlatformManagerUnixPlugin;
import org.gudy.azureus2.plugins.Plugin;
import org.gudy.azureus2.plugins.PluginException;
import org.gudy.azureus2.plugins.PluginInterface;
import org.gudy.azureus2.plugins.update.UpdatableComponent;
import org.gudy.azureus2.plugins.update.UpdateChecker;

/**
 * @author TuxPaper
 * @created Jul 24, 2007
 *
 */
public class PlatformManagerPluginDelegate implements Plugin, UpdatableComponent {

    public void initialize(PluginInterface pluginInterface) throws PluginException {
        PlatformManager platform = PlatformManagerFactory.getPlatformManager();
        int platformType = platform.getPlatformType();
        if (platformType == PlatformManager.PT_WINDOWS) {
            org.gudy.azureus2.platform.win32.PlatformManagerUpdateChecker plugin = new org.gudy.azureus2.platform.win32.PlatformManagerUpdateChecker();
            plugin.initialize(pluginInterface);
        } else if (platformType == PlatformManager.PT_MACOSX) {
            org.gudy.azureus2.platform.macosx.PlatformManagerUpdateChecker plugin = new org.gudy.azureus2.platform.macosx.PlatformManagerUpdateChecker();
            plugin.initialize(pluginInterface);
        } else if (platformType == PlatformManager.PT_UNIX) {
            PlatformManagerUnixPlugin plugin = new PlatformManagerUnixPlugin();
            plugin.initialize(pluginInterface);
        } else {
            Properties pluginProperties = pluginInterface.getPluginProperties();
            pluginProperties.setProperty("plugin.name", "Platform-Specific Support");
            pluginProperties.setProperty("plugin.version", "1.0");
            pluginProperties.setProperty("plugin.version.info", "Not required for this platform");
        }
    }

    public String getName() {
        return ("Mixin only");
    }

    public int getMaximumCheckTime() {
        return (0);
    }

    public void checkForUpdate(UpdateChecker checker) {
    }
}
