package com.bluebrim.text.shared.swing;

/**
 * Copy of javax.swing.text.StateInvariantError a few changes made in order to work around visibility problems
 * when deriving from javax.swing.text.DefaultStyledDocument.
 *
 */
public class CoStateInvariantError extends Error {

    public CoStateInvariantError(String s) {
        super(s);
    }
}
