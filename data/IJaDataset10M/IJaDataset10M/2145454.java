package org.conann.exceptions;

import static java.lang.String.format;

@SuppressWarnings({ "AssignmentToCollectionOrArrayFieldFromParameter" })
public class WebBeansException extends RuntimeException {

    private final Object[] params;

    public WebBeansException(String message, Object... messageParams) {
        super(format(message, messageParams));
        params = messageParams;
    }

    public WebBeansException(Throwable cause, String message, Object... messageParams) {
        super(format(message, messageParams), cause);
        params = messageParams;
    }

    public Object[] getParams() {
        return params;
    }
}
