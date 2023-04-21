package net.sourceforge.javagg.dataio.newdataio.xml.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface StreamProvider {

    public InputStream getInputStream() throws IOException;

    public OutputStream getOutputStream() throws IOException;

    /**
     * Returns wether or not {@#getInputStream} would return
     * a stream with no data in it.
     * @return <code>true</code> iff a subsequent call to
     * {@#getInputStream} would return a stream with at least one
     * availalbe byte or throw an IOException 
     * @throws IOException iff an I/O error occurs
     */
    public boolean isInputAvailable() throws IOException;
}
