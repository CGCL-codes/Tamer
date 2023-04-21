package cc.sprite;

import java.lang.reflect.Method;

/**
 * The general exception class for this toolkit.  
 * 
 * This exception provides static methods to create any of the 
 * specific exceptions that can be thrown by this toolkit.
 * 
 * Note that the static exception factory methods do not throw
 * the exception they just create one, so the usage usually follows
 * this pattern:
 * 
 * <pre><code>
 *   throw WException.templateNotFound(templateClass);
 * </code></pre>
 * 
 * @author Joe Mayer
 */
public class WException extends Exception {

    /** The default serial version uid. */
    private static final long serialVersionUID = 1L;

    /**
   * A constructor called by the static exception methods.
   * @param msg  The exception message.
   * @param cause  The cause of the exception.
   */
    private WException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
   * A construct called by the static exception methods.
   * @param msg The exception message.
   */
    private WException(String msg) {
        super(msg);
    }

    /**
   * Thrown when a template for the specified form class cannot be found.
   * @param className  The class that no template can be found for.
   * @return A new exception.
   */
    public static WException templateNotFound(String className) {
        return new WException("Missing template for class '" + className + "'.");
    }

    /**
   * Thrown when a new element call loading a template fails.
   * @param cause The cause of the new element failure.
   * @return A new exception.
   */
    public static WException newElementFailed(Throwable cause) {
        return new WException("New element failed with\n " + cause.toString(), cause);
    }

    /**
   * Thrown when there is an IO error reading a template.
   * @param cause The cause of the read exception.
   * @return A new exception.
   */
    public static WException readException(Throwable cause) {
        return new WException("Reading template file failed with\n " + cause.toString(), cause);
    }

    /**
   * Thrown when an expected character is missing in a template file. 
   * @param missing  The missing characters
   * @param name  The template file name
   * @param line The line in the template where the char is missing from.
   * @return A new exception.
   */
    public static WException missingChars(String missing, String name, int line) {
        return new WException("Missing '" + missing + "' in '" + name + "' on line " + line + ".");
    }

    /**
   * Thrown when an unexpected character is found in a template file. 
   * @param ch  The unexpected character that was found.
   * @param name  The template file name.
   * @param line The line in the template.
   * @return A new exception.
   */
    public static WException unexpectedChar(char ch, String name, int line) {
        String code = String.valueOf((int) ch);
        if (ch < 32 || ch > 127) {
            ch = ' ';
        }
        return new WException("Unexpected character '" + ch + "' (" + code + ") in '" + name + "' on line " + line + ".");
    }

    /**
   * Thrown when an expected tag name is missing in a template file. 
   * @param name  The template file name.
   * @param line The line in the template.
   * @return A new exception.
   */
    public static WException missingTagName(String name, int line) {
        return new WException("Missing a tag name in '" + name + "' on line " + line + ".");
    }

    /**
   * Thrown when an expected property name is missing in a template file. 
   * @param name  The template file name.
   * @param line The line in the template where the char is missing from.
   * @return A new exception.
   */
    public static WException missingPropertyName(String name, int line) {
        return new WException("Missing a property name in '" + name + "' on line " + line + ".");
    }

    /**
   * Thrown when an expected property value is missing in a template file. 
   * @param name  The template file name.
   * @param line The line in the template.
   * @return A new exception.
   */
    public static WException missingPropertyValue(String name, int line) {
        return new WException("Missing a property value in '" + name + "' on line " + line + ".");
    }

    /**
   * Thrown when an a tag name cannot be found in any tag libraries. 
   * @param tag The name of the tag that cannot be found.
   * @param name  The template file name.
   * @param line The line in the template.
   * @return A new exception.
   */
    public static WException tagNotFound(String tag, String name, int line) {
        return new WException("Tag '" + tag + "' in '" + name + "' on line " + line + " is not declared in any tag library.");
    }

    /**
   * Thrown when a tag does not support a given property.
   * @param prop The property.
   * @param name  The template file name.
   * @param line The line in the template.
   * @return A new exception.
   */
    public static WException unsupportedProperty(String prop, String name, int line) {
        return new WException("Property '" + prop + "' is not supported by in '" + name + "' on line " + line + ".");
    }

    /**
   * Thrown when newSession method fails to create a session.
   * @param cause The cause of the failure, or null.
   * @return A new exception.
   */
    public static WException newSessionFailed(Throwable cause) {
        String msg = (cause == null) ? "no message." : cause.toString();
        return new WException("newSession method failed to create a session with\n " + msg, cause);
    }

    /**
   * Thrown when the sequence number on an httpRequest doesn't match the
   * frame counters internal sequence number.  This indicates that a
   * duplicate or lost request occured.  An exception is sent back to
   * the browser which then refreshes the page to re-sync.
   * @return A new exception.
   */
    public static WException requestOutOfSequence() {
        return new WException("HttpRequest recieved out of sequence.");
    }

    /**
   * Thrown when an event handler fails.
   * @param m The method that failed.
   * @param cause The exception it threw.
   * @return A new exception.
   */
    public static WException eventHandlerFailed(Method m, Throwable cause) {
        return new WException("Event handler " + m.getName() + " failed with\n " + cause.toString(), cause);
    }
}
