package mil.army.usace.ehlschlaeger.rgik.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

/**
 * Reader that with more useful behaviors for plain text files. Includes the
 * buffering and line number behaviors of LineNumberReader, and reading past end
 * of file will throw an exception.
 * <p>
 * Copyright <a href="http://faculty.wiu.edu/CR-Ehlschlaeger2/">Charles R.
 * Ehlschlaeger</a>, work: 309-298-1841, fax: 309-298-3003, This software is
 * freely usable for research and educational purposes. Contact C. R.
 * Ehlschlaeger for permission for other purposes. Use of this software requires
 * appropriate citation in all published and unpublished documentation.
 * 
 * @author William R. Zwicky
 */
public class MyReader extends Reader {

    private LineNumberReader source;

    private File myfile;

    /**
     * Forbidden!
     */
    @SuppressWarnings("unused")
    private MyReader() {
    }

    /**
     * Open a file for reading.
     * @param filename
     * @throws IOException 
     */
    public MyReader(File filename) throws IOException {
        myfile = filename.getAbsoluteFile();
        source = new LineNumberReader(new FileReader(myfile));
        source.setLineNumber(1);
    }

    /**
     * @param filename
     * @throws IOException 
     */
    public MyReader(String filename) throws IOException {
        this(new File(filename));
    }

    /**
     * Close the file and release its resources.
     * @see java.io.Reader#close()
     */
    @Override
    public void close() throws IOException {
        source.close();
    }

    /**
     * Tells whether this stream supports the mark() operation, which it does.
     * @return true
     * @see java.io.Reader#markSupported()
     */
    @Override
    public boolean markSupported() {
        return source.markSupported();
    }

    /**
     * Mark the present position in the stream. Subsequent calls to reset() will
     * attempt to reposition the stream to this point, and will also reset the
     * line number appropriately.
     * 
     * @see java.io.Reader#mark(int)
     */
    @Override
    public void mark(int readAheadLimit) throws IOException {
        source.mark(readAheadLimit);
    }

    /**
     * Resets the stream to the most recent mark.
     * @see java.io.Reader#reset()
     */
    @Override
    public void reset() throws IOException {
        source.reset();
    }

    /**
     * Skips over characters without reading them.
     * @see java.io.Reader#skip(long)
     */
    @Override
    public long skip(long n) throws IOException {
        return source.skip(n);
    }

    /**
     * Tells whether this stream is ready to be read. A buffered character
     * stream is ready if the buffer is not empty, or if the underlying
     * character stream is ready.
     * 
     * @see java.io.Reader#ready()
     */
    @Override
    public boolean ready() throws IOException {
        return source.ready();
    }

    /**
     * Reads a single character. 
     * @see java.io.Reader#read()
     */
    @Override
    public int read() throws IOException {
        int ch = source.read();
        if (ch < 0) throw new IOException("Read past end of file " + myfile);
        return ch;
    }

    /**
     * Reads characters into a portion of an array.
     * @see java.io.Reader#read(char[], int, int)
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int actual = source.read(cbuf, off, len);
        if (actual < 0) throw new IOException("Read past end of file " + myfile);
        return actual;
    }

    /**
     * Read a line of text.
     * Reads past end of file are converted into exceptions. Exceptions are
     * wrapped with another exception that includes the file name in the
     * message.
     * 
     * @throws IOException
     *             on any error.
     */
    public String readLine() throws IOException {
        try {
            String s = source.readLine();
            if (s == null) throw new IOException("Read past end of file " + myfile);
            return s;
        } catch (IOException ioe) {
            throw new IOException("Error reading file: " + myfile, ioe);
        }
    }

    /**
     * Get the current line number.
     * The first line of the file is considered "1".
     * 
     * @return the current line number.
     */
    public int getLineNumber() {
        return source.getLineNumber();
    }
}
