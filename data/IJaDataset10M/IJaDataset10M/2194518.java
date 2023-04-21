package org.argouml.profile.internal.ocl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents a composite ModelInterpreter
 * 
 * @author maurelio1234
 */
public class CompositeModelInterpreter implements ModelInterpreter {

    private Set<ModelInterpreter> set = new HashSet<ModelInterpreter>();

    /**
     * Adds a ModelInterpreter to this set
     * 
     * @param mi the model interpreter
     */
    public void addModelInterpreter(ModelInterpreter mi) {
        set.add(mi);
    }

    public Object invokeFeature(Map<String, Object> vt, Object subject, String feature, String type, Object[] parameters) {
        for (ModelInterpreter mi : set) {
            Object ret = mi.invokeFeature(vt, subject, feature, type, parameters);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    public Object getBuiltInSymbol(String sym) {
        for (ModelInterpreter mi : set) {
            Object ret = mi.getBuiltInSymbol(sym);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
}
