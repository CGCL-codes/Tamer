package mx4j.tools.remote.soap;

import java.io.IOException;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.NotificationListener;
import javax.management.remote.NotificationResult;
import javax.security.auth.Subject;
import mx4j.remote.RemoteNotificationServerHandler;
import mx4j.remote.DefaultRemoteNotificationServerHandler;
import mx4j.remote.NotificationTuple;

/**
 * @author <a href="mailto:biorn_steedom@users.sourceforge.net">Simone Bordet</a>
 * @version $Revision: 1944 $
 */
class ServerInvoker implements SOAPConnection {

    private final MBeanServer server;

    private final RemoteNotificationServerHandler notificationHandler;

    ServerInvoker(MBeanServer server, Map environment) {
        this.server = server;
        this.notificationHandler = new DefaultRemoteNotificationServerHandler(environment);
    }

    public String connect(Object credentials) throws IOException, SecurityException {
        throw new Error("connect(Object) must not be propagated along the invocation chain");
    }

    public void close() throws IOException {
        NotificationTuple[] tuples = notificationHandler.close();
        for (int i = 0; i < tuples.length; ++i) {
            NotificationTuple tuple = tuples[i];
            try {
                server.removeNotificationListener(tuple.getObjectName(), tuple.getNotificationListener(), tuple.getNotificationFilter(), tuple.getHandback());
            } catch (InstanceNotFoundException ignored) {
            } catch (ListenerNotFoundException ignored) {
            }
        }
    }

    public ObjectInstance createMBean(String className, ObjectName name, Object[] params, String[] signature, Subject delegate) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
        return server.createMBean(className, name, params, signature);
    }

    public ObjectInstance createMBean(String className, ObjectName name, ObjectName loaderName, Object[] params, String[] signature, Subject delegate) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
        return server.createMBean(className, name, loaderName, params, signature);
    }

    public void unregisterMBean(ObjectName name, Subject delegate) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
        server.unregisterMBean(name);
    }

    public ObjectInstance getObjectInstance(ObjectName name, Subject delegate) throws InstanceNotFoundException, IOException {
        return server.getObjectInstance(name);
    }

    public Set queryMBeans(ObjectName name, Object query, Subject delegate) throws IOException {
        return server.queryMBeans(name, (QueryExp) query);
    }

    public Set queryNames(ObjectName name, Object query, Subject delegate) throws IOException {
        return server.queryNames(name, (QueryExp) query);
    }

    public boolean isRegistered(ObjectName name, Subject delegate) throws IOException {
        return server.isRegistered(name);
    }

    public Integer getMBeanCount(Subject delegate) throws IOException {
        return server.getMBeanCount();
    }

    public Object getAttribute(ObjectName name, String attribute, Subject delegate) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
        return server.getAttribute(name, attribute);
    }

    public AttributeList getAttributes(ObjectName name, String[] attributes, Subject delegate) throws InstanceNotFoundException, ReflectionException, IOException {
        return server.getAttributes(name, attributes);
    }

    public void setAttribute(ObjectName name, Attribute attribute, Subject delegate) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
        server.setAttribute(name, attribute);
    }

    public AttributeList setAttributes(ObjectName name, AttributeList attributes, Subject delegate) throws InstanceNotFoundException, ReflectionException, IOException {
        return server.setAttributes(name, attributes);
    }

    public Object invoke(ObjectName name, String operationName, Object[] params, String[] signature, Subject delegate) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
        return server.invoke(name, operationName, params, signature);
    }

    public String getDefaultDomain(Subject delegate) throws IOException {
        return server.getDefaultDomain();
    }

    public String[] getDomains(Subject delegate) throws IOException {
        return server.getDomains();
    }

    public MBeanInfo getMBeanInfo(ObjectName name, Subject delegate) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
        return server.getMBeanInfo(name);
    }

    public boolean isInstanceOf(ObjectName name, String className, Subject delegate) throws InstanceNotFoundException, IOException {
        return server.isInstanceOf(name, className);
    }

    public void addNotificationListener(ObjectName name, ObjectName listener, Object filter, Object handback, Subject delegate) throws InstanceNotFoundException, IOException {
        server.addNotificationListener(name, listener, (NotificationFilter) filter, handback);
    }

    public void removeNotificationListener(ObjectName name, ObjectName listener, Subject delegate) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        server.removeNotificationListener(name, listener);
    }

    public void removeNotificationListener(ObjectName name, ObjectName listener, Object filter, Object handback, Subject delegate) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        server.removeNotificationListener(name, listener, (NotificationFilter) filter, handback);
    }

    public Integer[] addNotificationListeners(ObjectName[] names, Subject[] delegates) throws InstanceNotFoundException, IOException {
        ArrayList ids = new ArrayList();
        for (int i = 0; i < names.length; ++i) {
            ObjectName name = names[i];
            Integer id = notificationHandler.generateListenerID(name, null);
            NotificationListener listener = notificationHandler.getServerNotificationListener();
            server.addNotificationListener(name, listener, null, id);
            notificationHandler.addNotificationListener(id, new NotificationTuple(name, listener, null, id));
            ids.add(id);
        }
        return (Integer[]) ids.toArray(new Integer[ids.size()]);
    }

    public void removeNotificationListeners(ObjectName observed, Integer[] listenerIDs, Subject delegate) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
        for (int i = 0; i < listenerIDs.length; ++i) {
            Integer id = listenerIDs[i];
            NotificationTuple tuple = notificationHandler.removeNotificationListener(id);
            server.removeNotificationListener(observed, tuple.getNotificationListener(), tuple.getNotificationFilter(), tuple.getHandback());
        }
    }

    public NotificationResult fetchNotifications(long clientSequenceNumber, int maxNotifications, long timeout) throws IOException {
        return notificationHandler.fetchNotifications(clientSequenceNumber, maxNotifications, timeout);
    }
}
