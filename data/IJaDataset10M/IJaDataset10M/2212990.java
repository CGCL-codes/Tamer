package com.erclab.xbuilder.formelements;

import com.erclab.internal.xpresso.formelements.Hidden;
import com.erclab.internal.xpresso.forms.Form;

public class DefaultText extends Hidden {

    @Override
    public String[] processSubmittedValues(Form theForm, String[] submittedValues) {
        return new String[] { this.parameters };
    }
}
