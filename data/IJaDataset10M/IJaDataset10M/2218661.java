package au.csiro.pidclient.business;

/**
 * Represent a property triple: <index, type, value>.
 * <p>
 * A property triple is associated with an ANDS Persistent Identifier handle.
 * 
 * Copyright 2010, CSIRO Australia All rights reserved.
 * 
 * @author Robert Bridle on 10/02/2010
 * @version $Revision: 7131 $ $Date: 2010-06-09 14:25:15 +1000 (Wed, 09 Jun 2010) $
 */
public class AndsPidResponseProperty {

    /**
     * The numeric index of a the property.
     */
    private int index;

    /**
     * The type of the property {@see ANDPersistentIdentifierClient.HandleType}.
     */
    private String type;

    /**
     * The value of the property.
     */
    private String value;

    /**
     * 
     */
    public AndsPidResponseProperty() {
    }

    /**
     * Constructor
     * 
     * @param index
     *            a properties numeric index.
     * @param type
     *            a properties type.
     * @param value
     *            a properties value.
     */
    public AndsPidResponseProperty(int index, String type, String value) {
        this.index = index;
        this.type = type;
        this.value = value;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index
     *            the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
