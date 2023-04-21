package org.jaffa.metadata;

import org.jaffa.datatypes.Defaults;

/**
 * An instance of this class will hold meta information for a Boolean field.
 */
public class BooleanFieldMetaData extends FieldMetaData {

    /** The default width.*/
    public static final int DEFAULT_WIDTH = 2;

    /** The default layout.*/
    public static final String DEFAULT_LAYOUT = null;

    /** The default pattern.*/
    public static final String DEFAULT_PATTERN = null;

    private String m_layout = null;

    private String m_pattern = null;

    /** Creates an instance.
     */
    public BooleanFieldMetaData() {
        this(null, null, Boolean.FALSE, null, null);
    }

    /** Creates an instance.
     * @param name The field name.
     * @param labelToken The token used for displaying labels.
     * @param isMandatory Indicates if the field is mandatory.
     * @param layout The layout.
     * @param pattern The pattern.
     */
    public BooleanFieldMetaData(String name, String labelToken, Boolean isMandatory, String layout, String pattern) {
        super(name, Defaults.BOOLEAN, labelToken, isMandatory);
        m_layout = layout;
        m_pattern = pattern;
    }

    /** Getter for property layout.
     * @return Value of property layout.
     */
    public String getLayout() {
        return m_layout;
    }

    /** Getter for property pattern.
     * @return Value of property pattern.
     */
    public String getPattern() {
        return m_pattern;
    }

    /** Returns a clone of the object.
     * @return a clone of the object.
     */
    public Object clone() {
        return super.clone();
    }

    /** Returns the hash code.
     * @return the hash code.
     */
    public int hashCode() {
        int i = 0;
        i = super.hashCode();
        if (m_layout != null) i += m_layout.hashCode();
        if (m_pattern != null) i += m_pattern.hashCode();
        return i;
    }

    /** Compares this object with another BooleanFieldMetaData object.
     * Returns a true if both the objects have the same properties.
     * @param obj the other BooleanFieldMetaData object.
     * @return a true if both the objects have the same properties.
     */
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj instanceof BooleanFieldMetaData) {
            BooleanFieldMetaData field2 = (BooleanFieldMetaData) obj;
            if (super.equals(field2)) {
                if (((m_layout != null && m_layout.equals(field2.m_layout)) || (m_layout == null && field2.m_layout == null)) && ((m_pattern != null && m_pattern.equals(field2.m_pattern)) || (m_pattern == null && field2.m_pattern == null))) isEqual = true;
            }
        }
        return isEqual;
    }

    /** Compares this object with another BooleanFieldMetaData object.
     * Note: this class has a natural ordering that is inconsistent with equals
     * @param obj the other BooleanFieldMetaData object.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    public int compareTo(Object obj) {
        return super.compareTo(obj);
    }

    /** Returns the diagnostic information.
     * @return the diagnostic information.
     */
    public String toString() {
        String comma = ", ";
        String equals = "=";
        StringBuffer buffer = new StringBuffer(super.toString());
        buffer.append(comma);
        buffer.append("Layout");
        buffer.append(equals);
        buffer.append(m_layout);
        buffer.append(comma);
        buffer.append("Pattern");
        buffer.append(equals);
        buffer.append(m_pattern);
        return buffer.toString();
    }

    /** Getter for property width.
     * @return Value of property width.
     */
    public int getWidth() {
        return DEFAULT_WIDTH;
    }
}
