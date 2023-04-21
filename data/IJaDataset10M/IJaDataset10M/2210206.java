package org.apache.mina.filter.stream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Tests {@link StreamWriteFilter}.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class StreamWriteFilterTest extends AbstractStreamWriteFilterTest<InputStream, StreamWriteFilter> {

    @Override
    protected StreamWriteFilter createFilter() {
        return new StreamWriteFilter();
    }

    @Override
    protected InputStream createMessage(byte[] data) throws Exception {
        return new ByteArrayInputStream(data);
    }
}
