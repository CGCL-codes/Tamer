package org.antdepo.utils;

import org.apache.tools.ant.Project;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * expands nested property references within a provided string
 */
public class PropertyUtil {

    /**
     * expand a given Properties object and return a new one.
     * This will process each key to a given Properties object, get its value
     * and expand it.  Each value may contain references to other keys within this
     * given Properties object, and if so, all keys and their expanded keyValues will be resolved
     * into a new Properties object that will be returned.
     */
    public static Properties expand(Map properties) {
        Properties expandedProperties = new Properties();
        for (Iterator iter = properties.keySet().iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            String keyValue = (String) properties.get(key);
            String expandedKeyValue = expand(keyValue, properties);
            expandedProperties.setProperty(key, expandedKeyValue);
        }
        return expandedProperties;
    }

    /**
     * expand a keyString that may contain references to properties
     * located in provided Properties object
     */
    public static String expand(String keyString, Properties properties) {
        return PropertyUtil.expand(keyString, (Map) properties);
    }

    /**
     * expand a keyString that may contain references other properties
     * located in provided Map object
     * NOTE:  this is a recursive method in case references to properties are
     * nested within another reference
     */
    public static String expand(String keyString, Map properties) {
        String expandedLine = lineExpand(keyString, properties);
        if (resolvesToLiteral(expandedLine)) {
            return expandedLine;
        }
        return expand(expandedLine, properties);
    }

    /**
     * expand a keyString that may contain referecnes to other properties
     * @param keyString string containing props
     * @param project Ant project
     * @return expanded string                    
     */
    public static String expand(String keyString, Project project) {
        return expand(keyString, project.getProperties());
    }

    /**
     * parse the given keyString and expand each reference to a property from the given
     * map object.
     */
    private static String lineExpand(String keyString, Map properties) {
        if (resolvesToLiteral(keyString)) {
            return keyString;
        }
        StringTokenizer keyStringTokenizer = new StringTokenizer(keyString, "${}", true);
        StringBuffer sb = new StringBuffer();
        if (keyStringTokenizer.countTokens() == 1) {
            if (!properties.containsKey(keyString)) {
                return "";
            }
            return (String) properties.get(keyString);
        }
        boolean dollar = false;
        boolean lp = false;
        boolean rp = false;
        while (keyStringTokenizer.hasMoreTokens()) {
            String expTok;
            if (dollar == true && lp == true && rp == true) {
                dollar = false;
                lp = false;
                rp = false;
            }
            String tok = keyStringTokenizer.nextToken();
            if (dollar == false && lp == false && rp == false) {
                if (!tok.equals("$")) {
                    sb.append(tok);
                }
            }
            if (dollar == true && lp == true && rp == false) {
                if (!tok.equals("}")) {
                    expTok = tok;
                    String expVal = (String) properties.get(expTok);
                    if (expVal == null) {
                    }
                    sb.append(expVal);
                } else {
                    rp = true;
                    continue;
                }
            }
            if (tok.equals("$")) {
                if (dollar == true) {
                    throw new PropertyUtilException("parsing error: $$ invalid");
                }
                if (lp == true) {
                    throw new PropertyUtilException("parsing error: {$ invalid");
                }
                if (rp == true) {
                    throw new PropertyUtilException("parsing error: }$ invalid");
                }
                dollar = true;
                continue;
            }
            if (tok.equals("{")) {
                if (dollar == false) {
                    throw new PropertyUtilException("parsing error: $ symbol must occur before { symbol");
                }
                if (lp == true) {
                    throw new PropertyUtilException("parsing error: {{ invalid");
                }
                if (rp == true) {
                    throw new PropertyUtilException("parsing error: }{ invalid");
                }
                lp = true;
                continue;
            }
            if (tok.equals("}")) {
                if (dollar == false) {
                    throw new PropertyUtilException("parsing error: $ symbol must occur before } symbol");
                }
                if (lp == false) {
                    throw new PropertyUtilException("parsing error: { symbol must occur before } symbol");
                }
                if (rp == true) {
                    throw new PropertyUtilException("parsing error: }} invalid");
                }
                rp = true;
                continue;
            }
        }
        if (null == sb.toString() || "".equals(sb.toString())) throw new PropertyUtilException("null string return:" + keyString);
        return sb.toString();
    }

    /**
     * determine if the provided keyString contains any references to properties
     */
    private static boolean resolvesToLiteral(String keyString) {
        if (keyString.indexOf("${") > -1) {
            return false;
        }
        return true;
    }

    public static class PropertyUtilException extends RuntimeException {

        public PropertyUtilException(String msg) {
            super(msg);
        }
    }
}
