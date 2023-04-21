package mykeynote.exceptions.keynote;

import mykeynote.server.Report;

public class KeyNotFoundException extends Exception {

    private static final long serialVersionUID = 8614894846148459654L;

    public KeyNotFoundException(String message, Report report, String unique) {
        super(message);
        report.reportErrorLog(unique, message);
    }
}
