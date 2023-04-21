package org.jasen.config;

import java.io.Serializable;

/**
 * <P>
 * 	Holds the configuration for a single jASEN plugin component.
 * </P>
 * @author Jason Polites
 */
public class JasenPluginConfiguration implements Serializable, Comparable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3674376794340473895L;

    private String priority;

    private String name;

    private String displayName;

    private String description;

    private String type;

    private String calculator;

    private String properties;

    /**
     *
     */
    public JasenPluginConfiguration() {
        super();
    }

    /**
     * Gets the calculator used to compute the result for this plugin
     * @return The FQCN of the calculator class as a String
     */
    public String getCalculator() {
        return calculator;
    }

    /**
     * Sets the calculator used to compute the result for this plugin
     * @param calculatorClass The FQCN of the calculator class as a String
     */
    public void setCalculator(String calculatorClass) {
        this.calculator = calculatorClass;
    }

    /**
     * Gets the internal name used to identify this plugin
     * @return A String representation of the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the internal name used to identify this plugin
     * @param name A String representation of the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the plugin type.  This represents the fully qualified class name of the plugin class
     * @return The FQCN of the plugin class as a String
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the plugin type.  This represents the fully qualified class name of the plugin class
     * @param type The FQCN of the plugin class as a String
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the priority for this plugin.  This should be a parseable integer string
     * @return A String representation of an integer
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the priority for this plugin.  This should be a parseable integer string
     * @param priority A String representation of an integer
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Gets the classpath relative path to the properties file used to configure this plugin
     * @return A String which represents either the absolute, or classpath-relative path to the properties file
     */
    public String getProperties() {
        return properties;
    }

    /**
     * Gets the classpath relative path to the properties file used to configure this plugin
     * <br/>
     * This file MUST be in the classpath for the application
     * @param properties A String which represents either the absolute, or classpath-relative path to the properties file
     */
    public void setProperties(String properties) {
        this.properties = properties;
    }

    /**
     * Gets the summary description of this plugin.  Used for display in GUI systems etc.
     * @return Returns the description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the summary description of this plugin.  Used for display in GUI systems etc.
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the user friendly name of this plugin.  Used for display in GUI systems etc.
     * @return Returns the displayName.
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Sets the user friendly name of this plugin.  Used for display in GUI systems etc.
     * @param displayName The displayName to set.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int compareTo(Object o) {
        if (o instanceof JasenPluginConfiguration) {
            int thisP = Integer.parseInt(priority);
            int thatP = Integer.parseInt(((JasenPluginConfiguration) o).getPriority());
            return thisP - thatP;
        } else {
            return 1;
        }
    }
}
