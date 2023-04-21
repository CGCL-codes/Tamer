package net.sf.webwarp.util.string;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.webwarp.util.string.GenericCharEncoder;
import net.sf.webwarp.util.string.HtmlStringEncoder;
import net.sf.webwarp.util.string.StringEncoder;
import org.junit.Test;

public class StringEncoderTest {

    @Test
    public void testEncodeHex() {
        StringEncoder encoder = new StringEncoder("abc", StringEncoder.ENCODE_HEXADECIMAL);
        String result = encoder.encode("12345agfbghc33");
        assertNotNull(result);
        assertTrue(result.startsWith("12345"));
        assertTrue(result.contains("gf"));
        assertTrue(result.contains("gh"));
        assertTrue(result.endsWith("33"));
    }

    @Test
    public void testEncodeNum() {
        StringEncoder encoder = new StringEncoder("abc", StringEncoder.ENCODE_HTML_NUMERIC);
        String result = encoder.encode("12345agfbghc33");
        assertNotNull(result);
        assertTrue(result.startsWith("12345"));
        assertTrue(result.contains("gf"));
        assertTrue(result.contains("gh"));
        assertTrue(result.endsWith("33"));
    }

    @Test
    public void testEncodeOctal() {
        StringEncoder encoder = new StringEncoder("abc", StringEncoder.ENCODE_OCTAL);
        String result = encoder.encode("12345agfbghc33");
        assertNotNull(result);
        assertTrue(result.startsWith("12345"));
        assertTrue(result.contains("gf"));
        assertTrue(result.contains("gh"));
        assertTrue(result.endsWith("33"));
    }

    @Test
    public void testMisc() {
        try {
            StringEncoder encoder;
            try {
                encoder = new StringEncoder(" #*", -100);
            } catch (Exception e) {
                System.out.println(e);
            }
            encoder = new StringEncoder(" #*?", StringEncoder.ENCODE_HEXADECIMAL);
            System.out.println(encoder.encode("sk  djhlfsa#@#�*"));
            System.out.println("'#' = '" + encoder.encode("#") + "'");
            System.out.println("'?' = '" + encoder.encode("?") + "'");
            System.out.println("'*' = '" + encoder.encode("*") + "'");
            System.out.println("' ' = '" + encoder.encode(" ") + "'");
            System.out.println(encoder.encode("sk  djhlfsa#@#�*"));
            System.out.println(encoder.encode("sk  djhlfsa#@#�*"));
            System.out.println(encoder.encode("sk  djhlfsa#@#�*"));
            System.out.println(encoder.encode("noreplacement") == "noreplacement");
            encoder = new StringEncoder(" #*", StringEncoder.ENCODE_OCTAL);
            System.out.println("'#' = '" + encoder.encode("#") + "'");
            System.out.println("'?' = '" + encoder.encode("?") + "'");
            System.out.println("'*' = '" + encoder.encode("*") + "'");
            System.out.println("' ' = '" + encoder.encode(" ") + "'");
            System.out.println(encoder.encode("sk  djhlfsa#@#�*"));
            encoder = HtmlStringEncoder.instance();
            System.out.println("'>' = '" + encoder.encode(">") + "'");
            System.out.println("'&' = '" + encoder.encode("&") + "'");
            System.out.println("'\"' = '" + encoder.encode("\"") + "'");
            System.out.println("'<' = '" + encoder.encode("<") + "'");
            System.out.println(encoder.encode("a'bcdefg23435ADJH���������" + (char) 270));
            encoder = new StringEncoder("\"", new GenericCharEncoder('"', "\"\""));
            System.out.println("'\"' = '" + encoder.encode("\"") + "'");
            System.out.println(encoder.encode("a'bcdefg23435\"ADJH���������"));
            encoder = new StringEncoder("" + (char) 500 + (char) 501 + (char) 502, StringEncoder.ENCODE_HTML_NUMERIC);
            String str = "'" + (char) 500 + (char) 501 + (char) 502 + (char) 503 + "'";
            System.out.println(str + " = '" + encoder.encode(str) + "'");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
