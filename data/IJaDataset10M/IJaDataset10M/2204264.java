package com.raelity.jvi.swing;

import javax.swing.Action;

/**
 * Stuff to work with editor panes.
 */
public interface TextOps {

    /** Move cursor to start of line indicated in place1. a1 = line */
    public static final int GOTO_LINE = 1;

    /** Move cursor to line maintaining column position. a1 = line */
    public static final int CURSOR_TO_LINE = GOTO_LINE + 1;

    /** Position at/near begin line depending on flags. a1 = line, a2 = flag */
    public static final int BEGIN_LINE = CURSOR_TO_LINE + 1;

    /** Move cursor to the right */
    public static final int CURSOR_RIGHT = BEGIN_LINE + 1;

    /** Move cursor to the left */
    public static final int CURSOR_LEFT = CURSOR_RIGHT + 1;

    /** Insert the text at specified cursor position. a1 = offset, text */
    public static final int INSERT_TEXT = CURSOR_LEFT + 1;

    /** Insert the text at the current cursor position. */
    public static final int INSERT_NEW_LINE = INSERT_TEXT + 1;

    /** Insert the text at the current cursor position. */
    public static final int INSERT_TAB = INSERT_NEW_LINE + 1;

    /** Insert the text at the current cursor position. */
    public static final int KEY_TYPED = INSERT_TAB + 1;

    /** Delete previous character */
    public static final int DELETE_PREVIOUS_CHAR = KEY_TYPED + 1;

    public void xact(Action action);

    public void xact(Action action, String keyTyped);

    void xact(String redoAction);

    public void xop(int op, String s);

    public void xop(int op);
}
