package org.verus.ngl.utilities.marc;

/**
 *
 * @author root
 */
public class NGLSubfield {

    /** Creates a new instance of NGLSubfield */
    public NGLSubfield() {
    }

    private char identifier = ' ';

    private String data = "";

    private Boolean mandatory = false;

    public char getIdentifier() {
        return identifier;
    }

    public void setIdentifier(char identifier) {
        this.identifier = identifier;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * Holds value of property afId.
     */
    private String afId;

    /**
     * Getter for property adId.
     * @return Value of property adId.
     */
    public String getAfId() {
        return this.afId;
    }

    /**
     * Setter for property adId.
     * @param adId New value of property adId.
     */
    public void setAfId(String afId) {
        this.afId = afId;
    }

    public Boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }
}
