package net.sf.springlayout.web.validator;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

/**
 * Provides type validation for decimals with the ability to specify a maxuimum number of decimal places
 *
 * @author Rob Monie
 *
 */
public class DecimalValidator extends AbstractValidationRule {

    private int maxDecimalPlaces;

    public DecimalValidator() {
    }

    /**
    * @return the maxDecimalPlaces
    */
    public int getMaxDecimalPlaces() {
        return maxDecimalPlaces;
    }

    /**
    * Specify the max number of decimal places to test for.  Default is 0
    * @param maxDecimalPlaces the maxDecimalPlaces to set
    */
    public void setMaxDecimalPlaces(int maxDecimalPlaces) {
        this.maxDecimalPlaces = maxDecimalPlaces;
    }

    /**
    * This method is not implemented as valdiating of types is taken care of
    * through the binding process. If the value of this field is not of type
    * Integer then it will never get here.
    *
    * @see net.sf.springlayout.web.validator.ValidationRule#validate(javax.servlet.http.HttpServletRequest,
    *      java.lang.Object, org.springframework.validation.Errors,
    *      java.lang.String)
    */
    public void validate(HttpServletRequest request, Object obj, Errors errors, String fieldKey) {
        if (!isValid(obj, fieldKey)) {
            errors.rejectValue(fieldKey, "errors.tooManyDecimalPlaces");
        }
    }

    protected boolean isValidInternal(Object obj, String fieldKey) {
        Object fieldValue = this.getFieldValue(obj, fieldKey);
        if (fieldValue == null || "".equals(fieldValue.toString())) return true;
        if (!(fieldValue instanceof Double)) {
            try {
                new Double(fieldValue.toString());
            } catch (NumberFormatException e) {
                return false;
            }
        }
        String decimalString = StringUtils.substringAfter(fieldValue.toString(), ".");
        if (decimalString.length() > this.maxDecimalPlaces) {
            return false;
        } else return true;
    }

    public String getJavascript() {
        StringBuffer script = new StringBuffer();
        script.append("LAYOUT.validateDecimal(field," + this.maxDecimalPlaces + ",");
        script.append("alerts)");
        return script.toString();
    }
}
