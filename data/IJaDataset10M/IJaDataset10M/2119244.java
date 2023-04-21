package org.myrobotlab.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.myrobotlab.cmdline.CMDLine;
import org.myrobotlab.fileLib.FileIO;
import org.myrobotlab.framework.MethodEntry;
import org.myrobotlab.framework.NotifyEntry;
import org.myrobotlab.framework.Service;
import org.myrobotlab.framework.ServiceEnvironment;
import org.myrobotlab.framework.ServiceInfo;
import org.myrobotlab.framework.ServiceWrapper;
import org.myrobotlab.service.interfaces.ServiceInterface;
import org.simpleframework.xml.Element;

/**
 * 
 * Runtime is responsible for the creation and removal of all Services
 * and the associated static regestries 
 * It maintains state information regarding possible & running local Services
 * It maintains state information regarding foreign Runtimes
 * It is a singleton and should be the only service of Runtime running in a
 * process
 * The host and registry maps are used in routing communication to the
 * appropriate service (be it local or remote)
 * It will be the first Service created
 * It also wraps the real JVM Runtime object
 *
 */
public class Runtime extends Service {

    private static final long serialVersionUID = 1L;

    private static HashMap<URL, ServiceEnvironment> hosts = new HashMap<URL, ServiceEnvironment>();

    ;

    private static HashMap<String, ServiceWrapper> registry = new HashMap<String, ServiceWrapper>();

    private static boolean inclusiveExportFilterEnabled = false;

    private static boolean exclusiveExportFilterEnabled = false;

    private static HashMap<String, String> inclusiveExportFilter = new HashMap<String, String>();

    private static HashMap<String, String> exclusiveExportFilter = new HashMap<String, String>();

    private static HashMap<String, String> hideMethods = new HashMap<String, String>();

    private static boolean needsRestart = false;

    private static boolean checkForDependencies = true;

    public static final String registered = "registered";

    public static final ServiceInfo serviceInfo = ServiceInfo.getInstance();

    @Element
    public String proxyHost;

    @Element
    public String proxyPort;

    @Element
    public String proxyUserName;

    @Element
    public String proxyPassword;

    static ServiceInterface gui = null;

    public static final Logger LOG = Logger.getLogger(Runtime.class.getCanonicalName());

    private static Runtime INSTANCE = null;

    private Runtime(String n) {
        super(n, Runtime.class.getCanonicalName());
        hideMethods.put("main", null);
        hideMethods.put("loadDefaultConfiguration", null);
        hideMethods.put("getToolTip", null);
        hideMethods.put("run", null);
        hideMethods.put("access$0", null);
        serviceInfo.getLocalServiceData();
        startService();
    }

    public static boolean isRuntime(Service newService) {
        return newService.getClass().equals(Runtime.class);
    }

    public static Runtime getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Runtime("BORG " + new Random().nextInt(99999));
        }
        return INSTANCE;
    }

    @Override
    public void stopService() {
        super.stopService();
        INSTANCE = null;
    }

    @Override
    public void loadDefaultConfiguration() {
    }

    @Override
    public String getToolTip() {
        return "Runtime singleton service";
    }

    public int exec(String[] params) {
        java.lang.Runtime r = java.lang.Runtime.getRuntime();
        try {
            Process p = r.exec(params);
            return p.exitValue();
        } catch (IOException e) {
            logException(e);
        }
        return 0;
    }

    public static final long getTotalMemory() {
        return java.lang.Runtime.getRuntime().totalMemory();
    }

    public static final long getFreeMemory() {
        return java.lang.Runtime.getRuntime().freeMemory();
    }

    public static final int availableProcessors() {
        return java.lang.Runtime.getRuntime().availableProcessors();
    }

    public static final void exit(int status) {
        java.lang.Runtime.getRuntime().exit(status);
    }

    public static final void gc() {
        java.lang.Runtime.getRuntime().gc();
    }

    public static final void loadLibrary(String filename) {
        java.lang.Runtime.getRuntime().loadLibrary(filename);
    }

    public static synchronized Service register(Service s, URL url) {
        ServiceEnvironment se = null;
        if (!hosts.containsKey(url)) {
            se = new ServiceEnvironment(url);
            hosts.put(url, se);
        } else {
            se = hosts.get(url);
        }
        if (se.serviceDirectory.containsKey(s.getName())) {
            LOG.error("attempting to register " + s.getName() + " which is already registered in " + url);
            if (INSTANCE != null) {
                INSTANCE.invoke("collision", s.getName());
            }
            return s;
        } else {
            ServiceWrapper sw = new ServiceWrapper(s, se);
            se.serviceDirectory.put(s.getName(), sw);
            registry.put(s.getName(), sw);
            if (INSTANCE != null) {
                INSTANCE.invoke("registered", sw);
            }
        }
        return s;
    }

    /**
	 * registers a ServiceEnvironment which is a complete set of Services from a
	 * foreign instance of MRL. It returns whether changes have been made.  This
	 * is necessary to determine if the register should be echoed back.
	 * 
	 * @param url
	 * @param s
	 * @return
	 */
    public static synchronized boolean register(URL url, ServiceEnvironment s) {
        if (!hosts.containsKey(url)) {
            LOG.info("adding new ServiceEnvironment " + url);
        } else {
            ServiceEnvironment se = hosts.get(url);
            if (se.serviceDirectory.size() == s.serviceDirectory.size()) {
                boolean equal = true;
                s.serviceDirectory.keySet().iterator();
                Iterator<String> it = s.serviceDirectory.keySet().iterator();
                while (it.hasNext()) {
                    String serviceName = it.next();
                    if (!se.serviceDirectory.containsKey(serviceName)) {
                        equal = false;
                        break;
                    }
                }
                if (equal) {
                    LOG.info("ServiceEnvironment " + url + " already exists - with same count and names");
                    return false;
                }
            }
            LOG.info("replacing ServiceEnvironment " + url);
        }
        s.accessURL = url;
        hosts.put(url, s);
        s.serviceDirectory.keySet().iterator();
        Iterator<String> it = s.serviceDirectory.keySet().iterator();
        while (it.hasNext()) {
            String serviceName = it.next();
            LOG.info("adding " + serviceName + " to registry");
            registry.put(serviceName, s.serviceDirectory.get(serviceName));
            INSTANCE.invoke("registered", s.serviceDirectory.get(serviceName));
        }
        return true;
    }

    public static void unregister(URL url, String name) {
        if (!registry.containsKey(name)) {
            LOG.error("unregister " + name + " does not exist in registry");
        } else {
            registry.remove(name);
        }
        if (!hosts.containsKey(url)) {
            LOG.error("unregister environment does note exist for " + url + "." + name);
            return;
        }
        ServiceEnvironment se = hosts.get(url);
        if (!se.serviceDirectory.containsKey(name)) {
            LOG.error("unregister " + name + " does note exist for " + url + "." + name);
        } else {
            INSTANCE.invoke("released", se.serviceDirectory.get(name));
            se.serviceDirectory.remove(name);
        }
    }

    public static void unregisterAll(URL url) {
        if (!hosts.containsKey(url)) {
            LOG.error("unregisterAll " + url + " does not exist");
            return;
        }
        ServiceEnvironment se = hosts.get(url);
        Iterator<String> it = se.serviceDirectory.keySet().iterator();
        while (it.hasNext()) {
            String serviceName = it.next();
            unregister(url, serviceName);
        }
    }

    public static void unregisterAll() {
        Iterator<URL> it = hosts.keySet().iterator();
        while (it.hasNext()) {
            URL se = it.next();
            unregisterAll(se);
        }
    }

    public int getServiceCount() {
        int cnt = 0;
        Iterator<URL> it = hosts.keySet().iterator();
        while (it.hasNext()) {
            URL sen = it.next();
            ServiceEnvironment se = hosts.get(sen);
            Iterator<String> it2 = se.serviceDirectory.keySet().iterator();
            while (it2.hasNext()) {
                String serviceName = it2.next();
                ++cnt;
            }
        }
        return cnt;
    }

    public int getServiceEnvironmentCount() {
        return hosts.size();
    }

    public static ServiceEnvironment getLocalServices() {
        if (!hosts.containsKey(null)) {
            LOG.error("local (null) ServiceEnvironment does not exist");
            return null;
        }
        return hosts.get(null);
    }

    /**
	 * getLocalServicesForExport returns a filtered map of Service references
	 * to export to another instance of MRL.  The objective of filtering may help resolve
	 * functionality, security, or technical issues.  For example, the Dalvik JVM
	 * can only run certain Services.  It would be error prone to export a GUIService
	 * to a jvm which does not support swing. 
	 * 
	 * Since the map of Services is made for export - it is NOT a copy but references
	 * 
	 * The filtering is done by Service Type.. although in the future it could be extended
	 * to Service.getName()
	 * 
	 * @return
	 */
    public static ServiceEnvironment getLocalServicesForExport() {
        if (!hosts.containsKey(null)) {
            LOG.error("local (null) ServiceEnvironment does not exist");
            return null;
        }
        ServiceEnvironment local = hosts.get(null);
        inclusiveExportFilterEnabled = true;
        addInclusiveExportFilterServiceType("RemoteAdapter");
        addInclusiveExportFilterServiceType("SensorMonitor");
        addInclusiveExportFilterServiceType("Clock");
        addInclusiveExportFilterServiceType("Logging");
        addInclusiveExportFilterServiceType("GUIService");
        addInclusiveExportFilterServiceType("Runtime");
        if (!inclusiveExportFilterEnabled && !exclusiveExportFilterEnabled) {
            return local;
        }
        ServiceEnvironment export = new ServiceEnvironment(null);
        Iterator<String> it = local.serviceDirectory.keySet().iterator();
        while (it.hasNext()) {
            String name = it.next();
            ServiceWrapper sw = local.serviceDirectory.get(name);
            if (inclusiveExportFilterEnabled && inclusiveExportFilter.containsKey(sw.getServiceType())) {
                LOG.debug("adding " + name + " " + sw.getServiceType() + " to export ");
                ServiceWrapper sw2 = new ServiceWrapper(name, sw.get(), export);
                export.serviceDirectory.put(name, sw2);
            } else {
                LOG.debug("adding " + name + " with name info only");
                ServiceWrapper sw2 = new ServiceWrapper(name, null, export);
                export.serviceDirectory.put(name, sw2);
            }
        }
        return export;
    }

    public static void addInclusiveExportFilterServiceType(String packageName, String className) {
        inclusiveExportFilter.put(packageName + "." + className, className);
    }

    public static void addInclusiveExportFilterServiceType(String shortClassName) {
        inclusiveExportFilter.put("org.myrobotlab.service." + shortClassName, shortClassName);
    }

    public static ServiceWrapper getService(String name) {
        if (!registry.containsKey(name)) {
            LOG.debug("service " + name + " does not exist");
            return null;
        }
        return registry.get(name);
    }

    public static ServiceWrapper getService(URL url, String name) {
        if (!hosts.containsKey(url)) {
            LOG.error("getService environment does note exist for " + url + "." + name);
            return null;
        }
        ServiceEnvironment se = hosts.get(url);
        if (!se.serviceDirectory.containsKey(name)) {
            LOG.error("getService " + name + " does note exist for " + url + "." + name);
            return null;
        }
        return se.serviceDirectory.get(name);
    }

    public static boolean release(String name) {
        return release(null, name);
    }

    public static boolean release(URL url, String name) {
        ServiceWrapper sw = getService(url, name);
        if (sw != null) {
            if (sw.isValid()) {
                sw.get().stopService();
                registry.remove(name);
                ServiceEnvironment se = hosts.get(url);
                INSTANCE.invoke("released", se.serviceDirectory.get(name));
                se.serviceDirectory.remove(name);
                return true;
            }
        }
        return false;
    }

    public static void release(URL url) {
        ServiceEnvironment se = hosts.get(url);
        Iterator<String> it = se.serviceDirectory.keySet().iterator();
        while (it.hasNext()) {
            String name = it.next();
            release(url, name);
        }
    }

    public static void releaseAll() {
        LOG.debug("releaseAll");
        Iterator<URL> it = hosts.keySet().iterator();
        while (it.hasNext()) {
            URL sen = it.next();
            ServiceEnvironment se = hosts.get(sen);
            Iterator<String> seit = se.serviceDirectory.keySet().iterator();
            while (seit.hasNext()) {
                String serviceName = seit.next();
                ServiceWrapper sw = se.serviceDirectory.get(serviceName);
                LOG.info("stopping service " + se.accessURL + "/" + serviceName);
                if (sw.service != null) {
                    sw.service.stopService();
                } else {
                    LOG.warn("unknown type and/or remote service");
                }
            }
        }
        it = hosts.keySet().iterator();
        while (it.hasNext()) {
            URL sen = it.next();
            ServiceEnvironment se = hosts.get(sen);
            LOG.info("clearing environment " + se.accessURL);
            se.serviceDirectory.clear();
        }
        LOG.info("clearing hosts environments");
        hosts.clear();
        LOG.info("clearing registry");
        registry.clear();
    }

    public static HashMap<String, ServiceWrapper> getRegistry() {
        return registry;
    }

    public static ServiceEnvironment getServiceEnvironment(URL url) {
        if (hosts.containsKey(url)) {
            return hosts.get(url);
        }
        return null;
    }

    public static HashMap<URL, ServiceEnvironment> getServiceEnvironments() {
        return new HashMap<URL, ServiceEnvironment>(hosts);
    }

    public static HashMap<String, MethodEntry> getMethodMap(String serviceName) {
        if (!registry.containsKey(serviceName)) {
            LOG.error(serviceName + " not in registry - can not return method map");
            return null;
        }
        HashMap<String, MethodEntry> ret = new HashMap<String, MethodEntry>();
        ServiceWrapper sw = registry.get(serviceName);
        Class<?> c = sw.service.getClass();
        Method[] methods = c.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            Method m = methods[i];
            if (!hideMethods.containsKey(m.getName())) {
                MethodEntry me = new MethodEntry();
                me.name = m.getName();
                me.parameterTypes = m.getParameterTypes();
                me.returnType = m.getReturnType();
                String s = MethodEntry.getSignature(me.name, me.parameterTypes, me.returnType);
                ret.put(s, me);
            }
        }
        return ret;
    }

    public static boolean save(String filename) {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(filename);
            out = new ObjectOutputStream(fos);
            out.writeObject(hosts);
            out.writeObject(registry);
            out.writeObject(hideMethods);
        } catch (Exception e) {
            Service.logException(e);
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static boolean load(String filename) {
        try {
            FileInputStream fis;
            fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            hosts = (HashMap<URL, ServiceEnvironment>) in.readObject();
            registry = (HashMap<String, ServiceWrapper>) in.readObject();
            hideMethods = (HashMap<String, String>) in.readObject();
            in.close();
        } catch (Exception e) {
            Service.logException(e);
            return false;
        }
        return true;
    }

    public static void startLocalServices() {
        ServiceEnvironment se = getLocalServices();
        Iterator<String> it = se.serviceDirectory.keySet().iterator();
        while (it.hasNext()) {
            String serviceName = it.next();
            ServiceWrapper sw = se.serviceDirectory.get(serviceName);
            sw.service.startService();
        }
    }

    /**
	 * a method which returns a xml representation of all the listeners and routes in the
	 * runtime system
	 * @return
	 */
    public static String dumpNotifyEntries() {
        StringBuffer sb = new StringBuffer();
        sb.append("<NotifyEntries>");
        Iterator<String> it = registry.keySet().iterator();
        while (it.hasNext()) {
            String serviceName = it.next();
            ServiceWrapper sw = registry.get(serviceName);
            sb.append("<service name=\"" + sw.getName() + "\" serviceEnironment=\"" + sw.getAccessURL() + "\">");
            Iterator<String> nit = sw.getNotifyListKeySet().iterator();
            while (nit.hasNext()) {
                String n = nit.next();
                sb.append("<notify map=\"" + n + "\">");
                ArrayList<NotifyEntry> nes = sw.getNotifyList(n);
                for (int i = 0; i < nes.size(); ++i) {
                    NotifyEntry ne = nes.get(i);
                    sb.append("<notifyEntry outMethod=\"" + ne.outMethod + "\" name=\"" + ne.name + "\" inMethod=\"" + ne.outMethod + "\" />");
                }
                sb.append("</notify>");
            }
            sb.append("</service>");
        }
        sb.append("</NotifyEntries>");
        return sb.toString();
    }

    public static Vector<String> getServicesFromInterface(String interfaceName) {
        Vector<String> ret = new Vector<String>();
        Iterator<String> it = registry.keySet().iterator();
        while (it.hasNext()) {
            String serviceName = it.next();
            ServiceWrapper sw = registry.get(serviceName);
            Class<?> c = sw.service.getClass();
            Class<?>[] interfaces = c.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                Class<?> m = interfaces[i];
                if (m.getCanonicalName().equals(interfaceName)) {
                    ret.add(sw.service.getName());
                }
            }
        }
        return ret;
    }

    public static void requestRestart() {
        needsRestart = true;
    }

    public static boolean needsRestart() {
        return needsRestart;
    }

    /**
	 * registration event
	 * @param name - the name of the Service which was successfully registered
	 * @return
	 */
    public ServiceWrapper registered(ServiceWrapper sw) {
        return sw;
    }

    /**
	 * release event 
	 * @param name - the name of the Service which was successfully released
	 * @return
	 */
    public ServiceWrapper released(ServiceWrapper sw) {
        return sw;
    }

    /**
	 * collision event - when a registration is attempted but there is a 
	 * name collision
	 * @param name - the name of the two Services with the same name
	 * @return
	 */
    public String collision(String name) {
        return name;
    }

    static void help() {
        System.out.println("Runtime " + version());
        System.out.println("-h       			# help ");
        System.out.println("-list        		# list services");
        System.out.println("-logToConsole       # redirects logging to console");
        System.out.println("-logLevel        	# log level [DEBUG | INFO | WARNING | ERROR | FATAL]");
        System.out.println("-service [Service Name] [Service] ...");
        System.out.println("example:");
        System.out.println(helpString);
    }

    static String version() {
        String v = FileIO.getResourceFile("version.txt");
        System.out.println(v);
        return v;
    }

    static String helpString = "java -Djava.library.path=./libraries/native/x86.32.windows org.myrobotlab.service.Runtime -service gui GUIService -logLevel DEBUG -logToConsole";

    public static final void invokeCMDLine(CMDLine cmdline) {
        if (cmdline.containsKey("-h") || cmdline.containsKey("--help")) {
            help();
            return;
        }
        if (cmdline.containsKey("-v") || cmdline.containsKey("--version")) {
            version();
            return;
        }
        System.out.println("service count " + cmdline.getArgumentCount("-service") / 2);
        if (cmdline.getArgumentCount("-service") > 0 && cmdline.getArgumentCount("-service") % 2 == 0) {
            for (int i = 0; i < cmdline.getArgumentCount("-service"); i += 2) {
                LOG.info("attempting to invoke : org.myrobotlab.service." + cmdline.getSafeArgument("-service", i + 1, "") + " named " + cmdline.getSafeArgument("-service", i, ""));
                String name = cmdline.getSafeArgument("-service", i, "");
                String type = cmdline.getSafeArgument("-service", i + 1, "");
                ServiceInterface s = Runtime.create(name, type);
                if (s != null) {
                    s.startService();
                } else {
                    LOG.error("could not create service " + name + " " + type);
                }
                if (s.hasDisplay()) {
                    gui = s;
                }
            }
            if (gui != null) {
                gui.display();
            }
        } else if (cmdline.hasSwitch("-list")) {
            System.out.println(getServiceShortClassNames());
        } else {
            help();
            return;
        }
    }

    public static String[] getServiceShortClassNames() {
        return getServiceShortClassNames(null);
    }

    public static String[] getServiceShortClassNames(String filter) {
        return ServiceInfo.getInstance().getShortClassNames(filter);
    }

    /**
	 * @param name - name of Service to be removed and whos resources will be released
	 */
    public static void releaseService(String name) {
        Runtime.release(name);
    }

    /**
	 * this "should" be the gateway function to a MyRobotLab instance
	 * going through this main will allow the see{@link}MyRobotLabClassLoader 
	 * to load the appropriate classes and give access to the addURL to allow dynamic
	 * additions of new modules without having to restart.
	 * 
	 * TODO :   -cmd <method> invokes the appropriate static method e.g. -cmd setLogLevel DEBUG
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        URL url = null;
        try {
            url = new URL("http://0.0.0.0:0");
        } catch (MalformedURLException e2) {
            Service.logException(e2);
        }
        System.out.println(url.getHost());
        System.out.println(url.getPort());
        CMDLine cmdline = new CMDLine();
        cmdline.splitLine(args);
        try {
            if (cmdline.containsKey("-logToConsole")) {
                addAppender(LOGGING_APPENDER_CONSOLE);
                setLogLevel(LOG_LEVEL_DEBUG);
            } else if (cmdline.containsKey("-logToRemote")) {
                String host = cmdline.getSafeArgument("-logToRemote", 0, "localhost");
                String port = cmdline.getSafeArgument("-logToRemote", 1, "4445");
                addAppender(LOGGING_APPENDER_SOCKET, host, port);
                setLogLevel(LOG_LEVEL_DEBUG);
            } else {
                addAppender(LOGGING_APPENDER_ROLLING_FILE);
                setLogLevel(LOG_LEVEL_WARN);
            }
            if (cmdline.containsKey("-logLevel")) {
                setLogLevel(cmdline.getSafeArgument("-logLevel", 0, "DEBUG"));
            }
            if (cmdline.containsKey("-update")) {
                updateAll();
                return;
            } else {
                invokeCMDLine(cmdline);
            }
        } catch (Exception e) {
            Service.logException(e);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static ServiceInterface createAndStart(String name, String type) {
        ServiceInterface s = create(name, type);
        if (s == null) {
            LOG.error("cannot start service " + name);
            return null;
        }
        s.startService();
        return s;
    }

    public static synchronized ServiceInterface create(String name, String type) {
        return create(name, "org.myrobotlab.service.", type);
    }

    /**
	 * @param name - name of Service
	 * @param pkgName - package of Service in case Services are created in different packages
	 * @param type - type of Service
	 * @return
	 */
    public static synchronized ServiceInterface create(String name, String pkgName, String type) {
        try {
            LOG.debug("Runtime.create - Class.forName");
            String typeName = pkgName + type;
            return createService(name, typeName);
        } catch (Exception e) {
            Service.logException(e);
        }
        return null;
    }

    /**
	 * FIXME - deprecate - require calling code to implement loop - support only
	 * the single update(fullTypeName) - that way calling code can handle
	 * detailed info such as reporting to gui/console which components are being
	 * updated and which have errors in the update process.  Will need a list of all
	 * or filtered  ArrayList<fullTypeName>
	 * 
	 * update - force system to check for all dependencies of all possible
	 * Services - Ivy will attempt to check & fufill dependencies by downloading
	 * jars from the repo 
	 */
    public static void updateAll() {
        boolean getNewRepoData = true;
        serviceInfo.clearErrors();
        if (getNewRepoData) serviceInfo.getRepoServiceData("serviceData.xml");
        if (!serviceInfo.hasErrors()) {
            serviceInfo.update();
            if (!serviceInfo.hasErrors()) {
                return;
            }
        }
        ArrayList<String> errors = serviceInfo.getErrors();
        for (int i = 0; i < errors.size(); ++i) {
            LOG.error(errors.get(i));
        }
    }

    /**
	 * publishing point of Ivy sub system - sends even failedDependency when the
	 * retrieve report for a Service fails
	 * @param dep
	 * @return
	 */
    public String failedDependency(String dep) {
        return dep;
    }

    /**
	 * @param name
	 * @param cls
	 * @return
	 */
    public static synchronized ServiceInterface createService(String name, String fullTypeName) {
        LOG.debug("Runtime.createService");
        if (name == null || name.length() == 0 || fullTypeName == null || fullTypeName.length() == 0) {
            LOG.error(fullTypeName + " not a type or " + name + " not defined ");
            return null;
        }
        ServiceWrapper sw = Runtime.getService(name);
        if (sw != null) {
            LOG.debug("service " + name + " already exists");
            return sw.service;
        }
        try {
            LOG.debug("ABOUT TO LOAD CLASS");
            LOG.info("loader for this class " + Runtime.class.getClassLoader().getClass().getCanonicalName());
            LOG.info("parent " + Runtime.class.getClassLoader().getParent().getClass().getCanonicalName());
            LOG.info("system class loader " + ClassLoader.getSystemClassLoader());
            LOG.info("parent should be null" + ClassLoader.getSystemClassLoader().getParent().getClass().getCanonicalName());
            LOG.info("thread context " + Thread.currentThread().getContextClassLoader().getClass().getCanonicalName());
            LOG.info("thread context parent " + Thread.currentThread().getContextClassLoader().getParent().getClass().getCanonicalName());
            LOG.info("refreshing classloader");
            Class<?> cls = Class.forName(fullTypeName);
            Constructor<?> constructor = cls.getConstructor(new Class[] { String.class });
            Object newService = constructor.newInstance(new Object[] { name });
            LOG.info("returning " + fullTypeName);
            return (Service) newService;
        } catch (Exception e) {
            Service.logException(e);
        }
        return null;
    }

    public String dump() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nhosts:\n");
        Iterator<URL> hkeys = hosts.keySet().iterator();
        while (hkeys.hasNext()) {
            URL url = hkeys.next();
            ServiceEnvironment se = hosts.get(url);
            sb.append("\t");
            sb.append(url);
            if ((se.accessURL != url) && (!url.equals(se.accessURL))) {
                sb.append(" key not equal to data ");
                sb.append(se.accessURL);
            }
            sb.append("\n");
            Iterator<String> it2 = se.serviceDirectory.keySet().iterator();
            while (it2.hasNext()) {
                String serviceName = it2.next();
                ServiceWrapper sw = se.serviceDirectory.get(serviceName);
                sb.append("\t\t");
                sb.append(serviceName);
                if ((sw.name != sw.name) && (!serviceName.equals(sw.name))) {
                    sb.append(" key not equal to data ");
                    sb.append(sw.name);
                }
                if ((sw.host.accessURL != url) && (!sw.host.accessURL.equals(url))) {
                    sb.append(" service wrapper host accessURL " + sw.host.accessURL + " not equal to " + url);
                }
                sb.append("\n");
            }
        }
        sb.append("\nregistry:");
        Iterator<String> rkeys = registry.keySet().iterator();
        while (rkeys.hasNext()) {
            String serviceName = rkeys.next();
            ServiceWrapper sw = registry.get(serviceName);
            sb.append("\n");
            sb.append(serviceName);
            sb.append(" ");
            sb.append(sw.host.accessURL);
        }
        return sb.toString();
    }

    /**
	 * this method attempts to connect to the repo
	 * and populate information regarding the latest ServiceDescriptors
	 * and their latest dependencies
	 */
    public static void checkForUpdates() {
        serviceInfo.getLocalServiceData();
        serviceInfo.getRepoData();
        INSTANCE.invoke("proposedUpdates", serviceInfo);
    }

    /**
	 * this method is an event notifier
	 * that there were updates found
	 */
    public ServiceInfo proposedUpdates(ServiceInfo si) {
        return si;
    }

    public static void installLatestAll() {
    }
}
