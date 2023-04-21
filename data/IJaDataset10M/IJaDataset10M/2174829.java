package com.amazonaws.eclipse.sdk.ui.classpath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import com.amazonaws.eclipse.sdk.ui.SdkInstall;

/**
 * The Classpath container containing the AWS SDK for Java JAR files, and those of
 * its third-party dependencies.
 */
public class AwsClasspathContainer implements IClasspathContainer {

    /** The SDK containing the libraries to expose on the Java Project classpath */
    private SdkInstall sdkInstall;

    /**
     * Creates a new AWS SDK for Java classpath container exposing the libraries
     * contained in the specified SDK.
     *
     * @param sdkInstall
     *            The SDK containing the libraries to expose on the Java project
     *            classpath.
     */
    public AwsClasspathContainer(SdkInstall sdkInstall) {
        if (sdkInstall == null) throw new IllegalArgumentException("No SDK version specified");
        this.sdkInstall = sdkInstall;
    }

    public IClasspathEntry[] getClasspathEntries() {
        return loadSdkClasspathEntriesAsArray(sdkInstall);
    }

    /**
     * @see org.eclipse.jdt.core.IClasspathContainer#getDescription()
     */
    public String getDescription() {
        return "AWS SDK for Java";
    }

    /**
     * Returns the version number of this SDK install.
     * @return the version number of this SDK install.
     */
    public String getVersion() {
        return sdkInstall.getVersion();
    }

    /**
     * @see org.eclipse.jdt.core.IClasspathContainer#getKind()
     */
    public int getKind() {
        return IClasspathContainer.K_APPLICATION;
    }

    /**
     * @see org.eclipse.jdt.core.IClasspathContainer#getPath()
     */
    public IPath getPath() {
        return new Path("com.amazonaws.eclipse.sdk.AWS_JAVA_SDK");
    }

    /**
     * Loads the JDT classpath entries for the AWS SDK for Java from the specified
     * SDK install base and returns them as an array.
     *
     * @param sdkInstall
     *            The SDK install from which to load the classpath entries.
     *
     * @return An array of the JDT classpath entries for the AWS SDK for Java at the
     *         specified install base.
     */
    private IClasspathEntry[] loadSdkClasspathEntriesAsArray(SdkInstall sdkInstall) {
        List<IClasspathEntry> classpathEntries = loadSdkClasspathEntries(sdkInstall);
        return classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]);
    }

    /**
     * Loads the JDT classpath entries for the AWS SDK for Java from the specified
     * SDK install and returns them as a list.
     *
     * @param sdkInstall
     *            The SDK install from which to load the classpath entries.
     *
     * @return A list of the JDT classpath entries for the AWS SDK for Java at the
     *         specified install base.
     */
    private List<IClasspathEntry> loadSdkClasspathEntries(SdkInstall sdkInstall) {
        List<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>();
        IPath sdkJarPath, sdkSourceJarPath;
        try {
            sdkJarPath = new Path(sdkInstall.getSdkJar().getAbsolutePath());
            sdkSourceJarPath = new Path(sdkInstall.getSdkSourceJar().getAbsolutePath());
        } catch (FileNotFoundException e) {
            return new ArrayList<IClasspathEntry>();
        }
        IClasspathAttribute externalJavadocPath = JavaCore.newClasspathAttribute(IClasspathAttribute.JAVADOC_LOCATION_ATTRIBUTE_NAME, sdkInstall.getJavadocURL());
        if (externalJavadocPath.getValue() != null) {
            classpathEntries.add(JavaCore.newLibraryEntry(sdkJarPath, sdkSourceJarPath, null, new IAccessRule[0], new IClasspathAttribute[] { externalJavadocPath }, true));
        } else {
            classpathEntries.add(JavaCore.newLibraryEntry(sdkJarPath, sdkSourceJarPath, null, new IAccessRule[0], new IClasspathAttribute[0], true));
        }
        for (File jarFile : sdkInstall.getThirdPartyJars()) {
            IPath thirdPartyJarPath = new Path(jarFile.getAbsolutePath());
            classpathEntries.add(JavaCore.newLibraryEntry(thirdPartyJarPath, thirdPartyJarPath, null, true));
        }
        return classpathEntries;
    }
}
