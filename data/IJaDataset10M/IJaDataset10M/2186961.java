package net.sf.japi.net.rest.util;

import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/** Unit Test for {@link LineInputStream}.
 *
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
@SuppressWarnings({ "HardcodedLineSeparator", "IOResourceOpenedButNotSafelyClosed" })
public class LineInputStreamTest {

    /** Tests that reading CRLF-terminated lines from a LineInputStream does not discard byte data.
     * @throws IOException (unexpected)
     */
    @Test
    public void testReadLineCRLF() throws IOException {
        final byte[] testData = "Line1\r\nLine2\r\ndata".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        final String line1 = testling.readLine();
        Assert.assertEquals("Readline must return complete lines without CRLF.", "Line1", line1);
        final String line2 = testling.readLine();
        Assert.assertEquals("Readline must return complete lines without CRLF.", "Line2", line2);
        Assert.assertEquals("Readline must not read ahead.", 'd', testling.read());
        Assert.assertEquals("Readline must not read ahead.", 'a', testling.read());
        Assert.assertEquals("Readline must not read ahead.", 't', testling.read());
        Assert.assertEquals("Readline must not read ahead.", 'a', testling.read());
        Assert.assertNull("Expecting no data.", testling.readLine());
        Assert.assertEquals("Expecting end of file.", -1, testling.read());
    }

    /** Tests that reading LF-terminated lines from a LineInputStream does not discard byte data.
     * @throws IOException (unexpected)
     */
    @Test
    public void testReadLineLF() throws IOException {
        final byte[] testData = "Line1\nLine2\ndata".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        final String line1 = testling.readLine();
        Assert.assertEquals("Readline must return complete lines without LF.", "Line1", line1);
        final String line2 = testling.readLine();
        Assert.assertEquals("Readline must return complete lines without LF.", "Line2", line2);
        Assert.assertEquals("Readline must not read ahead.", 'd', testling.read());
        Assert.assertEquals("Readline must not read ahead.", 'a', testling.read());
        Assert.assertEquals("Readline must not read ahead.", 't', testling.read());
        Assert.assertEquals("Readline must not read ahead.", 'a', testling.read());
        Assert.assertNull("Expecting no data.", testling.readLine());
        Assert.assertEquals("Expecting end of file.", -1, testling.read());
    }

    @Test
    public void testReadLineEmpty() throws IOException {
        final byte[] testData = "".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertNull("Expecting no data.", testling.readLine());
        Assert.assertEquals("Expecting end of file.", -1, testling.read());
    }

    @Test
    public void testReadLineEmptyRead() throws IOException {
        final byte[] testData = "".getBytes();
        final FilterInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertEquals("Expecting end of file.", -1, testling.read());
    }

    @Test
    public void testReadLineEmptyReadLine() throws IOException {
        final byte[] testData = "".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertNull("Expecting no data.", testling.readLine());
    }

    @Test
    public void testReadLineEmptyCRLF() throws IOException {
        final byte[] testData = "\r\n".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertEquals("Expecting one empty line.", "", testling.readLine());
        Assert.assertNull("Expecting no more data.", testling.readLine());
        Assert.assertEquals("Expecting end of file.", -1, testling.read());
    }

    @Test
    public void testReadLineEmptyCRLFRead() throws IOException {
        final byte[] testData = "\r\n".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertEquals("Expecting one empty line.", "", testling.readLine());
        Assert.assertEquals("Expecting end of file.", -1, testling.read());
    }

    @Test
    public void testReadLineEmptyCRLFReadLine() throws IOException {
        final byte[] testData = "\r\n".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertEquals("Expecting one empty line.", "", testling.readLine());
        Assert.assertNull("Expecting no more data.", testling.readLine());
    }

    @Test
    public void testReadLineEmptyLF() throws IOException {
        final byte[] testData = "\n".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertEquals("Expecting one empty line.", "", testling.readLine());
        Assert.assertNull("Expecting no more data.", testling.readLine());
        Assert.assertEquals("Expecting end of file.", -1, testling.read());
    }

    @Test
    public void testReadLineEmptyLFRead() throws IOException {
        final byte[] testData = "\n".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertEquals("Expecting one empty line.", "", testling.readLine());
        Assert.assertEquals("Expecting end of file.", -1, testling.read());
    }

    @Test
    public void testReadLineEmptyLFReadLine() throws IOException {
        final byte[] testData = "\n".getBytes();
        final LineInputStream testling = new LineInputStream(new ByteArrayInputStream(testData));
        Assert.assertEquals("Expecting one empty line.", "", testling.readLine());
        Assert.assertNull("Expecting no more data.", testling.readLine());
    }
}
