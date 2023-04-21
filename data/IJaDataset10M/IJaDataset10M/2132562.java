package org.jtools.classloader.report;

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import org.jtools.classloader.ClType;
import org.jtools.classloader.ClAdapter;
import org.jtools.classloader.ClAction;
import org.jtools.compare.PackageComparator;

/**
 * Utility methods for the classloader report.
 *
 * @since Ant 1.7
 */
public class ClReportUtil {

    private static ClReportUtil singleton = new ClReportUtil();

    /**
     * Gets the singleton report util.
     *
     * @return The singleton report util.
     */
    public static ClReportUtil getReportUtil() {
        return singleton;
    }

    /**
     * Sets the singleton report util.
     *
     * @param util
     *            New singleton report util instance.
     */
    public static void setReportUtil(ClReportUtil util) {
        if (util != null) {
            singleton = util;
        }
    }

    /**
     * Constructor for derived classes.
     */
    protected ClReportUtil() {
    }

    /**
     * Callback method to add classloaders to the list of loaders to report.
     *
     * @param context
     *            The context.
     * @param cl
     *            The classloader instance to add.
     * @param role
     *            The name of the classloader instance.
     * @param handlesByLoader
     *            A list of loader names by instance.
     * @param loaderByHandle
     *            A list of loader instances by name.
     * @param to
     *            The reporter to report errors against.
     * @return <code>true</code>, if successfully executed,
     *         <code>false</code> otherwise.
     */
    public boolean addLoaderToReport(ClReportContext context, ClassLoader cl, ClReportHandle role, Map<ClassLoader, SortedSet<ClReportHandle>> handlesByLoader, Map<ClReportHandle, ClassLoader> loaderByHandle, ClReporter to) {
        ClassLoader old = loaderByHandle.put(role, cl);
        if (old != null) {
            throw new RuntimeException("duplicate classloader " + role);
        }
        if (cl != null) {
            SortedSet<ClReportHandle> set = handlesByLoader.get(cl);
            boolean isNew = (set == null);
            if (set == null) {
                set = new TreeSet<ClReportHandle>();
                handlesByLoader.put(cl, set);
            }
            set.add(role);
            if (isNew) {
                ClAdapter adapter = context.getUtil().findAdapter(context, cl, null, to, role + "->parent", role.getName());
                boolean adapterFound = (adapter != null);
                if (adapterFound) {
                    ClassLoader parent = adapter.getParent(cl);
                    if (parent == null) {
                        parent = adapter.getDefaultParent();
                    }
                    if (parent != null) {
                        addLoaderToReport(context, adapter.getParent(cl), new ClReportHandle(ClType.PARENT, role.toString()), handlesByLoader, loaderByHandle, to);
                    }
                }
                adapter = context.getUtil().findAdapter(context, cl, ClAction.REPORT, to, "report for " + role, "");
                if (adapter != null) {
                    adapter.addReportable(context, cl, role, handlesByLoader, loaderByHandle);
                }
                return adapterFound && (adapter != null);
            }
        }
        return true;
    }

    /**
     * handle the report for a single classloader
     * @param context The context.
     * @param to
     *            Reporter to report.
     * @param cl
     *            ClassloaderBase instance to report.
     * @param name
     *            name of the classloader instance.
     * @param handlesByLoader Handles by loader.
     */
    public void report(ClReportContext context, ClReporter to, ClassLoader cl, ClReportHandle name, Map handlesByLoader) {
        to.beginClassloader(name);
        ClAdapter baseAdapter = context.getUtil().findAdapter(context, cl, null, to, "parent for " + name, "");
        if (baseAdapter != null) {
            ClassLoader parent = baseAdapter.getParent(cl);
            if (parent != null) {
                SortedSet handles = (SortedSet) handlesByLoader.get(parent);
                to.reportExlicitelyParent((ClReportHandle) handles.first());
            } else {
                parent = baseAdapter.getDefaultParent();
                if (parent != null) {
                    SortedSet handles = (SortedSet) handlesByLoader.get(parent);
                    to.reportImplicitelyParent((ClReportHandle) handles.first());
                } else {
                    to.reportImplicitelyParent(ClReportHandle.BOOTSTRAPHANDLE);
                }
            }
        }
        to.reportClass(cl.getClass());
        SortedSet roles = (SortedSet) handlesByLoader.get(cl);
        for (Iterator iRole = roles.iterator(); iRole.hasNext(); ) {
            to.reportRole((ClReportHandle) iRole.next());
        }
        ClAdapter adapter = context.getUtil().findAdapter(context, cl, ClAction.GETPATH, to, "entries for " + name, "");
        if (adapter != null) {
            String[] cp = adapter.getClasspath(context, cl, false);
            if (cp == null) {
                to.reportError("entries for " + name + " not investigatable (adapter retrieves no path)");
            } else {
                to.beginEntries(cp.length);
                for (int i = 0; i < cp.length; i++) {
                    to.reportEntry("url", cp[i]);
                }
            }
        }
        if (context.isReportPackages()) {
            reportPackages(context, to, baseAdapter, cl, name);
        }
        adapter = context.getUtil().findAdapter(context, cl, ClAction.REPORT, to, "additional parameters for " + name, "");
        if (adapter != null) {
            adapter.report(to, context, cl, name);
        }
    }

    /**
     * Handles the report.
     *
     * @param context
     *            The report context.
     * @param handlesByLoader
     *            A map.
     * @param loaderByHandle
     *            A map.
     * @param to
     *            The reporter to report to.
     * @param allHandlersFound
     *            a flag indicating whether all handlers for classloaders where
     *            found.
     */
    public void report(ClReportContext context, Map handlesByLoader, Map loaderByHandle, ClReporter to, boolean allHandlersFound) {
        to.beginReport();
        if (!allHandlersFound) {
            to.reportError("WARNING: As of missing Loaderhandlers," + " this report might not be complete.");
        }
        URL[] urls = context.getUtil().getBootstrapClasspathURLs();
        if (urls == null) {
            to.reportError("WARNING: Unable to determine bootstrap classpath." + "\n         Please report this error to Ant's bugtracking " + " system with information" + "\n         about your environment " + " (JVM-Vendor, JVM-Version, OS, application context).");
        } else {
            to.beginClassloader(ClReportHandle.BOOTSTRAPHANDLE);
            to.beginEntries(urls.length);
            for (int i = 0; i < urls.length; i++) {
                to.reportEntry(urls[i]);
            }
            to.endEntries(urls.length);
            to.endClassloader(ClReportHandle.BOOTSTRAPHANDLE);
        }
        for (Iterator iRole = loaderByHandle.keySet().iterator(); iRole.hasNext(); ) {
            ClReportHandle role = (ClReportHandle) iRole.next();
            ClassLoader cl = (ClassLoader) loaderByHandle.get(role);
            if (cl == null) {
                if (role.isSingleton()) {
                    to.reportUnassignedRole(role);
                }
            } else {
                SortedSet handles = (SortedSet) handlesByLoader.get(cl);
                if (role.equals(handles.first())) {
                    report(context, to, cl, role, handlesByLoader);
                }
            }
        }
        to.endReport();
    }

    private void reportPackages(ClReportContext task, ClReporter to, ClAdapter adapter, ClassLoader classloader, ClReportHandle role) {
        Package[] pkgs = adapter.getPackages(task, classloader, role);
        if (pkgs == null) {
            to.reportError("packages of " + role + " not investigatable");
        } else {
            Arrays.<Package>sort(pkgs, PackageComparator.SINGLETON);
            to.beginPackages(pkgs.length);
            for (int i = 0; i < pkgs.length; i++) {
                to.reportPackage(pkgs[i].getName());
            }
            to.endPackages(pkgs.length);
        }
    }
}
