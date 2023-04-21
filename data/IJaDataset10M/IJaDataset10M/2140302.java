package com.actionbazaar.web;

import java.io.Serializable;

/**
 * Encapsulates a phone number.
 * @author Ryan Cuprak
 */
public class PhoneNumber implements Serializable {

    /**
     * Serial UID
     */
    private static final Long serialVersionUID = -45550661215465050L;

    /**
     * Area code
     */
    private String areaCode = "";

    /**
     * Prefix
     */
    private String prefix = "";

    /**
     * Line Number
     */
    private String lineNumber = "";

    /**
     * Default constructor
     */
    public PhoneNumber() {
    }

    /**
     * Constructs a new phone number
     * @param areaCode - area code
     * @param prefix - prefix (aka exchange)
     * @param lineNumber - line number
     */
    public PhoneNumber(String areaCode, String prefix, String lineNumber) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNumber = lineNumber;
    }

    /**
     * Returns the area code
     * @return area code
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * Sets the area code
     * @param areaCode - area code
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * Returns the prefix
     * @return prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix
     * @param prefix - prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the line number
     * @return line number
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the line number
     * @param lineNumber - line number
     */
    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Returns a formatted representation of the phone number
     * @return phone number
     */
    @Override
    public String toString() {
        return areaCode + "-" + prefix + "-" + lineNumber;
    }
}
