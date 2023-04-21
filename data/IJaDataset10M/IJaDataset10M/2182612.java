package de.xirp.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import de.xirp.db.DatabaseManager;
import de.xirp.io.comm.CommunicationManager;
import de.xirp.io.comm.data.DatapoolManager;
import de.xirp.io.command.CommandGamepadManager;
import de.xirp.io.command.CommandKeyManager;
import de.xirp.io.command.CommandManager;
import de.xirp.io.gamepad.GamepadManager;
import de.xirp.mail.MailManager;
import de.xirp.plugin.PluginManager;
import de.xirp.profile.ProfileManager;
import de.xirp.settings.PropertiesManager;
import de.xirp.speech.TextToSpeechManager;
import de.xirp.ui.event.ProgressEvent;
import de.xirp.ui.event.ProgressListener;
import de.xirp.ui.util.ApplicationManager;
import de.xirp.ui.util.HelpManager;
import de.xirp.ui.util.HotkeyManager;
import de.xirp.ui.util.MessageManager;
import de.xirp.ui.util.ressource.ColorManager;
import de.xirp.ui.util.ressource.FontManager;
import de.xirp.ui.util.ressource.ImageManager;
import de.xirp.util.Constants;
import de.xirp.util.I18n;
import de.xirp.util.mouse.MouseEventSaver;
import de.xirp.util.serialization.ObjectDeSerializer;
import de.xirp.util.serialization.ObjectSerializer;
import de.xirp.util.serialization.SerializationException;

/**
 * Factory for all managers of the application which implement
 * {@link AbstractManager}. The factory initializes all the managers
 * and stops them if requested. The factory methods should not be used
 * if you don't know what you are doing.
 * 
 * @author Rabea Gransberger
 * @author Matthias Gernand
 */
public final class ManagerFactory {

    /**
	 * The Logger for this class.
	 */
    private static Logger logClass = Logger.getLogger(ManagerFactory.class);

    /**
	 * Map of the managers of this application
	 */
    private static Map<String, AbstractManager> managers = new HashMap<String, AbstractManager>();

    /**
	 * Order in which the managers should be started.
	 */
    private static List<String> managerOrder = new ArrayList<String>();

    /**
	 * Time the managers need for starting
	 */
    private static HashMap<String, Long> startUpTime = new HashMap<String, Long>();

    /**
	 * Percent of the overall time each manager needed for startup
	 */
    private static HashMap<String, Double> percents = new HashMap<String, Double>();

    /**
	 * Flag indicating if the factory is already initialized
	 */
    private static boolean initialized = false;

    /**
	 * File in which the startup times of the managers are saved
	 */
    private static final File startUpFile = new File(Constants.CONF_DIR + File.separator + "ManagerStartUpTimes.dat");

    /**
	 * Instantiates all managers if not done before and loads the file
	 * which holds the percentages for each manager to be used on
	 * startup.
	 */
    private static void init() {
        if (!initialized) {
            try {
                percents = ObjectDeSerializer.<HashMap<String, Double>>getObject(startUpFile);
            } catch (IOException e) {
                logClass.error("Error: " + e.getMessage() + Constants.LINE_SEPARATOR, e);
            } catch (SerializationException e) {
                logClass.error("Error: " + e.getMessage() + Constants.LINE_SEPARATOR, e);
            }
            addManagerInstance(MouseEventSaver.class);
            addManagerInstance(DeleteManager.class);
            addManagerInstance(ProfileManager.class);
            addManagerInstance(PropertiesManager.class);
            addManagerInstance(CommandManager.class);
            addManagerInstance(ApplicationManager.class);
            addManagerInstance(CommandKeyManager.class);
            addManagerInstance(CommandGamepadManager.class);
            addManagerInstance(FontManager.class);
            addManagerInstance(ColorManager.class);
            addManagerInstance(ImageManager.class);
            addManagerInstance(HotkeyManager.class);
            addManagerInstance(PluginManager.class);
            addManagerInstance(CommunicationManager.class);
            addManagerInstance(DatapoolManager.class);
            addManagerInstance(HelpManager.class);
            addManagerInstance(DatabaseManager.class);
            addManagerInstance(MessageManager.class);
            addManagerInstance(ExternalProgramManager.class);
            addManagerInstance(MailManager.class);
            addManagerInstance(PrintManager.class);
            addManagerInstance(TextToSpeechManager.class);
            addManagerInstance(GamepadManager.class);
            initialized = true;
        }
    }

    /**
	 * Creates a new instance of the given manager class and puts it
	 * into the hashmap and order list
	 * 
	 * @param clazz
	 *            the managers class to create a new instance of
	 */
    private static void addManagerInstance(Class<?> clazz) {
        try {
            String key = getKey(clazz);
            managers.put(key, (AbstractManager) clazz.newInstance());
            managerOrder.add(key);
        } catch (InstantiationException e) {
            logClass.error("Error: " + e.getMessage() + Constants.LINE_SEPARATOR, e);
        } catch (IllegalAccessException e) {
            logClass.error("Error " + e.getMessage() + Constants.LINE_SEPARATOR, e);
        }
    }

    /**
	 * Gets the key of the manager class which is used for storing in
	 * the internal map.
	 * 
	 * @param clazz
	 *            the class of the manager
	 * @return the key for the class
	 */
    private static String getKey(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    /**
	 * Starts the manager of the given class and prints any exception
	 * on startup as error to the log.
	 * 
	 * @param manager
	 *            the manager to start
	 * @param pg
	 *            the progress bar which is updated when the manager
	 *            makes progress
	 * @param mesg
	 *            the label to which the progress of the manager is
	 *            written
	 * @param percent
	 *            the overall percentage the managers have for startup
	 */
    private static void start(final AbstractManager manager, final ProgressBar pg, final Label mesg, final double percent) {
        long start = System.nanoTime();
        final String className = manager.getClass().getName();
        final double percentage = getPercent(className) * percent;
        try {
            final int base = (int) (pg.getMaximum() * percentage);
            final int oldSelection = pg.getSelection();
            ProgressListener listener = new ProgressListener() {

                public void progress(final ProgressEvent event) {
                    int add = 0;
                    double percent = event.getPercentage();
                    if (percent != -1) {
                        add = (int) (base * percent);
                    }
                    pg.setSelection(oldSelection + add);
                    mesg.setText(event.getMessage());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        logClass.error("Error: " + e.getMessage() + Constants.LINE_SEPARATOR, e);
                    }
                }
            };
            manager.addProgressListener(listener);
            mesg.setText(manager.getStartMessage());
            try {
                manager.start();
            } catch (Exception e) {
                logClass.error("Error: " + e.getMessage() + Constants.LINE_SEPARATOR, e);
            }
            pg.setSelection(oldSelection + base);
            manager.removeProgressListener(listener);
            manager.deleteProgressListenerList();
        } catch (Exception e) {
            logClass.error(I18n.getString("ManagerFactory.log.couldNotStartManager", (e.getMessage() + Constants.LINE_SEPARATOR), e));
        }
        long end = System.nanoTime();
        long diff = end - start;
        startUpTime.put(className, diff);
    }

    /**
	 * Gets the percents for the given manager which it takes from the
	 * overall percentage to complete it startup
	 * 
	 * @param name
	 *            the fully qualified name of the manager
	 * @return the percentage or <code>0</code> if the manager was
	 *         not found
	 */
    private static double getPercent(final String name) {
        Double perc = percents.get(name);
        if (perc != null) {
            return perc.doubleValue();
        }
        return 0;
    }

    /**
	 * Starts all managers of this factory and updates the progress
	 * bar and label of the splash screen accordingly.
	 * 
	 * @param pg
	 *            the progress bar
	 * @param mesg
	 *            the message label
	 * @param percentage
	 *            the overall percentage for the managers
	 */
    public static void start(final ProgressBar pg, final Label mesg, double percentage) {
        long start = System.nanoTime();
        ManagerFactory.init();
        for (String key : managerOrder) {
            AbstractManager manager = managers.get(key);
            ManagerFactory.start(manager, pg, mesg, percentage);
        }
        long end = System.nanoTime();
        long diff = end - start;
        percents.clear();
        for (Map.Entry<String, Long> entry : startUpTime.entrySet()) {
            double percent = entry.getValue() / (double) diff;
            percents.put(entry.getKey(), percent);
        }
        try {
            ObjectSerializer.<HashMap<String, Double>>writeToDisk(percents, startUpFile);
        } catch (IOException e) {
            logClass.error("Error: " + e.getMessage() + Constants.LINE_SEPARATOR, e);
        }
        Collections.reverse(managerOrder);
        startUpTime.clear();
        percents.clear();
    }

    /**
	 * Stops all registered managers
	 */
    public static void stop() {
        logClass.debug(I18n.getString("ManagerFactory.log.stoppingManagers") + Constants.LINE_SEPARATOR);
        for (String key : managerOrder) {
            AbstractManager manager = managers.get(key);
            try {
                manager.stop();
            } catch (Exception e) {
                logClass.error(I18n.getString("ManagerFactory.log.errorStoppingManager", (e.getMessage() + Constants.LINE_SEPARATOR)), e);
            }
        }
    }
}
