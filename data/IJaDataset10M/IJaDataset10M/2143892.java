package fr.soleil.bensikin.lifecycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.roles.IRightsManager;
import fr.soleil.bensikin.actions.roles.RightsManagerFactory;
import fr.soleil.bensikin.containers.context.ContextDetailPanel;
import fr.soleil.bensikin.data.context.manager.ContextManagerFactory;
import fr.soleil.bensikin.data.snapshot.manager.SnapshotManagerFactory;
import fr.soleil.bensikin.datasources.tango.TangoManagerFactory;
import fr.soleil.bensikin.favorites.Favorites;
import fr.soleil.bensikin.favorites.FavoritesManagerFactory;
import fr.soleil.bensikin.favorites.IFavoritesManager;
import fr.soleil.bensikin.history.History;
import fr.soleil.bensikin.history.manager.HistoryManagerFactory;
import fr.soleil.bensikin.history.manager.IHistoryManager;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.manager.IOptionsManager;
import fr.soleil.bensikin.options.manager.OptionsManagerFactory;
import fr.soleil.bensikin.options.sub.SaveOptions;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.util.component.Splash;

/**
 * The default implementation.
 * 
 * @author CLAISSE
 */
public class DefaultLifeCycleManager implements LifeCycleManager {

    static final Logger logger = LoggerFactory.getLogger(DefaultLifeCycleManager.class);

    private String historyPath;

    private String optionsPath;

    private String favoritesPath;

    boolean hasHistorySave = true;

    public DefaultLifeCycleManager() {
    }

    /**
	 * Called before the GUI graphics containers are instantiated. Loads from
	 * files :
	 * <UL>
	 * <LI>the application's state from the history file
	 * <LI>the application's options
	 * </UL>
	 * And :
	 * <UL>
	 * <LI>initializes the application's ressources
	 * <LI>instantiates static implementations of various managers
	 * </UL>
	 * 
	 * @param startParameters
	 *            Not used
	 */
    @Override
    public void applicationWillStart(final Hashtable startParameters, final Splash splash) {
        startFactories();
        final Locale currentLocale = new Locale("en", "US");
        try {
            Messages.initResourceBundle(currentLocale);
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        loadOptions(splash);
        loadFavorites();
        if (hasHistorySave) {
            loadHistory(splash);
        }
    }

    /**
	 * Loads the application's options.
	 */
    private void loadOptions(final Splash splash) {
        splash.progress(5);
        splash.setMessage("loading options...");
        Bensikin.loadOptions();
        splash.progress(6);
        splash.progress(8);
        splash.setMessage("Loading history options");
        setHasToLoadHistory();
        splash.progress(9);
        splash.setMessage("Loading history options done");
        final String msg = Messages.getLogMessage("APPLICATION_WILL_START_LOAD_OPTIONS_OK");
        logger.debug(msg);
    }

    /**
	 * Sets the load history boolean early so that the history isn't loaded if
	 * it doesn't have to.
	 */
    private void setHasToLoadHistory() {
        final Options options = Options.getOptionsInstance();
        final SaveOptions saveOptions = options.getSaveOptions();
        saveOptions.push();
    }

    /**
	 * Loads the application's favorites.
	 */
    private void loadFavorites() {
        try {
            favoritesPath = Bensikin.getPathToResources() + "/favorites";
            final File f = new File(favoritesPath);
            if (!f.canWrite()) {
                f.mkdir();
            }
            favoritesPath += "/favorites.xml";
            final IFavoritesManager favoritesManager = FavoritesManagerFactory.getCurrentImpl();
            favoritesManager.loadFavorites(favoritesPath);
            final String msg = Messages.getLogMessage("APPLICATION_WILL_START_LOAD_FAVORITES_OK");
            logger.debug(msg);
        } catch (final FileNotFoundException fnfe) {
            final String msg = Messages.getLogMessage("APPLICATION_WILL_START_LOAD_FAVORITES_WARNING");
            logger.warn(msg, fnfe);
            return;
        } catch (final Exception e) {
            final String msg = Messages.getLogMessage("APPLICATION_WILL_START_LOAD_FAVORITES_KO");
            logger.error(msg, e);
            return;
        }
    }

    /**
	 * Instantiates the following managers with the following types:
	 * <UL>
	 * <LI>ILogger (DEFAULT_TYPE)
	 * <LI>ITangoManager (REAL_IMPL_TYPE)
	 * <LI>ISnapManager (REAL_IMPL_TYPE)
	 * <LI>IHistoryManager (XML_IMPL_TYPE)
	 * <LI>IOptionsManager (XML_IMPL_TYPE)
	 * <LI>IFavoritesManager (XML_IMPL_TYPE)
	 * <LI>IContextManager (XML_IMPL_TYPE)
	 * <LI>ISnapshotManager (XML_IMPL_TYPE)
	 * </UL>
	 */
    private void startFactories() {
        TangoManagerFactory.getImpl(TangoManagerFactory.REAL_IMPL_TYPE);
        HistoryManagerFactory.getImpl(HistoryManagerFactory.XML_IMPL_TYPE);
        OptionsManagerFactory.getImpl(OptionsManagerFactory.XML_IMPL_TYPE);
        FavoritesManagerFactory.getImpl(FavoritesManagerFactory.XML_IMPL_TYPE);
        ContextManagerFactory.getImpl(ContextManagerFactory.XML_IMPL_TYPE);
        SnapshotManagerFactory.getImpl(SnapshotManagerFactory.XML_IMPL_TYPE);
        RightsManagerFactory.getImpl(RightsManagerFactory.SNAPSHOTS_ONLY_OPERATOR);
    }

    /**
	 * Loads the application's history.
	 */
    private void loadHistory(final Splash splash) {
        try {
            splash.progress(10);
            splash.setMessage("preparing history");
            historyPath = Bensikin.getPathToResources() + "/history";
            final File f = new File(historyPath);
            if (!f.canWrite()) {
                f.mkdir();
            }
            historyPath += "/history.xml";
            splash.progress(11);
            splash.setMessage("initializing history manager");
            final IHistoryManager historyManager = HistoryManagerFactory.getCurrentImpl();
            splash.progress(12);
            splash.setMessage("loading history...");
            final History history = historyManager.loadHistory(historyPath);
            splash.progress(13);
            splash.setMessage("applying history");
            History.setHistory(history);
            splash.progress(14);
            history.setOpenedAndSelectedEntities();
            splash.progress(15);
            splash.setMessage("history fully loaded");
            final String msg = Messages.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY_OK");
            logger.debug(msg);
        } catch (final FileNotFoundException fnfe) {
            final String msg = Messages.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY_WARNING");
            splash.progress(15);
            splash.setMessage(msg);
            logger.warn(msg, fnfe);
            return;
        } catch (final Exception e) {
            final String msg = Messages.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY_KO");
            splash.progress(15);
            splash.setMessage(msg);
            logger.error(msg, e);
            return;
        }
    }

    /**
	 * Called just after the GUI graphics containers are instantiated. Is used
	 * for operations that need the containers to already exist: -pushing the
	 * pre loaded history and options to the display components -setting the
	 * window size
	 * 
	 * @param startParameters
	 *            Not used
	 */
    @Override
    public void applicationStarted(final Hashtable startParameters) {
        final Options options = Options.getInstance();
        try {
            options.push();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final History history = History.getHistory();
        try {
            history.push();
        } catch (final Exception e) {
        }
        final IRightsManager rightsManager = RightsManagerFactory.getCurrentImpl();
        if (Bensikin.isRestricted()) {
            rightsManager.disableUselessFields();
        } else {
            ContextDetailPanel.getInstance().getAttributeTableSelectionBean().start();
        }
    }

    /**
	 * Called when the application detects a shutdown request, be it through the
	 * close icon or through the menu's Exit option. Is used to: -save
	 * everything that has to be saved -close resources And finally, shutdowns
	 * the application.
	 * 
	 * @param endParameters
	 *            Not used
	 */
    @Override
    public void applicationClosed(final Hashtable endParameters) {
        try {
            System.out.println("Bensikin will close !");
            saveOptions();
            saveFavorites();
            if (hasHistorySave) {
                saveHistory();
            }
            System.out.println("Bensikin closed");
            System.exit(0);
        } catch (final Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
	 * Saves the application's favorites.
	 * 
	 * @throws Exception
	 */
    private void saveFavorites() throws Exception {
        try {
            final Favorites favoritesToSave = Favorites.getInstance();
            final IFavoritesManager favoritesManager = FavoritesManagerFactory.getCurrentImpl();
            favoritesManager.saveFavorites(favoritesToSave, favoritesPath);
            final String msg = Messages.getLogMessage("APPLICATION_WILL_STOP_SAVE_FAVORITES_OK");
            logger.debug(msg);
        } catch (final Exception e) {
            final String msg = Messages.getLogMessage("APPLICATION_WILL_STOP_SAVE_FAVORITES_KO");
            logger.error(msg, e);
            return;
        }
    }

    /**
	 * Saves the application's options.
	 * 
	 * @throws Exception
	 */
    private void saveOptions() throws Exception {
        try {
            final Options optionsToSave = Options.getInstance();
            final IOptionsManager optionsManager = OptionsManagerFactory.getCurrentImpl();
            optionsManager.saveOptions(optionsToSave, optionsPath);
            final String msg = Messages.getLogMessage("APPLICATION_WILL_STOP_SAVE_OPTIONS_OK");
            logger.debug(msg);
        } catch (final Exception e) {
            final String msg = Messages.getLogMessage("APPLICATION_WILL_STOP_SAVE_OPTIONS_KO");
            logger.error(msg, e);
            return;
        }
    }

    /**
	 * Saves the application's history.
	 * 
	 * @throws Exception
	 */
    private void saveHistory() throws Exception {
        try {
            final History historyToSave = History.getCurrentHistory();
            final IHistoryManager historyManager = HistoryManagerFactory.getCurrentImpl();
            historyManager.saveHistory(historyToSave, historyPath);
            final String msg = Messages.getLogMessage("APPLICATION_WILL_STOP_SAVE_HISTORY_OK");
            logger.debug(msg);
        } catch (final Exception e) {
            final String msg = Messages.getLogMessage("APPLICATION_WILL_STOP_SAVE_HISTORY_KO");
            logger.error(msg, e);
            return;
        }
    }

    @Override
    public void setHasHistorySave(final boolean b) {
        hasHistorySave = b;
    }

    @Override
    public boolean hasHistorySave() {
        return hasHistorySave;
    }
}
