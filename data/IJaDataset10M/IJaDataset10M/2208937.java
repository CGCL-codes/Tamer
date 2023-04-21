package com.hongbo.cobweb.nmr.converter.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link org.apache.camel.StreamCache} implementation for Cache the Reader {@link java.io.Reader}s
 */
public class ReaderCache extends StringReader implements StreamCache {

    private static final transient Logger LOG = LoggerFactory.getLogger(ReaderCache.class);

    private String data;

    public ReaderCache(String data) {
        super(data);
        this.data = data;
    }

    public void close() {
    }

    @Override
    public void reset() {
        try {
            super.reset();
        } catch (IOException e) {
            LOG.warn("Cannot reset cache", e);
        }
    }

    public void writeTo(OutputStream os) throws IOException {
        os.write(data.getBytes());
    }

    String getData() {
        return data;
    }
}
