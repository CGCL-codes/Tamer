package org.nbplugin.jpa.datasource;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.UUID;

/**
 * Class representing a DataSource
 * 
 * @author shofmann
 * @version $Revision: 1.2 $
 * last modified by $Author: sebhof $
 */
public class DataSource {

    /** display name */
    private String name;

    public static final String PROP_NAME = "name";

    /** name to bind to JNDI tree */
    private String jndiName;

    public static final String PROP_JNDINAME = "jndiName";

    /** JDBC Connection URL */
    private String url;

    /** JDBC username */
    private String username;

    /** JDBC password */
    private String password;

    /** JDBC driver class */
    private String driverClass;

    /** JAR file containing the driver class */
    private File driverJAR;

    /** unique ID of this data source */
    private UUID uuid;

    /**
     * Get the value of jndiName
     *
     * @return the value of jndiName
     */
    public String getJndiName() {
        return jndiName;
    }

    /**
     * Set the value of jndiName
     *
     * @param jndiName new value of jndiName
     */
    public void setJndiName(String jndiName) {
        String oldJndiName = this.jndiName;
        this.jndiName = jndiName;
        propertyChangeSupport.firePropertyChange(PROP_JNDINAME, oldJndiName, jndiName);
    }

    /**
     * Get the value of uuid
     *
     * @return the value of uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Set the value of uuid
     *
     * @param uuid new value of uuid
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the value of driverJAR
     *
     * @return the value of driverJAR
     */
    public File getDriverJAR() {
        return driverJAR;
    }

    /**
     * Set the value of driverJAR
     *
     * @param driverJAR new value of driverJAR
     */
    public void setDriverJAR(File driverJAR) {
        this.driverJAR = driverJAR;
    }

    /**
     * Get the value of driverClass
     *
     * @return the value of driverClass
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * Set the value of driverClass
     *
     * @param driverClass new value of driverClass
     */
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the value of url
     *
     * @return the value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the value of url
     *
     * @param url new value of url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange(PROP_NAME, oldName, name);
    }

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
