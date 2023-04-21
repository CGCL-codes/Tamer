package nz.co.invensysenergy.test.com.dalsemi;

import java.io.*;
import com.dalsemi.shell.server.SystemInputStream;
import nz.co.invensysenergy.test.*;
import nz.co.invensysenergy.test.comm.DataBuffer;

/**
 * This is an abstract superclass intended to test the stream behaviour implemented
 * in SystemInputStream- itself an abstract superclass. The tests run by calling
 * the abstract method {@link #constructStream}, which returns an instance of the
 * class to be tested. The behaviour tested here is a semi-random collection of
 * behaviours which have been broken in various releases of the TINI OS.
 */
public abstract class SystemInputStreamTest extends TestSuite {

    public static final byte[] BASE = "This is a test\r\n*".getBytes();

    public static final byte[] BASE_RESULT = "This is a test\r\n".getBytes();

    public static final byte[] BACKSPACED_SOURCE = "One three\b\b\b\b\btwo\r\n*".getBytes();

    public static final byte[] BACKSPACED_RESULT = "One two\r\n".getBytes();

    public static final byte[] BACKSPACED_ECHO = "One three\b \b\b \b\b \b\b \b\b \btwo\r\n*".getBytes();

    public static final byte[] BACKSPACED_KEYSTROKE_ECHO = "One three\b \b\b \b\b \b\b \b\b \btwo\r\n".getBytes();

    public static final int BACKSPACE_POS = 10;

    static final byte[] INITIAL_BACKSPACE_SOURCE = "\b\bHi\bo\r*".getBytes();

    static final byte[] INITIAL_BACKSPACE_RESULT = "Ho\r".getBytes();

    static final byte[] INITIAL_BACKSPACE_ECHO = "Hi\b \bo\r\n*".getBytes();

    static final byte[] INITIAL_BACKSPACE_KEYSTROKE_ECHO = "Hi\b \bo\r\n".getBytes();

    static final byte[] INITIAL_BACKSPACE_WITH_RAW_READ_ECHO = "nHi\b \bo\r\n*".getBytes();

    static final byte[] OVER_BACKSPACING_SOURCE = "Hi\b\b\bThere\n*".getBytes();

    static final byte[] OVER_BACKSPACING_RESULT = "There\n".getBytes();

    static final byte[] OVER_BACKSPACING_ECHO = "Hi\b \b\b \bThere\n*".getBytes();

    static final byte[] OVER_BACKSPACING_KEYSTROKE_ECHO = "Hi\b \b\b \bThere\n".getBytes();

    static final byte[] BACKSPACING_TO_NOTHING_SOURCE = "help\b\b\b\b\b\b\b\r\n*".getBytes();

    static final byte[] BACKSPACING_TO_NOTHING_RESULT = "\r\n".getBytes();

    static final byte[] BACKSPACING_TO_NOTHING_ECHO = "help\b \b\b \b\b \b\b \b\r\n*".getBytes();

    static final byte[] BACKSPACING_TO_NOTHING_KEYSTROKE_ECHO = "help\b \b\b \b\b \b\b \b\r\n".getBytes();

    static final byte[] POS_ONE_BACKSPACE_SOURCE = "H\bWho?".getBytes();

    static final byte[] POS_ONE_BACKSPACE_RESULT = "Who?".getBytes();

    static final byte[] POS_ONE_BACKSPACE_ECHO = "H\b \bWho?".getBytes();

    static final byte[] NO_ECHO = new byte[0];

    static final int INTERCHARACTER_TIME = 100;

    private static final String UP_ARROW = "\033\117\101";

    private static final String DOWN_ARROW = "\033\117\102";

    private static final String LEFT_ARROW = "\033\117\104";

    private static final String RIGHT_ARROW = "\033\117\103";

    private static final byte[] LEFT_AND_RIGHT_ARROWS_SOURCE = ("One" + RIGHT_ARROW + "Three" + LEFT_ARROW + LEFT_ARROW + LEFT_ARROW + "ere\r\n").getBytes();

    private static final byte[] LEFT_AND_RIGHT_ARROWS_RESULT = "One There\r\n".getBytes();

    private static final byte[] LEFT_AND_RIGHT_ARROWS_ECHO = "One Three\b \b\b \b\b \bere\r\n".getBytes();

    private static final byte[] UP_AND_DOWN_ARROWS_SOURCE = ("One" + UP_ARROW + "Two" + DOWN_ARROW + "3\r\n").getBytes();

    private static final byte[] UP_AND_DOWN_ARROWS_RESULT = "OneTwo3\r\n".getBytes();

    /**
   * Adds all the core SystemInputStream tests to the test suite.
   */
    public SystemInputStreamTest() {
        addTest(new TestBaseRead());
        addTest(new TestBlockingRead());
        addTest(new TestBackspacedRead());
        addTest(new TestInitialBackspaceRead());
        addTest(new TestOverBackspacingRead());
        addTest(new TestPosOneBackspaceRead());
        addTest(new TestLeftAndRightArrowKeys());
        addTest(new TestUpAndDownArrowKeys());
        addTest(new TestKeystrokeEntry());
        addTest(new TestNonBlockingKeystrokes());
        addTest(new TestInitialBackspaceKeystrokes());
        addTest(new TestOverBackspacingKeystrokes());
        addTest(new TestNoEcho());
        addTest(new TestBackspacingToNothing());
        addTest(new TestBackspacingToNothingKeystrokes());
        addTest(new TestReadAndReadLine());
        addTest(new TestBackspaceEchoAfterRawMode());
        addTest(new TestReadBufferInRawMode());
    }

    /**
   * To inherit from this test suite, supply a method that calls the constructor
   * for the exact subtype of SystemInputStream that is under test.
   *
   * @param in The core stream that the SystemInputStream will supplement.
   * @param out The stream used for echoing input.
   * @return A concrete subclass of SystemInputStream.
   */
    abstract SystemInputStream constructStream(InputStream in, PrintStream out);

    /**
   * This test ensures a standard boring string can be read using a
   * SerialInputStream. This string contains no special characters and it
   * terminated by CR LF.
   */
    class TestBaseRead extends TestCase {

        protected final byte[] TxData = getTestString();

        protected final byte[] RxData = getExpectedResult();

        protected final byte[] EchoData = getExpectedEcho();

        /**
     * @return The data to be sent to the stream under test
     */
        protected byte[] getTestString() {
            return BASE;
        }

        /**
     * @return The data expected to be read from the stream under test
     */
        protected byte[] getExpectedResult() {
            return BASE_RESULT;
        }

        /**
     * @return The data expected to be echoed.
     */
        protected byte[] getExpectedEcho() {
            return getTestString();
        }

        /**
     * Runs the test.
     *
     * @throws IOException If the test fails.
     */
        protected void runTest() throws IOException {
            PipedInputStream echoPipeIn = new PipedInputStream();
            PipedOutputStream echoPipeOut = new PipedOutputStream(echoPipeIn);
            PrintStream echoOut = new PrintStream(echoPipeOut);
            PipedInputStream sourcePipeIn = new PipedInputStream();
            PipedOutputStream sourcePipeOut = new PipedOutputStream(sourcePipeIn);
            SystemInputStream in = constructStream(sourcePipeIn, echoOut);
            try {
                doAnyTricks(in, sourcePipeOut);
                sendTestData(TxData, sourcePipeOut);
                DataBuffer result = blockUntilDataAdded(in);
                DataBuffer echo = new DataBuffer(EchoData.length + 5);
                echo.add(echoPipeIn);
                check(result.equals(RxData), result.reportComparison(RxData));
                check(echo.equals(EchoData), echo.reportComparison(EchoData));
            } finally {
                sourcePipeIn.close();
                sourcePipeOut.close();
                echoPipeIn.close();
                echoPipeOut.close();
                in.close();
            }
        }

        /**
     * Writes the test data.
     */
        protected void sendTestData(byte[] data, OutputStream out) throws IOException {
            out.write(TxData);
        }

        /**
     * A Template Method allowing derived tests to include special behaviour.
     */
        protected void doAnyTricks(SystemInputStream in, OutputStream out) throws IOException {
        }

        protected DataBuffer blockUntilDataAdded(InputStream in) throws IOException {
            DataBuffer result = new DataBuffer(in.available());
            result.add(in);
            return result;
        }
    }

    /**
   * This test ensure a string containing backspaces cam be interpreted and
   * echoed correctly.
   */
    class TestBackspacedRead extends TestBaseRead {

        protected byte[] getTestString() {
            return BACKSPACED_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return BACKSPACED_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return BACKSPACED_ECHO;
        }
    }

    /**
   * This test ensures that backspaces at the start of a string are ignored.
   * Also confirms that there is nothing special about just one backspace. Also
   * confirms that an /r is echoed at /r/n (if not following /n anyway).
   */
    class TestInitialBackspaceRead extends TestBaseRead {

        protected byte[] getTestString() {
            return INITIAL_BACKSPACE_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return INITIAL_BACKSPACE_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return INITIAL_BACKSPACE_ECHO;
        }
    }

    /**
   * This test ensures you can backspace to the start of the string and beyond
   * correctly. Also tests that read works without having first called
   * available().
   */
    class TestOverBackspacingRead extends TestBaseRead {

        protected byte[] getTestString() {
            return OVER_BACKSPACING_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return OVER_BACKSPACING_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return OVER_BACKSPACING_ECHO;
        }

        protected DataBuffer blockUntilDataAdded(InputStream in) throws IOException {
            DataBuffer result = new DataBuffer(TxData.length + 5);
            result.addBlocking(in);
            return result;
        }
    }

    /**
   * Just in case there is anything special about a backspace after one
   * character, this test checks for that. Also this string is terminated by
   * stream closure rather than a linefeed. That means that available() returns
   * zero until an actual read attempt is made. This is a fundamental limitation
   * of the Java streaming model since there is no way to learning the stream
   * has closed without chancing an uninterruptable blocking call.
   */
    class TestPosOneBackspaceRead extends TestBaseRead {

        protected byte[] getTestString() {
            return POS_ONE_BACKSPACE_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return POS_ONE_BACKSPACE_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return POS_ONE_BACKSPACE_ECHO;
        }

        /**
     * This string is not terminated by \c\n so need to close the stream.
     */
        protected void sendTestData(byte[] data, OutputStream out) throws IOException {
            super.sendTestData(data, out);
            out.close();
        }

        protected DataBuffer blockUntilDataAdded(InputStream in) throws IOException {
            DataBuffer result = new DataBuffer(TxData.length + 5);
            result.addBlocking(in);
            return result;
        }
    }

    /**
   * The KeystrokeSimulator sends keystrokes from a source string into a pipe
   * one at a time
   */
    class KeystrokeSimulator extends Thread {

        private byte[] data;

        private OutputStream pipe;

        public KeystrokeSimulator(byte[] theData, OutputStream thePipe) {
            data = theData;
            pipe = thePipe;
        }

        public KeystrokeSimulator(byte theChar, OutputStream thePipe) {
            data = new byte[1];
            data[0] = theChar;
            pipe = thePipe;
        }

        /**
     * Main processing method for the KeystrokeSimulator object
     */
        public void run() {
            try {
                for (int thisByte = 0; thisByte < data.length; thisByte++) {
                    sleep(INTERCHARACTER_TIME);
                    pipe.write(data[thisByte]);
                    if ((data[thisByte] == '\r') && (thisByte < data.length - 1) && (data[thisByte + 1] == '\n')) {
                        pipe.write(data[++thisByte]);
                    }
                    if ((data[thisByte] == '\r') || (data[thisByte] == '\n')) {
                        break;
                    }
                }
            } catch (Exception burn) {
            }
        }
    }

    /**
   * The way the stream processes keystrokes is different to streams travelling
   * at full blast. This test simulated keystroke entry with backspaces.
   */
    class TestKeystrokeEntry extends TestBackspacedRead {

        protected byte[] getExpectedEcho() {
            return BACKSPACED_KEYSTROKE_ECHO;
        }

        protected void sendTestData(byte[] data, OutputStream out) {
            new KeystrokeSimulator(data, out).start();
        }

        protected DataBuffer blockUntilDataAdded(InputStream in) throws IOException {
            DataBuffer result = new DataBuffer(TxData.length + 5);
            result.addBlocking(in);
            return result;
        }
    }

    /**
   * This test checks that the strategy of calling available() and then read()
   * works with a SerialInputStream (rather than just blocking on read()). This
   * simulates event driven serial comms.
   */
    class TestNonBlockingKeystrokes extends TestKeystrokeEntry {

        protected DataBuffer blockUntilDataAdded(InputStream in) throws IOException {
            while (in.available() == 0) {
                try {
                    Thread.sleep(INTERCHARACTER_TIME / 2);
                } catch (InterruptedException burn) {
                }
            }
            DataBuffer result = new DataBuffer(in.available());
            result.add(in);
            return result;
        }
    }

    /**
   * This test confirms that the keystroke code handles initial backspaces and
   * also only a \r at the end.
   */
    class TestInitialBackspaceKeystrokes extends TestKeystrokeEntry {

        protected byte[] getTestString() {
            return INITIAL_BACKSPACE_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return INITIAL_BACKSPACE_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return INITIAL_BACKSPACE_KEYSTROKE_ECHO;
        }
    }

    /**
   * This test confirms the keystroke code handles over backspacing and also
   * only a \n at the end.
   */
    class TestOverBackspacingKeystrokes extends TestKeystrokeEntry {

        protected byte[] getTestString() {
            return OVER_BACKSPACING_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return OVER_BACKSPACING_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return OVER_BACKSPACING_KEYSTROKE_ECHO;
        }
    }

    /**
   * Tests that characters are not echoed when echo is off.
   */
    class TestNoEcho extends TestBaseRead {

        protected byte[] getExpectedEcho() {
            return NO_ECHO;
        }

        protected void doAnyTricks(SystemInputStream in, OutputStream out) {
            in.setEcho(false);
        }
    }

    /**
   * Tests a command line can be backspaced to nothing (and beyond) without any
   * ill effects.
   */
    class TestBackspacingToNothing extends TestOverBackspacingRead {

        protected byte[] getTestString() {
            return BACKSPACING_TO_NOTHING_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return BACKSPACING_TO_NOTHING_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return BACKSPACING_TO_NOTHING_ECHO;
        }
    }

    /**
   * Tests a command line can be backspaced to nothing and beyond with simulated
   * non-blocking keystrokes.
   */
    class TestBackspacingToNothingKeystrokes extends TestNonBlockingKeystrokes {

        protected byte[] getTestString() {
            return BACKSPACING_TO_NOTHING_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return BACKSPACING_TO_NOTHING_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return BACKSPACING_TO_NOTHING_KEYSTROKE_ECHO;
        }
    }

    /**
   * Tests that you can alternate between read() and readLine() (which I would
   * have thought should have very similar functionality in non-raw mode).
   * Farshad has found that calling read, readLine, read, readLine causes an
   * ArrayIndexOutOfBounds exception in SystemInputStream.read (apparently due
   * to a negative value for count_available).
   */
    class TestReadAndReadLine extends TestCase {

        protected void runTest() throws IOException {
            PipedInputStream echoPipeIn = new PipedInputStream();
            PipedOutputStream echoPipeOut = new PipedOutputStream(echoPipeIn);
            PrintStream echoOut = new PrintStream(echoPipeOut);
            PipedInputStream sourcePipeIn = new PipedInputStream();
            PipedOutputStream sourcePipeOut = new PipedOutputStream(sourcePipeIn);
            SystemInputStream in = constructStream(sourcePipeIn, echoOut);
            final byte[][] txData = { { 72, 101, 108, 108, 111, 13, 10 }, { 111, 13, 10 }, { 84, 104, 101, 114, 101, 13, 10 }, { 111, 13, 10 } };
            byte[] buffer = new byte[10];
            sourcePipeOut.write(txData[0]);
            int count = in.read(buffer);
            assertEquals(txData[0], buffer, count);
            sourcePipeOut.write(txData[1]);
            String result = in.readLine();
            assertEquals(txData[1], txData[1].length - 2, result.getBytes(), result.length());
            sourcePipeOut.write(txData[2]);
            count = in.read(buffer);
            assertEquals(txData[2], buffer, count);
            sourcePipeOut.write(txData[3]);
            result = in.readLine();
            assertEquals(txData[3], txData[1].length - 2, result.getBytes(), result.length());
            sourcePipeIn.close();
            sourcePipeOut.close();
            echoPipeIn.close();
            echoPipeOut.close();
            in.close();
        }
    }

    /**
   * Tests that backspace works correctly after rawMode has been used. In
   * particular, once rawMode is set to false, the input should be considered to
   * be at the start of the line (and so backspaces should be ignored). This is
   * because raw input is read as characters, and each character can be acted on
   * and probably include some feedback so when raw mode is switched off,
   * everything should start afresh. This is backed up by the result from the
   * read() call not making any reference to characters read in raw mode, even
   * though they may not be seperated from the non-raw characters by a carriage
   * return.<p>
   *
   * This is a bug in TINI OS 1.02b.
   */
    class TestBackspaceEchoAfterRawMode extends TestBaseRead {

        protected byte[] getTestString() {
            return INITIAL_BACKSPACE_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return INITIAL_BACKSPACE_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return INITIAL_BACKSPACE_WITH_RAW_READ_ECHO;
        }

        protected void doAnyTricks(final SystemInputStream in, final OutputStream out) throws IOException {
            final int CH = 110;
            in.setRawMode(true);
            new KeystrokeSimulator((byte) CH, out).start();
            int ch = readCharacter(in);
            assertEquals(CH, ch);
            in.setRawMode(false);
        }

        protected int readCharacter(SystemInputStream in) throws IOException {
            return in.read();
        }
    }

    /**
   * This test is just like TestBackspaceEchoAfterRawMode but it makes sure that
   * read(byte[]) blocks in raw mode. According to the API documentation
   * read(byte[]) should return -1 for EOF, or throw an exception but should
   * otherwise block until data is available. In TINI 1.02b, this test fails.
   */
    class TestReadBufferInRawMode extends TestBackspaceEchoAfterRawMode {

        protected int readCharacter(SystemInputStream in) throws IOException {
            byte[] buffer = new byte[10];
            int count = in.read(buffer);
            check(count == 1, "readCharacter found count=" + count);
            return buffer[0];
        }
    }

    /**
   * I have some tests that pass for an EventBasedSerialDevice but which fail
   * for a StreamBasedSerialDevice. This leads me to suspect that the blocking
   * behaviour when reading a SystemInputStream might be different to the
   * non-blocking behaviour (when available() is not checked first). Hence this
   * test varies the base read by blocking on the read.
   */
    class TestBlockingRead extends TestBaseRead {

        protected void sendTestData(final byte[] data, final OutputStream out) throws IOException {
            new Thread() {

                public void run() {
                    try {
                        sleep(500);
                        out.write(data);
                        sleep(1000);
                    } catch (Exception e) {
                        System.err.println("Exception when attempting sendTestData in TestBlockingRead: " + e);
                    }
                }
            }.start();
        }

        protected DataBuffer blockUntilDataAdded(InputStream in) throws IOException {
            final DataBuffer result = new DataBuffer(20);
            final byte[] buffer = new byte[20];
            try {
                int count = in.read(buffer);
                if (count > 0) {
                    result.add(buffer, count);
                }
                Thread.sleep(500);
            } catch (IOException e) {
                System.err.println(this + ": IOException on read: " + e);
            } catch (InterruptedException burn) {
            }
            return result;
        }
    }

    /**
   * Tests that left and right arrow keys are converted as expected.
   */
    private class TestLeftAndRightArrowKeys extends TestBaseRead {

        protected byte[] getTestString() {
            return LEFT_AND_RIGHT_ARROWS_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return LEFT_AND_RIGHT_ARROWS_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return LEFT_AND_RIGHT_ARROWS_ECHO;
        }
    }

    /**
   * Tests that up and down arrow keys are ignored.
   */
    private class TestUpAndDownArrowKeys extends TestBaseRead {

        protected byte[] getTestString() {
            return UP_AND_DOWN_ARROWS_SOURCE;
        }

        protected byte[] getExpectedResult() {
            return UP_AND_DOWN_ARROWS_RESULT;
        }

        protected byte[] getExpectedEcho() {
            return getExpectedResult();
        }
    }
}
