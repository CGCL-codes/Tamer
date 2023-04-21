package org.t2framework.t2.testing.ut;

public class Validators {

    public static ValidatorMessageResource validate(Validator... validators) {
        return validate(new ValidatorMessageResource(), validators);
    }

    public static ValidatorMessageResource validate(ValidatorMessageResource resource, Validator... validators) {
        if (validators.length == 0) {
            return ValidatorMessageResource.EMPTY;
        }
        for (Validator validator : validators) {
            final boolean valid = validator.validate();
            if (valid) {
                continue;
            }
            resource.addErrrorMessages(validator.getErrorMessages());
            if (!validator.continueToNext()) {
                break;
            }
        }
        return resource;
    }
}
