package org.ztemplates.validation;

import org.ztemplates.message.ZMessages;
import org.ztemplates.property.ZProperty;

public class ZLengthValidator implements ZIValidator {

    private final int minlength;

    private final int maxlength;

    private final String message;

    private final ZProperty<String> prop;

    public ZLengthValidator(int minlength, int maxlength, String message, ZProperty<String> prop) {
        this.minlength = minlength;
        this.maxlength = maxlength;
        this.message = message;
        this.prop = prop;
    }

    public void validate(ZMessages messages) {
        if (prop.isEmpty()) {
            return;
        }
        String val = prop.getStringValue();
        if (val.length() > maxlength) {
            messages.addError(message, prop);
        } else if (val.length() < minlength) {
            messages.addError(message, prop);
        }
    }
}
