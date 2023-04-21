package org.acs.elated.test.commons;

import junit.framework.*;
import org.acs.elated.commons.*;

public class TestMIME extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetFileExt() {
        String ext = MIME.getFileExt("image/junk");
        assertEquals(ext, "");
        ext = MIME.getFileExt("image/jpeg");
        assertEquals(ext, "jpg");
        ext = MIME.getFileExt("application/msword");
        assertEquals(ext, "doc");
    }

    public void testGetMIMEType() {
        String mime = MIME.getMIMEType("something.");
        assertEquals(mime, MIME.DEFAULT_MIME_TYPE);
        mime = MIME.getMIMEType("image.jpg");
        assertEquals(mime, "image/jpeg");
        mime = MIME.getMIMEType("my.saved.file.doc");
        assertEquals(mime, "application/msword");
    }
}
