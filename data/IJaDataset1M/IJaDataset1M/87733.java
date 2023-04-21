package org.oclc.da.common.bytestream.impl;

import java.io.IOException;
import java.io.InputStream;
import org.oclc.da.common.bytestream.BytestreamLoader;
import org.oclc.da.common.bytestream.Sink;
import org.oclc.da.exceptions.DAExceptionCodes;
import org.oclc.da.exceptions.DASystemException;
import org.oclc.da.logging.Logger;

/** This class implements content loading from streams. Any input stream can
 * be fed into this class.
 * @author stanesca
 */
public class StreamedBytestreamLoader implements BytestreamLoader {

    private static int BLOCK_SIZE = 64 * 1024;

    private String location;

    private InputStream stream;

    private Sink sink;

    private long size;

    private boolean done;

    /** Logger instance. */
    private Logger logger = Logger.newInstance();

    /** Constructor. Note that the input stream will be closed by the loader.
     * @param is The input stream containing the data. 
     * @param loc The location of the data. See <code>location()</code>.
     */
    public StreamedBytestreamLoader(InputStream is, String loc) {
        location = loc;
        stream = is;
        size = -1;
    }

    /** Constructor. Note that the input stream will be closed by the loader.
     * @param is The input stream containing the data. 
     * @param loc The location of the data. See <code>location()</code>.
     * @param size The size of the data, if it can be reliably obtained without
     * reading the content, for example the value <code>File.size()</code>.
     */
    public StreamedBytestreamLoader(InputStream is, String loc, long size) {
        location = loc;
        stream = is;
        this.size = (size < 0) ? -1 : size;
    }

    /**
     * @see BytestreamLoader#load()
     */
    public void load() {
        if (done) return;
        try {
            for (done = false; !done; ) {
                byte[] bytes = new byte[BLOCK_SIZE];
                int count = stream.read(bytes);
                if (count < 0) {
                    done = true;
                } else if (count > 0) {
                    byte[] bytesRead = new byte[count];
                    System.arraycopy(bytes, 0, bytesRead, 0, count);
                    sink.addBlock(bytesRead);
                }
            }
            sink.loaded();
        } catch (IOException e) {
            logger.log(DAExceptionCodes.ERROR_WRITING, Logger.WARN, this, "load", "IOException", new DASystemException(DAExceptionCodes.IO_ERROR, new String[] { location, "request", "later" }));
            sink.failed(new DASystemException(DAExceptionCodes.MUST_COMPLETE, new String[] { "load " + location }));
            done = true;
        }
        try {
            stream.close();
        } catch (IOException e1) {
        }
        return;
    }

    /**
     * @see BytestreamLoader#getLocation()
     */
    public String getLocation() {
        return location;
    }

    /**
     * BytestreamLoader#setSink(org.oclc.da.common.Sink)
     * @param sink 
     * @return Success or failure
     */
    public boolean setSink(Sink sink) {
        if (this.sink == null) {
            this.sink = sink;
            return true;
        } else return false;
    }

    /**
     * @see org.oclc.da.common.bytestream.BytestreamLoader#getSize()
     */
    public long getSize() {
        return size;
    }

    /**
     * @see org.oclc.da.common.bytestream.BytestreamLoader#isFinished()
     */
    public boolean isFinished() {
        return done;
    }
}
