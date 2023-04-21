package cz.fi.muni.xkremser.editor.client.mods;

import com.google.gwt.user.client.rpc.IsSerializable;
import cz.fi.muni.xkremser.editor.server.mods.Yes;

/**
 * The Class TypeOfResourceTypeClient.
 */
public class TypeOfResourceTypeClient implements IsSerializable {

    /** The value. */
    protected String value;

    /** The collection. */
    protected YesClient collection;

    /** The manuscript. */
    protected YesClient manuscript;

    /**
     * Gets the value of the value property.
     * 
     * @return possible object is {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *        allowed object is {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the collection property.
     * 
     * @return possible object is {@link Yes }
     */
    public YesClient getCollection() {
        return collection;
    }

    /**
     * Sets the value of the collection property.
     * 
     * @param value
     *        allowed object is {@link Yes }
     */
    public void setCollection(YesClient value) {
        this.collection = value;
    }

    /**
     * Gets the value of the manuscript property.
     * 
     * @return possible object is {@link Yes }
     */
    public YesClient getManuscript() {
        return manuscript;
    }

    /**
     * Sets the value of the manuscript property.
     * 
     * @param value
     *        allowed object is {@link Yes }
     */
    public void setManuscript(YesClient value) {
        this.manuscript = value;
    }
}
