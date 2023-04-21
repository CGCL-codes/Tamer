package org.weasis.launcher.jnlp;

import java.util.Properties;

/**
 * This class represent the "resources" section of a Jnlp file
 * 
 * @author jlrz
 * 
 */
public class Resources {

    private final Properties jnlpProp;

    /**
     * Creates an empty instance of Resources class
     */
    public Resources(Properties jnlpProp) {
        this.jnlpProp = jnlpProp;
    }

    /**
     * Converts to a string representing the "resources" section of a Jnlp file in XML format
     * 
     * @return a string representing the "resources" section of a Jnlp file in XML format
     */
    public String toXml() {
        StringBuffer result = new StringBuffer();
        result.append("<resources>\n");
        if (jnlpProp != null) {
            int num = 1;
            String key = "resources.java." + num;
            while (jnlpProp.containsKey(key)) {
                result.append(jnlpProp.getProperty(key));
                result.append("\n");
                num++;
                key = "resources.java." + num;
            }
            result.append("<jar href=\"");
            result.append(jnlpProp.getProperty("resources.jar.main"));
            result.append("\" main=\"true\" />\n");
            num = 1;
            key = "resources.jar." + num;
            while (jnlpProp.containsKey(key)) {
                result.append("<jar href=\"");
                result.append(jnlpProp.getProperty(key));
                result.append("\" />\n");
                num++;
                key = "resources.jar." + num;
            }
            num = 1;
            key = "resources.extension." + num;
            while (jnlpProp.containsKey(key)) {
                result.append("<extension href=\"");
                result.append(jnlpProp.getProperty(key));
                result.append("\" />\n");
                num++;
                key = "resources.extension." + num;
            }
            num = 1;
            key = "resources.param." + num;
            while (jnlpProp.containsKey(key)) {
                String val = jnlpProp.getProperty(key);
                int index = val.indexOf("=");
                if (index > 0) {
                    result.append("<property name=\"");
                    result.append(val.substring(0, index));
                    result.append("\" value=\"");
                    if (val.length() > index + 1) {
                        result.append(val.substring(index + 1));
                    }
                    result.append("\" />\n");
                }
                num++;
                key = "resources.param." + num;
            }
        }
        result.append("</resources>\n");
        return result.toString();
    }
}
