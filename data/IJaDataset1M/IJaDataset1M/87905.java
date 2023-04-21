package org.nightlabs.wstunnel.local.socket;

import java.io.IOException;
import java.io.InputStream;
import org.nightlabs.wstunnel.client.jaxws.IOException_Exception;
import org.nightlabs.wstunnel.client.jaxws.UnknownSessionException_Exception;
import org.nightlabs.wstunnel.local.ClientSession;

/**
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class WSTunnelInputStream extends InputStream {

    private ClientSession session;

    /**
	 * Create a new WSTunnelInputStream instance.
	 * @param session
	 */
    public WSTunnelInputStream(ClientSession session) {
        super();
        this.session = session;
    }

    @Override
    public int read() throws IOException {
        byte[] b = new byte[1];
        int len = read(b);
        if (len == -1) return -1; else return b[0];
    }

    @Override
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        byte[] result;
        try {
            result = session.getConnection().read(session.getSessionId(), len);
        } catch (IOException_Exception e) {
            throw new IOException("Server side IOException: " + e.getMessage(), e);
        } catch (UnknownSessionException_Exception e) {
            throw new IllegalStateException("Unknown session", e);
        }
        if (result == null) return -1;
        System.arraycopy(result, 0, b, off, result.length);
        return result.length;
    }
}
