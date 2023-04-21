package net.beeger.osmorc.frameworkintegration;

import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

/**
 * This interface encapsulates framework-specific runtime configuration.
 *
 * @author <a href="mailto:janthomae@janthomae.de">Jan Thom&auml;</a>
 * @version $Id$
 */
public interface FrameworkRunner extends Disposable {

    /**
   * Returns an array of command line parameters that can be used to install and run the specified bundles.
   *
   * @param urlsOfBundlesToInstall an array containing the URLs of the bundles to be installed
   * @return a list of command line parameters
   */
    @NotNull
    public String[] getCommandlineParameters(@NotNull String[] urlsOfBundlesToInstall);

    /**
   * Returns a map of system properties to be set in order to install and run the specified bundles.
   *
   * @param urlsOfBundlesToInstall an array containing the URLs of the bundles to be installed
   * @return a map of system properties
   */
    @NotNull
    public Map<String, String> getSystemProperties(@NotNull String[] urlsOfBundlesToInstall);

    /**
   * Instructs the FrameworkRunnner to run any custom installation steps that are required for installing the given
   * bundles.
   *
   * @param urlsOfBundlesToInstall an array containing the URLs of the bundles to be installed.
   */
    public void runCustomInstallationSteps(@NotNull String[] urlsOfBundlesToInstall);

    /**
   * Returns true, if the framework supports the notion of exploded (not packaged to a jar file) bundles.
   *
   * @return true if the framework supports exploded bundles, false otherwise.
   */
    public boolean supportsExplodedBundles();

    /**
   * @return the main class of the framework to run.
   */
    @NotNull
    public String getMainClass();

    /**
   * @return the working directory in which the framework should be run.
   */
    @NotNull
    public String getWorkingDirectory();
}
