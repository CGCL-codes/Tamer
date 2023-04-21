package net.sf.refactorit.source.edit;

import org.apache.log4j.Category;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LineReader extends Reader {

    private char buffer[];

    private int numChars = 0, nextCharIndex = 0;

    /**
   * Create a buffering character-input stream that uses an input buffer of
   * the default size.
   *
   * @param reader reader
   */
    public LineReader(Reader reader) {
        super(reader);
        buffer = new char[8192];
    }

    /**
   * Create a buffering character-input stream that uses an input buffer of
   * the default size.
   *
   * @param reader reader
   */
    public LineReader(Reader reader, int bufferSize) {
        super(reader);
        buffer = new char[bufferSize];
    }

    /**
   * Fill the input buffer.
   */
    private void fill() throws IOException {
        int n;
        do {
            n = ((Reader) lock).read(buffer, 0, buffer.length);
        } while (n == 0);
        if (n > 0) {
            numChars = n;
            nextCharIndex = 0;
        }
    }

    /**
   * Read a line of text.  A line is considered to be terminated by any one
   * of a line feed ('\n'), a carriage return ('\r'), or a carriage return
   * followed immediately by a linefeed.
   *
   * @return     A String containing the contents of the line, including
   *             any line-termination characters, or null if the end of the
   *             stream has been reached
   *
   * @exception  IOException  If an I/O error occurs
   */
    public String readLine() throws IOException {
        StringBuffer stringBuffer = null;
        int startChar;
        boolean eolStarted = false;
        synchronized (lock) {
            while (true) {
                if (nextCharIndex >= numChars) {
                    fill();
                }
                if (nextCharIndex >= numChars) {
                    if (stringBuffer != null && stringBuffer.length() > 0) {
                        return stringBuffer.toString();
                    } else {
                        return null;
                    }
                }
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer(80);
                }
                startChar = nextCharIndex;
                boolean eol = false;
                for (; nextCharIndex < numChars; nextCharIndex++) {
                    if (buffer[nextCharIndex] == '\r') {
                        if (!eolStarted) {
                            eolStarted = true;
                            continue;
                        }
                    }
                    if (buffer[nextCharIndex] == '\n') {
                        nextCharIndex++;
                        eol = true;
                        break;
                    }
                    if (eolStarted) {
                        eol = true;
                        break;
                    }
                }
                stringBuffer.append(buffer, startChar, nextCharIndex - startChar);
                if (eol) {
                    return stringBuffer.toString();
                }
            }
        }
    }

    public int read(char cbuf[], int off, int len) throws IOException {
        synchronized (lock) {
            if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            int n = read1(cbuf, off, len);
            if (n <= 0) {
                return n;
            }
            while ((n < len) && ((Reader) lock).ready()) {
                int n1 = read1(cbuf, off + n, len - n);
                if (n1 <= 0) {
                    break;
                }
                n += n1;
            }
            return n;
        }
    }

    private int read1(char[] cbuf, int off, int len) throws IOException {
        if (nextCharIndex >= numChars) {
            if (len >= buffer.length) {
                return ((Reader) lock).read(cbuf, off, len);
            }
            fill();
        }
        if (nextCharIndex >= numChars) {
            return -1;
        }
        int n = Math.min(len, numChars - nextCharIndex);
        System.arraycopy(buffer, nextCharIndex, cbuf, off, n);
        nextCharIndex += n;
        return n;
    }

    /**
   * Close the stream.
   *
   * @exception  IOException  If an I/O error occurs
   */
    public void close() throws IOException {
        synchronized (lock) {
            if (lock == null) {
                return;
            }
            ((Reader) lock).close();
            lock = null;
            buffer = null;
        }
    }

    /**
   * Test driver for {@link LineReader}.
   */
    public static final class TestDriver extends TestCase {

        /** Logger instance. */
        private static final Category cat = Category.getInstance(TestDriver.class.getName());

        public TestDriver(String name) {
            super(name);
        }

        public static Test suite() {
            final TestSuite suite = new TestSuite(TestDriver.class);
            suite.setName("LineReader tests");
            return suite;
        }

        /**
     * Tests that buffered reader reads lines correctly.
     */
        public void testGenericReadLine() throws Exception {
            cat.info("Testing that buffered reader reads lines correctly.");
            LineReader reader = new LineReader(new StringReader("first line\r\nsecond line\nthird line\rfourth line\r\rthe end"));
            assertEquals("read 1st line", "first line\r\n", readLine(reader));
            assertEquals("read 2nd line", "second line\n", readLine(reader));
            assertEquals("read 3rd line", "third line\r", readLine(reader));
            assertEquals("read 4th line", "fourth line\r", readLine(reader));
            assertEquals("read 5th line", "\r", readLine(reader));
            assertEquals("read 6th line", "the end", readLine(reader));
            reader = new LineReader(new StringReader("first line\r\nsecond line\nthird line\rfourth line\r\rthe end"), 1);
            assertEquals("read 1st line (bufsize 1)", "first line\r\n", readLine(reader));
            assertEquals("read 2nd line (bufsize 1)", "second line\n", readLine(reader));
            assertEquals("read 3rd line (bufsize 1)", "third line\r", readLine(reader));
            assertEquals("read 4th line (bufsize 1)", "fourth line\r", readLine(reader));
            assertEquals("read 5th line (bufsize 1)", "\r", readLine(reader));
            assertEquals("read 6th line (bufsize 1)", "the end", readLine(reader));
            reader = new LineReader(new StringReader("\r"));
            assertEquals("read single \\r line", "\r", readLine(reader));
            reader = new LineReader(new StringReader("\n"));
            assertEquals("read single \\n line", "\n", readLine(reader));
            reader = new LineReader(new StringReader("\r\n"));
            assertEquals("read single \\r\\n line", "\r\n", readLine(reader));
            reader = new LineReader(new StringReader(""));
            assertEquals("read empty stream", null, reader.readLine());
            cat.info("SUCCESS");
        }

        private String readLine(Reader in) throws IOException {
            String result;
            if (in instanceof LineReader) {
                result = ((LineReader) in).readLine();
            } else {
                char[] buf = new char[8192];
                int len = in.read(buf);
                result = new String(buf, 0, len);
            }
            return result;
        }
    }
}
