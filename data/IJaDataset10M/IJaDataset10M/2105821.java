package org.icenigrid.gridsam.core.plugin.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;

/**
 * CommandListener implementation that accumulates the output/error stream data
 * as a string
 */
public class PipedCommandListener implements CommandListener {

    /**
     * the output stream
     */
    private PipedInputStream oOutputStream;

    /**
     * the error stream
     */
    private PipedInputStream oErrorStream;

    /**
     * the piped output stream
     */
    private PipedOutputStream oPipedOutputStream;

    /**
     * the piped output stream
     */
    private PipedOutputStream oPipedErrorStream;

    /**
     * default constructor
     */
    public PipedCommandListener() {
        try {
            oPipedOutputStream = new PipedOutputStream();
            oOutputStream = new PipedInputStream();
            oPipedOutputStream.connect(oOutputStream);
        } catch (IOException xEx) {
            throw new IllegalStateException("unexpected error: cannot create piped stream - " + xEx.getMessage());
        }
        try {
            oPipedErrorStream = new PipedOutputStream();
            oErrorStream = new PipedInputStream();
            oPipedErrorStream.connect(oErrorStream);
        } catch (IOException xEx) {
            throw new IllegalStateException("unexpected error: cannot create piped stream - " + xEx.getMessage());
        }
    }

    /**
     * called when output stream data is available
     * 
     * @param pBuffer
     *            the buffer that contains the output stream data
     */
    public void onOutputStreamData(ByteBuffer pBuffer) {
        try {
            if (pBuffer != null) {
                oPipedOutputStream.write(pBuffer.array(), pBuffer.position(), pBuffer.limit());
            } else {
                oPipedOutputStream.close();
            }
        } catch (Exception xEx) {
            throw new IllegalStateException("unexpected error: failed to write to piped stream - " + xEx.getMessage());
        }
    }

    /**
     * called when error stream data is available
     * 
     * @param pBuffer
     *            the buffer that contains the error stream data
     */
    public void onErrorStreamData(ByteBuffer pBuffer) {
        try {
            if (pBuffer != null) {
                oPipedErrorStream.write(pBuffer.array(), pBuffer.position(), pBuffer.limit());
            } else {
                oPipedErrorStream.close();
            }
        } catch (Exception xEx) {
            throw new IllegalStateException("unexpected error: failed to write to piped stream - " + xEx.getMessage());
        }
    }

    /**
     * get an InputStream to read data written to the standard output
     * 
     * @return InputStream stream to read standard output
     */
    public InputStream getOutputStream() {
        return oOutputStream;
    }

    /**
     * get an InputStream to read data written to the standard error
     * 
     * @return InputStream stream to read standard error
     */
    public InputStream getErrorStream() {
        return oErrorStream;
    }
}
