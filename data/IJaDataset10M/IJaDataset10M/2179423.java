package org.dbe.composer.wfengine.bpel.server.deploy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.dbe.composer.wfengine.bpel.deploy.SdlDeployConstants;

/**
 * Enumeration class for process persistence type.
 */
public class SdlProcessPersistenceType {

    public static final SdlProcessPersistenceType FULL = new SdlProcessPersistenceType(SdlDeployConstants.PERSISTENCE_TYPE_FULL);

    public static final SdlProcessPersistenceType FINAL = new SdlProcessPersistenceType(SdlDeployConstants.PERSISTENCE_TYPE_FINAL);

    public static final SdlProcessPersistenceType NONE = new SdlProcessPersistenceType(SdlDeployConstants.PERSISTENCE_TYPE_NONE);

    /** Array of all persistence types. */
    private static final SdlProcessPersistenceType[] sTypes = new SdlProcessPersistenceType[] { FULL, FINAL, NONE };

    /** Maps type names to type instances. */
    private static Map sTypesMap;

    /** Name of persistence type. */
    private final String mName;

    /**
     * Constructs persistence type instance from name.
     *
     * @param aName
     */
    protected SdlProcessPersistenceType(String aName) {
        mName = aName;
    }

    /**
     * Returns persistence type instance for specified name.
     * If no matching type is found, returns the default persistence type (full).
     */
    public static SdlProcessPersistenceType getPersistenceType(String aName) {
        SdlProcessPersistenceType result = (SdlProcessPersistenceType) getTypesMap().get(aName);
        if (result == null) {
            result = FULL;
        }
        return result;
    }

    /**
     * Returns name of persistence type.
     */
    protected String getName() {
        return mName;
    }

    /**
     * Returns map from type names to type instances.
     */
    protected static Map getTypesMap() {
        if (sTypesMap == null) {
            Map map = new HashMap();
            for (int i = 0; i < sTypes.length; ++i) {
                map.put(sTypes[i].getName(), sTypes[i]);
            }
            sTypesMap = Collections.unmodifiableMap(map);
        }
        return sTypesMap;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName();
    }
}
