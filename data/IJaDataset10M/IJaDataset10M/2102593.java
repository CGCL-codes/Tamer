package org.sink.swing;

import javax.swing.*;
import org.sink.exceptions.SinkException;

/**
 * Error handling class. Formats and displays the Error in a JOptionPane
 */
public class ErrorController {

    private boolean format;

    private String msg;

    private SinkException sinkEx;

    private Exception ex;

    /**
	 * A constructor for a SinkException that formats (or not) and displays the error / exception.
	 *  
	 * @param e the exception that holds the error message
	 * @param format says if the message should be formated or not.
	 */
    public ErrorController(SinkException e, boolean format) {
        this.format = format;
        this.sinkEx = e;
        this.msg = sinkEx.getMessage();
        displayError(sinkEx.getSeverity());
    }

    /**
	 * A constructor for a SinkException that formats and displays the error / exception.
	 *  
	 * @param e the exception that holds the error message
	 */
    public ErrorController(SinkException e) {
        this(e, true);
    }

    /**
	 * A constructor for an Exception that formats and displays the error / exception with severity "error".
	 *  
	 * @param e the exception that holds the error message
	 */
    public ErrorController(Exception e) {
        this(e, true);
    }

    /**
	 * A constructor for an Exception that formats (or not) and displays the error / exception with severity "error".
	 *  
	 * @param e the exception that holds the error message
	 * @param format says if the message should be formated or not.
	 */
    public ErrorController(Exception e, boolean format) {
        this(e, format, "Error");
    }

    /**
	 * A constructor for an Exception that formats and displays the error / exception with a custom severity.
	 *  
	 * @param e the exception that holds the error message
	 * @param format says if the message should be formated or not.
	 */
    public ErrorController(Exception e, boolean format, String severity) {
        this.format = format;
        this.ex = e;
        this.msg = ex.getMessage();
        displayError();
    }

    /**
	 * @return the error message.
	 */
    public String getMessage() {
        return msg;
    }

    private String formatMsg(String m) {
        if (m.length() > 500) m = m.substring(0, 500) + "...";
        StringBuffer temp = new StringBuffer(m);
        int limit = 60;
        int len = temp.length();
        int pos = 0;
        int i = 0;
        while (len > limit) {
            pos = temp.indexOf(" ", limit + i * limit);
            if (pos == -1) break;
            temp.insert(pos + 1, "\n");
            len = temp.substring(pos + 2).length();
            i++;
        }
        return temp.toString();
    }

    /**
	 * Displays the message in a JOptionPane with a severity of "error"
	 * 
	 * @param m the message.
	 */
    private void displayError() {
        displayError("error");
    }

    /**
	 * Displays the message in a JOptionPane with a specified severity
	 * 
	 * @param m the message.
	 */
    private void displayError(String severity) {
        if (format == true) msg = formatMsg(msg);
        if (severity.toLowerCase().equals("error")) JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE); else if (severity.toLowerCase().equals("warning")) JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE); else if (severity.toLowerCase().equals("information")) JOptionPane.showMessageDialog(null, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
