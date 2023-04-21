package test.mx4j.tools.remote.soap;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import javax.management.remote.JMXServiceURL;
import test.javax.management.remote.JMXConnectorInvocationTestCase;

/**
 * @version $Revision: 1.4 $
 */
public class SOAPConnectorInvocationTest extends JMXConnectorInvocationTestCase {

    public SOAPConnectorInvocationTest(String name) {
        super(name);
    }

    public JMXServiceURL createJMXConnectorServerAddress() throws MalformedURLException {
        return new JMXServiceURL("soap", null, 8080, "/soap");
    }

    public Map getEnvironment() {
        return new HashMap();
    }
}
