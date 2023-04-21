package org.opennms.protocols.xml.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * The Class XmlGroup.
 * 
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public class XmlGroup implements Serializable, Comparable<XmlGroup> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2716588565159391498L;

    /** The Constant OF_XML_OBJECTS. */
    private static final XmlObject[] OF_XML_OBJECTS = new XmlObject[0];

    /** The group name. */
    @XmlAttribute(name = "name", required = true)
    private String m_name;

    /** The resource type. */
    @XmlAttribute(name = "resource-type", required = true)
    private String m_resourceType;

    /** The resource XPath. */
    @XmlAttribute(name = "resource-xpath", required = true)
    private String m_resourceXpath;

    /** The key XPath (for resource instance). */
    @XmlAttribute(name = "key-xpath", required = false)
    private String m_keyXpath;

    /** The Resource Time XPath (for RRD updates). */
    @XmlAttribute(name = "timestamp-xpath", required = false)
    private String m_timestampXpath;

    /** The Resource Time Format (for RRD updates). */
    @XmlAttribute(name = "timestamp-format", required = false)
    private String m_timestampFormat;

    /** The XML objects list. */
    @XmlElement(name = "xml-object")
    private List<XmlObject> m_xmlObjects = new ArrayList<XmlObject>();

    /**
     * Gets the name.
     *
     * @return the name
     */
    @XmlTransient
    public String getName() {
        return m_name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Gets the XML objects.
     *
     * @return the XML objects
     */
    @XmlTransient
    public List<XmlObject> getXmlObjects() {
        return m_xmlObjects;
    }

    /**
     * Sets the XML objects.
     *
     * @param xmlObjects the new XML objects
     */
    public void setXmlObjects(List<XmlObject> xmlObjects) {
        m_xmlObjects = xmlObjects;
    }

    /**
     * Adds a new XML object.
     *
     * @param xmlObject the XML object
     */
    public void addXmlObject(XmlObject xmlObject) {
        m_xmlObjects.add(xmlObject);
    }

    /**
     * Removes a XML object.
     *
     * @param xmlObject the XML object
     */
    public void removeXmlObject(XmlObject xmlObject) {
        m_xmlObjects.remove(xmlObject);
    }

    /**
     * Removes a XML object by name.
     *
     * @param name the XML object name
     */
    public void removeObjectByName(String name) {
        for (Iterator<XmlObject> itr = m_xmlObjects.iterator(); itr.hasNext(); ) {
            XmlObject column = itr.next();
            if (column.getName().equals(name)) {
                m_xmlObjects.remove(column);
                return;
            }
        }
    }

    /**
     * Gets the resource type.
     *
     * @return the resource type
     */
    @XmlTransient
    public String getResourceType() {
        return m_resourceType;
    }

    /**
     * Sets the resource type.
     *
     * @param resourceType the new resource type
     */
    public void setResourceType(String resourceType) {
        m_resourceType = resourceType;
    }

    /**
     * Gets the resource XPath.
     *
     * @return the resource XPath
     */
    @XmlTransient
    public String getResourceXpath() {
        return m_resourceXpath;
    }

    /**
     * Sets the resource XPath.
     *
     * @param resourceXpath the new resource XPath
     */
    public void setResourceXpath(String resourceXpath) {
        this.m_resourceXpath = resourceXpath;
    }

    /**
     * Gets the key XPath (for resource instance).
     * 
     * @return the key XPath
     */
    @XmlTransient
    public String getKeyXpath() {
        return m_keyXpath;
    }

    /**
     * Sets the key XPath.
     *
     * @param keyXpath the new key XPath
     */
    public void setKeyXpath(String keyXpath) {
        this.m_keyXpath = keyXpath;
    }

    /**
     * Gets the timestamp xpath.
     *
     * @return the timestamp xpath
     */
    @XmlTransient
    public String getTimestampXpath() {
        return m_timestampXpath;
    }

    /**
     * Sets the timestamp xpath.
     *
     * @param timestampXpath the new timestamp xpath
     */
    public void setTimestampXpath(String timestampXpath) {
        this.m_timestampXpath = timestampXpath;
    }

    /**
     * Gets the timestamp format.
     *
     * @return the timestamp format
     */
    @XmlTransient
    public String getTimestampFormat() {
        return m_timestampFormat;
    }

    /**
     * Sets the timestamp format.
     *
     * @param timestampFormat the new timestamp format
     */
    public void setTimestampFormat(String timestampFormat) {
        this.m_timestampFormat = timestampFormat;
    }

    /**
     * Gets the if type.
     *
     * @return the if type
     */
    @XmlTransient
    public String getIfType() {
        return m_resourceType.equals("node") ? "ignore" : "all";
    }

    @Override
    public int compareTo(XmlGroup obj) {
        return new CompareToBuilder().append(getName(), obj.getName()).append(getResourceType(), obj.getResourceType()).append(getResourceXpath(), obj.getResourceXpath()).append(getKeyXpath(), obj.getKeyXpath()).append(getXmlObjects().toArray(OF_XML_OBJECTS), obj.getXmlObjects().toArray(OF_XML_OBJECTS)).toComparison();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XmlGroup) {
            XmlGroup other = (XmlGroup) obj;
            return new EqualsBuilder().append(getName(), other.getName()).append(getResourceType(), other.getResourceType()).append(getResourceXpath(), other.getResourceXpath()).append(getKeyXpath(), other.getKeyXpath()).append(getXmlObjects().toArray(OF_XML_OBJECTS), other.getXmlObjects().toArray(OF_XML_OBJECTS)).isEquals();
        }
        return false;
    }
}
