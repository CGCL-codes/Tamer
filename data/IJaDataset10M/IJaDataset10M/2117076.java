package net.sf.jmoney.serializeddatastore;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import net.sf.jmoney.JMoneyPlugin;
import net.sf.jmoney.model2.DatastoreManager;
import net.sf.jmoney.model2.Session;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/** 
 * The main class for the plug-in that implements the data storage
 * in an XML file.
 *
 * @author Nigel Westbury
 */
public class SerializedDatastorePlugin extends AbstractUIPlugin {

    public static final boolean DEBUG = "true".equalsIgnoreCase(Platform.getDebugOption("net.sf.jmoney.serializeddatastore/debug"));

    private static SerializedDatastorePlugin plugin;

    private ResourceBundle resourceBundle;

    private static String[] filterPatterns;

    private static String[] filterNames;

    /**
	 * The constructor.
	 */
    public SerializedDatastorePlugin() {
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("net.sf.jmoney.serializeddatastore.Language");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }

    /**
	 * Returns the shared instance.
	 */
    public static SerializedDatastorePlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
    public static String getResourceString(String key) {
        ResourceBundle bundle = SerializedDatastorePlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
	 * Returns the plugin's resource bundle,
	 */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        init();
    }

    /**
	 * Initialize the list of file types supported by the plug-ins.
	 */
    public static void init() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] elements = registry.getConfigurationElementsFor("net.sf.jmoney.serializeddatastore.filestores");
        int count = 0;
        for (IConfigurationElement element : elements) {
            if (element.getName().equals("file-format")) {
                count++;
            }
        }
        filterPatterns = new String[count];
        filterNames = new String[count];
        int k = 0;
        for (IConfigurationElement element : elements) {
            if (element.getName().equals("file-format")) {
                String filePattern = element.getAttribute("file-pattern");
                String formatDescription = element.getAttribute("format-description");
                filterPatterns[k] = filePattern;
                filterNames[k] = formatDescription + " (" + filePattern + ")";
                k++;
            }
        }
    }

    /**
	 * @return an array of file extensions for all supported file formats
	 * 			 Each file extension is of the format '*.xxx'.
	 * 			This is the format expected by the FileDialog methods.
	 */
    public static String[] getFilterExtensions() {
        return filterPatterns;
    }

    /**
	 * @return an array of file descriptions for all supported file formats
	 * 			 Each file description is of the format 'description (*.xxx)'.
	 * 			This is the format expected by the FileDialog methods.
	 */
    public static String[] getFilterNames() {
        return filterNames;
    }

    /**
	 * This method returns an array of elements where the file extension matches the given
	 * file extension.  The caller will usually load the class object from one
	 * or more of the elements in an attempt to load or save the data.
	 * <P>
	 * This method returns the IConfigurationElement objects.  It does not
	 * load the classes.  The reason is that the caller may not necessarily
	 * need to load all the classes.  The Eclipse rules say that plug-ins
	 * should only be loaded as needed.
	 * 
	 * @return a collection of IConfigurationElement objects
	 */
    public static IConfigurationElement[] getElements(String fileName) {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        Vector<IConfigurationElement> matchingElements = new Vector<IConfigurationElement>();
        for (IConfigurationElement element : registry.getConfigurationElementsFor("net.sf.jmoney.serializeddatastore.filestores")) {
            if (element.getName().equals("file-format")) {
                String filePattern = element.getAttribute("file-pattern");
                if (filePattern.charAt(0) == '*' && fileName.endsWith(filePattern.substring(1))) {
                    matchingElements.add(element);
                }
            }
        }
        return matchingElements.toArray(new IConfigurationElement[0]);
    }

    /**
	 * This method returns the IFileDatastore implementation class given the id.
	 */
    public static IFileDatastore getFileDatastoreImplementation(String id) {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for (IConfigurationElement element : registry.getConfigurationElementsFor("net.sf.jmoney.serializeddatastore.filestores")) {
            if (element.getName().equals("file-format")) {
                String id2 = element.getNamespaceIdentifier() + '.' + element.getAttribute("id");
                if (id2.equals(id)) {
                    try {
                        return (IFileDatastore) element.createExecutableExtension("class");
                    } catch (CoreException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
	 * Check that we have a current session and that the session
	 * was created by this plug-in.  If not, display an appropriate
	 * message to the user indicating that the user operation
	 * is not available and giving the reasons why the user
	 * operation is not available.
	 * @param window
	 *  
	 * @return true if the current session was created by this
	 * 			plug-in, false if no session is open
	 * 			or if the current session was created by
	 * 			another plug-in that also implements a datastore.
	 */
    public static boolean checkSessionImplementation(IWorkbenchWindow window) {
        DatastoreManager sessionManager = JMoneyPlugin.getDefault().getSessionManager();
        if (sessionManager == null) {
            MessageDialog dialog = new MessageDialog(window.getShell(), "Menu item unavailable", null, "No session is open.", MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
            dialog.open();
            return false;
        } else if (sessionManager instanceof SessionManager) {
            return true;
        } else {
            MessageDialog dialog = new MessageDialog(window.getShell(), "Menu item unavailable", null, "This session cannot be saved using this 'save' action.  " + "More than one plug-in is installed that provides a " + "datastore implementation.  The current session was " + "created using a different plug-in from the plug-in that " + "created this 'save' action.  You can only use this 'save' " + "action if the session was created using the corresponding " + "'new' or 'open' action.", MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
            dialog.open();
            return false;
        }
    }

    /**
	 * Create a new empty session.
	 * 
	 * @return A new SessionManager object.
	 */
    public SessionManager newSession() {
        SessionManager sessionManager = new SessionManager(null, null, null);
        SimpleObjectKey sessionKey = new SimpleObjectKey(sessionManager);
        Session newSession = new Session(sessionKey, null);
        sessionKey.setObject(newSession);
        sessionManager.setSession(newSession);
        return sessionManager;
    }
}
