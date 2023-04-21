package com.misyshealthcare.connect.base.clinicaldata;

import java.util.Calendar;

public class Measurements {

    private Calendar entryDateTime;

    private String description;

    private String position;

    private String location;

    private String value;

    private String unitOfMeasure;

    private Code code;

    private String comment;

    private String source;

    private Code[] codes;

    public Measurements() {
    }

    public Measurements(Calendar entryDateTime, String description, String position, String location, String value, String unitOfMeasure, Code[] codes, String comment, String source) {
        this.entryDateTime = entryDateTime;
        this.description = description;
        this.position = position;
        this.location = location;
        this.value = value;
        this.unitOfMeasure = unitOfMeasure;
        this.comment = comment;
        this.source = source;
        this.codes = codes;
        if (codes != null && codes.length > 0) {
            this.code = codes[0];
        }
    }

    /**
	 * @return the entryDateTime
	 */
    public Calendar getEntryDateTime() {
        return entryDateTime;
    }

    /**
	 * @param entryDateTime the entryDateTime to set
	 */
    public void setEntryDateTime(Calendar entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the position
	 */
    public String getPosition() {
        return position;
    }

    /**
	 * @param position the position to set
	 */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
	 * @return the location
	 */
    public String getLocation() {
        return location;
    }

    /**
	 * @param location the location to set
	 */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * @return the unitOfMeasure
	 */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
	 * @param unitOfMeasure the unitOfMeasure to set
	 */
    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    /**
	 * @return the code
	 */
    public Code getCode() {
        return code;
    }

    /**
	 * @param code the code to set
	 */
    public void setCode(Code code) {
        this.code = code;
    }

    /**
	 * @return the comment
	 */
    public String getComment() {
        return comment;
    }

    /**
	 * @param comment the comment to set
	 */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
	 * @return the source
	 */
    public String getSource() {
        return source;
    }

    /**
	 * @param source the source to set
	 */
    public void setSource(String source) {
        this.source = source;
    }

    /**
	 * @return the codes
	 */
    public Code[] getCodes() {
        return codes;
    }

    /**
	 * @param codes the codes to set
	 */
    public void setCodes(Code[] codes) {
        this.codes = codes;
    }
}
