package com.msli.rcp.app;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import com.msli.app.status.Status;
import com.msli.core.exception.AlreadyInitializedException;
import com.msli.core.exception.NotYetInitializedException;
import com.msli.core.util.CoreUtils;
import com.msli.core.util.Disposable;
import com.msli.core.util.Singleton;
import com.msli.rcp.status.StatusReporter;
import com.msli.rcp.util.DisplayRunner;

/**
 * A pseudo service that provides global access to plugin-specific application
 * resources, via the application's plugin, as well as global services commonly
 * used by applications.
 * <p>
 * The client application must initialize this service when its plugin
 * (MsliApplicationPlugin) is created.
 * <p>
 * In RCP, the "application" is a singleton, and each application has an
 * application plugin/bundle. RCP exposes many global resources (references,
 * services) via singletons. This class consolidates the commonly used ones in
 * one place. It also exposes the application plugin, through which
 * plugin-specific resources are accessed. For the client, using this class
 * helps to eliminate reliance on the singleton pattern in implementing an
 * application and the plugins that may contribute to it. Use of this service
 * will hopefully also ease the transition to Eclipse 4 (which intends to
 * eliminate the use of singletons).
 * @author jonb
 */
public class MsliApplicationService extends Disposable.IdentityEquality implements Singleton.Disposable {

    /**
	 * Used for extension and to create a singleton instance.
	 * @param plugin Shared exposed application plugin. Never null.
	 * @throws AlreadyInitializedException if the client has already created an
	 * instance of this class.
	 */
    public MsliApplicationService(MsliApplicationPlugin plugin) {
        CoreUtils.assertNotYetInit(INSTANCE, this, "This is a singleton. It can only be created once. instance=" + INSTANCE);
        CoreUtils.assertNonNullArg(plugin);
        _plugin = plugin;
        INSTANCE = this;
    }

    /**
	 * Used by the client application to set its application plugin.
	 * Must be called by the client application after the plugin is
	 * created, but before this service is used. Re-initialization is not
	 * allowed.
	 * @param plugin Shared exposed application plugin. Never null.
	 * @throws AlreadyInitializedException if the plugin has already been set.
	 */
    public void setApplicationPlugin(MsliApplicationPlugin plugin) {
        CoreUtils.assertNotDisposed(this);
        CoreUtils.assertNotYetInit(_plugin, this, "Application plugin is already set.");
        CoreUtils.assertNonNullArg(plugin);
        _plugin = plugin;
    }

    /**
	 * Gets the application plugin associated with this application service.
	 * @return Shared exposed application plugin.  Never null.
	 * @throws NotYetInitializedException if the plugin has not yet been set.
	 */
    public MsliApplicationPlugin getApplicationPlugin() {
        CoreUtils.assertNotDisposed(this);
        CoreUtils.assertAlreadyInit(_plugin, this, "Must first call setApplicationPlugin().");
        return _plugin;
    }

    protected void implDispose() {
        super.dispose();
        _plugin = CoreUtils.dispose(_plugin);
    }

    private MsliApplicationPlugin _plugin = null;

    /**
	 * Gets the singleton instance. Subclasses should forward to this super
	 * class.
	 * @throws NotYetInitializedException if the client has not yet created an
	 * instance of this class.
	 */
    public static MsliApplicationService getInstance() {
        CoreUtils.assertAlreadyInit(INSTANCE, null, "Must first create an instance of this class.");
        CoreUtils.assertNotDisposed(INSTANCE);
        return INSTANCE;
    }

    /**
	 * Gets the global application plugin.
	 * @return Shared exposed application plugin.  Never null.
	 * @throws NotYetInitializedException if the plugin has not yet been set.
	 */
    public static MsliApplicationPlugin getPlugin() {
        return getInstance().getApplicationPlugin();
    }

    /**
	 * Safely gets the global application plugin. If the plugin has not yet been
	 * initialized, returns null instead of throwing an exception.
	 * @return Shared exposed application plugin. None if null.
	 */
    public static MsliApplicationPlugin getPluginSafely() {
        try {
            return getInstance().getApplicationPlugin();
        } catch (NotYetInitializedException ex) {
            return null;
        }
    }

    /**
	 * Gets the global workbench associated with the application.
	 * @returns Shared exposed workbench. Never null.
	 * @throws IllegalStateException if the application has not yet created
	 * the workbench (by calling PlatformUI.createAndRunWorkbench()).
	 */
    public static IWorkbench getWorkbench() {
        return PlatformUI.getWorkbench();
    }

    /**
	 * Gets the global display associated with the application workbench.
	 * @returns Shared exposed display. Never null.
	 * @throws IllegalStateException if the application has not yet created
	 * the workbench (by calling PlatformUI.createAndRunWorkbench()).
	 */
    public static Display getDisplay() {
        return getWorkbench().getDisplay();
    }

    /**
	 * Gets the currently active window associated with the application
	 * workbench. Not intended for use during GUI construction since the target
	 * window will not be active.
	 * @returns Shared exposed window. Null if no window is active.
	 * @throws IllegalStateException if the application has not yet created the
	 * workbench (by calling PlatformUI.createAndRunWorkbench()).
	 */
    public static IWorkbenchWindow getActiveWindow() {
        Display display = getDisplay();
        DisplayRunner<IWorkbenchWindow> runner = new DisplayRunner<IWorkbenchWindow>(display) {

            @Override
            protected IWorkbenchWindow doTask(Display display) {
                return getWorkbench().getActiveWorkbenchWindow();
            }
        };
        IWorkbenchWindow window = runner.syncExec();
        return window;
    }

    /**
	 * Gets the shell associated with the currently active application window.
	 * @returns Shared exposed shell. Null if no window is active.
	 * @throws IllegalStateException if the application has not yet created
	 * the workbench (by calling PlatformUI.createAndRunWorkbench()).
	 */
    public static Shell getActiveShell() {
        IWorkbenchWindow window = getActiveWindow();
        if (window == null) return null;
        return window.getShell();
    }

    /**
	 * Gets the RCP preference store associated with the GUI. Intended for use
	 * by the application to configure aspects of the GUI directly supported by
	 * RCP. Such preferences are based on the "old" preference system, and use a
	 * global preference store. For application-specific preferences, use
	 * MsliApplicationPlugin.getPreferenceService().
	 * @returns Shared exposed preference store. Never null.
	 * @see MsliApplicationPlugin#getPreferenceService()
	 */
    public static IPreferenceStore getRcpGuiPreferences() {
        return PlatformUI.getPreferenceStore();
    }

    /**
	 * Gets the global workspace used by the application. As a side effect,
	 * activates the resource plugin.
	 * @returns Shared exposed workspace. Never null.
	 * @throws IllegalStateException if the workspace was closed.
	 */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
	 * Gets the default status reporter for this plugin, which will log all
	 * status safely, and show all status in a dialog assuredly. For other
	 * behavior, including progress reporting, clients must create their own
	 * reporter.
	 * @return Shared singleton status reporter. Never null.
	 * @see StatusReporter
	 */
    public static StatusReporter getStatusReporter() {
        if (STATUS_REPORTER == null) {
            STATUS_REPORTER = StatusReporter.newShowDialog();
        }
        return STATUS_REPORTER;
    }

    private static StatusReporter STATUS_REPORTER = null;

    /**
	 * Reports status to the user using the default status reporter. The
	 * application log will be safely updated, and a user dialog will assuredly
	 * appear.
	 * @param status Temp input status object. Never null.
	 */
    public static void reportStatus(Status status) {
        getStatusReporter().reportStatus(status);
    }

    /**
	 * Reports status to the user using the default status reporter. The
	 * application log will be safely updated, and a user dialog will assuredly
	 * appear.
	 * @param type Status type. Never null.
	 * @param title Status title. If null, defaults to the exception type.
	 * @param message Status message. If null, defaults to the exception
	 * message.
	 * @param exception Shared exposed causal exception. If null, none will be
	 * included, the default title will be none, and the default message will be
	 * "A problem has occurred.".
	 */
    public static void reportStatus(Status.Type type, String title, String message, Throwable cause) {
        getStatusReporter().reportStatus(type, title, message, cause);
    }

    /**
	 * Assuredly resolves a display for the plugin (i.e. throws no exceptions,
	 * never returns null). If need be (e.g. no plugin), returns the default
	 * display. Typically used when resolving a shell for a dialog.
	 * @return Shared exposed display. Never null.
	 */
    public static Display getDisplayAssuredly() {
        Display display = null;
        try {
            display = getDisplay();
            if (display != null) return display;
        } catch (Exception ex) {
        }
        display = Display.getDefault();
        if (display != null) return display;
        CoreUtils.assertNonNullState(display, null, "System returned a null Display.  Should never happen.");
        return display;
    }

    /**
	 * Assuredly resolves a shell for the plugin (i.e. throws no exceptions,
	 * never returns null). If need be (e.g. no plugin or active window),
	 * returns a TempShell. Typically used when a shell is needed for a dialog.
	 * If a TempShell is returned, the user is responsible for disposing it.
	 * @return Shared exposed existing shell, or a ceded TempShell. Never null.
	 */
    public static Shell getShellAssuredly() {
        Shell shell = null;
        try {
            shell = getActiveShell();
            if (shell != null) return shell;
        } catch (Exception ex) {
        }
        final Display display = getDisplayAssuredly();
        DisplayRunner<Shell> runner = new DisplayRunner<Shell>(display) {

            @Override
            protected Shell doTask(Display display) {
                return display.getActiveShell();
            }
        };
        shell = runner.syncExec();
        if (shell != null) return shell;
        shell = new TempShell(display);
        CoreUtils.assertNonNullState(shell, null, "System returned a null Shell.  Should never happen.");
        return shell;
    }

    /**
	 * Gets a shell provider that simply calls getShellAssuredly(). Typically
	 * used when a shell is needed for a dialog. If a TempShell is returned, the
	 * user is responsible for disposing it.
	 * @return Shared output provider. Never null.
	 */
    public static IShellProvider getShellProvider() {
        if (SHELL_PROVIDER == null) {
            SHELL_PROVIDER = new IShellProvider() {

                @Override
                public Shell getShell() {
                    return getShellAssuredly();
                }
            };
        }
        return SHELL_PROVIDER;
    }

    private static IShellProvider SHELL_PROVIDER = null;

    /**
	 * Intended as a "temporary" shell, which is created for a single use, such
	 * as for showing a dialog when no active shell exists. Unlike a "normal"
	 * shell, the client is always responsible for disposing a temporary one.
	 */
    public static class TempShell extends Shell {

        TempShell(Display display) {
            super(display);
        }

        @Override
        protected void checkSubclass() {
        }
    }

    private static MsliApplicationService INSTANCE = null;
}
