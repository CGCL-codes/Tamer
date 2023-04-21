package br.com.napoleao.controlfin.util;

import java.util.Locale;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;

public class DecimalFormatter extends NumberFormatter {

    public DecimalFormatter(String editPattern, String displayPattern, Locale loc) {
        super(editPattern, displayPattern, loc);
    }

    @Override
    public Object getValue() {
        if (super.getValue() instanceof Integer) {
            return ((Integer) super.getValue()).doubleValue();
        } else if (super.getValue() instanceof Long) {
            return ((Long) super.getValue()).doubleValue();
        } else {
            return super.getValue();
        }
    }
}
