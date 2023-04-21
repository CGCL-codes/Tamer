package net.sf.amemailchecker.mail.header;

import net.sf.amemailchecker.mail.MimeConstants;
import net.sf.amemailchecker.mail.impl.letter.LetterImpl;
import net.sf.amemailchecker.mail.impl.letter.RawMessagePart;
import net.sf.amemailchecker.mail.parser.MailMessageParser;
import org.junit.Before;
import org.junit.Test;
import java.util.Calendar;
import java.util.GregorianCalendar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateHeaderParseTest {

    private MailMessageParser parser;

    @Before
    public void before() {
        parser = new MailMessageParser();
    }

    @Test
    public void test_Parse() {
        Calendar calendar = test_Parse("Thu, 7 Apr 2011 07:57:09 +0000");
        assertEquals(5, calendar.get(Calendar.DAY_OF_WEEK));
        calendar = test_Parse("7 Apr 2011 01:20:51 -0500");
        assertEquals(3, calendar.get(Calendar.MONTH));
    }

    @Test
    public void test_ParseAbsent() {
        LetterImpl letter = new LetterImpl(new RawMessagePart());
        parser.parseDateHeader(letter);
        assertNotNull(letter.getDate());
    }

    private Calendar test_Parse(String header) {
        LetterImpl letter = new LetterImpl(new RawMessagePart());
        letter.getRawMessage().getHeaders().put(MimeConstants.HEADER_DATE, header);
        parser.parseDateHeader(letter);
        assertNotNull(letter.getDate());
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(letter.getDate());
        return calendar;
    }
}
