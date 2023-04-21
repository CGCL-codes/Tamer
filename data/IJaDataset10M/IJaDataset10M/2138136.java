package com.jaccal.console;

import com.jaccal.CardException;

/**
 * BreakPoint is used by Anubis to insert a breakpoint in the script.
 * @author Chang Sau Sheong
 */
public class BreakPointException extends CardException {

    String message = I18N.getString("default_breakpoint_message");

    public BreakPointException(String message) {
        super(message);
    }

    public String getMessage() {
        return message;
    }
}
