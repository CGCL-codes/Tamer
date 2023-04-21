package com.jiexplorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import com.jiexplorer.db.JIThumbnailCache;
import com.jiexplorer.db.JIThumbnailService;
import com.jiexplorer.filetask.FileTask;
import com.jiexplorer.filetask.ProgressDialog;
import com.jiexplorer.gui.AbstractDiskObjectList;
import com.jiexplorer.gui.ButtonBar;
import com.jiexplorer.gui.DiskObjectTabInfo;
import com.jiexplorer.gui.StatusBarPanel;
import com.jiexplorer.gui.cattree.JICatTreePanel;
import com.jiexplorer.gui.datetree.JIDateTreePanel;
import com.jiexplorer.gui.keyword.JIKeyWordPane;
import com.jiexplorer.gui.preferences.JIPreferences;
import com.jiexplorer.gui.table.JITableListPane;
import com.jiexplorer.gui.thumb.JIThumbnailListPane;
import com.jiexplorer.gui.tree.JIDirTree;
import com.jiexplorer.gui.tree.JITreeNode;
import com.jiexplorer.util.DiskObject;
import com.jiexplorer.util.JIExplorerContext;
import com.jiexplorer.util.OrderedDiskObjectList;

public final class JIExplorer extends JFrame {

    /**
	 *
	 */
    private static final long serialVersionUID = -4037233623450799072L;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JIExplorer.class);

    public static final String APPNAME = "JIExplorer";

    public static final String VERSION = "1.15";

    private static JIExplorer instance;

    public static ImageIcon smallIcon;

    public static ImageIcon medIcon;

    public static String ROOT_FOLDER;

    public static String USER_ROOT;

    public static final int TABINDEX_DIRTREE = 0;

    public static final int TABINDEX_KEYWORDS = 1;

    public static final int TABINDEX_CATAGORY = 2;

    public static final int TABINDEX_DATE = 3;

    public static boolean loading = false;

    private JPanel jPanel;

    private BorderLayout borderLayout1;

    private JSplitPane jSplitPane;

    private JSplitPane jSplitPane2;

    private JIExplorerMenuBar jiExplorerMenuBar;

    private ButtonBar jAddressPanel;

    private DefaultTreeModel treeModel;

    private JIDirTree jTreeDisk;

    private JIKeyWordPane keywordPane;

    private JICatTreePanel catagoryPane;

    private JIDateTreePanel datePane;

    private JTabbedPane jTabPane;

    private DiskObjectTabInfo jiPreview;

    private AbstractDiskObjectList listScrollPane;

    private StatusBarPanel statusBar;

    private JIExplorerContext context;

    private JIExplorer() {
        super("JIExplorer");
        final long start = System.currentTimeMillis();
        log.debug("JIExplorer::JIExplorer() - " + start);
        smallIcon = new ImageIcon(JIExplorer.class.getResource("/icons/JIExplorerIcon32.gif"));
        medIcon = new ImageIcon(JIExplorer.class.getResource("/icons/JIExplorerIcon64.gif"));
        setIconImage(smallIcon.getImage());
        this.context = new JIExplorerContext();
        try {
            System.setSecurityManager(null);
            initGUI();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        log.debug("JIExplorer::JIExplorer() Finished - " + (System.currentTimeMillis() - start));
    }

    public static JIExplorer instance() {
        if (instance == null) {
            instance = new JIExplorer();
        }
        return instance;
    }

    public void resetPreferences() {
        this.listScrollPane.resetPreferences();
    }

    public JIDirTree getJDirTree() {
        return this.jTreeDisk;
    }

    public void addKeyWord(final String key) {
        this.keywordPane.insertKeyWord(key);
    }

    protected void close() {
        setVisible(false);
        JIThumbnailCache.getInstance().close();
        JIThumbnailService.getInstance().close();
        System.exit(0);
    }

    public void gotoLastDirectory() {
        this.jTreeDisk.gotoLastDirectory();
    }

    private final void initGUI() throws Exception {
        setBackground(Color.white);
        this.jPanel = new JPanel();
        this.borderLayout1 = new BorderLayout();
        this.jSplitPane = new JSplitPane();
        this.jSplitPane2 = new JSplitPane();
        this.jiExplorerMenuBar = new JIExplorerMenuBar();
        this.statusBar = StatusBarPanel.getInstance();
        this.jAddressPanel = new ButtonBar(this);
        this.treeModel = createTreeModel();
        this.jTreeDisk = new JIDirTree(this.treeModel);
        this.jTreeDisk.addObserver(this.statusBar);
        this.jTabPane = new JTabbedPane();
        this.jiPreview = new DiskObjectTabInfo();
        this.jTabPane.addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent e) {
                if (JIExplorer.this.jTabPane.getSelectedIndex() == TABINDEX_KEYWORDS) {
                    JIExplorer.this.keywordPane.updateKeyWords();
                    getContext().setState(JIExplorerContext.KEY_WORDS_STATE);
                }
                if (JIExplorer.this.jTabPane.getSelectedIndex() == TABINDEX_CATAGORY) {
                    JIExplorer.this.catagoryPane.reload();
                    getContext().setState(JIExplorerContext.CATEGORY_STATE);
                }
                if (JIExplorer.this.jTabPane.getSelectedIndex() == TABINDEX_DIRTREE) {
                    if ((getContext().getSelectedDirNodes() != null) && (JIExplorer.this.listScrollPane != null)) {
                        getContext().setState(JIExplorerContext.DIRECTORY_STATE);
                        JIExplorer.this.jTreeDisk.gotoLastDirectory();
                        JIExplorer.this.listScrollPane.reload();
                    }
                }
                if (JIExplorer.this.jTabPane.getSelectedIndex() == TABINDEX_DATE) {
                    JIExplorer.this.datePane.reload();
                    getContext().setState(JIExplorerContext.DATE_STATE);
                }
            }
        });
        this.jTabPane.add(new JScrollPane(this.jTreeDisk), "Dir", TABINDEX_DIRTREE);
        this.keywordPane = new JIKeyWordPane();
        this.jTabPane.add(this.keywordPane, "Key Words", TABINDEX_KEYWORDS);
        this.catagoryPane = new JICatTreePanel();
        this.jTabPane.add(this.catagoryPane, "Categories", TABINDEX_CATAGORY);
        this.datePane = new JIDateTreePanel();
        this.jTabPane.add(this.datePane, "Date", TABINDEX_DATE);
        prepareFileList();
        this.jSplitPane2.setBorder(null);
        this.jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        this.jSplitPane2.add(this.jTabPane, JSplitPane.TOP);
        this.jSplitPane2.add(this.jiPreview, JSplitPane.BOTTOM);
        this.jSplitPane.setDividerLocation(JIPreferences.getInstance().getHorizontalDividerLoc());
        this.jSplitPane.setBorder(null);
        this.jSplitPane.add(this.jSplitPane2, JSplitPane.LEFT);
        this.jSplitPane.setDividerLocation(JIPreferences.getInstance().getVerticalDividerLoc());
        this.jSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                final int divSize = JIExplorer.this.jSplitPane.getDividerSize();
                final int divLoc = JIExplorer.this.jSplitPane.getDividerLocation();
                final Dimension previewDim = new Dimension(JIExplorer.this.jSplitPane2.getWidth() - divSize, JIExplorer.this.jSplitPane2.getHeight() - divLoc - divSize);
                JIExplorer.this.jiPreview.setSize(previewDim);
                JIPreferences.getInstance().setVerticalDividerLoc(divLoc);
            }
        });
        this.jSplitPane2.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                final int divSize = JIExplorer.this.jSplitPane2.getDividerSize();
                final int divLoc = JIExplorer.this.jSplitPane2.getDividerLocation();
                final Dimension previewDim = new Dimension(JIExplorer.this.jSplitPane2.getWidth() - divSize, JIExplorer.this.jSplitPane2.getHeight() - divLoc - divSize);
                JIExplorer.this.jiPreview.setSize(previewDim);
                JIPreferences.getInstance().setHorizontalDividerLoc(divLoc);
            }
        });
        this.jSplitPane2.setDividerLocation(JIPreferences.getInstance().getHorizontalDividerLoc());
        this.jSplitPane.setDividerLocation(JIPreferences.getInstance().getVerticalDividerLoc());
        this.jPanel.setLayout(this.borderLayout1);
        this.jPanel.setPreferredSize(JIPreferences.getInstance().getBrowserDim());
        this.jPanel.add(this.jAddressPanel, BorderLayout.NORTH);
        this.jPanel.add(this.jSplitPane, BorderLayout.CENTER);
        this.jPanel.add(this.statusBar, BorderLayout.SOUTH);
        this.jPanel.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(final ComponentEvent e) {
                JIPreferences.getInstance().setBrowserDim(JIExplorer.this.jPanel.getSize().width, JIExplorer.this.jPanel.getSize().height);
            }
        });
        final Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1, 1));
        contentPane.add(this.jPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                close();
            }
        });
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(final ComponentEvent e) {
                JIPreferences.getInstance().setBrowserDim(JIExplorer.this.jPanel.getSize().width, JIExplorer.this.jPanel.getSize().height);
            }
        });
        setJMenuBar(this.jiExplorerMenuBar);
        pack();
    }

    public final void sortFileList() {
        DiskObject lastSelectedDObj = null;
        if (getContext().getLastSelectedDiskObjectIndex() > -1) {
            lastSelectedDObj = getContext().getLastSelectedDiskObj();
        }
        this.listScrollPane.getFileListModel().sort(JIPreferences.getInstance().getThumbnailSortOrder());
        if (getContext().getLastSelectedDiskObjectIndex() > -1) {
            if (this.listScrollPane instanceof JITableListPane) {
                final JIExplorerContext reloadContext = this.listScrollPane.getReloadContext();
                final OrderedDiskObjectList dObjList = this.listScrollPane.getFileListModel().getDiskObjectList();
                this.jSplitPane.remove(this.listScrollPane);
                this.listScrollPane = null;
                this.listScrollPane = new JITableListPane(dObjList);
                this.listScrollPane.setReloadContext(reloadContext);
                this.jSplitPane.add(this.listScrollPane, JSplitPane.RIGHT);
                this.listScrollPane.addObserver(this.jiPreview);
                this.listScrollPane.addObserver(this.statusBar);
                this.listScrollPane.addObserver(this.jAddressPanel);
                this.jTreeDisk.setJIList(this.listScrollPane);
                this.keywordPane.addObserver(this.listScrollPane);
                this.catagoryPane.addObserver(this.listScrollPane);
                this.datePane.addObserver(this.listScrollPane);
            }
            this.listScrollPane.restoreSelection(lastSelectedDObj);
        }
    }

    public final void prepareFileList() {
        if (this.listScrollPane != null) {
            final int lastSelectedIndex = getContext().getLastSelectedDiskObjectIndex();
            final JIExplorerContext reloadContext = this.listScrollPane.getReloadContext();
            final OrderedDiskObjectList dObjList = this.listScrollPane.getFileListModel().getDiskObjectList();
            DiskObject lastSelectedDObj = null;
            if (lastSelectedIndex > -1) {
                lastSelectedDObj = getContext().getLastSelectedDiskObj();
            }
            this.jSplitPane.remove(this.listScrollPane);
            this.listScrollPane = null;
            if (JIPreferences.getInstance().getImagesViewType().equals(JIPreferences.THUMB_VIEW)) {
                this.listScrollPane = new JIThumbnailListPane(this, dObjList);
            } else if (JIPreferences.getInstance().getImagesViewType().equals(JIPreferences.PREVIEW_VIEW)) {
                this.listScrollPane = new JIThumbnailListPane(this, 1, true, dObjList);
            } else {
                this.listScrollPane = new JITableListPane(dObjList);
            }
            this.listScrollPane.setReloadContext(reloadContext);
            this.jSplitPane.add(this.listScrollPane, JSplitPane.RIGHT);
            this.listScrollPane.addObserver(this.jiPreview);
            this.listScrollPane.addObserver(this.statusBar);
            this.listScrollPane.addObserver(this.jAddressPanel);
            this.jTreeDisk.setJIList(this.listScrollPane);
            if (lastSelectedIndex > -1) {
                this.listScrollPane.restoreSelection(lastSelectedDObj);
            }
        } else {
            this.listScrollPane = null;
            if (JIPreferences.getInstance().getImagesViewType().equals(JIPreferences.THUMB_VIEW)) {
                this.listScrollPane = new JIThumbnailListPane(this);
            } else if (JIPreferences.getInstance().getImagesViewType().equals(JIPreferences.PREVIEW_VIEW)) {
                this.listScrollPane = new JIThumbnailListPane(this, 1, true);
            } else {
                this.listScrollPane = new JITableListPane();
            }
            this.jSplitPane.add(this.listScrollPane, JSplitPane.RIGHT);
            this.listScrollPane.addObserver(this.jiPreview);
            this.listScrollPane.addObserver(this.statusBar);
            this.listScrollPane.addObserver(this.jAddressPanel);
            this.jTreeDisk.setJIList(this.listScrollPane);
        }
        this.keywordPane.addObserver(this.listScrollPane);
        this.catagoryPane.addObserver(this.listScrollPane);
        this.datePane.addObserver(this.listScrollPane);
    }

    private final DefaultTreeModel createTreeModel() {
        JITreeNode rootNode = null;
        final String osName = System.getProperty("os.name").toLowerCase();
        if (osName.toLowerCase().startsWith("windows")) {
            final File MY_COMPUTER_FOLDER_FILE = new File(ROOT_FOLDER);
            MY_COMPUTER_FOLDER_FILE.mkdirs();
            MY_COMPUTER_FOLDER_FILE.deleteOnExit();
            rootNode = new JITreeNode(MY_COMPUTER_FOLDER_FILE);
            rootNode.setRoot(true);
            rootNode.explore();
        } else {
            final File rootFile = new File(ROOT_FOLDER);
            rootNode = new JITreeNode(rootFile);
            rootNode.setRoot(true);
            rootNode.explore();
        }
        return new DefaultTreeModel(rootNode);
    }

    /**
	 * @return the context
	 */
    public final synchronized JIExplorerContext getContext() {
        return this.context;
    }

    /**
	 * @param context the context to set
	 */
    public final synchronized void setContext(final JIExplorerContext context) {
        this.context = context;
    }

    public final void runAction(final FileTask action) {
        final ProgressDialog pd = new ProgressDialog((JIExplorer.this), action.getOperationName(), action, null);
        pd.run();
        this.listScrollPane.reload();
    }
}
