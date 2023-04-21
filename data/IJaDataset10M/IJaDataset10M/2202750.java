package com.google.gwt.eclipse.core.runtime;

import com.google.gdt.eclipse.core.JavaProjectUtilities;
import com.google.gwt.eclipse.core.launch.GWTLaunchConfiguration;
import com.google.gwt.eclipse.core.preferences.GWTPreferences;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathEntryResolver;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Special RuntimeClasspathEntryResolver for GWT projects that mangles the
 * classpath for OOPHM to work if the appropriate launch settings are set.
 */
public class RuntimeClasspathEntryResolver implements IRuntimeClasspathEntryResolver {

    private static final IRuntimeClasspathEntry[] NO_ENTRIES = new IRuntimeClasspathEntry[0];

    public IRuntimeClasspathEntry[] resolveRuntimeClasspathEntry(IRuntimeClasspathEntry entry, IJavaProject project) throws CoreException {
        GWTRuntime gwtSdk = findGWTSdk(entry);
        if (gwtSdk == null) {
            return NO_ENTRIES;
        }
        IClasspathEntry[] classpathEntries = gwtSdk.getClasspathEntries();
        return resolveClasspathEntries(Arrays.asList(classpathEntries), false);
    }

    public IRuntimeClasspathEntry[] resolveRuntimeClasspathEntry(IRuntimeClasspathEntry entry, ILaunchConfiguration configuration) throws CoreException {
        GWTRuntime gwtSdk = findGWTSdk(entry);
        if (gwtSdk == null) {
            return NO_ENTRIES;
        }
        List<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>(Arrays.asList(gwtSdk.getClasspathEntries()));
        boolean useOOPHM = GWTLaunchConfiguration.launchWithTransitionalOophm(configuration);
        if (useOOPHM) {
            if (gwtSdk instanceof GWTJarsRuntime) {
                IPath oophmJar = gwtSdk.getInstallationPath().append(GWTJarsRuntime.GWT_OOPHM_JAR);
                if (oophmJar.toFile().exists()) {
                    classpathEntries.add(0, JavaCore.newLibraryEntry(oophmJar, null, null));
                } else {
                }
            } else {
                IJavaProject oophmProject = JavaProjectUtilities.findJavaProject(GWTProjectsRuntime.GWT_OOPHM_PROJECT_NAME);
                if (oophmProject != null) {
                    classpathEntries.add(0, JavaCore.newProjectEntry(new Path(oophmProject.getElementName()).makeAbsolute()));
                }
            }
        }
        return resolveClasspathEntries(classpathEntries, useOOPHM);
    }

    public IVMInstall resolveVMInstall(IClasspathEntry entry) {
        return null;
    }

    /**
   * Expand out the list of dependencies for a given IJavaProject.
   */
    private List<IRuntimeClasspathEntry> dependenciesForProject(IJavaProject project) throws CoreException {
        ArrayList<IRuntimeClasspathEntry> out = new ArrayList<IRuntimeClasspathEntry>();
        String[] deps = JavaRuntime.computeDefaultRuntimeClassPath(project);
        for (String dep : deps) {
            IRuntimeClasspathEntry cpEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(dep));
            out.add(cpEntry);
        }
        return out;
    }

    private GWTRuntime findGWTSdk(IRuntimeClasspathEntry entry) {
        GWTRuntime gwtSdk = GWTPreferences.getSdkManager().findSdkForPath(entry.getPath());
        if (gwtSdk != null) {
            return gwtSdk;
        }
        if (entry.getType() != IRuntimeClasspathEntry.PROJECT) {
            return null;
        }
        String entryProjectName = entry.getPath().lastSegment();
        IJavaProject entryJavaProject = JavaProjectUtilities.findJavaProject(entryProjectName);
        if (entryJavaProject != null && GWTProjectsRuntime.isGWTRuntimeProject(entryJavaProject)) {
            gwtSdk = GWTProjectsRuntime.syntheziseContributorRuntime();
            if (gwtSdk.validate().isOK()) {
                return gwtSdk;
            }
        }
        return null;
    }

    /**
   * Take a list of IRuntimeClasspathEntry and remove all of the jars that
   * pertain to SWT from it. Mutates the list in place.
   */
    private void removeSwtJars(Collection<IRuntimeClasspathEntry> entries) {
        List<IRuntimeClasspathEntry> swtJars = new ArrayList<IRuntimeClasspathEntry>();
        for (IRuntimeClasspathEntry entry : entries) {
            if (entry.getType() == IRuntimeClasspathEntry.ARCHIVE && entry.getPath().lastSegment().startsWith("org.eclipse.swt")) {
                swtJars.add(entry);
            }
        }
        entries.removeAll(swtJars);
    }

    /**
   * Given a list of IClasspathEntry and a boolean signaling whether to use
   * OOPHM, produce an array of IRuntimeClasspathEntry based on that list, but
   * with entries that were projects expanded out to include their dependencies,
   * and if we're using OOPHM, with the SWT jars removed.
   */
    private IRuntimeClasspathEntry[] resolveClasspathEntries(List<IClasspathEntry> classpathEntries, boolean useOOPHM) throws CoreException {
        LinkedHashSet<IRuntimeClasspathEntry> runtimeClasspathEntries = new LinkedHashSet<IRuntimeClasspathEntry>();
        for (IClasspathEntry classpathEntry : classpathEntries) {
            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
                String projectName = classpathEntry.getPath().lastSegment();
                IJavaProject theproject = JavaProjectUtilities.findJavaProject(projectName);
                IRuntimeClasspathEntry projectEntry = JavaRuntime.newProjectRuntimeClasspathEntry(theproject);
                runtimeClasspathEntries.add(projectEntry);
                runtimeClasspathEntries.addAll(dependenciesForProject(theproject));
            } else {
                runtimeClasspathEntries.add(JavaRuntime.newArchiveRuntimeClasspathEntry(classpathEntry.getPath()));
            }
        }
        if (useOOPHM) {
            removeSwtJars(runtimeClasspathEntries);
        }
        return runtimeClasspathEntries.toArray(NO_ENTRIES);
    }
}
