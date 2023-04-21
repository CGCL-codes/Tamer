package mx4j.tools.remote.resolver.hessian;

import java.io.IOException;
import java.util.Map;
import javax.management.remote.JMXServiceURL;
import mx4j.tools.remote.caucho.hessian.HessianClientInvoker;
import mx4j.tools.remote.caucho.hessian.HessianServlet;
import mx4j.tools.remote.http.HTTPResolver;

/**
 * @version $Revision: 1.1 $
 */
public class Resolver extends HTTPResolver {

    public Object lookupClient(JMXServiceURL url, Map environment) throws IOException {
        String endpoint = getEndpoint(url, environment);
        return new HessianClientInvoker(endpoint);
    }

    protected String getServletClassName() {
        return HessianServlet.class.getName();
    }
}
