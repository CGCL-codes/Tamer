package net.sf.gateway.client.authorization;

import java.io.File;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import net.sf.gateway.client.ClientConfig;
import net.sf.gateway.client.gwc2008V01.SQLModuleType;
import net.sf.gateway.client.gwc2008V01.SSTRegisterModuleType;
import net.sf.gateway.client.gwc2008V01.WebServiceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ws.security.WSPasswordCallback;

/**
 * Simple password callback handler. This just checks if the password for the private key
 * is being requested, and if so sets that value.
 */
public class PasswordCallbackHandler implements CallbackHandler {

    protected static final Log LOG = LogFactory.getLog(PasswordCallbackHandler.class.getName());

    public void handle(Callback[] callbacks) throws IOException {
        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback cb = (WSPasswordCallback) callbacks[i];
            ClientConfig configuration = new ClientConfig(new File("config.xml"));
            SSTRegisterModuleType sstMod = configuration.getConfDoc().getModules().getSstregisterModule();
            String gatewayServiceName = sstMod.getSstpWebService();
            WebServiceType gatewayServiceConfig = configuration.getWebServiceMap().get(gatewayServiceName);
            if (cb.getIdentifier().equals(gatewayServiceConfig.getUser())) {
                cb.setPassword(gatewayServiceConfig.getPass());
            }
            SQLModuleType sqlMod = configuration.getConfDoc().getModules().getSqlModule();
            String sqlServiceName = sqlMod.getSqlWebService();
            WebServiceType sqlServiceConfig = configuration.getWebServiceMap().get(sqlServiceName);
            if (cb.getIdentifier().equals(sqlServiceConfig.getUser())) {
                cb.setPassword(sqlServiceConfig.getPass());
            }
        }
    }
}
