package pl.xperios.rdk.client.rpcservices;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;

/**
 *
 * @author Praca
 */
public interface LoggingRpcServiceAsync {

    /**
     *
     * @param message
     * @param logLevel
     * @param date
     * @param isClient
     * @param callback
     */
    public void sendMessage(String message, int logLevel, Date date, boolean isClient, AsyncCallback<Boolean> callback);
}
