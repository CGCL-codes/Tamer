package org.jaffa.components.audit.components.auditdefinitionobjectmaintenance.dto;

/** The related object returned by the AuditDefinitionObjectMaintenance.
 */
public class AuditDefinitionFieldDto {

    /** Holds value of property objectName. */
    private java.lang.String objectName;

    /** Holds value of property fieldName. */
    private java.lang.String fieldName;

    /** Holds value of property isAuditTrigger. */
    private java.lang.Boolean isAuditTrigger;

    /** Holds value of property isAuditField. */
    private java.lang.Boolean isAuditField;

    /** Getter for property objectName.
     * @return Value of property objectName.
     */
    public java.lang.String getObjectName() {
        return objectName;
    }

    /** Setter for property objectName.
     * @param objectName New value of property objectName.
     */
    public void setObjectName(java.lang.String objectName) {
        if (objectName == null || objectName.length() == 0) this.objectName = null; else this.objectName = objectName;
    }

    /** Getter for property fieldName.
     * @return Value of property fieldName.
     */
    public java.lang.String getFieldName() {
        return fieldName;
    }

    /** Setter for property fieldName.
     * @param fieldName New value of property fieldName.
     */
    public void setFieldName(java.lang.String fieldName) {
        if (fieldName == null || fieldName.length() == 0) this.fieldName = null; else this.fieldName = fieldName;
    }

    /** Getter for property isAuditTrigger.
     * @return Value of property isAuditTrigger.
     */
    public java.lang.Boolean getIsAuditTrigger() {
        return isAuditTrigger;
    }

    /** Setter for property isAuditTrigger.
     * @param isAuditTrigger New value of property isAuditTrigger.
     */
    public void setIsAuditTrigger(java.lang.Boolean isAuditTrigger) {
        this.isAuditTrigger = isAuditTrigger;
    }

    /** Getter for property isAuditField.
     * @return Value of property isAuditField.
     */
    public java.lang.Boolean getIsAuditField() {
        return isAuditField;
    }

    /** Setter for property isAuditField.
     * @param isAuditField New value of property isAuditField.
     */
    public void setIsAuditField(java.lang.Boolean isAuditField) {
        this.isAuditField = isAuditField;
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<AuditDefinitionFieldDto>");
        buf.append("<objectName>");
        if (objectName != null) buf.append(objectName);
        buf.append("</objectName>");
        buf.append("<fieldName>");
        if (fieldName != null) buf.append(fieldName);
        buf.append("</fieldName>");
        buf.append("<isAuditTrigger>");
        if (isAuditTrigger != null) buf.append(isAuditTrigger);
        buf.append("</isAuditTrigger>");
        buf.append("<isAuditField>");
        if (isAuditField != null) buf.append(isAuditField);
        buf.append("</isAuditField>");
        buf.append("</AuditDefinitionFieldDto>");
        return buf.toString();
    }
}
