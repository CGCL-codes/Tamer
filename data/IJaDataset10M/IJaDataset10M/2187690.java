package org.kaleidofoundry.core.io;

import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

public class MimeTypesDefaultServiceTest extends Assert {

    @Test
    public void testMimeExtentions() {
        MimeTypesDefaultService mimes = null;
        String mimeType = null;
        String[] ext = null;
        List<String> resultsValues = null;
        mimes = MimeTypesFactory.getService();
        assertNotNull(mimes);
        mimeType = "application/postscript";
        resultsValues = Arrays.asList(new String[] { "ps", "ai", "eps" });
        ext = mimes.getExtentions(mimeType);
        assertNotNull(ext);
        assertEquals(resultsValues.size(), ext.length);
        for (String v : resultsValues) {
            assertTrue(Arrays.asList(ext).contains(v));
        }
        assertTrue(mimes.isMimeTypeBinary(mimeType));
        mimeType = "text/plain";
        resultsValues = Arrays.asList(new String[] { "txt", "text", "diff" });
        ext = mimes.getExtentions(mimeType);
        assertNotNull(ext);
        assertEquals(resultsValues.size(), ext.length);
        for (String v : resultsValues) {
            assertTrue(Arrays.asList(ext).contains(v));
        }
        assertTrue(mimes.isMimeTypeAscii(mimeType));
    }

    @Test
    public void testMimeType() {
        MimeTypesDefaultService mimes = null;
        String extention = null;
        String mimeType = null;
        mimes = MimeTypesFactory.getService();
        assertNotNull(mimes);
        extention = "ps";
        mimeType = mimes.getMimeType(extention);
        assertEquals(mimeType, "application/postscript");
        extention = "txt";
        mimeType = mimes.getMimeType(extention);
        assertEquals(mimeType, "text/plain");
    }
}
