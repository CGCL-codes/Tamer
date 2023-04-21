package cz.fi.muni.xkremser.editor.client.mods;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class StringPlusDisplayLabelClient.
 */
public class StringPlusDisplayLabelClient implements IsSerializable {

    /** The value. */
    protected String value;

    /** The display label. */
    protected String displayLabel;

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
     * Gets the value of the displayLabel property.
     * 
     * @return possible object is {@link String }
     */
    public String getDisplayLabel() {
        return displayLabel;
    }

    /**
     * Sets the value of the displayLabel property.
     * 
     * @param value
     *        allowed object is {@link String }
     */
    public void setDisplayLabel(String value) {
        this.displayLabel = value;
    }
}
