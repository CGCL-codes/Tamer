package net.sf.oxygen.client;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import java.util.jar.JarFile;
import net.sf.oxygen.client.helper.ComponentListenerHelper;
import net.sf.oxygen.client.helper.ComponentRecord;
import net.sf.oxygen.client.helper.ComponentRegsitry;
import net.sf.oxygen.client.loader.JarComponentLoader;
import net.sf.oxygen.client.loader.NotExportedException;
import net.sf.oxygen.core.component.*;
import net.sf.oxygen.core.event.ComponentEvent;
import net.sf.oxygen.core.event.ComponentListener;
import org.apache.log4j.Logger;

/**
 * The central clearing house for components (and services) in the oxygen
 * framework. If one class is at the core of oxygen - this is it.
 * @author <A HREF="mailto:seniorc@users.sourceforge.net?subject=net.sf.oxygen.client.ComponentManagerImpl">Chris Senior</A>
 */
public class ComponentManagerImpl implements ComponentManager {

    /**
   * For class debug
   */
    private static final Logger logger = Logger.getLogger(ComponentManagerImpl.class);

    /**
   * Component registry handles component state information
   */
    private ComponentRegsitry componentRegistry;

    /**
   * Component listener helper - manages listeners for component events
   */
    private ComponentListenerHelper componentListenerHelper;

    /**
   * A list of the loaders used for components
   */
    private List componentLoaders;

    /**
   * A root directory for framework persistent data
   */
    private File frameworkRoot;

    /**
   * The run levels in the system
   * @associates <{net.sf.oxygen.client.RunLevelImpl}>
   * @supplierCardinality 1
   */
    private Vector runLevels;

    /**
   * The current run level
   * @supplierCardinality 0..**/
    private RunLevelImpl currentLevel;

    /**
   * Create component manager
   */
    public ComponentManagerImpl() {
        File rootDir = new File("oxygen-framework");
        if (!rootDir.exists()) {
            rootDir.mkdir();
            logger.debug("Made new framework directory=" + rootDir.getAbsolutePath());
        }
        logger.debug("Creating component manager using root=" + rootDir);
        this.componentRegistry = new ComponentRegsitry();
        this.componentListenerHelper = new ComponentListenerHelper();
        this.componentLoaders = new Vector();
        this.frameworkRoot = rootDir;
        this.runLevels = new Vector();
        this.currentLevel = new RunLevelImpl();
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#install(File)
   */
    public ComponentIdentifier install(File f) throws IllegalComponentStateException {
        return install(f, f.getAbsolutePath());
    }

    /**
   * Implementation of install - used by both public install methods
   * @param f The file (local) to install
   * @param location The original download location (if different)
   * @return The new component id
   * @throws IllegalComponentStateException If already installed
   */
    private synchronized ComponentIdentifier install(File f, String location) throws IllegalComponentStateException {
        logger.debug("Install file=" + f + "; location=" + location);
        ComponentIdentifier id = null;
        if (componentRegistry.containsJar(f.getAbsolutePath())) throw new IllegalComponentStateException("Component from JAR=" + f.getAbsolutePath() + "; Already installed");
        try {
            JarFile jar = new JarFile(f);
            logger.debug("Opened jar file");
            JarComponentLoader loader = new JarComponentLoader(jar, componentRegistry.nextAvailableID(), this);
            id = loader.getComponentIdentifier();
            componentLoaders.add(loader);
            logger.debug("New component has ID=" + id);
            ComponentRecord record = new ComponentRecord(id, f.getAbsolutePath(), location, ComponentState.INSTALLED);
            componentRegistry.addComponentRecord(record);
            logger.debug("Created new component record: " + record);
            logger.debug("Notify component listeners...");
            componentListenerHelper.fireComponentInstalled(new ComponentEvent(id, this));
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
        logger.info("New component installed ID=" + id);
        return id;
    }

    /**
   * @see ComponentManager#install(String)
   */
    public ComponentIdentifier install(String location) throws IllegalComponentStateException {
        logger.debug("Install from location=" + location);
        File localFile = null;
        try {
            URL url = new URL(location);
            logger.debug("Got URL=" + url);
            localFile = new File(frameworkRoot, url.getFile());
            int read = 0;
            InputStream in = null;
            OutputStream out = null;
            try {
                logger.debug("Writing to local file=" + localFile.getAbsolutePath());
                in = url.openStream();
                logger.debug("Got input stream");
                out = new FileOutputStream(localFile);
                logger.debug("Got output stream");
                boolean reading = true;
                byte[] buffer = new byte[1024];
                int len = 10;
                while (len > 0) {
                    len = in.read(buffer);
                    if (len > 0) {
                        out.write(buffer, 0, len);
                        read += len;
                        logger.debug("Transferred block size=" + len + " bytes");
                    } else {
                        logger.debug("EOF encountered after " + read + " bytes; stop transfer");
                        reading = false;
                    }
                }
            } catch (IOException ioe) {
                logger.error("General IO problem after " + read + " bytes", ioe);
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    logger.debug("LocalController and remote files transferred " + read + " bytes - complete");
                } catch (IOException e1) {
                    logger.error("Unable to close streams after transfer", e1);
                }
            }
        } catch (MalformedURLException e) {
            logger.debug("Not a real URL=" + location + "; trying as local file...");
            localFile = new File(location);
        }
        return install(localFile, location);
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#installedComponents()
   */
    public List installedComponents() {
        List results = componentRegistry.getComponentIdentifiers(ComponentState.INSTALLED);
        results.addAll(componentRegistry.getComponentIdentifiers(ComponentState.RUNNING));
        return results;
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#runningComponents()
   */
    public List runningComponents() {
        return componentRegistry.getComponentIdentifiers(ComponentState.RUNNING);
    }

    /**
   * Utility to find a loader for a given component
   * @param id
   * @return
   */
    private JarComponentLoader getLoader(ComponentIdentifier id) {
        Iterator iter = componentLoaders.iterator();
        while (iter.hasNext()) {
            JarComponentLoader loader = (JarComponentLoader) iter.next();
            if (loader.getComponentIdentifier().equals(id)) {
                return loader;
            }
        }
        return null;
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#start(net.sf.oxygen.core.component.ComponentIdentifier)
   */
    public void start(ComponentIdentifier id) throws IllegalComponentStateException, IllegalRunLevelException {
        startImpl(id);
        currentLevel.addComponent(id);
    }

    /**
   * Actually starts a component but does not call any run level methods
   * @param id
   * @throws IllegalComponentStateException
   */
    private void startImpl(ComponentIdentifier id) throws IllegalComponentStateException {
        logger.debug("Trying to start " + id);
        ComponentRecord record = componentRegistry.getRecordFor(id);
        if (record == null) {
            logger.error("No such component as component " + id);
            throw new IllegalArgumentException("No such component where id=" + id);
        }
        if (record.getState().equals(ComponentState.RUNNING)) {
            logger.warn(id + " was already running");
            return;
        } else if (!record.getState().equals(ComponentState.INSTALLED)) {
            logger.error(id + " is not installed");
            throw new IllegalComponentStateException(id + " is not " + ComponentState.INSTALLED);
        }
        JarComponentLoader loader = getLoader(id);
        if (loader == null) {
            logger.error("No such component as " + id);
            throw new IllegalArgumentException("No such component as " + id);
        }
        if (loader.isActive()) {
            logger.debug("Component is 'ACTIVE' - calling activator start()...");
            ComponentActivator activator = loader.getComponentActivator();
            try {
                activator.start();
            } catch (Throwable e) {
                logger.error("Caught exception in component start...", e);
                return;
            }
        }
        record.setState(ComponentState.RUNNING);
        logger.debug(id + " started, calling component listeners...");
        componentListenerHelper.fireComponentStarted(new ComponentEvent(id, this));
        logger.info(id + " is started");
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#stop(net.sf.oxygen.core.component.ComponentIdentifier)
   */
    public synchronized void stop(ComponentIdentifier id) throws IllegalComponentStateException, IllegalRunLevelException {
        stopImpl(id);
        currentLevel.removeComponent(id);
    }

    /**
   * Real implementation of stop method - but does not remove from run level
   * @param id The component to stop
   */
    private synchronized void stopImpl(ComponentIdentifier id) throws IllegalComponentStateException {
        logger.debug("Trying to stop " + id);
        ComponentRecord record = componentRegistry.getRecordFor(id);
        if (record == null) {
            logger.error("No such component as " + id);
            throw new IllegalArgumentException("No such component as " + id);
        }
        if (record.getState().equals(ComponentState.INSTALLED)) {
            logger.warn(id + " was already stopped");
            return;
        } else if (!record.getState().equals(ComponentState.RUNNING)) {
            logger.error(id + " is not RUNNING");
            throw new IllegalComponentStateException(id + " is not " + ComponentState.RUNNING);
        }
        JarComponentLoader loader = getLoader(id);
        if (loader == null) {
            logger.error("No such component as component " + id);
            throw new IllegalArgumentException("No such component as " + id);
        }
        if (loader.isActive()) {
            logger.debug("Component is 'ACTIVE' - calling activator stop()...");
            ComponentActivator activator = loader.getComponentActivator();
            try {
                activator.stop();
            } catch (Throwable e) {
                logger.error("Caught exception in component stop...", e);
                return;
            }
        }
        record.setState(ComponentState.INSTALLED);
        logger.debug(id + " stopped, calling component listeners...");
        componentListenerHelper.fireComponentStopped(new ComponentEvent(id, this));
        logger.info(id + " is stopped");
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#uninstall(net.sf.oxygen.core.component.ComponentIdentifier)
   */
    public synchronized void uninstall(ComponentIdentifier id) throws IllegalComponentStateException {
        logger.debug("Trying to uninstalll " + id);
        ComponentRecord record = componentRegistry.getRecordFor(id);
        if (record == null) {
            logger.error("No such component as " + id);
            throw new IllegalArgumentException("No such component as " + id);
        }
        if (!record.getState().equals(ComponentState.INSTALLED)) {
            logger.error(id + "was not INSTALLED");
            throw new IllegalComponentStateException(id + " is not " + ComponentState.INSTALLED);
        }
        record.setState(ComponentState.UNINSTALLED);
        componentRegistry.removeComponentRecord(id);
        componentLoaders.remove(getLoader(id));
        logger.debug(id + "is uninstalled, calling component listeners...");
        componentListenerHelper.fireComponentUninstalled(new ComponentEvent(id, this));
        logger.info(id + "is uninstalled");
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#update(net.sf.oxygen.core.component.ComponentIdentifier)
   */
    public synchronized void update(ComponentIdentifier id) throws IllegalComponentStateException {
        logger.debug("Trying to update " + id);
        JarComponentLoader loader = getLoader(id);
        String jar = loader.getJarFileName();
        try {
            stop(id);
            uninstall(id);
            install(jar);
        } catch (IllegalComponentStateException e) {
            logger.error(e);
        } catch (IllegalRunLevelException e) {
            logger.error(e);
        }
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#addComponentListener(net.sf.oxygen.core.event.ComponentListener)
   */
    public void addComponentListener(ComponentListener listener) {
        componentListenerHelper.addComponentListener(listener);
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#removeComponentListener(net.sf.oxygen.core.event.ComponentListener)
   */
    public void removeComponentListener(ComponentListener listener) {
        componentListenerHelper.removeComponentListener(listener);
    }

    /**
   * Import a class from another component on behalf of a given component loader.
   * The given loader is excluded from the list of component loaders to ask for
   * the class.
   * @param name The name of the class
   * @param resolve Should the class be resolved with the first loader that has it
   * @param exclude Do not search this loader for the class
   * @return The class exported by the first available loader
   * @throws ClassNotFoundException
   */
    public Class importClass(String name, boolean resolve, JarComponentLoader exclude) throws ClassNotFoundException {
        Iterator iter = componentLoaders.iterator();
        while (iter.hasNext()) {
            JarComponentLoader loader = (JarComponentLoader) iter.next();
            if (!loader.equals(exclude)) {
                try {
                    Class found = loader.loadExport(name, resolve);
                    return found;
                } catch (ClassNotFoundException e) {
                } catch (NotExportedException e) {
                    System.err.println("WARNING: Class " + name + " found but not exported by component=" + loader.getComponentIdentifier());
                }
            }
        }
        throw new ClassNotFoundException(name);
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#shutdown()
   */
    public void shutdown() {
        logger.debug("Trying to shut down");
        if (runLevels.isEmpty()) {
            stopAllComponentsIndividually();
        } else {
            try {
                setCurrentRunLevel((RunLevel) runLevels.firstElement());
            } catch (IllegalRunLevelException e) {
                logger.error(e);
            }
            stopAllComponentsIndividually();
        }
        logger.info("Framework shutdown");
    }

    /**
   * Iterates all components and stops them
   */
    private void stopAllComponentsIndividually() {
        List components = componentRegistry.getComponentIdentifiers();
        Iterator iterator = components.iterator();
        while (iterator.hasNext()) {
            ComponentIdentifier id = (ComponentIdentifier) iterator.next();
            try {
                stop(id);
            } catch (IllegalComponentStateException e) {
                logger.error(e);
            } catch (IllegalRunLevelException e) {
                logger.error(e);
            }
        }
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#defineRunLevel(java.lang.String)
   */
    public synchronized RunLevel defineRunLevel(String name) throws IllegalRunLevelException {
        logger.debug("Trying to define new run level name=" + name);
        if (currentLevel.isDefined()) {
            logger.warn("Current level is already defined");
            throw new IllegalRunLevelException("Current run level is already defined");
        }
        if (currentLevel.componentCount() <= 0) {
            logger.warn("No components started - cannot define run level");
            throw new IllegalRunLevelException("Components must be started before defining a run level");
        }
        logger.debug("Current undefined level - being defined as name=" + name);
        RunLevelImpl level = currentLevel;
        level.setName(name);
        runLevels.add(level);
        logger.debug("Creating new undefined level for future activities");
        currentLevel = new RunLevelImpl();
        return level;
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#getCurrentRunLevel()
   */
    public RunLevel getCurrentRunLevel() throws IllegalRunLevelException {
        if (currentLevel.isDefined()) {
            return currentLevel;
        } else {
            logger.warn("Not in a defined run level");
            throw new IllegalRunLevelException("Not in a defined run level");
        }
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#runLevels()
   */
    public List runLevels() {
        return (List) runLevels.clone();
    }

    /**
   * @see net.sf.oxygen.core.component.ComponentManager#setCurrentRunLevel(net.sf.oxygen.core.component.RunLevel)
   */
    public synchronized void setCurrentRunLevel(RunLevel rl) throws IllegalRunLevelException {
        logger.debug("Trying to set run level=" + rl);
        int desiredIndex = runLevels.indexOf(rl);
        if (desiredIndex < 0) {
            logger.warn("Run level=" + rl + " is not a valid defined run level");
            throw new IllegalRunLevelException(rl + " is not a valid defined run level");
        }
        int currentIndex = runLevels.size();
        if (currentLevel.isDefined()) {
            currentIndex = runLevels.indexOf(currentLevel);
            if (currentIndex < 0) {
                logger.error("Current index should be a member of defined run levels - it is not");
                throw new IllegalStateException("Current run level not a member of run levels");
            }
        } else {
            stop(currentLevel);
            currentLevel = (RunLevelImpl) runLevels.lastElement();
        }
        boolean starting = desiredIndex > currentIndex;
        while (currentIndex != desiredIndex) {
            if (starting) {
                start(currentLevel);
                currentIndex++;
            } else {
                stop(currentLevel);
                currentIndex--;
            }
            currentLevel = (RunLevelImpl) runLevels.get(currentIndex);
        }
    }

    /**
   * Start all components in the run level in forwards order.
   * @param rl The run level to start
   */
    private void start(RunLevel rl) {
        ListIterator li = rl.forwardComponentIterator();
        while (li.hasNext()) {
            ComponentIdentifier id = (ComponentIdentifier) li.next();
            try {
                start(id);
            } catch (IllegalComponentStateException e) {
                logger.error(e);
            } catch (IllegalRunLevelException e) {
                logger.error(e);
            }
        }
    }

    /**
   * Stop all components in the run level in revers order
   * @param rl The run level to start
   */
    private void stop(RunLevel rl) {
        ListIterator li = rl.backwardComponentIterator();
        while (li.hasPrevious()) {
            ComponentIdentifier id = (ComponentIdentifier) li.previous();
            try {
                stop(id);
            } catch (IllegalComponentStateException e) {
                logger.error(e);
            } catch (IllegalRunLevelException e) {
                logger.error(e);
            }
        }
    }
}
