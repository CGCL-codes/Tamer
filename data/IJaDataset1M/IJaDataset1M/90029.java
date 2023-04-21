package org.nextframework.bean.editors;

import java.beans.PropertyEditorSupport;
import org.apache.commons.lang.StringUtils;
import org.nextframework.types.Telefone;

public class TelefonePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.isEmpty(text)) {
            setValue(new Telefone(text));
        } else {
            setValue(null);
        }
    }

    @Override
    public String getAsText() {
        if (getValue() == null) {
            return "";
        }
        return getValue().toString();
    }
}
