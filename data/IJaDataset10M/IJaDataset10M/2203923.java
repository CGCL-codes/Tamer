package org.obe.client.api.base;

import javax.security.auth.callback.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;

/**
 * The standard JAAS callback handler for OBE. Some application servers use
 * non-standard callbacks which require a custom callback handler. For example,
 * WebLogic Server passes a URLCallback to collect the server's URL.
 *
 * @author Adrian Price
 */
public class OBECallbackHandler implements CallbackHandler {

    private static final Log _logger = LogFactory.getLog(OBECallbackHandler.class);

    private static final String JAAS = "JAAS: ";

    protected final String _url;

    protected final String _principal;

    protected final String _credentials;

    public OBECallbackHandler(String url, String principal, String credentials) {
        _url = url;
        _principal = principal;
        _credentials = credentials;
    }

    public final void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) handle(callbacks[i]);
    }

    /**
     * Handles a specific callback. Subclasses can override this method in
     * order to handle non-standard callback types. This class handles the
     * standard callback types
     * <code>javax.security.auth.callback.TextOutputCallback</code>,
     * <code>javax.security.auth.callback.NameCallback</code>,
     * <code>javax.security.auth.callback.PasswordCallback</code>.
     *
     * @param callback The callback to handle.
     * @throws IOException If an I/O error occurs.
     * @throws UnsupportedCallbackException If this CallbackHandler does not
     * support this Callback type.
     */
    protected void handle(Callback callback) throws IOException, UnsupportedCallbackException {
        if (callback instanceof TextOutputCallback) {
            TextOutputCallback toc = (TextOutputCallback) callback;
            switch(toc.getMessageType()) {
                case TextOutputCallback.INFORMATION:
                    _logger.info(JAAS + toc.getMessage());
                    break;
                case TextOutputCallback.ERROR:
                    _logger.error(JAAS + toc.getMessage());
                    break;
                case TextOutputCallback.WARNING:
                    _logger.warn(JAAS + toc.getMessage());
                    break;
                default:
                    throw new IOException("Unsupported message type: " + toc.getMessageType());
            }
        } else if (callback instanceof NameCallback) {
            ((NameCallback) callback).setName(_principal);
        } else if (callback instanceof PasswordCallback) {
            ((PasswordCallback) callback).setPassword(_credentials.toCharArray());
        } else {
            throw new UnsupportedCallbackException(callback);
        }
    }
}
