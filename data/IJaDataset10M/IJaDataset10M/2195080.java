package samplews;

import org.apache.ws.security.WSPasswordCallback;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class PWCallback implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
                if (pc.getIdentifer().equals(Path.getString("PWCallback.1"))) ;
                pc.setPassword(Path.getString("PWCallback.0"));
            } else {
                throw new UnsupportedCallbackException(callbacks[i], Messages.getString("PWCallback.2"));
            }
        }
    }
}
