package org.openlogbooks.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Peter Neil
 * 
 */
public class ValidateableSimpleDateFormat extends SimpleDateFormat {

    static final long serialVersionUID = 2L;

    private String validationRegEx;

    public ValidateableSimpleDateFormat(String pattern, String validationRegEx, boolean validateNow) {
        super(pattern);
        this.setValidationRegEx(validationRegEx);
        if (validateNow && !this.isValid()) {
            throw new RuntimeException("The provided date pattern " + this.toPattern() + " does not validate against the provided validationg regular expression " + this.getValidationRegEx());
        }
    }

    public ValidateableSimpleDateFormat(String pattern, String validationRegEx) {
        this(pattern, validationRegEx, false);
    }

    public String getValidationRegEx() {
        return validationRegEx;
    }

    public void setValidationRegEx(String validationRegEx) {
        this.validationRegEx = validationRegEx;
    }

    public boolean isValid() {
        Calendar date = Calendar.getInstance();
        Pattern validDate = Pattern.compile(this.getValidationRegEx());
        Matcher matcher = validDate.matcher(this.format(date.getTime()));
        return matcher.matches();
    }
}
