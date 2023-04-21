package net.sourceforge.pyrus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.sourceforge.pyrus.hal.Config;
import net.sourceforge.pyrus.hal.Discovery;
import net.sourceforge.pyrus.hal.MObject;
import net.sourceforge.pyrus.hal.Discovery.MObjectDesc;
import net.sourceforge.pyrus.hal.annotation.Dependency;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author izaera@gmail.com
 */
class KernelImpl extends net.sourceforge.pyrus.hal.Kernel {

    private static final Log log = LogFactory.getLog(KernelImpl.class);

    private static final String CONFIG_FILE = "config";

    private Map<String, MObject> mobjects = new HashMap<String, MObject>();

    private Map<MObject, Thread> threads = new HashMap<MObject, Thread>();

    private Properties props = new Properties();

    private ConfigImpl config;

    private DiscoveryImpl discovery;

    private KernelListeners listeners = new KernelListeners();

    private int threadStopTimeout = 5;

    private boolean stop = false;

    private String installPath = "/pyrus";

    private String rebootFile = "work/.pyrus-last-run";

    private String afterRunFile = "work/.pyrus-after-run";

    private OSType osType;

    private OSArch osArch;

    protected KernelImpl() {
        log.info("ctor - detecting hardware architecture");
        String _osType = System.getProperty("os.name");
        if (_osType.equalsIgnoreCase("linux")) {
            osType = OSType.LINUX;
        } else {
            throw new KernelPanicException("Unsupported OS type: " + _osType);
        }
        String _osArch = System.getProperty("os.arch");
        if (_osArch.equalsIgnoreCase("i386")) {
            osArch = OSArch.I386;
        } else {
            throw new KernelPanicException("Unsupported OS architecture: " + _osArch);
        }
        log.info("ctor - loading configuration");
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
            props.load(is);
            is.close();
        } catch (Exception e) {
            throw new KernelPanicException("Cannot load kernel configuration", e);
        }
        if (props.getProperty("installPath") != null) {
            installPath = props.getProperty("installPath");
            if (installPath.endsWith("/") || installPath.endsWith("\\")) {
                installPath = installPath.substring(0, installPath.length() - 1);
            }
        }
        log.info("ctor - installPath=" + installPath);
        if (props.getProperty("rebootFile") != null) {
            rebootFile = props.getProperty("rebootFile");
        }
        log.info("ctor - rebootFile=" + rebootFile);
        if (props.getProperty("afterRunFile") != null) {
            afterRunFile = props.getProperty("afterRunFile");
        }
        log.info("ctor - afterRunFile=" + afterRunFile);
        if (props.getProperty("threadStopTimeout") != null) {
            threadStopTimeout = Integer.parseInt(props.getProperty("threadStopTimeout"));
        }
        log.info("ctor - threadStopTimeout=" + threadStopTimeout);
        log.info("ctor - activating antialiasing for text");
        System.setProperty("swing.aatext", "true");
        config = new ConfigImpl(this);
        try {
            discovery = new DiscoveryImpl();
        } catch (Exception e) {
            throw new KernelPanicException("Cannot start discovery service", e);
        }
    }

    public OSType getOSType() {
        return osType;
    }

    public OSArch getOSArch() {
        return osArch;
    }

    public void run() {
        try {
            log.info("run - booting");
            log.info("run - starting preinstalled MObjects");
            startMObjects();
            log.info("run - running loop");
            loop();
            log.info("run - stopping managed objects");
            for (MObject mo : mobjects.values()) {
                stop(mo);
            }
            log.info("run - kernel shut down");
            System.exit(0);
        } catch (Throwable t) {
            log.fatal("run - kernel panic", t);
        }
    }

    public void addListener(Listener listener) {
        listeners.addListener(listener);
    }

    public void removeListener(Listener listener) {
        listeners.removeListener(listener);
    }

    /**
	 * Get kernel installation path
	 * @return directory where kernel is installed
	 */
    public String getInstallPath() {
        return installPath;
    }

    /**
	 * Get configuration service
	 * @return configuration service
	 */
    public Config getConfig() {
        return config;
    }

    /**
	 * Get discovery service
	 * @return discovery service
	 */
    public Discovery getDiscovery() {
        return discovery;
    }

    /**
	 * Install a managed object. The id of the object will be the full name of
	 * the clazz followed by a semicolon (:) and the subId. If subId is null no
	 * semicolon is appended.
	 * @param object the managed object
	 * @param clazz the interface of the object (to compose its id)
	 * @param subId the optional subId of the object
	 */
    public void installMObject(MObject object, Class<? extends MObject> clazz, String subId) {
        if (subId != null && !subId.equals("")) {
            object.setId(clazz.getName() + "-" + subId);
        } else {
            object.setId(clazz.getName());
        }
        synchronized (mobjects) {
            populateInjections(object);
            start(object);
            if (mobjects.containsKey(object.getId())) {
                throw new KernelPanicException("Managed object already installed: " + object.getDescription());
            }
            log.info("installMObject - installing managed object: [" + object.getId() + "] " + object.getDescription());
            mobjects.put(object.getId(), object);
        }
        listeners.objectInstalled(new Event(this, object, clazz, subId));
    }

    /**
	 * Install a managed object
	 * @param object the managed object
	 */
    public void installMObject(MObject object, Class<? extends MObject> clazz) {
        installMObject(object, clazz, null);
    }

    /**
	 * Get a singleton managed object
	 * @param <T> type of object
	 * @param clazz class of object
	 * @return the managed object
	 */
    public <T extends MObject> T getMObject(Class<T> clazz) {
        return getMObject(clazz, null);
    }

    /**
	 * Get a managed object by id
	 * @param <T> type of object
	 * @param clazz class of object
	 * @param id id of object
	 * @return the managed object
	 */
    @SuppressWarnings("unchecked")
    public <T extends MObject> T getMObject(Class<T> clazz, String id) {
        synchronized (mobjects) {
            if (id != null && !id.equals("")) {
                return (T) mobjects.get(clazz.getName() + ":" + id);
            } else {
                return (T) mobjects.get(clazz.getName());
            }
        }
    }

    public void enumMObjects(Class<? extends MObject> clazz, Listener receiver) {
        synchronized (mobjects) {
            for (String key : mobjects.keySet()) {
                if (key.startsWith(clazz.getName())) {
                    MObject mo = mobjects.get(key);
                    Event ev = new Event(this, mo, getInterface(mo), getSubId(mo));
                    try {
                        receiver.objectInstalled(ev);
                    } catch (Exception e) {
                        log.error("enumMObjects - error invoking listener", e);
                    }
                }
            }
        }
    }

    /**
	 * Execute a command 
	 * @param cmd the command to execute
	 * @return true if executed correctly, false otherwise
	 */
    public boolean exec(String cmd) {
        try {
            Runtime.getRuntime().exec(installPath + "/bin/" + cmd);
            return true;
        } catch (IOException e) {
            log.error("exec - cannot execute: " + cmd, e);
            return false;
        }
    }

    /**
	 * Sleep for millis milliseconds
	 * @param millis milliseconds to sleep
	 * @return true if sleep was successful, false if sleep was interrupted
	 */
    public boolean sleep(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
	 * Shutdown kernel
	 */
    public void shutdown() {
        shutdown(null);
    }

    /**
	 * Shutdown kernel and run a command after
	 * @param afterRunCommand the command to run after kernel shutdown
	 */
    public void shutdown(String afterRunCommand) {
        log.info("shutdown - afterRunCommand=" + afterRunCommand);
        if (afterRunCommand != null) {
            try {
                Writer fw = new OutputStreamWriter(new FileOutputStream(afterRunFile), "UTF-8");
                fw.write(afterRunCommand);
                fw.close();
            } catch (Exception e) {
                log.error("shutdown - cannot schedule after-run command: " + afterRunCommand);
            }
        }
        File f = new File(rebootFile);
        if (!f.delete()) {
            log.warn("shutdown - cannot delete reboot file: " + rebootFile);
            f.deleteOnExit();
        }
        stop = true;
        synchronized (this) {
            notify();
        }
    }

    /**
	 * Reboot kernel
	 */
    @Override
    public void reboot() {
        log.info("reboot");
        stop = true;
        synchronized (this) {
            notify();
        }
    }

    private void startMObjects() {
        List<MObject> pending = new ArrayList<MObject>();
        List<MObject> ordered = new ArrayList<MObject>();
        Map<String, Boolean> addedIds = new HashMap<String, Boolean>();
        try {
            log.info("startMObject - creating preinstalled objects");
            for (MObjectDesc desc : discovery.getMObjectDescs()) {
                String id = desc.getInterface().getName();
                if (desc.getSubId() != null) {
                    id += "-" + desc.getSubId();
                }
                MObject obj = desc.getType().newInstance();
                obj.setId(id);
                mobjects.put(id, obj);
                log.debug("startMObject - created: " + obj.getId());
            }
        } catch (Exception e) {
            throw new KernelPanicException("Cannot create preinstalled MObject", e);
        }
        pending.addAll(mobjects.values());
        while (pending.size() > 0) {
            List<MObject> addedThisRound = new ArrayList<MObject>();
            for (int i = 0; i < pending.size(); i++) {
                MObject mo = pending.get(i);
                String[] deps = getDependencies(mo);
                if (deps == null || deps.length == 0) {
                    ordered.add(mo);
                    addedIds.put(mo.getId(), true);
                    addedThisRound.add(mo);
                } else {
                    boolean depsLoaded = true;
                    for (String dep : deps) {
                        if (!addedIds.containsKey(dep)) {
                            depsLoaded = false;
                            break;
                        }
                    }
                    if (depsLoaded) {
                        ordered.add(mo);
                        addedIds.put(mo.getId(), true);
                        addedThisRound.add(mo);
                    }
                }
            }
            log.debug("startMObject - new ordering round");
            if (addedThisRound.size() > 0) {
                for (MObject mo : addedThisRound) {
                    log.debug("startMObject - added: " + mo.getId());
                    pending.remove(mo);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Some managed objects have unsatisfied dependencies: ");
                sb.append("(pending: ");
                for (MObject mo : pending) {
                    sb.append("[");
                    sb.append(mo.getDescription());
                    sb.append("]");
                }
                sb.append(") (ordered: ");
                for (MObject mo : ordered) {
                    sb.append(" [");
                    sb.append(mo.getDescription());
                    sb.append("]");
                }
                sb.append(")");
                throw new KernelPanicException(sb.toString());
            }
        }
        for (MObject mo : ordered) {
            populateInjections(mo);
            log.info("start - starting managed object: [" + mo.getId() + "] " + mo.getDescription());
            mo.start();
            listeners.objectInstalled(new Event(this, mo, getInterface(mo), getSubId(mo)));
        }
        for (MObject mo : ordered) {
            if (mo instanceof Runnable) {
                Thread t = new Thread((Runnable) mo);
                t.setDaemon(true);
                t.start();
                threads.put(mo, t);
            }
        }
    }

    private void loop() {
        while (!stop) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                if (!stop) {
                    log.warn("dispatchEvents - exception in kernel loop", e);
                    sleep(1000);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private String[] getDependencies(MObject mo) {
        List<String> deps = new ArrayList<String>();
        for (Field f : mo.getClass().getDeclaredFields()) {
            for (Annotation an : f.getAnnotations()) {
                if (an instanceof Dependency) {
                    Dependency min = (Dependency) an;
                    Class<? extends MObject> type = min.value();
                    if (type == MObject.class) {
                        type = (Class<? extends MObject>) f.getType();
                    }
                    String id = min.id();
                    if (id != null && !id.equals("")) {
                        deps.add(type.getName() + ":" + id);
                    } else {
                        deps.add(type.getName());
                    }
                }
            }
        }
        return deps.toArray(new String[] {});
    }

    @SuppressWarnings("unchecked")
    private void populateInjections(MObject mo) {
        log.info("populateInjections - populating dependencies of managed object: [" + mo.getId() + "] " + mo.getDescription());
        for (Field f : mo.getClass().getDeclaredFields()) {
            for (Annotation an : f.getAnnotations()) {
                if (an instanceof Dependency) {
                    Dependency min = (Dependency) an;
                    Class<? extends MObject> type = min.value();
                    if (type == MObject.class) {
                        type = (Class<? extends MObject>) f.getType();
                    }
                    String id = min.id();
                    MObject dep = getMObject(type, id);
                    f.setAccessible(true);
                    try {
                        f.set(mo, dep);
                    } catch (Exception e) {
                        throw new KernelPanicException("Cannot populate field " + f.getName() + " of managed object: " + mo.getId(), e);
                    }
                }
            }
        }
    }

    private void start(MObject mo) {
        log.info("start - starting managed object: [" + mo.getId() + "] " + mo.getDescription());
        mo.start();
        if (mo instanceof Runnable) {
            Thread t = new Thread((Runnable) mo);
            t.setDaemon(true);
            t.start();
            threads.put(mo, t);
        }
    }

    @SuppressWarnings("deprecation")
    private void stop(MObject mo) {
        log.info("stop - stopping managed object: [" + mo.getId() + "] " + mo.getDescription());
        try {
            mo.stop();
        } catch (Throwable t) {
            log.error("stop - error stopping managed object: [" + mo.getId() + "] " + mo.getDescription(), t);
        }
        Thread t = threads.get(mo);
        if (t != null) {
            int loop;
            for (loop = 0; loop < threadStopTimeout; loop++) {
                if (!t.isAlive()) {
                    break;
                } else {
                    log.warn("stop - waiting for runnable managed object to stop: [" + mo.getId() + "] " + mo.getDescription());
                    sleep(1000);
                }
            }
            if (t.isAlive()) {
                log.error("stop - forcing stop of runnable managed object: [" + mo.getId() + "] " + mo.getDescription());
                threads.get(mo).stop();
            } else {
                log.info("stop - runnable managed object stopped gracefully: [" + mo.getId() + "] " + mo.getDescription());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends MObject> getInterface(MObject obj) {
        String[] parts = obj.getId().split("-");
        try {
            return (Class<? extends MObject>) Class.forName(parts[0]);
        } catch (ClassNotFoundException e) {
            throw new KernelPanicException("Cannot determine object interface: " + obj.getId(), e);
        }
    }

    private String getSubId(MObject obj) {
        String[] parts = obj.getId().split("-");
        return parts.length > 1 ? parts[1] : null;
    }
}
