package xdoclet.modules.mockobjects.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xjavadoc.XClass;

/**
 * Utility methods.
 *
 * @author    Joe Walnes
 * @author    Stig J&oslash;rgensen
 * @created   5. februar 2003
 */
public class CodeUtils {

    private static final Map wrappers = new HashMap();

    static {
        wrappers.put("boolean", "Boolean");
        wrappers.put("char", "Character");
        wrappers.put("byte", "Byte");
        wrappers.put("short", "Short");
        wrappers.put("int", "Integer");
        wrappers.put("long", "Long");
        wrappers.put("float", "Float");
        wrappers.put("double", "Double");
    }

    /**
     * For a class: returns the methods for the specied class and its superclasses. <p>
     *
     * For an interface: returns the methods for the specified interface and all its extending interfaces.
     *
     * @param startClass
     * @return
     * @todo:unittest
     */
    public static List getAllMethods(XClass startClass) {
        return new ArrayList(startClass.getMethods(true));
    }

    public static String wrapValue(String name, String type) {
        if (wrappers.containsKey(type)) {
            StringBuffer result = new StringBuffer();
            result.append("new ");
            result.append(wrappers.get(type));
            result.append('(');
            result.append(name);
            result.append(')');
            return result.toString();
        } else {
            return name;
        }
    }

    public static String unwrapValue(String name, String type) {
        StringBuffer result = new StringBuffer();
        if (wrappers.containsKey(type)) {
            result.append("((");
            result.append(wrappers.get(type));
            result.append(')');
            result.append(name);
            result.append(").");
            result.append(type);
            result.append("Value()");
        } else {
            result.append('(');
            result.append(type);
            result.append(')');
            result.append(name);
        }
        return result.toString();
    }

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
