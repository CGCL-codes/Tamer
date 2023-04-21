package tms.client.entities;

import java.util.ArrayList;
import tms.client.accesscontrol.AccessControlledResource;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Sub field for an Index field on an InputModel.
 * 
 * @author Werner Liebenberg
 * @author Wildrich Fourie
 */
public class InputModelSubfield extends InputField implements IsSerializable {

    private ArrayList<InputField> _inputmodelsubfieldsubfields = null;

    private long inputmodelsubfieldid = -1;

    public InputModelSubfield() {
    }

    /**
	 * Create a new InputModelSubfield from the field parameter.
	 * @param field
	 */
    public InputModelSubfield(Field field) {
        this.setFielddatatypeid(field.getFielddatatypeid());
        this.setFielddatatypename(field.getFielddatatypename());
        this.setFieldid(field.getFieldid());
        this.setFieldname(field.getFieldname());
        this.setFieldtypeid(field.getFieldtypeid());
        this.setFieldtypename(field.getFieldtypename());
        this.setMaxlength(field.getMaxlength());
        this.setPresetAttributes(field.getPresetAttributes());
        this.setInuse(field.isInuse());
        this.setDefaultSortIndexField(field.getDefaultSortIndexField());
        this.setProjectField(field.getProjectField());
        this.setSynonymField(field.getSynonymField());
        this.setContextField(field.getContextField());
        this.setDefinitionField(field.getDefinitionField());
        this.setNoteToManagerField(field.getNoteToManager());
        this.setSortindex(field.getSortindex());
        this.setMandatory(field.isMandatory());
        if (field instanceof InputModelSubfield) {
            InputModelSubfield inputField = (InputModelSubfield) field;
            this.setInputmodelid(inputField.getInputmodelid());
            this.setInputmodelfieldid(inputField.getInputmodelfieldid());
            this.setInputmodelsubfieldid(inputField.getInputmodelsubfieldid());
            this.setDefaultvalue(inputField.getDefaultvalue());
            this.setMinoccur(inputField.getMinoccur());
            this.setMaxoccur(inputField.getMaxoccur());
        } else {
            this.setMinoccur(field.getMinoccur());
            this.setMaxoccur(field.getMaxoccur());
        }
    }

    public long getInputmodelsubfieldid() {
        return inputmodelsubfieldid;
    }

    public void setInputmodelsubfieldid(long inputmodelsubfieldid) {
        this.inputmodelsubfieldid = inputmodelsubfieldid;
    }

    @Override
    public ArrayList<InputField> getInputmodelsubfields() {
        return new ArrayList<InputField>();
    }

    public void setInputModelsubfieldsubfields(ArrayList<InputField> inputmodelsubfieldsubfields) {
        _inputmodelsubfieldsubfields = inputmodelsubfieldsubfields;
    }

    @Override
    public ArrayList<InputField> getInputInputModelSubfieldSubFields() {
        if (_inputmodelsubfieldsubfields == null) _inputmodelsubfieldsubfields = new ArrayList<InputField>();
        return _inputmodelsubfieldsubfields;
    }

    public void addInputModelsubfieldsubbfield(InputField field) {
        this.getInputInputModelSubfieldSubFields().add(field);
    }

    @Override
    public boolean isSortindex() {
        return false;
    }

    @Override
    public String toString() {
        return "inputmodelsubfieldid = " + this.getInputmodelsubfieldid() + "\r\n" + super.toString();
    }

    @Override
    public int getResourceType() {
        return AccessControlledResource.RESOURCE_TYPE_INPUTMODELSUBFIELD;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InputModelSubfield) {
            if (this.getInputmodelsubfieldid() == ((InputModelSubfield) obj).getInputmodelsubfieldid()) return true; else return false;
        } else return false;
    }
}
