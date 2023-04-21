package org.jaffa.modules.printing.components.outputcommandmaintenance.dto;

import java.util.*;
import org.jaffa.components.dto.HeaderDto;

/** The input for the OutputCommandMaintenance.
 */
public class OutputCommandMaintenanceDeleteInDto {

    /** Holds value of property headerDto. */
    private HeaderDto headerDto;

    /** Holds value of property performDirtyReadCheck. */
    private Boolean performDirtyReadCheck;

    /** Holds value of property outputCommandId. */
    private java.lang.Long outputCommandId;

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

    /** Getter for property outputCommandId.
     * This property is used when performing the dirty read check.
     * @return Value of property outputCommandId.
     */
    public java.lang.Long getOutputCommandId() {
        return outputCommandId;
    }

    /** Setter for property outputCommandId.
     * This property is used when performing the dirty read check.
     * @param outputCommandId New value of property outputCommandId.
     */
    public void setOutputCommandId(java.lang.Long outputCommandId) {
        this.outputCommandId = outputCommandId;
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<OutputCommandMaintenanceDeleteInDto>");
        buf.append("<headerDto>");
        if (headerDto != null) buf.append(headerDto.toString());
        buf.append("</headerDto>");
        buf.append("<performDirtyReadCheck>");
        if (performDirtyReadCheck != null) buf.append(performDirtyReadCheck.toString());
        buf.append("</performDirtyReadCheck>");
        buf.append("<outputCommandId>");
        if (outputCommandId != null) buf.append(outputCommandId);
        buf.append("</outputCommandId>");
        buf.append("</OutputCommandMaintenanceDeleteInDto>");
        return buf.toString();
    }
}
