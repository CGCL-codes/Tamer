package org.mattressframework.core.providers.entity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import org.apache.log4j.Logger;
import org.mattressframework.api.HttpEntityWriter;
import org.mattressframework.api.annotations.SupportsMediaTypes;
import org.mattressframework.api.http.Header;
import org.mattressframework.api.http.MediaType;

/**
 * An {@link HttpEntityWriter} implementation that simply writes the entity to
 * the stream as a {@link String}.
 * 
 * <p>
 * This support is according to the JSR 311 spec.
 * </p>
 * 
 * @author Josh Devins (joshdevins@mattressframework.org)
 */
@SupportsMediaTypes("text/plain")
public final class StringHttpEntityWriter implements HttpEntityWriter {

    private static final Logger LOG = Logger.getLogger(StringHttpEntityWriter.class);

    @Deprecated
    public int getSize(final Object entity) {
        return entity == null ? 0 : entity.toString().length();
    }

    public boolean supportsType(final Class<?> type) {
        return true;
    }

    @Deprecated
    public void writeTo(final Object entity, final MediaType mediaType, final Map<String, Header> httpHeaders, final OutputStream entityStream) throws IOException {
        final OutputStreamWriter entityWriter = new OutputStreamWriter(entityStream, "UTF-8");
        writeTo(entity, mediaType, httpHeaders, entityWriter);
        entityWriter.flush();
    }

    public void writeTo(final Object entity, final MediaType mediaType, final Map<String, Header> httpHeaders, final OutputStreamWriter entityStream) throws IOException {
        if (entity == null) {
            return;
        }
        final String str = entity.toString();
        entityStream.write(str);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Response entity written (as String):");
            LOG.debug(str);
        }
    }
}
