package org.jaffa.rules.aop.metadata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.jboss.aop.Advised;
import org.jboss.aop.metadata.SimpleMetaData;

/**
 * An instance of this class encapsulates all the rules defined for a property.
 * It can also be used to encapsulate all the object-level rules.
 */
public class PropertyMetaData {

    private String m_name;

    private List m_attrList;

    private String m_extendsClass, m_extendsProperty;

    public PropertyMetaData(String name) {
        m_name = name;
    }

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return m_name;
    }

    /**
     * Getter for property extendsClass.
     * @return Value of property extendsClass.
     */
    public String getExtendsClass() {
        return m_extendsClass;
    }

    /**
     * Getter for property extendsProperty.
     * @return Value of property extendsProperty.
     */
    public String getExtendsProperty() {
        return m_extendsProperty;
    }

    /**
     * Get a list of all the attributes for this property.
     * @return a list of all the attributes for this property.
     */
    public List getAttributes() {
        return m_attrList;
    }

    /**
     * Get a specific attribute from this property. Note if there are duplicate attributes
     * the first one will be returned.
     * @param name the attribute name.
     * @return the value for the input attribute.
     */
    public PropertyAttribute getAttribute(String name) {
        if (m_attrList == null) return null;
        for (Iterator i = m_attrList.iterator(); i.hasNext(); ) {
            PropertyAttribute a = (PropertyAttribute) i.next();
            if (a.getName().equals(name)) return a;
        }
        return null;
    }

    /**
     * See a specific attribute exists on this property.
     * @param name the attribute name.
     * @return true if the input attribute exists on this property.
     */
    public boolean hasAttribute(String name) {
        return getAttribute(name) != null;
    }

    /**
     * Set property inheritence rules
     * NOTE: This will be ignored if extendsClass/extendsProperty have already been set.
     * @param extendsClass The class being extended.
     * @param extendsProperty The property being extended.
     */
    void addPropertyExtends(String extendsClass, String extendsProperty) {
        if (m_extendsClass == null && m_extendsProperty == null) {
            m_extendsClass = extendsClass;
            m_extendsProperty = extendsProperty;
        }
    }

    /** Adds rules to this property.
     * @param attribute the rule name.
     * @parameters the paremeters for the rule.
     */
    void addAttribute(String attribute, Properties parameters) {
        if (m_attrList == null) {
            m_attrList = new ArrayList();
        }
        PropertyAttribute attr = new PropertyAttribute();
        attr.setName(attribute);
        attr.setParameters(parameters);
        m_attrList.add(attr);
    }
}
