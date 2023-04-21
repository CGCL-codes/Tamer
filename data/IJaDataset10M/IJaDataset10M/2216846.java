package org.jboss.resteasy.spi;

import javax.ws.rs.core.Response;

/**
 * Thrown by RESTEasy when HTTP Method Not Allowed (405) is encountered
 */
public class MethodNotAllowedException extends LoggableFailure {

    public MethodNotAllowedException(String s) {
        super(s, 405);
    }

    public MethodNotAllowedException(String s, Response response) {
        super(s, response);
    }

    public MethodNotAllowedException(String s, Throwable throwable, Response response) {
        super(s, throwable, response);
    }

    public MethodNotAllowedException(String s, Throwable throwable) {
        super(s, throwable, 405);
    }

    public MethodNotAllowedException(Throwable throwable) {
        super(throwable, 405);
    }

    public MethodNotAllowedException(Throwable throwable, Response response) {
        super(throwable, response);
    }
}
