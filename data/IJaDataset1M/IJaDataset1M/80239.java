package com.thesett.common.webapp.actions;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.StringTokenizer;
import java.util.logging.Level;
import com.thesett.common.error.UserReadableError;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * ErrorHandler is a top-level error handler for struts based web applications. It defines a single static method for
 * handling exceptions, logging them as errors and translating them into Struts ActionErrors. This is defined here
 * rather than in {@link BaseAction} because the error handling code may also be called directly from a JSP page and not
 * just from Struts actions.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Log all top-level exceptions as errors.
 * <tr><td> Translate exceptions into errors.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public class ErrorHandler {

    /** Used for logging. */
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(ErrorHandler.class.getName());

    /**
     * Converts an exception into struts action errors. The exception stack trace is stored under the 'exception'
     * message key. The message resource 'error.internalerror' is stored under the message key 'generalerror'. The stack
     * trace is pretty printed in HTML.
     *
     * @param exception The exception to be converted into struts action errors.
     * @param errors    The struts action errors object into which the action errors should be placed.
     *
     * @todo  This method can be modified to check if the exception is a user readable exception and to insert the user
     *        readable message under the 'generalerror' message key. Currently it does not handle user readable errors
     *        like this.
     */
    public static void handleErrors(Throwable exception, ActionErrors errors) {
        log.log(Level.SEVERE, exception.getMessage(), exception);
        if (exception.getCause() == null) {
            log.fine("Exception.getCause() is null");
        }
        if ((exception instanceof WrappedStrutsServletException) && (exception.getCause() != null)) {
            exception = exception.getCause();
            log.fine("Unwrapped WrappedStrutsServletException");
        }
        Writer stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(new HTMLFilter(stackTrace)));
        errors.add("exception", new ActionError("error.general", stackTrace));
        if (exception instanceof UserReadableError) {
            UserReadableError userError = (UserReadableError) exception;
            if (userError.isUserReadable()) {
                if (userError.getUserMessageKey() != null) {
                    errors.add("generalerror", new ActionError(userError.getUserMessageKey(), userError.getUserMessageKey()));
                } else {
                    errors.add("generalerror", new ActionError("error.general", userError.getUserMessage()));
                }
                return;
            }
        }
        errors.add("generalerror", new ActionError("error.internalerror"));
    }
}

/**
 * Filter writer that converts from text to HTML. This filter replaces newline characters '\n' with html line breaks.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Filter new lines into HTML line breaks.
 * </table></pre>
 *
 * @author Rupert Smith
 * @todo   Add more HTML filtering to this.
 */
class HTMLFilter extends FilterWriter {

    /**
     * Creates a new HTMLFilter object.
     *
     * @param out The writer to filter output to.
     */
    public HTMLFilter(Writer out) {
        super(out);
    }

    /**
     * Writed a single character to the filtered writer. No filtering is done for this method.
     *
     * @param  c The character to write.
     *
     * @throws IOException If the writer won't accept the character.
     */
    public void write(int c) throws IOException {
        out.write(c);
    }

    /**
     * Writes an array of characters to the filtered writer. No filtering is done for this method.
     *
     * @param  cbuf The character array to write.
     * @param  off  The offset into the array to begin writing from.
     * @param  len  The number of characters to write.
     *
     * @throws IOException If the writer won't accept the character.
     */
    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }

    /**
     * Writes a string of characters to the filtered writer. Any newline characters '\n' are replaced with an HTML break
     * tag "&lt;br&gt;".
     *
     * @param  str The character array to write.
     * @param  off The offset into the array to begin writing from.
     * @param  len The number of characters to write.
     *
     * @throws IOException If the writer won't accept the string.
     */
    public void write(String str, int off, int len) throws IOException {
        String inputString = str.substring(off, off + len);
        StringBuffer outputString = new StringBuffer();
        for (StringTokenizer tokenizer = new StringTokenizer(inputString, "\n", true); tokenizer.hasMoreTokens(); ) {
            String nextToken = tokenizer.nextToken();
            if ("\n".equals(nextToken)) {
                outputString.append("<br>");
            } else {
                outputString.append(nextToken);
            }
        }
        out.write(outputString.toString());
    }
}
