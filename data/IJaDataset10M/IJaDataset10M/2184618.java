package test.mx4j.tools.remote.hessian;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import javax.management.remote.JMXServiceURL;
import test.javax.management.remote.JMXNotificationsTestCase;

/**
 * @version $Revision: 1.4 $
 */
public class HessianNotificationsTest extends JMXNotificationsTestCase {

    public HessianNotificationsTest(String name) {
        super(name);
    }

    public JMXServiceURL createJMXConnectorServerAddress() throws MalformedURLException {
        return new JMXServiceURL("hessian", null, 8080, "/hessian");
    }

    public Map getEnvironment() {
        return new HashMap();
    }

    public void testNonSerializableNotifications() throws Exception {
    }
}
