package org.owasp.orizon.report;

/**
 * @author thesp0nge
 *
 */
public class CsvFormatter implements Formatter {

    public String footer(String s) {
        return s;
    }

    public String format(Reportable r) {
        return r.getSeverity() + "," + r.getLineNo() + "," + r.getErrorMessage() + "," + r.getScore() + "," + r.getSnippet() + "\n";
    }

    public String format(String s) {
        return s;
    }

    public String header(String s) {
        return "Severity,Line Number,Error Message, Score,Code Snippet\n";
    }

    public String horizontalLine() {
        return "";
    }

    public String newLine() {
        return "\n";
    }

    public String startListReportable() {
        return "";
    }

    public String stopListReportable() {
        return "";
    }
}
