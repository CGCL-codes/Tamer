package hambo.testsvc;

import java.util.*;
import hambo.util.OID;
import hambo.svc.*;
import junit.framework.*;

public class LoaderTest extends TestCase {

    public LoaderTest(String name) {
        super(name);
    }

    protected void setUp() {
    }

    public void testLoadServices() {
        Properties prop = new Properties();
        prop.put("loadOrder", "log user");
        prop.put("service.log.class", "hambo.svc.log.LogServiceManager");
        prop.put("service.log.id", "ems:/hambo/svc/log");
        prop.put("service.user.class", "hambo.svc.user.UserServiceManager");
        prop.put("service.user.id", "ems:/hambo/svc/user");
        ServiceManagerException exception = load(prop);
        if (exception != null) {
            exception.printStackTrace();
        }
        assert (exception == null);
    }

    public void testNoLoadOrder() {
        Properties prop = new Properties();
        prop.put("service.log.class", "hambo.svc.log.LogServiceManager");
        prop.put("service.user.class", "hambo.svc.user.UserServiceManager");
        ServiceManagerException exception = load(prop);
        assert (exception != null && (exception.toString().indexOf("Required property \"loadOrder") != -1));
    }

    public void testNoClassDefined() {
        Properties prop = new Properties();
        prop.put("loadOrder", "log user");
        ServiceManagerException exception = load(prop);
        assert (exception != null && (exception.toString().indexOf(".class not defined") != -1));
    }

    public void testNoClassFound() {
        Properties prop = new Properties();
        prop.put("loadOrder", "log user");
        prop.put("service.log.class", "hambo.svc.LogServiceManager");
        prop.put("service.user.class", "hambo.svc.user.UserServiceManager");
        ServiceManagerException exception = load(prop);
        assert (exception != null && (exception.toString().indexOf("Failed to load service class") != -1));
    }

    protected ServiceManagerException load(Properties prop) {
        ServiceManagerLoader loader = new ServiceManagerLoader(prop);
        try {
            loader.loadServices();
        } catch (ServiceManagerException ex) {
            return ex;
        }
        return null;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new LoaderTest("testLoadServices"));
        suite.addTest(new LoaderTest("testNoLoadOrder"));
        suite.addTest(new LoaderTest("testNoClassDefined"));
        suite.addTest(new LoaderTest("testNoClassFound"));
        return suite;
    }
}
