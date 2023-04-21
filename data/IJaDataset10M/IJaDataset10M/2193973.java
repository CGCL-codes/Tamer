package org.dbwiki.exception.data;

import org.dbwiki.exception.WikiException;

public class WikiNodeException extends WikiException {

    public static final long serialVersionUID = 8814L;

    public static final int InvalidIdentifierFormat = 1000;

    private int _errorCode;

    public WikiNodeException(int errorCode, String message) {
        super(message);
        _errorCode = errorCode;
    }

    public WikiNodeException(Exception exception) {
        super(exception);
        _errorCode = -1;
    }

    public String errorCodeMessage() {
        switch(_errorCode) {
            case InvalidIdentifierFormat:
                return " [Invalid node identifier format]";
            default:
                return "";
        }
    }

    public String exceptionPrefix() {
        return "[NODE]";
    }
}
