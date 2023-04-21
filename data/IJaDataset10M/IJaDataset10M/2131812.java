package com.sun.mail.dsn;

/**
 * An abstract report type, to be included in a MultipartReport.
 * Subclasses define specific report types, such as DeliverStatus
 * and DispositionNotification.
 *
 * @since	JavaMail 1.4.2
 */
public abstract class Report {

    protected String type;

    /**
     * Construct a report of the indicated MIME subtype.
     * The primary MIME type is always "message".
     */
    protected Report(String type) {
        this.type = type;
    }

    /**
     * Get the MIME subtype of the report.
     * The primary MIME type is always "message".
     */
    public String getType() {
        return type;
    }
}
