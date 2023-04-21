package org.gerhardb.jibs.textPad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.tree.TreePath;
import org.gerhardb.jibs.*;
import org.gerhardb.lib.dirtree.ExtendedDirectoryTreePopUp;
import org.gerhardb.lib.dirtree.rdp.BasicOptionsManager;
import org.gerhardb.lib.dirtree.rdp.IListShowTree;
import org.gerhardb.lib.dirtree.rdp.ILoad;
import org.gerhardb.lib.dirtree.rdp.ListShowTreeCoordinator;
import org.gerhardb.lib.dirtree.rdp.PathManager;
import org.gerhardb.lib.io.FilenameFileFilter;
import org.gerhardb.lib.scroller.ScrollerChangeEvent;
import org.gerhardb.lib.scroller.ScrollerListener;
import org.gerhardb.lib.scroller.ScrollerSlider;
import org.gerhardb.lib.swing.JFileChooserExtra;
import org.gerhardb.lib.swing.SwingUtils;
import org.gerhardb.lib.swing.UIMenu;
import org.gerhardb.lib.util.ActionHelpers;
import org.gerhardb.lib.util.Icons;
import org.gerhardb.lib.util.app.InfoPlugins;
import org.gerhardb.lib.util.startup.AppPluginStartup;
import org.gerhardb.lib.util.startup.AppStarter;
import org.gerhardb.lib.util.startup.Loading;

/**
 * Info Display.
 */
public class TextPad extends JFrame implements ScrollerListener, IListShowTree, InfoPlugins, ILoad {

    private static final String LAST_FILE = "LastFile";

    private static final String[] FILE_NAME_ENDINGS = new String[] { ".txt", ".htm", ".html" };

    static final String COUNT_TEXT_FILES = "COUNT_TEXT_FILES";

    private static final String FILE_PANEL_SIZE = "ListPicturePanelDividerLocation";

    private static final String TREE_PANEL_SIZE = "TreePanelSize";

    private static final String FONT_SIZE = "font_size";

    public static final int TREE_MIN = 100;

    public static final int FILE_MIN = 1000;

    static final Preferences clsPrefs = Preferences.userRoot().node("/org/gerhardb/jibs/textPad/TextPad");

    JEditorPane myEditorPane = new JEditorPane();

    private JSplitPane myListPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    private JSplitPane myTreePanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    TextPadActions myActions;

    JToolBar myToolBar = new JToolBar();

    private ScrollerSlider mySlider;

    UIMenu myUImenu = new UIMenu(this, getAppIDandTargetType());

    private JComboBox<String> myFontSize = new JComboBox<String>(new String[] { "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "32", "48", "64", "72", "80", "96", "128" });

    boolean iExitOnClose;

    boolean iNeedToFinalize = true;

    ListShowTreeCoordinator myLST;

    Loading myLoading = new Loading(this, "Text Pad");

    /**
	 * Prints basic Information Report.
	 */
    public TextPad(boolean exitOnClose) {
        this.iExitOnClose = exitOnClose;
        this.myLST = new ListShowTreeCoordinator(this);
        this.myLST.init(this);
        this.myActions = new TextPadActions(this);
        this.myLST.addActions(this.myActions);
        buildAndDisplayInterface();
        File loadFile = new File(this.myLST.getPathManager().getDirectoryAbsolute(PathManager.DIR_TREE));
        this.myLST.load(loadFile, this.myLoading, this, Thread.currentThread());
    }

    public ListShowTreeCoordinator getLST() {
        return this.myLST;
    }

    @Override
    public void info(StringBuffer sb) {
        sb.append("From TextPad");
    }

    @Override
    public String getAppIDandTargetType() {
        return "Text";
    }

    @Override
    public JFrame getTopFrame() {
        return this;
    }

    @Override
    public ScrollerListener getScrollerListener() {
        return this;
    }

    @Override
    public FilenameFileFilter getFilenameFileFilter() {
        return new FilenameFileFilter(FILE_NAME_ENDINGS);
    }

    @Override
    public boolean getCountTargetFiles() {
        return clsPrefs.getBoolean(COUNT_TEXT_FILES, true);
    }

    @Override
    public void showFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        String url = file.toURI().toString();
        try {
            this.myEditorPane.setPage(url);
            this.myEditorPane.setCaretPosition(0);
        } catch (Exception ex) {
            System.out.println("Can't find file: " + url);
            ex.printStackTrace();
        }
    }

    @Override
    public JPopupMenu getPopupMenu(TreePath path) {
        return new ExtendedDirectoryTreePopUp(this.myLST.getTree(), path, getCountTargetFiles());
    }

    @Override
    public void scrollerChanged(ScrollerChangeEvent ce) {
        showFile(this.myLST.getScroller().getCurrentFile());
    }

    /**
	 * Put setVisable on the AWT Event Queue to avoid following error message:
	 * Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
	 * at javax.swing.plaf.basic.BasicSliderUI.calculateTrackBuffer(Unknown Source)
	 *
	 */
    @Override
    public void awtComplete() {
        showDefaultDirectory();
        this.myLoading.dispose();
        this.myLoading = null;
        System.out.println("\n" + "**************************************************** \n" + "*         JIBS Background Population Ended         * \n" + "**************************************************** \n");
    }

    private void buildAndDisplayInterface() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        this.setIconImage(Icons.getIcon(Icons.JIBS_16).getImage());
        setupToolBar();
        this.mySlider = new ScrollerSlider(SwingConstants.VERTICAL, this.myLST.getScroller());
        this.mySlider.setInverted(true);
        this.mySlider.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        Dimension size = new Dimension(700, 400);
        setSize(size);
        setTitle("JIBS TextPad");
        this.myEditorPane.setEditable(false);
        this.myEditorPane.setDisabledTextColor(Color.black);
        this.myEditorPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        JScrollPane center = new JScrollPane(this.myEditorPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.myTreePanel.setLeftComponent(center);
        this.myTreePanel.setRightComponent(new JScrollPane(this.myLST.getExtendedDirectoryTree()));
        JPanel sliders = new JPanel(new BorderLayout());
        sliders.add(new JScrollPane(this.myLST.getFileList()), BorderLayout.CENTER);
        sliders.add(new JScrollPane(this.mySlider), BorderLayout.WEST);
        this.myListPanel.setLeftComponent(sliders);
        this.myListPanel.setRightComponent(this.myTreePanel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(this.myToolBar, BorderLayout.NORTH);
        getContentPane().add(this.myListPanel, BorderLayout.CENTER);
        getContentPane().add(this.myLST.getStatusBarManager().getStatusPanel(), BorderLayout.SOUTH);
        this.setJMenuBar(this.makeMenus());
        this.myUImenu.initLookAndFeel();
        this.updateFont();
        SwingUtils.sizeScreen(this, .75f);
        validate();
        this.myListPanel.setDividerLocation(clsPrefs.getInt(FILE_PANEL_SIZE, FILE_MIN));
        this.myTreePanel.setDividerLocation(clsPrefs.getInt(TREE_PANEL_SIZE, TREE_MIN));
        SwingUtils.centerOnScreen(this);
        super.setVisible(true);
    }

    void setupToolBar() {
        this.myToolBar.add(this.myLST.getRDPmanager().getMoveManager().getUndoButton());
        this.myToolBar.add(this.myLST.getRDPmanager().getMoveManager().getRedoButton());
        this.myToolBar.addSeparator();
        this.myLST.getFileListManager().addButtonsToToolBar(this.myToolBar, this.myActions);
        this.myToolBar.add(this.myActions.getToolBarButton("view", "reload"));
        this.myToolBar.addSeparator();
        this.myToolBar.add(new JLabel("Font Size  "));
        this.myFontSize.setSelectedItem(clsPrefs.get(FONT_SIZE, "12"));
        this.myFontSize.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                TextPad.this.updateFont();
            }
        });
        this.myFontSize.setMaximumSize(new Dimension(20, 20));
        this.myToolBar.add(this.myFontSize);
        this.myToolBar.addSeparator();
        this.myToolBar.addSeparator();
        this.myToolBar.add(javax.swing.Box.createHorizontalGlue());
        this.myLST.getRDPmanager().addButtonsToToolbar(this.myToolBar);
        this.myToolBar.addSeparator();
        this.myToolBar.add(this.myActions.getToolBarButton("help", "about"));
        this.myToolBar.add(this.myActions.getToolBarButton("help", "help"));
    }

    void exit() {
        if (this.iNeedToFinalize) {
            this.iNeedToFinalize = false;
            clsPrefs.putInt(FILE_PANEL_SIZE, this.myListPanel.getDividerLocation());
            clsPrefs.putInt(TREE_PANEL_SIZE, this.myTreePanel.getDividerLocation());
            clsPrefs.put(FONT_SIZE, (String) this.myFontSize.getSelectedItem());
            try {
                clsPrefs.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            this.myLST.storeDirectories();
            this.myLST.getPathManager().flush();
            System.out.println("" + "**************************************************** \n" + "*           JIBS Text Pad Exited Normally          * \n" + "**************************************************** \n");
            System.out.println(AppPluginStartup.JIBS_OUT);
            AppStarter.closeDownApp();
            if (this.iExitOnClose) {
                System.exit(0);
            } else {
                this.setVisible(false);
                dispose();
            }
        }
    }

    void open() {
        String fileName = this.myLST.getTreeManager().getDTNReaderWriter().getFileName();
        JFileChooserExtra chooser = new JFileChooserExtra(clsPrefs.get(LAST_FILE, null));
        chooser.setSaveName(getAppIDandTargetType(), fileName);
        chooser.setDialogTitle(Jibs.getString("RecreateDirectories.20") + fileName);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File picked = chooser.getSelectedFile();
            if (picked == null || !picked.exists()) {
                return;
            }
            try {
                clsPrefs.put(LAST_FILE, picked.toString());
                clsPrefs.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            showFile(picked);
        }
    }

    JMenuBar makeMenus() {
        JMenu toolsMenu = new JMenu("Tools");
        class OptionsAction extends AbstractAction {

            @Override
            public void actionPerformed(ActionEvent e) {
                new TextPadOptionsDialog(TextPad.this);
            }
        }
        JMenuItem options = new JMenuItem("Options");
        toolsMenu.add(options);
        options.addActionListener(new OptionsAction());
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(this.myActions.getFileMenu());
        menuBar.add(this.myActions.getEditMenu());
        menuBar.add(this.myLST.getSortManager().getSortMenu());
        menuBar.add(this.myActions.getViewMenu());
        menuBar.add(toolsMenu);
        menuBar.add(this.myUImenu.populateUIMenu(ActionHelpers.makeMenu("ui")));
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(this.myActions.getHelpMenu());
        return menuBar;
    }

    void updateFont() {
        int fontSize = 12;
        try {
            fontSize = Integer.parseInt((String) this.myFontSize.getSelectedItem());
            Font font = new Font("Serif", Font.PLAIN, fontSize);
            this.myEditorPane.setFont(font);
        } catch (Exception ex) {
            this.myFontSize.setSelectedItem("12");
        }
    }

    private void showDefaultDirectory() {
        String dir = null;
        BasicOptionsManager bom = this.myLST.getBasicOptionsManager();
        switch(bom.getStartType()) {
            case BasicOptionsManager.START_PARTICULAR_DIRECTORY:
                dir = this.myLST.getPathManager().getDirectoryAbsolute(PathManager.DIR_START);
                try {
                    this.myLST.getFileListManager().setTargetDir(this.myLST.getPathManager().getDirectoryAbsolute(PathManager.DIR_START));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case BasicOptionsManager.START_LAST_VIEWED_DIRECTORY:
                dir = this.myLST.getPathManager().getDirectoryAbsolute(PathManager.DIR_LAST_VIEWED);
                break;
            case BasicOptionsManager.START_NO_DIRECTORY:
            default:
                break;
        }
        try {
            if (dir != null) {
                this.myLST.getFileListManager().setTargetDir(dir);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AppStarter.startUpApp(args, "org.gerhardb.jibs.Jibs", true);
        new TextPad(true);
    }
}
