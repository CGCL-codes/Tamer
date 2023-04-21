package freets.tools;

import java.text.*;
import java.io.*;
import java.util.*;

/**
 * A comfortable class for displaying error messages on basis of the error
 * class. The class uses an object of the class MessageFormat and delivers 
 * five arguments, which can be used by the localized message.
 *
 * {0}: exception or error message
 * {1}: class neame of exception or error
 * {2}: guess, in which file the exception was thrown
 * {3}: linenumber in the file
 * {4}: current date and time
 *
 * The messages are in the ResourceBundle named "Errors". The class name of
 * the exception object is used as resourcename. If no resource for an
 * exception class is found, the super class is search.
 *
 * @version $id$
 * @author  W. Sauter
 */
public class LocalizedError {

    public static final String ERROR_BUNDLE = "freets/properties/Errors";

    protected static final char COLON = ':';

    protected static final char OPEN_BRACE = '(';

    protected static final char CLOSE_BRACE = ')';

    public static void display(Throwable error) {
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle(ERROR_BUNDLE);
        } catch (MissingResourceException e) {
            error.printStackTrace(System.err);
            return;
        }
        String message = null;
        Class c = error.getClass();
        while ((message == null) && (c != Object.class)) {
            try {
                message = bundle.getString(c.getName());
            } catch (MissingResourceException e) {
                c = c.getSuperclass();
            }
        }
        if (message == null) {
            error.printStackTrace(System.err);
            return;
        }
        String filename = "";
        int linenum = 0;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter out = new PrintWriter(sw);
            error.printStackTrace(out);
            String trace = sw.toString();
            int pos = trace.indexOf(COLON);
            if (error.getMessage() != null) pos = trace.indexOf(COLON, pos + 1);
            int pos2 = trace.indexOf(CLOSE_BRACE, pos);
            linenum = Integer.parseInt(trace.substring(pos + 1, pos2));
            pos2 = trace.lastIndexOf(OPEN_BRACE, pos);
            filename = trace.substring(pos2 + 1, pos);
        } catch (Exception e) {
            ;
        }
        String errmsg = error.getMessage();
        Object[] args = { ((errmsg != null) ? errmsg : ""), error.getClass().getName(), filename, new Integer(linenum), new Date() };
        System.out.println(MessageFormat.format(message, args));
    }
}
