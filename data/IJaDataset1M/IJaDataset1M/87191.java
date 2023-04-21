package test.mx4j.util;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.naming.NamingException;
import junit.framework.TestCase;
import mx4j.util.StandardMBeanProxy;

/**
 *
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 737 $
 */
public class StandardMBeanProxyTest extends TestCase {

    public StandardMBeanProxyTest(String s) {
        super(s);
    }

    public void testNotRegisteredMBean() throws Exception {
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        ObjectName name = new ObjectName("domain:key=value");
        LocalService mbean = new LocalService();
        server.registerMBean(mbean, name);
        ObjectName dummy = new ObjectName("domain:key=dummy");
        try {
            LocalServiceMBean proxy = (LocalServiceMBean) StandardMBeanProxy.create(LocalServiceMBean.class, server, dummy);
            fail("ObjectName is not known to the server");
        } catch (IllegalArgumentException x) {
        }
    }

    public void testDeregisteredMBean() throws Exception {
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        ObjectName name = new ObjectName("domain:key=value");
        LocalService mbean = new LocalService();
        server.registerMBean(mbean, name);
        LocalServiceMBean proxy = (LocalServiceMBean) StandardMBeanProxy.create(LocalServiceMBean.class, server, name);
        server.unregisterMBean(name);
        try {
            proxy.throwCheckedException();
            fail();
        } catch (NamingException x) {
            fail("Expecting an InstanceNotFoundException");
        } catch (UndeclaredThrowableException x) {
            Throwable xx = x.getUndeclaredThrowable();
            if (!(xx instanceof InstanceNotFoundException)) fail("Expecting an InstanceNotFoundException");
        }
    }

    public void testCheckedException() throws Exception {
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        ObjectName name = new ObjectName("domain:key=value");
        LocalService mbean = new LocalService();
        server.registerMBean(mbean, name);
        LocalServiceMBean proxy = (LocalServiceMBean) StandardMBeanProxy.create(LocalServiceMBean.class, server, name);
        try {
            proxy.throwCheckedException();
            fail();
        } catch (NamingException x) {
        }
    }

    public void testMBeanException() throws Exception {
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        ObjectName name = new ObjectName("domain:key=value");
        LocalService mbean = new LocalService();
        server.registerMBean(mbean, name);
        LocalServiceMBean proxy = (LocalServiceMBean) StandardMBeanProxy.create(LocalServiceMBean.class, server, name);
        try {
            proxy.throwMBeanException();
            fail();
        } catch (MBeanException x) {
        }
    }

    public void testRuntimeException() throws Exception {
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        ObjectName name = new ObjectName("domain:key=value");
        LocalService mbean = new LocalService();
        server.registerMBean(mbean, name);
        LocalServiceMBean proxy = (LocalServiceMBean) StandardMBeanProxy.create(LocalServiceMBean.class, server, name);
        try {
            proxy.throwNullPointerException();
            fail();
        } catch (NullPointerException x) {
        }
    }

    public void testError() throws Exception {
        MBeanServer server = MBeanServerFactory.newMBeanServer();
        ObjectName name = new ObjectName("domain:key=value");
        LocalService mbean = new LocalService();
        server.registerMBean(mbean, name);
        LocalServiceMBean proxy = (LocalServiceMBean) StandardMBeanProxy.create(LocalServiceMBean.class, server, name);
        try {
            proxy.throwError();
            fail();
        } catch (Error x) {
        }
    }

    public interface LocalServiceMBean {

        public void throwCheckedException() throws NamingException;

        public void throwMBeanException() throws MBeanException;

        public void throwNullPointerException();

        public void throwError();
    }

    public interface RemoteService {

        public void throwCheckedException() throws NamingException, IOException;

        public void throwMBeanException() throws MBeanException, IOException;

        public void throwNullPointerException() throws IOException;

        public void throwError() throws IOException;
    }

    public class LocalService implements LocalServiceMBean {

        public void throwCheckedException() throws NamingException {
            throw new NamingException();
        }

        public void throwMBeanException() throws MBeanException {
            throw new MBeanException(new Exception());
        }

        public void throwNullPointerException() {
            throw new NullPointerException();
        }

        public void throwError() {
            throw new Error();
        }
    }
}
