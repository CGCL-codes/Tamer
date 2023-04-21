package it.dangelo.saj.impl.validation.json_vl.parser;

import it.dangelo.saj.SAJException;

@SuppressWarnings("serial")
public class ValidatorCreationException extends SAJException {

    public ValidatorCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorCreationException(String message) {
        super(message);
    }
}
