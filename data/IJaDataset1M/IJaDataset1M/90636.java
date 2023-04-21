package org.archive.io.arc;

import it.unimi.dsi.fastutil.io.RepositionableStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import junit.framework.TestCase;
import org.archive.io.GzippedInputStream;

public class ARCUtilsTest extends TestCase {

    public void testGetArcfileName() throws URISyntaxException {
        final String filename = "x.arc.gz";
        assertEquals(filename, ARCUtils.parseArcFilename("/tmp/one.two/" + filename));
        assertEquals(filename, ARCUtils.parseArcFilename("http://archive.org/tmp/one.two/" + filename));
        assertEquals(filename, ARCUtils.parseArcFilename("rsync://archive.org/tmp/one.two/" + filename));
    }

    public void testCompressedStream() throws IOException {
        byte[] bytes = "test".getBytes();
        ByteArrayInputStream baos = new ByteArrayInputStream(bytes);
        assertFalse(ARCUtils.testCompressedStream(baos));
        byte[] gzippedMetaData = GzippedInputStream.gzip(bytes);
        baos = new ByteArrayInputStream(gzippedMetaData);
        assertTrue(ARCUtils.testCompressedStream(baos));
        gzippedMetaData = GzippedInputStream.gzip(bytes);
        final RepositionableByteArrayInputStream rbaos = new RepositionableByteArrayInputStream(gzippedMetaData);
        final int totalBytes = gzippedMetaData.length;
        assertTrue(ARCUtils.testCompressedRepositionalStream(rbaos));
        long available = rbaos.available();
        assertEquals(available, totalBytes);
        assertEquals(rbaos.position(), 0);
    }

    private class RepositionableByteArrayInputStream extends ByteArrayInputStream implements RepositionableStream {

        public RepositionableByteArrayInputStream(final byte[] bytes) {
            super(bytes);
        }

        public void position(long p) {
            this.pos = (int) p;
        }

        public long position() {
            return this.pos;
        }
    }
}
