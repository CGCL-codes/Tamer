package org.openedc.tools.codegen.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import static org.openedc.tools.codegen.util.CommonStringUtils.*;
import static org.openedc.tools.codegen.util.SqlNamingUtils.*;

/**
 *  
 *  a set of static utility methods used for common Java naming conventions
 *
 * @author peter
 */
public class JavaNamingUtils extends CommonStringUtils {

    /** Creates a new instance of JavaNamingUtils */
    private JavaNamingUtils() {
    }

    /**
     * 
     * @param text 
     * @return 
     */
    public static String ConvertIdentifierToPropertyName(String text) {
        return CommonStringUtils.ConvertWordsToCamelCaseIdentifier(CommonStringUtils.SplitWords(text));
    }

    /**
     * Create an identifier to a proper class name with a suffix and a prefix
     * 
     * @param text  the text to convert to a proper class name
     * @param suffix a suffix for the class name
     * @return the propert class name
     */
    public static String ConvertIdentifierToClassName(String text, String suffix) {
        return ConvertIdentifierToClassName(text) + suffix;
    }

    /**
     * Create an identifier to a proper class name with a suffix and a prefix
     * 
     * @param text  the text to convert to a proper class name
     * @param suffix a suffix for the class name
     * @param prefix a prefix for the class name
     * @return the propert class name
     */
    public static String ConvertIdentifierToClassName(String text, String suffix, String prefix) {
        return ConvertIdentifierToClassName(text, suffix) + prefix;
    }

    /**
     * 
     * @param type 
     * @return
     */
    public static String CreateDefaultObjectIdName(String type) {
        String oid = null;
        int lastDot = type.lastIndexOf(".");
        String className = type.substring(lastDot + 1);
        oid = StringUtils.uncapitalize(className) + StringUtils.capitalize(DEFAULT_TABLE_ID_SUFFIX.toLowerCase());
        return oid;
    }

    /**
     * 
     * @param text 
     * @return
     */
    public static String ConvertIdentifierToClassName(String text) {
        return CommonStringUtils.ConvertWordsToPascalCaseIdentifier(CommonStringUtils.SplitWords(text));
    }

    /**
     * 
     * @param text 
     * @param prefix 
     * @return
     */
    public static String ConvertIdentifierToInterfaceName(String text, String prefix) {
        return prefix + ConvertIdentifierToInterfaceName(text);
    }

    /**
     * 
     * @param text 
     * @return
     */
    public static String ConvertIdentifierToInterfaceName(String text) {
        return ConvertIdentifierToClassName(text);
    }

    /**
     * 
     * @param text 
     * @return 
     */
    public static String ConvertIdentifierToLabel(String text) {
        return WordUtils.capitalizeFully(CommonStringUtils.SeparateWords(text, WordSeparators.Space.getSeparator(), new String[0]));
    }

    /**
     * 
     * @param text 
     * @param type specify the type of identifier it is
     * @return
     */
    public static String ConvertIdentifierToPropertyGetter(String text, String type) {
        if (type != null && type.equals(boolean.class.getName())) {
            return "is" + ConvertIdentifierToClassName(text);
        } else {
            return "get" + ConvertIdentifierToClassName(text);
        }
    }

    /**
     * 
     * @param text 
     * @return 
     */
    public static String ConvertIdentifierToPropertySetter(String text) {
        return "set" + ConvertIdentifierToClassName(text);
    }

    /**
     * 
     * @param text 
     * @return 
     */
    public static String ConvertIdentifierToRemover(String text) {
        return "removeItemFrom" + ConvertIdentifierToClassName(text);
    }

    /**
     * 
     * @param text 
     * @return 
     */
    public static String ConvertIdentifierToAdder(String text) {
        return "addItemTo" + ConvertIdentifierToClassName(text);
    }

    /**
     * 
     * @param text 
     * @return 
     */
    public static String ConvertIdentifierToAddAll(String text) {
        return "addAllItemsTo" + ConvertIdentifierToClassName(text);
    }

    /**
     *  format the BeanContainer class for a specified entity model
     * 
     * @param entityModelName the name of the entity model
     * @param businessBeanContainerClass the name of the default formal BeanContainer class name
     * @return
     */
    public static String FormatBusinessBeanContainerClass(String entityModelName, String businessBeanContainerClass) {
        int lastDot = businessBeanContainerClass.lastIndexOf(".");
        StringBuilder buffer = new StringBuilder(businessBeanContainerClass);
        buffer.insert(lastDot + 1, StringUtils.capitalize(entityModelName));
        return buffer.toString();
    }

    /**
     * remove the package name from the java class
     * @param formalJavaClass 
     * @return
     */
    public static String StripPackageName(String formalJavaClass) {
        int last = formalJavaClass.lastIndexOf(".");
        if (last > -1) {
            return formalJavaClass.substring(last + 1);
        } else {
            return formalJavaClass;
        }
    }

    /**
     * Strip off the formal package declaration if the referenced type is in the same package as the default one
     * The declaredOrDefaultPackages can be a single package name or a bunch of them
     *  
     * 
     * @param declaredOrDefaultPackages 
     * @param type
     * @return
     */
    public static String DeclareType(String declaredOrDefaultPackages, String type) {
        String returnType = "ERROR";
        if (type.startsWith("java.lang.")) {
            returnType = type.substring(type.indexOf("java.lang.") + "java.lang.".length());
        } else {
            int lastDot = type.lastIndexOf(".");
            if (lastDot > -1) {
                String typePackage = type.substring(0, lastDot);
                if (declaredOrDefaultPackages.contains(typePackage)) {
                    returnType = type.substring(lastDot + 1);
                } else {
                    returnType = type;
                }
            } else {
                returnType = type;
            }
        }
        return returnType;
    }
}
