package ptolemy.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
This is a class that is used to report errors.  It provides a
set of static methods that are called to report errors.  However, the
actual reporting of the errors is deferred to an instance of this class
that is set using the setMessageHandler() method.  Normally there
is only one instance, set up by the application, so the class is
a singleton.  But this is not enforced.
<p>
This base class simply writes the errors to System.err.
When an applet or application starts up, it may wish to set a subclass
of this class as the message handler, to allow a nicer way of
reporting errors.  For example, a swing application will probably
want to report errors in a dialog box, using for example
the derived class GraphicalMessageHandler.
@see GraphicalMessageHandler

@author  Edward A. Lee, Steve Neuendorffer
@version $Id: MessageHandler.java,v 1.1.1.1 2004/01/26 21:52:02 hyuklim Exp $
*/
public class MessageHandler {

    /** Defer to the set message handler to show the specified
     *  error message.
     *  @param info The message.
     */
    public static void error(String info) {
        _handler._error(info);
    }

    /** Defer to the set message handler to
     *  show the specified message and exception information.
     *  If the exception is an instance of CancelException, then it
     *  is not shown.  By default, only the message of the exception
     *  is thrown.  The stack trace information is only shown if the
     *  user clicks on the "Display Stack Trace" button.
     *
     *  @param info The message.
     *  @param exception The exception.
     *  @see CancelException
     */
    public static void error(String info, Exception exception) {
        _handler._error(info, exception);
    }

    /** Return the message handler instance that is used by the static
     *  methods in this class.
     */
    public static MessageHandler getMessageHandler() {
        return _handler;
    }

    /** Defer to the set message handler to show the specified
     *  message.
     *  @param info The message.
     */
    public static void message(String info) {
        _handler._message(info);
    }

    /** Set the message handler instance that is used by the static
     *  methods in this class.  If the given handler is null, then
     *  do nothing.
     *  @param handler The message handler.
     */
    public static void setMessageHandler(MessageHandler handler) {
        if (handler != null) {
            _handler = handler;
        }
    }

    /** Defer to the set message handler to
     *  show the specified message in a modal dialog.  If the user
     *  clicks on the "Cancel" button, then throw an exception.
     *  This gives the user the option of not continuing the
     *  execution, something that is particularly useful if continuing
     *  execution will result in repeated warnings.
     *  @param info The message.
     *  @exception CancelException If the user clicks on the "Cancel" button.
     */
    public static void warning(String info) throws CancelException {
        _handler._warning(info);
    }

    /** Show the specified message and exception information
     *  in a modal dialog.  If the user
     *  clicks on the "Cancel" button, then throw an exception.
     *  This gives the user the option of not continuing the
     *  execution, something that is particularly useful if continuing
     *  execution will result in repeated warnings.
     *  By default, only the message of the exception
     *  is thrown.  The stack trace information is only shown if the
     *  user clicks on the "Display Stack Trace" button.
     *  @param info The message.
     *  @exception CancelException If the user clicks on the "Cancel" button.
     */
    public static void warning(String info, Exception exception) throws CancelException {
        _handler._warning(info + ": " + exception.getMessage(), exception);
    }

    /** Show the specified error message.
     *  @param info The message.
     */
    protected void _error(String info) {
        System.err.println(info);
    }

    /** Show the specified message and exception information.
     *  If the exception is an instance of CancelException, then it
     *  is not shown.  By default, only the message of the exception
     *  is thrown.  The stack trace information is only shown if the
     *  user clicks on the "Display Stack Trace" button.
     *
     *  @param info The message.
     *  @param exception The exception.
     *  @see CancelException
     */
    protected void _error(String info, Exception exception) {
        if (exception instanceof CancelException) return;
        System.err.println(info);
        exception.printStackTrace();
    }

    /** Show the specified message.
     *  @param info The message.
     */
    protected void _message(String info) {
        System.err.println(info);
    }

    /** Show the specified message in a modal dialog.  If the user
     *  clicks on the "Cancel" button, then throw an exception.
     *  This gives the user the option of not continuing the
     *  execution, something that is particularly useful if continuing
     *  execution will result in repeated warnings.
     *  @param info The message.
     *  @exception CancelException If the user clicks on the "Cancel" button.
     */
    protected void _warning(String info) throws CancelException {
        _error(info);
    }

    /** Show the specified message and exception information
     *  in a modal dialog.  If the user
     *  clicks on the "Cancel" button, then throw an exception.
     *  This gives the user the option of not continuing the
     *  execution, something that is particularly useful if continuing
     *  execution will result in repeated warnings.
     *  By default, only the message of the exception
     *  is thrown.  The stack trace information is only shown if the
     *  user clicks on the "Display Stack Trace" button.
     *  @param info The message.
     *  @exception CancelException If the user clicks on the "Cancel" button.
     */
    protected void _warning(String info, Exception exception) throws CancelException {
        _error(info, exception);
    }

    private static MessageHandler _handler = new MessageHandler();
}
