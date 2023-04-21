package com.google.gwt.gadgets.client;

import com.google.gwt.gadgets.client.impl.PreferencesFeatureImpl;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to retrieve an instance of {@link PreferencesFeature}. This
 * class is designed to allow a unit test to add a mock implementation of
 * {@link PreferencesFeature}.
 */
public class PreferencesProvider {

    private static PreferencesFeature preferences;

    private static Map<String, PreferencesFeature> preferencesMap = new HashMap<String, PreferencesFeature>();

    /**
   * Use this method to override the implementation of preferencesFeature
   * (useful for unit testing).
   * 
   * @param preferencesFeature A class that implements a way to set and retrieve
   *          Preferences.
   */
    public static void set(PreferencesFeature preferencesFeature) {
        preferences = preferencesFeature;
    }

    /**
   * Use this method to override the implementation of preferencesFeature for a
   * specific module (useful for unit testing)
   * 
   * @param moduleId identifier for a gadgets module.
   * @param preferencesFeature A class that implements a way to set and retrieve
   *          Preferences.
   */
    public static void set(String moduleId, PreferencesFeature preferencesFeature) {
        preferencesMap.put(moduleId, preferencesFeature);
    }

    /**
   * To remove any previously set {@link PreferencesFeature} implementation.
   */
    public static void reset() {
        preferences = null;
        preferencesMap.clear();
    }

    /**
   * Retrieve a copy of the global {@link PreferencesFeature}.
   */
    public static PreferencesFeature get() {
        if (preferences == null) {
            preferences = PreferencesFeatureImpl.get();
        }
        return preferences;
    }

    /**
   * Retrieve a copy of the global {@link PreferencesFeature}.
   */
    public static PreferencesFeature get(String moduleId) {
        if (preferencesMap.get(moduleId) == null) {
            preferencesMap.put(moduleId, PreferencesFeatureImpl.get(moduleId));
        }
        return preferencesMap.get(moduleId);
    }
}
