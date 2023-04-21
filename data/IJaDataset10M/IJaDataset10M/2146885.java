package org.skycastle.util.issue;

/**
 * Used to store some reported problem, warning or note, that should be shown to the user or logged. Similar to compiler
 * warning and error messages.
 *
 * @author Hans Haggstrom
 */
public interface Issue {

    /**
     * @return more in depth description of what the issue is and where it is.
     */
    String getSummary();

    /**
     * @return the Severity of the issue.
     */
    Severity getSeverity();

    /**
     * @return type of report.  Same string for all reports of this type (enables grouping of them).
     */
    String getType();

    /**
     * @return the exception that caused the problem, if available.
     */
    Throwable getCause();
}
