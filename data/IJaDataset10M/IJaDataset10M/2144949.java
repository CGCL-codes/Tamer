package com.bluebrim.postscript.impl.shared;

import java.io.*;
import com.bluebrim.base.shared.debug.*;

/**
 * Simple extension to PrintWriter which ensures a specified line break sequence regardless
 * of platform. Stupidly enough, the PrintWriter has no way to specify this. (The line break
 * sequence is determined by the current value of a global system property at the time
 * of creation of the PrintWriter.)
 *
 * Also, this class always uses ISO 8859-1 character encoding for char to byte
 * conversion to be fully platform independent. Finally, binary (byte) output
 * capabilities like that of PrintStream has been added.  /Markus
 *
 * Note: the correctness of this class dependes on whether the println(*) methods
 * actually call println() or not. According to Markus P, the documentation of
 * PrintWriter guarantees that this actually will be the case. /Magnus
 *
 * <p><b>Creation date:</b> 2001-06-18
 * <br><b>Documentation last updated:</b> 2001-10-31
 *
 * @author Magnus Ihse (magnus.ihse@appeal.se)
 * @author Markus Persson 2001-07-27
 */
public class CoPostscriptWriter extends PrintWriter {

    protected OutputStream m_outStream;

    /**
 * @param out the destination stream. It is recommended that it be buffered.
 * @author Markus Persson 2001-07-27
 */
    public CoPostscriptWriter(OutputStream out) {
        super(createBufferedWriterTo(out));
        m_outStream = out;
    }

    public void println() {
        print("\n");
    }

    /**
 * Need a method to be able to catch exceptions ...
 * @author Markus Persson 2001-07-27
 */
    private static Writer createBufferedWriterTo(OutputStream stream) {
        try {
            return new BufferedWriter(new OutputStreamWriter(stream, CoPostscriptUtil.ISO_LATIN_1_CONVERTER_KEY));
        } catch (UnsupportedEncodingException uee) {
            throw new CoAssertionFailedException("Encoding " + CoPostscriptUtil.ISO_LATIN_1_CONVERTER_KEY + " not supported!");
        }
    }

    /**
 * Retrieve OutputStream for sending successive chunks of binary data.
 * Use it to efficiently output several small chunks and/or if you need
 * the OutputStream protocol.
 *
 * NOTE: Mixing calls to the recieved stream and calls to this writer
 * is not permitted. Following a call to the writer, the stream must be
 * reobtained using this method before calling the stream again. For
 * maximum implementation independence however, it is recommended that
 * the write(byte[] ...) methods of this writer is used to write your
 * binary data is such mixed scenarios.
 *
 * PENDING: This method should perhaps be named something like
 * getFlushedToOutputStream(),
 *
 * @author Markus Persson 2001-07-27
 */
    public OutputStream getFlushedOutputStream() {
        flush();
        return m_outStream;
    }

    public void write(byte[] buf) {
        try {
            getFlushedOutputStream().write(buf);
        } catch (IOException ioe) {
            setError();
        }
    }

    /**
 * Write <code>len</code> bytes from the specified byte array starting at
 * offset <code>off</code> to this stream.
 *
 * @param  buf   A byte array
 * @param  off   Offset from which to start taking bytes
 * @param  len   Number of bytes to write
 */
    public void write(byte[] buf, int off, int len) {
        try {
            getFlushedOutputStream().write(buf, off, len);
        } catch (IOException ioe) {
            setError();
        }
    }

    public void writeln(byte[] buf) {
        synchronized (lock) {
            write(buf);
            println();
        }
    }

    /**
 * Write <code>len</code> bytes from the specified byte array starting at
 * offset <code>off</code> to this stream, followed by a newline. If
 * automatic flushing is enabled the stream will be flushed.
 *
 * @param  buf   A byte array
 * @param  off   Offset from which to start taking bytes
 * @param  len   Number of bytes to write
 */
    public void writeln(byte[] buf, int off, int len) {
        synchronized (lock) {
            write(buf, off, len);
            println();
        }
    }
}
