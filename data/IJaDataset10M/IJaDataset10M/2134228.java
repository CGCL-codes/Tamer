package net.sf.jhylafax;

import gnu.hylafax.HylaFAXClient;
import gnu.inet.ftp.ConnectionEvent;
import gnu.inet.ftp.ConnectionListener;
import gnu.inet.ftp.ServerResponseException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicBorders;
import net.sf.jhylafax.JobHelper.StatusResponse;
import net.sf.jhylafax.addressbook.AddressBook;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.xnap.commons.gui.AboutDialog;
import org.xnap.commons.gui.Builder;
import org.xnap.commons.gui.Dialogs;
import org.xnap.commons.gui.ErrorDialog;
import org.xnap.commons.gui.action.AbstractXNapAction;
import org.xnap.commons.gui.factory.DefaultFactory;
import org.xnap.commons.gui.shortcut.ActionShortcut;
import org.xnap.commons.gui.util.GUIHelper;
import org.xnap.commons.gui.util.IconHelper;
import org.xnap.commons.gui.util.LabelUpdater;
import org.xnap.commons.gui.util.WhatsThisAction;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import org.xnap.commons.i18n.I18nManager;
import org.xnap.commons.io.Job;
import org.xnap.commons.io.ProgressMonitor;
import org.xnap.commons.io.UserAbortException;
import org.xnap.commons.settings.SettingStore;
import org.xnap.commons.util.FileHelper;

/**
 * The main class, that provides the main window as well as the network connection.
 * 
 * @author Steffen Pingel
 */
@SuppressWarnings("serial")
public class JHylaFAX extends JFrame implements LocaleChangeListener {

    public static final I18n i18n = I18nFactory.getI18n(JHylaFAX.class);

    public static final String JDK14_LOG_CONFIG_KEY = "java.util.logging.config.file";

    public static final Locale[] SUPPORTED_LOCALES = { Locale.ENGLISH, new Locale("ca"), new Locale("cs"), new Locale("de"), new Locale("es"), new Locale("fr"), new Locale("it"), new Locale("nl"), new Locale("pl"), new Locale("pt", "BR"), new Locale("ru"), new Locale("sv"), new Locale("tr") };

    private static final Log logger = LogFactory.getLog(JHylaFAX.class);

    private static final int BUFFER_SIZE = 1024 * 512;

    private static JHylaFAX app;

    private static String version;

    private JTabbedPane mainTabbedPane;

    private JMenu fileMenu;

    private SettingsDialogAction settingsDialogAction;

    private JPanel statusBarPanel;

    private JLabel statusLabel;

    private JLabel serverInfoLabel;

    private ExitAction exitAction;

    private UpdateStatusAction updateStatusAction;

    private HylaFAXClient connection;

    private JToolBar mainToolBar;

    private SendAction sendAction;

    private PollAction pollAction;

    private JMenu helpMenu;

    private AboutAction aboutAction;

    private String password;

    private String adminPassword;

    private ConnectionHandler connectionHandler = new ConnectionHandler();

    private ActionShortcut sendActionShortcut;

    private AddressBook addressBook;

    private AddressBookAction addressBookAction;

    private ActionShortcut updateStatusActionShortcut;

    private ReceiveQueuePanel recvqPanel;

    private JobQueuePanel sendqPanel;

    private JobQueuePanel pollqPanel;

    private JobQueuePanel doneqPanel;

    private DocumentQueuePanel docqPanel;

    private SettingsWizardAction settingsWizardAction;

    private JobQueue jobQueue = new JobQueue();

    private NotificationTimer notificationTimer = new NotificationTimer();

    private File addressBookFile;

    private Object connectionLock = new Object();

    private FaxTray tray;

    public JHylaFAX() {
        app = this;
        try {
            Settings.load(getSettingsFile());
            Settings.DEFAULT_COMPLETION_MODE.updateMode();
        } catch (FileNotFoundException e) {
            logger.debug("Error loading settings", e);
        } catch (IOException e) {
            logger.debug("Error loading settings", e);
            ErrorDialog.showError(this, i18n.tr("Could not load settings"), i18n.tr("JHylaFAX Error"), e);
        }
        updateResourceBundle();
        I18nManager.getInstance().addLocaleChangeListener(new LabelUpdater());
        initializeToolkit();
        initialize();
        restoreLayout();
        PropertyChangeListener disconnector = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (connection != null) {
                    try {
                        connection.quit();
                    } catch (Exception e) {
                        logger.debug("Error closing connection", e);
                    }
                    connection = null;
                }
            }
        };
        Settings.HOSTNAME.addPropertyChangeListener(disconnector);
        Settings.PORT.addPropertyChangeListener(disconnector);
        Settings.USE_PASSIVE.addPropertyChangeListener(disconnector);
        Settings.USERNAME.addPropertyChangeListener(disconnector);
        Settings.LOCALE.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateResourceBundle();
            }
        });
        notificationTimer.settingsUpdated();
    }

    private void restoreLayout() {
        SettingStore store = new SettingStore(Settings.backstore);
        store.restoreWindow("window.main", 40, 40, 540, 400, this);
        recvqPanel.restoreLayout(store, new String[] { "sender", "pages", "time", "filename", "filesize", "error" });
        sendqPanel.restoreLayout(store, new String[] { "id", "priority", "sender", "number", "dials", "pages", "error", "state" });
        pollqPanel.restoreLayout(store, new String[] { "id", "priority", "sender", "number", "dials", "pages", "error", "state" });
        doneqPanel.restoreLayout(store, new String[] { "id", "priority", "sender", "number", "dials", "pages", "error", "state" });
        docqPanel.restoreLayout(store, new String[] { "permissions", "owner", "modified", "filename", "filesize", "time" });
    }

    private void saveLayout() {
        SettingStore store = new SettingStore(Settings.backstore);
        store.saveWindow("window.main", this);
        recvqPanel.saveLayout(store);
        sendqPanel.saveLayout(store);
        pollqPanel.saveLayout(store);
        doneqPanel.saveLayout(store);
        docqPanel.saveLayout(store);
    }

    private void updateResourceBundle() {
        try {
            I18nManager.getInstance().setDefaultLocale(Settings.LOCALE.getValue());
        } catch (MissingResourceException e) {
            logger.warn("Error loading resource bundle", e);
        }
    }

    public static void initializeToolkit() {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        UIManager.put("SplitPaneDivider.border", new BasicBorders.MarginBorder());
        UIManager.put("TitledBorder.font", UIManager.getFont("Label.font").deriveFont(Font.BOLD));
        UIManager.put("TitledBorder.titleColor", UIManager.getColor("Label.foreground").brighter());
        Builder.setProperty(DefaultFactory.ENHANCED_TEXT_FIELD_MENU_KEY, true);
    }

    private void initialize() {
        initializeIcon();
        initializeActions();
        initializeShortCuts();
        initializeMenuBar();
        initializeContent();
        initializeToolBar();
        initializeStatusBar();
        initializeSystemTray();
        updateLabels();
        updateServerInfo();
    }

    private void initializeIcon() {
        List<? extends Image> images = IconHelper.getApplicationIcons("kdeprintfax.png");
        if (images != null) {
            setIconImages(images);
        }
    }

    private void initializeActions() {
        updateStatusAction = new UpdateStatusAction();
        settingsDialogAction = new SettingsDialogAction();
        settingsWizardAction = new SettingsWizardAction();
        addressBookAction = new AddressBookAction();
        exitAction = new ExitAction();
        sendAction = new SendAction();
        pollAction = new PollAction();
        aboutAction = new AboutAction();
    }

    private void initializeShortCuts() {
        sendActionShortcut = new ActionShortcut(sendAction);
        sendActionShortcut.setKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        updateStatusActionShortcut = new ActionShortcut(updateStatusAction);
        updateStatusActionShortcut.setKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
    }

    private void initializeContent() {
        setTitle("JHylaFAX");
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                exit();
            }
        });
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setBorder(GUIHelper.createEmptyBorder(10));
        getContentPane().add(mainTabbedPane, BorderLayout.CENTER);
        recvqPanel = new ReceiveQueuePanel("recvq");
        mainTabbedPane.addTab("", IconHelper.getTabTitleIcon("folder_inbox.png"), recvqPanel);
        sendqPanel = new JobQueuePanel("sendq");
        mainTabbedPane.addTab("", IconHelper.getTabTitleIcon("folder_outbox.png"), sendqPanel);
        doneqPanel = new JobQueuePanel("doneq");
        mainTabbedPane.addTab("", IconHelper.getTabTitleIcon("folder_sent_mail.png"), doneqPanel);
        pollqPanel = new JobQueuePanel("pollq");
        if (Settings.SHOW_POLLQ.getValue()) {
            mainTabbedPane.addTab("", IconHelper.getTabTitleIcon("folder_print.png"), pollqPanel);
        }
        docqPanel = new DocumentQueuePanel("docq");
        mainTabbedPane.addTab("", IconHelper.getTabTitleIcon("folder_txt.png"), docqPanel);
    }

    private void initializeSystemTray() {
        tray = new FaxTray();
        if (tray.isSupported()) {
            tray.getPopupMenu().add(new MenuItem((String) exitAction.getValue(Action.NAME)));
        }
    }

    public void exit() {
        if (addressBook != null) {
            try {
                addressBook.saveLayout(new SettingStore(Settings.backstore));
                addressBook.store(addressBookFile);
            } catch (Exception e) {
                logger.debug("Error storing addressbook", e);
                ErrorDialog.showError(JHylaFAX.this, i18n.tr("Could not store addressbook"), i18n.tr("JHylaFAX Error"), e);
            }
        }
        saveLayout();
        try {
            Settings.store(getSettingsFile());
        } catch (IOException e) {
            logger.debug("Error storing settings", e);
            ErrorDialog.showError(JHylaFAX.this, i18n.tr("Could not store settings"), i18n.tr("JHylaFAX Error"), e);
        }
        System.exit(0);
    }

    private void initializeToolBar() {
        mainToolBar = new JToolBar();
        getContentPane().add(mainToolBar, BorderLayout.NORTH);
        mainToolBar.add(Builder.createToolBarButton(sendAction));
        mainToolBar.addSeparator();
        mainToolBar.add(Builder.createToolBarButton(updateStatusAction));
        mainToolBar.addSeparator();
        mainToolBar.add(Builder.createToolBarButton(addressBookAction));
        mainToolBar.addSeparator();
        mainToolBar.add(Builder.createToolBarButton(settingsDialogAction));
    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        fileMenu = new JMenu();
        menuBar.add(fileMenu);
        JComponent item = Builder.createMenuItem(sendAction);
        sendActionShortcut.setMenuItem((JMenuItem) item);
        fileMenu.add(item);
        fileMenu.add(Builder.createMenuItem(pollAction));
        fileMenu.addSeparator();
        item = Builder.createMenuItem(updateStatusAction);
        fileMenu.add(item);
        updateStatusActionShortcut.setMenuItem((JMenuItem) item);
        fileMenu.addSeparator();
        fileMenu.add(Builder.createMenuItem(settingsDialogAction));
        fileMenu.add(Builder.createMenuItem(settingsWizardAction));
        fileMenu.addSeparator();
        fileMenu.add(Builder.createMenuItem(addressBookAction));
        fileMenu.addSeparator();
        fileMenu.add(Builder.createMenuItem(exitAction));
        helpMenu = new JMenu();
        menuBar.add(helpMenu);
        helpMenu.add(Builder.createMenuItem(new WhatsThisAction()));
        helpMenu.addSeparator();
        helpMenu.add(Builder.createMenuItem(aboutAction));
    }

    private void initializeStatusBar() {
        statusBarPanel = new JPanel(new BorderLayout());
        getContentPane().add(statusBarPanel, BorderLayout.SOUTH);
        statusLabel = new JLabel();
        statusBarPanel.add(statusLabel, BorderLayout.CENTER);
        serverInfoLabel = new JLabel();
        statusBarPanel.add(serverInfoLabel, BorderLayout.EAST);
    }

    public static JHylaFAX getInstance() {
        return app;
    }

    public HylaFAXClient getConnection(ProgressMonitor monitor) throws IOException, ServerResponseException {
        synchronized (connectionLock) {
            monitor.setText(i18n.tr("Connecting to {0}", Settings.HOSTNAME.getValue() + ":" + Settings.PORT.getValue()));
            if (connection == null) {
                connection = new HylaFAXClient();
                connection.addConnectionListener(connectionHandler);
                connection.setPassive(Settings.USE_PASSIVE.getValue());
            }
            logger.info("Connecting to HylaFAX server at " + Settings.HOSTNAME.getValue() + ":" + Settings.PORT.getValue());
            connection.open(Settings.HOSTNAME.getValue(), Settings.PORT.getValue());
            if (password == null) {
                password = Settings.PASSWORD.getValue();
            }
            while (true) {
                if (connection.user(Settings.USERNAME.getValue())) {
                    if (password.length() == 0) {
                        password = requestPassword(monitor, false);
                        if (password == null) {
                            throw new UserAbortException();
                        }
                    }
                    try {
                        connection.pass(password);
                    } catch (ServerResponseException e) {
                        password = "";
                        continue;
                    }
                }
                break;
            }
            if (adminPassword == null) {
                adminPassword = Settings.ADMIN_PASSWORD.getValue();
            }
            if (Settings.ADMIN_MODE.getValue()) {
                while (true) {
                    if (adminPassword.length() == 0) {
                        adminPassword = requestPassword(monitor, true);
                        if (adminPassword == null) {
                            break;
                        }
                    }
                    try {
                        connection.admin(adminPassword);
                    } catch (ServerResponseException e) {
                        adminPassword = "";
                        continue;
                    }
                    break;
                }
            }
            return connection;
        }
    }

    private String requestPassword(final ProgressMonitor monitor, final boolean admin) throws ServerResponseException {
        final String[] password = new String[1];
        Runnable runner = new Runnable() {

            public void run() {
                String host = Settings.USERNAME.getValue() + "@" + Settings.HOSTNAME.getValue();
                password[0] = Dialogs.requestPassword(monitor.getComponent(), (admin) ? i18n.tr("Enter admin password for {0}:", host) : i18n.tr("Enter password for {0}:", host), i18n.tr("JHylaFAX Connection"));
            }
        };
        try {
            SwingUtilities.invokeAndWait(runner);
            return password[0];
        } catch (InterruptedException e) {
            logger.error("Unexpected exception while waiting for password", e);
            throw new ServerResponseException(i18n.tr("Abort by user."));
        } catch (InvocationTargetException e) {
            logger.error("Unexpected exception while waiting for password", e);
            throw new ServerResponseException(i18n.tr("Abort by user."));
        }
    }

    public static File getSettingsFile() throws IOException {
        return new File(FileHelper.getHomeDir("jhylafax"), "jhlafax.prefs");
    }

    public static File getAddressBookFile() throws IOException {
        if (Settings.CUSTOMIZE_ADDRESS_BOOK_FILENAME.getValue()) {
            return new File(Settings.ADDRESS_BOOK_FILENAME.getValue());
        } else {
            return new File(FileHelper.getHomeDir("jhylafax"), Settings.ADDRESS_BOOK_FILENAME.getDefaultValue());
        }
    }

    public <T> T runJob(final Job<T> job) throws Exception {
        return jobQueue.runJob(null, job);
    }

    public <T> T runJob(JDialog owner, final Job<T> job) throws Exception {
        return jobQueue.runJob(owner, job);
    }

    public void updateServerInfo() {
        serverInfoLabel.setText(Settings.HOSTNAME.getValue() + ":" + Settings.PORT.getValue());
    }

    public void updateLabels() {
        fileMenu.setText(i18n.tr("File"));
        helpMenu.setText(i18n.tr("Help"));
        for (int i = 0; i < mainTabbedPane.getTabCount(); i++) {
            AbstractQueuePanel panel = (AbstractQueuePanel) mainTabbedPane.getComponent(i);
            panel.updateLabels();
            String queueName = panel.getQueueName();
            if (queueName.equals("recvq")) {
                mainTabbedPane.setTitleAt(i, i18n.tr("Received"));
            } else if (queueName.equals("sendq")) {
                mainTabbedPane.setTitleAt(i, i18n.tr("Sending"));
            } else if (queueName.equals("pollq")) {
                mainTabbedPane.setTitleAt(i, i18n.tr("Pollable"));
            } else if (queueName.equals("doneq")) {
                mainTabbedPane.setTitleAt(i, i18n.tr("Done"));
            } else if (queueName.equals("docq")) {
                mainTabbedPane.setTitleAt(i, i18n.tr("Documents"));
            }
        }
        sendAction.putValue(Action.NAME, i18n.tr("Send Fax..."));
        sendAction.putValue(Action.SHORT_DESCRIPTION, i18n.tr("Opens a dialog for sending a fax"));
        pollAction.putValue(Action.NAME, i18n.tr("Poll Fax..."));
        pollAction.putValue(Action.SHORT_DESCRIPTION, i18n.tr("Opens a dialog for polling a fax"));
        addressBookAction.putValue(Action.NAME, i18n.tr("Address Book"));
        updateStatusAction.putValue(Action.NAME, i18n.tr("Update Status"));
        updateStatusAction.putValue(Action.SHORT_DESCRIPTION, i18n.tr("Queries the status from the server"));
        settingsDialogAction.putValue(Action.NAME, i18n.tr("Settings..."));
        settingsDialogAction.putValue(Action.SHORT_DESCRIPTION, i18n.tr("Displays the settings dialog"));
        settingsDialogAction.updateLabels();
        settingsWizardAction.putValue(Action.NAME, i18n.tr("Setup Wizard..."));
        settingsWizardAction.putValue(Action.SHORT_DESCRIPTION, i18n.tr("Displays the settings wizard"));
        settingsWizardAction.updateLabels();
        exitAction.putValue(Action.NAME, i18n.tr("Exit"));
        exitAction.putValue(Action.SHORT_DESCRIPTION, i18n.tr("Closes the application"));
        aboutAction.putValue(Action.NAME, i18n.tr("About..."));
        aboutAction.putValue(Action.SHORT_DESCRIPTION, i18n.tr("Opens a dialog that displays funny information"));
        GUIHelper.setMnemonics(getJMenuBar());
    }

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        final ArgsHandler handler = new ArgsHandler();
        handler.evaluate(args);
        evaluateArgumentsPreVisible(handler);
        ThreadGroup tg = new ThreadGroup("JHylaFAXThreadGroup") {

            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        };
        Thread mainRunner = new Thread(tg, "JHylaFAXMain") {

            public void run() {
                setContextClassLoader(JHylaFAX.class.getClassLoader());
                JHylaFAX app = new JHylaFAX();
                app.setVisible(true);
                app.evaluateArgumentsPostVisible(handler);
            }
        };
        mainRunner.start();
    }

    private static void evaluateArgumentsPreVisible(ArgsHandler handler) {
        URL url = JHylaFAX.class.getResource(handler.getLogConfigFilename());
        if (url != null) {
            PropertyConfigurator.configure(url);
        } else {
            System.err.println("Could not find logging configuration: " + handler.getLogConfigFilename());
            BasicConfigurator.configure();
        }
    }

    protected void evaluateArgumentsPostVisible(ArgsHandler handler) {
        if (!Settings.HAS_SEEN_WIZARD.getValue()) {
            SettingsWizard wizard = new SettingsWizard(JHylaFAX.this);
            wizard.setModal(true);
            wizard.setLocationRelativeTo(JHylaFAX.this);
            wizard.setVisible(true);
        }
        if (Settings.UPDATE_ON_STARTUP.getValue()) {
            updateTables();
        }
        String[] filenames = handler.getFilenames();
        String[] numbers = handler.getNumbers();
        File tempFile = null;
        if (handler.getReadStdin()) {
            tempFile = saveStdInToFile();
            if (tempFile != null) {
                String[] newFilenames = new String[filenames.length + 1];
                newFilenames[0] = tempFile.getAbsolutePath();
                System.arraycopy(filenames, 0, newFilenames, 1, filenames.length);
                filenames = newFilenames;
            }
        }
        if (filenames.length > 0 || numbers.length > 0) {
            SendDialog dialog = new SendDialog(this);
            for (String number : numbers) {
                dialog.setNumber(number);
            }
            if (filenames.length > 0) {
                dialog.setDocument(filenames[0]);
                for (int i = 1; i < filenames.length; i++) {
                    dialog.addDocument(filenames[i]);
                }
            }
            dialog.setQuitAfterSending(handler.getQuitAfterSending());
            dialog.setLocationRelativeTo(JHylaFAX.this);
            dialog.setVisible(true);
        }
    }

    private File saveStdInToFile() {
        File tempFile;
        try {
            tempFile = File.createTempFile("jhylafax", null);
            tempFile.deleteOnExit();
        } catch (IOException e) {
            logger.debug("Error creating temporary file", e);
            ErrorDialog.showError(JHylaFAX.getInstance(), i18n.tr("Error creating temporary file"), i18n.tr("JHylaFAX Error"), e);
            return null;
        }
        try {
            BufferedInputStream in = new BufferedInputStream(System.in);
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));
                try {
                    byte[] buf = new byte[BUFFER_SIZE];
                    while (true) {
                        int bytesRead = in.read(buf);
                        if (bytesRead <= 0) {
                            break;
                        }
                        out.write(buf, 0, bytesRead);
                    }
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            logger.debug("Error creating temporary file", e);
            ErrorDialog.showError(JHylaFAX.getInstance(), i18n.tr("Error creating temporary file"), i18n.tr("JHylaFAX Error"), e);
            return null;
        }
        return tempFile;
    }

    public void updateTables() {
        updateTables(JobHelper.updateStatus());
    }

    public void updateTables(StatusResponse response) {
        jobQueue.setLastUpdate(System.currentTimeMillis());
        if (response != null) {
            updateServerInfo();
            statusLabel.setText(response.status);
            statusLabel.setToolTipText("<html><pre><b>" + response.verboseStatus + "</b></pre>");
            recvqPanel.setData(response.recvq);
            sendqPanel.setData(response.sendq);
            pollqPanel.setData(response.pollq);
            doneqPanel.setData(response.doneq);
            docqPanel.setData(response.docq);
        }
    }

    private class AboutAction extends AbstractXNapAction {

        private AboutDialog dialog;

        public AboutAction() {
            putValue(ICON_FILENAME, "jhylafax.png");
        }

        public void actionPerformed(ActionEvent e) {
            if (dialog == null) {
                dialog = new AboutDialog();
                dialog.setTitle(i18n.tr("About JHylaFax {0}", getVersion()));
                dialog.setLocationRelativeTo(JHylaFAX.this);
                dialog.addHTMLTab(i18n.tr("General Information"), "about.html", true);
                JTextArea textArea = dialog.addTab(i18n.tr("License"), "LICENSE.jhylafax");
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
                dialog.addHTMLTab(i18n.tr("3rd Party Software"), "LICENSE.other.html", true);
                dialog.getTabbedPane().setSelectedIndex(0);
            }
            if (!dialog.isVisible()) {
                dialog.setVisible(true);
            }
        }
    }

    private class AddressBookAction extends AbstractXNapAction {

        public AddressBookAction() {
            putValue(ICON_FILENAME, "contents.png");
        }

        public void actionPerformed(ActionEvent e) {
            getAddressBook().setVisible(true);
        }
    }

    private class SendAction extends AbstractXNapAction {

        public SendAction() {
            putValue(ICON_FILENAME, "mail_new.png");
        }

        public void actionPerformed(ActionEvent e) {
            SendDialog dialog = new SendDialog(JHylaFAX.this);
            dialog.setLocationRelativeTo(JHylaFAX.this);
            dialog.setVisible(true);
        }
    }

    private class PollAction extends AbstractXNapAction {

        public PollAction() {
            putValue(ICON_FILENAME, "mail_get.png");
        }

        public void actionPerformed(ActionEvent e) {
            PollDialog dialog = new PollDialog(JHylaFAX.this);
            dialog.setLocationRelativeTo(JHylaFAX.this);
            dialog.setVisible(true);
        }
    }

    private class SettingsDialogAction extends AbstractXNapAction implements LocaleChangeListener {

        private SettingsDialog dialog;

        public SettingsDialogAction() {
            putValue(ICON_FILENAME, "configure.png");
        }

        public void updateLabels() {
            if (dialog != null) {
                dialog.updateLabels();
            }
        }

        public void actionPerformed(ActionEvent e) {
            if (dialog == null) {
                dialog = new SettingsDialog(JHylaFAX.this);
                dialog.setLocationRelativeTo(JHylaFAX.this);
            }
            if (!dialog.isVisible()) {
                dialog.revert();
                dialog.setVisible(true);
            }
        }
    }

    private class SettingsWizardAction extends AbstractXNapAction implements LocaleChangeListener {

        public SettingsWizardAction() {
            putValue(ICON_FILENAME, "wizard.png");
        }

        public void updateLabels() {
        }

        public void actionPerformed(ActionEvent e) {
            SettingsWizard wizard = new SettingsWizard(JHylaFAX.this);
            wizard.setLocationRelativeTo(JHylaFAX.this);
            wizard.setVisible(true);
        }
    }

    private class ExitAction extends AbstractXNapAction {

        public ExitAction() {
            putValue(ICON_FILENAME, "exit.png");
        }

        public void actionPerformed(ActionEvent e) {
            exit();
        }
    }

    private class UpdateStatusAction extends AbstractXNapAction {

        public UpdateStatusAction() {
            putValue(ICON_FILENAME, "reload.png");
        }

        public void actionPerformed(ActionEvent e) {
            updateTables();
        }
    }

    private class ConnectionHandler implements ConnectionListener {

        public void connectionOpened(ConnectionEvent event) {
            logger.info("Connected to " + event.getRemoteInetAddress().getHostAddress() + ":" + event.getRemotePort());
        }

        public void connectionClosed(ConnectionEvent event) {
            logger.info("Disconnected from " + event.getRemoteInetAddress().getHostAddress() + ":" + event.getRemotePort());
            connection.removeConnectionListener(this);
            connection = null;
        }

        public void connectionFailed(Exception e) {
            logger.info("Connection failed", e);
            connection.removeConnectionListener(this);
            connection = null;
        }
    }

    public AddressBook getAddressBook() {
        if (addressBook == null) {
            addressBook = new AddressBook();
            addressBook.restoreLayout(new SettingStore(Settings.backstore));
            try {
                addressBookFile = getAddressBookFile();
                if (addressBookFile.exists()) {
                    addressBook.load(addressBookFile);
                }
            } catch (Exception e) {
                logger.debug("Error loading addressbook", e);
                ErrorDialog.showError(JHylaFAX.this, i18n.tr("Could not load addressbook"), i18n.tr("JHylaFAX Error"), e);
            }
        }
        return addressBook;
    }

    public void showError(String message, Exception e) {
        ErrorDialog.showError(this, message, i18n.tr("JHylaFAX Error"), e);
    }

    public void showError(String message) {
        Dialogs.showError(this, message, i18n.tr("JHylaFAX Error"));
    }

    public void resetAllTables() {
        recvqPanel.resetTable();
        sendqPanel.resetTable();
        pollqPanel.resetTable();
        doneqPanel.resetTable();
        docqPanel.resetTable();
    }

    public void runNotification(Notification notification) {
        jobQueue.addNotification(notification);
    }

    public void settingsUpdated() {
        notificationTimer.settingsUpdated();
    }

    public static synchronized String getVersion() {
        if (version == null) {
            try {
                InputStream in = JHylaFAX.class.getResourceAsStream("version.properties");
                try {
                    Properties p = new Properties();
                    p.load(in);
                    version = p.getProperty("jhylafax.version", "");
                } finally {
                    in.close();
                }
            } catch (IOException e) {
                logger.debug("Error loading version properties", e);
            }
        }
        return version;
    }
}
