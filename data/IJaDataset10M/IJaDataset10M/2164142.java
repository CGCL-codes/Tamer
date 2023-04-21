package de.fhg.igd.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import de.fhg.igd.io.VariableSubstitutionInputStream;

/**
 * This class allows to read a given property file and transform
 * it into an according <code>Properties</code> class.<p>
 *
 * The user can specify by flags, wether the 
 *
 * <p><ol>
 *   <li>Java System Properties, 
 *   <li>Shell Variables, 
 *   <li>WhatIs Variables, and/or 
 *   <li>additionally given key/value-Map
 * </ol><p>
 *
 * are used to substitute accordingly given variables
 * <pre>${&lt;variable_name&gt;} resp. ${WhatIs:&lt;whatis_name&gt;}</pre>
 * while parsing the configuration property files.<p>
 *
 * Furthermore, this class automatically transforms backslashes
 * into double backslashes. Since the method <code>Properties.load()</code> 
 * transforms escaped characters (with a leading backslash) this 
 * is necessary, espacially when thinking of separators in file 
 * system paths on Windows-bases system (e.g. 'user.home=D:\\user').<p>
 *
 * Besides the new feature of variable substitution with
 * the above defined syntax, configuration files further on have to
 * satisfy the syntax as defined by the <code>Properties</code> class.
 * 
 * @author Jan Peters
 * @version "$Id: VariableSubstitution.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class VariableSubstitution {

    /**
     * Flag signals substitution of System Properties.
     *
     * @see java.lang.System#getProperties
     */
    public static final int SYSTEM_PROPERTIES = 0x01;

    /**
     * Flag signals substitution of WhatIs Variables.
     *
     * @see de.fhg.igd.util.WhatIs
     */
    public static final int WHATIS_VARIABLES = 0x02;

    /**
     * Flag signals substitution of Shell Variables.
     *
     * @see de.fhg.igd.util.VariablesContext
     */
    public static final int SHELL_VARIABLES = 0x04;

    /**
     * Comparable to <code>String.replace(char,char)</code>
     * but not limited to characters, this method replaces
     * all occurences of <code>searchStr</code> in the
     * given string <code>value</code> with <code>replacement</code>.
     *
     * <p><ul>
     * <li>If <code>value</code> is <code>null</code>, this method 
     *   returns <code>null</code>.
     * <li>If <code>searchStr</code> is <code>null</code>, this 
     *   method returns <code>value</code>.
     * <li>If <code>replacement</code> is <code>null</code>, all 
     *   occurences of <code>searchStr</code> are deleted from 
     *   <code>value</code>.
     * <li>If <code>searchStr</code> is not found within 
     *   <code>value</code>, this method returns <code>value</code>.
     * <li>Otherwise, a new string is generated.
     * </ul><p>
     * 
     * @param value the string to apply replacements to.
     * @param searchStr the string to replace.
     * @param replacement the replacement string.
     * @return a new string resulting from replacing all occurrences
     *   of <code>searchStr</code> with <code>replacement</code>.
     */
    public static String replaceAll(String value, String searchStr, String replacement) {
        StringBuffer strbuf;
        int searchLen;
        int pos;
        if (value == null) {
            return null;
        }
        if (searchStr == null) {
            return value;
        }
        if (replacement == null) {
            replacement = "";
        }
        if (value.indexOf(searchStr) == -1) {
            return value;
        }
        strbuf = new StringBuffer();
        searchLen = searchStr.length();
        while ((pos = value.indexOf(searchStr)) != -1) {
            strbuf.append(value.substring(0, pos));
            strbuf.append(replacement);
            value = value.substring(pos + searchLen);
        }
        strbuf.append(value);
        return strbuf.toString();
    }

    /**
     * Returns a <code>Map</code> containing all variables
     * from the selected variable pools as key/value-pairs.<p>
     *
     * The variable flags (<code>SYSTEM_PROPERTIES</code>,
     * <code>SHELL_VARIABLES</code> and <code>WHATIS_VARIABLES</code>)
     * can be combined using the binary or-operator '|'.<p>
     *
     * The variable pools are imported in this order: when 
     * same keys occur in different pools a later imported
     * variable overwrites a previously imported one.<p>
     *
     * Backslashes contained in the value-part of the chosen
     * variable pools are automatically replaced by double
     * backslashes.
     *
     * @param variableFlags Flags to choose variable pools.
     * @return the variable <code>Map</code>.
     */
    public static Map getVariableMap(int variableFlags) {
        return getVariableMap(variableFlags, null);
    }

    /**
     * Returns a <code>Map</code> containing all variables
     * from the selected variable pools as key/value-pairs.<p>
     *
     * The variable flags (<code>SYSTEM_PROPERTIES</code>,
     * <code>SHELL_VARIABLES</code> and <code>WHATIS_VARIABLES</code>)
     * can be combined using the binary or-operator '|'.
     * If <code>additionalVariables</code> is not <code>null</code>
     * its key/value-pairs are added to the resulting variable map.<p>
     *
     * The variable pools are imported in this order: when 
     * same keys occur in different pools a later imported
     * variable overwrites a previously imported one.<p>
     *
     * Backslashes contained in the value-part of the chosen
     * variable pools are automatically replaced by double
     * backslashes.
     *
     * @param variableFlags Flags to choose variable pools.
     * @param additionalVariables An additional user-defined 
     *   variable pool.
     * @return the variable <code>Map</code>.
     */
    public static Map getVariableMap(int variableFlags, Map additionalVariables) {
        Iterator it;
        String value;
        String key;
        Map variables;
        variables = new HashMap();
        if ((variableFlags & SYSTEM_PROPERTIES) != 0) {
            for (it = System.getProperties().keySet().iterator(); it.hasNext(); ) {
                key = (String) it.next();
                value = (String) System.getProperty(key);
                if (value != null) {
                    value = replaceAll(value, "\\", "\\\\");
                }
                variables.put(key, value);
            }
        }
        if ((variableFlags & WHATIS_VARIABLES) != 0) {
            for (it = WhatIs.keySet().iterator(); it.hasNext(); ) {
                key = (String) it.next();
                value = (String) WhatIs.stringValue(key);
                if (value != null) {
                    value = replaceAll(value, "\\", "\\\\");
                }
                variables.put("WhatIs:" + key, value);
            }
        }
        if ((variableFlags & SHELL_VARIABLES) != 0) {
            for (it = Variables.getContext().keys(); it.hasNext(); ) {
                key = (String) it.next();
                value = (String) Variables.getContext().get(key);
                if (value != null) {
                    value = replaceAll(value, "\\", "\\\\");
                }
                variables.put(key, value);
            }
        }
        if (additionalVariables != null) {
            for (it = additionalVariables.keySet().iterator(); it.hasNext(); ) {
                key = (String) it.next();
                value = (String) additionalVariables.get(key);
                if (value != null) {
                    value = replaceAll(value, "\\", "\\\\");
                }
                variables.put(key, value);
            }
        }
        return variables;
    }

    /**
     * Parses the configuration file with the given <code>filename</code>
     * and returns the according <code>Properties</code> class.
     *
     * @param filename Name of the configuration file to parse.
     * @return the according <code>Properties</code> class.
     * @exception IOException if an error occurs while parsing the file.
     *
     * @see java.util.Properties#load
     */
    public static Properties parseConfigFile(String filename) throws IOException {
        return parseConfigFile(filename, 0x00);
    }

    /**
     * Parses the configuration file with the given <code>filename</code>
     * and returns the according <code>Properties</code> class.
     *
     * The given <code>variableFlags</code> select variable pools to be 
     * used for variable substitution (compare <code>getVariableMap</code>)
     * while parsing the configuration file.
     *
     * @param filename Name of the configuration file to parse.
     * @param variableFlags Flags to choose variable pools.
     * @return the according <code>Properties</code> class.
     * @exception IOException if an error occurs while parsing the file.
     *
     * @see java.util.Properties#load
     */
    public static Properties parseConfigFile(String filename, int substitutionFlags) throws IOException {
        Properties properties;
        properties = new Properties();
        properties.load(new VariableSubstitutionInputStream(new FileInputStream(filename), getVariableMap(substitutionFlags)));
        return properties;
    }

    /**
     * Parses the configuration file with the given <code>filename</code>
     * and returns the according <code>Properties</code> class.
     *
     * The given <code>variableFlags</code> select variable pools to be 
     * used for variable substitution (compare <code>getVariableMap</code>)
     * while parsing the configuration file.
     *
     * @param filename Name of the configuration file to parse.
     * @param variableFlags Flags to choose variable pools.
     * @param additionalVariables An additional user-defined variable pool.
     * @return the according <code>Properties</code> class.
     * @exception IOException if an error occurs while parsing the file.
     *
     * @see java.util.Properties#load
     */
    public static Properties parseConfigFile(String filename, int substitutionFlags, Map additionalVariables) throws IOException {
        Properties properties;
        properties = new Properties();
        properties.load(new VariableSubstitutionInputStream(new FileInputStream(filename), getVariableMap(substitutionFlags, additionalVariables)));
        return properties;
    }
}
