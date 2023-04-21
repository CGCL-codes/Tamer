package org.gems.designer.metamodel.gen;

import org.gems.designer.model.meta.BaseTypes;
import org.gems.designer.model.meta.MetaType;

public class GeneratorUtilities {

    /**
	 * 
	 */
    public GeneratorUtilities() {
        super();
    }

    public static String getIdentifier(String name) {
        name = name.replaceAll(" ", "");
        name = name.replaceAll("\\.", "");
        return name;
    }

    public static String asObject(String type, String val) {
        if (AttributeInfo.BOOLEAN_TYPE.equals(type)) {
            return "new Boolean(" + val + ")";
        }
        if (AttributeInfo.DECIMAL_TYPE.equals(type)) {
            return "new Double(" + val + ")";
        }
        if (AttributeInfo.STRING_TYPE.equals(type) || "Enumeration".equals(type)) {
            if (val.indexOf("\"") < 0) {
                return "\"" + val + "\"";
            }
            return val;
        }
        if (AttributeInfo.INT_TYPE.equals(type)) {
            return "new Integer(" + val + ")";
        }
        return val;
    }

    public static String asObject(MetaType type, Object val) {
        if (BaseTypes.BOOLEAN_TYPE.isAssignableFrom(type)) {
            return "new Boolean(" + val + ")";
        }
        if (BaseTypes.INTEGER_TYPE.isAssignableFrom(type)) {
            return "new Integer(" + val + ")";
        }
        if (BaseTypes.DECIMAL_TYPE.isAssignableFrom(type)) {
            return "new Double(" + val + ")";
        }
        if (BaseTypes.STRING_TYPE.isAssignableFrom(type) || BaseTypes.ENUMERATION_TYPE.isAssignableFrom(type)) {
            if (("" + val).indexOf("\"") < 0) {
                return "\"" + val + "\"";
            }
            return "" + val;
        }
        return "" + val;
    }

    public static String toObject(String type, String val) {
        if (AttributeInfo.BOOLEAN_TYPE.equals(type)) {
            return "new Boolean(" + val + ")";
        }
        if (AttributeInfo.DECIMAL_TYPE.equals(type)) {
            return "new Double(" + val + ")";
        }
        if (AttributeInfo.INT_TYPE.equals(type)) {
            return "new Integer(" + val + ")";
        }
        return val;
    }
}
