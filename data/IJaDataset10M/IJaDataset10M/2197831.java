package org.jaffa.modules.printing.components.formusagelookup.dto;

import org.jaffa.util.StringHelper;
import org.jaffa.datatypes.Formatter;

/** The output for the FormUsageLookup contains an array of instances of this class.
 */
public class FormUsageLookupOutRowDto {

    /** Holds value of property formName. */
    private java.lang.String formName;

    /** Holds value of property eventName. */
    private java.lang.String eventName;

    /** Holds value of property formAlternate. */
    private java.lang.String formAlternate;

    /** Holds value of property copies. */
    private java.lang.Long copies;

    /** Holds value of property createdOn. */
    private org.jaffa.datatypes.DateTime createdOn;

    /** Holds value of property createdBy. */
    private java.lang.String createdBy;

    /** Holds value of property lastChangedOn. */
    private org.jaffa.datatypes.DateTime lastChangedOn;

    /** Holds value of property lastChangedBy. */
    private java.lang.String lastChangedBy;

    /** Getter for property formName.
     * @return Value of property formName.
     */
    public java.lang.String getFormName() {
        return formName;
    }

    /** Setter for property formName.
     * @param formName New value of property formName.
     */
    public void setFormName(java.lang.String formName) {
        if (formName == null || formName.length() == 0) this.formName = null; else this.formName = formName;
    }

    /** Getter for property eventName.
     * @return Value of property eventName.
     */
    public java.lang.String getEventName() {
        return eventName;
    }

    /** Setter for property eventName.
     * @param eventName New value of property eventName.
     */
    public void setEventName(java.lang.String eventName) {
        if (eventName == null || eventName.length() == 0) this.eventName = null; else this.eventName = eventName;
    }

    /** Getter for property formAlternate.
     * @return Value of property formAlternate.
     */
    public java.lang.String getFormAlternate() {
        return formAlternate;
    }

    /** Setter for property formAlternate.
     * @param formAlternate New value of property formAlternate.
     */
    public void setFormAlternate(java.lang.String formAlternate) {
        if (formAlternate == null || formAlternate.length() == 0) this.formAlternate = null; else this.formAlternate = formAlternate;
    }

    /** Getter for property copies.
     * @return Value of property copies.
     */
    public java.lang.Long getCopies() {
        return copies;
    }

    /** Setter for property copies.
     * @param copies New value of property copies.
     */
    public void setCopies(java.lang.Long copies) {
        this.copies = copies;
    }

    /** Getter for property createdOn.
     * @return Value of property createdOn.
     */
    public org.jaffa.datatypes.DateTime getCreatedOn() {
        return createdOn;
    }

    /** Setter for property createdOn.
     * @param createdOn New value of property createdOn.
     */
    public void setCreatedOn(org.jaffa.datatypes.DateTime createdOn) {
        this.createdOn = createdOn;
    }

    /** Getter for property createdBy.
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /** Setter for property createdBy.
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        if (createdBy == null || createdBy.length() == 0) this.createdBy = null; else this.createdBy = createdBy;
    }

    /** Getter for property lastChangedOn.
     * @return Value of property lastChangedOn.
     */
    public org.jaffa.datatypes.DateTime getLastChangedOn() {
        return lastChangedOn;
    }

    /** Setter for property lastChangedOn.
     * @param lastChangedOn New value of property lastChangedOn.
     */
    public void setLastChangedOn(org.jaffa.datatypes.DateTime lastChangedOn) {
        this.lastChangedOn = lastChangedOn;
    }

    /** Getter for property lastChangedBy.
     * @return Value of property lastChangedBy.
     */
    public java.lang.String getLastChangedBy() {
        return lastChangedBy;
    }

    /** Setter for property lastChangedBy.
     * @param lastChangedBy New value of property lastChangedBy.
     */
    public void setLastChangedBy(java.lang.String lastChangedBy) {
        if (lastChangedBy == null || lastChangedBy.length() == 0) this.lastChangedBy = null; else this.lastChangedBy = lastChangedBy;
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<FormUsageLookupOutRowDto>");
        buf.append("<formName>");
        if (formName != null) buf.append(StringHelper.convertToHTML(Formatter.format(formName)));
        buf.append("</formName>");
        buf.append("<eventName>");
        if (eventName != null) buf.append(StringHelper.convertToHTML(Formatter.format(eventName)));
        buf.append("</eventName>");
        buf.append("<formAlternate>");
        if (formAlternate != null) buf.append(StringHelper.convertToHTML(Formatter.format(formAlternate)));
        buf.append("</formAlternate>");
        buf.append("<copies>");
        if (copies != null) buf.append(StringHelper.convertToHTML(Formatter.format(copies)));
        buf.append("</copies>");
        buf.append("<createdOn>");
        if (createdOn != null) buf.append(StringHelper.convertToHTML(Formatter.format(createdOn)));
        buf.append("</createdOn>");
        buf.append("<createdBy>");
        if (createdBy != null) buf.append(StringHelper.convertToHTML(Formatter.format(createdBy)));
        buf.append("</createdBy>");
        buf.append("<lastChangedOn>");
        if (lastChangedOn != null) buf.append(StringHelper.convertToHTML(Formatter.format(lastChangedOn)));
        buf.append("</lastChangedOn>");
        buf.append("<lastChangedBy>");
        if (lastChangedBy != null) buf.append(StringHelper.convertToHTML(Formatter.format(lastChangedBy)));
        buf.append("</lastChangedBy>");
        buf.append("</FormUsageLookupOutRowDto>");
        return buf.toString();
    }
}
