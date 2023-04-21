package com.phloc.commons.io.streamprovider;

import static org.junit.Assert.assertEquals;
import java.io.InputStream;
import org.junit.Test;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.StreamUtils;

/**
 * Test class for class {@link StringInputStreamProvider}.
 * 
 * @author philip
 */
public final class StringInputStreamProviderTest {

    @Test
    public void testSimple() {
        final String s = "Hallo Weltäöü";
        InputStream aIS = new StringInputStreamProvider(s, CCharset.CHARSET_UTF_8).getInputStream();
        assertEquals(s, StreamUtils.getAllBytesAsString(aIS, CCharset.CHARSET_UTF_8));
        aIS = new StringInputStreamProvider(s.toCharArray(), CCharset.CHARSET_UTF_8).getInputStream();
        assertEquals(s, StreamUtils.getAllBytesAsString(aIS, CCharset.CHARSET_UTF_8));
        aIS = new StringInputStreamProvider(new StringBuilder(s), CCharset.CHARSET_UTF_8).getInputStream();
        assertEquals(s, StreamUtils.getAllBytesAsString(aIS, CCharset.CHARSET_UTF_8));
    }
}
