package org.wayuus.wajas;

import org.wayuus.wajas.config.WajasServerHandlerSupport;

/**
 * @author scusin
 */
public class DemoWajasServerHandler extends WajasServerHandlerSupport {

    public static final String[] TEST_DEFAULT_CONFIGS_LOCATIONS = new String[] { "test/server/init/init.xml" };

    public static final String TEST_VERSION = "0.0.1";

    /**
	 * 
	 */
    public DemoWajasServerHandler() {
        setVersion(TEST_VERSION);
    }

    public DemoWajasServerHandler(boolean useTestInitFile) {
        this();
        if (useTestInitFile) setConfigLocations(TEST_DEFAULT_CONFIGS_LOCATIONS);
    }
}
