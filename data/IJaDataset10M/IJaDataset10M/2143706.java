package net.sf.opendf.util.source;

import java.util.List;

/**
 * A subclass of MultiErrorException specifically used by the parsers.
 */
public class ParserErrorException extends MultiErrorException {

    public ParserErrorException(String message, List<GenericError> errors) {
        super(message, errors);
    }
}
