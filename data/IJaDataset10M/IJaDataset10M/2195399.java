package uk.org.beton.ftpsync.model;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Holds a message with an indication of how severe it is.
 *
 * @author Rick Beton
 * @version $Id: Message.java 670 2007-03-27 19:36:19Z rick $
 */
public final class TextMessage extends Message {

    private TextMessage(int level, String text) {
        super(level, text, null);
    }

    public static Message createFatal(String text) {
        return new TextMessage(FATAL, text);
    }

    public static Message createError(String text) {
        return new TextMessage(ERROR, text);
    }

    public static Message createInfo(String text) {
        return new TextMessage(INFO, text);
    }

    public static Message createVerbose(String text) {
        return new TextMessage(VERBOSE, text);
    }

    public static Message createDebug(String text) {
        return new TextMessage(DEBUG, text);
    }

    public TextMessage(Exception e, String description) {
        super(ERROR, processException(e, description), e);
    }

    private static String processException(Exception e, String description) {
        final StringWriter name = new StringWriter();
        e.printStackTrace(new PrintWriter(name));
        if (description == null) {
            return name.toString();
        }
        final StringBuilder b = new StringBuilder(name.toString());
        while (b.charAt(b.length() - 1) == '\n' || b.charAt(b.length() - 1) == '\r') {
            b.setLength(b.length() - 1);
        }
        b.append(": ");
        b.append(description);
        return b.toString();
    }
}
