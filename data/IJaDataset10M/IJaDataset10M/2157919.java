package juploader.gui;

import juploader.gui.draganddrop.DragAndDropListener;
import juploader.gui.draganddrop.DragAndDropTransferHandler;
import juploader.gui.listeners.FilesToUploadListener;
import juploader.gui.listeners.MouseWheelOnComboListener;
import juploader.gui.listeners.PopupMenuMouseListener;
import juploader.gui.utils.FileFilterFactory;
import juploader.gui.utils.FileNameRecognizer;
import juploader.gui.utils.FileNameShortener;
import juploader.gui.utils.FileSizePresenter;
import juploader.gui.utils.GuiUtils;
import juploader.gui.utils.Messager;
import juploader.gui.utils.RecentFiles;
import juploader.plugin.PluginInfo;
import juploader.pluginmanager.PluginManager;
import juploader.preferences.ApplicationPreferences;
import juploader.upload.plugin.UploadProvider;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Główne okno aplikacji.
 *
 * @author Adam Pawelec
 */
public class MainWindow extends SingleFrameApplication {

    private JComboBox filesCombo;

    private JLabel informationLabel;

    /** Panel z zakładkami dla pluginów. */
    private JTabbedPane pluginsTabbedPanel;

    private javax.swing.Action openFilesAction;

    private javax.swing.Action addFilesAction;

    private javax.swing.Action addUrlAction;

    private javax.swing.Action addScreenshotAction;

    private javax.swing.Action quitAction;

    private javax.swing.Action fullScreenshotAction;

    private javax.swing.Action partScreenshotAction;

    private javax.swing.Action removeFileAction;

    private javax.swing.Action removeAllFilesAction;

    private javax.swing.Action alwaysOnTopAction;

    private javax.swing.Action preferencesAction;

    private javax.swing.Action recentFilesAction;

    private javax.swing.Action aboutProgramAction;

    /** Mapa zasobów. */
    private ResourceMap resourceMap;

    /** Ustawienia programu. */
    private final ApplicationPreferences preferences = ApplicationPreferences.getInstance();

    /** Obiekt służący do wyświetlania okien dialogowych. */
    private Messager messager;

    /** Lista plików do wysłania. */
    private final List<File> filesToUpload;

    /** Manager pluginów. */
    private final PluginManager pluginManager = PluginManager.getInstance();

    /** Wykonawca wątków, współdzielony przez wszystkie pluginy. */
    private final ExecutorService executorService;

    /** Słuchacze zmiany listy plików. */
    private final List<FilesToUploadListener> filesToUploadListeners;

    /** Historia ostatnich plików. */
    private RecentFiles recentFiles;

    private JMenuBar menuBar;

    public MainWindow() {
        filesToUpload = new ArrayList<File>();
        executorService = Executors.newCachedThreadPool();
        filesToUploadListeners = new ArrayList<FilesToUploadListener>();
    }

    @Override
    protected void startup() {
        resourceMap = getContext().getResourceMap();
        messager = new Messager(getMainFrame());
        ApplicationActionMap actionMap = getContext().getActionMap();
        openFilesAction = actionMap.get("openFiles");
        addFilesAction = actionMap.get("addFiles");
        removeFileAction = actionMap.get("removeFile");
        removeAllFilesAction = actionMap.get("removeAllFiles");
        addUrlAction = actionMap.get("addUrl");
        addScreenshotAction = actionMap.get("addScreenshot");
        fullScreenshotAction = actionMap.get("fullScreenshot");
        partScreenshotAction = actionMap.get("partScreenshot");
        quitAction = actionMap.get("quit");
        alwaysOnTopAction = actionMap.get("alwaysOnTop");
        preferencesAction = actionMap.get("preferences");
        recentFilesAction = actionMap.get("recentFiles");
        aboutProgramAction = actionMap.get("aboutProgram");
        setEnableRemoveFilesActions(false);
        GuiUtils.setLookAndFeel();
        GuiUtils.localizeStandardComponents();
        initGUI();
        addExitListener(new ExitListenerImpl());
        getMainFrame().setTransferHandler(new DragAndDropTransferHandler(new DragAndDropListenerImpl()));
        getMainFrame().setName("mainWindow");
        show(getMainFrame());
    }

    @Override
    protected void shutdown() {
        super.shutdown();
        preferences.setRecentFiles(recentFiles.getRecentFilesList());
        preferences.setLastSelectedTab(pluginsTabbedPanel.getSelectedIndex());
    }

    private void initGUI() {
        getMainView().setMenuBar(createMenuBar());
        getMainView().setComponent(createContentPane());
    }

    private JMenuBar createMenuBar() {
        this.menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        fileMenu.setName("menu.file");
        fileMenu.add(openFilesAction);
        fileMenu.add(addFilesAction);
        JMenu screenshotMenu = new JMenu(addScreenshotAction);
        screenshotMenu.add(fullScreenshotAction);
        screenshotMenu.add(partScreenshotAction);
        fileMenu.addSeparator();
        JMenu recentFilesMenu = createRecentFilesMenu();
        fileMenu.add(recentFilesMenu);
        fileMenu.addSeparator();
        fileMenu.add(quitAction);
        JMenu toolsMenu = new JMenu();
        toolsMenu.setName("menu.tools");
        toolsMenu.add(preferencesAction);
        JMenu helpMenu = new JMenu();
        helpMenu.setName("menu.help");
        helpMenu.add(aboutProgramAction);
        menuBar.add(fileMenu);
        menuBar.add(createServicesMenu());
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JMenu createServicesMenu() {
        JMenu services = new JMenu();
        services.setName("menu.services");
        List<UploadProvider> plugins = PluginManager.getInstance().readAllUploadPlugins();
        int index = 0;
        for (UploadProvider plugin : plugins) {
            final int finalIndex = index;
            PluginInfo pluginInfo = plugin.getPluginInfo();
            JMenuItem serviceItem = new JMenuItem(pluginInfo.getName(), pluginInfo.getIcon());
            serviceItem.setAccelerator(KeyStroke.getKeyStroke("ctrl " + (finalIndex + 1)));
            serviceItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    pluginsTabbedPanel.setSelectedIndex(finalIndex);
                }
            });
            services.add(serviceItem);
            index++;
        }
        return services;
    }

    private JMenu createRecentFilesMenu() {
        JMenu recentFilesMenu = new JMenu(recentFilesAction);
        List<File> recentFilesList = preferences.getRecentFiles();
        recentFiles = new RecentFiles(recentFilesList, recentFilesMenu);
        recentFiles.setListener(new RecentFiles.RecentFileSelectedListener() {

            public void recentFileSelected(File file, ActionEvent e) {
                if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 0) {
                    clearFilesToUpload();
                }
                addFileToList(file);
                synchronizeFilesCombo();
            }
        });
        return recentFilesMenu;
    }

    private JPanel createContentPane() {
        JPanel contentPane = new JPanel(new MigLayout(null, "[grow]", "[pref][grow]"));
        contentPane.add(createFileSelectionPanel(), "grow, wrap");
        pluginsTabbedPanel = createPluginsTabs();
        contentPane.add(pluginsTabbedPanel, "grow");
        return contentPane;
    }

    private JPanel createFileSelectionPanel() {
        JPanel panel = new JPanel(new MigLayout(null, "[pref][grow][pref]"));
        panel.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("border.files.text")));
        JLabel fileLabel = new JLabel();
        fileLabel.setName("label.file");
        filesCombo = new JComboBox();
        filesCombo.addMouseWheelListener(new MouseWheelOnComboListener());
        filesCombo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                synchronizeInformationLabel();
            }
        });
        informationLabel = new JLabel();
        JButton openFilesButton = new JButton(openFilesAction);
        openFilesButton.setText(null);
        openFilesButton.addMouseListener(new PopupMenuMouseListener(createOpenFilesButtonPopupMenu()));
        JButton removeFilesButton = new JButton(removeFileAction);
        removeFilesButton.setText(null);
        removeFilesButton.addMouseListener(new PopupMenuMouseListener(createRemoveFileButtonPopupMenu()));
        panel.add(fileLabel);
        panel.add(filesCombo, "span, grow, wrap");
        panel.add(informationLabel, "span 2");
        panel.add(openFilesButton, "sg 1");
        panel.add(removeFilesButton, "sg 1");
        return panel;
    }

    private JPopupMenu createOpenFilesButtonPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(openFilesAction);
        popupMenu.add(addFilesAction);
        JMenu screenshotMenu = new JMenu(addScreenshotAction);
        screenshotMenu.add(fullScreenshotAction);
        screenshotMenu.add(partScreenshotAction);
        return popupMenu;
    }

    private JPopupMenu createRemoveFileButtonPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.add(removeFileAction);
        popupMenu.add(removeAllFilesAction);
        return popupMenu;
    }

    /** Tworzy zakładki dla plugin uploadu. */
    private JTabbedPane createPluginsTabs() {
        JTabbedPane panel = new JTabbedPane();
        List<UploadProvider> plugins = pluginManager.readAllUploadPlugins();
        if (plugins.isEmpty()) {
            messager.showWarningDialog("message.noPlugins");
        } else {
            int index = 0;
            for (UploadProvider plugin : plugins) {
                UploadProviderPanel pluginPanel = new UploadProviderPanel(getMainFrame(), plugin, this, executorService, index);
                pluginPanel.addToTabbedPane(panel);
                index++;
            }
            int lastSelectedTab = preferences.getLastSelectedTab();
            if (lastSelectedTab < panel.getTabCount()) {
                panel.setSelectedIndex(lastSelectedTab);
            }
        }
        return panel;
    }

    @Action
    public void openFiles() {
        selectAndHandleFiles(true);
    }

    @Action
    public void addFiles() {
        selectAndHandleFiles(false);
    }

    /**
     * Wyświetla okno dialogowe służące do wyboru plików i dodaje je do listy
     * plików do wysłania.
     *
     * @param clear określa, czy zwartość aktualnej listy plików do wysłania ma
     *              zostać wyczyszczona
     */
    private void selectAndHandleFiles(boolean clear) throws HeadlessException {
        JFileChooser fileChooser = createFileChooser();
        if (fileChooser.showOpenDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            if (clear) {
                clearFilesToUpload();
            }
            File[] selectedFiles = fileChooser.getSelectedFiles();
            addFilesToList(selectedFiles);
            synchronizeFilesCombo();
            preferences.setLastDirectory(fileChooser.getCurrentDirectory());
        }
    }

    private void clearFilesToUpload() {
        filesToUpload.clear();
    }

    @Action
    public void removeFile() {
        File selectedFile = getSelectedFile();
        if (selectedFile != null) {
            filesToUpload.remove(selectedFile);
            synchronizeFilesCombo();
            notifyFilesToUploadListeners();
        }
    }

    @Action
    public void removeAllFiles() {
        clearFilesToUpload();
        synchronizeFilesCombo();
    }

    @Action
    public void addUrl() {
    }

    @Action
    public void addScreenshot() {
    }

    @Action
    public void fullScreenshot() {
    }

    @Action
    public void partScreenshot() {
    }

    @Action
    public void recentFiles() {
    }

    @Action
    public void quit() {
        exit();
    }

    @Action
    public void alwaysOnTop() {
    }

    @Action
    public void aboutProgram() {
        JOptionPane.showMessageDialog(getMainFrame(), resourceMap.getString("message.aboutProgram"), resourceMap.getString("message.aboutProgram.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    @Action
    public void preferences() {
        PreferencesDialog dialog = new PreferencesDialog(preferences, getMainFrame());
        dialog.pack();
        dialog.setLocationRelativeTo(getMainFrame());
        dialog.setVisible(true);
    }

    private void setEnableRemoveFilesActions(boolean b) {
        removeAllFilesAction.setEnabled(b);
        removeFileAction.setEnabled(b);
    }

    /**
     * Tworzy JFileChooser, przygotowany do użycia przez różne akcje.
     *
     * @return okno dialogowe służące do wyboru pliku
     */
    private JFileChooser createFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileHidingEnabled(!preferences.isShowHiddenFiles());
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setCurrentDirectory(preferences.getLastDirectory());
        List<FileNameExtensionFilter> filters = FileFilterFactory.createFileNameFilters();
        for (FileNameExtensionFilter filter : filters) {
            fileChooser.addChoosableFileFilter(filter);
        }
        fileChooser.setAcceptAllFileFilterUsed(true);
        return fileChooser;
    }

    private void synchronizeFilesCombo() {
        filesCombo.removeAllItems();
        FileNameRecognizer recognizer = new FileNameRecognizer();
        for (File file : filesToUpload) {
            if (preferences.isShowFullPath()) {
                filesCombo.addItem(FileNameShortener.shortenFileName(file.getAbsolutePath()));
            } else {
                filesCombo.addItem(FileNameShortener.shortenFileName(recognizer.getRecognizedFileName(file)));
            }
            synchronizeInformationLabel();
        }
        notifyFilesToUploadListeners();
    }

    private File getSelectedFile() {
        int index = filesCombo.getSelectedIndex();
        if (index < 0) {
            return null;
        } else {
            return filesToUpload.get(index);
        }
    }

    private void synchronizeInformationLabel() {
        File file = getSelectedFile();
        if (file == null) {
            informationLabel.setText(null);
            setEnableRemoveFilesActions(false);
        } else {
            long selectedFileSize = file.length();
            long totalFileSize = getFilesToUploadTotalSize();
            int fileCount = filesToUpload.size();
            FileSizePresenter presenter = new FileSizePresenter(selectedFileSize);
            String selectedSize = preferences.isShowBinaryPrefixes() ? presenter.presentInIec() : presenter.presentInSi();
            presenter.setFileSize(totalFileSize);
            String totalSize = preferences.isShowBinaryPrefixes() ? presenter.presentInIec() : presenter.presentInSi();
            informationLabel.setText(MessageFormat.format(resourceMap.getString("label.information.format"), selectedSize, totalSize, fileCount));
            setEnableRemoveFilesActions(true);
        }
    }

    /**
     * Dodaje plik do listy plików do wysłania, sprawdzając czy plik nie został
     * już dodany, na pewno istnieje i czy jest zwykłym plikiem.
     *
     * @param file plik do dodania
     */
    private void addFileToList(File file) {
        if (!filesToUpload.contains(file) && file.exists() && file.isFile()) {
            filesToUpload.add(file);
            recentFiles.addFile(file, true);
        }
    }

    /**
     * Dodaje tablicę plików do listy plików do wysłania.
     *
     * @param files tablica z wybranymi plikami
     */
    private void addFilesToList(File[] files) {
        for (File file : files) {
            addFileToList(file);
        }
    }

    /**
     * Dodaje listę plików do listy plików do wysłania.
     *
     * @param files tablica z wybranymi plikami
     */
    private void addFilesToList(List<File> files) {
        for (File file : files) {
            addFileToList(file);
        }
    }

    /**
     * Dodaj słuchacza zmiany listy plików do wysłania.
     *
     * @param listener słuchacz
     */
    public synchronized void addFilesToUploadListeners(FilesToUploadListener listener) {
        filesToUploadListeners.add(listener);
    }

    private void notifyFilesToUploadListeners() {
        for (FilesToUploadListener filesToUploadListener : filesToUploadListeners) {
            filesToUploadListener.filesToUploadChanged(Collections.unmodifiableList(filesToUpload));
        }
    }

    /**
     * Zwraca łączny rozmiar plików wybranych do uploadu.
     *
     * @return łączny rozmiar plików (w bajtach)
     */
    private long getFilesToUploadTotalSize() {
        long totalSize = 0;
        for (File file : filesToUpload) {
            totalSize += file.length();
        }
        return totalSize;
    }

    /**
     * Zwraca listę plików wraz z plikami znajdującymi się w katalogach (bez
     * tych katalogów).
     *
     * @param files lista plików/katalogów
     * @return lista plików
     */
    private List<File> readSubirectories(List<File> files) {
        List<File> result = new ArrayList<File>();
        for (File file : files) {
            if (file.isDirectory()) {
                File[] filesInDirectory = file.listFiles();
                Arrays.sort(filesInDirectory);
                result.addAll(Arrays.asList(filesInDirectory));
            } else {
                result.add(file);
            }
        }
        return result;
    }

    public JTabbedPane getTabbedPanel() {
        return pluginsTabbedPanel;
    }

    private class ExitListenerImpl implements ExitListener {

        public boolean canExit(EventObject event) {
            boolean exit = false;
            exit = !preferences.isAskBeforeExit() || messager.showConfirmDialog("message.beforeExit");
            return exit;
        }

        public void willExit(EventObject event) {
            executorService.shutdownNow();
        }
    }

    private class DragAndDropListenerImpl implements DragAndDropListener {

        public void dropped(List<File> droppedFiles) {
            addFilesToList(readSubirectories(droppedFiles));
            synchronizeFilesCombo();
        }
    }
}
