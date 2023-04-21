package test.mx4j.tools.adaptor.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Set;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.NamingException;
import mx4j.tools.adaptor.rmi.iiop.IIOPAdaptor;
import mx4j.tools.adaptor.rmi.iiop.IIOPAdaptorMBean;
import mx4j.tools.connector.RemoteMBeanServer;
import mx4j.tools.connector.rmi.iiop.IIOPConnector;
import mx4j.tools.adaptor.interceptor.TimingAdaptorInterceptor;
import mx4j.tools.naming.CosNamingService;
import test.MX4JTestCase;
import test.MutableInteger;

/**
 *
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 1719 $
 */
public class IIOPAdaptorTest extends MX4JTestCase {

    private MBeanServer m_server;

    private ObjectName m_naming;

    private int m_port = 1098;

    public IIOPAdaptorTest(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        m_server = newMBeanServer();
        m_naming = new ObjectName("Naming:type=tnameserv");
        CosNamingService naming = new CosNamingService(m_port);
        m_server.registerMBean(naming, m_naming);
        m_server.invoke(m_naming, "start", null, null);
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
        System.setProperty(Context.PROVIDER_URL, "iiop://localhost:" + m_port);
        sleep(5000);
    }

    protected void tearDown() throws Exception {
        System.getProperties().remove(Context.INITIAL_CONTEXT_FACTORY);
        System.getProperties().remove(Context.PROVIDER_URL);
        m_server.invoke(m_naming, "stop", null, null);
        m_server.unregisterMBean(m_naming);
        sleep(5000);
    }

    public void testAdaptorNoName() throws Exception {
        IIOPAdaptor adaptor = new IIOPAdaptor();
        adaptor.setMBeanServer(m_server);
        try {
            adaptor.start();
            fail("Cannot start adaptor with no name");
        } catch (NamingException ignored) {
        }
    }

    public void testAdaptorNotRegistered() throws Exception {
        IIOPAdaptor adaptor = new IIOPAdaptor();
        adaptor.setMBeanServer(m_server);
        String jndiName = "iiop";
        adaptor.setJNDIName(jndiName);
        adaptor.start();
        IIOPConnector connector = new IIOPConnector();
        Hashtable env = new Hashtable();
        connector.connect(jndiName, env);
        System.out.println("NAME: " + connector.getRemoteHostName());
        System.out.println("IP: " + connector.getRemoteHostAddress());
        System.out.println("COUNT: " + connector.getRemoteMBeanServer().getMBeanCount());
    }

    public void testAddInterceptorNotRegistered() throws Exception {
        IIOPAdaptor adaptor = new IIOPAdaptor();
        String jndiName = "iiop";
        adaptor.setJNDIName(jndiName);
        adaptor.setMBeanServer(m_server);
        TimingAdaptorInterceptor intr = new TimingAdaptorInterceptor();
        intr.setEnabled(true);
        adaptor.addInterceptor(intr);
        adaptor.start();
        IIOPConnector connector = new IIOPConnector();
        Hashtable env = new Hashtable();
        connector.connect(jndiName, env);
        System.out.println("NAME: " + connector.getRemoteHostName());
        System.out.println("IP: " + connector.getRemoteHostAddress());
        System.out.println("COUNT: " + connector.getRemoteMBeanServer().getMBeanCount());
    }

    public void testAddInterceptorRegistered() throws Exception {
        int initialCount = m_server.getMBeanCount().intValue();
        IIOPAdaptor adaptor = new IIOPAdaptor();
        String jndiName = "iiop";
        adaptor.setJNDIName(jndiName);
        ObjectName adaptorName = new ObjectName("Adaptor:protocol=IIOP");
        TimingAdaptorInterceptor intr = new TimingAdaptorInterceptor();
        ObjectName interceptorName = new ObjectName("AdaptorInterceptor:type=timing");
        intr.setObjectName(interceptorName);
        intr.setEnabled(true);
        adaptor.addInterceptor(intr);
        m_server.registerMBean(adaptor, adaptorName);
        adaptor.start();
        IIOPConnector connector = new IIOPConnector();
        Hashtable env = new Hashtable();
        connector.connect(jndiName, env);
        RemoteMBeanServer server = connector.getRemoteMBeanServer();
        System.out.println("NAME: " + connector.getRemoteHostName());
        System.out.println("IP: " + connector.getRemoteHostAddress());
        int count = server.getMBeanCount().intValue();
        System.out.println("COUNT: " + count);
        System.out.println("MBEANS: " + server.queryMBeans(null, null));
        if (count - initialCount != 5) {
            fail("Interceptors not registered");
        }
        Set mbeans = m_server.queryMBeans(interceptorName, null);
        if (mbeans.size() != 1) {
            fail("Interceptor registered with wrong name");
        }
        ObjectInstance instance = (ObjectInstance) mbeans.iterator().next();
        if (!instance.getClassName().equals(intr.getClass().getName())) {
            fail("Interceptor registered with wrong name");
        }
    }

    public void testStartStopConnect() throws Exception {
        ObjectName adaptor = new ObjectName("Adaptor:protocol=IIOP");
        m_server.createMBean("mx4j.tools.adaptor.rmi.iiop.IIOPAdaptor", adaptor, null);
        IIOPAdaptorMBean mbean = (IIOPAdaptorMBean) MBeanServerInvocationHandler.newProxyInstance(m_server, adaptor, IIOPAdaptorMBean.class, false);
        String jndiName = "iiop";
        mbean.setJNDIName(jndiName);
        mbean.start();
        IIOPConnector connector = new IIOPConnector();
        Hashtable env = new Hashtable();
        connector.connect(jndiName, env);
        RemoteMBeanServer server = connector.getRemoteMBeanServer();
        System.out.println("NAME: " + connector.getRemoteHostName());
        System.out.println("IP: " + connector.getRemoteHostAddress());
        server.invoke(adaptor, "stop", null, null);
        try {
            System.out.println("IP: " + connector.getRemoteHostAddress());
            fail("Connector should be disabled");
        } catch (RemoteException ignored) {
        }
        mbean.start();
        connector.connect(jndiName, env);
        System.out.println("IP: " + connector.getRemoteHostAddress());
        System.out.println("COUNT: " + server.getMBeanCount());
    }

    public void testAddRemoveListeners() throws Exception {
        ObjectName adaptor = new ObjectName("Adaptor:protocol=IIOP");
        m_server.createMBean("mx4j.tools.adaptor.rmi.iiop.IIOPAdaptor", adaptor, null);
        IIOPAdaptorMBean mbean = (IIOPAdaptorMBean) MBeanServerInvocationHandler.newProxyInstance(m_server, adaptor, IIOPAdaptorMBean.class, false);
        String jndiName = "iiop";
        mbean.setJNDIName(jndiName);
        mbean.start();
        IIOPConnector connector = new IIOPConnector();
        Hashtable env = new Hashtable();
        connector.connect(jndiName, env);
        RemoteMBeanServer server = connector.getRemoteMBeanServer();
        MutableInteger integer = new MutableInteger(0);
        Listener listener = new Listener(integer);
        server.addNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"), listener, null, null);
        server.createMBean("javax.management.loading.MLet", new ObjectName(":type=MLet1"), null);
        Thread.sleep(1000);
        if (integer.get() < 1) {
            fail("Remote notifications are not working");
        }
        server.removeNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"), listener, null, null);
        server.unregisterMBean(new ObjectName(":type=MLet1"));
        if (integer.get() != 1) {
            fail("Remote NotificationListener not removed");
        }
    }

    public void testRemoteNotificationListeners() throws Exception {
        ObjectName adaptor = new ObjectName("Adaptor:protocol=IIOP");
        m_server.createMBean("mx4j.tools.adaptor.rmi.iiop.IIOPAdaptor", adaptor, null);
        IIOPAdaptorMBean mbean = (IIOPAdaptorMBean) MBeanServerInvocationHandler.newProxyInstance(m_server, adaptor, IIOPAdaptorMBean.class, false);
        String jndiName = "iiop";
        mbean.setJNDIName(jndiName);
        mbean.start();
        IIOPConnector connector = new IIOPConnector();
        Hashtable env = new Hashtable();
        connector.connect(jndiName, env);
        RemoteMBeanServer server = connector.getRemoteMBeanServer();
        Listener listener = new Listener(new MutableInteger(0));
        server.addNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"), listener, null, null);
        server.createMBean("javax.management.loading.MLet", new ObjectName(":type=MLet1"), null);
        MutableInteger integer = listener.getInteger();
        if (integer == null) {
            fail("Listener must not be serialized");
        }
        Thread.sleep(1000);
        if (integer.get() < 1) {
            fail("Remote notifications are not working");
        }
    }

    public void testRemoteNotificationHandbacks() throws Exception {
        ObjectName adaptor = new ObjectName("Adaptor:protocol=IIOP");
        m_server.createMBean("mx4j.tools.adaptor.rmi.iiop.IIOPAdaptor", adaptor, null);
        IIOPAdaptorMBean mbean = (IIOPAdaptorMBean) MBeanServerInvocationHandler.newProxyInstance(m_server, adaptor, IIOPAdaptorMBean.class, false);
        String jndiName = "iiop";
        mbean.setJNDIName(jndiName);
        mbean.start();
        IIOPConnector connector = new IIOPConnector();
        Hashtable env = new Hashtable();
        connector.connect(jndiName, env);
        RemoteMBeanServer server = connector.getRemoteMBeanServer();
        Listener handback = new Listener(new MutableInteger(0));
        Listener listener = new Listener(new MutableInteger(0));
        server.addNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"), listener, null, handback);
        server.createMBean("javax.management.loading.MLet", new ObjectName(":type=MLet2"), null);
        Thread.sleep(1000);
        MutableInteger integer = listener.getInteger();
        if (integer == null) {
            fail("Listener must not be serialized");
        }
        if (integer.get() < 1) {
            fail("Remote notifications are not working");
        }
        Listener hb = (Listener) listener.getHandBack();
        if (hb.getInteger() != null) {
            fail("Handback must be serialized");
        }
    }

    public void testRemoteListenerCallback() throws Exception {
        ObjectName adaptor = new ObjectName("Adaptor:protocol=IIOP");
        try {
            m_server.createMBean("mx4j.tools.adaptor.rmi.iiop.IIOPAdaptor", adaptor, null);
            IIOPAdaptorMBean mbean = (IIOPAdaptorMBean) MBeanServerInvocationHandler.newProxyInstance(m_server, adaptor, IIOPAdaptorMBean.class, false);
            String jndiName = "iiop";
            mbean.setJNDIName(jndiName);
            mbean.start();
            final IIOPConnector connector = new IIOPConnector();
            Hashtable env = new Hashtable();
            connector.connect(jndiName, env);
            RemoteMBeanServer server = connector.getRemoteMBeanServer();
            final MutableInteger integer = new MutableInteger(0);
            NotificationListener listener = new NotificationListener() {

                public void handleNotification(Notification notification, Object handback) {
                    if (notification instanceof MBeanServerNotification) {
                        try {
                            MBeanServerNotification n = (MBeanServerNotification) notification;
                            System.out.println("Before callback...");
                            MBeanInfo info = connector.getRemoteMBeanServer().getMBeanInfo(n.getMBeanName());
                            System.out.println("After callback");
                            if (info == null) {
                                fail("Null MBeanInfo");
                            }
                            integer.set(integer.get() + 1);
                        } catch (Exception x) {
                            x.printStackTrace();
                            integer.set(0);
                        }
                    } else {
                        integer.set(0);
                    }
                }
            };
            ObjectName delegate = new ObjectName("JMImplementation:type=MBeanServerDelegate");
            server.addNotificationListener(delegate, listener, null, null);
            server.createMBean("javax.management.loading.MLet", new ObjectName(":type=MLet2"), null);
            Thread.sleep(1000);
            server.removeNotificationListener(delegate, listener, null, null);
            if (integer.get() != 1) {
                fail("Problems with notification callbacks");
            }
        } finally {
            m_server.unregisterMBean(adaptor);
        }
    }

    private static class Listener implements NotificationListener, Serializable {

        private transient MutableInteger m_notified;

        private Object m_handback;

        public Listener(MutableInteger notified) {
            m_notified = notified;
        }

        public void handleNotification(Notification notification, Object handback) {
            m_notified.set(m_notified.get() + 1);
            m_handback = handback;
        }

        private MutableInteger getInteger() {
            return m_notified;
        }

        private Object getHandBack() {
            return m_handback;
        }
    }
}
