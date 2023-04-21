package com.google.gwt.util.tools;

import com.google.gwt.dev.util.Empty;

/**
 * Argument handler for flags that have no parameters.
 */
public abstract class ArgHandlerFlag extends ArgHandler {

    @Override
    public String[] getTagArgs() {
        return Empty.STRINGS;
    }

    @Override
    public int handle(String[] args, int startIndex) {
        if (setFlag()) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    public abstract boolean setFlag();
}
