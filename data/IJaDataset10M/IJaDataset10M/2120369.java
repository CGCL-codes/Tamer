package com.ibm.tuningfork.core;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.ibm.tuningfork.core.dialog.ErrorHandling;
import com.ibm.tuningfork.core.refresh.Refresher;
import com.ibm.tuningfork.core.sharing.Transceiver;
import com.ibm.tuningfork.infra.util.FileUtility;
import com.ibm.tuningfork.infra.util.WarningIssuer;

/**
 * The user interface plugin for the TuningFork visualization and analysis tool.
 */
public final class TuningForkCorePlugin extends AbstractUIPlugin {

    public static final String ID = "com.ibm.tuningfork.core";

    private static TuningForkCorePlugin plugin;

    private Refresher refresher;

    public TuningForkCorePlugin() {
        plugin = this;
    }

    public final void start(final BundleContext context) throws Exception {
        super.start(context);
        IPath workspace = Platform.getLocation();
        IPath cache = workspace.append("cache");
        IPath socket = workspace.append("socketTraces");
        IPath shared = workspace.append("sharedTraces");
        IPath imported = workspace.append("importedTraces");
        String fourDown = Platform.getConfigurationLocation().getURL().getPath();
        FileUtility.setUserSpecificDirectory(Path.fromOSString(fourDown).removeLastSegments(4).toOSString());
        FileUtility.setCacheDirectory(cache.toOSString());
        FileUtility.setSocketDirectory(socket.toOSString());
        FileUtility.setSharedDirectory(shared.toOSString());
        FileUtility.setImportDirectory(imported.toOSString());
        EventTypeSpaceExtensionPointManager.initialize();
        ChunkProcessorDescriptorExtensionPointManager.initialize();
        FeedGroupManager.initialize();
        TextWriterExtensionPointManager.initialize();
        Transceiver.initialize();
        WarningIssuer.setInstance(new WarningIssuer() {

            public int warn(String title, String message, String[] buttons) {
                return ErrorHandling.displayWarningWithResponseFromAnyThread(title, message, buttons);
            }
        });
    }

    public final void stop(final BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    public static final TuningForkCorePlugin getDefault() {
        return plugin;
    }

    public static final ImageDescriptor getImageDescriptor(final String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(ID, path);
    }

    public final synchronized Refresher getRefresher() {
        if (refresher == null) {
            refresher = new Refresher();
        }
        return refresher;
    }
}
