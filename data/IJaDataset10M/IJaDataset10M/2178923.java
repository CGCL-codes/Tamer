package net.sourceforge.c4jplugin.runtime;

import java.io.IOException;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

public class C4JRuntimeContainer implements IClasspathContainer {

    private IClasspathEntry[] fClasspathEntries;

    private static IPath c4jrtPath = null;

    public IClasspathEntry[] getClasspathEntries() {
        if (fClasspathEntries == null) {
            if (c4jrtPath == null) getC4JRtClasspath();
            fClasspathEntries = new IClasspathEntry[1];
            fClasspathEntries[0] = JavaCore.newLibraryEntry(c4jrtPath, null, null, false);
        }
        return fClasspathEntries;
    }

    public String getDescription() {
        return Messages.c4jRuntimeContainerName;
    }

    public int getKind() {
        return IClasspathContainer.K_APPLICATION;
    }

    public IPath getPath() {
        return new Path(C4JRuntime.C4JRT_CONTAINER);
    }

    /**
	 * Get the c4j.jar classpath entry. This is usually in
	 * plugins/net.sourceforge.c4jplugin.runtime_ <VERSION>/c4j.jar
	 */
    public static IPath getC4JRtClasspath() {
        if (c4jrtPath == null) {
            Bundle runtime = Platform.getBundle(C4JRuntime.ID_PLUGIN);
            if (runtime != null) {
                URL installLoc = runtime.getEntry(C4JRuntime.C4J_LIBRARY_NAME);
                if (installLoc == null) {
                    IPath path = new Path(runtime.getLocation().split("@")[1]);
                    c4jrtPath = new Path(Platform.getInstallLocation().getURL().getFile()).append(path);
                } else {
                    try {
                        c4jrtPath = new Path(FileLocator.resolve(installLoc).getPath());
                    } catch (IOException e) {
                    }
                }
            }
        }
        return c4jrtPath;
    }
}
