package net.dromard.filesynchronizer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import net.dromard.common.swing.UIAction;
import net.dromard.filesynchronizer.gui.table.FileTableManager;
import net.dromard.filesynchronizer.modules.ModuleManager;

public class MainFrame extends JFrame implements ManagerListener {

    private static final long serialVersionUID = 317260848003010016L;

    private static final String MENU_ITEM_SELECT_SOURCE = "Select Source/Destination";

    private static final String MENU_ITEM_SYNCHRONIZE = "Synchronize";

    private static final String MENU_ITEM_PROCESS = "Process";

    private static final String MENU_ITEM_DISPLAY_LOGS = "Display logs";

    private static final String MENU_ITEM_QUIT = "Quit";

    private static MainFrame mainFrame;

    private final ProgressBarHandler progress = new ProgressBarHandler(this);

    private AbstractManager tableManager;

    private File source = null;

    private File destination = null;

    private JTextArea control;

    private JSplitPane bottomSplitPanel;

    private TextAreaOutputStream textareaOutputStream;

    private JScrollPane scrollpane;

    private String lastSearch = null;

    private final UIAction MENU_ITEM_SELECT_SOURCE_ACTION = new UIAction(MENU_ITEM_SELECT_SOURCE, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {

        private static final long serialVersionUID = 0;

        @Override
        public void actionPerformed(final ActionEvent e) {
            selectSourceDestination();
        }
    };

    private final UIAction MENU_ITEM_PROCESS_ACTION = new UIAction(MENU_ITEM_PROCESS, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {

        private static final long serialVersionUID = 0;

        @Override
        public void actionPerformed(final ActionEvent e) {
            process();
        }
    };

    private final UIAction MENU_ITEM_SYNCHRONIZE_ACTION = new UIAction(MENU_ITEM_SYNCHRONIZE, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {

        private static final long serialVersionUID = 0;

        @Override
        public void actionPerformed(final ActionEvent e) {
            synchronize();
        }
    };

    private final UIAction MENU_ITEM_DISPLAY_LOGS_ACTION = new UIAction(MENU_ITEM_DISPLAY_LOGS, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)) {

        private static final long serialVersionUID = 0;

        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                if (((JRadioButtonMenuItem) e.getSource()).isSelected()) {
                    enableLog();
                } else {
                    disableLog();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    };

    private final UIAction MENU_ITEM_QUIT_ACTION = new UIAction(MENU_ITEM_QUIT, null, null, KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {

        private static final long serialVersionUID = 0;

        @Override
        public void actionPerformed(final ActionEvent e) {
            System.exit(0);
        }
    };

    private final UIAction MENU_ITEM_SEARCH_ACTION = new UIAction("File", "Search for a file", null, KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {

        private static final long serialVersionUID = 0;

        @Override
        public void actionPerformed(final ActionEvent e) {
            searchIntoTable(JOptionPane.showInputDialog(getInstance(), "Search string:"));
        }
    };

    private final UIAction MENU_ITEM_SEARCH_NEXT_ACTION = new UIAction("Next", "Search next", null, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.VK_SHIFT | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {

        private static final long serialVersionUID = 0;

        @Override
        public void actionPerformed(final ActionEvent e) {
            searchIntoTable(lastSearch);
        }
    };

    public static MainFrame getInstance() {
        if (mainFrame == null) {
            mainFrame = new MainFrame();
        }
        return mainFrame;
    }

    public ProgressBarHandler getProgressBarHandler() {
        return progress;
    }

    private MainFrame() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        UIManager.put("SplitPaneUI", net.dromard.common.swing.ui.ThreePointsSplitPaneUI.class.getName());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
        List<ManagerListener> listeners = ModuleManager.getInstance().registerModules();
        for (ManagerListener listener : listeners) {
            getTableManager().addListener(listener);
        }
        getGlassPane().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() > 1) {
                    int option = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to stop process ?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (option == JOptionPane.OK_OPTION) {
                        getTableManager().stopCurrentProcess();
                    }
                }
            }
        });
    }

    private AbstractManager getTableManager() {
        return tableManager;
    }

    public void init() {
        setTitle("File Synchronizer");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        tabbedPane.setOpaque(false);
        JTable table = new JTable();
        tableManager = new FileTableManager(table);
        tableManager.addListener(this);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.getViewport().setOpaque(false);
        tableScrollPane.setOpaque(false);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.setOpaque(false);
        tabbedPane.addTab("Table View", tableScrollPane);
        FileDiffDetailsPanel filesDetails = new FileDiffDetailsPanel();
        tableManager.addListener(filesDetails);
        tabbedPane.addTab("Details", filesDetails);
        bottomSplitPanel = new JSplitPane();
        bottomSplitPanel.setTopComponent(tabbedPane);
        control = new JTextArea();
        scrollpane = new JScrollPane(control);
        bottomSplitPanel.setBottomComponent(scrollpane);
        bottomSplitPanel.setOpaque(false);
        bottomSplitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
        bottomSplitPanel.setBorder(null);
        bottomSplitPanel.setBackground(Color.WHITE);
        scrollpane.setVisible(false);
        bottomSplitPanel.setDividerLocation(1d);
        getContentPane().add(bottomSplitPanel, BorderLayout.CENTER);
        JMenu searchMenu = new JMenu("Search");
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem selectSourceMenuItem = new JMenuItem(MENU_ITEM_SELECT_SOURCE_ACTION);
        JMenuItem synchronizeMenuItem = new JMenuItem(MENU_ITEM_SYNCHRONIZE_ACTION);
        JMenuItem processMenuItem = new JMenuItem(MENU_ITEM_PROCESS_ACTION);
        JRadioButtonMenuItem displayLogMenuItem = new JRadioButtonMenuItem(MENU_ITEM_DISPLAY_LOGS_ACTION);
        JMenuItem quitMenuItem = new JMenuItem(MENU_ITEM_QUIT_ACTION);
        JMenuItem search = new JMenuItem(MENU_ITEM_SEARCH_ACTION);
        JMenuItem searchNext = new JMenuItem(MENU_ITEM_SEARCH_NEXT_ACTION);
        menu.add(selectSourceMenuItem);
        menu.add(synchronizeMenuItem);
        menu.add(processMenuItem);
        menu.addSeparator();
        menu.add(displayLogMenuItem);
        menu.addSeparator();
        menu.add(quitMenuItem);
        searchMenu.add(search);
        searchMenu.add(searchNext);
        menuBar.add(menu);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void searchIntoTable(String search) {
        if (search != null && search.length() > 0) {
            if (((FileTableManager) tableManager).search(search) == -1) {
                JOptionPane.showConfirmDialog(this, "Ooops no file was found with '" + search + "'", "Nothing found", JOptionPane.OK_OPTION);
                search = null;
            } else {
                this.lastSearch = search;
            }
        }
    }

    private void enableLog() {
        scrollpane.setVisible(true);
        if (textareaOutputStream != null) {
            textareaOutputStream.enable(true);
        } else {
            textareaOutputStream = new TextAreaOutputStream(control);
        }
        bottomSplitPanel.setDividerLocation(0.75);
    }

    private void disableLog() throws IOException {
        if (textareaOutputStream != null) {
            textareaOutputStream.enable(false);
        }
        bottomSplitPanel.setDividerLocation(1d);
        scrollpane.setVisible(false);
    }

    private void selectSourceDestination() {
        File tmp = showChooserOpenDialog(this, "Select source ...", source);
        if (tmp != null) {
            source = tmp;
        }
        tmp = showChooserOpenDialog(this, "Select destination ...", destination);
        if (tmp != null) {
            destination = tmp;
        }
        synchronize();
    }

    private void synchronize() {
        if (source == null || destination == null) {
            JOptionPane.showMessageDialog(this, "You must select files (or folder) to synchronized !", "Ooops ...", JOptionPane.ERROR_MESSAGE);
        } else {
            getTableManager().synchronize(source, destination);
        }
    }

    private void process() {
        if (source == null || destination == null) {
            JOptionPane.showMessageDialog(this, "You must select files (or folder) to synchronized !", "Ooops ...", JOptionPane.ERROR_MESSAGE);
        } else {
            getTableManager().processSynchronization();
        }
    }

    /**
     * @return the destination
     */
    public File getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(final File destination) {
        this.destination = destination;
    }

    /**
     * @return the source
     */
    public File getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(final File source) {
        this.source = source;
    }

    public void synchronizeStarted(final AbstractManager initiator) {
        System.out.println("Synchronization started at " + new Date());
        progress.progress("View differences");
    }

    public void synchronizeStopped(final AbstractManager initiator) {
        System.out.println("Synchronization stopped at " + new Date());
        progress.progress("Stopping synchronization process");
        progress.stop();
    }

    public void synchronizeFinished(final AbstractManager initiator) {
        System.out.println("Synchronize Finished at " + new Date());
        progress.stop();
    }

    public void processStarted(final AbstractManager initiator) {
        System.out.println("Processing started at " + new Date());
        progress.progress("Applying changes");
    }

    public void processStopped(final AbstractManager initiator) {
        System.out.println("Processing stopped at " + new Date());
        progress.progress("Stopping process");
        progress.stop();
    }

    public void processFinished(final AbstractManager initiator) {
        System.out.println("Processing finished at " + new Date());
        progress.stop();
    }

    public File showChooserOpenDialog(final Component parent, final String title, final File currentDirectory) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(currentDirectory);
        fileChooser.setDialogTitle(title);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(parent)) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static void main(final String[] args) {
        final MainFrame frame = getInstance();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.setSize(600, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (args.length == 2) {
                    frame.setSource(new File(args[0]));
                    frame.setDestination(new File(args[1]));
                    frame.synchronize();
                }
            }
        });
    }
}
