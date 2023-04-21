package com.jvantage.ce.property;

import com.jvantage.ce.common.Constants;
import org.apache.commons.lang.*;
import java.io.*;
import java.util.*;

public class Property implements java.io.Serializable {

    private String description = null;

    private String label = null;

    private String profileName = null;

    private long propertyID = 0;

    private String value = null;

    /**
     * Property constructor comment.
     */
    public Property() {
        super();
    }

    /**
     * Creates a new Property object.
     *
     * @param label
     */
    public Property(String label) {
        setLabel(label);
        setValue(null);
    }

    /**
     * Property constructor comment.
     */
    public Property(String label, String value) {
        super();
        setLabel(label);
        setValue(value);
    }

    /**
     *
     *
     * @return
     */
    public long getDatabaseID() {
        return propertyID;
    }

    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     *
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /** Getter for property profileName.
     * @return Value of property profileName.
     *
     */
    public java.lang.String getProfileName() {
        return profileName;
    }

    /** Getter for property propertyID.
     * @return Value of property propertyID.
     *
     */
    public long getPropertyID() {
        return propertyID;
    }

    /**
     *
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    public String getValueTokenAtIndex(int tokenIndex) throws NoSuchElementException {
        StringTokenizer parser = new StringTokenizer(value, Constants.sfTokenDelimiter);
        if (parser.countTokens() < tokenIndex) {
            throw new NoSuchElementException("Token index [" + tokenIndex + "] was requested from string [" + value + "] which has only [" + parser.countTokens() + "] tokens.");
        }
        int i = 1;
        while (parser.hasMoreTokens()) {
            String token = parser.nextToken();
            if (i++ == tokenIndex) {
                return token;
            }
        }
        return "";
    }

    public int getValueTokenCount() {
        StringTokenizer parser = new StringTokenizer(value, Constants.sfTokenDelimiter);
        return parser.countTokens();
    }

    /**
     *
     *
     * @param propertyID
     */
    public void setDatabaseID(long propertyID) {
        this.propertyID = propertyID;
    }

    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     *
     *
     * @param label
     */
    public void setLabel(String label) {
        this.label = (label == null) ? null : label.toUpperCase();
    }

    /** Setter for property profileName.
     * @param profileName New value of property profileName.
     *
     */
    public void setProfileName(java.lang.String profileName) {
        this.profileName = profileName;
    }

    /** Setter for property propertyID.
     * @param propertyID New value of property propertyID.
     *
     */
    public void setPropertyID(long propertyID) {
        this.propertyID = propertyID;
    }

    /**
     *
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     *
     * @return
     */
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        pw.println(Constants.sfIndent1 + "Showing Property");
        pw.println(Constants.sfIndent1 + "================");
        pw.println(Constants.sfIndent2 + " propertyID [" + getPropertyID() + "]");
        pw.println(Constants.sfIndent2 + "      label [" + getLabel() + "]");
        pw.println(Constants.sfIndent2 + "      value [" + getValue() + "]");
        pw.println(Constants.sfIndent2 + "description [" + getDescription() + "]");
        pw.println();
        pw.flush();
        pw.close();
        return sw.toString();
    }
}
