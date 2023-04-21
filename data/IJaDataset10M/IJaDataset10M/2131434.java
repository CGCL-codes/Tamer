package org.blueoxygen.cimande.gx.field;

import java.util.ArrayList;
import org.blueoxygen.cimande.gx.entity.GxField;

public class ListField extends FieldForm {

    public String execute() {
        setFields((ArrayList<GxField>) manager.getList("FROM " + GxField.class.getName() + " g WHERE g.logInformation.activeFlag=1", null, null));
        modelMap.put("fieldlist", getFields());
        return SUCCESS;
    }
}
