package org.allcolor.yahp.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 * @version v0.94
 */
public class CByteArrayUrlConnection extends URLConnection {

    /** DOCUMENT ME! */
    private InputStream in;

    public CByteArrayUrlConnection(final URL url, byte[] in) {
        super(url);
        this.in = new ByteArrayInputStream(in);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws IOException DOCUMENT ME!
	 */
    public InputStream getInputStream() throws IOException {
        return in;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @throws IOException DOCUMENT ME!
	 */
    public void connect() throws IOException {
    }
}
