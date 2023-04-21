package org.jaffa.modules.printing.components.printeroutputtypemaintenance.dto;

import java.util.*;

/** The output for the PrinterOutputTypeMaintenance.
 */
public class PrinterOutputTypeMaintenanceRetrieveOutDto {

    /** Holds value of property outputType. */
    private java.lang.String outputType;

    /** Holds value of property description. */
    private java.lang.String description;

    /** Holds value of property directPrinting. */
    private java.lang.Boolean directPrinting;

    /** Holds value of property createdOn. */
    private org.jaffa.datatypes.DateTime createdOn;

    /** Holds value of property createdBy. */
    private java.lang.String createdBy;

    /** Holds value of property lastChangedOn. */
    private org.jaffa.datatypes.DateTime lastChangedOn;

    /** Holds value of property lastChangedBy. */
    private java.lang.String lastChangedBy;

    /** Holds an array of OutputCommand objects to be returned. */
    private List outputCommandList;

    /** Default Constructor.*/
    public PrinterOutputTypeMaintenanceRetrieveOutDto() {
        outputCommandList = new ArrayList();
    }

    /** Getter for property outputType.
     * @return Value of property outputType.
     */
    public java.lang.String getOutputType() {
        return outputType;
    }

    /** Setter for property outputType.
     * @param outputType New value of property outputType.
     */
    public void setOutputType(java.lang.String outputType) {
        if (outputType == null || outputType.length() == 0) this.outputType = null; else this.outputType = outputType;
    }

    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }

    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        if (description == null || description.length() == 0) this.description = null; else this.description = description;
    }

    /** Getter for property directPrinting.
     * @return Value of property directPrinting.
     */
    public java.lang.Boolean getDirectPrinting() {
        return directPrinting;
    }

    /** Setter for property directPrinting.
     * @param directPrinting New value of property directPrinting.
     */
    public void setDirectPrinting(java.lang.Boolean directPrinting) {
        this.directPrinting = directPrinting;
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

    /** Add OutputCommand objects.
     * @param outputCommand OutputCommand.
     */
    public void addOutputCommand(OutputCommandDto outputCommand) {
        outputCommandList.add(outputCommand);
    }

    /** Add OutputCommand at the specified position.
     * @param outputCommand OutputCommand.
     * @param index The position at which the OutputCommand is to be added.
     */
    public void setOutputCommand(OutputCommandDto outputCommand, int index) {
        if ((index < 0) || (index > outputCommandList.size())) throw new IndexOutOfBoundsException();
        outputCommandList.set(index, outputCommand);
    }

    /** Add an array of OutputCommand objects. This will overwrite existing OutputCommand objects.
     * @param objects An array of OutputCommand objects.
     */
    public void setOutputCommand(OutputCommandDto[] objects) {
        outputCommandList = Arrays.asList(objects);
    }

    /** Clear existing OutputCommand objects.
     */
    public void clearOutputCommand() {
        outputCommandList.clear();
    }

    /** Remove OutputCommand object.
     * @param outputCommand OutputCommand.
     * @return A true indicates a OutputCommand object was removed. A false indicates, the OutputCommand object was not found.
     */
    public boolean removeOutputCommand(OutputCommandDto outputCommand) {
        return outputCommandList.remove(outputCommand);
    }

    /** Returns a OutputCommand object from the specified position.
     * @param index The position index.
     * @return OutputCommand.
     */
    public OutputCommandDto getOutputCommand(int index) {
        if ((index < 0) || (index > outputCommandList.size())) throw new IndexOutOfBoundsException();
        return (OutputCommandDto) outputCommandList.get(index);
    }

    /** Returns an array of OutputCommand objects.
     * @return An array of OutputCommand objects.
     */
    public OutputCommandDto[] getOutputCommand() {
        return (OutputCommandDto[]) outputCommandList.toArray(new OutputCommandDto[0]);
    }

    /** Returns a count of OutputCommand objects.
     * @return The count of OutputCommand objects.
     */
    public int getOutputCommandCount() {
        return outputCommandList.size();
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<PrinterOutputTypeMaintenanceRetrieveOutDto>");
        buf.append("<outputType>");
        if (outputType != null) buf.append(outputType);
        buf.append("</outputType>");
        buf.append("<description>");
        if (description != null) buf.append(description);
        buf.append("</description>");
        buf.append("<directPrinting>");
        if (directPrinting != null) buf.append(directPrinting);
        buf.append("</directPrinting>");
        buf.append("<createdOn>");
        if (createdOn != null) buf.append(createdOn);
        buf.append("</createdOn>");
        buf.append("<createdBy>");
        if (createdBy != null) buf.append(createdBy);
        buf.append("</createdBy>");
        buf.append("<lastChangedOn>");
        if (lastChangedOn != null) buf.append(lastChangedOn);
        buf.append("</lastChangedOn>");
        buf.append("<lastChangedBy>");
        if (lastChangedBy != null) buf.append(lastChangedBy);
        buf.append("</lastChangedBy>");
        buf.append("<outputCommands>");
        OutputCommandDto[] outputCommands = getOutputCommand();
        for (int i = 0; i < outputCommands.length; i++) {
            buf.append(outputCommands[i].toString());
        }
        buf.append("</outputCommands>");
        buf.append("</PrinterOutputTypeMaintenanceRetrieveOutDto>");
        return buf.toString();
    }
}
