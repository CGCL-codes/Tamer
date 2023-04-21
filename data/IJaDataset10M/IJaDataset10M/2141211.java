package org.xaware.ide.xadev.datamodel;

import org.jdom.Element;

/**
 * Contains information about the file.
 *
 * @author M Pavan Kumar
 * @version 1.0
 */
public class FileInfo {

    /** Reference to String. */
    private String seperator;

    /** Reference to String. */
    private String record_type;

    /** Reference to String. */
    private String request_type;

    /** Integer holding fieldcount. */
    private int fieldCount;

    /** Reference to Element. */
    private Element recordLayout;

    /** Reference to String. */
    private String recordStartPosition;

    /** Integer holding record length. */
    private int recordLength;

    /** Reference to String. */
    private String characterEncoding;

    /** Reference to String. */
    private Element designerElement = null;

    /** Reference to String. */
    private String bizComponentType;

    /** Reference to String. */
    private String file;

    private boolean ignoreUnmatchedRecords;

    /**
     * Creates a new FileInfo object.
     */
    public FileInfo() {
    }

    /**
     * Creates a new FileInfo object.
     *
     * @param separator String.
     */
    public FileInfo(final String separator) {
        this.seperator = separator;
    }

    /**
     * To set the seperator.
     *
     * @param seperator String.
     */
    public void setSeperator(final String seperator) {
        this.seperator = seperator;
    }

    /**
     * To set the ignoreUnmatchedRecords.
     *
     * @param ignore boolean.
     */
    public void setIgnoreUnmatchedRecords(final boolean ignore) {
        this.ignoreUnmatchedRecords = ignore;
    }

    /**
     * To set the record type.
     *
     * @param record_type String.
     */
    public void setRecord_type(final String record_type) {
        this.record_type = record_type;
    }

    /**
     * To set the fieldcount.
     *
     * @param fieldCount integer.
     */
    public void setFieldCount(final int fieldCount) {
        this.fieldCount = fieldCount;
    }

    /**
     * To set the record layout.
     *
     * @param recordLayout Element
     */
    public void setRecordLayout(final Element recordLayout) {
        this.recordLayout = recordLayout;
    }

    /**
     * To set record start position.
     *
     * @param recordStartPosition String.
     */
    public void setRecordStartPosition(final String recordStartPosition) {
        this.recordStartPosition = recordStartPosition;
    }

    /**
     * To set the record length.
     *
     * @param recordLength integer.
     */
    public void setRecordLength(final int recordLength) {
        this.recordLength = recordLength;
    }

    /**
     * To get the seperator.
     *
     * @return seperator String.
     */
    public String getSeperator() {
        return seperator;
    }

    /**
     * To get the ignoreUnmatchedRecords.
     *
     * return boolean.
     */
    public boolean getIgnoreUnmatchedRecords() {
        return this.ignoreUnmatchedRecords;
    }

    /**
     * To get the record type.
     *
     * @return record type string.
     */
    public String getRecord_type() {
        return record_type;
    }

    /**
     * To get field count.
     *
     * @return fieldcount integer.
     */
    public int getFieldCount() {
        return fieldCount;
    }

    /**
     * To get record layout.
     *
     * @return record layout Element.
     */
    public Element getRecordLayout() {
        return recordLayout;
    }

    /**
     * To get record start position.
     *
     * @return start position String.
     */
    public String getRecordStartPosition() {
        return recordStartPosition;
    }

    /**
     * To get record length.
     *
     * @return record length integer.
     */
    public int getRecordLength() {
        return recordLength;
    }

    /**
     * To get the character Encoding
     *
     * @return string
     */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * To set the character Encoding
     *
     * @param code string
     */
    public void setCharacterEncoding(final String code) {
        characterEncoding = code;
    }

    /**
     * Returns the Designer Element
     *
     * @return Designer Element
     */
    public Element getDesignerElement() {
        return designerElement;
    }

    /**
     * Sets the Designer Element
     *
     * @param elem Designer Element
     */
    public void setDesignerElement(final Element elem) {
        designerElement = elem;
    }

    /**
     * Sets the bizComponentType
     *
     * @return bizComponentType
     */
    public String getBizComponentType() {
        return bizComponentType;
    }

    /**
     * returns the bizComponentType
     *
     * @param bizComponentType String
     */
    public void setBizComponentType(final String bizComponentType) {
        this.bizComponentType = bizComponentType;
    }

    /**
     * To get the request type.
     *
     * @return requesttype as String.
     */
    public String getRequest_type() {
        return request_type;
    }

    /**
     * To set the request type.
     *
     * @param request_type String.
     */
    public void setRequest_type(final String request_type) {
        this.request_type = request_type;
    }

    /**
     * To return file as String
     *
     * @return String filepath.
     */
    public String getFile() {
        return file;
    }

    /**
     * To set the file.
     *
     * @param file String used to set file.
     */
    public void setFile(final String file) {
        this.file = file;
    }
}
