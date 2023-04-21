package com.evaserver.rof;

import com.evaserver.rof.util.ROFScriptParserConfig;

/**
 *
 *
 * @author Max Antoni
 * @version $Revision: 68 $
 */
public class WindowManagerFactory {

    private static final String RESOURCE_PATH = WindowManagerFactory.class.getPackage().getName().replace('.', '/');

    public static WindowManager newInstance() {
        WindowManager windowManager = new WindowManager(ROFScriptParserConfig.INSTANCE);
        windowManager.setTemplateResourcePath(RESOURCE_PATH);
        windowManager.setCacheScripts(false);
        windowManager.setLoadBrowserConfigurationsFromClasspath(false);
        windowManager.init();
        return windowManager;
    }
}
