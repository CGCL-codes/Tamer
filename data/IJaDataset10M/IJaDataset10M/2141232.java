package jimo.osgi.modules.core;

import java.util.*;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * Creates a copy of the properties associated with a service registration.
 * Checks that all the keys are strings and adds the class names.
 * Note! Creation of PropertiesDictionary must be synchronized.
 *
 * @author Jan Stein
 */
public class ConfigurationDictionary extends Dictionary {

    private String[] keys;

    private Object[] values;

    private int size;

    private int pidIndex = -1;

    private int fidIndex = -1;

    private int locIndex = -1;

    public ConfigurationDictionary(Dictionary in) {
        int max_size = in != null ? in.size() + 3 : 3;
        keys = new String[max_size];
        values = new Object[max_size];
        size = 0;
        if (in != null) {
            try {
                for (Enumeration e = in.keys(); e.hasMoreElements(); ) {
                    String key = (String) e.nextElement();
                    if (pidIndex == -1 && key.equalsIgnoreCase(Constants.SERVICE_PID)) {
                        pidIndex = size;
                    } else if (fidIndex == -1 && key.equalsIgnoreCase(ConfigurationAdmin.SERVICE_FACTORYPID)) {
                        fidIndex = size;
                    } else if (locIndex == -1 && key.equalsIgnoreCase(ConfigurationAdmin.SERVICE_BUNDLELOCATION)) {
                        locIndex = size;
                    } else {
                        for (int i = size - 1; i >= 0; i--) {
                            if (key.equalsIgnoreCase(keys[i])) {
                                throw new IllegalArgumentException("Several entries for property: " + key);
                            }
                        }
                    }
                    keys[size] = key;
                    values[size++] = in.get(key);
                }
            } catch (ClassCastException ignore) {
                throw new IllegalArgumentException("Properties contains key that is not of type java.lang.String");
            }
        }
    }

    public Object get(Object key) {
        if (key == Constants.SERVICE_PID) {
            return (pidIndex >= 0) ? values[pidIndex] : null;
        } else if (key == ConfigurationAdmin.SERVICE_FACTORYPID) {
            return (fidIndex >= 0) ? values[fidIndex] : null;
        } else if (key == ConfigurationAdmin.SERVICE_BUNDLELOCATION) {
            return (locIndex >= 0) ? values[locIndex] : null;
        }
        for (int i = size - 1; i >= 0; i--) {
            if (((String) key).equalsIgnoreCase(keys[i])) {
                return values[i];
            }
        }
        return null;
    }

    public String[] keyArray() {
        if (keys.length != size) {
            String[] nkeys = new String[size];
            System.arraycopy(keys, 0, nkeys, 0, size);
            keys = nkeys;
        }
        return (String[]) keys.clone();
    }

    public int size() {
        return size;
    }

    public Enumeration elements() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Enumeration keys() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Object put(Object k, Object v) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Object remove(Object k) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
