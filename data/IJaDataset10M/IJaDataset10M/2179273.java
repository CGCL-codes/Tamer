package saadadb.util;

/** * @version $Id: ChangeType.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 * <p>Title: SAADA </p>
 * <p>Description: Automatic Archival System For Astronomical Data -
    This is framework of a PhD funded by the CNES and by the Region Alsace.</p>
 * <p>Copyright: Copyright (c) 2002-2006</p>
 * <p>Company: Observatoire Astronomique Strasbourg-CNES</p>
 * @author: MILLAN Patrick
 */
public class ChangeType {

    public static final int nbType = 9;

    public static final String[] tabType = { "String", "double", "float", "long", "int", "byte", "boolean", "char", "short" };

    public static final String[] tabClass = { "String", "Double", "Float", "Long", "Integer", "Byte", "Boolean", "Character", "Short" };

    public static final String[] tabTypeSQL = { "A", "I", "J", "D", "E", "F", "L", "B" };

    public static final String[] tabSQLFromFits = { "String", "int", "int", "double", "float", "float", "boolean", "byte" };

    public static final String[] tabSQLFromPSQL = { "", "int", "int", "double precision", "float", "float", "boolean", "smallint" };

    public static final String[] tabSQLFromSybase = { "", "int", "int", "double precision", "float", "float", "bit", "smallint" };

    /**
	 * @param type
	 * @return
	 */
    public static String getType(String type) {
        String test;
        for (int i = 0; i < nbType; i++) {
            test = tabType[i];
            if (type.indexOf(test) >= 0) {
                return test;
            }
        }
        return "";
    }

    /**
	 * @param typeJava
	 * @return
	 */
    public static String getTypeSqlFromTypeJava_SYBASE(String typeJava) {
        if (typeJava.equals("short")) {
            return "smallint";
        } else if (typeJava.equals("class java.lang.Long") || typeJava.equals("long")) {
            return "numeric";
        } else if (typeJava.equals("class java.lang.Integer") || typeJava.equals("int")) {
            return "int";
        } else if (typeJava.equals("class java.lang.Byte") || typeJava.equals("byte")) {
            return "tinyint";
        } else if (typeJava.equals("class java.lang.Character")) {
            return "Character";
        } else if (typeJava.equals("char")) {
            return "char";
        } else if (typeJava.equals("boolean")) {
            return "bit";
        } else if (typeJava.equals("class java.lang.Float") || typeJava.equals("float")) {
            return "float";
        } else if (typeJava.equals("class java.lang.Double") || typeJava.equals("double")) {
            return "double precision";
        } else if (typeJava.indexOf("String") >= 0) {
            return "text";
        } else if (typeJava.indexOf("Date") >= 0) {
            return "datetime";
        }
        return "";
    }

    /**
	 * @param typeJava
	 * @return
	 */
    public static String getTypeJavaFromTypeClass(String typeJava) {
        String test;
        for (int i = 0; i < nbType; i++) {
            test = tabClass[i];
            if (typeJava.equals("class java.lang." + test)) {
                return test;
            }
            test = tabType[i];
            if (typeJava.equals(test)) {
                return test;
            }
        }
        if (typeJava.equals("class java.util.Date")) {
            return "Date";
        }
        return "String";
    }
}
