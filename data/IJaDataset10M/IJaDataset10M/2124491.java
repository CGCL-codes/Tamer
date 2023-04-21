package com.aha.zk.form;

import org.zkoss.zul.Textbox;
import com.aha.zk.form.api.FormInputElement;

public class FormTextbox extends Textbox implements FormInputElement {

    @Override
    public Object getObjectValue() {
        return super.getText();
    }

    @Override
    public void setObjectValue(Object val) {
        super.setText((String) val);
    }
}
