package org.wam.core;

import org.wam.core.WamElement.Stage;

/**
 * Represents an error, warning, or information message attached to a WAM element
 */
public class WamMessage {

    /**
	 * The types of element messages available
	 */
    public static enum Type {

        /**
		 * Represents an information message
		 */
        INFO, /**
		 * Represents a warning message
		 */
        WARNING, /**
		 * Represents an error message
		 */
        ERROR, /**
		 * Represents a fatal error which effectively disables an element or document
		 */
        FATAL
    }

    /**
	 * Defines a center that can store WAM messages
	 */
    public interface WamMessageCenter {

        /**
		 * Records a message in this message center
		 * 
		 * @param type The type of the message
		 * @param text The text of the message
		 * @param exception The exception which may have caused the message
		 * @param params Any parameters relevant to the message
		 */
        void message(Type type, String text, Throwable exception, Object... params);

        /**
		 * Records an fatal error in this message center. A fatal error disables this item so that
		 * it will not function. Short-hand for
		 * {@link #message(WamMessage.Type, String, Throwable, Object...)}
		 * 
		 * @param text A description of the error
		 * @param exception The exception that may have caused the error
		 * @param params Any parameters that may be relevant to the error
		 */
        void fatal(String text, Throwable exception, Object... params);

        /**
		 * Records an error in this message center. Short-hand for
		 * {@link #message(WamMessage.Type, String, Throwable, Object...)}
		 * 
		 * @param text A description of the error
		 * @param exception The exception that may have caused the error
		 * @param params Any parameters that may be relevant to the error
		 */
        void error(String text, Throwable exception, Object... params);

        /**
		 * Records a warning in this message center. Short-hand for
		 * {@link #message(WamMessage.Type, String, Throwable, Object...)}
		 * 
		 * @param text A description of the warning
		 * @param params Any parameters that may be relevant to the warning
		 */
        void warn(String text, Object... params);

        /**
		 * @return The worst type of message associated with this message center
		 */
        Type getWorstMessageType();

        /**
		 * @return All messages attached to this message center
		 */
        WamMessage[] getAllMessages();
    }

    /**
	 * The WAM document that the message is for
	 */
    public final WamDocument document;

    /**
	 * The WAM element that the message is for
	 */
    public final WamElement element;

    /**
	 * The type of this message
	 */
    public final Type type;

    /**
	 * The stage of processing at which this message occurred
	 */
    public final Stage stage;

    /**
	 * The text of the message, describing the problem
	 */
    public final String text;

    /**
	 * The exception that may have caused this message
	 */
    public final Throwable exception;

    private java.util.Map<String, Object> theParams;

    private WamMessage(WamDocument doc, WamElement anElement, Type aType, Stage aStage, String aText, Throwable anException, Object... params) {
        if (doc == null && anElement == null) throw new NullPointerException("A WAM message cannot be created without a document or" + " an element");
        if (aType == null) throw new NullPointerException("A WAM message cannot be created without a type");
        if (aStage == null) throw new NullPointerException("A WAM message cannot be created without a creation stage");
        if (aText == null) throw new NullPointerException("A WAM message cannnot be created without a description");
        document = doc;
        element = anElement;
        type = aType;
        stage = aStage;
        text = aText;
        exception = anException;
        if (params != null && params.length > 0) {
            theParams = new java.util.HashMap<String, Object>();
            if (params.length % 2 != 0) throw new IllegalArgumentException("message params must be in format" + " [name, value, name, value, ...]--odd argument count not allowed");
            theParams = new java.util.HashMap<String, Object>();
            for (int d = 0; d < params.length; d += 2) {
                if (!(params[d] instanceof String)) throw new IllegalArgumentException("message params must be in format" + " [name, value, name, value, ...]--even indices must be strings");
                theParams.put((String) params[d], params[d + 1]);
            }
        }
    }

    WamMessage(WamDocument doc, Type aType, Stage aStage, String aText, Throwable anException, Object... params) {
        this(doc, null, aType, aStage, aText, anException, params);
    }

    WamMessage(WamElement anElement, Type aType, Stage aStage, String aText, Throwable anException, Object... params) {
        this(null, anElement, aType, aStage, aText, anException, params);
    }

    /**
	 * @param param The name of the parameter to get
	 * @return The value of the given parameter
	 */
    public Object getParam(String param) {
        if (theParams == null) return null;
        return theParams.get(param);
    }

    /**
	 * @return All parameter names with values in this message
	 */
    public String[] getParams() {
        if (theParams == null) return new String[0];
        return theParams.keySet().toArray(new String[theParams.size()]);
    }

    @Override
    public final boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    /**
	 * Prints out a representation of the path to the problem element and the description of the
	 * error
	 */
    @Override
    public String toString() {
        WamElement el = element;
        String ret = "";
        while (el != null) ret = (el.getNamespace() != null ? el.getNamespace() + ":" : "") + el.getTagName() + "/" + ret;
        return ret + ": " + text;
    }

    /**
	 * @return A full description of the message, including the path to the problem element, the
	 *         description of the error, any parameters in the message, and the stack trace of the
	 *         exception that caused the problem
	 */
    public String toFullString() {
        String ret = toString();
        if (theParams != null) {
            for (java.util.Map.Entry<String, Object> param : theParams.entrySet()) ret += "\n" + param.getKey() + "=" + param.getValue();
        }
        if (exception != null) ret += "\n" + exception;
        return ret;
    }
}
