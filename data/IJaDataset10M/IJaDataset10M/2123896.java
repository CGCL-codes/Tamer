package ti.oscript.console;

import java.io.IOException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import oscript.OscriptInterpreter;
import oscript.fs.AbstractFile;
import oscript.parser.ParseException;
import ti.mcore.Environment;
import ti.plato.ui.u.WorkspaceSaveParticipant;

/**
 * The main plugin class to be used in the desktop.
 */
public class ConsolePlugin extends AbstractUIPlugin {

    static AbstractFile CONSOLE_INIT_OS;

    private static ConsolePlugin plugin;

    static WorkspaceSaveContainer workspaceSaveContainer = new WorkspaceSaveContainer();

    /**
	 * The constructor.
	 */
    public ConsolePlugin() {
        if (plugin == null) {
            ti.oscript.OscriptPlugin.getDefault().registerClient(this);
            try {
                OscriptInterpreter.eval(OscriptInterpreter.resolve("console-global-init.os", false));
                CONSOLE_INIT_OS = OscriptInterpreter.resolve("console-init.os", false);
            } catch (IOException e) {
                Environment.getEnvironment().unhandledException(e);
            } catch (ParseException e) {
                Environment.getEnvironment().unhandledException(e);
            }
        }
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        new WorkspaceSaveParticipant(workspaceSaveContainer, this, "save2", -1);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        ResourcesPlugin.getWorkspace().removeSaveParticipant(this);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static ConsolePlugin getDefault() {
        return plugin;
    }

    /**
   * Log an error to the ILog for this plugin
   * 
   * @param message the localized error message text
   * @param exception the associated exception, or null
   */
    public static void logError(String message, Throwable exception) {
        System.err.println("message=" + message + ", exception=" + exception);
        Thread.dumpStack();
        plugin.getLog().log(new Status(IStatus.ERROR, plugin.getBundle().getSymbolicName(), 0, message, exception));
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("ti.oscript.console", path);
    }
}
