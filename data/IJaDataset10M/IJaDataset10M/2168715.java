package org.simpleframework.http.session;

import org.simpleframework.util.FormatException;

/**
 * The <code>SessionException</code> is used to indicate that some
 * operation failed when trying to access a session or when trying
 * to perform some operation on an existing session. Typically this
 * is thrown when a session no longer exists.
 *
 * @author Niall Gallagher
 */
public class SessionException extends FormatException {

    /**
    * This constructor is used if there is a description of the 
    * event that caused the exception required. This can be given
    * a message used to describe the situation for the exception.
    * 
    * @param template this is a description of the exception
    * @param list this is the list of arguments that can be used
    */
    public SessionException(String template, Object... list) {
        super(template, list);
    }
}
