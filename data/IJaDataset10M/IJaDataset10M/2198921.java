package au.edu.uq.itee.maenad.util;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A wrapper for Reader instances that can add characters before and after the
 * original stream.
 *
 * This class can be used to create Reader instances that wrap existing readers
 * and have an additional prologue (characters read before the original reader)
 * and epilogue (characters read after the original reader).
 */
public class WrappingReader extends Reader {

    /**
     * The wrapper Reader instance.
     */
    private final Reader originalReader;

    /**
     * The characters to provide before the original content.
     */
    private final char[] prologue;

    /**
     * The characters to provide after the original content.
     */
    private final char[] epilogue;

    /**
     * Our current position in our own stream.
     */
    private int pos = 0;

    /**
     * The position of the first epilogue character we sent.
     *
     * This is set to -1 until we actually have sent such character.
     */
    private int firstEpilogueChar = -1;

    /**
     * Flag if the reader has been closed.
     *
     * This is used to fail faster and with better error messages if the reader
     * is not used properly.
     */
    private boolean closed = false;

    /**
     * Wraps a reader instance around the provided reader using the given data.
     *
     * @param originalReader The original reader to wrap.
     * @param prologue The characters to send before the original reader's content.
     * @param epilogue The characters to send after the original reader's content.
     * @param lock The Java object to lock upon.
     */
    public WrappingReader(Reader originalReader, char[] prologue, char[] epilogue, Object lock) {
        super(lock);
        this.originalReader = originalReader;
        this.prologue = prologue;
        this.epilogue = epilogue;
    }

    /**
     * Wraps a reader instance around the provided reader using the given data.
     *
     * @param originalReader The original reader to wrap.
     * @param prologue The characters to send before the original reader's content.
     * @param epilogue The characters to send after the original reader's content.
     */
    public WrappingReader(Reader originalReader, char[] prologue, char[] epilogue) {
        this.originalReader = originalReader;
        this.prologue = prologue;
        this.epilogue = epilogue;
    }

    /**
     * Wraps a reader instance around the provided reader using the given data.
     *
     * @param originalReader The original reader to wrap.
     * @param prologue The characters to send before the original reader's content.
     * @param epilogue The characters to send after the original reader's content.
     * @param lock The Java object to lock upon.
     */
    public WrappingReader(Reader originalReader, String prologue, String epilogue, Object lock) {
        super(lock);
        this.originalReader = originalReader;
        this.prologue = prologue.toCharArray();
        this.epilogue = epilogue.toCharArray();
    }

    /**
     * Wraps a reader instance around the provided reader using the given data.
     *
     * @param originalReader The original reader to wrap.
     * @param prologue The characters to send before the original reader's content.
     * @param epilogue The characters to send after the original reader's content.
     */
    public WrappingReader(Reader originalReader, String prologue, String epilogue) {
        this.originalReader = originalReader;
        this.prologue = prologue.toCharArray();
        this.epilogue = epilogue.toCharArray();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Reader has been closed already");
        }
        int oldPos = pos;
        Logger.getLogger(getClass().getName()).log(Level.FINE, String.format("Reading %d characters from position %d", len, pos));
        if (pos < this.prologue.length) {
            final int toCopy = Math.min(this.prologue.length - pos, len);
            Logger.getLogger(getClass().getName()).log(Level.FINE, String.format("Copying %d characters from prologue", toCopy));
            System.arraycopy(this.prologue, pos, cbuf, off, toCopy);
            pos += toCopy;
            if (toCopy == len) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, "Copied from prologue only");
                return len;
            }
        }
        if (firstEpilogueChar == -1) {
            final int copiedSoFar = pos - oldPos;
            final int read = originalReader.read(cbuf, off + copiedSoFar, len - copiedSoFar);
            Logger.getLogger(getClass().getName()).log(Level.FINE, String.format("Got %d characters from delegate", read));
            if (read != -1) {
                pos += read;
                if (pos - oldPos == len) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, "We do not reach epilogue");
                    return len;
                }
            }
            firstEpilogueChar = pos;
        }
        final int copiedSoFar = pos - oldPos;
        final int epiloguePos = pos - firstEpilogueChar;
        final int toCopy = Math.min(this.epilogue.length - epiloguePos, len - copiedSoFar);
        if ((toCopy <= 0) && (copiedSoFar == 0)) {
            return -1;
        }
        Logger.getLogger(getClass().getName()).log(Level.FINE, String.format("Copying %d characters from epilogue", toCopy));
        System.arraycopy(this.epilogue, epiloguePos, cbuf, off + copiedSoFar, toCopy);
        pos += toCopy;
        Logger.getLogger(getClass().getName()).log(Level.FINE, String.format("Copied %d characters, now at position %d", pos - oldPos, pos));
        return pos - oldPos;
    }

    @Override
    public void close() throws IOException {
        originalReader.close();
        closed = true;
    }
}
