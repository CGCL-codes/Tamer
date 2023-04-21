package com.greatlogic.bigquery.db;

import static com.greatlogic.bigquery.db.GAEDBEnums.*;

public class GAEDBException extends Exception {

    private static final long serialVersionUID = 0x4a53494442457863L;

    private Throwable _originalThrowable;

    static void validateParameterIsNotNull(final String parName, final Object parValue) throws GAEDBException {
        if (parValue == null) {
            throw new GAEDBException(EGAEDBException.ParameterIsNull, parName);
        }
    }

    public GAEDBException(final EGAEDBException dbException) {
        this(dbException, null, null);
    }

    public GAEDBException(final EGAEDBException dbException, final CharSequence message) {
        this(dbException, message, null);
    }

    public GAEDBException(final EGAEDBException dbException, final Throwable originalThrowable) {
        this(dbException, null, originalThrowable);
    }

    public GAEDBException(final EGAEDBException dbException, final CharSequence message, final Throwable originalThrowable) {
        super(dbException == null ? message.toString() : dbException.toString() + (GAEUtil.isEmpty(message) ? "" : ":" + message) + (originalThrowable == null ? "" : " - " + originalThrowable.getMessage()), originalThrowable);
        _originalThrowable = originalThrowable == null ? this : originalThrowable;
    }

    /**
 * Returns the original exception that caused the exception. For example, if a SQL exception was the
 * original cause of this exception then the throwable will be that exception.
 * @return The original <code>Throwable</code> that caused this exception.
 */
    public Throwable getOriginalThrowable() {
        return _originalThrowable;
    }
}
