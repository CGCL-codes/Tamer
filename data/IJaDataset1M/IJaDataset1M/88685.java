package org.formaria.aria.exception;

import org.formaria.aria.Project;

/**
 * <p>An interface defining the method that will be called when a validation/service
 * or other area of functionality
 * fails. The interface can be implemented by any class and in this way error
 * reporting or logging can be redirected to a variety of destinations.</p>
 * <p>Copyright: Copyright (c) Formaria Ltd., 1998-2005<br>
 * License:      see license.txt</p>
 * $Revision: 1.2 $
 */
public interface ExceptionHandler {

    /**
   * A method called when an exeption has been trapped.
   *
   * @param c Component being validated
   * @param ex The exception caused
   * @param checker The object being used to check validity and throw exceptions.
   * @return true to continue with error checking or false to suppress further
   * checking.
   */
    public boolean handleException(Object c, Exception ex, Object checker);

    /**
   * informs the handler when page checking is starting or stopping. Typically
   * when it starts the page will begin to accumulate message which are to be displayed.
   * When the parameter is false the page will usually display the accumulated
   * messages
   * @param accumulate boolean to indicate whether the accumulation is started or stopped.
   * @param level int which indicates the most serious level of error encountered
   * @return the new level which might be set to zero if a confirm dialog is displayed
   */
    public int accumulateMessages(boolean accumulate, int level);

    /**
   * Handle an exception during the invocation of a page's event handler. The page
   * normally implements this interface and has the first chance at handling the
   * error. Thereafter if false is returned a central (optional) exception
   * handler owned by the project is invoked.
   *
   * @param project the current project
   * @param container the page
   * @param error the exception or error that was thrown
   * @return true to continue processing, false to stop processing
   */
    public boolean handleEventHandlerException(Project project, Object container, Throwable error);
}
