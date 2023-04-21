package org.nakedobjects.plugins.html.task;

import org.nakedobjects.plugins.html.action.ActionException;

public class TaskLookupException extends ActionException {

    private static final long serialVersionUID = 1L;

    public TaskLookupException() {
    }

    public TaskLookupException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public TaskLookupException(final String msg) {
        super(msg);
    }

    public TaskLookupException(final Throwable cause) {
        super(cause);
    }
}
