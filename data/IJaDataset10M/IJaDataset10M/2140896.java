package br.com.linkcom.neo.bean.editors;

import java.beans.PropertyEditorSupport;
import org.apache.commons.lang.StringUtils;
import br.com.linkcom.neo.types.Cpf;

public class CpfPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.isEmpty(text)) {
            setValue(new Cpf(text));
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
