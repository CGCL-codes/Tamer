package org.taak;

import org.taak.error.*;

/**
 * Defines execution condition codes.
 *     NONE        Normal condition.
 *     CONTINUE    Contine execution from the begin of a loop.
 *     BREAK       Exit the enclosing loop.
 *     RETURN      Exit the current function.
 *     EXIT        Exit the current script.
 *     REDIRECT    Redirect to another URL in a web application.
 *     ERROR       Exceptional condition.  Unwind to appropriate try/catch
 *                 construct.
 */
public interface Condition {

    public static final int NONE = 0;

    public static final int CONTINUE = 1;

    public static final int BREAK = 2;

    public static final int RETURN = 3;

    public static final int EXIT = 4;

    public static final int REDIRECT = 5;

    public static final int ERROR = 6;
}
