package test.mx4j.tools.remote.hessian;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import javax.management.remote.JMXServiceURL;
import test.javax.management.remote.JMXConnectorTestCase;

/**
 * @version $
 */
public class HessianConnectorTest extends JMXConnectorTestCase {

    public HessianConnectorTest(String name) {
        super(name);
    }

    public JMXServiceURL createJMXConnectorServerAddress() throws MalformedURLException {
        return new JMXServiceURL("hessian", null, 8080, "/hessian");
    }

    public Map getEnvironment() {
        return new HashMap();
    }

    public void testDefaultClassLoader() throws Exception {
    }

    public void testConnectWithProviderClassLoader() throws Exception {
    }
}
