package com.erclab.internal.xpresso.formelements;

import com.erclab.internal.xpresso.formelements.SimpleInputBox;
import com.erclab.internal.xpresso.forms.Form;

public class HiddenSessionId extends SimpleInputBox {

    public String toHTML(Form theForm) {
        return "<input type=\"hidden\" name=\"Field" + this.idFormElement + "\"" + "id=\"" + this.destinationColumn + " value=\"\">";
    }

    public String[] processSubmittedValues(Form theForm, String[] submittedValues) {
        return new String[] { theForm.getFormParameters().getUser().getSessionId() + "" };
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
