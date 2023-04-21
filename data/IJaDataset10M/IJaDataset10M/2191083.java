package org.jaffa.modules.printing.components.formtemplatefinder.dto;

import java.util.*;
import org.jaffa.components.finder.*;
import org.jaffa.components.dto.HeaderDto;

/** The input for the FormTemplateFinder.
 */
public class FormTemplateFinderInDto extends FinderInDto {

    /** Holds value of property headerDto. */
    private HeaderDto headerDto;

    /** Holds value of property formId. */
    private IntegerCriteriaField formId;

    /** Holds value of property templateData. */
    private RawCriteriaField templateData;

    /** Holds value of property layoutData. */
    private RawCriteriaField layoutData;

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
    public IntegerCriteriaField getFormId() {
        return formId;
    }

    /** Setter for property formId.
     * @param formId New value of property formId.
     */
    public void setFormId(IntegerCriteriaField formId) {
        this.formId = formId;
    }

    /** Getter for property templateData.
     * @return Value of property templateData.
     */
    public RawCriteriaField getTemplateData() {
        return templateData;
    }

    /** Setter for property templateData.
     * @param templateData New value of property templateData.
     */
    public void setTemplateData(RawCriteriaField templateData) {
        this.templateData = templateData;
    }

    /** Getter for property layoutData.
     * @return Value of property layoutData.
     */
    public RawCriteriaField getLayoutData() {
        return layoutData;
    }

    /** Setter for property layoutData.
     * @param layoutData New value of property layoutData.
     */
    public void setLayoutData(RawCriteriaField layoutData) {
        this.layoutData = layoutData;
    }

    /** Returns the debug information
     * @return The debug information
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<FormTemplateFinderInDto>");
        buf.append("<headerDto>");
        if (headerDto != null) buf.append(headerDto.toString());
        buf.append("</headerDto>");
        buf.append("<formId>");
        if (formId != null) buf.append(formId);
        buf.append("</formId>");
        buf.append("<templateData>");
        if (templateData != null) buf.append(templateData);
        buf.append("</templateData>");
        buf.append("<layoutData>");
        if (layoutData != null) buf.append(layoutData);
        buf.append("</layoutData>");
        buf.append("<orderByFields>");
        OrderByField[] orderByFields = getOrderByFields();
        if (orderByFields != null) {
            for (int i = 0; i < orderByFields.length; i++) buf.append(orderByFields[i].toString());
        }
        buf.append("</orderByFields>");
        buf.append("<maxRecords>");
        if (getMaxRecords() != null) buf.append(getMaxRecords());
        buf.append("</maxRecords>");
        buf.append("</FormTemplateFinderInDto>");
        return buf.toString();
    }
}
