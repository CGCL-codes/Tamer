package jhomenet.shell.server;

import java.io.*;

/**
 * This is an implementation of an <code>InputStream</code> for use as <code>System.in</code>.
 * Since the underlying root <code>InputStream</code> for a TINI program can come from
 * a serial port, a socket, or any other kind of input, the differing behaviors of these
 * input devices must be reconciled by this class.  This class must ensure that input
 * becomes available only after an end of line sequence is received, and that the amount
 * available does not represent any 'inaccessible' data (if the data has not been followed
 * by an end of line character).  It must also handle '\r', '\n', and '\r\n' as end of 
 * line sequences.
 *
 * @author Stephen Hess, Lorne Smith, Kris Ardis
 * @version 1.0
 *
 * @see com.dalsemi.shell.server.SystemPrintStream
 * @see com.dalsemi.shell.server.Session
 */
public class SystemInputStream extends InputStream {

    private boolean terrible_trouble = false;

    /**
    * The user session that owns this <code>SystemInputStream</code>.  
    */
    protected Session session;

    /**
    * The underlying <code>InputStream</code> that this 
    * <code<SystemPrintStream</code> reads from.
    */
    protected InputStream root;

    /**
    * The 'echo' stream.  If <code>ECHO</code> is <code>true</code>, 
    * this <code>PrintStream</code> will write everything it reads.  This is useful
    * in a telnet session, for instance, when a user types something and 
    * it needs to be echoed back to the telnet client.
    *
    * @see #ECHO
    */
    protected PrintStream out;

    /**
    * Indicates that the <code>SystemInputStream</code> should return
    * data as soon as it is available, and not wait until an 
    * end of line sequence is received.
    *
    * @see #rawRead()
    * @see #rawRead(byte[],int,int)
    */
    protected boolean rawMode;

    /**
    * The internal buffer that holds all the data that has been received
    * and not yet read by an application.
    */
    protected byte[] buffer;

    /**
    * The starting position in the internal buffer where
    * valid data is located.
    */
    protected int startPos;

    /**
    * The ending position in the internal buffer
    * where valid data ends.  This is the next
    * available offset for new data into the
    * internal buffer.
    */
    protected int endPos;

    /**
    * If this <code>SystemInputStream</code> reads from a file,
    * this represents the name of the file.
    */
    public String fileInName = null;

    /**
    * Indicates whether or not to echo the character just read.
    *
    * @see #out
    */
    protected boolean ECHO = true;

    /**
    * Backspace and delete defines
    */
    protected static final int ASCII_BS = 0x08;

    protected static final int ASCII_DEL = 0x7F;

    protected static final int ASCII_SPACE = 0x20;

    /**
    * Byte sequence for a backspace.  (Arrow back, overwrite
    * next character, then arrow back again.)
    */
    protected static final byte[] ERASE = { (byte) ASCII_BS, (byte) ASCII_SPACE, (byte) ASCII_BS };

    private int count_available;

    private int THRESHHOLD;

    private static final int BUFFER_LENGTH = 50;

    /**
    * Creates a new <code>SystemPrintStream</code> with the
    * specified underlying root stream and echo stream.
    *
    * @param  in  underlying InputStream to use
    * @param  out stream for echo characters
    */
    public SystemInputStream(InputStream in, PrintStream out) {
        this.out = out;
        root = in;
        buffer = new byte[BUFFER_LENGTH];
        startPos = 0;
        endPos = 0;
        count_available = 0;
        THRESHHOLD = buffer.length >> 2;
        rawMode = false;
    }

    /**
    * Creates a new <code>SystemInputStream</code> with the
    * specified underlying root stream and echo stream. Calling
    * this constructor signifies that the underlying 
    * <code>InputStream</code> is reading from a file.
    *
    * @param  in  underlying InputStream to use
    * @param  out stream for echo characters
    * @param  fileInName name of the file this stream is reading from
    */
    public SystemInputStream(InputStream in, PrintStream out, String fileInName) {
        this(in, out);
        this.fileInName = fileInName;
    }

    /**
    * Turns on and off echoing back characters read by this stream.
    * This is useful if a session is reading a password, it will not 
    * be echoed back to the session.
    *
    * @param echo <code>true</code> to enable echoing, <code>false</code>
    * to disable it
    *
    * @see #setEchoStream(java.io.PrintStream)
    */
    public synchronized void setEcho(boolean echo) {
        ECHO = echo;
    }

    /**
    * Returns state of read character echo.
    *
    * @return echo status
    *
    * @see #setEchoStream(java.io.PrintStream)
    * @see #setEcho(boolean)
    */
    public synchronized boolean getEcho() {
        return ECHO;
    }

    /**
    * Sets the <code>PrintStream</code> to echo back characters read by this stream.
    * Note: you must call <code>setEcho(true)</code> to enable echoing.
    *
    * @param  echo stream to echo back characters read
    *
    * @see #setEcho(boolean)
    */
    public synchronized void setEchoStream(PrintStream echo) {
        out = echo;
    }

    /**
    * Informs this stream of its owning session.  This allows this stream
    * to call into the session when needed.
    *
    * @param  session  the owning session
    */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
    * This method should be overridden by subclassing <code>InputStream</code>s.
    * It should only be called from within a <code>synchronized</code> block.  It blocks until
    * data is available and returns the first value it reads.  It does not wait
    * for an end of line sequence.
    *
    * @return the value read from the underlying stream
    *
    * @throws IOException if an error occurs reading from the underlying stream.
    *
    * @see #rawRead(byte[],int,int)
    * @see #rawAvailable()
    * @see #setRawMode(boolean)
    */
    protected int rawRead() throws IOException {
        int c = root.read();
        if ((ECHO) && (out != null)) {
            if (c != -1) out.print((char) c);
        }
        return c;
    }

    /**
    * <p>Reads from the underlying stream and returns
    * data even if an end of line sequence is not received.  If
    * not operating in raw reading mode, it also checks and handles
    * backspace events.  Stops if it finds an end of stream.</p>
    * 
    * <p>This method should only be called from within a <code>synchronized</code> block.</p>
    *
    * @return number of bytes read
    *
    * @throws IOException if an error occurs reading from the underlying stream
    *
    * @see #rawRead()
    * @see #setRawMode(boolean)
    * @see #rawAvailable()
    */
    protected int rawRead(byte[] buff, int off, int len) throws IOException {
        int ch;
        int i = 0;
        for (i = 0; i < len; i++) {
            ch = rawRead();
            if (ch == -1) {
                if (i == 0) return -1;
                return i;
            }
            buff[i + off] = (byte) ch;
            if (rawAvailable() == 0) len = i + 1;
        }
        return len;
    }

    /**
    * This method should be overridden by subclassing <code>InputStream</code>s.
    * It returns the amount available from the underlying root stream.  
    * This is not the amount available if the <code>SystemInputStream</code>
    * is not in raw mode.
    *
    * @return amount of data available directly from the underlying stream
    *
    * @throws IOException if an error occurs in the underlying stream.
    *
    * @see #setRawMode(boolean)
    * @see #rawRead()
    * @see #rawRead(byte[],int,int)
    */
    protected int rawAvailable() throws IOException {
        return root.available();
    }

    byte[] temp = new byte[0];

    private synchronized void readLineFill() throws IOException {
        if (startPos > THRESHHOLD) {
            int diff = startPos;
            for (int i = 0; i < endPos - startPos; i++) buffer[i] = buffer[i + startPos];
            endPos -= diff;
            startPos = 0;
        }
        int amount = rawAvailable();
        while (amount > 0) {
            if (temp.length < amount) temp = new byte[amount];
            amount = rawRead(temp, 0, amount);
            if (amount == -1) throw new IOException();
            increaseBuffer(amount);
            System.arraycopy(temp, 0, buffer, endPos, amount);
            endPos += amount;
            amount = rawAvailable();
        }
        if (!rawMode) fixBackspaces();
        count_available = 0;
        for (int i = startPos; i < endPos; i++) {
            if ((buffer[i] == '\r') || (buffer[i] == '\n')) {
                count_available = i - startPos + 1;
            }
        }
    }

    private void fixBackspaces() {
        int i = 0;
        int len = endPos - startPos;
        while (i < len) {
            if (i == 0) while (((buffer[startPos] == ASCII_BS) || buffer[startPos] == ASCII_DEL) && (startPos < endPos)) {
                len--;
                startPos++;
            }
            if (i < len) {
                int ch = buffer[i + startPos];
                if (((ch == ASCII_BS) || (ch == ASCII_DEL)) && (i < len)) {
                    for (int j = i - 1; j < len - 2; j++) {
                        buffer[startPos + j] = buffer[startPos + j + 2];
                    }
                    i -= 2;
                    len -= 2;
                }
                i++;
            }
        }
        endPos = startPos + len;
    }

    private synchronized int blockUntilEndOfLine() throws IOException {
        while (true) {
            int ch = rawRead();
            if (ch != -1) {
                increaseBuffer(1);
                buffer[endPos] = (byte) ch;
                endPos++;
            }
            if ((ch == ASCII_BS) || (ch == ASCII_DEL)) {
                if (endPos > startPos + 1) endPos -= 2; else endPos--;
            }
            if ((ch == -1) || (ch == '\n')) {
                count_available = endPos - startPos;
                return endPos;
            }
            if (ch == '\r') {
                if (rawAvailable() > 0) {
                    ch = rawRead();
                    if (ch != -1) {
                        increaseBuffer(1);
                        buffer[endPos++] = (byte) ch;
                    }
                    if (ch != '\n') {
                        count_available = endPos - startPos - 1;
                        return endPos;
                    }
                }
                count_available = endPos - startPos;
                return endPos;
            }
        }
    }

    /**
    * Reads a line of text up to but not including the end of line sequence.
    * This method will wait for more input if an end of line sequence or the 
    * end of the stream cannot be found.  This method recognizes '\r', '\n',
    * and '\r\n' and end of line sequences.  If the end of the stream is 
    * reached, null is returned.  Blank lines are returned as empty strings
    * (a <code>String</code> of length 0).
    *
    * @return next line of text available from the underlying <code>
    * InputStream</code>, or <code>null</code> if no more input will be
    * available due to the end of the stream
    * 
    * @see #read()
    * @see #read(byte[],int,int)
    */
    public synchronized String readLine() {
        String ret = null;
        try {
            readLineFill();
            int i = startPos;
            int lineIndex = -1;
            while ((lineIndex == -1) && (i < endPos)) {
                if (buffer[i] == '\n') lineIndex = i; else if (buffer[i] == '\r') {
                    if ((i + 1 < endPos) && (buffer[i + 1] == '\n')) lineIndex = i + 1; else lineIndex = i;
                }
                i++;
            }
            if (lineIndex == -1) lineIndex = blockUntilEndOfLine() - 1;
            if (lineIndex < 0) return "";
            int temp_lineIndex = lineIndex;
            while ((temp_lineIndex >= 0) && ((buffer[temp_lineIndex] == '\r') || (buffer[temp_lineIndex] == '\n'))) {
                temp_lineIndex--;
            }
            if (temp_lineIndex <= startPos - 1) ret = ""; else ret = new String(buffer, startPos, temp_lineIndex - startPos + 1);
            count_available -= (lineIndex - startPos + 1);
            startPos = lineIndex + 1;
        } catch (IOException ioe) {
            terrible_trouble = true;
            return null;
        }
        return ret;
    }

    /**
    * Reads the next character from the stream.  If raw mode
    * is not enabled, this method will only return data that
    * has been followed by an end of line sequence.
    *
    * @return the next character from the stream, or
    * <code>-1</code> if the end of the stream has been
    * reached
    *
    * @throws IOException if an error occurs in the underlying stream
    *
    * @see #setRawMode(boolean)
    * @see #readLine()
    * @see #read(byte[],int,int)
    */
    public final synchronized int read() throws IOException {
        int ret = 0;
        if (rawMode) {
            if (endPos > startPos) {
                ret = buffer[startPos];
                startPos++;
                count_available--;
                return ret;
            } else return rawRead();
        } else {
            if (count_available == 0) {
                readLineFill();
                if (count_available == 0) blockUntilEndOfLine();
                if (count_available == 0) return -1;
            }
            ret = buffer[startPos] & 0x0ff;
            startPos++;
            count_available--;
            return ret;
        }
    }

    /**
    * Tries to read <code>len</code> bytes from the stream.
    * If the end of stream is encountered, no more data
    * will be read.  If raw mode is not enabled, only 
    * data that has been followed by an end of line 
    * sequence or the end of the stream will be reported.  
    * This method will block until enough data is available 
    * to return or the end of the stream is  reached.
    *
    * @param buff byte array to store the data read
    * @param off  offset to begin storing data in the array
    * @param len  amount of data requested
    *
    * @return  the number of characters read, or
    * <code>-1</code> if the end of the stream has been
    * reached
    *
    * @throws IOException if an error occurs in the underlying stream
    *
    * @see #read()
    * @see #readLine()
    * @see #setRawMode(boolean)
    */
    public synchronized int read(byte[] buff, int off, int len) throws IOException {
        int ret = 0;
        if (len == 0) return 0;
        if (off + len > buff.length) len = buff.length - off;
        if (rawMode) {
            int non_raw = (endPos - startPos) > len ? len : endPos - startPos;
            System.arraycopy(buffer, startPos, buff, off, non_raw);
            startPos += non_raw;
            count_available -= non_raw;
            if ((non_raw != len)) {
                byte[] temp = new byte[len - non_raw];
                int temp_read = rawRead(temp, 0, temp.length);
                if (temp_read == -1) return -1;
                System.arraycopy(temp, 0, buff, off + non_raw, temp.length);
                non_raw += temp_read;
            }
            return non_raw;
        } else {
            while (count_available == 0) {
                readLineFill();
                if (count_available == 0) blockUntilEndOfLine();
                if (count_available == 0) return -1;
            }
            len = Math.min(count_available, len - off);
            System.arraycopy(buffer, startPos, buff, off, len);
            startPos += len;
            count_available -= len;
            return len;
        }
    }

    /**
    * Ensures the internal buffer is large enough 
    * for more data.  This method should only be
    * called from within a <code>synchronized</code> block.
    * 
    * @param newDataSize the amount of new data
    * that is about to be copied into the buffer
    * at location <code>endPos</code>
    *
    */
    protected void increaseBuffer(int newDataSize) {
        if (endPos + newDataSize > buffer.length) {
            byte[] temp = new byte[buffer.length + BUFFER_LENGTH + newDataSize];
            System.arraycopy(buffer, 0, temp, 0, buffer.length);
            THRESHHOLD = temp.length >> 2;
            buffer = temp;
        }
    }

    /**
    * Returns the number of bytes that can be read from this input
    * stream without blocking.  This is equivalent to the number
    * of bytes that have been followed by end of line sequences (or
    * by the end of the stream), including the end of line sequences.
    *
    * @return the number of bytes that can be read from this input stream
    * without blocking
    *
    * @throws IOException if an error occurs in the underlying stream
    *
    * @see #setRawMode(boolean)
    */
    public final synchronized int available() throws IOException {
        readLineFill();
        if (rawMode) {
            return root.available() + (endPos - startPos);
        } else return count_available;
    }

    /**
    * Sets the mode for reading data from the underlying stream.
    * If raw mode is set to <code>true</code>, any call to read data will
    * return the next available data from the underlying stream.
    * If raw mode is <code>false</code>, read calls only report data
    * that has been followed by an end of line character, or the 
    * end of the stream, even if the read must block to do so.
    *
    * @param  rawMode <code>false</code> to block for an end of line
    * sequence, <code>true</code> to return any available data
    *
    * @see #rawRead()
    * @see #rawRead(byte[],int,int)
    * @see #rawAvailable()
    * @see #read()
    * @see #read(byte[],int,int)
    * @see #available()
    */
    public void setRawMode(boolean rawMode) {
        synchronized (this) {
            this.rawMode = rawMode;
        }
    }

    /**
    * Sets the underlying <code>InputStream</code> to use.
    *
    * @param  newIn new root <code>InputStream</code> to use
    *
    * @see #getRootStream()
    */
    public void setRootStream(InputStream newIn) {
        synchronized (this) {
            if (newIn == null) root = null; else root = newIn;
        }
    }

    /**
    * Returns the underlying root <code>InputStream</code> of this stream.
    *
    * @return the root stream
    *
    * @see #setRootStream(java.io.InputStream)
    */
    public InputStream getRootStream() {
        return root;
    }

    public boolean errorOccurred() {
        return terrible_trouble;
    }
}
