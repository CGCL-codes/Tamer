package org.gocha.inetools.http;

import java.io.File;
import java.nio.charset.Charset;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gocha
 */
public class FileCharsetDetectorTest {

    public FileCharsetDetectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSomeMethod() {
        File f = new File("/home/gocha/coding/test/RssTranslate/appdata/lenta.ru.rss");
        FileCharsetDetector detect = new FileCharsetDetector(f);
        FileCharsetDetector.getMimeExtensionsAliases().put("rss", "text/xml");
        Charset cs = detect.getCharset();
    }
}
