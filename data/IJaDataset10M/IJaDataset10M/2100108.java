package org.javalite.activejdbc.validation;

public class EmailValidator extends RegexpValidator {

    public EmailValidator(String attribute) {
        super(attribute, "\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b");
        setMessage("email has bad format");
    }
}
