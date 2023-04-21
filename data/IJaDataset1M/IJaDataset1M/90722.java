package pedro.soa.validation;

import pedro.system.*;
import pedro.soa.alerts.SystemErrorAlert;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class StringMaskValidator extends AbstractEditFieldValidationService implements ConstraintDescription {

    /**
     * field values must match this regular expression mask
     */
    private Pattern pattern;

    public StringMaskValidator(String pattern) {
        setMask(pattern);
    }

    public String getConstraintsDescription() {
        return pattern.pattern();
    }

    /**
     * sets the mask that field values must match.
     *
     * @param pattern the field mask
     */
    public void setMask(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    private String getNoMatchError(String value) {
        String errorMessage = PedroResources.getMessage("validation.regexp.noMatchError", value, pattern.pattern(), getFieldName());
        return errorMessage;
    }

    private String getRegExpError(String mask) {
        String errorMessage = PedroResources.getMessage("validation.regexp.illegalRegExpError", mask, getFieldName());
        return errorMessage;
    }

    public ArrayList validate(PedroFormContext pedroFormContext, String value) {
        ArrayList alerts = new ArrayList();
        try {
            if (isEmpty(value) == true) {
                return alerts;
            }
            Matcher matcher = pattern.matcher(value);
            boolean matchesMask = matcher.matches();
            if (matchesMask == true) {
                return alerts;
            } else {
                SystemErrorAlert errorAlert = new SystemErrorAlert(getNoMatchError(value));
                alerts.add(errorAlert);
            }
        } catch (Exception err) {
            SystemErrorAlert errorAlert = new SystemErrorAlert(getRegExpError(pattern.pattern()));
            alerts.add(errorAlert);
        }
        return alerts;
    }
}
