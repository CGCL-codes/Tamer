package org.isurf.spmiddleware.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for IOUtil.
 */
public class IOUtilTest {

    /**
	 * Test method for {@link org.isurf.spmiddleware.utils.IOUtil#close(java.io.Closeable)}.
	 */
    @Test
    public void testCloseUsingNull() {
        IOUtil.close(null);
    }

    /**
	 * Test method for {@link org.isurf.spmiddleware.utils.IOUtil#close(java.io.Closeable)}.
	 */
    @Test
    public void testClose() throws IOException {
        Reader reader = new StringReader("sgfs");
        assertTrue(reader.ready());
        IOUtil.close(reader);
        try {
            reader.ready();
            fail("IOException should have been thrown");
        } catch (IOException ioe) {
            assertNotNull(ioe.getMessage());
        }
    }
}
