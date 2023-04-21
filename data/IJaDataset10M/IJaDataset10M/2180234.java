package org.knowhowlab.osgi.jmx.beans.framework;

import org.knowhowlab.osgi.jmx.Utils;
import org.knowhowlab.osgi.jmx.beans.AbstractMBean;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.jmx.framework.BundleStateMBean;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.packageadmin.RequiredBundle;
import org.osgi.service.startlevel.StartLevel;
import javax.management.*;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import java.io.IOException;
import java.util.*;

/**
 * BundleStateMBean Implementation
 *
 * @author dmytro.pishchukhin
 */
public class BundleState extends AbstractMBean implements BundleStateMBean, NotificationBroadcaster, BundleListener {

    private NotificationBroadcasterSupport nbs;

    private MBeanNotificationInfo[] notificationInfos;

    private int sequenceNumber = 0;

    public BundleState() throws NotCompliantMBeanException {
        super(BundleStateMBean.class);
        nbs = new NotificationBroadcasterSupport();
    }

    public long[] getRequiredBundles(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return getRequiredBundles(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getRequiredBundles error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("getRequiredBundles error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getRequiredBundles error", e);
            throw new IOException(e.getMessage());
        }
    }

    public TabularData listBundles() throws IOException {
        try {
            Bundle[] bundles = visitor.getBundles();
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            StartLevel startLevel = visitor.getStartLevel();
            if (startLevel == null) {
                throw new IOException("StartLevel is not available");
            }
            TabularDataSupport dataSupport = new TabularDataSupport(BUNDLES_TYPE);
            if (bundles != null) {
                for (Bundle bundle : bundles) {
                    Map<String, Object> values = new HashMap<String, Object>();
                    values.put(EXPORTED_PACKAGES, getExportedPackages(bundle, packageAdmin));
                    values.put(FRAGMENT, isFragment(bundle, packageAdmin));
                    values.put(FRAGMENTS, Utils.toLongArray(getFragments(bundle, packageAdmin)));
                    values.put(HEADERS, getHeaders(bundle));
                    values.put(HOSTS, Utils.toLongArray(getHosts(bundle, packageAdmin)));
                    values.put(IDENTIFIER, bundle.getBundleId());
                    values.put(IMPORTED_PACKAGES, getImportedPackages(bundle, packageAdmin));
                    values.put(LAST_MODIFIED, bundle.getLastModified());
                    values.put(LOCATION, bundle.getLocation());
                    values.put(PERSISTENTLY_STARTED, isPersistentlyStarted(bundle, startLevel));
                    values.put(REGISTERED_SERVICES, Utils.toLongArray(getRegisteredServices(bundle)));
                    values.put(REMOVAL_PENDING, isRemovalPending(bundle, packageAdmin));
                    values.put(REQUIRED, isRequired(bundle, packageAdmin));
                    values.put(REQUIRED_BUNDLES, Utils.toLongArray(getRequiredBundles(bundle, packageAdmin)));
                    values.put(REQUIRING_BUNDLES, Utils.toLongArray(getRequiringBundles(bundle, packageAdmin)));
                    values.put(START_LEVEL, getStartLevel(bundle, startLevel));
                    values.put(STATE, getState(bundle));
                    values.put(SERVICES_IN_USE, Utils.toLongArray(getServicesInUse(bundle)));
                    values.put(SYMBOLIC_NAME, getSymbolicName(bundle));
                    values.put(VERSION, getVersion(bundle));
                    dataSupport.put(new CompositeDataSupport(BUNDLE_TYPE, values));
                }
            }
            return dataSupport;
        } catch (IOException e) {
            logVisitor.warning("listBundles error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("listBundles error", e);
            throw new IOException(e.getMessage());
        }
    }

    public String[] getExportedPackages(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return getExportedPackages(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getExportedPackages error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("getExportedPackages error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getExportedPackages error", e);
            throw new IOException(e.getMessage());
        }
    }

    public long[] getFragments(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return getFragments(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getFragments error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("getFragments error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getFragments error", e);
            throw new IOException(e.getMessage());
        }
    }

    public TabularData getHeaders(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            return getHeaders(bundle);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getHeaders error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getHeaders error", e);
            throw new IOException(e.getMessage());
        }
    }

    public long[] getHosts(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return getHosts(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getHosts error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("getHosts error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getHosts error", e);
            throw new IOException(e.getMessage());
        }
    }

    public String[] getImportedPackages(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return getImportedPackages(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getImportedPackages error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("getImportedPackages error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getImportedPackages error", e);
            throw new IOException(e.getMessage());
        }
    }

    public long getLastModified(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            return bundle.getLastModified();
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getLastModified error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getLastModified error", e);
            throw new IOException(e.getMessage());
        }
    }

    public long[] getRegisteredServices(long id) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(id);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + id);
            }
            return getRegisteredServices(bundle);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getRegisteredServices error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getRegisteredServices error", e);
            throw new IOException(e.getMessage());
        }
    }

    public long[] getRequiringBundles(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return getRequiringBundles(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getRequiringBundles error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("getRequiringBundles error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getRequiringBundles error", e);
            throw new IOException(e.getMessage());
        }
    }

    public long[] getServicesInUse(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            return getServicesInUse(bundle);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getServicesInUse error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getServicesInUse error", e);
            throw new IOException(e.getMessage());
        }
    }

    public int getStartLevel(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            StartLevel startLevel = visitor.getStartLevel();
            if (startLevel == null) {
                throw new IOException("StartLevel is not available");
            }
            return getStartLevel(bundle, startLevel);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getStartLevel error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("getStartLevel error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getStartLevel error", e);
            throw new IOException(e.getMessage());
        }
    }

    public String getState(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Bundle ID is wrong: " + bundleIdentifier);
            }
            return getState(bundle);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getState error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getState error", e);
            throw new IOException(e.getMessage());
        }
    }

    public String getSymbolicName(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            return getSymbolicName(bundle);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getSymbolicName error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getSymbolicName error", e);
            throw new IOException(e.getMessage());
        }
    }

    public boolean isPersistentlyStarted(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            StartLevel startLevel = visitor.getStartLevel();
            if (startLevel == null) {
                throw new IOException("StartLevel is not available");
            }
            return isPersistentlyStarted(bundle, startLevel);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("isPersistentlyStarted error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("isPersistentlyStarted error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("isPersistentlyStarted error", e);
            throw new IOException(e.getMessage());
        }
    }

    public boolean isFragment(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return isFragment(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("isFragment error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("isFragment error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("isFragment error", e);
            throw new IOException(e.getMessage());
        }
    }

    public boolean isRemovalPending(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return isRemovalPending(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("isRemovalPending error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("isRemovalPending error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("isRemovalPending error", e);
            throw new IOException(e.getMessage());
        }
    }

    public boolean isRequired(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            PackageAdmin packageAdmin = visitor.getPackageAdmin();
            if (packageAdmin == null) {
                throw new IOException("PackageAdmin is not available");
            }
            return isRequired(bundle, packageAdmin);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("isRequired error", e);
            throw e;
        } catch (IOException e) {
            logVisitor.warning("isRequired error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("isRequired error", e);
            throw new IOException(e.getMessage());
        }
    }

    public String getLocation(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            return bundle.getLocation();
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getLocation error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getLocation error", e);
            throw new IOException(e.getMessage());
        }
    }

    public String getVersion(long bundleIdentifier) throws IOException {
        try {
            Bundle bundle = visitor.getBundle(bundleIdentifier);
            if (bundle == null) {
                throw new IllegalArgumentException("Wrong Bundle ID: " + bundleIdentifier);
            }
            return getVersion(bundle);
        } catch (IllegalArgumentException e) {
            logVisitor.warning("getVersion error", e);
            throw e;
        } catch (Exception e) {
            logVisitor.warning("getVersion error", e);
            throw new IOException(e.getMessage());
        }
    }

    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        nbs.addNotificationListener(listener, filter, handback);
    }

    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        nbs.removeNotificationListener(listener);
    }

    public synchronized MBeanNotificationInfo[] getNotificationInfo() {
        if (notificationInfos == null) {
            notificationInfos = new MBeanNotificationInfo[] { new MBeanNotificationInfo(new String[] { BundleStateMBean.EVENT }, Notification.class.getName(), BundleStateMBean.EVENT) };
        }
        return notificationInfos;
    }

    public synchronized void bundleChanged(BundleEvent event) {
        Notification notification = new Notification(BundleStateMBean.EVENT, this, ++sequenceNumber, System.currentTimeMillis());
        try {
            Bundle bundle = event.getBundle();
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(IDENTIFIER, bundle.getBundleId());
            values.put(LOCATION, bundle.getLocation());
            values.put(SYMBOLIC_NAME, bundle.getSymbolicName());
            values.put(EVENT, event.getType());
            notification.setUserData(new CompositeDataSupport(BundleStateMBean.BUNDLE_EVENT_TYPE, values));
            nbs.sendNotification(notification);
        } catch (Exception e) {
            logVisitor.warning("Unable to send BundleEvent notification", e);
        }
    }

    private String[] getExportedPackages(Bundle bundle, PackageAdmin packageAdmin) {
        ExportedPackage[] exportedPackages = packageAdmin.getExportedPackages(bundle);
        List<String> result = new ArrayList<String>();
        if (exportedPackages != null) {
            for (ExportedPackage exportedPackage : exportedPackages) {
                result.add(exportedPackage.getName() + ';' + exportedPackage.getVersion().toString());
            }
        }
        return result.toArray(new String[result.size()]);
    }

    private boolean isFragment(Bundle bundle, PackageAdmin packageAdmin) {
        return packageAdmin.getBundleType(bundle) == PackageAdmin.BUNDLE_TYPE_FRAGMENT;
    }

    private long[] getFragments(Bundle bundle, PackageAdmin packageAdmin) {
        return Utils.getIds(packageAdmin.getFragments(bundle));
    }

    private long[] getHosts(Bundle bundle, PackageAdmin packageAdmin) {
        return Utils.getIds(packageAdmin.getHosts(bundle));
    }

    private String[] getImportedPackages(Bundle bundle, PackageAdmin packageAdmin) {
        ExportedPackage[] exportedPackages = packageAdmin.getExportedPackages((Bundle) null);
        if (exportedPackages != null) {
            Set<String> result = new HashSet<String>();
            for (ExportedPackage exportedPackage : exportedPackages) {
                Bundle[] importingBundles = exportedPackage.getImportingBundles();
                if (importingBundles != null) {
                    for (Bundle importingBundle : importingBundles) {
                        if (bundle.equals(importingBundle)) {
                            result.add(exportedPackage.getName() + ";" + exportedPackage.getVersion().toString());
                            break;
                        }
                    }
                }
            }
            return result.toArray(new String[result.size()]);
        }
        return new String[0];
    }

    private boolean isPersistentlyStarted(Bundle bundle, StartLevel startLevel) {
        return startLevel.isBundlePersistentlyStarted(bundle);
    }

    private long[] getRegisteredServices(Bundle bundle) {
        return Utils.getIds(bundle.getRegisteredServices());
    }

    private boolean isRemovalPending(Bundle bundle, PackageAdmin packageAdmin) {
        RequiredBundle[] requiredBundles = packageAdmin.getRequiredBundles(bundle.getSymbolicName());
        if (requiredBundles != null) {
            for (RequiredBundle requiredBundle : requiredBundles) {
                if (bundle.equals(requiredBundle.getBundle())) {
                    return requiredBundle.isRemovalPending();
                }
            }
        }
        return false;
    }

    private boolean isRequired(Bundle bundle, PackageAdmin packageAdmin) {
        RequiredBundle[] requiredBundles = packageAdmin.getRequiredBundles(bundle.getSymbolicName());
        if (requiredBundles != null) {
            for (RequiredBundle requiredBundle : requiredBundles) {
                if (bundle.equals(requiredBundle.getBundle())) {
                    Bundle[] requiringBundles = requiredBundle.getRequiringBundles();
                    return requiringBundles != null && requiringBundles.length > 0;
                }
            }
        }
        return false;
    }

    private String getVersion(Bundle bundle) {
        return bundle.getVersion().toString();
    }

    private String getSymbolicName(Bundle bundle) {
        return bundle.getSymbolicName();
    }

    private String getState(Bundle bundle) {
        return stateAsString(bundle.getState());
    }

    private int getStartLevel(Bundle bundle, StartLevel startLevel) {
        return startLevel.getBundleStartLevel(bundle);
    }

    private long[] getServicesInUse(Bundle bundle) {
        return Utils.getIds(bundle.getServicesInUse());
    }

    private long[] getRequiredBundles(Bundle bundle, PackageAdmin packageAdmin) {
        RequiredBundle[] requiredBundles = packageAdmin.getRequiredBundles(null);
        if (requiredBundles != null) {
            Set<Bundle> result = new HashSet<Bundle>();
            for (RequiredBundle requiredBundle : requiredBundles) {
                Bundle[] requiringBundles = requiredBundle.getRequiringBundles();
                if (requiringBundles != null) {
                    for (Bundle requiringBundle : requiringBundles) {
                        if (bundle.equals(requiringBundle)) {
                            Bundle associatedRequiredBundle = requiredBundle.getBundle();
                            if (associatedRequiredBundle != null) {
                                result.add(associatedRequiredBundle);
                                break;
                            }
                        }
                    }
                }
            }
            return Utils.getIds(result.toArray(new Bundle[result.size()]));
        }
        return new long[0];
    }

    private long[] getRequiringBundles(Bundle bundle, PackageAdmin packageAdmin) {
        RequiredBundle[] requiredBundles = packageAdmin.getRequiredBundles(bundle.getSymbolicName());
        if (requiredBundles != null) {
            for (RequiredBundle requiredBundle : requiredBundles) {
                if (bundle.equals(requiredBundle.getBundle())) {
                    Bundle[] requiringBundles = requiredBundle.getRequiringBundles();
                    return Utils.getIds(requiringBundles);
                }
            }
        }
        return new long[0];
    }

    private TabularData getHeaders(Bundle bundle) throws OpenDataException {
        TabularDataSupport dataSupport = new TabularDataSupport(HEADERS_TYPE);
        Dictionary headers = bundle.getHeaders();
        Enumeration keys = headers.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(KEY, key);
            values.put(VALUE, headers.get(key));
            dataSupport.put(new CompositeDataSupport(HEADER_TYPE, values));
        }
        return dataSupport;
    }

    private String stateAsString(int state) {
        switch(state) {
            case Bundle.UNINSTALLED:
                return UNINSTALLED;
            case Bundle.INSTALLED:
                return INSTALLED;
            case Bundle.RESOLVED:
                return RESOLVED;
            case Bundle.STARTING:
                return STARTING;
            case Bundle.STOPPING:
                return STOPPING;
            case Bundle.ACTIVE:
                return ACTIVE;
            default:
                return UNKNOWN;
        }
    }
}
