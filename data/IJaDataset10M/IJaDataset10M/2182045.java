package org.mortbay.jetty;

import java.io.IOException;

public class EofException extends IOException {

    public EofException() {
    }

    public EofException(String reason) {
        super(reason);
    }

    public EofException(Throwable th) {
        initCause(th);
    }
}
