package org.da.expressionj.functions;

import java.util.HashMap;
import java.util.Map;
import org.da.expressionj.model.Constant;

/**
 * This singleton class allows to bind names in the mathematical expressions to global constants.
 *
 * @version 0.9.1
 */
public class ConstantsDefinitions {

    private static ConstantsDefinitions global = null;

    private Map<String, Constant> constants = new HashMap();

    private ConstantsDefinitions() {
    }

    public static ConstantsDefinitions getInstance() {
        if (global == null) {
            global = new ConstantsDefinitions();
        }
        return global;
    }

    /**
    * Reset all the bindings.
    */
    public void reset() {
        constants.clear();
    }

    public Map<String, Constant> getConstants() {
        return constants;
    }

    public boolean hasConstant(String name) {
        return constants.containsKey(name);
    }

    /**
    * Return the function of a given name.
    */
    public Constant getConstant(String name) {
        return constants.get(name);
    }

    /**
    * Add a new global constant binding.
    */
    public void addConstant(String name, Constant constant) {
        constant.setName(name);
        constants.put(name, constant);
    }
}
