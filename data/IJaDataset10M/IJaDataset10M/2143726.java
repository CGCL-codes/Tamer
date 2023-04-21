package br.gov.demoiselle.eclipse.util.utility.xml.jsf;

import java.util.ArrayList;
import br.gov.demoiselle.eclipse.util.utility.CommonsConstants;

public class JsfObjectsUtil implements CommonsConstants {

    public static String findClassOfPackageType(String type) {
        String prefix = type.substring(0, type.indexOf(':') + 1);
        String sufix = type.substring(type.indexOf(':') + 1, type.length());
        if (prefix.equals(AbstractJsfObject.URI_JSF_AJAX)) return PACKAGE_AJAX4JSF + "." + upperFirstChar(sufix);
        if (prefix.equals(AbstractJsfObject.URI_JSF_CORE)) return PACKAGE_CORE + "." + upperFirstChar(sufix);
        if (prefix.equals(AbstractJsfObject.URI_JSF_HTML)) return PACKAGE_HTML + "." + upperFirstChar(sufix);
        if (prefix.equals(AbstractJsfObject.URI_JSF_RICH)) return PACKAGE_RICHFACES + "." + upperFirstChar(sufix);
        if (prefix.equals(AbstractJsfObject.URI_JSF_TOMAHAWK)) return PACKAGE_TOMAHAWK + "." + upperFirstChar(sufix);
        return null;
    }

    public static String findClassOfType(String type) {
        StringBuffer className = new StringBuffer(type);
        if (className.length() > 0) className.setCharAt(0, Character.toUpperCase(className.charAt(0)));
        for (int i = 0; i < CLASS_AJAX4JSF.length; i++) {
            if (CLASS_AJAX4JSF[i].equals(className.toString())) {
                return PACKAGE_AJAX4JSF + "." + className;
            }
        }
        for (int i = 0; i < CLASS_CORE.length; i++) {
            if (CLASS_CORE[i].equals(className.toString())) {
                return PACKAGE_CORE + "." + className;
            }
        }
        for (int i = 0; i < CLASS_HTML.length; i++) {
            if (CLASS_HTML[i].equals(className.toString())) {
                return PACKAGE_HTML + "." + className;
            }
        }
        for (int i = 0; i < CLASS_RICHFACES.length; i++) {
            if (CLASS_RICHFACES[i].equals(className.toString())) {
                return PACKAGE_RICHFACES + "." + className;
            }
        }
        for (int i = 0; i < CLASS_TOMAHAWK.length; i++) {
            if (CLASS_TOMAHAWK[i].equals(className.toString())) {
                return PACKAGE_TOMAHAWK + "." + className;
            }
        }
        return null;
    }

    public static ArrayList<String> getFieldTypes() {
        ArrayList<String> result = new ArrayList<String>();
        result.add("calendar");
        result.add("inputText");
        result.add("inputHidden");
        result.add("selectOneMenu");
        result.add("selectOneRadio");
        result.add("selectManyCheckbox");
        result.add("outputText");
        result.add("graphicImage");
        return result;
    }

    public static ArrayList<String> getTypes() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < CLASS_AJAX4JSF.length; i++) result.add(AbstractJsfObject.URI_JSF_AJAX + lowFirstChar(CLASS_AJAX4JSF[i]));
        for (int i = 0; i < CLASS_CORE.length; i++) result.add(AbstractJsfObject.URI_JSF_CORE + lowFirstChar(CLASS_CORE[i]));
        for (int i = 0; i < CLASS_HTML.length; i++) result.add(AbstractJsfObject.URI_JSF_HTML + lowFirstChar(CLASS_HTML[i]));
        for (int i = 0; i < CLASS_RICHFACES.length; i++) result.add(AbstractJsfObject.URI_JSF_RICH + lowFirstChar(CLASS_RICHFACES[i]));
        for (int i = 0; i < CLASS_TOMAHAWK.length; i++) result.add(AbstractJsfObject.URI_JSF_TOMAHAWK + lowFirstChar(CLASS_TOMAHAWK[i]));
        return result;
    }

    public static String lowFirstChar(String field) {
        StringBuffer fieldBuffer = new StringBuffer(field);
        fieldBuffer.setCharAt(0, Character.toLowerCase(fieldBuffer.charAt(0)));
        if (fieldBuffer.length() > 0) return fieldBuffer.toString(); else return null;
    }

    public static String upperFirstChar(String field) {
        StringBuffer fieldBuffer = new StringBuffer(field);
        fieldBuffer.setCharAt(0, Character.toUpperCase(fieldBuffer.charAt(0)));
        if (fieldBuffer.length() > 0) return fieldBuffer.toString(); else return null;
    }
}
