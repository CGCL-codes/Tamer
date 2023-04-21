package org.jaffa.modules.printing.components.formeventmaintenance.dto;

/** The output for the FormEventMaintenance prevalidations.
 */
public class FormEventMaintenancePrevalidateOutDto extends FormEventMaintenanceRetrieveOutDto {

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<FormEventMaintenancePrevalidateOutDto>");
        buf.append("<eventName>");
        if (getEventName() != null) buf.append(getEventName());
        buf.append("</eventName>");
        buf.append("<description>");
        if (getDescription() != null) buf.append(getDescription());
        buf.append("</description>");
        buf.append("<formUsages>");
        FormUsageDto[] formUsages = getFormUsage();
        for (int i = 0; i < formUsages.length; i++) {
            buf.append(formUsages[i].toString());
        }
        buf.append("</formUsages>");
        buf.append("</FormEventMaintenancePrevalidateOutDto>");
        return buf.toString();
    }
}
