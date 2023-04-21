package pedro.mda.model;

public class TextFieldModel extends EditFieldModel {

    public TextFieldModel() {
        super();
    }

    public Object clone() {
        TextFieldModel cloneField = new TextFieldModel();
        super.populateCloneAttributes(this, cloneField);
        return cloneField;
    }
}
