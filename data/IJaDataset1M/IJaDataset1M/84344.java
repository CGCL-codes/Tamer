package org.jdeluxe;

import java.io.File;
import java.util.Locale;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jdeluxe.config.Icons;
import org.jdeluxe.db.DBInitializer;
import org.jdeluxe.db.DBManager;
import org.jdeluxe.file.PluginFileManager;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

    /** The d. */
    private Debug d = new Debug();

    /** The plugin. */
    private static Activator plugin;

    /** The dbm. */
    private DBManager dbm;

    /** The locale. */
    private Locale locale;

    /**
	 * The constructor.
	 */
    public Activator() {
        plugin = this;
        locale = Locale.getDefault();
    }

    /**
	 * Initialize all icons and graphics in the plugins ImageRegistry.
	 *
	 * @param registry the registry
	 */
    protected void initializeImageRegistry(ImageRegistry registry) {
        Icons.initImages(registry);
    }

    /**
	 * (non-Javadoc).
	 *
	 * @param context the context
	 *
	 * @throws Exception the exception
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        d.out("Statelocation = " + this.getStateLocation());
        String pluginPath = this.getStateLocation().toOSString();
        String dbPath = pluginPath + File.separator + "xmldb";
        d.out("DBPath = " + dbPath);
        if (!new File(dbPath).exists()) {
            new File(dbPath).mkdir();
        }
        DBInitializer dbi = new DBInitializer(dbPath, "conf.xml", "catalog.xml", "data", "admin", "");
        dbi.initXmlDb(true);
        dbm = DBManager.getInstance();
        dbm.openDB(dbPath);
        dbm.initCollections(false);
        dbm.initI18NConfig(true);
        dbm.initQueries(false);
        PluginFileManager pfm = PluginFileManager.getInstance();
        pfm.init(pluginPath);
    }

    /**
	 * (non-Javadoc).
	 *
	 * @param context the context
	 *
	 * @throws Exception the exception
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        dbm.closeDB();
        super.stop(context);
    }

    /**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 *
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(Constants.ID_PLUGIN, path);
    }

    /**
	 * Gets the DB manager.
	 *
	 * @return the DB manager
	 */
    public DBManager getDBManager() {
        return dbm;
    }

    /**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
    public Locale getLocale() {
        return locale;
    }

    /**
	 * Activate exist logger.
	 */
    public void activateExistLogger() {
        Logger logger = Logger.getRootLogger();
        try {
            SimpleLayout layout = new SimpleLayout();
            ConsoleAppender consoleAppender = new ConsoleAppender(layout);
            logger.addAppender(consoleAppender);
            logger.setLevel(Level.ALL);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
