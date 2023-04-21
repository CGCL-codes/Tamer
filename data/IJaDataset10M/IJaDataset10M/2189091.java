package org.simfony.validate;

import org.simfony.Element;
import org.simfony.Form;
import org.simfony.Validator;

/**
 * FormValidator is used to validate forms.
 *
 * @author vilmantas_baranauskas@yahoo.com
 */
public abstract class FormValidator implements Validator {

    /**
    * Validates the element. This method invokes <code>{@link #validate(Form, boolean)}</code>
    * method if the element is instance of Form. Otherwise it does nothing.
    *
    * @param element Element to be validated.
    * @param valid <code>true</code> if previous validations in the
    *   specified context were successful.
    *
    * @return true if validation was successful.
    */
    public boolean validate(Element element, boolean valid) {
        if (element instanceof Form) {
            valid = validate((Form) element, valid);
        }
        return valid;
    }

    /**
    * Validates the form.
    *
    * @param form Form to be validated.
    * @param valid <code>true</code> if previous validations in the
    *   specified context were successful. E.g. if you have several
    *   forms, all previously validated forms returned <code>true</code>.
    *
    * @return true if validation was successful.
    */
    public abstract boolean validate(Form form, boolean valid);
}
