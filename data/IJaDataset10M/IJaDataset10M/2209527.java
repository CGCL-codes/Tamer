package au.edu.monash.merc.capture.struts2.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class PasswordIntegrityValidator extends FieldValidatorSupport {

    static Pattern digitPattern = Pattern.compile("[0-9]");

    static Pattern letterPattern = Pattern.compile("[a-zA-Z]");

    static Pattern specialCharsDefaultPattern = Pattern.compile("!@#$");

    private String specialCharacters;

    public String getSpecialCharacters() {
        return specialCharacters;
    }

    public void setSpecialCharacters(String specialCharacters) {
        this.specialCharacters = specialCharacters;
    }

    @Override
    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        Object value = this.getFieldValue(fieldName, object);
        if (!(value instanceof String)) {
            addFieldError(fieldName, object);
        } else {
            String fieldValue = (String) value;
            fieldValue = fieldValue.trim();
            if (fieldValue.length() == 0) {
                addFieldError(fieldName, object);
            } else {
                Matcher digitMatcher = digitPattern.matcher(fieldValue);
                Matcher letterMatcher = letterPattern.matcher(fieldValue);
                Matcher specialCharacterMatcher = null;
                if (getSpecialCharacters() != null) {
                    Pattern speciallPattern = Pattern.compile("[" + getSpecialCharacters() + "]");
                    specialCharacterMatcher = speciallPattern.matcher(fieldValue);
                } else {
                    specialCharacterMatcher = null;
                }
                if (!digitMatcher.find()) {
                    addFieldError(fieldName, object);
                } else if (!letterMatcher.find()) {
                    addFieldError(fieldName, object);
                } else if (specialCharacterMatcher != null && !specialCharacterMatcher.find()) {
                    addFieldError(fieldName, object);
                }
            }
        }
    }
}
