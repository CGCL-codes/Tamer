package org.netbeans.module.flexbean.modules.platform;

import java.io.IOException;
import org.netbeans.module.flexbean.api.platform.Platform;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;

/**
 *
 * @author arnaud
 */
public final class PlatformManager {

    private static final String PLATFORMS_REGISTRY_FOLDER = "Services/Platforms/org-netbeans-module-flexbean-platform";

    private static PlatformManager _manager;

    private final FileObject _registry = Repository.getDefault().getDefaultFileSystem().findResource(PLATFORMS_REGISTRY_FOLDER);

    private PlatformManager() {
    }

    public static PlatformManager getDefault() {
        if (_manager == null) {
            _manager = new PlatformManager();
        }
        return _manager;
    }

    public Platform[] getPlatforms() {
        final FileObject[] refs = _registry.getChildren();
        final Platform[] platforms = new Platform[refs.length];
        for (int ix = 0; ix < refs.length; ix++) {
            platforms[ix] = _loadFromRegistry(refs[ix]);
        }
        return platforms;
    }

    public boolean addPlatform(Platform platform) {
        boolean result = false;
        if (platform != null) {
            result = _contains(platform);
            if (!result) {
                try {
                    _writeToRegistry(platform);
                    result = true;
                } catch (IOException ex) {
                    ErrorManager.getDefault().notify(ErrorManager.EXCEPTION, ex);
                }
            }
        }
        return result;
    }

    private boolean _contains(Platform platform) {
        boolean result = false;
        if (platform != null) {
            final FileObject[] refs = _registry.getChildren();
            for (int ix = 0; !result && ix < refs.length; ix++) {
                String sdkCustomerName = (String) refs[ix].getAttribute("name");
                result = sdkCustomerName.equalsIgnoreCase(platform.getPlatformName());
            }
        }
        return result;
    }

    private Platform _loadFromRegistry(FileObject registryEntry) {
        Platform result = null;
        PlatformBuilder builder = PlatformFactory.getDefault().getBuilder(PlatformType.FLEX);
        result = builder.loadFromRegistryEntry(registryEntry);
        return result;
    }

    private void _writeToRegistry(Platform platform) throws IOException {
        if (platform != null) {
            PlatformBuilder builder = PlatformFactory.getDefault().getBuilder(PlatformType.FLEX);
            builder.saveToRegistry(_registry, platform);
        }
    }
}
