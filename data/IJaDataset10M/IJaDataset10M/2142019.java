package net.sourceforge.eclipse.junitdoclet;

import org.apache.log4j.Logger;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class JunitdocletPlugin extends AbstractUIPlugin {

    public JunitdocletPlugin() {
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("net.sourceforge.eclipse.junitdoclet.JunitdocletPluginResources");
        } catch (MissingResourceException _ex) {
            resourceBundle = null;
        }
    }

    protected void initializeImageRegistry(ImageRegistry reg) {
        super.initializeImageRegistry(reg);
        reg.put("icon_junitdoclet", AbstractUIPlugin.imageDescriptorFromPlugin(getBundle().getSymbolicName(), "icons/jUnitDoclet.gif"));
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        console = new MessageConsole("JunitDoclet Console", getImageRegistry().getDescriptor("icon_junitdoclet"));
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    public static JunitdocletPlugin getDefault() {
        return plugin;
    }

    public static String getResourceString(String key) {
        ResourceBundle bundle = getDefault().getResourceBundle();
        try {
            String returnString = bundle == null ? key : bundle.getString(key);
            return returnString;
        } catch (MissingResourceException _ex) {
            return key;
        }
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public MessageConsole getConsole() {
        return console;
    }

    public static void log(int severity, String message, Throwable throwable) {
        org.eclipse.core.runtime.IStatus status = new Status(severity, getDefault().getBundle().getSymbolicName(), 0, message, throwable);
        getDefault().getLog().log(status);
    }

    public static final String ICON_JUNITDOCLET = "icon_junitdoclet";

    private static JunitdocletPlugin plugin;

    private ResourceBundle resourceBundle;

    private MessageConsole console;
}
