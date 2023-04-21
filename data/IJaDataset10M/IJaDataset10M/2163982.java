package de.tfh.pdvl.hp.protocol;

import junit.framework.TestCase;

/**
 * @author s717689
 *
 */
public class MessageTest extends TestCase {

    MessageParser parser;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MessageTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        parser = new MessageParser();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        parser = null;
    }

    /**
     * Constructor for MessageTest.
     * @param name
     */
    public MessageTest(String name) {
        super(name);
    }

    public void testQueryMessage() throws MessageParserException {
        String s = "query";
        Message m = new QueryMessage();
        assertEquals(s, m.toString());
    }

    public void testInfoMessage1() throws MessageParserException {
        String s = "info, 2500, square, 0, 0.3";
        InfoMessage m = new InfoMessage();
        m.setAmplitude(0.3);
        m.setFrequency(2500);
        m.setOffset(0);
        m.setWaveShape(WaveShape.SQUARE);
        assertEquals(m.toString(), s);
    }

    public void testSetMessage() {
        String s = "set, 1000, sine, 0.5, 2";
        SetMessage m = new SetMessage();
        m.setFrequency(1000);
        m.setWaveShape(WaveShape.SINE);
        m.setOffset(0.5);
        m.setAmplitude(2);
        assertEquals(s, m.toString());
    }

    public void testInfoMessage5() {
        String s = "info, 1000, sine, 0.5, 2";
        InfoMessage m = new InfoMessage();
        m.setFrequency(1000);
        m.setWaveShape(WaveShape.SINE);
        m.setOffset(0.5);
        m.setAmplitude(2);
        assertEquals(s, m.toString());
    }

    public void testInfoMessage6() {
        String s = "info, 1000, sine, -0.5, 2";
        InfoMessage m = new InfoMessage();
        m.setFrequency(1000);
        m.setWaveShape(WaveShape.SINE);
        m.setOffset(-0.5);
        m.setAmplitude(2);
        assertEquals(s, m.toString());
    }

    public void testErroMessage() {
        String s = "error, 2, invalid waveShape";
        ErrorMessage m = new ErrorMessage();
        m.setErrorCode(2);
        m.setErrorText("invalid waveShape");
        assertEquals(s, m.toString());
    }
}
