package de.mpiwg.vspace.oaw.generation;

import java.util.HashMap;
import java.util.Map;

public class AdditionalPropertyRegistry {

    public static final AdditionalPropertyRegistry INSTANCE = new AdditionalPropertyRegistry();

    private Map<String, String> additionalProperties;

    private AdditionalPropertyRegistry() {
        init();
    }

    public void init() {
        additionalProperties = new HashMap<String, String>();
    }

    public void addProperty(String key, String value) {
        additionalProperties.put(key, value);
    }

    public void removeProperty(String key) {
        additionalProperties.remove(key);
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }
}
