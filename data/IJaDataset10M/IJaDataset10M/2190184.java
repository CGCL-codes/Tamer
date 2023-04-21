package eu.medsea.mimeutil.test;

import java.io.File;
import junit.framework.TestCase;
import eu.medsea.mimeutil.MimeUtil;
import eu.medsea.mimeutil.MimeException;

public class MimeUtilTest extends TestCase {

    public void testGetBestMatch() {
        assertEquals(MimeUtil.getPreferedMimeType("application/*;q=0.2,text/xml;q=0.1", "application/xml,text/xml"), "application/xml");
        assertEquals(MimeUtil.getPreferedMimeType("application/xml;q=0.1,text/*;q=0.2", "application/xml,text/xml"), "text/xml");
        assertEquals(MimeUtil.getPreferedMimeType("application/*,text/xml;q=0.1", "application/xml,text/xml"), "text/xml");
        assertEquals(MimeUtil.getPreferedMimeType("application/xml;q=0.1,text/*", "application/xml,text/xml"), "application/xml");
        assertEquals(MimeUtil.getPreferedMimeType("*/*,text/xml;q=0.1", "application/xml,text/xml"), "text/xml");
        assertEquals(MimeUtil.getPreferedMimeType("application/xml;q=0.1,text/*,*/*", "application/xml,text/xml"), "application/xml");
        assertEquals(MimeUtil.getPreferedMimeType("application/xml", "text/html"), "text/html");
        assertEquals(MimeUtil.getPreferedMimeType("application/xml,text/xml", "text/xml,application/xml"), "application/xml");
        assertEquals(MimeUtil.getPreferedMimeType("text/xml,application/xml", "text/xml,application/xml"), "text/xml");
        assertEquals(MimeUtil.getPreferedMimeType("application/xml;q=0.1,text/xml", "application/xml,text/xml"), "text/xml");
        assertEquals(MimeUtil.getPreferedMimeType("application/xml,text/xml;q=0.1", "application/xml,text/xml"), "application/xml");
        assertEquals(MimeUtil.getPreferedMimeType("application/xml;q=0.1,text/xml", "application/xml,text/xml"), "text/xml");
    }

    public void testIsMimeTypeKnown() {
        assertTrue(MimeUtil.isMimeTypeKnown("application/xml"));
        assertTrue(MimeUtil.isMimeTypeKnown("text/xml"));
        assertTrue(MimeUtil.isMimeTypeKnown("text/plain"));
        assertFalse(MimeUtil.isMimeTypeKnown("abc/abc"));
        MimeUtil.addKnownMimeType("abc/abc");
        assertTrue(MimeUtil.isMimeTypeKnown("abc/abc"));
        assertTrue(MimeUtil.isMimeTypeKnown(MimeUtil.UNKNOWN_MIME_TYPE));
        assertFalse(MimeUtil.isMimeTypeKnown("application/x-unknown-mime-type"));
    }

    public void testFirstMimeType() {
        assertEquals(MimeUtil.getFirstMimeType("text/html, application/xml"), "text/html");
        assertEquals(MimeUtil.getFirstMimeType("text/plain, application/xml"), "text/plain");
        assertEquals(MimeUtil.getFirstMimeType(null), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getFirstMimeType("  "), MimeUtil.UNKNOWN_MIME_TYPE);
    }

    public void testGetMimeTypeAsString() {
        assertEquals(MimeUtil.getMimeType("test_files/e.xml"), "text/xml,application/xml");
        assertEquals(MimeUtil.getMimeType("a.de"), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMimeType("test_files/d-png.img"), "image/png");
        assertEquals(MimeUtil.getMimeType("test_files/e.xml", false), "text/xml");
        assertEquals(MimeUtil.getMimeType("a.de", false), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMimeType("test_files/d-png.img", false), "image/png");
    }

    public void testGetMimeTypeAsFile() {
        assertEquals(MimeUtil.getMimeType(new File("test_files/e.xml")), "text/xml,application/xml");
        assertEquals(MimeUtil.getMimeType(new File("a.de")), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMimeType(new File("test_files/d-png.img")), "image/png");
        assertEquals(MimeUtil.getMimeType(new File("test_files/e.xml"), false), "text/xml");
        assertEquals(MimeUtil.getMimeType(new File("a.de"), false), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMimeType(new File("test_files/d-png.img"), false), "image/png");
    }

    public void testGetMagicMimeTypeByString() {
        assertEquals(MimeUtil.getMagicMimeType("a.abc"), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMagicMimeType("a.de"), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMagicMimeType("a.url"), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMagicMimeType("test_files/a.html"), "text/html");
        assertEquals(MimeUtil.getMagicMimeType("test_files/b.jpg"), "image/jpeg");
        assertEquals(MimeUtil.getMagicMimeType("test_files/c.gif"), "image/gif");
        assertEquals(MimeUtil.getMagicMimeType("test_files/d.png"), "image/png");
        assertEquals(MimeUtil.getMagicMimeType("test_files/e.svg"), "image/svg+xml");
        assertEquals(MimeUtil.getMagicMimeType("test_files/b-jpg.img"), "image/jpeg");
        assertEquals(MimeUtil.getMagicMimeType("test_files/c-gif.img"), "image/gif");
        assertEquals(MimeUtil.getMagicMimeType("test_files/d-png.img"), "image/png");
        assertEquals(MimeUtil.getMagicMimeType("test_files/e-svg.img"), "image/svg+xml");
    }

    public void testGetMagicMimeTypeByFile() {
        assertEquals(MimeUtil.getMagicMimeType(new File("a.abc")), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMagicMimeType(new File("a.de")), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMagicMimeType(new File("a.url")), MimeUtil.UNKNOWN_MIME_TYPE);
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/a.html")), "text/html");
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/b.jpg")), "image/jpeg");
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/c.gif")), "image/gif");
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/d.png")), "image/png");
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/e.svg")), "image/svg+xml");
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/b-jpg.img")), "image/jpeg");
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/c-gif.img")), "image/gif");
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/d-png.img")), "image/png");
        assertEquals(MimeUtil.getMagicMimeType(new File("test_files/e-svg.img")), "image/svg+xml");
    }

    public void testMimeQuality() {
        assertEquals(MimeUtil.getMimeQuality("*/*"), 0.01, 0.0);
        assertEquals(MimeUtil.getMimeQuality("*/*;q=0.4"), 0.4, 0.0);
        assertEquals(MimeUtil.getMimeQuality("text/*"), 0.02, 0.0);
        assertEquals(MimeUtil.getMimeQuality("text/* ; q=0.2"), 0.2, 0.0);
        assertEquals(MimeUtil.getMimeQuality("application/*"), 0.02, 0.0);
        assertEquals(MimeUtil.getMimeQuality("text/html"), 1.0, 0.0);
        assertEquals(MimeUtil.getMimeQuality("application/abc"), 1.0, 0.0);
        assertEquals(MimeUtil.getMimeQuality("application/abc ;q=0.9"), 0.9, 0.0);
        assertEquals(MimeUtil.getMimeQuality("application/abc;a=ignore-a;q=0.7"), 0.7, 0.0);
        assertEquals(MimeUtil.getMimeQuality("application/abc;q=10"), 1.0, 0.0);
        try {
            assertEquals(MimeUtil.getMimeQuality("application/abc;q=a"), 0.0, 0.0);
            fail("Should not have reached here");
        } catch (MimeException expected) {
        }
        try {
            assertEquals(MimeUtil.getMimeQuality("application/abc;q=hello"), 0.0, 0.0);
            fail("Should not have reached here");
        } catch (MimeException expected) {
        }
    }

    public void testMajorCoponent() {
        assertEquals(MimeUtil.getMajorComponent("image/png;q=0.5"), "image");
        assertEquals(MimeUtil.getMajorComponent("text/xml"), "text");
        assertEquals(MimeUtil.getMajorComponent("application/xml;level=1"), "application");
        assertEquals(MimeUtil.getMajorComponent("chemical/x-pdb"), "chemical");
        try {
            assertEquals(MimeUtil.getMajorComponent("vnd.ms-cab-compressed"), "vnd.ms-cab-compressed");
            fail("Should not have reached here");
        } catch (MimeException expected) {
        }
    }

    public void testMinorCoponent() {
        assertEquals(MimeUtil.getMinorComponent("image/png"), "png");
        assertEquals(MimeUtil.getMinorComponent("text/xml"), "xml");
        assertEquals(MimeUtil.getMinorComponent("application/xml"), "xml");
        assertEquals(MimeUtil.getMinorComponent("chemical/x-pdb"), "x-pdb");
        try {
            assertEquals(MimeUtil.getMinorComponent("vnd.ms-cab-compressed"), "vnd.ms-cab-compressed");
            fail("Should not have reached here");
        } catch (MimeException expected) {
        }
    }

    public void testGetExtensionMimeTypeAsString() {
        assertEquals(MimeUtil.getExtensionMimeTypes("a.png"), "image/png");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.xml"), "text/xml,application/xml");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.txt"), "text/plain");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.TXT"), "text/plain");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.TxT"), "text/plain");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.html"), "text/html");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.xhtml"), "application/xhtml+xml,text/html");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.cab"), "application/vnd.ms-cab-compressed,application/cab,application/x-compress,application/x-compressed,zz-application/zz-winassoc-cab");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.xyz"), "chemical/x-pdb");
        assertEquals(MimeUtil.getExtensionMimeTypes("a.xxx"), MimeUtil.UNKNOWN_MIME_TYPE);
    }

    public void testGetExtensionMimeTypeAsFile() {
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.png")), "image/png");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.xml")), "text/xml,application/xml");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.txt")), "text/plain");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.TXT")), "text/plain");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.TxT")), "text/plain");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.html")), "text/html");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.xhtml")), "application/xhtml+xml,text/html");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.cab")), "application/vnd.ms-cab-compressed,application/cab,application/x-compress,application/x-compressed,zz-application/zz-winassoc-cab");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.xyz")), "chemical/x-pdb");
        assertEquals(MimeUtil.getExtensionMimeTypes(new File("a.xxx")), MimeUtil.UNKNOWN_MIME_TYPE);
    }
}
