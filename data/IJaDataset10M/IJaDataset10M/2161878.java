package f06.osgi.framework;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import org.osgi.framework.AdminPermission;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.RequiredBundle;
import f06.util.ManifestEntry;
import f06.util.TextUtil;

class HostBundle extends AbstractBundle {

    private BundleActivator activator;

    protected BundleContext context;

    protected BundleClassLoader classLoader;

    protected volatile boolean activationTriggered;

    public HostBundle(Framework framework) {
        super(framework);
    }

    boolean resolve() {
        return framework.resolveBundles(new Bundle[] { this });
    }

    public URL getResource(String name) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AdminPermission(this, AdminPermission.RESOURCE));
        }
        if (getState() == INSTALLED) {
            if (!resolve()) {
                return getEntry(name);
            }
        }
        ClassLoader classLoader = getClassLoader();
        return classLoader.getResource(name);
    }

    public Enumeration getResources(String name) throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AdminPermission(this, AdminPermission.RESOURCE));
        }
        if (getState() == INSTALLED) {
            if (!resolve()) {
                BundleURLClassPath classPath = framework.getBundleURLClassPath(this);
                return classPath.findEntries("", name, false);
            }
        }
        ClassLoader classLoader = getClassLoader();
        return classLoader.getResources(name);
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AdminPermission(this, AdminPermission.CLASS));
        }
        if (getState() == INSTALLED) {
            if (!resolve()) {
                FrameworkEvent frameworkEvent = new FrameworkEvent(FrameworkEvent.ERROR, this, new BundleException(new StringBuilder("Bundle(id=").append(getBundleId()).append(") cannot be resolved.").toString()));
                framework.postFrameworkEvent(frameworkEvent);
                throw new ClassNotFoundException(new StringBuilder("Bundle(id=").append(getBundleId()).append(") cannot be resolved.").toString());
            }
        } else if (getState() == UNINSTALLED) {
            throw new IllegalStateException(new StringBuilder("Attempting to load a class from uninstalled Bundle(id=").append(getBundleId()).append(").").toString());
        }
        ClassLoader classLoader = getClassLoader();
        return classLoader.loadClass(name);
    }

    public void start(int options) throws BundleException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AdminPermission(this, AdminPermission.EXECUTE));
        }
        if (getState() == UNINSTALLED) {
            throw new IllegalStateException(new StringBuilder(this.toString()).append(" has been uninstalled. It can't move to another state.").toString());
        }
        if ((options & START_TRANSIENT) != 0) {
            if (framework.getStartLevel() < framework.getBundleStartLevel(this)) {
                throw new BundleException(new StringBuilder(this.toString()).append(" cannot be started due to the Framework's current start level.").toString());
            }
        } else {
            framework.setBundleAutostartSetting(this, options == START_ACTIVATION_POLICY ? AbstractBundle.STARTED_DECLARED : AbstractBundle.STARTED_EAGER);
        }
        if (getState() == STARTING) {
            if (framework.isBundleActivationPolicyUsed(this)) {
                return;
            } else {
                synchronized (this) {
                    try {
                        wait(15 * 1000);
                    } catch (InterruptedException e) {
                        framework.log(LogService.LOG_ERROR, e.getMessage(), e);
                    }
                }
                if (getState() != ACTIVE) {
                    throw new BundleException(new StringBuilder(this.toString()).append(" was unable to be started.").toString());
                }
            }
        } else if (getState() == STOPPING) {
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
            }
            if (getState() != RESOLVED) {
                throw new BundleException(new StringBuilder(this.toString()).append(" was unable to be stopped.").toString());
            }
            return;
        }
        if (getState() == ACTIVE) {
            return;
        } else if (getState() == INSTALLED) {
            if (!resolve()) {
                throw new BundleException(new StringBuilder(this.toString()).append(" cannot be resolved.").toString());
            }
        }
        if ((options & START_TRANSIENT) != 0) {
            if (framework.getStartLevel() < framework.getBundleStartLevel(this)) {
                return;
            }
        }
        BundleContext context = framework.createBundleContext(this);
        setBundleContext(context);
        setState(STARTING);
        if (framework.isBundleActivationPolicyUsed(this)) {
            String activationPolicy = (String) getHeaders().get(Constants.BUNDLE_ACTIVATIONPOLICY);
            if (activationPolicy != null) {
                BundleEvent eventStarting = new BundleEvent(BundleEvent.LAZY_ACTIVATION, this);
                framework.postBundleEvent(eventStarting);
            } else {
                activate();
            }
        } else {
            activate();
        }
    }

    void activate() throws BundleException {
        if (isActivationTriggered()) {
            return;
        }
        setActivationTriggered(true);
        final String className = (String) getHeaders().get(Constants.BUNDLE_ACTIVATOR);
        if (className != null) {
            BundleEvent eventStarting = new BundleEvent(BundleEvent.STARTING, this);
            framework.sendBundleEvent(eventStarting);
            try {
                ProtectionDomain protectionDomain = framework.getProtectionDomain(this);
                AccessController.doPrivileged(new PrivilegedExceptionAction() {

                    public Object run() throws Exception {
                        ClassLoader classLoader = getClassLoader();
                        Class cls = classLoader.loadClass(className);
                        activator = (BundleActivator) cls.newInstance();
                        activator.start(getBundleContext());
                        setActivationTriggered(false);
                        return null;
                    }
                }, new AccessControlContext(new ProtectionDomain[] { protectionDomain }));
            } catch (Throwable t) {
                framework.log(LogService.LOG_ERROR, t.getMessage(), t);
                setState(RESOLVED);
                framework.unregisterServices(this);
                context = null;
                throw new BundleException(new StringBuilder("A problem occurred starting ").append(this.toString()).append(".").toString(), t);
            }
            if (getState() == UNINSTALLED) {
                throw new BundleException(new StringBuilder(this.toString()).append(" has been uninstalled while bundle start was running.").toString());
            }
        }
        setState(ACTIVE);
        BundleEvent eventStarted = new BundleEvent(BundleEvent.STARTED, this);
        framework.sendBundleEvent(eventStarted);
    }

    public void stop(int options) throws BundleException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AdminPermission(this, AdminPermission.EXECUTE));
        }
        if (getState() == UNINSTALLED) {
            throw new IllegalStateException(new StringBuilder(this.toString()).append(" has been uninstalled.").toString());
        } else if ((getState() & (STARTING | STOPPING)) != 0) {
            synchronized (this) {
                try {
                    wait(15 * 1000);
                } catch (InterruptedException e) {
                    framework.log(LogService.LOG_ERROR, e.getMessage(), e);
                }
            }
            if ((getState() & (STARTING | STOPPING)) != 0) {
                throw new BundleException(new StringBuilder(this.toString()).append(" was enabled to be stopped.").toString());
            }
        }
        if (options != STOP_TRANSIENT) {
            framework.setBundleAutostartSetting(this, AbstractBundle.STOPPED);
        }
        if (getState() != ACTIVE) {
            return;
        }
        setState(STOPPING);
        BundleEvent eventStopping = new BundleEvent(BundleEvent.STOPPING, this);
        framework.sendBundleEvent(eventStopping);
        BundleException bundleException = null;
        if (activator != null) {
            try {
                ProtectionDomain protectionDomain = framework.getProtectionDomain(this);
                AccessController.doPrivileged(new PrivilegedExceptionAction() {

                    public Object run() throws Exception {
                        BundleContext bundleContext = getBundleContext();
                        activator.stop(bundleContext);
                        activator = null;
                        return null;
                    }
                }, new AccessControlContext(new ProtectionDomain[] { protectionDomain }));
            } catch (Exception e) {
                bundleException = new BundleException("An error occurred during bundle stop procedure.", e);
            }
            framework.unregisterServices(this);
        }
        if (getState() == UNINSTALLED) {
            throw new BundleException(new StringBuilder(this.toString()).append(" has been uninstalled while bundle stop was running.").toString());
        }
        setState(RESOLVED);
        setBundleContext(null);
        BundleEvent eventStopped = new BundleEvent(BundleEvent.STOPPED, this);
        framework.sendBundleEvent(eventStopped);
        if (bundleException != null) {
            throw bundleException;
        }
    }

    public void uninstall() throws BundleException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AdminPermission(this, AdminPermission.LIFECYCLE));
        }
        if (getState() == UNINSTALLED) {
            throw new IllegalStateException(new StringBuilder(this.toString()).append(" has been uninstalled.").toString());
        } else if ((getState() & (ACTIVE | STARTING | STOPPING)) != 0) {
            try {
                stop();
            } catch (BundleException e) {
                FrameworkEvent frameworkEvent = new FrameworkEvent(FrameworkEvent.ERROR, this, e);
                framework.postFrameworkEvent(frameworkEvent);
            }
        }
        setState(UNINSTALLED);
        framework.setRemovalPending(this);
        try {
            boolean removable = true;
            ExportedPackage[] exportedPackages = framework.getExportedPackages(this);
            if (exportedPackages != null) {
                for (int i = 0; i < exportedPackages.length; i++) {
                    ExportedPackage exportedPackage = exportedPackages[i];
                    if (exportedPackage.getImportingBundles() != null) {
                        removable = false;
                    }
                    ((ExportedPackageImpl) exportedPackage).setRemovalPending0(true);
                }
            }
            RequiredBundle[] requiredBundles = framework.getRequiredBundles(getSymbolicName());
            if (requiredBundles != null) {
                for (int i = 0; i < requiredBundles.length; i++) {
                    RequiredBundle requiredBundle = requiredBundles[i];
                    if (requiredBundle.getBundle() == this) {
                        removable = false;
                        ((RequiredBundleImpl) requiredBundle).setRemovalPending0(true);
                        break;
                    }
                }
            }
            BundleEvent bundleEvent = new BundleEvent(BundleEvent.UNINSTALLED, this);
            framework.sendBundleEvent(bundleEvent);
            if (removable) {
                framework.unresolveBundle(this);
                setBundleClassLoader(null);
                framework.remove(this);
            }
        } catch (Exception e) {
            throw new BundleException(e.getMessage(), e);
        }
    }

    public void update(InputStream is) throws BundleException {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new AdminPermission(this, AdminPermission.LIFECYCLE));
            }
            int oldState = getState();
            if (getState() == UNINSTALLED) {
                throw new IllegalStateException(new StringBuilder(this.toString()).append(" has been uninstalled.").toString());
            }
            if ((getState() & (ACTIVE | STARTING | STOPPING)) != 0) {
                stop(STOP_TRANSIENT);
            }
            setBundleClassLoader(null);
            BundleException bundleException = null;
            try {
                framework.update(this, is);
                String requiredEE = (String) getHeaders().get(Constants.BUNDLE_REQUIREDEXECUTIONENVIRONMENT);
                if (requiredEE != null) {
                    String ee = System.getProperty(Constants.FRAMEWORK_EXECUTIONENVIRONMENT);
                    if (!ee.contains(requiredEE)) {
                        throw new BundleException(new StringBuilder(this.toString()).append(" requires an unsopperted execution environment (=" + requiredEE + ").").toString());
                    }
                }
                boolean removable = true;
                ExportedPackage[] exportedPackages = framework.getExportedPackages(this);
                if (exportedPackages != null) {
                    for (int i = 0; i < exportedPackages.length; i++) {
                        ExportedPackage exportedPackage = exportedPackages[i];
                        if (exportedPackage.getImportingBundles() != null) {
                            removable = false;
                        }
                        ((ExportedPackageImpl) exportedPackage).setRemovalPending0(true);
                    }
                }
                RequiredBundle[] requiredBundles = framework.getRequiredBundles(getSymbolicName());
                if (requiredBundles != null) {
                    for (int i = 0; i < requiredBundles.length; i++) {
                        RequiredBundle requiredBundle = requiredBundles[i];
                        if (requiredBundle.getBundle() == HostBundle.this) {
                            removable = false;
                            ((RequiredBundleImpl) requiredBundle).setRemovalPending0(true);
                            break;
                        }
                    }
                }
                if (removable) {
                    framework.unresolveBundle(this);
                }
            } catch (BundleException e) {
                bundleException = e;
            }
            setState(Bundle.INSTALLED);
            BundleEvent bundleEvent = new BundleEvent(BundleEvent.UPDATED, this);
            framework.sendBundleEvent(bundleEvent);
            if (oldState == ACTIVE) {
                try {
                    start();
                } catch (BundleException e) {
                    FrameworkEvent frameworkEvent = new FrameworkEvent(FrameworkEvent.ERROR, this, e);
                    framework.postFrameworkEvent(frameworkEvent);
                }
            }
            if (bundleException != null) {
                throw bundleException;
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    public ServiceReference[] getServicesInUse() {
        BundleContext context = getBundleContext();
        if (context != null) {
            return ((BundleContextImpl) context).getServicesInUse();
        }
        return null;
    }

    void setBundleContext(BundleContext context) {
        this.context = context;
    }

    public BundleContext getBundleContext() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AdminPermission(this, AdminPermission.CONTEXT));
        }
        return context;
    }

    ClassLoader getClassLoader() {
        if (classLoader == null) {
            if (getState() == INSTALLED && getState() == UNINSTALLED) {
                throw new IllegalStateException("Cannot create a class loader for an unresolved Bundle.");
            }
            classLoader = framework.createBundleClassLoader(this);
        }
        return classLoader;
    }

    public void setBundleClassLoader(BundleClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected boolean isActivationTriggered() {
        return activationTriggered;
    }

    boolean isActivationTriggered(String pkgName) {
        boolean matched = true;
        String activationPolicy = (String) getHeaders().get(Constants.BUNDLE_ACTIVATIONPOLICY);
        if (activationPolicy != null) {
            try {
                ManifestEntry entry = ManifestEntry.parse(activationPolicy)[0];
                if (entry.hasAttribute(Constants.EXCLUDE_DIRECTIVE)) {
                    String[] exclude = entry.getAttributeValue(Constants.EXCLUDE_DIRECTIVE).split("\\,");
                    for (int i = 0; i < exclude.length; i++) {
                        String path = exclude[i];
                        if (TextUtil.wildcardCompare(path, pkgName) == 0) {
                            matched = false;
                            break;
                        }
                    }
                }
                if (matched) {
                    String[] include;
                    if (entry.hasAttribute(Constants.INCLUDE_DIRECTIVE)) {
                        include = entry.getAttributeValue(Constants.INCLUDE_DIRECTIVE).split("\\,");
                        for (int i = 0; i < include.length; i++) {
                            String path = include[i];
                            if (TextUtil.wildcardCompare(path, pkgName) != 0) {
                                matched = false;
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                framework.log(LogService.LOG_ERROR, e.getMessage(), e);
                matched = false;
            }
        }
        return matched;
    }

    protected void setActivationTriggered(boolean activationTriggered) {
        this.activationTriggered = activationTriggered;
    }
}
