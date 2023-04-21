package ch.arpage.collaboweb.struts.forms;

import java.util.List;
import java.util.Map;
import ch.arpage.collaboweb.model.Attribute;
import ch.arpage.collaboweb.model.Validation;

/**
 * ActionForm for the attribute model class
 * 
 * @see Attribute
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class AttributeForm extends AbstractForm {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private Attribute attribute;

    /**
	 * Creates a new AttributeForm.
	 */
    public AttributeForm() {
        this(new Attribute());
    }

    /**
	 * Creates a new AttributeForm based on the given attribute.
	 * @param attribute the attribute
	 */
    public AttributeForm(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
	 * Set the typeId.
	 * @param typeId the typeId to set
	 */
    public void setTypeId(int typeId) {
        attribute.setTypeId(typeId);
    }

    /**
	 * Returns the typeId.
	 * @return the typeId
	 */
    public int getTypeId() {
        return attribute.getTypeId();
    }

    /**
	 * Returns the attribute.
	 * @return the attribute
	 */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
	 * Returns the attributeId.
	 * @return the attributeId
	 * @see ch.arpage.collaboweb.model.Attribute#getAttributeId()
	 */
    public int getAttributeId() {
        return attribute.getAttributeId();
    }

    /**
	 * Returns the choices.
	 * @return the choices
	 * @see ch.arpage.collaboweb.model.Attribute#getChoices()
	 */
    public String getChoices() {
        return attribute.getChoices();
    }

    /**
	 * Returns the dataType.
	 * @return the dataType
	 * @see ch.arpage.collaboweb.model.Attribute#getDataType()
	 */
    public int getDataType() {
        return attribute.getDataType();
    }

    /**
	 * Returns the defaultValue.
	 * @return the dafaultValue
	 * @see ch.arpage.collaboweb.model.Attribute#getDefaultValue()
	 */
    public String getDefaultValue() {
        return attribute.getDefaultValue();
    }

    /**
	 * Returns the labels.
	 * @return the labels
	 * @see ch.arpage.collaboweb.model.LabelableBean#getLabels()
	 */
    public Map<String, String> getLabels() {
        return attribute.getLabels();
    }

    /**
	 * Returns the searchFieldType.
	 * @return the searchFieldType
	 * @see ch.arpage.collaboweb.model.Attribute#getSearchFieldType()
	 */
    public int getSearchFieldType() {
        return attribute.getSearchFieldType();
    }

    /**
	 * Set the attributeId
	 * @param attributeId	The attributeId to set
	 * @see ch.arpage.collaboweb.model.Attribute#setAttributeId(int)
	 */
    public void setAttributeId(int attributeId) {
        attribute.setAttributeId(attributeId);
    }

    /**
	 * Set the choices
	 * @param choices	The choices to set
	 * @see ch.arpage.collaboweb.model.Attribute#setChoices(java.lang.String)
	 */
    public void setChoices(String choices) {
        attribute.setChoices(choices);
    }

    /**
	 * Set the dataType
	 * @param dataType	The dataType to set
	 * @see ch.arpage.collaboweb.model.Attribute#setDataType(int)
	 */
    public void setDataType(int dataType) {
        attribute.setDataType(dataType);
    }

    /**
	 * Set the defaultValue
	 * @param defaultValue	The defaultValue to set
	 * @see ch.arpage.collaboweb.model.Attribute#setDefaultValue(java.lang.String)
	 */
    public void setDefaultValue(String defaultValue) {
        attribute.setDefaultValue(defaultValue);
    }

    /**
	 * Set the labels
	 * @param labels	The labels to set
	 * @see ch.arpage.collaboweb.model.LabelableBean#setLabels(java.util.Map)
	 */
    public void setLabels(Map<String, String> labels) {
        attribute.setLabels(labels);
    }

    /**
	 * Set the searchFieldType
	 * @param searchFieldType	The searchFieldType to set
	 * @see ch.arpage.collaboweb.model.Attribute#setSearchFieldType(int)
	 */
    public void setSearchFieldType(int searchFieldType) {
        attribute.setSearchFieldType(searchFieldType);
    }

    /**
	 * Returns the calculated.
	 * @return the calculated
	 * @see ch.arpage.collaboweb.model.Attribute#isCalculated()
	 */
    public boolean isCalculated() {
        return attribute.isCalculated();
    }

    /**
	 * Set the calculated
	 * @param calculated	The calculated to set
	 * @see ch.arpage.collaboweb.model.Attribute#setCalculated(boolean)
	 */
    public void setCalculated(boolean calculated) {
        attribute.setCalculated(calculated);
    }

    /**
	 * Returns the editor.
	 * @return the editor
	 * @see ch.arpage.collaboweb.model.Attribute#getEditor()
	 */
    public int getEditor() {
        return attribute.getEditor();
    }

    /**
	 * Returns the formatter.
	 * @return the formatter
	 * @see ch.arpage.collaboweb.model.Attribute#getFormatter()
	 */
    public String getFormatter() {
        return attribute.getFormatter();
    }

    /**
	 * Set the editor
	 * @param editor	The editor to set
	 * @see ch.arpage.collaboweb.model.Attribute#setEditor(int)
	 */
    public void setEditor(int editor) {
        attribute.setEditor(editor);
    }

    /**
	 * Set the formatter
	 * @param formatter	The formatter to set
	 * @see ch.arpage.collaboweb.model.Attribute#setFormatter(java.lang.String)
	 */
    public void setFormatter(String formatter) {
        attribute.setFormatter(formatter);
    }

    /**
	 * Returns the formOrder.
	 * @return the formOrder
	 * @see ch.arpage.collaboweb.model.Attribute#getFormOrder()
	 */
    public int getFormOrder() {
        return attribute.getFormOrder();
    }

    /**
	 * Set the formOrder
	 * @param formOrder	The formOrder to set
	 * @see ch.arpage.collaboweb.model.Attribute#setFormOrder(int)
	 */
    public void setFormOrder(int formOrder) {
        attribute.setFormOrder(formOrder);
    }

    /**
	 * Returns the loadInList.
	 * @return the loadInList
	 * @see ch.arpage.collaboweb.model.Attribute#isLoadInList()
	 */
    public boolean isLoadInList() {
        return attribute.isLoadInList();
    }

    /**
	 * Set the loadInList
	 * @param loadInList	The loadInList to set
	 * @see ch.arpage.collaboweb.model.Attribute#setLoadInList(boolean)
	 */
    public void setLoadInList(boolean loadInList) {
        attribute.setLoadInList(loadInList);
    }

    /**
	 * Returns the validations.
	 * @return the validations
	 * @see ch.arpage.collaboweb.model.Attribute#getValidations()
	 */
    public List<Validation> getValidations() {
        return attribute.getValidations();
    }

    /**
	 * Returns the identifier.
	 * @return the identifier
	 * @see ch.arpage.collaboweb.model.Attribute#getIdentifier()
	 */
    public String getIdentifier() {
        return attribute.getIdentifier();
    }

    /**
	 * Set the identifier.
	 * @param identifier	The identifier to set
	 * @see ch.arpage.collaboweb.model.Attribute#setIdentifier(java.lang.String)
	 */
    public void setIdentifier(String identifier) {
        attribute.setIdentifier(identifier);
    }

    /**
	 * Returns the namePart.
	 * @return the namePart
	 * @see ch.arpage.collaboweb.model.Attribute#isNamePart()
	 */
    public boolean isNamePart() {
        return attribute.isNamePart();
    }

    /**
	 * Set the namePart.
	 * @param namePart	The namePart to set
	 * @see ch.arpage.collaboweb.model.Attribute#setNamePart(boolean)
	 */
    public void setNamePart(boolean namePart) {
        attribute.setNamePart(namePart);
    }
}
