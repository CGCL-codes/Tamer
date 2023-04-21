package net.sf.uitags.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import junit.framework.TestCase;

public class ResourceCloserTest extends TestCase {

    public void testDoesNotFailOnNullResource() {
        Reader reader = null;
        ResourceCloser.close(reader);
        InputStream inputStream = null;
        ResourceCloser.close(inputStream);
    }

    public void testDoesNotFailOnExceptionCausedByClosingResource() {
        ResourceCloser.close(new MockReader());
        ResourceCloser.close(new MockInputStream());
    }

    private static final class MockReader extends Reader {

        public void close() throws IOException {
            throw new IOException("Thrown by a test method that always throws an exception.");
        }

        public int read(char[] cbuf, int off, int len) throws IOException {
            return 0;
        }
    }

    private static final class MockInputStream extends InputStream {

        public void close() throws IOException {
            throw new IOException("Thrown by a test method that always throws an exception.");
        }

        public int read() throws IOException {
            return 0;
        }
    }
}
