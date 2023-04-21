package com.cosylab.vdct.db;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Matej
 */
public class DBTemplateInstance extends DBComment {

    protected String templateInstanceId;

    protected String templateId;

    protected Hashtable properties = null;

    protected Vector propertiesV = null;

    protected int x = -1;

    protected int y = -1;

    protected java.awt.Color color = java.awt.Color.black;

    protected String description = null;

    protected Vector templateFields = null;

    /**
	 * Constructor.
	 */
    public DBTemplateInstance(String templateInstanceId, String templateId) {
        properties = new Hashtable();
        propertiesV = new Vector();
        templateFields = new Vector();
        this.templateInstanceId = templateInstanceId;
        this.templateId = templateId;
    }

    /**
	 * Returns the properties.
	 * @return Hashtable
	 */
    public Hashtable getProperties() {
        return properties;
    }

    /**
	 * Returns the properties.
	 * @return Vector
	 */
    public Vector getPropertiesV() {
        return propertiesV;
    }

    /**
	 * Returns the properties.
	 * @return Vector
	 */
    public void addProperty(Object key, String value) {
        if (!propertiesV.contains(key)) {
            properties.put(key, value);
            propertiesV.addElement(key);
        }
    }

    /**
	 * Returns the templateId.
	 * @return String
	 */
    public String getTemplateId() {
        return templateId;
    }

    /**
	 * Returns the color.
	 * @return java.awt.Color
	 */
    public java.awt.Color getColor() {
        return color;
    }

    /**
	 * Returns the description.
	 * @return String
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Returns the x.
	 * @return int
	 */
    public int getX() {
        return x;
    }

    /**
	 * Returns the y.
	 * @return int
	 */
    public int getY() {
        return y;
    }

    /**
	 * Sets the color.
	 * @param color The color to set
	 */
    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    /**
	 * Sets the description.
	 * @param description The description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Sets the x.
	 * @param x The x to set
	 */
    public void setX(int x) {
        this.x = x;
    }

    /**
	 * Sets the y.
	 * @param y The y to set
	 */
    public void setY(int y) {
        this.y = y;
    }

    /**
	 * Returns the templateInstanceId.
	 * @return String
	 */
    public String getTemplateInstanceId() {
        return templateInstanceId;
    }

    /**
	 * @return
	 */
    public Vector getTemplateFields() {
        return templateFields;
    }
}
