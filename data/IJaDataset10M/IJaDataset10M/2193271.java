package org.restlet.ext.jaxrs.internal.exceptions;

import java.util.Collection;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Response.Status;

/**
 * The server is refusing to service the request because the entity of the
 * request is in a format not accepted by the requested resource for the
 * requested method.
 * 
 * @author Stephan Koops
 * @see <a href="http://tools.ietf.org/html/rfc2616#sec10.4.16">RFC 2616,
 *      Section 10.4.16, "415 Unsupported Media Type"</a>
 */
public class UnsupportedMediaTypeWebAppException extends WebApplicationException {

    private static final long serialVersionUID = 767927925135821476L;

    private final Collection<Variant> accepted;

    /**
     * @param accepted
     *            the accepted Variants.
     */
    public UnsupportedMediaTypeWebAppException(Collection<Variant> accepted) {
        super(Status.UNSUPPORTED_MEDIA_TYPE);
        if (accepted == null) {
            throw new IllegalArgumentException("You have to give a collection of accepted Variants.");
        }
        this.accepted = accepted;
    }

    /**
     * Returns the accepted {@link Variant}s.
     * 
     * @return the accepted MediaTypes.
     */
    public Collection<Variant> getAccepted() {
        return this.accepted;
    }
}
