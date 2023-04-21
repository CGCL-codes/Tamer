package com.explosion.utilities.preferences.persist;

import java.util.Map;
import java.util.prefs.Preferences;
import com.explosion.utilities.preferences.impl.inmemory.InMemoryPreferencePersister;
import com.explosion.utilities.preferences.impl.sun.SunPreferencePersister;
import com.explosion.utilities.preferences.impl.xml.XMLPreferencePersistenceRequirement;
import com.explosion.utilities.preferences.impl.xml.XMLPreferencePersister;

/**
 * @author Stephen Cowx
 * Created on 14-Mar-2005
 */
public class PreferencePersisterFactory {

    /**
     * 
     */
    public PreferencePersisterFactory() {
    }

    public static PreferencePersister createPreferencePersister(Object referenceToStore) {
        PreferencePersister persister = null;
        if (referenceToStore instanceof Map) {
            return InMemoryPreferencePersister.createPreferencePersistor(referenceToStore);
        } else if (referenceToStore instanceof Preferences) {
            return SunPreferencePersister.createPreferencePersistor(referenceToStore);
        } else if (referenceToStore instanceof XMLPreferencePersistenceRequirement) {
            return XMLPreferencePersister.createPreferencePersistor(referenceToStore);
        }
        return persister;
    }
}
