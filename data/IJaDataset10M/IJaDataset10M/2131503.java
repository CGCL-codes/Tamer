package org.opennms.protocols.sftp;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * The class for handling SFTP+3GPP URL Connection.
 * 
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public class Sftp3gppUrlHandler extends URLStreamHandler {

    /** The Constant PROTOCOL. */
    public static final String PROTOCOL = "sftp.3gpp";

    @Override
    protected int getDefaultPort() {
        return 22;
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new Sftp3gppUrlConnection(url);
    }
}
