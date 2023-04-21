package org.evogui.components.content;

import org.evogui.api.DialogField;
import org.evogui.api.GUIConstants;
import java.util.List;

public class EditDisplayFieldsDialog {

    @DialogField(label = "Fields ", type = GUIConstants.DIALOG_FIELDS_LIST)
    private List<Boolean> fieldsList;

    public List<Boolean> getFieldsList() {
        return fieldsList;
    }

    public void setFieldsList(List<Boolean> fieldsList) {
        this.fieldsList = fieldsList;
    }
}
