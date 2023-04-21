package com.cyberoblivion.modsynch.common;

/**
 * Error which is thrown from inside any part of the application to
 * signal that things have not gone well.  It is expected that details
 * of the reason for the error will have been logged in the global
 * log.  If the log information suffices, it would be OK to generate an
 * error with no message and no throwable, but the category is
 * required.</p>

 * <P>The compiler does not insist that methods declare errors which
 * they throw.  In general, errors are conditions which an application
 * cannot correct.</p>

 * <P>Application segments which complete successfully do not generate
 * errors.  Methods which return application error events on errors
 * return <CODE>null</code> on success.

 * @author wat
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */
public class ApplicationError extends Error {

    /**
   * The compiler desires a version ID for serializable classes.  */
    static final long serialVersionUID = 0;

    /**
   * Empty string for use in cases where there is really no message
   * which can enhance the data contained in the log and category
   * information.*/
    public static final String ES = "";

    /**
   * Bug such as motion fault.  These are flags which are to
   * be set in a mask.  */
    public static final short CAT_PLC = 6;

    /**
   * Record the error category.  This may be broken down rather finely
   * in the future.  */
    short category;

    /** Obligatory constructor.*/
    public ApplicationError() {
    }

    /**
   * Error with a message.  This constructor should not normally be
   * used, a category is required.  This may be used as a temporary
   * placeholder if the category is not known, but in this case, the
   * code must be flagged with a commment indicating that there is a
   * change to do.
   * @param msg text to be displayed in the message.
   */
    public ApplicationError(String msg) {
        super(msg);
    }

    /**
   * Error with a message and throwable.  This constructor should not
   * normally be used, a category is required.  This may be used as a
   * temporary placeholder if the category is not known, but in this
   * case, the code must be flagged with a commment indicating that
   * there is a change to do.
   * @param msg text to be displayed in the message.
   * @param t throwable which explains the cause of the error
   */
    public ApplicationError(String msg, Throwable t) {
        super(msg, t);
    }

    /**
   * Error with a category and throwable
   * @param cat error category
   * @param t throwable which explains the cause of the error
   */
    public ApplicationError(short cat, Throwable t) {
        super(ES, t);
        this.category = cat;
    }

    /**
   * Error with a message and a category
   * @param cat error category
   * @param msg 
   */
    public ApplicationError(short cat, String msg) {
        super(msg);
        this.category = cat;
    }

    /**
   * Error with a message, a category, and a throwable
   * @param cat 
   * @param msg text to be displayed in the message.
 * @param t throwable which explains the cause of the error
   */
    public ApplicationError(short cat, String msg, Throwable t) {
        super(msg, t);
        this.category = cat;
    }
}
