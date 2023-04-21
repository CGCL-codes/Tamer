package org.pachyderm.apollo.app;

import com.webobjects.foundation.NSUndoManager;

public class CXResponder {

    private CXResponder _nextResponder;

    public CXResponder() {
        super();
    }

    public boolean acceptsFirstResponder() {
        return false;
    }

    public boolean becomeFirstResponder() {
        return true;
    }

    public boolean resignFirstResponder() {
        return true;
    }

    public void setNextResponder(CXResponder responder) {
    }

    public CXResponder nextResponder() {
        return _nextResponder;
    }

    public NSUndoManager undoManager() {
        return null;
    }
}
