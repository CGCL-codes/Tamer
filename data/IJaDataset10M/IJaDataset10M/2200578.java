package com.ideo.sweetdevria.config.elements;

import java.io.Serializable;

/**
 * <p>A JavaBean representing the configuration information of a <code>&lt;message-resources&gt;</code> element in a RIA configuration file.</p>
 */
public class MessageResourcesConfig implements Serializable {

    /**
	 * Generated serial version UID.
	 */
    private static final long serialVersionUID = 4265710466604207858L;

    /**
     * Has this component been completely configured?
     */
    protected boolean configured = false;

    /**
     * Fully qualified Java class name of the MessageResourcesFactory class
     * we should use.
     */
    protected String factory = "com.ideo.sweetdevria.util.PropertyMessageResourcesFactory";

    /**
     * Get the factory.
     * @return  Returns the factory.
     */
    public String getFactory() {
        return (this.factory);
    }

    /**
     * Set the factory.
     * @param factory  The factory to set.
     */
    public void setFactory(String factory) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.factory = factory;
    }

    protected String key = "??";

    /**
     * Get the key.
     * @return  Returns the key.
     */
    public String getKey() {
        return (this.key);
    }

    /**
     * Set the key.
     * @param key  The key to set.
     */
    public void setKey(String key) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.key = key;
    }

    /**
     * Should we return <code>null</code> for unknown message keys?
     */
    protected boolean nullValue = true;

    public boolean getNull() {
        return (this.nullValue);
    }

    public void setNull(boolean nullValue) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.nullValue = nullValue;
    }

    /**
     * Parameter that is passed to the <code>createResources()</code> method
     * of our MessageResourcesFactory implementation.
     */
    protected String parameter = null;

    /**
     * Get the parameter.
     * @return  Returns the parameter.
     */
    public String getParameter() {
        return (this.parameter);
    }

    /**
     * Set the parameter.
     * @param parameter  The parameter to set.
     */
    public void setParameter(String parameter) {
        if (configured) {
            throw new IllegalStateException("Configuration is frozen");
        }
        this.parameter = parameter;
    }

    /**
     * Freeze the configuration of this component.
     */
    public void freeze() {
        configured = true;
    }

    /**
     * Return a String representation of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MessageResourcesConfig[");
        sb.append("factory=");
        sb.append(this.factory);
        sb.append(",null=");
        sb.append(this.nullValue);
        sb.append(",parameter=");
        sb.append(this.parameter);
        sb.append("]");
        return (sb.toString());
    }
}
