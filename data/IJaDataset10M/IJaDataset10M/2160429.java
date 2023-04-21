package com.volantis.styling.impl.engine.sheet;

import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.cornerstone.stack.Stack;
import com.volantis.synergetics.cornerstone.stack.ArrayListStack;

/**
 * A stack of {@link CompiledStyleSheet}s.
 */
public class StyleSheetStack {

    /**
     * The underlying type unsafe stack.
     */
    private final Stack stack;

    /**
     * Initialise.
     */
    public StyleSheetStack() {
        stack = new ArrayListStack();
    }

    /**
     * Push the style sheet on the stack.
     *
     * @param styleSheet The style sheet to push.
     */
    public void push(CompiledStyleSheet styleSheet) {
        if (styleSheet == null) {
            throw new IllegalArgumentException("styleSheet cannot be null");
        }
        stack.push(styleSheet);
    }

    /**
     * Pop the style sheet off the stack.
     *
     * @param styleSheet The style sheet that is expected to be on the top of
     * the stack.
     */
    public void pop(CompiledStyleSheet styleSheet) {
        if (styleSheet == null) {
            throw new IllegalArgumentException("styleSheet cannot be null");
        }
        CompiledStyleSheet poppedStyleSheet = (CompiledStyleSheet) stack.pop();
        if (poppedStyleSheet != styleSheet) {
            throw new IllegalStateException("Popped style sheet " + poppedStyleSheet + " does not match expected style sheet " + styleSheet);
        }
    }

    /**
     * Get the current depth of the stack.
     *
     * @return The depth of the stack.
     */
    public int depth() {
        return stack.depth();
    }
}
