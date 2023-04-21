package net.sf.openforge.util;

import java.util.*;

/**
 * Provides simple named key :: object mappoing for classes that need to store complex
 * objects by name
 *
 * @author <a href="mailto:Jonathan.Harris@xilinx.com">Jonathan
 * C. Harris</a>
 * @version $Id
 */
public class NamedValueManager {

    private static final String rcs_id = "RCS_REVISION: $Rev: 2 $";

    private HashMap values = new HashMap();

    public NamedValueManager() {
        ;
    }

    public Object getNamedValue(String name) {
        return values.get(name);
    }

    public void putNamedValue(String name, Object o) {
        values.put(name, o);
    }

    public boolean containsNamedValue(String name) {
        return values.containsKey(name);
    }

    public void removeNamedValue(String name) {
        values.remove(name);
    }

    public void clearNamedValues() {
        values.clear();
    }

    public Map getNamedValueMap() {
        return values;
    }
}
