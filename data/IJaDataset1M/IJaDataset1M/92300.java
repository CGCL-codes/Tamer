package org.restlet.ext.jaxrs.internal.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyReader;
import org.restlet.data.MediaType;

/**
 * This kind of Exception is thrown, if MessageBodyReaders are used, but are not
 * available. Normally this does not occurs.
 * 
 * @author Stephan Koops
 */
public class NoMessageBodyReaderException extends WebApplicationException {

    private static final long serialVersionUID = 9177449724300611418L;

    private final Class<?> paramType;

    private final MediaType mediaType;

    /**
     * @param paramType
     * @param mediaType
     */
    public NoMessageBodyReaderException(MediaType mediaType, Class<?> paramType) {
        super(Status.UNSUPPORTED_MEDIA_TYPE);
        this.mediaType = mediaType;
        this.paramType = paramType;
    }

    /**
     * Returns the media type for which (in combination with the java parameter
     * type, see {@link #getParamType()}) no {@link MessageBodyReader} was
     * found.
     * 
     * @return the media type for which (in combination with the java parameter
     *         type, see {@link #getParamType()}) no {@link MessageBodyReader}
     *         was found.
     */
    public MediaType getMediaType() {
        return this.mediaType;
    }

    /**
     * Returns the java parameter type for which (in combination with the media
     * type, see {@link #getMediaType()}) no {@link MessageBodyReader} was
     * found.
     * 
     * @return the java parameter type for which (in combination with the media
     *         type, see {@link #getMediaType()}) no {@link MessageBodyReader}
     *         was found.
     */
    public Class<?> getParamType() {
        return this.paramType;
    }
}
