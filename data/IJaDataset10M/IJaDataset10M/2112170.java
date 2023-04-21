package de.spieleck.app.jacson;

import java.io.Reader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.PipedWriter;
import java.io.PipedReader;
import java.io.PrintWriter;
import de.spieleck.config.ConfigNode;
import de.spieleck.app.jacson.source.LineChunkSource;

/**
 * Wrap a Jacson into a {@link java.io.FilterReader}.
 *
 * @author fsn
 */
public class JacsonReader extends FilterReader {

    /** XXX Message of PipedReader */
    public static final String WRITE_END_DEAD = "Write end dead";

    public static final String PIPE_BROKEN = "Pipe broken";

    /** Embedded worker Jacson */
    protected Jacson jacson;

    /** The state used by this Jacson */
    protected JacsonState rootState;

    /** The embedded reader */
    protected Reader reader;

    /** The piped writer Jacson writes into */
    protected PipedWriter pWriter;

    /** The name of the configuration in filesystem */
    protected String configName;

    /** 
     * Create a new Reader with a prescribed configuration.
     *
     * @param reader The reader providing Jacson input.
     */
    public JacsonReader(Reader reader) throws JacsonConfigException, IOException {
        super(new PipedReader());
        pWriter = new PipedWriter();
        pWriter.connect((PipedReader) in);
        this.reader = reader;
    }

    /**
     * Create a new reader with prescribed input and configuration
     *
     * @param reader The reader providing Jacson input.
     * @param configName Name of Jacson configuration file 
     */
    public JacsonReader(Reader reader, String configName) throws JacsonConfigException, IOException {
        this(reader);
        setConfig(configName);
    }

    /**
     * Store the name of the used Jacson configuration.
     *
     * @param configName Name of Jacson configuration file 
     */
    public void setConfig(String configName) {
        this.configName = configName;
    }

    /**
     * Setup the Jacson to actually proceed. 
     * This spawns a thread in which the Jacson works to
     * feed the PipedReader from behind with output data.
     */
    protected void setupJacson() throws IOException {
        rootState = obtainRootState();
        try {
            ConfigNode config = Jacson.obtainConfig(configName, rootState);
            jacson = new Jacson(config, rootState);
            jacson.getReport().setPrintWriter(new PrintWriter(pWriter));
        } catch (JacsonException e) {
            throw new IOException(e.getClass() + ": " + e.getMessage());
        }
        runJacson();
    }

    /**
     * Factory method for root state.
     * Override in subclasses.
     */
    protected JacsonState obtainRootState() {
        return new JacsonState();
    }

    protected void runJacson() {
        final LineChunkSource lcs = new LineChunkSource();
        lcs.setReader(reader);
        Thread th = new Thread(new Runnable() {

            public void run() {
                try {
                    jacson.run(lcs);
                    jacson.summary();
                    pWriter.flush();
                    pWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        th.setDaemon(true);
        th.start();
    }

    /**
     * Read a single character.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public int read() throws IOException {
        if (jacson == null) setupJacson();
        try {
            return in.read();
        } catch (IOException io) {
            if (isIgnorableException(io)) return -1;
            throw io;
        }
    }

    /**
     * Read characters into a portion of an array.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public int read(char cbuf[], int off, int len) throws IOException {
        if (jacson == null) setupJacson();
        try {
            return in.read(cbuf, off, len);
        } catch (IOException io) {
            if (isIgnorableException(io)) return -1;
            throw io;
        }
    }

    /**
     * Skip characters.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public long skip(long n) throws IOException {
        if (jacson == null) setupJacson();
        return in.skip(n);
    }

    /**
     * Tell whether this stream is ready to be read.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public boolean ready() throws IOException {
        if (jacson == null) setupJacson();
        try {
            return in.ready();
        } catch (IOException io) {
            if (isIgnorableException(io)) return false;
            throw io;
        }
    }

    /**
     * Tell whether this stream supports the mark() operation.
     */
    public boolean markSupported() {
        return in.markSupported();
    }

    /**
     * Mark the present position in the stream.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void mark(int readAheadLimit) throws IOException {
        in.mark(readAheadLimit);
    }

    /**
     * Reset the stream.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }

    /**
     * Close the stream.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void close() throws IOException {
        pWriter.close();
    }

    /**
     * Check for pipe exceptions which are allowed in my context.
     * XXX This depends on the actual message in the exception and
     * might depend on JDK.
     * XXX Maybe the JDK Pipe"Pair" is not what we need!
     * @param io The Exception to inspect.
     */
    protected static boolean isIgnorableException(IOException io) {
        String msg = io.getMessage();
        return WRITE_END_DEAD.equals(msg) || PIPE_BROKEN.equals(msg);
    }
}
