package com.jdbwc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * Handles reading a gzipped Input stream.<br />
 *
 * @author Tim Gall
 * @version 2010-04-15
 */
public final class GzipStreamReader extends HttpEntityWrapper {

    public GzipStreamReader(final HttpEntity entity) {
        super(entity);
    }

    /**
         * @see org.apache.http.entity.HttpEntityWrapper#getContent()
         */
    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        InputStream wrappedin = wrappedEntity.getContent();
        return new GZIPInputStream(wrappedin);
    }

    /**
         * @see org.apache.http.entity.HttpEntityWrapper#getContentLength()
         */
    @Override
    public long getContentLength() {
        return -1;
    }
}
