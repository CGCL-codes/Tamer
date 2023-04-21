package ontorama.webkbtools.util;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class ParserException extends Exception {

    private String errorMsg = null;

    public ParserException(String message) {
        errorMsg = "\nParser failed:\n";
        errorMsg = errorMsg + message;
    }

    public String getMessage() {
        return errorMsg;
    }
}
