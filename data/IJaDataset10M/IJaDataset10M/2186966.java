package net.sf.amemailchecker.mail.composer;

import net.sf.amemailchecker.mail.MimeConstants;
import net.sf.amemailchecker.mail.TestUtil;
import net.sf.amemailchecker.mail.impl.letter.LetterImpl;
import net.sf.amemailchecker.mail.impl.letter.RawMessagePart;
import net.sf.amemailchecker.mail.parser.MailMessageParser;
import net.sf.amemailchecker.mail.parser.MailMessageResolver;
import net.sf.amemailchecker.mail.parser.composer.StringHeaderComposer;
import org.junit.Before;
import org.junit.Test;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComposeStringHeaderTest {

    private String NON_ASCII_VAL_1;

    @Before
    public void before() throws Exception {
        Properties properties = TestUtil.load(getClass());
        NON_ASCII_VAL_1 = properties.getProperty("non.ascii.val.1");
    }

    @Test
    public void test_ComposeSubjectHeader() {
        String headerValue = "net.sf.amemailchecker.mail.parser.composer.StringHeaderComposer" + "net.sf.amemailchecker.mail.parser.composer.StringHeaderComposer";
        composeSubjectHeader(headerValue);
    }

    @Test
    public void test_ComposeSubjectHeaderShort() {
        String headerValue = "net.sf.";
        composeSubjectHeader(headerValue);
    }

    @Test
    public void test_ComposeSubjectHeaderEncoded() {
        composeSubjectHeader(NON_ASCII_VAL_1);
    }

    private void composeSubjectHeader(String headerValue) {
        StringHeaderComposer composer = new StringHeaderComposer();
        composer.reuseFor(MimeConstants.HEADER_SUBJECT, headerValue);
        LetterImpl letter = new LetterImpl(new RawMessagePart());
        MailMessageResolver resolver = new MailMessageResolver();
        String next;
        while ((next = composer.readNextToken()) != null) {
            assertTrue(next.length() <= 76);
            resolver.resolveHeader(next, MimeConstants.HEADER_SUBJECT, letter.getRawMessage());
        }
        MailMessageParser parser = new MailMessageParser();
        parser.parseSubjectHeader(letter);
        assertEquals(headerValue, letter.getSubject());
    }
}
