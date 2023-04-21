package com.google.code.ebmlviewer.viewer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import com.google.code.ebmlviewer.io.EbmlFile;
import com.google.code.ebmlviewer.viewer.compontents.tabs.TabPane;
import com.google.code.ebmlviewer.viewer.compontents.tabs.TabPaneEvent;
import com.google.code.ebmlviewer.viewer.compontents.tabs.TabPaneListener;
import com.google.code.ebmlviewer.viewer.util.PlainTextResourceBundleControl;
import static com.google.code.ebmlviewer.viewer.compontents.Components.configurePersistentBounds;
import static com.google.code.ebmlviewer.viewer.compontents.Events.getFrameForEvent;
import static java.awt.EventQueue.invokeLater;

/** The default application launcher. */
public final class Viewer {

    private static final ResourceBundle resources = ResourceBundle.getBundle(Viewer.class.getPackage().getName() + ".messages", new PlainTextResourceBundleControl("txt", "UTF-8"));

    public static void main(String[] args) {
        configureLogging();
        configureLookAndFeel();
        invokeLater(new Runnable() {

            @Override
            public void run() {
                Viewer viewer = new Viewer();
                viewer.getFrame().setVisible(true);
            }
        });
    }

    private static void configureLogging() {
        InputStream stream = Viewer.class.getResourceAsStream("logging.properties");
        if (stream == null) {
            return;
        }
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            throw new AssertionError(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                Logger.getLogger(Viewer.class.getName()).log(Level.WARNING, "exception thrown while closing an I/O resource", e);
            }
        }
    }

    private static void configureLookAndFeel() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (Exception e) {
                    Logger.getLogger(Viewer.class.getName()).log(Level.WARNING, "exception thrown while changing the L&F", e);
                }
                break;
            }
        }
    }

    private JFrame frame;

    private JFileChooser fileChooser;

    private TabPane tabPane;

    private final int maximumRecentlyUsedFiles = 16;

    private List<File> recentlyUsedFiles;

    private JMenu recentlyUsedFilesMenu;

    private Viewer() {
        recentlyUsedFiles = new ArrayList<File>(loadRecentlyUsedFiles());
    }

    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame(resources.getString("viewer@frame.title"));
            frame.setIconImages(Arrays.asList(new ImageIcon(Viewer.class.getResource("matroska_16x16.png")).getImage(), new ImageIcon(Viewer.class.getResource("matroska_32x32.png")).getImage()));
            frame.setName("viewer");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            tabPane = new TabPane();
            frame.setContentPane(tabPane);
            Action openAction = new AbstractAction(resources.getString("open@action.name")) {

                @Override
                public void actionPerformed(ActionEvent event) {
                    JFileChooser fileChooser = getFileChooser();
                    if (fileChooser.showOpenDialog(getFrameForEvent(event)) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    open(Arrays.asList(fileChooser.getSelectedFiles()));
                }
            };
            openAction.putValue(Action.SHORT_DESCRIPTION, resources.getString("open@action.description"));
            openAction.putValue(Action.MNEMONIC_KEY, KeyStroke.getKeyStroke(resources.getString("open@action.mnemonic")).getKeyCode());
            openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(resources.getString("open@action.accelerator")));
            Action closeActiveTabAction = tabPane.getCloseSelectedTabAction();
            closeActiveTabAction.putValue(Action.NAME, resources.getString("closeActiveTab@action.name"));
            closeActiveTabAction.putValue(Action.SHORT_DESCRIPTION, resources.getString("closeActiveTab@action.description"));
            closeActiveTabAction.putValue(Action.MNEMONIC_KEY, KeyStroke.getKeyStroke(resources.getString("closeActiveTab@action.mnemonic")).getKeyCode());
            closeActiveTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(resources.getString("closeActiveTab@action.accelerator")));
            Action exitAction = new AbstractAction(resources.getString("exit@action.name")) {

                @Override
                public void actionPerformed(ActionEvent event) {
                    getFrameForEvent(event).dispose();
                }
            };
            exitAction.putValue(Action.SHORT_DESCRIPTION, resources.getString("exit@action.description"));
            exitAction.putValue(Action.MNEMONIC_KEY, KeyStroke.getKeyStroke(resources.getString("exit@action.mnemonic")).getKeyCode());
            exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(resources.getString("exit@action.accelerator")));
            JMenuBar menuBar = new JMenuBar();
            frame.setJMenuBar(menuBar);
            JMenu fileMenu = menuBar.add(new JMenu(resources.getString("file@menu.name")));
            fileMenu.setMnemonic(KeyStroke.getKeyStroke(resources.getString("file@menu.mnemonic")).getKeyCode());
            fileMenu.add(new JMenuItem(openAction)).setToolTipText(null);
            fileMenu.add(getRecentlyUsedFilesMenu());
            fileMenu.add(new JMenuItem(closeActiveTabAction)).setToolTipText(null);
            fileMenu.addSeparator();
            fileMenu.add(new JMenuItem(exitAction)).setToolTipText(null);
            configurePersistentBounds(frame, Preferences.userNodeForPackage(Viewer.class), frame.getName());
            tabPane.addTabPaneListener(new TabPaneListener() {

                @Override
                public void tabAdded(TabPaneEvent event) {
                }

                @Override
                public void tabRemoved(TabPaneEvent event) {
                }

                @Override
                public void tabSelected(TabPaneEvent event) {
                    EbmlFileTab tab = (EbmlFileTab) event.getTab();
                    if (tab == null) {
                        frame.setTitle(resources.getString("viewer@frame.title"));
                    } else {
                        frame.setTitle(tab.getFile().getAbsolutePath() + " - " + resources.getString("viewer@frame.title"));
                    }
                }
            });
        }
        return frame;
    }

    private JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            EbmlFileView fileView = new EbmlFileView(fileChooser.getFileSystemView());
            fileView.override("mkv", resources.getString("fileChooser.mkv.description"), new ImageIcon(Viewer.class.getResource("mkv_16x16.png")));
            fileView.override("mk3d", resources.getString("fileChooser.mk3d.description"), new ImageIcon(Viewer.class.getResource("mk3d_16x16.png")));
            fileView.override("mka", resources.getString("fileChooser.mka.description"), new ImageIcon(Viewer.class.getResource("mka_16x16.png")));
            fileView.override("mks", resources.getString("fileChooser.mks.description"), new ImageIcon(Viewer.class.getResource("matroska_16x16.png")));
            fileView.override("webm", resources.getString("fileChooser.webm.description"), new ImageIcon(Viewer.class.getResource("webm_16x16.png")));
            fileChooser.setFileView(fileView);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(resources.getString("fileChooser.filter.allSupported.description"), resources.getString("fileChooser.filter.allSupported.extensions").split("[ ]+")));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(resources.getString("fileChooser.filter.matroska.description"), resources.getString("fileChooser.filter.matroska.extensions").split("[ ]+")));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(resources.getString("fileChooser.filter.webm.description"), resources.getString("fileChooser.filter.webm.extensions").split("[ ]+")));
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[0]);
        }
        return fileChooser;
    }

    private void open(List<File> files) {
        List<File> notFoundFiles = new LinkedList<File>();
        FileView fileView = getFileChooser().getFileView();
        EbmlFileTab tab = null;
        for (File file : files) {
            try {
                tab = new EbmlFileTab(new EbmlFile(file), file, fileView.getIcon(file));
                tabPane.addTab(tab);
                addRecentlyUsedFile(file);
            } catch (FileNotFoundException ignored) {
                notFoundFiles.add(file);
            }
        }
        if (tab != null) {
            tabPane.setSelectedTab(tab);
        }
        if (!notFoundFiles.isEmpty()) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(resources.getString("fileChooser.fileNotFound.header"));
            for (File file : notFoundFiles) {
                buffer.append(String.format(resources.getString("fileChooser.fileNotFound.entry"), file));
            }
            JOptionPane.showMessageDialog(frame, buffer.toString(), frame.getTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private JMenu getRecentlyUsedFilesMenu() {
        if (recentlyUsedFilesMenu == null) {
            recentlyUsedFilesMenu = new JMenu(resources.getString("recentlyUsedFiles@menu.name"));
            recentlyUsedFilesMenu.setMnemonic(KeyStroke.getKeyStroke(resources.getString("recentlyUsedFiles@menu.mnemonic")).getKeyCode());
            updateRecentlyUsedFilesMenu();
        }
        return recentlyUsedFilesMenu;
    }

    private void updateRecentlyUsedFilesMenu() {
        if (recentlyUsedFilesMenu != null) {
            recentlyUsedFilesMenu.removeAll();
            recentlyUsedFilesMenu.setEnabled(!recentlyUsedFiles.isEmpty());
            for (int i = 0; i < recentlyUsedFiles.size(); i++) {
                recentlyUsedFilesMenu.add(new OpenRecentlyUsedFileAction(i, recentlyUsedFiles.get(i)));
            }
            recentlyUsedFilesMenu.addSeparator();
            Action clearRecentlyUsedFilesAction = new AbstractAction(resources.getString("clearRecentlyUsedFiles@action.name")) {

                @Override
                public void actionPerformed(ActionEvent event) {
                    clearRecentlyUsedFiles();
                }
            };
            clearRecentlyUsedFilesAction.putValue(Action.SHORT_DESCRIPTION, resources.getString("clearRecentlyUsedFiles@action.description"));
            clearRecentlyUsedFilesAction.putValue(Action.MNEMONIC_KEY, KeyStroke.getKeyStroke(resources.getString("clearRecentlyUsedFiles@action.mnemonic")).getKeyCode());
            recentlyUsedFilesMenu.add(new JMenuItem(clearRecentlyUsedFilesAction)).setToolTipText(null);
        }
    }

    private void addRecentlyUsedFile(File file) {
        recentlyUsedFiles.remove(file);
        recentlyUsedFiles.add(0, file);
        while (recentlyUsedFiles.size() > maximumRecentlyUsedFiles) {
            recentlyUsedFiles.remove(recentlyUsedFiles.size() - 1);
        }
        saveRecentlyUsedFiles(recentlyUsedFiles);
        updateRecentlyUsedFilesMenu();
    }

    private void clearRecentlyUsedFiles() {
        recentlyUsedFiles.clear();
        saveRecentlyUsedFiles(recentlyUsedFiles);
        updateRecentlyUsedFilesMenu();
    }

    private List<File> loadRecentlyUsedFiles() {
        Preferences preferences = Preferences.userNodeForPackage(getClass());
        String paths = preferences.get("recentlyUsedFiles", null);
        if (paths == null) {
            return Collections.emptyList();
        }
        List<File> files = new LinkedList<File>();
        for (String path : paths.split(";")) {
            if (!path.isEmpty()) {
                files.add(new File(path));
            }
            if (files.size() >= maximumRecentlyUsedFiles) {
                break;
            }
        }
        return files;
    }

    private void saveRecentlyUsedFiles(List<File> files) {
        Preferences preferences = Preferences.userNodeForPackage(getClass());
        StringBuilder paths = new StringBuilder();
        for (File file : files) {
            if (paths.length() > 0) {
                paths.append(';');
            }
            paths.append(file.getAbsolutePath());
        }
        preferences.put("recentlyUsedFiles", paths.toString());
    }

    private class OpenRecentlyUsedFileAction extends AbstractAction {

        private final File file;

        private OpenRecentlyUsedFileAction(int index, File file) {
            super(file.getAbsolutePath());
            if (index < 10) {
                putValue(MNEMONIC_KEY, KeyEvent.VK_0 + (index < 9 ? index + 1 : 0));
            }
            this.file = file;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            open(Collections.singletonList(file));
        }
    }
}
