package net.sourceforge.oracle.jutils;

/**
 *
 * @author asales
 */
public class JackSumAlgo {

    private int id;

    private String description;

    private String encoding;

    private int groupId;

    private String name;

    private String separator;

    private long value;

    public JackSumAlgo() {
    }

    /**
     * @return the description
     */
    public String getDescription() {
        if (description == null) {
            return "";
        } else {
            return description;
        }
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        if (encoding == null) {
            return "";
        } else {
            return encoding;
        }
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the name
     */
    public String getName() {
        if (name == null) {
            return "";
        } else {
            return name;
        }
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the separator
     */
    public String getSeparator() {
        if (separator == null) {
            return "";
        } else {
            return separator;
        }
    }

    /**
     * @param separator the separator to set
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * @return the value
     */
    public long getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
