package com.google.appengine.eclipse.core.sdk;

import com.google.appengine.eclipse.core.AppEngineCorePlugin;
import com.google.appengine.eclipse.core.AppEngineCorePluginLog;
import com.google.gdt.eclipse.core.StatusUtilities;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * A singleton class that is used to return bridge implementations for a
 * particular App Engine SDK location.
 * 
 * TODO: Need to have this class listen for App Engine SDK addition and removal
 * events, so that bridges can be removed when App Engine SDKs are removed, and
 * bridges can be created when App Engine SDKs are created.
 * 
 */
public class AppEngineBridgeFactory {

    /**
   * Pairs an {@link AppEngineBridge} and the last modified time stamp of the
   * underlying install URL. We use this information to determine when our cache
   * is stale.
   */
    private static class BridgeCacheEntry {

        private AppEngineBridge bridge;

        private long sdkLocationLastModified;

        private BridgeCacheEntry(AppEngineBridge bridge, long sdkLocationLastModified) {
            this.bridge = bridge;
            this.sdkLocationLastModified = sdkLocationLastModified;
        }

        public AppEngineBridge getAppEngineBridge() {
            return bridge;
        }

        public long getSdkLocationLastModified() {
            return sdkLocationLastModified;
        }
    }

    private static final Map<IPath, BridgeCacheEntry> pathToBridgeCacheEntryMap = new HashMap<IPath, BridgeCacheEntry>();

    public static synchronized AppEngineBridge getAppEngineBridge(IPath sdkLocation) throws CoreException {
        File sdkDir = sdkLocation.toFile();
        if (sdkDir.exists()) {
            if (sdkDir.isFile()) {
                throw new CoreException(new Status(Status.ERROR, AppEngineCorePlugin.PLUGIN_ID, "SDK location '" + sdkDir.getAbsolutePath() + "' is not a directory"));
            }
        } else {
            throw new CoreException(new Status(Status.ERROR, AppEngineCorePlugin.PLUGIN_ID, "SDK directory '" + sdkDir.getAbsolutePath() + "' does not exist"));
        }
        long sdkLocationLastModified = sdkDir.lastModified();
        BridgeCacheEntry bridgeCacheEntry = pathToBridgeCacheEntryMap.get(sdkLocation);
        if (bridgeCacheEntry == null || bridgeCacheEntry.getSdkLocationLastModified() != sdkLocationLastModified) {
            bridgeCacheEntry = new BridgeCacheEntry(createBridge(sdkLocation), sdkLocationLastModified);
            pathToBridgeCacheEntryMap.put(sdkLocation, bridgeCacheEntry);
        }
        return bridgeCacheEntry.getAppEngineBridge();
    }

    /**
   * Creates an AppEngineBridge using classes from the bundled
   * appengine-tools-api.jar. This is not cached, unlike
   * getAppEngineBridge(IPath) above.
   */
    protected static AppEngineBridge createBridgeWithBundledToolsJar(IPath sdkLocation) throws CoreException {
        File apiToolsJar = AppEngineCorePlugin.getDefault().getStateLocation().append(AppEngineBridge.APPENGINE_TOOLS_JAR_NAME).toFile();
        return createBridge(sdkLocation, apiToolsJar, true);
    }

    private static AppEngineBridge createBridge(IPath sdkLocation) throws CoreException {
        File apiToolsJar = sdkLocation.append(GaeSdk.APPENGINE_TOOLS_API_JAR_PATH).toFile();
        return createBridge(sdkLocation, apiToolsJar, false);
    }

    /**
   * Creates an AppEnginge bridge.
   * 
   * @param sdkLocation The location of the sdk
   * @param apiToolsJar The location of the api-tools-jar
   * @param doSetSdkRoot If true, will set the location of the sdk in the
   *          api-tools-jar
   */
    private static AppEngineBridge createBridge(IPath sdkLocation, File apiToolsJar, boolean doSetSdkRoot) throws CoreException {
        try {
            URL bridgeJarURL = null;
            if (AppEngineCorePlugin.getDefault().inDevelopmentMode()) {
                bridgeJarURL = FileLocator.find(AppEngineCorePlugin.getDefault().getBundle(), new Path("proxy_bin"), (Map<?, ?>) null);
            } else {
                bridgeJarURL = AppEngineCorePlugin.getDefault().getStateLocation().append(AppEngineBridge.APPENGINE_PROXY_JAR_NAME).toFile().toURI().toURL();
            }
            if (bridgeJarURL == null) {
                throw new CoreException(StatusUtilities.newErrorStatus(("Unable to locate " + AppEngineBridge.APPENGINE_PROXY_JAR_NAME + " at the root of " + AppEngineCorePlugin.PLUGIN_ID), AppEngineCorePlugin.PLUGIN_ID));
            }
            URL resolvedBridgeJarURL = FileLocator.resolve(bridgeJarURL);
            URLClassLoader bridgeClassLoader = new URLClassLoader(new URL[] { apiToolsJar.toURI().toURL(), resolvedBridgeJarURL }, AppEngineBridgeFactory.class.getClassLoader());
            if (doSetSdkRoot) {
                Class<?> sdkInfoClass = bridgeClassLoader.loadClass("com.google.appengine.tools.info.SdkInfo");
                Method setSdkRoot = sdkInfoClass.getMethod("setSdkRoot", File.class);
                Object sdkInfo = sdkInfoClass.newInstance();
                setSdkRoot.invoke(sdkInfo, sdkLocation.toFile());
            }
            Class<?> clazz = bridgeClassLoader.loadClass("com.google.appengine.eclipse.core.proxy.AppEngineBridgeImpl");
            AppEngineBridge bridge = (AppEngineBridge) clazz.newInstance();
            return bridge;
        } catch (Throwable e) {
            AppEngineCorePluginLog.logError(e);
            throw new CoreException(new Status(Status.ERROR, AppEngineCorePlugin.PLUGIN_ID, "Failed to initialize App Engine SDK at " + sdkLocation.toOSString(), e));
        }
    }
}
