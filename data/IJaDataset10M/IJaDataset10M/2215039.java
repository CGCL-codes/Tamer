package org.tagbox.engine.parser;

import org.tagbox.engine.TagBoxException;
import org.tagbox.util.Log;

public class ParserException extends TagBoxException {

    public ParserException(String reason, String expr) {
        super(reason + ", in expression: " + expr);
    }

    public ParserException(String expr, Throwable cause) {
        super("error in expression: " + expr, cause);
    }
}
