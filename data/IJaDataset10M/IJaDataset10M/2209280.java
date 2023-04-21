package umlc.parseTree;

import java.util.*;

public class UmlType {

    private static Hashtable languageMappings = new Hashtable();

    public static String INTEGER = "INTEGER";

    public static String VOID = "VOID";

    public static String BOOLEAN = "BOOLEAN";

    public static String BYTE = "BYTE";

    public static String CHAR = "CHAR";

    public static String SHORT = "SHORT";

    public static String FLOAT = "FLOAT";

    public static String LONG = "LONG";

    public static String DOUBLE = "DOUBLE";

    public static String STRING = "STRING";

    public static String DECIMAL = "DECIMAL";

    public static String DATE = "DATE";

    public static String BLOB = "BLOB";

    public static String CLASS = "CLASS";

    public String class_name;

    public String type;

    UmlType() {
    }

    UmlType(String _type) {
        type = _type;
    }

    public static void loadMapping(String language, Properties mapping) {
        languageMappings.put(language, mapping);
    }

    public String getType(String language) {
        if (type.equals("CLASS")) return class_name; else {
            Properties p = (Properties) languageMappings.get(language);
            return p.getProperty(type);
        }
    }

    void pretty_print() {
        if (type.equals("CLASS")) {
            System.out.print(" " + class_name + " ");
        } else {
            System.out.print(" " + type + " ");
        }
    }
}
