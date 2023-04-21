package org.mattressframework.api.exceptions;

/**
 * An exception that can define an error response status, message and entity.
 * This can be thrown from any resource method.
 * 
 * <p>
 * The root cause {@link Throwable} should be logged and dealt with by the
 * application, as it will not be wrapped by this exception or dealt with by the
 * framework.
 * </p>
 * 
 * @author Josh Devins (joshdevins@mattressframework.org)
 */
public final class WebApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int status = -1;

    private Object entity;

    public WebApplicationException() {
        super();
    }

    public WebApplicationException(final int status) {
        this(status, null);
    }

    public WebApplicationException(final int status, final String message) {
        this(message);
        this.status = status;
    }

    public WebApplicationException(final String message) {
        super(message);
    }

    public Object getEntity() {
        return entity;
    }

    public int getStatus() {
        return status;
    }

    public void setEntity(final Object entity) {
        this.entity = entity;
    }
}
