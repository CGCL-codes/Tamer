package fr.cnes.sitools.datasource.jdbc.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Class for attribute value
 * 
 * @author jp.boignard (AKKA Technologies)
 * 
 */
@XStreamAlias("attribute")
public final class AttributeValue {

    /**
   * Attribute name
   */
    private String name;

    /**
   * Attribute value
   */
    private Object value;

    /**
   * Constructor (full)
   * 
   * @param name String
   * @param value Object
   */
    public AttributeValue(String name, Object value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
   * Default constructor
   */
    public AttributeValue() {
        super();
    }

    /**
   * Gets the name value
   * 
   * @return the name
   */
    public String getName() {
        return name;
    }

    /**
   * Sets the value of name
   * 
   * @param name
   *          the name to set
   */
    public void setName(String name) {
        this.name = name;
    }

    /**
   * Gets the value value
   * 
   * @return the value
   */
    public Object getValue() {
        return value;
    }

    /**
   * Sets the value of value
   * 
   * @param value
   *          the value to set
   */
    public void setValue(Object value) {
        this.value = value;
    }
}
