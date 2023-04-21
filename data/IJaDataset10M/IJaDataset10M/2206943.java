package br.com.linkcom.neo.bean.editors;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.commons.lang.StringUtils;
import br.com.linkcom.neo.types.Money;

public class MoneyPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.isEmpty(text)) {
            try {
                text = text.replace(".", "");
                text = text.replace(',', '.');
                setValue(new Money(text));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            setValue(null);
        }
    }

    @Override
    public String getAsText() {
        if (getValue() == null) return null;
        Money money = (Money) getValue();
        BigDecimal value = money.getValue();
        NumberFormat numberFormat = new DecimalFormat("#,##0.00");
        return numberFormat.format(value);
    }
}
