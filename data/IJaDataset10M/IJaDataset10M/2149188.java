package net.sf.atmodem4j.core.parser;

import java.io.ByteArrayInputStream;
import net.sf.atmodem4j.core.Modem;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.sf.atmodem4j.core.parser.ParserMatcher.eqResultCodeToken;
import static net.sf.atmodem4j.core.parser.ParserMatcher.eqParser;

/**
 *
 * @author aploese
 */
public class ParserTest {

    private Modem modem;

    public ParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRING() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("RING");
        sb.append("\r\n");
        ByteArrayInputStream is = new ByteArrayInputStream(sb.toString().getBytes());
        Parser parser = new Parser(false);
        parser.setLineChars('\r', '\n');
        IMocksControl ctl = EasyMock.createStrictControl();
        modem = ctl.createMock("modem", Modem.class);
        parser.setModem(modem);
        modem.parsedRing(parser);
        EasyMock.replay(modem);
        parser.setInputStream(is);
        Thread.sleep(200);
        EasyMock.verify(modem);
    }

    @Test
    public void testAT() throws Exception {
        doTestCommand("AT", "OK");
    }

    @Test
    public void testS0() throws Exception {
        doTestCommand("ATS0?", "OK", "000");
    }

    @Test
    public void testATD() throws Exception {
        doTestCommand("ATD **", "CONNECT");
    }

    @Test
    public void testATDText() throws Exception {
    }

    @Test
    public void testATGMR() throws Exception {
        doTestCommand("AT+GMR", "OK", "+GMR: \"Version 1.87 / 31.07.2000\"");
    }

    @Test
    public void testPrompt() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("AT+CMGS=6\r\r\n>0000" + Modem.EOF + "\r\nOK\r\n");
        ByteArrayInputStream is = new ByteArrayInputStream(sb.toString().getBytes());
        Parser parser = new Parser(false);
        parser.setLineChars('\r', '\n');
        IMocksControl ctl = EasyMock.createStrictControl();
        modem = ctl.createMock("modem", Modem.class);
        parser.setModem(modem);
        modem.parsedPrompt(parser);
        modem.parsedResultCode(eqParser(parser), eqResultCodeToken(new ResultCodeToken("OK", new String[] { "0000" + Modem.EOF }, "AT+CMGS=6")));
        EasyMock.replay(modem);
        parser.setInputStream(is);
        Thread.sleep(200);
        EasyMock.verify(modem);
    }

    private void doTestCommand(String command, String resultCode, String... data) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(command).append("\r\r\n");
        for (String s : data) {
            sb.append(s).append("\r\n");
        }
        sb.append(resultCode).append("\r\n");
        ByteArrayInputStream is = new ByteArrayInputStream(sb.toString().getBytes());
        Parser parser = new Parser(false);
        parser.setLineChars('\r', '\n');
        IMocksControl ctl = EasyMock.createStrictControl();
        modem = ctl.createMock("modem", Modem.class);
        parser.setModem(modem);
        modem.parsedResultCode(eqParser(parser), eqResultCodeToken(new ResultCodeToken(resultCode, data, command)));
        EasyMock.replay(modem);
        parser.setInputStream(is);
        Thread.sleep(200);
        EasyMock.verify(modem);
    }
}
