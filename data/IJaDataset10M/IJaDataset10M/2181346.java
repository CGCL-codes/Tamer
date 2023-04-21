package logahawk.listeners;

import java.util.*;
import logahawk.*;
import logahawk.formatters.*;

/**
 * This is a base class which implements the "IListener" and provides some additional functionality. This class makes
 * all of it's functionality available as static methods to those that cannot inherit directly from this class and must
 * use the interface.
 */
public abstract class BaseListener implements Listener {

    /**
	 * This will perform final formatting of the message, prepending the date and severity to the string message, and
	 * appending the formatted data string.
	 */
    public static String format(Severity severity, Date epoch, String message) {
        StringBuffer severityText = new StringBuffer(severity.toString());
        while (severityText.length() < 5) severityText.append(' ');
        StringBuffer sb = new StringBuffer();
        sb.append(DateArgFormatter.dateFormatter.format(epoch));
        sb.append(" ");
        sb.append(severityText.toString());
        sb.append(": ");
        sb.append(message == null || "".equals(message) ? "(Null)" : message);
        return sb.toString();
    }
}
