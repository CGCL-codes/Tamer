package org.judo.generate.mvc;

public class ParseException extends Exception {

    String parseError;

    public ParseException() {
    }

    public ParseException(String arg0) {
        super(arg0);
        parseError = arg0;
    }

    public String getParseError() {
        return parseError;
    }

    public ParseException(Throwable arg0) {
        super(arg0);
    }

    public ParseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
