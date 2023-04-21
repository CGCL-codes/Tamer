package org.cantaloop.tools.validation;

public class Validators {

    public static final Validator ALWAYS_VALID_VALIDATOR = new Validator() {

        public boolean supportsValidationOf(Class type) {
            return true;
        }

        public Object validate(Object o) {
            return o;
        }
    };
}
