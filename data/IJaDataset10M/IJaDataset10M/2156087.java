package org.columba.ristretto.imap.parser;

import org.columba.ristretto.imap.IMAPHeader;
import org.columba.ristretto.imap.IMAPResponse;
import org.columba.ristretto.io.CharSequenceSource;
import org.columba.ristretto.io.Source;
import org.columba.ristretto.message.Header;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author frd
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class IMAPHeaderParserTest {

    @Test
    public void testParse() throws Exception {
        String testData = "* 23 FETCH (UID 24 BODY[HEADER.FIELDS (Subject From To Cc Date Size Message-Id In-Reply-To References Content-Type)] {0}\r\n";
        Source literal = new CharSequenceSource("Message-ID: <dhrxkxm9.yzodjv3japay@frd>\n" + "From: frederikdietz@web.de\n" + "Date: Sat, 05 Jul 03 20:07:03 CEST\n" + "To: schwutte%deswahnsinns.de@deswahnsinns.de\n" + "Subject: test-email\n" + "\n");
        IMAPResponse r = IMAPResponseParser.parse(testData);
        r.appendResponseText(")");
        r.addLiteral(literal);
        IMAPHeader imapHeader = IMAPHeaderParser.parse(r);
        Header header = imapHeader.getHeader();
        Assert.assertTrue(imapHeader.getUid().intValue() == 24);
        Assert.assertEquals(header.get("Message-Id"), "<dhrxkxm9.yzodjv3japay@frd>");
        Assert.assertEquals(header.get("From"), "frederikdietz@web.de");
        Assert.assertEquals(header.get("Date"), "Sat, 05 Jul 03 20:07:03 CEST");
        Assert.assertEquals(header.get("To"), "schwutte%deswahnsinns.de@deswahnsinns.de");
        Assert.assertEquals(header.get("Subject"), "test-email");
    }

    @Test
    public void testParse2() throws Exception {
        String testData = "* 23 FETCH (BODY[HEADER.FIELDS (Subject From To Cc Date Size Message-Id In-Reply-To References Content-Type)] {0}\r\n";
        Source literal = new CharSequenceSource("Message-ID: <dhrxkxm9.yzodjv3japay@frd>\n" + "From: frederikdietz@web.de\n" + "Date: Sat, 05 Jul 03 20:07:03 CEST\n" + "To: schwutte%deswahnsinns.de@deswahnsinns.de\n" + "Subject: test-email\n" + "\n");
        IMAPResponse r = IMAPResponseParser.parse(testData);
        r.appendResponseText("UID 24)");
        r.addLiteral(literal);
        IMAPHeader imapHeader = IMAPHeaderParser.parse(r);
        Header header = imapHeader.getHeader();
        Assert.assertTrue(imapHeader.getUid().intValue() == 24);
        Assert.assertEquals(header.get("Message-Id"), "<dhrxkxm9.yzodjv3japay@frd>");
        Assert.assertEquals(header.get("From"), "frederikdietz@web.de");
        Assert.assertEquals(header.get("Date"), "Sat, 05 Jul 03 20:07:03 CEST");
        Assert.assertEquals(header.get("To"), "schwutte%deswahnsinns.de@deswahnsinns.de");
        Assert.assertEquals(header.get("Subject"), "test-email");
    }
}
