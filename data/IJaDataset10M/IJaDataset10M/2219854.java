package org.jboss.resteasy.spi;

import javax.ws.rs.core.Response;

/**
 * Thrown by RESTEasy when HTTP Not Found (404) is encountered
 */
public class NotFoundException extends Failure {

    public NotFoundException(String s) {
        super(s, 404);
    }

    public NotFoundException(String s, Response response) {
        super(s, response);
    }

    public NotFoundException(String s, Throwable throwable, Response response) {
        super(s, throwable, response);
    }

    public NotFoundException(String s, Throwable throwable) {
        super(s, throwable, 404);
    }

    public NotFoundException(Throwable throwable) {
        super(throwable, 404);
    }

    public NotFoundException(Throwable throwable, Response response) {
        super(throwable, response);
    }
}
