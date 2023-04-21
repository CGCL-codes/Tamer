package org.jaffa.components.attachment.components.attachmentmaintenance.dto;

import java.util.*;
import org.jaffa.components.dto.HeaderDto;

/** The input for the AttachmentMaintenance.
 */
public class AttachmentMaintenanceDeleteInDto {

    /** Holds value of property headerDto. */
    private HeaderDto headerDto;

    /** Holds value of property performDirtyReadCheck. */
    private Boolean performDirtyReadCheck;

    /** Holds value of property attachmentId. */
    private java.lang.String attachmentId;

    /** Holds value of property lastChangedOn. */
    private org.jaffa.datatypes.DateTime lastChangedOn;

    /** Getter for property headerDto.
     * @return Value of property headerDto.
     */
    public HeaderDto getHeaderDto() {
        return headerDto;
    }

    /** Setter for property headerDto.
     * @param headerDto New value of property headerDto.
     */
    public void setHeaderDto(HeaderDto headerDto) {
        this.headerDto = headerDto;
    }

    /** Getter for property performDirtyReadCheck.
     * @return Value of property performDirtyReadCheck.
     */
    public Boolean getPerformDirtyReadCheck() {
        return performDirtyReadCheck;
    }

    /** Setter for property performDirtyReadCheck.
     * @param performDirtyReadCheck New value of property performDirtyReadCheck.
     */
    public void setPerformDirtyReadCheck(Boolean performDirtyReadCheck) {
        this.performDirtyReadCheck = performDirtyReadCheck;
    }

    /** Getter for property attachmentId.
     * This property is used when performing the dirty read check.
     * @return Value of property attachmentId.
     */
    public java.lang.String getAttachmentId() {
        return attachmentId;
    }

    /** Setter for property attachmentId.
     * This property is used when performing the dirty read check.
     * @param attachmentId New value of property attachmentId.
     */
    public void setAttachmentId(java.lang.String attachmentId) {
        if (attachmentId == null || attachmentId.length() == 0) this.attachmentId = null; else this.attachmentId = attachmentId;
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

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<AttachmentMaintenanceDeleteInDto>");
        buf.append("<headerDto>");
        if (headerDto != null) buf.append(headerDto.toString());
        buf.append("</headerDto>");
        buf.append("<performDirtyReadCheck>");
        if (performDirtyReadCheck != null) buf.append(performDirtyReadCheck.toString());
        buf.append("</performDirtyReadCheck>");
        buf.append("<attachmentId>");
        if (attachmentId != null) buf.append(attachmentId);
        buf.append("</attachmentId>");
        buf.append("<lastChangedOn>");
        if (lastChangedOn != null) buf.append(lastChangedOn);
        buf.append("</lastChangedOn>");
        buf.append("</AttachmentMaintenanceDeleteInDto>");
        return buf.toString();
    }
}
