package org.netbeans.module.flexbean.modules.platform;

import org.netbeans.module.flexbean.spi.platform.PlatformInstaller;

/**
 *
 * @author arnaud
 */
public class PlatformInstallersProvider implements org.netbeans.module.flexbean.spi.platform.PlatformInstallersProvider {

    public PlatformInstaller[] getPlatformInstallers() {
        return new PlatformInstaller[] { new org.netbeans.module.flexbean.modules.platform.flex.wizard.FlexPlatformInstaller() };
    }

    public PlatformInstaller getPlatformInstaller(String type) {
        PlatformInstaller result = null;
        PlatformInstaller[] installers = getPlatformInstallers();
        for (int count = 0; count < installers.length; count++) {
            if (installers[count].getPlatformType().equalsIgnoreCase(type)) {
                result = installers[count];
                break;
            }
        }
        return result;
    }
}
