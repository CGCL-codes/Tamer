package org.avm.sure.vaadin.validator;

public class EmailValidator extends RegexpValidator {

    private static final long serialVersionUID = -7627061782964746654L;

    public EmailValidator(String errorMessage) {
        super("^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$", errorMessage);
    }
}
