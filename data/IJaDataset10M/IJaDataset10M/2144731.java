package net.sourceforge.copernicus.client.cim;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import javax.cim.CIMArgument;
import javax.cim.CIMClass;
import javax.cim.CIMDataType;
import javax.cim.CIMInstance;
import javax.cim.CIMObjectPath;
import javax.cim.CIMProperty;
import javax.cim.UnsignedInteger16;
import javax.cim.UnsignedInteger32;
import javax.security.auth.Subject;
import javax.wbem.CloseableIterator;
import javax.wbem.WBEMException;
import javax.wbem.client.PasswordCredential;
import javax.wbem.client.UserPrincipal;
import javax.wbem.client.WBEMClient;
import javax.wbem.client.WBEMClientFactory;
import javax.wbem.listener.IndicationListener;
import javax.wbem.listener.WBEMListener;
import javax.wbem.listener.WBEMListenerFactory;

class IndicationHandle {

    public CIMObjectPath destination;

    public CIMObjectPath filter;

    public CIMObjectPath subscription;
}

public class SblimWbemClient implements CimClient, IndicationListener {

    private static final String INSTANCE_INDICATION_CLASSNAME = "CIM_InstIndication";

    private static final int INDICATION_LISTENER_PORT = 8882;

    private WBEMClient client;

    private String namespace = "root/cimv2";

    private String localeName = "C";

    private ConcurrentHashMap<String, CIMClass> classCache = new ConcurrentHashMap<String, CIMClass>();

    private ConcurrentHashMap<String, Localization> localeCache = new ConcurrentHashMap<String, Localization>();

    private WBEMListener listener;

    private CimIndicationListener cimListener;

    private Object subscriptionHandle;

    public SblimWbemClient(String namespace, String localeName) {
        this.namespace = namespace;
        this.localeName = localeName;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public void connect(String pWbemUrl, String pUser, String pPassword) throws WBEMException, IOException {
        if (client != null) client.close();
        client = WBEMClientFactory.getClient("CIM-XML");
        URL url = new URL(pWbemUrl);
        CIMObjectPath path = new CIMObjectPath(url.getProtocol(), url.getHost(), String.valueOf(url.getPort()), null, null, null);
        Subject subject = new Subject();
        subject.getPrincipals().add(new UserPrincipal(pUser));
        subject.getPrivateCredentials().add(new PasswordCredential(pPassword));
        client.initialize(path, subject, new Locale[] { Locale.US });
        listener = WBEMListenerFactory.getListener("CIM-XML");
        listener.addListener(this, INDICATION_LISTENER_PORT, "http");
    }

    public void disconnect() {
        listener.removeListener(INDICATION_LISTENER_PORT);
        client.close();
    }

    public CIMInstance getInstance(CIMObjectPath name) throws WBEMException {
        return client.getInstance(name, false, false, null);
    }

    public Iterator<CIMObjectPath> enumerateInstanceNames(String pClassName) throws WBEMException {
        CloseableIterator iterator = client.enumerateInstanceNames(new CIMObjectPath(pClassName, namespace));
        return toStandardIterator(iterator);
    }

    public Iterator<CIMInstance> enumerateInstances(String className) throws WBEMException {
        CloseableIterator iterator = client.enumerateInstances(new CIMObjectPath(className, namespace), true, false, false, null);
        return toStandardIterator(iterator);
    }

    public Collection<CIMObjectPath> associatorNames(CIMObjectPath objectPath) throws WBEMException {
        CloseableIterator iterator = client.associatorNames(objectPath, null, null, null, null);
        return toArrayList(iterator);
    }

    public Iterator<CIMInstance> associators(CIMObjectPath objectPath) throws WBEMException {
        CloseableIterator iterator = client.associators(objectPath, null, null, null, null, true, false, null);
        return toStandardIterator(iterator);
    }

    public CIMClass getClass(String className) throws WBEMException {
        if (classCache.containsKey(className)) return classCache.get(className);
        CIMClass ret = client.getClass(new CIMObjectPath(className, namespace), false, true, false, null);
        if (ret != null) classCache.put(className, ret);
        return ret;
    }

    public Localization getLocalization(String className) {
        if (localeCache.containsKey(className)) return localeCache.get(className);
        try {
            Iterator<CIMObjectPath> opi = enumerateInstanceNames(className + "Localization");
            if (!opi.hasNext()) return null;
            CIMObjectPath objectName = opi.next();
            CIMInstance loc = getInstance(objectName);
            if (loc == null) return null;
            CIMArgument[] inArgs = { new CIMArgument("localeName", CIMDataType.STRING_T, localeName), new CIMArgument("format", CIMDataType.UINT32_T, new Integer(2)) };
            CIMArgument[] outArgs = new CIMArgument[2];
            UnsignedInteger32 rc = (UnsignedInteger32) invokeMethod(loc.getObjectPath(), "getLocalization", inArgs, outArgs);
            if (rc.intValue() == 0) {
                UnsignedInteger32 resultFormat = (UnsignedInteger32) outArgs[1].getValue();
                if (resultFormat.intValue() == 2) {
                    SimpleXmlLocalization ret = new SimpleXmlLocalization(outArgs[0].getValue().toString());
                    localeCache.put(className, ret);
                    return ret;
                } else if (resultFormat.intValue() == 1) {
                    PropertiesLocalization ret = new PropertiesLocalization(outArgs[0].getValue().toString());
                    localeCache.put(className, ret);
                    return ret;
                } else {
                    return null;
                }
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Object invokeMethod(CIMObjectPath objectName, String methodName, CIMArgument[] inputArguments, CIMArgument[] outputArguments) throws WBEMException {
        return client.invokeMethod(objectName, methodName, inputArguments, outputArguments);
    }

    public void modifyInstance(CIMInstance instance, String[] propertyList) throws WBEMException {
        client.modifyInstance(instance, propertyList);
    }

    public void subscribeToIndications(CimIndicationListener listener) throws UnknownHostException, WBEMException {
        subscriptionHandle = createSubscription(INSTANCE_INDICATION_CLASSNAME);
        cimListener = listener;
    }

    public void unsubscribeFromIndications() {
        if (subscriptionHandle == null) return;
        removeSubscription(subscriptionHandle);
        subscriptionHandle = null;
    }

    private Object createSubscription(String className) throws UnknownHostException, WBEMException {
        CIMProperty name = new CIMProperty("Name", CIMDataType.STRING_T, "Copernicus-Client-" + InetAddress.getLocalHost().getCanonicalHostName() + "-" + System.currentTimeMillis(), true, false, null);
        CIMProperty destination = new CIMProperty("Destination", CIMDataType.STRING_T, "http://" + InetAddress.getLocalHost().getCanonicalHostName() + ":" + INDICATION_LISTENER_PORT + "/" + className, false, false, null);
        CIMProperty persistenceType = new CIMProperty("PersistenceType", CIMDataType.UINT16_T, new UnsignedInteger16(3), false, false, null);
        CIMProperty[] listenerDestinationProperties = new CIMProperty[] { name, destination, persistenceType };
        CIMInstance listenerDestinationInstance = new CIMInstance(new CIMObjectPath("CIM_ListenerDestinationCIMXML", namespace), listenerDestinationProperties);
        CIMProperty query = new CIMProperty("Query", CIMDataType.STRING_T, "SELECT * FROM " + className, false, false, null);
        CIMProperty queryLanguage = new CIMProperty("QueryLanguage", CIMDataType.STRING_T, "WQL", false, false, null);
        CIMProperty sourceNameSpace = new CIMProperty("SourceNamespace", CIMDataType.STRING_T, namespace, false, false, null);
        CIMProperty[] filterProperties = new CIMProperty[] { name, query, queryLanguage, sourceNameSpace };
        CIMInstance filterInstance = new CIMInstance(new CIMObjectPath("CIM_IndicationFilter", namespace), filterProperties);
        CIMProperty handler = new CIMProperty("Handler", new CIMDataType("CIM_ListenerDestinationCIMXML"), listenerDestinationInstance.getObjectPath(), true, false, null);
        CIMProperty filter = new CIMProperty("Filter", new CIMDataType("CIM_IndicationFilter"), filterInstance.getObjectPath(), true, false, null);
        CIMProperty[] subscriptionProperties = new CIMProperty[] { handler, filter };
        CIMInstance subscriptionInstance = new CIMInstance(new CIMObjectPath("CIM_IndicationSubscription", namespace), subscriptionProperties);
        IndicationHandle handle = new IndicationHandle();
        try {
            CIMObjectPath indicationDestinationPath = client.createInstance(listenerDestinationInstance);
            if (indicationDestinationPath == null) throw new WBEMException("Failed to create CIM_ListenerDestinationCIMXML");
            handle.destination = indicationDestinationPath;
            CIMObjectPath indicationFilterPath = client.createInstance(filterInstance);
            if (indicationFilterPath == null) throw new WBEMException("Failed to create CIM_IndicationFilter");
            handle.filter = indicationFilterPath;
            CIMObjectPath indicationSubscriptionPath = client.createInstance(subscriptionInstance);
            if (indicationSubscriptionPath == null) throw new WBEMException("Failed to create CIM_IndicationSubscription");
            handle.subscription = indicationSubscriptionPath;
        } catch (WBEMException e) {
            removeSubscription(handle);
            throw e;
        }
        return handle;
    }

    private void removeSubscription(Object indicationHandle) {
        IndicationHandle castedHandle = (IndicationHandle) indicationHandle;
        if (castedHandle.subscription != null) {
            try {
                client.deleteInstance(castedHandle.subscription);
            } catch (WBEMException e) {
            }
        }
        if (castedHandle.filter != null) {
            try {
                client.deleteInstance(castedHandle.filter);
            } catch (WBEMException e) {
            }
        }
        if (castedHandle.destination != null) {
            try {
                client.deleteInstance(castedHandle.destination);
            } catch (WBEMException e) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> ArrayList<T> toArrayList(CloseableIterator iterator) {
        ArrayList<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add((T) iterator.next());
        }
        iterator.close();
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T> Iterator<T> toStandardIterator(CloseableIterator iterator) {
        ArrayList<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add((T) iterator.next());
        }
        iterator.close();
        return result.iterator();
    }

    @Override
    public void indicationOccured(String indicationUrl, CIMInstance indicationInstance) {
        if (indicationUrl.equals(INSTANCE_INDICATION_CLASSNAME)) {
            cimListener.onInstanceIndication(indicationInstance);
        }
    }
}
