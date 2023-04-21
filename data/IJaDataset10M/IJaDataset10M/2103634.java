package org.eclipse.equinox.internal.advancedconfigurator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.eclipse.equinox.internal.advancedconfigurator.utils.AdvancedConfiguratorConstants;
import org.eclipse.equinox.internal.advancedconfigurator.utils.BundleInfo;
import org.eclipse.equinox.internal.advancedconfigurator.utils.EquinoxUtils;
import org.eclipse.equinox.internal.advancedconfigurator.utils.SimpleConfiguratorUtils;
import org.eclipse.equinox.internal.advancedconfigurator.utils.StateResolverUtils;
import org.eclipse.equinox.internal.advancedconfigurator.utils.Utils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;

class ConfigApplier {

    private static final String LAST_BUNDLES_INFO = "last.bundles.info";

    private static final String PROP_DEVMODE = "osgi.dev";

    private final BundleContext manipulatingContext;

    private final PackageAdmin packageAdminService;

    private final StartLevel startLevelService;

    private final boolean runningOnEquinox;

    private final boolean inDevMode;

    private final Bundle callingBundle;

    private final URI baseLocation;

    ConfigApplier(BundleContext context, Bundle callingBundle) {
        manipulatingContext = context;
        this.callingBundle = callingBundle;
        runningOnEquinox = "Eclipse".equals(context.getProperty(Constants.FRAMEWORK_VENDOR));
        inDevMode = manipulatingContext.getProperty(PROP_DEVMODE) != null;
        baseLocation = runningOnEquinox ? EquinoxUtils.getInstallLocationURI(context) : null;
        ServiceReference packageAdminRef = manipulatingContext.getServiceReference(PackageAdmin.class.getName());
        if (packageAdminRef == null) throw new IllegalStateException("No PackageAdmin service is available.");
        packageAdminService = (PackageAdmin) manipulatingContext.getService(packageAdminRef);
        ServiceReference startLevelRef = manipulatingContext.getServiceReference(StartLevel.class.getName());
        if (startLevelRef == null) throw new IllegalStateException("No StartLevelService service is available.");
        startLevelService = (StartLevel) manipulatingContext.getService(startLevelRef);
    }

    void install(URL url, boolean exclusiveMode) throws IOException {
        List bundleInfoList = SimpleConfiguratorUtils.readConfiguration(url, baseLocation);
        if (Activator.DEBUG) System.out.println("applyConfiguration() bundleInfoList.size()=" + bundleInfoList.size());
        if (bundleInfoList.size() == 0) return;
        BundleInfo[] expectedState = Utils.getBundleInfosFromList(bundleInfoList);
        String systemBundleSymbolicName = manipulatingContext.getBundle(0).getSymbolicName();
        Version systemBundleVersion = manipulatingContext.getBundle(0).getVersion();
        if (systemBundleSymbolicName != null) {
            for (int i = 0; i < expectedState.length; i++) {
                String symbolicName = expectedState[i].getSymbolicName();
                if (!systemBundleSymbolicName.equals(symbolicName)) continue;
                Version version = Version.parseVersion(expectedState[i].getVersion());
                if (!systemBundleVersion.equals(version)) throw new IllegalStateException("The System Bundle was updated. The framework must be restarted to finalize the configuration change");
            }
        }
        HashSet toUninstall = null;
        if (!exclusiveMode) {
            BundleInfo[] lastInstalledBundles = getLastState();
            if (lastInstalledBundles != null) {
                toUninstall = new HashSet(Arrays.asList(lastInstalledBundles));
                toUninstall.removeAll(Arrays.asList(expectedState));
            }
            saveStateAsLast(url);
        }
        Collection prevouslyResolved = getResolvedBundles();
        Collection toRefresh = new ArrayList();
        Collection toStart = new ArrayList();
        if (exclusiveMode) {
            toRefresh.addAll(installBundles(expectedState, toStart));
            toRefresh.addAll(uninstallBundles(expectedState, packageAdminService));
        } else {
            toRefresh.addAll(installBundles(expectedState, toStart));
            if (toUninstall != null) toRefresh.addAll(uninstallBundles(toUninstall));
        }
        refreshPackages((Bundle[]) toRefresh.toArray(new Bundle[toRefresh.size()]), manipulatingContext);
        if (toRefresh.size() > 0) try {
            manipulatingContext.getBundle().loadClass("org.eclipse.osgi.service.resolver.PlatformAdmin");
            Bundle[] additionalRefresh = StateResolverUtils.getAdditionalRefresh(prevouslyResolved, manipulatingContext);
            if (additionalRefresh.length > 0) refreshPackages(additionalRefresh, manipulatingContext);
        } catch (ClassNotFoundException cnfe) {
        }
        startBundles((Bundle[]) toStart.toArray(new Bundle[toStart.size()]));
    }

    private Collection getResolvedBundles() {
        Collection resolved = new HashSet();
        Bundle[] allBundles = manipulatingContext.getBundles();
        for (int i = 0; i < allBundles.length; i++) if ((allBundles[i].getState() & (Bundle.INSTALLED | Bundle.UNINSTALLED)) == 0) resolved.add(allBundles[i]);
        return resolved;
    }

    private Collection uninstallBundles(HashSet toUninstall) {
        Collection removedBundles = new ArrayList(toUninstall.size());
        for (Iterator iterator = toUninstall.iterator(); iterator.hasNext(); ) {
            BundleInfo current = (BundleInfo) iterator.next();
            Bundle[] matchingBundles = packageAdminService.getBundles(current.getSymbolicName(), getVersionRange(current.getVersion()));
            for (int j = 0; matchingBundles != null && j < matchingBundles.length; j++) {
                try {
                    removedBundles.add(matchingBundles[j]);
                    matchingBundles[j].uninstall();
                } catch (BundleException e) {
                }
            }
        }
        return removedBundles;
    }

    private void saveStateAsLast(URL url) {
        InputStream sourceStream = null;
        OutputStream destinationStream = null;
        File lastBundlesTxt = getLastBundleInfo();
        try {
            try {
                destinationStream = new FileOutputStream(lastBundlesTxt);
                sourceStream = url.openStream();
                SimpleConfiguratorUtils.transferStreams(sourceStream, destinationStream);
            } finally {
                if (destinationStream != null) destinationStream.close();
                if (sourceStream != null) sourceStream.close();
            }
        } catch (IOException e) {
        }
    }

    private File getLastBundleInfo() {
        return manipulatingContext.getDataFile(LAST_BUNDLES_INFO);
    }

    private BundleInfo[] getLastState() {
        File lastBundlesInfo = getLastBundleInfo();
        if (!lastBundlesInfo.isFile()) return null;
        try {
            return (BundleInfo[]) SimpleConfiguratorUtils.readConfiguration(lastBundlesInfo.toURL(), baseLocation).toArray(new BundleInfo[1]);
        } catch (IOException e) {
            return null;
        }
    }

    private ArrayList installBundles(BundleInfo[] finalList, Collection toStart) {
        ArrayList toRefresh = new ArrayList();
        String useReferenceProperty = manipulatingContext.getProperty(AdvancedConfiguratorConstants.PROP_KEY_USE_REFERENCE);
        boolean useReference = useReferenceProperty == null ? runningOnEquinox : Boolean.valueOf(useReferenceProperty).booleanValue();
        for (int i = 0; i < finalList.length; i++) {
            if (finalList[i] == null) continue;
            String symbolicName = finalList[i].getSymbolicName();
            String version = finalList[i].getVersion();
            Bundle[] matches = null;
            if (symbolicName != null && version != null) matches = packageAdminService.getBundles(symbolicName, getVersionRange(version));
            String bundleLocation = SimpleConfiguratorUtils.getBundleLocation(finalList[i], useReference);
            Bundle current = matches == null ? null : (matches.length == 0 ? null : matches[0]);
            if (current == null) {
                try {
                    current = manipulatingContext.installBundle(bundleLocation);
                    if (Activator.DEBUG) System.out.println("installed bundle:" + finalList[i]);
                    toRefresh.add(current);
                } catch (BundleException e) {
                    if (Activator.DEBUG) {
                        System.err.println("Can't install " + symbolicName + '/' + version + " from location " + finalList[i].getLocation());
                        e.printStackTrace();
                    }
                    continue;
                }
            } else if (inDevMode && current.getBundleId() != 0 && current != manipulatingContext.getBundle() && !bundleLocation.equals(current.getLocation()) && !current.getLocation().startsWith("initial@")) {
                try {
                    current.uninstall();
                    toRefresh.add(current);
                } catch (BundleException e) {
                    if (Activator.DEBUG) {
                        System.err.println("Can't uninstall " + symbolicName + '/' + version + " from location " + current.getLocation());
                        e.printStackTrace();
                    }
                    continue;
                }
                try {
                    current = manipulatingContext.installBundle(bundleLocation);
                    if (Activator.DEBUG) System.out.println("installed bundle:" + finalList[i]);
                    toRefresh.add(current);
                } catch (BundleException e) {
                    if (Activator.DEBUG) {
                        System.err.println("Can't install " + symbolicName + '/' + version + " from location " + finalList[i].getLocation());
                        e.printStackTrace();
                    }
                    continue;
                }
            }
            if (finalList[i].isMarkedAsStarted()) {
                toStart.add(current);
            }
            int startLevel = finalList[i].getStartLevel();
            if (startLevel < 1) continue;
            if (current.getBundleId() == 0) continue;
            if (packageAdminService.getBundleType(current) == PackageAdmin.BUNDLE_TYPE_FRAGMENT) continue;
            if (AdvancedConfiguratorConstants.TARGET_CONFIGURATOR_NAME.equals(current.getSymbolicName())) continue;
            try {
                startLevelService.setBundleStartLevel(current, startLevel);
            } catch (IllegalArgumentException ex) {
                Utils.log(4, null, null, "Failed to set start level of Bundle:" + finalList[i], ex);
            }
        }
        return toRefresh;
    }

    private void refreshPackages(Bundle[] bundles, BundleContext context) {
        if (bundles.length == 0 || packageAdminService == null) return;
        final boolean[] flag = new boolean[] { false };
        FrameworkListener listener = new FrameworkListener() {

            public void frameworkEvent(FrameworkEvent event) {
                if (event.getType() == FrameworkEvent.PACKAGES_REFRESHED) {
                    synchronized (flag) {
                        flag[0] = true;
                        flag.notifyAll();
                    }
                }
            }
        };
        context.addFrameworkListener(listener);
        packageAdminService.refreshPackages(bundles);
        synchronized (flag) {
            while (!flag[0]) {
                try {
                    flag.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        context.removeFrameworkListener(listener);
    }

    private void startBundles(Bundle[] bundles) {
        for (int i = 0; i < bundles.length; i++) {
            Bundle bundle = bundles[i];
            if (bundle.getState() == Bundle.UNINSTALLED) {
                System.err.println("Could not start: " + bundle.getSymbolicName() + '(' + bundle.getLocation() + ':' + bundle.getBundleId() + ')' + ". It's state is uninstalled.");
                continue;
            }
            if (bundle.getState() == Bundle.STARTING && (bundle == callingBundle || bundle == manipulatingContext.getBundle())) continue;
            if (packageAdminService.getBundleType(bundle) == PackageAdmin.BUNDLE_TYPE_FRAGMENT) continue;
            try {
                bundle.start();
                if (Activator.DEBUG) System.out.println("started Bundle:" + bundle.getSymbolicName() + '(' + bundle.getLocation() + ':' + bundle.getBundleId() + ')');
            } catch (BundleException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Uninstall bundles which are not listed on finalList.  
	 * 
	 * @param finalList bundles list not to be uninstalled.
	 * @param packageAdmin package admin service.
	 * @return Collection HashSet of bundles finally installed.
	 */
    private Collection uninstallBundles(BundleInfo[] finalList, PackageAdmin packageAdmin) {
        Bundle[] allBundles = manipulatingContext.getBundles();
        Set removedBundles = new HashSet(allBundles.length);
        for (int i = 0; i < allBundles.length; i++) {
            if (allBundles[i].getBundleId() == 0) continue;
            if (allBundles[i].getSymbolicName().startsWith("org.eclipse.equinox.advancedconfigurator")) continue;
            removedBundles.add(allBundles[i]);
        }
        for (int i = 0; i < finalList.length; i++) {
            if (finalList[i] == null) continue;
            Bundle[] toAdd = packageAdmin.getBundles(finalList[i].getSymbolicName(), getVersionRange(finalList[i].getVersion()));
            for (int j = 0; toAdd != null && j < toAdd.length; j++) {
                removedBundles.remove(toAdd[j]);
            }
        }
        for (Iterator iter = removedBundles.iterator(); iter.hasNext(); ) {
            try {
                Bundle bundle = ((Bundle) iter.next());
                if (bundle.getLocation().startsWith("initial@")) {
                    if (Activator.DEBUG) System.out.println("Simple configurator thinks a bundle installed by the boot strap should be uninstalled:" + bundle.getSymbolicName() + '(' + bundle.getLocation() + ':' + bundle.getBundleId() + ')');
                    iter.remove();
                    continue;
                }
                bundle.uninstall();
                if (Activator.DEBUG) System.out.println("uninstalled Bundle:" + bundle.getSymbolicName() + '(' + bundle.getLocation() + ':' + bundle.getBundleId() + ')');
            } catch (BundleException e) {
                e.printStackTrace();
            }
        }
        return removedBundles;
    }

    private String getVersionRange(String version) {
        return version == null ? null : new StringBuffer().append('[').append(version).append(',').append(version).append(']').toString();
    }
}
