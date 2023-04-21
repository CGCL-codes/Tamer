package com.svcdelivery.osgi.enterprise.remote.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import com.sun.enterprise.ee.cms.core.Action;
import com.sun.enterprise.ee.cms.core.ActionException;
import com.sun.enterprise.ee.cms.core.FailureNotificationAction;
import com.sun.enterprise.ee.cms.core.FailureNotificationActionFactory;
import com.sun.enterprise.ee.cms.core.FailureNotificationSignal;
import com.sun.enterprise.ee.cms.core.GMSConstants;
import com.sun.enterprise.ee.cms.core.GMSException;
import com.sun.enterprise.ee.cms.core.GMSFactory;
import com.sun.enterprise.ee.cms.core.GroupHandle;
import com.sun.enterprise.ee.cms.core.GroupManagementService;
import com.sun.enterprise.ee.cms.core.JoinNotificationAction;
import com.sun.enterprise.ee.cms.core.JoinNotificationActionFactory;
import com.sun.enterprise.ee.cms.core.JoinNotificationSignal;
import com.sun.enterprise.ee.cms.core.MessageAction;
import com.sun.enterprise.ee.cms.core.MessageActionFactory;
import com.sun.enterprise.ee.cms.core.MessageSignal;
import com.sun.enterprise.ee.cms.core.ServiceProviderConfigurationKeys;
import com.sun.enterprise.ee.cms.core.Signal;
import com.sun.enterprise.ee.cms.core.SignalAcquireException;
import com.sun.enterprise.ee.cms.core.SignalReleaseException;
import com.svcdelivery.osgi.enterprise.remote.Constants;

public class Activator implements BundleActivator {

    /**
	 * The remote OSGi component name.
	 */
    public static final String COMPONENT_NAME = "remoteosgi";

    /**
	 * GMS group name.
	 */
    private static final String GROUP_NAME = "remote-osgi-service";

    /**
	 * Tracker for all services.
	 */
    private ServiceTracker tracker;

    /**
	 * Unique id to service reference for local endpoints.
	 */
    private Map<ServiceId, ServiceReference> registered;

    /**
	 * Unique id to service reference for remote endpoints.
	 */
    private Map<ServiceId, ServiceRegistration> remote;

    /**
	 * A list of active remote service handlers.
	 */
    private Map<ServiceId, RemoteServiceHandler> handlers;

    /**
	 * GMS instance.
	 */
    private GroupManagementService gms;

    private IdFactory idf;

    /**
	 * Server name.
	 */
    private String serverName;

    private MessageActionFactory messageActionFactory;

    private JoinNotificationActionFactory joinNotificationActionFactory;

    private FailureNotificationActionFactory failureNotificationActionFactory;

    /**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
    public void start(final BundleContext bundleContext) throws Exception {
        idf = new IdFactory();
        registered = new HashMap<ServiceId, ServiceReference>();
        remote = new HashMap<ServiceId, ServiceRegistration>();
        handlers = new HashMap<ServiceId, RemoteServiceHandler>();
        try {
            serverName = getServerName();
            final ServiceTrackerCustomizer customizer = new ServiceTrackerCustomizer() {

                @Override
                public void removedService(ServiceReference reference, Object service) {
                    ServiceId id = getId(reference);
                    if (registered.containsKey(id)) {
                        try {
                            GroupHandle gh = gms.getGroupHandle();
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(os);
                            Unregister unregister = new Unregister(id);
                            oos.writeObject(unregister);
                            oos.flush();
                            gh.sendMessage(COMPONENT_NAME, os.toByteArray());
                            registered.remove(id);
                        } catch (GMSException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void modifiedService(ServiceReference reference, Object service) {
                }

                @Override
                public Object addingService(ServiceReference reference) {
                    Object svc = null;
                    List<String> exportedInterfaces = readStringProperty(reference.getProperty(Constants.EXPORTED_INTERFACES));
                    List<String> objectClasses = readStringProperty(reference.getProperty("objectClass"));
                    List<String> remoteInterfaces = new ArrayList<String>();
                    for (String exportedInterface : exportedInterfaces) {
                        if (objectClasses.contains(exportedInterface)) {
                            remoteInterfaces.add(exportedInterface);
                        }
                    }
                    if (remoteInterfaces.size() > 0) {
                        try {
                            svc = bundleContext.getService(reference);
                            GroupHandle gh = gms.getGroupHandle();
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(os);
                            ServiceId id = getId(reference);
                            Register register = new Register();
                            register.setId(id);
                            register.setInterfaces(remoteInterfaces);
                            register.setProperties(getProperties(reference));
                            oos.writeObject(register);
                            oos.flush();
                            gh.sendMessage(COMPONENT_NAME, os.toByteArray());
                            registered.put(id, reference);
                        } catch (GMSException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return svc;
                }
            };
            Properties configProperties = new Properties();
            String vmul = System.getProperty("remote.osgi.vmul");
            if (vmul != null) {
                configProperties.put(ServiceProviderConfigurationKeys.IS_BOOTSTRAPPING_NODE, "true");
                configProperties.put(ServiceProviderConfigurationKeys.VIRTUAL_MULTICAST_URI_LIST, vmul);
            }
            String groupName = System.getProperty("remote.osgi.group.name");
            if (groupName == null) {
                groupName = GROUP_NAME;
            }
            gms = (GroupManagementService) GMSFactory.startGMSModule(serverName, groupName, GroupManagementService.MemberType.CORE, configProperties);
            gms.join();
            messageActionFactory = new MessageActionFactory() {

                @Override
                public Action produceAction() {
                    return new MessageAction() {

                        @Override
                        public void consumeSignal(Signal signal) throws ActionException {
                            if (signal instanceof MessageSignal) {
                                MessageSignal message = (MessageSignal) signal;
                                try {
                                    message.acquire();
                                    byte[] data = message.getMessage();
                                    ByteArrayInputStream is = new ByteArrayInputStream(data);
                                    try {
                                        ObjectInputStream ois = new ObjectInputStream(is);
                                        Object o = ois.readObject();
                                        if (o instanceof Register) {
                                            Register register = (Register) o;
                                            BundleProxyClassLoader cl = new BundleProxyClassLoader(bundleContext);
                                            List<String> ifaces = register.getInterfaces();
                                            Map<String, Object> props = register.getProperties();
                                            props.remove(Constants.EXPORTED_INTERFACES);
                                            props.put(Constants.SERVICE_IMPORTED, "*");
                                            Dictionary<String, Object> propsDictionary = new Hashtable<String, Object>(props);
                                            if (ifaces != null) {
                                                Class<?>[] interfaces = locateInterfaces(cl, ifaces);
                                                if (interfaces.length > 0) {
                                                    ServiceId serviceId = register.getId();
                                                    RemoteServiceHandler handler = new RemoteServiceHandler(gms, idf, serviceId);
                                                    Object proxy = Proxy.newProxyInstance(cl, interfaces, handler);
                                                    handlers.put(serviceId, handler);
                                                    ServiceRegistration reg = bundleContext.registerService(ifaces.toArray(new String[ifaces.size()]), proxy, propsDictionary);
                                                    remote.put(serviceId, reg);
                                                    System.out.println("Registering " + serviceId);
                                                } else {
                                                    System.out.println("Interfaces not found " + ifaces);
                                                }
                                            }
                                        } else if (o instanceof Unregister) {
                                            Unregister unregister = (Unregister) o;
                                            ServiceId serviceId = unregister.getServiceId();
                                            if (serviceId != null) {
                                                ServiceRegistration reg = remote.get(serviceId);
                                                if (reg != null) {
                                                    System.out.println("Unregistering " + serviceId);
                                                    reg.unregister();
                                                    remote.remove(serviceId);
                                                    handlers.remove(serviceId);
                                                }
                                            }
                                        } else if (o instanceof Call) {
                                            Call call = (Call) o;
                                            ServiceId serviceId = call.getServiceId();
                                            CallResponse response = new CallResponse(call.getCallId(), serviceId);
                                            ServiceReference reference = registered.get(serviceId);
                                            if (reference != null) {
                                                Object svc = bundleContext.getService(reference);
                                                if (svc != null) {
                                                    String methodSignature = call.getMethod();
                                                    Class<?> svcClass = svc.getClass();
                                                    Method method = null;
                                                    for (Method current : svcClass.getMethods()) {
                                                        if (methodSignature.equals(getSignature(current))) {
                                                            method = current;
                                                            break;
                                                        }
                                                    }
                                                    if (method != null) {
                                                        Object responseValue = method.invoke(svc, call.getParameters());
                                                        response.setResponse(responseValue);
                                                    } else {
                                                        response.setException(new RemoteException("Method not found: " + methodSignature));
                                                    }
                                                } else {
                                                    response.setException(new RemoteException("Service implementation not found for service " + serviceId.getId()));
                                                }
                                            } else {
                                                response.setException(new RemoteException("Service reference not found for service " + serviceId.getId()));
                                            }
                                            try {
                                                GroupHandle gh = gms.getGroupHandle();
                                                ByteArrayOutputStream os = new ByteArrayOutputStream();
                                                ObjectOutputStream oos = new ObjectOutputStream(os);
                                                oos.writeObject(response);
                                                oos.flush();
                                                gh.sendMessage(serviceId.getHost(), COMPONENT_NAME, os.toByteArray());
                                            } catch (GMSException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (o instanceof CallResponse) {
                                            CallResponse response = (CallResponse) o;
                                            ServiceId serviceId = response.getServiceId();
                                            RemoteServiceHandler handler = handlers.get(serviceId);
                                            if (handler != null) {
                                                handler.handleResponse(response);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    message.release();
                                } catch (SignalAcquireException e) {
                                    e.printStackTrace();
                                } catch (SignalReleaseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        private Class<?>[] locateInterfaces(BundleProxyClassLoader cl, List<String> ifaces) throws ClassNotFoundException {
                            Set<Class<?>> classes = new HashSet<Class<?>>();
                            for (String iface : ifaces) {
                                classes.add(cl.loadClass(iface));
                            }
                            return classes.toArray(new Class<?>[classes.size()]);
                        }
                    };
                }
            };
            gms.addActionFactory(messageActionFactory, COMPONENT_NAME);
            joinNotificationActionFactory = new JoinNotificationActionFactory() {

                @Override
                public Action produceAction() {
                    return new JoinNotificationAction() {

                        @Override
                        public void consumeSignal(Signal signal) throws ActionException {
                            if (signal instanceof JoinNotificationSignal) {
                                JoinNotificationSignal join = (JoinNotificationSignal) signal;
                                try {
                                    join.acquire();
                                    String member = join.getMemberToken();
                                    for (Entry<ServiceId, ServiceReference> entry : registered.entrySet()) {
                                        ServiceId id = entry.getKey();
                                        ServiceReference reference = entry.getValue();
                                        List<String> exportedInterfaces = readStringProperty(reference.getProperty(Constants.EXPORTED_INTERFACES));
                                        List<String> objectClasses = readStringProperty(reference.getProperty("objectClass"));
                                        List<String> remoteInterfaces = new ArrayList<String>();
                                        for (String exportedInterface : exportedInterfaces) {
                                            if (objectClasses.contains(exportedInterface)) {
                                                remoteInterfaces.add(exportedInterface);
                                            }
                                        }
                                        if (remoteInterfaces.size() > 0) {
                                            try {
                                                GroupHandle gh = gms.getGroupHandle();
                                                ByteArrayOutputStream os = new ByteArrayOutputStream();
                                                ObjectOutputStream oos = new ObjectOutputStream(os);
                                                Register register = new Register();
                                                register.setId(id);
                                                register.setInterfaces(remoteInterfaces);
                                                register.setProperties(getProperties(reference));
                                                oos.writeObject(register);
                                                oos.flush();
                                                gh.sendMessage(member, COMPONENT_NAME, os.toByteArray());
                                            } catch (GMSException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    join.release();
                                } catch (SignalAcquireException e) {
                                    e.printStackTrace();
                                } catch (SignalReleaseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                }
            };
            gms.addActionFactory(joinNotificationActionFactory);
            failureNotificationActionFactory = new FailureNotificationActionFactory() {

                @Override
                public Action produceAction() {
                    return new FailureNotificationAction() {

                        @Override
                        public void consumeSignal(Signal signal) throws ActionException {
                            if (signal instanceof FailureNotificationSignal) {
                                FailureNotificationSignal failure = (FailureNotificationSignal) signal;
                                try {
                                    failure.acquire();
                                    String member = failure.getMemberToken();
                                    List<ServiceId> toRemove = new ArrayList<ServiceId>();
                                    for (ServiceId serviceId : remote.keySet()) {
                                        if (member.equals(serviceId.getHost())) {
                                            toRemove.add(serviceId);
                                        }
                                    }
                                    for (ServiceId serviceId : toRemove) {
                                        ServiceRegistration reg = remote.remove(serviceId);
                                        handlers.remove(serviceId);
                                        reg.unregister();
                                    }
                                    failure.release();
                                } catch (SignalAcquireException e) {
                                    e.printStackTrace();
                                } catch (SignalReleaseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                }
            };
            gms.addActionFactory(failureNotificationActionFactory);
            try {
                Filter filter = bundleContext.createFilter("(objectClass=*)");
                tracker = new ServiceTracker(bundleContext, filter, customizer);
                tracker.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param reference
	 * @return
	 */
    protected ServiceId getId(ServiceReference reference) {
        ServiceId id = new ServiceId(serverName, reference.getProperty("service.id").toString());
        return id;
    }

    /**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
    public void stop(BundleContext bundleContext) throws Exception {
        if (tracker != null) {
            tracker.close();
        }
        if (gms != null) {
            gms.removeMessageActionFactory(COMPONENT_NAME);
            gms.removeActionFactory(joinNotificationActionFactory);
            gms.removeActionFactory(failureNotificationActionFactory);
            for (ServiceId id : registered.keySet()) {
                try {
                    GroupHandle gh = gms.getGroupHandle();
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    Unregister unregister = new Unregister(id);
                    oos.writeObject(unregister);
                    oos.flush();
                    gh.sendMessage(COMPONENT_NAME, os.toByteArray());
                } catch (GMSException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            registered.clear();
            for (ServiceRegistration reg : remote.values()) {
                reg.unregister();
            }
            remote.clear();
            handlers.clear();
            gms.shutdown(GMSConstants.shutdownType.INSTANCE_SHUTDOWN);
        }
    }

    private List<String> readStringProperty(Object property) {
        List<String> items = new ArrayList<String>();
        if (property instanceof String[]) {
            String[] itemArray = (String[]) property;
            for (String item : itemArray) {
                items.add(item);
            }
        } else if (property instanceof String) {
            items.add((String) property);
        }
        return items;
    }

    private Map<String, Object> getProperties(ServiceReference reference) {
        Map<String, Object> properties = new HashMap<String, Object>();
        for (String key : reference.getPropertyKeys()) {
            if (!"objectClass".equals(key)) {
                properties.put(key, reference.getProperty(key));
            }
        }
        return properties;
    }

    /**
	 * @return The server name.
	 */
    private static String getServerName() {
        String serverName = System.getProperty("remote.osgi.node.name");
        if (serverName == null) {
            try {
                serverName = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            int dot = serverName.indexOf('.');
            if (dot != -1) {
                serverName = serverName.substring(0, dot);
            }
            serverName = serverName.toLowerCase();
        }
        return serverName;
    }

    /**
	 * @param method
	 * @return
	 */
    public static String getSignature(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getName());
        for (Class<?> param : method.getParameterTypes()) {
            builder.append(",");
            builder.append(param.getName());
        }
        return builder.toString();
    }
}
