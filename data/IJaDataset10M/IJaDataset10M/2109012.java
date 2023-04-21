package com.volantis.synergetics.osgi.impl.framework.watcher;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Watches a specified directory, polling it at a specified interval to see
 * whether any packages have been installed, removed or changed and performs
 * the appropriate action.
 */
public class Watcher implements Runnable {

    /**
     * The context within which the watcher is running.
     */
    private final BundleContext context;

    /**
     * The directory to watch.
     */
    private final File dir;

    /**
     * The poll internal in milliseconds.
     */
    private final int intervalMS;

    /**
     * The URL to the directory as a string.
     */
    private final String dirURL;

    /**
     * Flag to determine whether the watcher is active.
     *
     * <p>If set to false then it means that the watcher is shutting down.</p>
     */
    private boolean active;

    /**
     * A map from location (URL represented as a string) to a Long which
     * represents the time at which the previous action on a bundle failed.
     * This is used to detect when the bundle has changed so the action can
     * be retried. Otherwise the action will keep failing and generating a
     * flood of errors.
     */
    private final Map failedActions;

    /**
     * A flag that is set the first time that the watcher runs to make sure
     * that any bundles that have not changed since the last time that the
     * framework was running are started.
     */
    private boolean startingUp;

    /**
     * Initialise.
     *
     * @param context  The context within which this is running.
     * @param dir      The directory to watch.
     * @param interval The poll interval.
     * @throws IOException If there was a problem converting the file into a
     *                     URL.
     */
    public Watcher(BundleContext context, File dir, int interval) throws IOException {
        this.context = context;
        this.intervalMS = interval * 1000;
        this.dir = dir.getCanonicalFile();
        dirURL = this.dir.toURL().toExternalForm();
        active = true;
        failedActions = new HashMap();
    }

    public void run() {
        startingUp = true;
        do {
            refresh();
            synchronized (this) {
                try {
                    wait(intervalMS);
                    if (!active) {
                        return;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        } while (true);
    }

    /**
     * Refresh the set of installed packages if necessary.
     */
    private void refresh() {
        Map installedBundles = getInstalledBundles(context);
        File[] bundleFiles = dir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        if (bundleFiles == null) {
            return;
        }
        Map watched = new HashMap();
        for (int i = 0; i < bundleFiles.length; i++) {
            File file = bundleFiles[i];
            try {
                File canonicalFile = file.getCanonicalFile();
                watched.put(canonicalFile.toURL().toExternalForm(), canonicalFile);
            } catch (IOException e) {
                IllegalStateException exception = new IllegalStateException("Cannot get canonical file for '" + file + "'");
                exception.initCause(e);
                exception.printStackTrace(System.err);
            }
        }
        Set locations = new HashSet();
        locations.addAll(watched.keySet());
        locations.addAll(installedBundles.keySet());
        Collection toUninstall = new HashSet();
        Collection toInstall = new HashSet();
        Collection toUpdate = new HashSet();
        Collection toStart = new HashSet();
        int changeCount = 0;
        for (Iterator i = locations.iterator(); i.hasNext(); ) {
            String location = (String) i.next();
            File file = (File) watched.get(location);
            Bundle installed = (Bundle) installedBundles.get(location);
            if (file == null) {
                if (installed == null) {
                    throw new IllegalStateException("'" + location + "' is in uber set but not in either watched " + "or installed");
                }
                URL url;
                try {
                    url = new URL(location);
                    File timeStampFile = getTimestampFile(url);
                    toUninstall.add(new UninstallAction(installed, timeStampFile));
                    changeCount += 1;
                } catch (MalformedURLException e) {
                }
            } else {
                File timeStampFile = getTimestampFile(file);
                long watchedLastModified = file.lastModified();
                Long l = (Long) failedActions.get(location);
                boolean previousActionFailed = false;
                if (l != null) {
                    long errorTimeStamp = l.longValue();
                    previousActionFailed = true;
                    if (watchedLastModified > errorTimeStamp) {
                        changeCount += 1;
                    }
                }
                if (installed == null) {
                    if (!previousActionFailed) {
                        changeCount += 1;
                    }
                    toInstall.add(new InstallAction(context, location, watchedLastModified, timeStampFile));
                } else {
                    long bundleLastUpdated = timeStampFile.lastModified();
                    if (watchedLastModified > bundleLastUpdated) {
                        if (!previousActionFailed) {
                            changeCount += 1;
                        }
                        toUpdate.add(new UpdateAction(installed, watchedLastModified, timeStampFile));
                    } else if (startingUp) {
                        toStart.add(new StartAction(installed, watchedLastModified));
                        changeCount += 1;
                    } else if (installed.getState() == Bundle.INSTALLED) {
                        toStart.add(new StartAction(installed, watchedLastModified));
                    }
                }
            }
        }
        for (Iterator i = toUninstall.iterator(); i.hasNext(); ) {
            Action action = (Action) i.next();
            action.takeAction(failedActions);
        }
        if (changeCount > 0) {
            for (Iterator i = toInstall.iterator(); i.hasNext(); ) {
                InstallAction action = (InstallAction) i.next();
                Bundle bundle = action.takeAction(failedActions);
                if (bundle != null) {
                    toStart.add(new StartAction(bundle, action.getWatchedLastModified()));
                }
            }
            for (Iterator i = toUpdate.iterator(); i.hasNext(); ) {
                UpdateAction action = (UpdateAction) i.next();
                Bundle bundle = action.takeAction(failedActions);
                if (bundle != null) {
                    toStart.add(new StartAction(bundle, action.getWatchedLastModified()));
                }
            }
            for (Iterator i = toStart.iterator(); i.hasNext(); ) {
                Action action = (Action) i.next();
                Bundle bundle = action.takeAction(failedActions);
                if (bundle != null) {
                    failedActions.remove(action.getLocation());
                }
            }
            ServiceReference reference = context.getServiceReference(PackageAdmin.class.getName());
            if (reference != null) {
                PackageAdmin packageAdmin = (PackageAdmin) context.getService(reference);
                packageAdmin.refreshPackages(null);
                installedBundles = getInstalledBundles(context);
                for (Iterator i = installedBundles.entrySet().iterator(); i.hasNext(); ) {
                    Map.Entry entry = (Map.Entry) i.next();
                    String location = (String) entry.getKey();
                    Bundle bundle = (Bundle) entry.getValue();
                    String message = location + " " + stateAsString(bundle.getState());
                    Reporter.report(message);
                }
            }
            startingUp = false;
        }
    }

    private String stateAsString(int state) {
        switch(state) {
            case Bundle.ACTIVE:
                return "ACTIVE";
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.STARTING:
                return "STARTING";
            case Bundle.STOPPING:
                return "STOPPING";
            case Bundle.UNINSTALLED:
                return "UNINSTALLED";
        }
        return "unknown";
    }

    private File getTimestampFile(File file) {
        File dir = file.getParentFile();
        String name = file.getName();
        return new File(dir, "." + name + ".wts");
    }

    private File getTimestampFile(URL url) {
        File file = new File(url.getPath());
        return getTimestampFile(file);
    }

    /**
     * Get a map of installed bundles from the context.
     * @param context The bundle context within which this is running.
     * @return A map from location (URL as a string) to bundle.
     */
    private Map getInstalledBundles(BundleContext context) {
        Bundle[] bundles = context.getBundles();
        Map map = new TreeMap();
        for (int i = 0; i < bundles.length; i++) {
            Bundle bundle = bundles[i];
            String location = bundle.getLocation();
            if (location.startsWith(dirURL)) {
                map.put(location, bundle);
            }
        }
        return map;
    }

    /**
     * Stops the watcher.
     */
    public void stop() {
        synchronized (this) {
            active = false;
            notifyAll();
        }
    }
}
