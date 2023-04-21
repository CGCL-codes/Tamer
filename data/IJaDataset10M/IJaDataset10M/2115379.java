package org.itsnat.impl.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jmarranz
 */
public class UserDataImpl implements UserData {

    protected Map userData = new HashMap();

    /**
     * Creates a new instance of UserDataImpl
     */
    public UserDataImpl() {
    }

    public Map getInternalMap() {
        return userData;
    }

    public String[] getUserDataNames() {
        Map userData = getInternalMap();
        String[] names = new String[userData.size()];
        return (String[]) userData.keySet().toArray(names);
    }

    public boolean containsName(String name) {
        Map userData = getInternalMap();
        return userData.containsKey(name);
    }

    public Object getUserData(String name) {
        Map userData = getInternalMap();
        return userData.get(name);
    }

    public Object setUserData(String name, Object value) {
        Map userData = getInternalMap();
        return userData.put(name, value);
    }

    public Object removeUserData(String name) {
        Map userData = getInternalMap();
        return userData.remove(name);
    }
}
