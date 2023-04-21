package org.mybatis.generator.internal.util;

import java.util.Locale;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * @author Jeff Butler
 */
public class JavaBeansUtil {

    /**
     *  
     */
    private JavaBeansUtil() {
        super();
    }

    /**
     * JavaBeans rules:
     * 
     * eMail > geteMail() firstName > getFirstName() URL > getURL() XAxis >
     * getXAxis() a > getA() B > invalid - this method assumes that this is not
     * the case. Call getValidPropertyName first. Yaxis > invalid - this method
     * assumes that this is not the case. Call getValidPropertyName first.
     * 
     * @param property
     * @return the getter method name
     */
    public static String getGetterMethodName(String property, FullyQualifiedJavaType fullyQualifiedJavaType) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        if (fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBooleanPrimitiveInstance())) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }
        return sb.toString();
    }

    /**
     * JavaBeans rules:
     * 
     * eMail > seteMail() firstName > setFirstName() URL > setURL() XAxis >
     * setXAxis() a > setA() B > invalid - this method assumes that this is not
     * the case. Call getValidPropertyName first. Yaxis > invalid - this method
     * assumes that this is not the case. Call getValidPropertyName first.
     * 
     * @param property
     * @return the setter method name
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "set");
        return sb.toString();
    }

    public static String getCamelCaseString(String inputString, boolean firstCharacterUppercase, boolean firstCharacterLowercase) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            switch(c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        if (firstCharacterLowercase) {
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        }
        return sb.toString();
    }

    /**
     * This method ensures that the specified input string is a valid Java
     * property name. The rules are as follows:
     * 
     * 1. If the first character is lower case, then OK 2. If the first two
     * characters are upper case, then OK 3. If the first character is upper
     * case, and the second character is lower case, then the first character
     * should be made lower case
     * 
     * eMail > eMail firstName > firstName URL > URL XAxis > XAxis a > a B > b
     * Yaxis > yaxis
     * 
     * @param inputString
     * @return the valid property name
     */
    public static String getValidPropertyName(String inputString) {
        String answer;
        if (inputString == null) {
            answer = null;
        } else if (inputString.length() < 2) {
            answer = inputString.toLowerCase(Locale.US);
        } else {
            if (Character.isUpperCase(inputString.charAt(0)) && !Character.isUpperCase(inputString.charAt(1))) {
                answer = inputString.substring(0, 1).toLowerCase(Locale.US) + inputString.substring(1);
            } else {
                answer = inputString;
            }
        }
        return answer;
    }
}
