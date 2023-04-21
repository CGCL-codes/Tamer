package freeguide.plugins.program.freeguide.viewer;

import freeguide.common.base.PluginInfo;
import freeguide.common.lib.fgspecific.Application;
import freeguide.common.lib.fgspecific.data.TVChannelsSet;
import freeguide.common.lib.fgspecific.data.TVData;
import freeguide.common.lib.general.Utils;
import freeguide.common.plugininterfaces.BaseModule;
import freeguide.common.plugininterfaces.FGLogger;
import freeguide.common.plugininterfaces.IApplication;
import freeguide.common.plugininterfaces.IExecutionController;
import freeguide.common.plugininterfaces.IModuleExport;
import freeguide.common.plugininterfaces.IModuleGrabber;
import freeguide.common.plugininterfaces.IModuleImport;
import freeguide.common.plugininterfaces.IModuleReminder;
import freeguide.common.plugininterfaces.IModuleStorage;
import freeguide.common.plugininterfaces.IModuleViewer;
import freeguide.plugins.program.freeguide.FreeGuide;
import freeguide.plugins.program.freeguide.dialogs.ChannelSetListDialog;
import freeguide.plugins.program.freeguide.lib.fgspecific.ExecutionController;
import freeguide.plugins.program.freeguide.lib.fgspecific.GrabberCommandRunner;
import freeguide.plugins.program.freeguide.lib.fgspecific.PluginsManager;
import freeguide.plugins.program.freeguide.lib.fgspecific.StoragePipe;
import freeguide.plugins.program.freeguide.lib.fgspecific.VersionCheckerThread;
import freeguide.plugins.program.freeguide.lib.general.LookAndFeelManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main window of application.
 *
 * @author Alex Buloichik (alex73 at zaval.org)
 */
public class MainController extends BaseModule implements IApplication {

    /** DOCUMENT ME! */
    public static final Config config = new Config();

    /** DOCUMENT ME! */
    public MainFrame mainFrame;

    /** DOCUMENT ME! */
    public IModuleViewer viewer;

    protected ExecutionController executionController = new ExecutionController();

    protected IModuleReminder[] reminders;

    protected JFrame applicationFrame;

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public Object getConfig() {
        return config;
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public IMainMenu getMainMenu() {
        return mainFrame.getMainMenuForExport();
    }

    /**
     * DOCUMENT_ME!
     */
    public void saveAllConfigs() {
        PluginsManager.saveAllConfigs();
    }

    /**
     * DOCUMENT_ME!
     *
     * @throws Exception DOCUMENT_ME!
     */
    public void reloadResourceBundle() throws Exception {
        i18n = ResourceBundle.getBundle("resources/i18n/MessagesBundle");
    }

    /**
     * DOCUMENT_ME!
     *
     * @param key DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public String getLocalizedMessage(String key) {
        String ans;
        try {
            ans = i18n.getString(key);
        } catch (java.util.MissingResourceException e) {
            FreeGuide.log.log(Level.WARNING, "Unable to find translatable string for key '" + key + "'.");
            ans = key;
        }
        return ans;
    }

    /**
     * DOCUMENT_ME!
     *
     * @param key DOCUMENT_ME!
     * @param params DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public String getLocalizedMessage(String key, Object[] params) {
        return MessageFormat.format(getLocalizedMessage(key), params);
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public IModuleReminder[] getReminders() {
        if (reminders == null) {
            PluginInfo[] infos = PluginsManager.getReminders();
            reminders = new IModuleReminder[infos.length];
            for (int i = 0; i < reminders.length; i++) {
                reminders[i] = (IModuleReminder) infos[i].getInstance();
            }
        }
        return reminders;
    }

    /**
     * DOCUMENT_ME!
     *
     * @param viewer DOCUMENT_ME!
     * @param grabberFromWizard DOCUMENT ME!
     */
    public void start(final IModuleViewer viewer, final String grabberFromWizard) {
        applicationFrame = FreeGuide.getPleaseWaitFrame();
        this.viewer = viewer;
        mainFrame = new MainFrame();
        mainFrame.setTitle("FreeGuide " + Application.VERSION.getDotFormat());
        setLookAndFeel();
        new MenuHandler(this);
        mainFrame.getContentPane().add(viewer.getPanel(), BorderLayout.CENTER);
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                saveConfigNow();
                stopModules();
                System.exit(0);
            }
        });
        mainFrame.getForegroundButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doShowGrabbers();
            }
        });
        if (grabberFromWizard != null) {
            config.getActiveGrabberIDs().add(grabberFromWizard);
        }
        mainFrame.pack();
        mainFrame.setBounds(config.ui.mainWindowPosition);
        startModules();
        mainFrame.getRootPane().setDefaultButton(viewer.getDefaultButton());
        mainFrame.setVisible(true);
        FreeGuide.hidePleaseWait();
        applicationFrame = mainFrame;
        if (!"no".equals(FreeGuide.config.privacyInfo)) {
            new VersionCheckerThread(getApplicationFrame()).start();
        }
        mainFrame.waitForClose();
        stopModules();
    }

    /**
     * Set a viewer
     *
     * @param viewerId DOCUMENT ME!
     */
    public void setViewer(String viewerId) {
        if (((MainController.Config) this.getConfig()).viewerId.equals(viewerId)) {
            return;
        }
        if (this.viewer != null) {
            mainFrame.getContentPane().remove(this.viewer.getPanel());
            this.viewer.close();
        }
        ((MainController.Config) this.getConfig()).viewerId = viewerId;
        this.viewer = (IModuleViewer) PluginsManager.getModuleByID(viewerId);
        mainFrame.getContentPane().add(this.viewer.getPanel(), BorderLayout.CENTER);
        this.viewer.open();
        this.viewer.getPanel().updateUI();
    }

    /**
     * Starts up all grabbers and reminders via their start() method.
     */
    protected void startModules() {
        viewer.open();
        final PluginInfo[] grabbers = PluginsManager.getGrabbers();
        for (int i = 0; i < grabbers.length; i++) {
            IModuleGrabber grabber = (IModuleGrabber) grabbers[i].getInstance();
            grabber.start();
        }
        final PluginInfo[] reminders = PluginsManager.getReminders();
        for (int i = 0; i < reminders.length; i++) {
            IModuleReminder reminder = (IModuleReminder) reminders[i].getInstance();
            reminder.addItemsToMenu(mainFrame.getMenuTools());
            reminder.start();
        }
    }

    /**
     * Shuts down all running reminders and grabbers via their stop()
     * method. The viewer is closed after all, too. It's called before
     * FreeGuide is closed.
     */
    protected void stopModules() {
        final PluginInfo[] reminders = PluginsManager.getReminders();
        for (int i = 0; i < reminders.length; i++) {
            IModuleReminder reminder = (IModuleReminder) reminders[i].getInstance();
            reminder.stop();
            reminder.cleanup(System.currentTimeMillis());
        }
        final PluginInfo[] grabbers = PluginsManager.getGrabbers();
        for (int i = 0; i < grabbers.length; i++) {
            IModuleGrabber grabber = (IModuleGrabber) grabbers[i].getInstance();
            grabber.stop();
        }
        final PluginInfo[] storages = PluginsManager.getStorages();
        for (int i = 0; i < storages.length; i++) {
            IModuleStorage storage = (IModuleStorage) storages[i].getInstance();
            storage.cleanup();
        }
        viewer.close();
    }

    /**
     * DOCUMENT_ME!
     */
    public static void remindersReschedule() {
        final PluginInfo[] reminders = PluginsManager.getReminders();
        for (int i = 0; i < reminders.length; i++) {
            ((IModuleReminder) reminders[i].getInstance()).reschedule();
        }
    }

    /**
     * DOCUMENT_ME!
     */
    public void saveConfigNow() {
        config.ui.mainWindowPosition = mainFrame.getBounds();
        super.saveConfigNow();
    }

    /**
     * Set L&F as described in config.
     */
    public void setLookAndFeel() {
        final String inspectedLFClassName;
        final String currentLAFClassName;
        if (config.ui.LFname == null) {
            inspectedLFClassName = UIManager.getSystemLookAndFeelClassName();
        } else {
            inspectedLFClassName = LookAndFeelManager.getLookAndFeelClassName(config.ui.LFname);
        }
        final LookAndFeel currentLAF = UIManager.getLookAndFeel();
        if (currentLAF != null) {
            currentLAFClassName = currentLAF.getClass().getName();
        } else {
            currentLAFClassName = null;
        }
        if ((inspectedLFClassName != null) && !inspectedLFClassName.equals(currentLAFClassName)) {
            try {
                UIManager.setLookAndFeel(inspectedLFClassName);
                if (mainFrame != null) {
                    SwingUtilities.updateComponentTreeUI(mainFrame);
                }
            } catch (Exception ex) {
                FreeGuide.log.log(Level.WARNING, "Error setup L&F to " + inspectedLFClassName, ex);
            }
        }
    }

    /**
     * DOCUMENT_ME!
     */
    public void doEditChannelsSets() {
        ChannelSetListDialog dialog = new ChannelSetListDialog(mainFrame, getDataStorage().getInfo().channelsList, config.channelsSetsList);
        Utils.centreDialog(mainFrame, dialog);
        boolean updated = dialog.showDialog();
        if (updated) {
            config.channelsSetsList = dialog.getChannelsSets();
            viewer.onChannelsSetsChanged();
        }
    }

    /**
     * Activates the grabber controller. That basically shows the
     * grabber dialog if grabbing is runnning, or starts the grabbing process
     */
    public void doStartGrabbers() {
        executionController.activate(this, new GrabberCommandRunner(), true);
    }

    /**
     * Activates the grabber controller.
     *
     * @see this.doStartGrabbers()
     */
    public void doShowGrabbers() {
        executionController.activate(this, new GrabberCommandRunner(), true);
    }

    /**
     * DOCUMENT_ME!
     *
     * @param exp DOCUMENT_ME!
     */
    public void exportTo(final IModuleExport exp) {
        new Thread() {

            public void run() {
                IModuleStorage.Info info = getDataStorage().getInfo().cloneInfo();
                try {
                    info.channelsList = null;
                    TVData data = getDataStorage().get(info);
                    exp.exportData(data, mainFrame);
                } catch (Exception ex) {
                    FreeGuide.log.log(Level.WARNING, "Error export data", ex);
                }
            }
        }.start();
    }

    /**
     * DOCUMENT_ME!
     *
     * @param imp DOCUMENT_ME!
     */
    public void importFrom(final IModuleImport imp) {
        new Thread() {

            public void run() {
                final StoragePipe pipe = new StoragePipe();
                imp.importDataUI(mainFrame, pipe, new FGLogger(FreeGuide.log));
                pipe.finish();
                viewer.onDataChanged();
            }
        }.start();
    }

    /**
     * DOCUMENT_ME!
     */
    public void doPrint() {
        viewer.printHTML();
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public JFrame getApplicationFrame() {
        return mainFrame;
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public JFrame getCurrentFrame() {
        return applicationFrame;
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public List getChannelsSetsList() {
        return config.channelsSetsList;
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public IModuleStorage getDataStorage() {
        return FreeGuide.storage;
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public IModuleViewer getViewer() {
        return viewer;
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public boolean isUnix() {
        return FreeGuide.runtimeInfo.isUnix;
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public TimeZone getTimeZone() {
        return FreeGuide.getTimeZone();
    }

    /**
     * DOCUMENT_ME!
     *
     * @deprecated use IBrowserLauncher instead
     * @return DOCUMENT_ME!
     */
    public String getBrowserCommand() {
        return FreeGuide.config.browserCommand;
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public String getInstallDirectory() {
        return FreeGuide.runtimeInfo.installDirectory;
    }

    public String getLibDirectory() {
        File ret = new File(getInstallDirectory(), "lib");
        if (!ret.isDirectory()) {
            ret = new File(getInstallDirectory(), "../build/package/lib");
        }
        if (!ret.isDirectory()) {
            ret = new File(getInstallDirectory(), "../lib");
        }
        return ret.toString();
    }

    public String getDocsDirectory() {
        return FreeGuide.runtimeInfo.docDirectory;
    }

    public Logger getLogger() {
        return FreeGuide.log;
    }

    public FGLogger getFGLogger() {
        return new FGLogger(FreeGuide.log);
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public String getWorkingDirectory() {
        return FreeGuide.config.workingDirectory;
    }

    /**
     * DOCUMENT_ME!
     */
    public void restart() {
        mainFrame.dispose();
    }

    /**
     * DOCUMENT_ME!
     *
     * @return DOCUMENT_ME!
     */
    public Locale[] getSupportedLocales() {
        final List result = new ArrayList();
        Locale[] all = Locale.getAvailableLocales();
        for (int i = 0; i < all.length; i++) {
            if (ResourceBundle.getBundle("resources/i18n/MessagesBundle", all[i]).getLocale().equals(all[i])) {
                result.add(all[i]);
            }
        }
        return (Locale[]) result.toArray(new Locale[result.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision$
     */
    public static class Config {

        /** The list of available channel sets (all are ChannelSet objects) */
        public static final Class channelsSetsList_TYPE = TVChannelsSet.class;

        /** DOCUMENT ME! */
        public static final Class activeGrabberIDs_TYPE = String.class;

        /** DOCUMENT ME! */
        public UI ui = new UI();

        /** DOCUMENT ME! */
        public List channelsSetsList = new ArrayList();

        /** DOCUMENT ME! */
        private Set activeGrabberIDs = new TreeSet();

        public Set getActiveGrabberIDs() {
            activeGrabberIDs.add("grabber-xmltv");
            return activeGrabberIDs;
        }

        public void setActiveGrabberIDs(TreeSet iActiveGrabberIDs) {
            activeGrabberIDs = iActiveGrabberIDs;
        }

        /** The default selected viewer */
        public String viewerId = FreeGuide.VIEWER_ID;

        /**
         * DOCUMENT ME!
         *
         * @author $author$
         * @version $Revision$
         */
        public static class UI {

            /** DOCUMENT ME! */
            public Rectangle mainWindowPosition;

            /** DOCUMENT ME! */
            public String LFname;

            /**
             * Creates a new UI object.
             */
            public UI() {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                mainWindowPosition = new Rectangle(640, 400);
                mainWindowPosition.setLocation((screenSize.width - mainWindowPosition.width) / 2, (screenSize.height - mainWindowPosition.height) / 2);
            }
        }
    }

    public IExecutionController getExecutionController() {
        return executionController;
    }

    public JButton getApplicationForegroundButton() {
        return mainFrame.getForegroundButton();
    }

    public JProgressBar getApplicationProgressBar() {
        return mainFrame.getProgressBar();
    }
}
