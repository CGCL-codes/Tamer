package org.jaffa.components.audit.components.audittransactionobjectmaintenance.dto;

import java.util.*;
import org.jaffa.components.dto.HeaderDto;

/** The input for the AuditTransactionObjectMaintenance.
 */
public class AuditTransactionObjectMaintenanceRetrieveInDto {

    /** Holds value of property headerDto. */
    private HeaderDto headerDto;

    /** Holds value of property auditObjectId. */
    private java.lang.String auditObjectId;

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

    /** Getter for property auditObjectId.
     * @return Value of property auditObjectId.
     */
    public java.lang.String getAuditObjectId() {
        return auditObjectId;
    }

    /** Setter for property auditObjectId.
     * @param auditObjectId New value of property auditObjectId.
     */
    public void setAuditObjectId(java.lang.String auditObjectId) {
        if (auditObjectId == null || auditObjectId.length() == 0) this.auditObjectId = null; else this.auditObjectId = auditObjectId;
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<AuditTransactionObjectMaintenanceRetrieveInDto>");
        buf.append("<headerDto>");
        if (headerDto != null) buf.append(headerDto.toString());
        buf.append("</headerDto>");
        buf.append("<auditObjectId>");
        if (auditObjectId != null) buf.append(auditObjectId);
        buf.append("</auditObjectId>");
        buf.append("</AuditTransactionObjectMaintenanceRetrieveInDto>");
        return buf.toString();
    }
}
