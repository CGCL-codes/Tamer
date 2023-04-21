package org.owasp.jxt;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * EncoderTest
 *
 * @author Jeffrey Ichnowski
 * @version $Revision: 8 $
 */
public class EncoderTest {

    @Test
    public void testEscapeJava() {
        assertEquals("abc", Encoder.JAVA_STRING.apply("abc"));
        assertEquals("\\\"", Encoder.JAVA_STRING.apply("\""));
    }

    @Test
    public void testEscapeXML() {
        assertEquals("abc", Encoder.XML.apply("abc"));
        assertEquals("&lt;&gt;&amp;&#39;&#34;", Encoder.XML.apply("<>&\'\""));
    }

    @Test
    public void testEscapeXMLRemoveInvalidChars() {
        assertEquals("A\rB\nC\tD E", Encoder.XML.apply("A\rB\nC\tD E"));
        assertEquals("A B C", Encoder.XML.apply("AB￾C"));
        assertEquals("X Y Z", Encoder.XML.apply("X?Y?Z"));
        assertEquals("HS ", Encoder.XML.apply("HS?"));
        char[] spair = new char[2];
        int n = Character.toChars(0x10000, spair, 0);
        assertEquals(2, n);
        assertEquals(new String(spair), Encoder.XML.apply(new String(spair)));
    }

    @Test
    public void testEscapeXMLContentRemoveInvalidChars() {
        assertEquals("A\rB\nC\tD E", Encoder.XML_CONTENT.apply("A\rB\nC\tD E"));
        assertEquals("A B C", Encoder.XML_CONTENT.apply("AB￾C"));
        assertEquals("X Y Z", Encoder.XML_CONTENT.apply("X?Y?Z"));
        assertEquals("HS ", Encoder.XML_CONTENT.apply("HS?"));
        char[] spair = new char[2];
        int n = Character.toChars(0x10000, spair, 0);
        assertEquals(2, n);
        assertEquals(new String(spair), Encoder.XML_CONTENT.apply(new String(spair)));
    }

    @Test
    public void testExceptionMask() {
        Encoder.XML.apply(new StringBuilder(), "abc");
        Encoder.XML.apply(new StringBuffer(), "abc");
        Encoder.XML.apply(new PrintWriter(new StringWriter()), "abc");
    }

    static void checkEncodeURIComponent(String expected, String input) {
        String actual = Encoder.URI_COMPONENT.apply(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeURIComponent() throws Exception {
        checkEncodeURIComponent("abc123xyz890ABC()'*~!._-", "abc123xyz890ABC()'*~!._-");
        checkEncodeURIComponent("%20%2b", " +");
        checkEncodeURIComponent("%23%26%25%3d", "#&%=");
        checkEncodeURIComponent("%c2%a0%ef%bc%aa", " Ｊ");
    }

    static void checkEncodeURI(String expected, String input) {
        String actual = Encoder.URI.apply(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodeURI() throws Exception {
        checkEncodeURI("abc123xyz890ABC()'*~!._-;,/?:@&=+$", "abc123xyz890ABC()'*~!._-;,/?:@&=+$");
        checkEncodeURI("%20+", " +");
        checkEncodeURI("#&%25=", "#&%=");
        checkEncodeURI("%c2%a0%ef%bc%aa", " Ｊ");
    }

    @Test
    public void testEncodeXHTML_URI() throws Exception {
        assertEquals("abc&amp;123=%3c%3e%20#", Encoder.XHTML_URI.apply("abc&123=<> #"));
    }

    @Test
    public void testScriptCode() throws Exception {
        assertEquals("ab< /x/.exec(z)", Encoder.SCRIPT_CODE.apply("ab</x/.exec(z)"));
    }

    @Test
    public void testSequence() throws Exception {
        assertEquals("a=%20&amp;b=%c2%a0", Encoder.XHTML_URI.apply("a= &b= "));
    }

    @Test
    public void testSequenceOfFour() throws Exception {
        final String src = "A\"B\'C\\D&E%F<G>H I\nJ";
        final String expected = "A%5c%5c%26%2334%3b" + "B%5c%5c%26%2339%3b" + "C%5c%5c%5c%5c" + "D%26amp%3b" + "E%25" + "F%26lt%3b" + "G%26gt%3b" + "H%20" + "I%5c%5cn" + "J";
        String verify = Encoder.JAVASCRIPT.apply(src);
        verify = Encoder.XML.apply(verify);
        verify = Encoder.JAVA_STRING.apply(verify);
        verify = Encoder.URI_COMPONENT.apply(verify);
        assertEquals(expected, verify);
        assertEquals(expected, Encoder.forSequence(Encoder.JAVASCRIPT, Encoder.XML, Encoder.JAVA_STRING, Encoder.URI_COMPONENT).apply(src));
    }

    @Test
    public void testXmlComment() {
        assertEquals("inside -~> outside?", Encoder.XML_COMMENT.apply("inside --> outside?"));
    }

    @Test
    public void testXmlCommentStart() {
        assertEquals("~> end?", Encoder.XML_COMMENT.apply("-> end?"));
        assertEquals("~-> end?", Encoder.XML_COMMENT.apply("--> end?"));
    }

    @Test
    public void testXmlCommentEnd() {
        assertEquals("comment ~", Encoder.XML_COMMENT.apply("comment -"));
        assertEquals("comment -~", Encoder.XML_COMMENT.apply("comment --"));
    }

    @Test
    public void testXmlCommentRemoveInvalidChars() {
        assertEquals("A B C D E ", Encoder.XML_COMMENT.apply("AB￾C?D?E?"));
    }
}
