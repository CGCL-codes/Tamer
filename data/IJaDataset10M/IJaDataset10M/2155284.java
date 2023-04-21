package com.bittorrent4j.encoding;

import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import org.bittorrent4j.encoding.BencodeBuilder;
import org.bittorrent4j.encoding.BencodeFactory;
import org.bittorrent4j.encoding.BencodeReader;
import org.bittorrent4j.encoding.BencodeWriter;

/**
 * @author <a href="mailto:opalka.richard@gmail.com">Richard Opalka</a>
 */
public final class BencodeFactoryImpl extends BencodeFactory {

    /**
     * Default encoding to be used.
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    public BencodeFactoryImpl() {
    }

    public BencodeWriter newBencodeWriter(final WritableByteChannel channel) {
        assertNotNull(channel);
        return new BencodeWriterImpl(channel, DEFAULT_ENCODING);
    }

    public BencodeBuilder newBencodeBuilder(final WritableByteChannel channel) {
        assertNotNull(channel);
        return new BencodeBuilderImpl(newBencodeWriter(channel));
    }

    public BencodeReader newBencodeReader(final ReadableByteChannel channel) {
        assertNotNull(channel);
        return new BencodeReaderImpl(channel, DEFAULT_ENCODING);
    }

    private void assertNotNull(final Object channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }
    }
}
