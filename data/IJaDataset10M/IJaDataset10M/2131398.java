package com.rapidminer.report;

/**
 * An exception which will be thrown if any error occurs during the generation of a report.
 * 
 * @author Sebastian Land, Ingo Mierswa
 */
public class ReportException extends Exception {

    private static final long serialVersionUID = -2411534346974518197L;

    private final String reason;

    public ReportException(Exception e, String reason) {
        if (e != null) {
            this.setStackTrace(e.getStackTrace());
        }
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
