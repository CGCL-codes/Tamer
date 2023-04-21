package com.newisys.joveutils.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import com.newisys.joveutils.logging.LogInterface;
import com.newisys.joveutils.external.JunoSupport;

/**
 * Contains a list of all the properties that will be used by a given simulation.
 * Provides a method to register properties.
 * Provides a method to do an end of run check.
 * @author scott.diesing
 *
 */
public class PropertyRegister {

    private final Properties leftOverProps = JunoSupport.getProperties();

    public static final PropertyRegister INSTANCE = new PropertyRegister();

    private final Map<String, JunoProperty> map = new HashMap<String, JunoProperty>();

    private final LogInterface logIntf;

    private PropertyRegister() {
        logIntf = new LogInterface("PropertyRegister", this);
    }

    /**
     * Adds the property to the property map and removes the property from the 
     * list of properties found in juno.ini and on the command line.
     * @param property
     */
    public final void registerProperty(JunoProperty property) {
        map.put(property.name, property);
    }

    /**
     * Checks to see if all the properties that were specified through juno.ini and 
     * on the command line have been used.
     * @return
     */
    public final boolean allPropertiesUsed() {
        for (String prop : InternalPropertyGlobals.propertiesRequested.keySet()) {
            leftOverProps.remove(stripEqual(prop));
        }
        leftOverProps.remove("javaclass");
        leftOverProps.remove("javaopt");
        leftOverProps.remove("javaclasspath");
        leftOverProps.remove("vera_2SR_config");
        leftOverProps.remove("initmem");
        leftOverProps.remove("CSR_CFG_FILE");
        Set<Object> keys = leftOverProps.keySet();
        boolean allUsed = true;
        for (Object obj : keys) {
            String key = (String) obj;
            if (key.charAt(0) != '#' && key.charAt(0) != '/' && (key.lastIndexOf("define+") != 0) && (key.lastIndexOf("rpa_") != 0)) {
                allUsed = false;
            }
        }
        return allUsed;
    }

    public final String unusedPropsToString() {
        String rvalue = "";
        Set<Object> keys = leftOverProps.keySet();
        for (Object obj : keys) {
            String key = (String) obj;
            if (key.charAt(0) != '#' && key.charAt(0) != '/' && (key.lastIndexOf("define+") != 0)) {
                rvalue += " " + key;
            }
        }
        return rvalue;
    }

    /**
     * Returns Property if it exists in the Register, or null if it does not exist.
     * @param name the name of the property
     * @return cooresponding Property or null
     */
    public final JunoProperty get(String name) {
        return map.get(name);
    }

    private String stripEqual(String x) {
        int index = x.lastIndexOf("=");
        return x.substring(0, index);
    }

    public final void setDescription(String propName, JunoPropertyCategory cat, String desc) {
        JunoProperty prop = get(propName);
        assert prop != null : "You must do a getProp* before doing a setDescription prop=" + propName;
        prop.setCategory(cat);
        prop.setDescription(desc);
        logIntf.config("propName=%s category=%s description=%s", propName, cat, desc);
    }
}
