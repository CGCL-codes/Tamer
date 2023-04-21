package com.knowgate.jcifs.https;

/**
 * A <code>URLStreamHandler</code> used to provide NTLM authentication
 * capabilities to the default HTTPS handler.  This acts as a wrapper,
 * handling authentication and passing control to the underlying
 * stream handler.
 */
public class Handler extends com.knowgate.jcifs.http.Handler {

    /**
     * The default HTTPS port (<code>443</code>).
     */
    public static final int DEFAULT_HTTPS_PORT = 443;

    /**
     * Returns the default HTTPS port.
     *
     * @return An <code>int</code> containing the default HTTPS port.
     */
    protected int getDefaultPort() {
        return DEFAULT_HTTPS_PORT;
    }
}
