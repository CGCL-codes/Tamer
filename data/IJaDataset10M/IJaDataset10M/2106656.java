package fastforward.wicket.validator;

import java.util.Map;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public class DecimalValidator extends AbstractValidator {

    private int maxDecimals;

    public DecimalValidator(int maxDecimals) {
        this.maxDecimals = maxDecimals;
    }

    @Override
    protected void onValidate(IValidatable validatable) {
        Number value = (Number) validatable.getValue();
        if (countDecimal(value.toString(), true) > maxDecimals) {
            error(validatable);
        }
    }

    /**
     * @see AbstractValidator#variablesMap(IValidatable)
     */
    protected Map variablesMap(IValidatable validatable) {
        final Map map = super.variablesMap(validatable);
        map.put("maxDecimals", maxDecimals);
        return map;
    }

    /**
     * @see AbstractValidator#resourceKey(FormComponent)
     */
    protected String resourceKey() {
        return "DecimalValidator.tooManyDecimals";
    }

    private static int countDecimal(String s, boolean ignoreTailingZeros) {
        if (ignoreTailingZeros) s = removeTailingZeros(s);
        int dotIndex = s.indexOf(".");
        if (dotIndex == -1) {
            return 0;
        } else {
            if (s.length() - 1 == dotIndex) return 0; else return s.substring(dotIndex + 1).length();
        }
    }

    private static String removeTailingZeros(String s) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '0') sb.deleteCharAt(sb.length() - 1); else break;
        }
        return sb.toString();
    }
}
