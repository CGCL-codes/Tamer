package org.jaffa.modules.printing.components.printeroutputtypemaintenance.dto;

import java.util.*;
import org.jaffa.components.dto.HeaderDto;

/** The input for the PrinterOutputTypeMaintenance.
 */
public class PrinterOutputTypeMaintenanceDeleteInDto {

    /** Holds value of property headerDto. */
    private HeaderDto headerDto;

    /** Holds value of property performDirtyReadCheck. */
    private Boolean performDirtyReadCheck;

    /** Holds value of property outputType. */
    private java.lang.String outputType;

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

    /** Getter for property outputType.
     * This property is used when performing the dirty read check.
     * @return Value of property outputType.
     */
    public java.lang.String getOutputType() {
        return outputType;
    }

    /** Setter for property outputType.
     * This property is used when performing the dirty read check.
     * @param outputType New value of property outputType.
     */
    public void setOutputType(java.lang.String outputType) {
        if (outputType == null || outputType.length() == 0) this.outputType = null; else this.outputType = outputType;
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
        buf.append("<PrinterOutputTypeMaintenanceDeleteInDto>");
        buf.append("<headerDto>");
        if (headerDto != null) buf.append(headerDto.toString());
        buf.append("</headerDto>");
        buf.append("<performDirtyReadCheck>");
        if (performDirtyReadCheck != null) buf.append(performDirtyReadCheck.toString());
        buf.append("</performDirtyReadCheck>");
        buf.append("<outputType>");
        if (outputType != null) buf.append(outputType);
        buf.append("</outputType>");
        buf.append("<lastChangedOn>");
        if (lastChangedOn != null) buf.append(lastChangedOn);
        buf.append("</lastChangedOn>");
        buf.append("</PrinterOutputTypeMaintenanceDeleteInDto>");
        return buf.toString();
    }
}
