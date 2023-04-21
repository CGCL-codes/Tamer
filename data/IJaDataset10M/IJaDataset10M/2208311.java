package com.izforge.izpack.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A nested element holder for the installation configuration document content.
 * The installation document must be passed in using a CDATA element.
 *
 * @author Scott Stark
 * @version $Revision: 2163 $
 */
public class ConfigHolder {

    /**
     * The parent element project
     */
    private Project project;

    /**
     * The config element body text with ${x} property references replaced
     */
    private String installText;

    /**
     * Taken from the ant org.apache.tools.ant.PropertyHelper and '$' replaced
     * with '@' to deal with @{x} style property references.
     * <p/>
     * Parses a string containing @{xxx} style property references
     * into two lists. The first list is a collection of text fragments, while
     * the other is a set of string property names. null entries in the
     * first list indicate a property reference from the second list.
     * <p/>
     * It can be overridden with a more efficient or customized version.
     *
     * @param value        Text to parse. Must not be null.
     * @param fragments    List to add text fragments to. Must not be null.
     * @param propertyRefs List to add property names to. Must not be null.
     * @throws BuildException if the string contains an opening @{ without a
     *                        closing }
     */
    static void parseCompileProperties(String value, Vector<String> fragments, Vector<String> propertyRefs) throws BuildException {
        int prev = 0;
        int pos;
        while ((pos = value.indexOf("@", prev)) >= 0) {
            if (pos > 0) {
                fragments.addElement(value.substring(prev, pos));
            }
            if (pos == (value.length() - 1)) {
                fragments.addElement("@");
                prev = pos + 1;
            } else if (value.charAt(pos + 1) != '{') {
                if (value.charAt(pos + 1) == '@') {
                    fragments.addElement("@");
                    prev = pos + 2;
                } else {
                    fragments.addElement(value.substring(pos, pos + 2));
                    prev = pos + 2;
                }
            } else {
                int endName = value.indexOf('}', pos);
                if (endName < 0) {
                    throw new BuildException("Syntax error in property: " + value);
                }
                String propertyName = value.substring(pos + 2, endName);
                fragments.addElement(null);
                propertyRefs.addElement(propertyName);
                prev = endName + 1;
            }
        }
        if (prev < value.length()) {
            fragments.addElement(value.substring(prev));
        }
    }

    ConfigHolder(Project project) {
        this.project = project;
    }

    /**
     * Called by ant to set the config element content. The content is scanned
     * for @{x} style property references and replaced with the x project
     * property.
     *
     * @param rawText - the raw config element body text.
     */
    public void addText(String rawText) {
        Vector<String> fragments = new Vector<String>();
        Vector<String> propertyRefs = new Vector<String>();
        parseCompileProperties(rawText, fragments, propertyRefs);
        StringBuffer sb = new StringBuffer();
        Enumeration<String> i = fragments.elements();
        Enumeration<String> j = propertyRefs.elements();
        while (i.hasMoreElements()) {
            String fragment = i.nextElement();
            if (fragment == null) {
                String propertyName = j.nextElement();
                Object replacement = null;
                if (replacement == null) {
                    replacement = project.getProperty(propertyName);
                }
                if (replacement == null) {
                    project.log("Property @{" + propertyName + "} has not been set", Project.MSG_VERBOSE);
                }
                if (replacement != null) {
                    fragment = replacement.toString();
                } else {
                    fragment = "@{" + propertyName + "}";
                }
            }
            sb.append(fragment);
        }
        installText = sb.toString();
    }

    /**
     * Get the config element body text with @{x} property references replaced
     *
     * @return the processed config element body text.
     */
    public String getText() {
        return installText;
    }
}
