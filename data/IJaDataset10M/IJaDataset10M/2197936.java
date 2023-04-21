package edu.upmc.opi.caBIG.caTIES.client.config;

import java.util.HashMap;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Constants;

public class PropertyDefaults {

    private static HashMap<String, String> valueMap = new HashMap<String, String>();

    static {
        valueMap.put(CaTIES_Constants.PROPERTY_KEY_APPLICATION_CONFIG_FILE, "config/application.properties");
        valueMap.put(CaTIES_Constants.PROPERTY_KEY_MMTX_CONFIG_FILE, "/config/mmtxRegistry.cfg");
    }

    public static String getDefaultForProperty(String propertyName) {
        return valueMap.get(propertyName);
    }
}
