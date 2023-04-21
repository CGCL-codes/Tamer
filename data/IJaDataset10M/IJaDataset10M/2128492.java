package mx4j.tools.remote.resolver.burlap;

import java.io.IOException;
import java.util.Map;
import javax.management.remote.JMXServiceURL;
import mx4j.tools.remote.caucho.burlap.BurlapClientInvoker;
import mx4j.tools.remote.caucho.burlap.BurlapServlet;
import mx4j.tools.remote.http.HTTPResolver;

/**
 * @version $Revision: 2109 $
 */
public class BURLAPResolver extends HTTPResolver {

    public Object lookupClient(JMXServiceURL url, Map environment) throws IOException {
        String endpoint = getEndpoint(url, environment);
        return new BurlapClientInvoker(endpoint);
    }

    protected String getServletClassName() {
        return BurlapServlet.class.getName();
    }
}
