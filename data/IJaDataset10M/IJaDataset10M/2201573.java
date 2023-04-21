package cloudware.config;

/**
* Exception thrown to indicate that a
* configuration property is not defined. It is thrown exclusively by
* {@link Configuration}, since it is the only class that has access to the
* set of defined properties. 
 */
public class MissingParameterException extends RuntimeException {

    MissingParameterException(String name) {
        super("Parameter \"" + name + "\" not found.");
    }

    MissingParameterException(String name, String motivation) {
        super("Parameter \"" + name + "\" not found " + motivation);
    }

    /**
* Extends message with info from stack trace.
* It tries to guess what class called {@link Configuration} and
* adds relevant info from the stack trace about it to the message.
*/
    public String getMessage() {
        StackTraceElement[] stack = getStackTrace();
        int pos;
        for (pos = 0; pos < stack.length; pos++) {
            if (!stack[pos].getClassName().equals(Configuration.class.getName())) break;
        }
        return super.getMessage() + "\nAt " + getStackTrace()[pos].getClassName() + "." + getStackTrace()[pos].getMethodName() + ":" + getStackTrace()[pos].getLineNumber();
    }

    /**
 * Returns the exception message without stack trace information
 */
    public String getShortMessage() {
        return super.getMessage();
    }
}
