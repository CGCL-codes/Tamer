package mx4j.tools.remote.provider.burlap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;
import mx4j.tools.remote.http.HTTPConnector;

/**
 * @version $Revision: 1.5 $
 */
public class ClientProvider implements JMXConnectorProvider {

    public JMXConnector newJMXConnector(JMXServiceURL url, Map environment) throws IOException {
        String protocol = url.getProtocol();
        if (!"burlap".equals(protocol)) throw new MalformedURLException("Wrong protocol " + protocol + " for provider " + this);
        return new HTTPConnector(url, environment);
    }
}
