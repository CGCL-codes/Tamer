package org.jaffa.modules.printing.components.formdefinitionviewer.dto;

import java.util.*;
import org.jaffa.components.dto.HeaderDto;

/** The input for the FormDefinitionViewer.
 */
public class FormDefinitionViewerInDto {

    /** Holds value of property headerDto. */
    private HeaderDto headerDto;

    /** Holds value of property formId. */
    private java.lang.Long formId;

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

    /** Getter for property formId.
     * @return Value of property formId.
     */
    public java.lang.Long getFormId() {
        return formId;
    }

    /** Setter for property formId.
     * @param formId New value of property formId.
     */
    public void setFormId(java.lang.Long formId) {
        this.formId = formId;
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<FormDefinitionViewerInDto>");
        buf.append("<headerDto>");
        if (headerDto != null) buf.append(headerDto.toString());
        buf.append("</headerDto>");
        buf.append("<formId>");
        if (formId != null) buf.append(formId);
        buf.append("</formId>");
        buf.append("</FormDefinitionViewerInDto>");
        return buf.toString();
    }
}
