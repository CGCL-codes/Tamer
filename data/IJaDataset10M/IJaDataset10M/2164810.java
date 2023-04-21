package com.amazonaws.eclipse.sdk.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

/**
 * A metadata file containing the particular version of the AWS SDK for Java being
 * used by a particular project.
 */
public class SdkProjectMetadata {

    private static final String SDK_INSTALL_ROOT_PROPERTY = "sdkInstallRoot";

    private static final String SDK_PROPERTIES_FILE_NAME = "sdk.properties";

    final IProject project;

    public SdkProjectMetadata(IProject project) {
        this.project = project;
    }

    /**
     * Returns the root directory of the version of the SDK plugin being used by
     * the specified project.
     *
     * @return The root directory of the version of the SDK plugin being used by
     * the specified project.
     * @throws IOException if the plugin root directory could not be written to.
     */
    public File getSdkInstallRootForProject() throws IOException {
        Properties properties = loadProperties();
        String installRoot = properties.getProperty(SDK_INSTALL_ROOT_PROPERTY);
        if (installRoot == null) return null;
        return new File(installRoot);
    }

    /**
     * Writes the propery file containing the root directory of the specified SDK
     * version.
     *
     * @param sdkInstallRoot The root directory of an SDK installation to be recorded.
     * @throws IOException if the plugin root directory could not be written to.
     */
    public void setSdkInstallRootForProject(File sdkInstallRoot) throws IOException {
        Properties properties = loadProperties();
        properties.setProperty(SDK_INSTALL_ROOT_PROPERTY, sdkInstallRoot.getAbsolutePath());
        saveProperties(properties);
    }

    /**
     * Empties the properties file to prepare for a version change.
     *
     * @throws IOException if the plugin root directory could not be written to.
     */
    public void clear() throws IOException {
        Properties properties = new Properties();
        saveProperties(properties);
    }

    private Properties loadProperties() throws IOException {
        IPath projectMetadataLocation = project.getWorkingLocation(SdkPlugin.PLUGIN_ID);
        Properties properties = new Properties();
        IPath sdkProperties = projectMetadataLocation.append(SDK_PROPERTIES_FILE_NAME);
        if (sdkProperties.toFile().exists() == false) {
            return properties;
        }
        properties.load(new FileInputStream(sdkProperties.toFile()));
        return properties;
    }

    private void saveProperties(Properties properties) throws IOException {
        IPath projectMetadataLocation = project.getWorkingLocation(SdkPlugin.PLUGIN_ID);
        IPath sdkProperties = projectMetadataLocation.append(SDK_PROPERTIES_FILE_NAME);
        properties.store(new FileOutputStream(sdkProperties.toFile()), null);
    }
}
