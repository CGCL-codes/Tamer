package de.ufinke.cubaja.config;

import java.util.HashMap;
import java.util.Map;

class NamedPropertyValue {

    private String name;

    private String provider;

    private Map<String, String> parms;

    NamedPropertyValue(String name, String provider) {
        this.name = name;
        this.provider = provider;
        parms = new HashMap<String, String>();
    }

    String getName() {
        return name;
    }

    String getProvider() {
        return provider;
    }

    void addParm(String parmName, String value) {
        parms.put(parmName, value);
    }

    Map<String, String> getParms() {
        return parms;
    }
}
