package org.crazydays.gameplan.db.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * XMLEncoderOutputStreamWrapper
 */
public class XMLEncoderOutputStreamWrapper extends FilterOutputStream {

    /**
     * XMLEncoderOutputStreamWrapper constructor.
     * 
     * @param outputStream OutputStream
     */
    public XMLEncoderOutputStreamWrapper(OutputStream outputStream) {
        super(outputStream);
    }

    /**
     * Close does not actually close to prevent XMLEncoder from closing the
     * input stream.
     * 
     * @throws IOException
     * @see java.io.FilterOutputStream#close()
     */
    public void close() throws IOException {
    }
}
