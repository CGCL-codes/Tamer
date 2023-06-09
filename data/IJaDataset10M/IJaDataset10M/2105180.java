package org.openmobster.core.mobileCloud.api.ui.framework.navigation;

import java.util.Map;
import java.util.HashMap;

/**
 * @author openmobster@gmail.com
 *
 */
final class ScreenManager {

    private static ScreenManager singleton;

    private Map<String, ScreenMetaData> registry;

    private ScreenManager() {
        this.registry = new HashMap<String, ScreenMetaData>();
    }

    static ScreenManager getInstance() {
        if (ScreenManager.singleton == null) {
            synchronized (ScreenManager.class) {
                if (ScreenManager.singleton == null) {
                    ScreenManager.singleton = new ScreenManager();
                }
            }
        }
        return ScreenManager.singleton;
    }

    static void stopSingleton() {
        ScreenManager.singleton = null;
    }

    void register(String id, Screen screen) {
        ScreenMetaData metadata = new ScreenMetaData();
        metadata.screen = screen;
        metadata.isRendered = false;
        screen.setId(id);
        this.registry.put(id, metadata);
    }

    Screen find(String id) {
        ScreenMetaData metadata = this.registry.get(id);
        if (metadata != null) {
            return metadata.screen;
        }
        return null;
    }

    void render(String id) {
        ScreenMetaData metadata = this.registry.get(id);
        if (metadata != null && !metadata.isRendered) {
            metadata.screen.render();
            metadata.isRendered = true;
        }
    }

    void forceRender(String id) {
        ScreenMetaData metadata = this.registry.get(id);
        if (metadata != null) {
            metadata.screen.render();
            metadata.isRendered = true;
        }
    }

    private static class ScreenMetaData {

        private Screen screen;

        private boolean isRendered;
    }
}
