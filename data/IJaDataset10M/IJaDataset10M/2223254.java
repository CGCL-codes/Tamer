package com.ademille.hvthelper.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import net.sf.jasperreports.engine.JRException;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;
import com.ademille.hvthelper.Version;
import com.ademille.hvthelper.gui.dialogs.HTVTSelectionDialog;
import com.ademille.hvthelper.gui.dialogs.ReportOptionsDialog;
import com.ademille.hvthelper.gui.logging.LogTable;
import com.ademille.hvthelper.gui.reports.AssignmentReport;
import com.ademille.hvthelper.gui.reports.CompanionReport;
import com.ademille.hvthelper.gui.reports.CursorIconControl;
import com.ademille.hvthelper.gui.reports.DistrictReport;
import com.ademille.hvthelper.gui.reports.UnassignedReport;
import com.ademille.hvthelper.gui.treenodes.DirtyListener;
import com.ademille.hvthelper.gui.treenodes.HVTHelperNode;
import com.ademille.hvthelper.gui.treenodes.TopNode;
import com.ademille.hvthelper.gui.util.CenteringFileChooser;
import com.ademille.hvthelper.gui.util.HVTAction;
import com.ademille.hvthelper.gui.util.ImageUtil;
import com.ademille.hvthelper.gui.util.HTHPreferences;
import com.ademille.hvthelper.gui.util.SortedLinkedList;
import com.ademille.hvthelper.gui.util.SortedLinkedListConverter;
import com.ademille.hvthelper.gui.wizards.exporthtml.ExportFinish;
import com.ademille.hvthelper.gui.wizards.exporthtml.FormatPage;
import com.ademille.hvthelper.gui.wizards.exporthtml.OutputDirPage;
import com.ademille.hvthelper.model.Companionship;
import com.ademille.hvthelper.model.District;
import com.ademille.hvthelper.model.Family;
import com.ademille.hvthelper.model.HVTeachingInfo;
import com.ademille.hvthelper.model.Quorum;
import com.ademille.hvthelper.model.Supervisor;
import com.ademille.hvthelper.model.Teacher;
import com.ademille.hvthelper.model.TeachingAssignmentInfo;
import com.ademille.hvthelper.parser.MLSParser;
import com.ademille.hvthelper.parser.ParserException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class HVTHelperGUI extends JFrame implements TreeSelectionListener, DirtyListener, CursorIconControl {

    private static final long serialVersionUID = 1L;

    private JPanel guiContentPane = null;

    private JMenuBar menuBar = null;

    private JMenu mnuFile = null;

    private JMenuItem mnuImport = null;

    private JMenuItem mnuExport = null;

    private JScrollPane scrollPaneTree = null;

    private JTree treeAssignments = null;

    private JPanel panelMain = null;

    private JSplitPane splitPaneTree = null;

    private JSplitPane splitPaneInfo = null;

    private JScrollPane scrollPaneLog = null;

    private JMenuItem mnuExit = null;

    private JScrollPane scrollPaneFamilies = null;

    private JTable tableFamilies = null;

    private static final Logger logger = Logger.getLogger("com.ademille.hvthelper");

    private JToolBar toolBarTop = null;

    private JButton btnImport = null;

    private JButton btnExport = null;

    private Action saveAction;

    private Action openAction;

    private Action importAction;

    private Action exportAction;

    private JPanel panelTree = null;

    private JPanel panelTreeSearch = null;

    private JTextField txtTreeSearch = null;

    private JButton btnDown = null;

    private JButton btnUp = null;

    private JMenu mnuHelp = null;

    private JMenuItem mnuAbout = null;

    private String aboutMessage;

    private JButton btnSearch = null;

    private List<HVTHelperNode> searchResults = null;

    private int searchPos = 0;

    private JLabel lblSearchStatus = null;

    private JPanel panelSearchStatus = null;

    private JPanel panelActions = null;

    private JPopupMenu treePopupMenu;

    private XStream xstream;

    private enum SearchResult {

        NEXT, PREVIOUS
    }

    ;

    private HVTeachingInfo hvtInfo;

    private String fileName = "";

    private boolean dirty = false;

    private static String title = "Home Teaching Helper " + Version.major + "." + Version.minor + "." + Version.bugfix;

    private JMenuItem mnuOpen = null;

    private JMenu mnuRecentFiles = null;

    private JMenuItem mnuSave = null;

    private JMenuItem mnuSaveAs = null;

    private LogTable logTable = null;

    private JMenu mnuReports = null;

    private JMenuItem mnuHTAssignments = null;

    private JMenuItem mnuUnassigned = null;

    private JMenuItem mnuNew = null;

    private JMenuItem mnuSupervisorReport = null;

    private HTHPreferences prefs;

    private JMenuItem mnuCompanionships;

    private JButton btnOpen;

    private JButton btnSave;

    private class NodeRenderer extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            HVTHelperNode node = (HVTHelperNode) value;
            ImageIcon icon = node.getIcon();
            if (icon != null) {
                setIcon(icon);
            }
            return this;
        }
    }

    private class XmlFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String name = f.getName();
            if (name.toLowerCase().endsWith(".xml")) {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "Home Teaching Helper Files (XML)";
        }
    }

    /**
	 * This is the default constructor
	 */
    public HVTHelperGUI() {
        super();
        prefs = new HTHPreferences();
        xstream = new XStream(new DomDriver());
        xstream.alias("teacher", Teacher.class);
        xstream.alias("family", Family.class);
        xstream.alias("supervisor", Supervisor.class);
        xstream.alias("companionship", Companionship.class);
        xstream.alias("quorum", Quorum.class);
        xstream.alias("hvteachinginfo", HVTeachingInfo.class);
        xstream.registerConverter(new SortedLinkedListConverter(xstream.getMapper()));
        createActions();
        initialize();
        createStartupModel();
        populateTree(hvtInfo);
        HVTHelperNode.setDirtyListener(this);
        setDirty(false);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });
    }

    private TeachingAssignmentInfo createHTStartupModel() {
        TeachingAssignmentInfo htInfo = new TeachingAssignmentInfo();
        htInfo.setHometeaching(true);
        Quorum eldersQuorum = new Quorum("Elders");
        Quorum highPriestQuorum = new Quorum("High Priests");
        List<Quorum> qList = new SortedLinkedList<Quorum>();
        qList.add(eldersQuorum);
        qList.add(highPriestQuorum);
        htInfo.setQuorums(qList);
        htInfo.setUnassigned(new SortedLinkedList<Family>());
        return htInfo;
    }

    private TeachingAssignmentInfo createVTStartupModel() {
        TeachingAssignmentInfo vtInfo = new TeachingAssignmentInfo();
        vtInfo.setHometeaching(false);
        Quorum reliefSocietyQuorum = new Quorum("Districts");
        List<Quorum> qList = new SortedLinkedList<Quorum>();
        qList.add(reliefSocietyQuorum);
        vtInfo.setQuorums(qList);
        vtInfo.setUnassigned(new SortedLinkedList<Family>());
        return vtInfo;
    }

    private void createStartupModel() {
        TeachingAssignmentInfo htInfo = createHTStartupModel();
        TeachingAssignmentInfo vtInfo = createVTStartupModel();
        hvtInfo = new HVTeachingInfo(htInfo, vtInfo);
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(814, 546);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/ademille/hvthelper/gui/icons/house.png")));
        this.setJMenuBar(getAppMenuBar());
        this.setContentPane(getGuiContentPane());
        this.setTitle(title);
        this.setLocationRelativeTo(null);
        logger.addAppender(logTable.getAppender());
        this.aboutMessage = "<html><center><b>Home Teaching Helper</b><br>" + Version.major + "." + Version.minor + "." + Version.bugfix + "<br>" + "Copyright 2008-2011<br> Aaron DeMille<br>" + "Build Date: " + Version.buildTime + "<br>" + "ajdemille@gmail.com</center><br><br>" + "Feel free to send me an email with comments or suggestions.<br>" + "I'd love to hear where and how this program is used.<br><br>" + "If you like the program, you can give positive feedback at <br>" + "<a href='http://sourceforge.net/projects/hvthelper'>http://sourceforge.net/projects/hvthelper</a><br><br>" + "Some icons provided by:<br>" + "http://www.famfamfam.com/lab/icons/silk/" + "</html>";
    }

    private void setTitleWithFileName() {
        String title = HVTHelperGUI.title;
        if (fileName != null && !"".equals(fileName)) {
            title += " - " + fileName;
        }
        if (isDirty()) {
            title += " *";
        }
        setTitle(title);
    }

    private void createActions() {
        saveAction = new HVTAction("Save", ImageUtil.createIcon("save_edit.gif"), "Save the Current Assignments", new Integer(KeyEvent.VK_S), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(false);
            }
        });
        openAction = new HVTAction("Open", ImageUtil.createIcon("fldr_obj.gif"), "Open an Assignments File", new Integer(KeyEvent.VK_O), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDirty()) {
                    int result = JOptionPane.showConfirmDialog(HVTHelperGUI.this, "You have unsaved changes. Would you like to save before opening a new file?", "Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.YES_OPTION && saveFile(false) || result == JOptionPane.NO_OPTION) {
                        openFile();
                    }
                } else {
                    openFile();
                }
            }
        });
        importAction = new HVTAction("Import MLS Data", ImageUtil.createIcon("import.gif"), "Import MLS Data from a Directory", new Integer(KeyEvent.VK_I), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDirty()) {
                    int result = JOptionPane.showConfirmDialog(HVTHelperGUI.this, "You have unsaved changes. Would you like to save before importing new Data?", "Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.YES_OPTION && saveFile(false) || result == JOptionPane.NO_OPTION) {
                        importMlsData();
                    }
                } else {
                    importMlsData();
                }
            }
        });
        exportAction = new HVTAction("Export Data", ImageUtil.createIcon("export.gif"), "Export Data to various formats", new Integer(KeyEvent.VK_E), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exportDataWizard();
            }
        });
    }

    private List<TreePath> getTreePathsForDepth(TreeNode root, int depth) {
        List<TreePath> paths = new LinkedList<TreePath>();
        if (root.getChildCount() == 0) {
            TreePath path = HVTHelperNode.getPath(root.getParent());
            paths.add(path);
        } else if (depth == 0) {
            TreePath path = HVTHelperNode.getPath(root);
            paths.add(path);
        } else {
            for (int i = 0; i < root.getChildCount(); i++) {
                List<TreePath> childPaths = getTreePathsForDepth(root.getChildAt(i), depth - 1);
                paths.addAll(childPaths);
            }
        }
        return paths;
    }

    private void expandTree() {
        List<TreePath> paths = getTreePathsForDepth((TreeNode) treeAssignments.getModel().getRoot(), 1);
        for (TreePath t : paths) {
            treeAssignments.expandPath(t);
        }
        treeAssignments.setSelectionRow(0);
    }

    /**
	 * This method initializes guiContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getGuiContentPane() {
        if (guiContentPane == null) {
            guiContentPane = new JPanel();
            guiContentPane.setLayout(new BorderLayout());
            guiContentPane.add(getSplitPaneTree(), BorderLayout.CENTER);
            guiContentPane.add(getToolBarTop(), BorderLayout.NORTH);
        }
        return guiContentPane;
    }

    /**
	 * This method initializes menuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
    private JMenuBar getAppMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            menuBar.add(getMnuFile());
            menuBar.add(getMnuReports());
            menuBar.add(getMnuHelp());
        }
        return menuBar;
    }

    /**
	 * This method initializes mnuFile
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getMnuFile() {
        if (mnuFile == null) {
            mnuFile = new JMenu();
            mnuFile.setText("File");
            mnuFile.setMnemonic(KeyEvent.VK_F);
            mnuFile.add(getMnuNew());
            mnuFile.add(getMnuOpen());
            mnuFile.add(getMnuSave());
            mnuFile.add(getMnuSaveAs());
            mnuFile.add(getMnuImport());
            mnuFile.add(getMnuExport());
            mnuFile.add(getMnuExit());
        }
        return mnuFile;
    }

    /**
	 * This method initializes mnuImport
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getMnuImport() {
        if (mnuImport == null) {
            mnuImport = new JMenuItem();
            mnuImport.setAction(importAction);
        }
        return mnuImport;
    }

    /**
	 * Read a directory that contains MLS Data and import it.
	 */
    private boolean importMlsData() {
        boolean imported = false;
        final JFileChooser fc = new CenteringFileChooser();
        fc.setApproveButtonText("Import");
        fc.setDialogTitle("Select Directory Containing MLS Data");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String lastPath = prefs.getLastImportPath();
        if (lastPath != null) fc.setCurrentDirectory(new File(lastPath));
        int retVal = fc.showOpenDialog(this);
        if (JOptionPane.OK_OPTION == retVal) {
            waitCursor();
            try {
                String inputDir = fc.getSelectedFile().getCanonicalPath();
                logger.info("Selected directory:" + inputDir);
                MLSParser parser = new MLSParser();
                try {
                    parser.parseFiles(inputDir);
                    hvtInfo = parser.getHVTeachingInfo();
                    if (hvtInfo == null) {
                        createStartupModel();
                    } else {
                        TeachingAssignmentInfo htInfo = hvtInfo.getHtInfo();
                        TeachingAssignmentInfo vtInfo = hvtInfo.getVtInfo();
                        if (hvtInfo.getHtInfo() == null) {
                            htInfo = createHTStartupModel();
                        }
                        if (hvtInfo.getVtInfo() == null) {
                            vtInfo = createVTStartupModel();
                        }
                        hvtInfo = new HVTeachingInfo(htInfo, vtInfo);
                    }
                    populateTree(hvtInfo);
                    imported = true;
                } catch (ParserException e) {
                    logger.error("Unable to parse files:" + e.getMessage());
                }
                prefs.setLastImportPath(inputDir);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        normalCursor();
        if (imported) {
            fileName = "";
            expandTree();
            setDirty(true);
        }
        return imported;
    }

    private void exportDataWizard() {
        @SuppressWarnings("rawtypes") Class[] clazz = new Class[] { FormatPage.class, OutputDirPage.class };
        WizardResultProducer finishIt = new ExportFinish(hvtInfo);
        Point p = getLocation();
        int width = getWidth();
        int height = getHeight();
        int wizHeight = 300;
        int wizWidth = 600;
        int x = p.x + (width / 2) - (wizWidth / 2);
        int y = p.y + (height / 2) - (wizHeight / 2);
        WizardDisplayer.showWizard(WizardPage.createWizard(clazz, finishIt), new Rectangle(x, y, wizWidth, wizHeight));
    }

    private void populateTree(HVTeachingInfo info) {
        HVTHelperNode top = new TopNode(info);
        DefaultTreeModel model = new DefaultTreeModel(top);
        HVTHelperNode.setModel(model);
        HVTHelperNode.setTree(treeAssignments);
        HVTHelperNode.setTopFrame(this);
        top.populate();
        treeAssignments.setModel(model);
        expandTree();
    }

    /**
	 * This method initializes mnuExport
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getMnuExport() {
        if (mnuExport == null) {
            mnuExport = new JMenuItem();
            mnuExport.setAction(exportAction);
        }
        return mnuExport;
    }

    /**
	 * This method initializes scrollPaneTree
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getScrollPaneTree() {
        if (scrollPaneTree == null) {
            scrollPaneTree = new JScrollPane();
            scrollPaneTree.setViewportView(getTreeAssignments());
        }
        return scrollPaneTree;
    }

    /**
	 * This method initializes treeAssignments
	 * 
	 * @return javax.swing.JTree
	 */
    private JTree getTreeAssignments() {
        if (treeAssignments == null) {
            treeAssignments = new JTree();
            treeAssignments.setModel(new DefaultTreeModel(new TopNode(null)));
            treeAssignments.setShowsRootHandles(true);
            treeAssignments.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            treeAssignments.addTreeSelectionListener(this);
            treeAssignments.setCellRenderer(new NodeRenderer());
            treeAssignments.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mousePressed(java.awt.event.MouseEvent e) {
                    showContextMenu(e);
                }

                public void mouseReleased(java.awt.event.MouseEvent e) {
                    showContextMenu(e);
                }
            });
        }
        return treeAssignments;
    }

    private void showContextMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            TreePath selPath = treeAssignments.getPathForLocation(e.getX(), e.getY());
            treeAssignments.setSelectionPath(selPath);
            if (null != treePopupMenu) {
                treePopupMenu.show((Component) e.getSource(), e.getX(), e.getY());
            }
        }
    }

    /**
	 * This method initializes panelMain
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getPanelMain() {
        if (panelMain == null) {
            panelMain = new JPanel();
            panelMain.setLayout(new BorderLayout());
            panelMain.add(getScrollPaneFamilies(), BorderLayout.CENTER);
        }
        return panelMain;
    }

    /**
	 * This method initializes splitPaneTree
	 * 
	 * @return javax.swing.JSplitPane
	 */
    private JSplitPane getSplitPaneTree() {
        if (splitPaneTree == null) {
            splitPaneTree = new JSplitPane();
            splitPaneTree.setDividerLocation(250);
            splitPaneTree.setLeftComponent(getPanelTree());
            splitPaneTree.setRightComponent(getSplitPaneInfo());
        }
        return splitPaneTree;
    }

    /**
	 * This method initializes splitPaneInfo
	 * 
	 * @return javax.swing.JSplitPane
	 */
    private JSplitPane getSplitPaneInfo() {
        if (splitPaneInfo == null) {
            splitPaneInfo = new JSplitPane();
            splitPaneInfo.setOrientation(JSplitPane.VERTICAL_SPLIT);
            splitPaneInfo.setDividerLocation(350);
            splitPaneInfo.setResizeWeight(0.85D);
            splitPaneInfo.setPreferredSize(new Dimension(555, 480));
            splitPaneInfo.setBottomComponent(getScrollPaneLog());
            splitPaneInfo.setTopComponent(getPanelMain());
        }
        return splitPaneInfo;
    }

    /**
	 * This method initializes scrollPaneLog
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getScrollPaneLog() {
        if (scrollPaneLog == null) {
            scrollPaneLog = new JScrollPane();
            scrollPaneLog.setViewportView(getLogTable());
        }
        return scrollPaneLog;
    }

    /**
	 * This method initializes mnuExit
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getMnuExit() {
        if (mnuExit == null) {
            mnuExit = new JMenuItem();
            mnuExit.setText("Exit");
            mnuExit.setMnemonic(KeyEvent.VK_X);
            mnuExit.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    exitApp();
                }
            });
        }
        return mnuExit;
    }

    /**
	 * This method initializes scrollPaneFamilies
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getScrollPaneFamilies() {
        if (scrollPaneFamilies == null) {
            scrollPaneFamilies = new JScrollPane();
            scrollPaneFamilies.setViewportView(getTableFamilies());
        }
        return scrollPaneFamilies;
    }

    /**
	 * This method initializes tableFamilies
	 * 
	 * @return javax.swing.JTable
	 */
    private JTable getTableFamilies() {
        if (tableFamilies == null) {
            tableFamilies = new JTable();
            tableFamilies.setColumnSelectionAllowed(true);
        }
        return tableFamilies;
    }

    /**
	 * This method initializes toolBarTop
	 * 
	 * @return javax.swing.JToolBar
	 */
    private JToolBar getToolBarTop() {
        if (toolBarTop == null) {
            toolBarTop = new JToolBar();
            toolBarTop.add(getBtnOpen());
            toolBarTop.add(getBtnSave());
            toolBarTop.addSeparator();
            toolBarTop.add(getBtnImport());
            toolBarTop.add(getBtnExport());
            toolBarTop.addSeparator();
            toolBarTop.add(getPanelActions());
        }
        return toolBarTop;
    }

    private JButton getBtnOpen() {
        if (btnOpen == null) {
            btnOpen = new JButton("Open");
            btnOpen.setAction(openAction);
            guiContentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK), "openFile");
            guiContentPane.getActionMap().put("openFile", openAction);
        }
        return btnOpen;
    }

    private JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton("Save");
            btnSave.setAction(saveAction);
            guiContentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK), "saveFile");
            guiContentPane.getActionMap().put("saveFile", saveAction);
        }
        return btnSave;
    }

    /**
	 * This method initializes btnImport
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getBtnImport() {
        if (btnImport == null) {
            btnImport = new JButton();
            btnImport.setAction(importAction);
        }
        return btnImport;
    }

    /**
	 * This method initializes btnExport
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getBtnExport() {
        if (btnExport == null) {
            btnExport = new JButton();
            btnExport.setAction(exportAction);
        }
        return btnExport;
    }

    /**
	 * This method initializes panelTree
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getPanelTree() {
        if (panelTree == null) {
            panelTree = new JPanel();
            panelTree.setLayout(new BorderLayout());
            panelTree.add(getScrollPaneTree(), BorderLayout.CENTER);
            panelTree.add(getPanelTreeSearch(), BorderLayout.SOUTH);
        }
        return panelTree;
    }

    /**
	 * This method initializes panelTreeSearch
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getPanelTreeSearch() {
        if (panelTreeSearch == null) {
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.anchor = GridBagConstraints.WEST;
            gridBagConstraints31.gridy = 1;
            gridBagConstraints31.gridwidth = 4;
            gridBagConstraints31.gridx = 0;
            lblSearchStatus = new JLabel();
            lblSearchStatus.setText("");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.gridx = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.gridx = 3;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.weightx = 1.0;
            panelTreeSearch = new JPanel();
            panelTreeSearch.setLayout(new GridBagLayout());
            panelTreeSearch.setVisible(true);
            panelTreeSearch.add(getTxtTreeSearch(), gridBagConstraints1);
            panelTreeSearch.add(getBtnSearch(), gridBagConstraints2);
            panelTreeSearch.add(getBtnUp(), gridBagConstraints3);
            panelTreeSearch.add(getBtnDown(), gridBagConstraints4);
            panelTreeSearch.add(getPanelSearchStatus(), gridBagConstraints31);
        }
        return panelTreeSearch;
    }

    /**
	 * This method initializes txtTreeSearch
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getTxtTreeSearch() {
        if (txtTreeSearch == null) {
            txtTreeSearch = new JTextField();
            txtTreeSearch.setPreferredSize(new Dimension(150, 25));
            txtTreeSearch.setText("");
        }
        return txtTreeSearch;
    }

    /**
	 * This method initializes btnDown
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getBtnDown() {
        if (btnDown == null) {
            btnDown = new JButton();
            btnDown.setText("");
            btnDown.setPreferredSize(new Dimension(25, 26));
            btnDown.setEnabled(false);
            btnDown.setIcon(new ImageIcon(getClass().getResource("/com/ademille/hvthelper/gui/icons/down.png")));
            btnDown.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showSearchNode(SearchResult.NEXT);
                }
            });
        }
        return btnDown;
    }

    /**
	 * This method initializes btnUp
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getBtnUp() {
        if (btnUp == null) {
            btnUp = new JButton();
            btnUp.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    showSearchNode(SearchResult.PREVIOUS);
                }
            });
            btnUp.setText("");
            btnUp.setPreferredSize(new Dimension(25, 26));
            btnUp.setEnabled(false);
            btnUp.setIcon(new ImageIcon(getClass().getResource("/com/ademille/hvthelper/gui/icons/up.png")));
        }
        return btnUp;
    }

    /**
	 * This method initializes mnuHelp
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getMnuHelp() {
        if (mnuHelp == null) {
            mnuHelp = new JMenu();
            mnuHelp.setText("Help");
            mnuHelp.setMnemonic(KeyEvent.VK_H);
            mnuHelp.add(getMnuAbout());
        }
        return mnuHelp;
    }

    /**
	 * This method initializes mnuAbout
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getMnuAbout() {
        if (mnuAbout == null) {
            mnuAbout = new JMenuItem();
            mnuAbout.setText("About");
            mnuAbout.setMnemonic(KeyEvent.VK_A);
            mnuAbout.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JOptionPane.showMessageDialog(HVTHelperGUI.this, aboutMessage);
                }
            });
        }
        return mnuAbout;
    }

    /**
	 * This method initializes btnSearch
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getBtnSearch() {
        if (btnSearch == null) {
            btnSearch = new JButton();
            btnSearch.setIcon(new ImageIcon(getClass().getResource("/com/ademille/hvthelper/gui/icons/magnifier.png")));
            btnSearch.setPreferredSize(new Dimension(25, 26));
            btnSearch.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (txtTreeSearch.getText().trim().equals("")) {
                        lblSearchStatus.setText("<html><font color=red>Please enter text to search for first.</font></html>");
                    } else {
                        searchResults = searchNode(txtTreeSearch.getText());
                        searchPos = -1;
                        showSearchNode(SearchResult.NEXT);
                        btnDown.grabFocus();
                    }
                }
            });
        }
        return btnSearch;
    }

    private void showSearchNode(SearchResult type) {
        DefaultTreeModel model = (DefaultTreeModel) treeAssignments.getModel();
        HVTHelperNode node = null;
        if (null != searchResults && searchResults.size() > 0) {
            if (SearchResult.NEXT == type) {
                if (searchPos + 1 < searchResults.size()) {
                    node = searchResults.get(++searchPos);
                }
            } else if (SearchResult.PREVIOUS == type) {
                if (searchPos - 1 >= 0) {
                    node = searchResults.get(--searchPos);
                }
            }
            btnDown.setEnabled(searchPos + 1 < searchResults.size());
            btnUp.setEnabled(searchPos - 1 >= 0);
            if (null != node) {
                TreeNode[] nodes = model.getPathToRoot(node);
                TreePath path = new TreePath(nodes);
                treeAssignments.scrollPathToVisible(path);
                treeAssignments.setSelectionPath(path);
            }
            lblSearchStatus.setText("Search Result: " + (searchPos + 1) + " of " + searchResults.size());
        } else {
            lblSearchStatus.setText("<html><font color=red>Search term not found.</font></html>");
        }
    }

    @SuppressWarnings("unchecked")
    private List<HVTHelperNode> searchNode(String str) {
        HVTHelperNode node = null;
        HVTHelperNode root = (HVTHelperNode) treeAssignments.getModel().getRoot();
        List<HVTHelperNode> nodeList = new LinkedList<HVTHelperNode>();
        Enumeration<HVTHelperNode> en = root.depthFirstEnumeration();
        str = str.toLowerCase();
        while (en.hasMoreElements()) {
            node = en.nextElement();
            String nodeName = node.getUserObject().toString().toLowerCase();
            if (nodeName.contains(str)) {
                nodeList.add(node);
            }
        }
        return nodeList;
    }

    /**
	 * This method initializes panelSearchStatus
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getPanelSearchStatus() {
        if (panelSearchStatus == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.gridx = -1;
            gridBagConstraints6.gridy = -1;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.gridwidth = 3;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.anchor = GridBagConstraints.WEST;
            gridBagConstraints5.gridwidth = 1;
            gridBagConstraints5.gridx = -1;
            gridBagConstraints5.gridy = -1;
            gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
            panelSearchStatus = new JPanel();
            panelSearchStatus.setLayout(new FlowLayout());
            panelSearchStatus.add(lblSearchStatus, null);
        }
        return panelSearchStatus;
    }

    /**
	 * This method initializes panelActions
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getPanelActions() {
        if (panelActions == null) {
            panelActions = new JPanel();
            panelActions.setLayout(new BoxLayout(getPanelActions(), BoxLayout.X_AXIS));
        }
        return panelActions;
    }

    /**
	 * This method initializes mnuOpen
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getMnuOpen() {
        if (mnuOpen == null) {
            mnuOpen = new JMenuItem();
            mnuOpen.setMnemonic(KeyEvent.VK_O);
            mnuOpen.setText("Open");
            mnuOpen.setAction(openAction);
        }
        return mnuOpen;
    }

    private File selectFile(boolean saveDialog) {
        return selectFile(saveDialog, false);
    }

    private File selectFile(boolean saveDialog, boolean saveAs) {
        final JFileChooser fc = new CenteringFileChooser();
        String lastPath = prefs.getLastFilePath();
        if (lastPath != null) fc.setCurrentDirectory(new File(lastPath));
        fc.addChoosableFileFilter(new XmlFilter());
        File f = null;
        if (saveAs) fc.setDialogTitle("Save As ...");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retVal = saveDialog ? fc.showSaveDialog(this) : fc.showOpenDialog(this);
        if (JOptionPane.OK_OPTION == retVal) {
            try {
                String inputFile = fc.getSelectedFile().getCanonicalPath();
                f = new File(inputFile);
                logger.info("Selected file:" + inputFile);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        if (f != null) {
            String fileName = f.getParent() + System.getProperty("file.separator");
            prefs.setLastFilePath(fileName);
            prefs.addRecentFile(f.getAbsolutePath());
        }
        return f;
    }

    private JMenu getMnuRecentFiles() {
        if (mnuRecentFiles == null) {
            mnuRecentFiles = new JMenu();
            mnuRecentFiles.setText("Recent Files");
            String fileName = null;
            int mruIndex = 0;
            while ((fileName = prefs.getRecentFile(mruIndex)) != null) {
                JMenuItem mruMenu = new JMenuItem(fileName);
                mnuRecentFiles.add(mruMenu);
                mruIndex++;
            }
        }
        return mnuRecentFiles;
    }

    /**
	 * This method initializes mnuSave
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getMnuSave() {
        if (mnuSave == null) {
            mnuSave = new JMenuItem();
            mnuSave.setMnemonic(KeyEvent.VK_S);
            mnuSave.setText("Save");
            mnuSave.setAction(saveAction);
        }
        return mnuSave;
    }

    /**
	 * This method initializes mnuSaveAs	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getMnuSaveAs() {
        if (mnuSaveAs == null) {
            mnuSaveAs = new JMenuItem();
            mnuSaveAs.setName("");
            mnuSaveAs.setText("Save As");
            mnuSaveAs.setMnemonic(KeyEvent.VK_A);
            mnuSaveAs.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    saveFile(true);
                }
            });
        }
        return mnuSaveAs;
    }

    /**
	 * This method initializes logTable	
	 * 	
	 * @return com.ademille.hvthelper.gui.logging.LogTable	
	 */
    private LogTable getLogTable() {
        if (logTable == null) {
            logTable = new LogTable();
        }
        return logTable;
    }

    /**
	 * This method initializes mnuReports	
	 * 	
	 * @return javax.swing.JMenu	
	 */
    private JMenu getMnuReports() {
        if (mnuReports == null) {
            mnuReports = new JMenu();
            mnuReports.setMnemonic(KeyEvent.VK_R);
            mnuReports.setText("Reports");
            mnuReports.add(getMnuSupervisorReport());
            mnuReports.add(getMnuCompanionships());
            mnuReports.add(getMnuHTAssignments());
            mnuReports.add(getMnuUnassigned());
        }
        return mnuReports;
    }

    /**
	 * This method initializes mnuHTAssignments	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getMnuHTAssignments() {
        if (mnuHTAssignments == null) {
            mnuHTAssignments = new JMenuItem();
            mnuHTAssignments.setText("Teaching Assignments");
            mnuHTAssignments.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        final ReportOptionsDialog dlg = new ReportOptionsDialog(HVTHelperGUI.this);
                        dlg.setModal(true);
                        dlg.setVisible(true);
                        if (!dlg.isCancelled()) {
                            final AssignmentReport visitReport = new AssignmentReport(dlg.getGroupType());
                            visitReport.showReport(hvtInfo, HVTHelperGUI.this);
                        }
                    } catch (JRException ex) {
                        logger.error(ex);
                    }
                }
            });
        }
        return mnuHTAssignments;
    }

    private JMenuItem getMnuUnassigned() {
        if (mnuUnassigned == null) {
            mnuUnassigned = new JMenuItem();
            mnuUnassigned.setText("Unassigned List");
            mnuUnassigned.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        final HTVTSelectionDialog dlg = new HTVTSelectionDialog(HVTHelperGUI.this);
                        dlg.setModal(true);
                        dlg.setVisible(true);
                        if (!dlg.isCancelled()) {
                            final UnassignedReport report = new UnassignedReport(dlg.isHomeTeaching());
                            report.showReport(hvtInfo, HVTHelperGUI.this);
                        }
                    } catch (JRException ex) {
                        logger.error(ex);
                    }
                }
            });
        }
        return mnuUnassigned;
    }

    private JMenuItem getMnuCompanionships() {
        if (mnuCompanionships == null) {
            mnuCompanionships = new JMenuItem("Companionships");
            mnuCompanionships.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        final ReportOptionsDialog dlg = new ReportOptionsDialog(HVTHelperGUI.this);
                        dlg.setModal(true);
                        dlg.setVisible(true);
                        if (!dlg.isCancelled()) {
                            final CompanionReport visitReport = new CompanionReport(dlg.getGroupType());
                            visitReport.showReport(hvtInfo, HVTHelperGUI.this);
                        }
                    } catch (JRException ex) {
                        System.err.println(ex);
                        logger.error(ex);
                    }
                }
            });
        }
        return mnuCompanionships;
    }

    /**
	 * This method initializes mnuNew	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getMnuNew() {
        if (mnuNew == null) {
            mnuNew = new JMenuItem();
            mnuNew.setText("New");
            mnuNew.setMnemonic(KeyEvent.VK_N);
            mnuNew.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (isDirty()) {
                        int result = JOptionPane.showConfirmDialog(HVTHelperGUI.this, "You have unsaved changes. Would you like to save before starting a new assignments list?", "Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (result == JOptionPane.YES_OPTION && saveFile(false) || result == JOptionPane.NO_OPTION) {
                            clearAssignments();
                        }
                    } else {
                        clearAssignments();
                    }
                }
            });
        }
        return mnuNew;
    }

    /**
	 * This method initializes mnuSupervisorReport	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getMnuSupervisorReport() {
        if (mnuSupervisorReport == null) {
            mnuSupervisorReport = new JMenuItem();
            mnuSupervisorReport.setText("District Report");
            mnuSupervisorReport.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        final ReportOptionsDialog dlg = new ReportOptionsDialog(HVTHelperGUI.this);
                        dlg.setModal(true);
                        dlg.setVisible(true);
                        if (!dlg.isCancelled()) {
                            final DistrictReport report = new DistrictReport(dlg.getGroupType());
                            report.showReport(hvtInfo, HVTHelperGUI.this);
                        }
                    } catch (JRException ex) {
                        logger.error(ex);
                    }
                }
            });
        }
        return mnuSupervisorReport;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        DOMConfigurator.configure("log4jconfig.xml");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                HVTHelperGUI thisClass = new HVTHelperGUI();
                thisClass.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                thisClass.pack();
                thisClass.setVisible(true);
            }
        });
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        HVTHelperNode node = (HVTHelperNode) treeAssignments.getLastSelectedPathComponent();
        if (node != null) {
            panelActions.removeAll();
            List<Action> actions = node.getActionList();
            if (null != actions) {
                for (Action a : actions) {
                    panelActions.add(new JButton(a));
                }
                treePopupMenu = new JPopupMenu();
                for (Action a : actions) {
                    JMenuItem m = new JMenuItem(a);
                    treePopupMenu.add(m);
                }
            }
            panelActions.revalidate();
            TableModel tm = new DefaultTableModel(node.getTableData(), node.getColumnNames()) {

                private static final long serialVersionUID = 1L;

                @Override
                public void setValueAt(Object aValue, int row, int column) {
                }
            };
            tableFamilies.setModel(tm);
        }
    }

    /**
	 * @return the dirty
	 */
    public boolean isDirty() {
        return dirty;
    }

    /**
	 * @param dirty the dirty to set
	 */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        setTitleWithFileName();
    }

    private boolean saveFile(boolean saveAs) {
        File f = null;
        boolean saved = false;
        if (!"".equals(fileName) && !saveAs) {
            f = new File(fileName);
        } else {
            f = selectFile(true, saveAs);
        }
        if (f != null) {
            waitCursor();
            String name = f.getAbsolutePath();
            if (!name.toLowerCase().endsWith(".xml")) {
                name += ".xml";
                f = new File(name);
            }
            if (f.getAbsolutePath().equals(fileName) || !f.exists() || JOptionPane.showConfirmDialog(this, "Overwrite file: " + f.getAbsolutePath() + "?", "Overwrite existing file?", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                FileOutputStream os;
                try {
                    os = new FileOutputStream(f);
                    Writer writer = new BufferedWriter(new OutputStreamWriter(os, "ISO-8859-1"));
                    writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
                    writer.flush();
                    xstream.toXML(hvtInfo, os);
                    logger.info("Saved file:" + f.getAbsolutePath());
                    fileName = f.getAbsolutePath();
                    setDirty(false);
                    writer.close();
                    os.close();
                    saved = true;
                } catch (Exception ex) {
                    logger.error("Unable to save file:" + ex.getMessage());
                }
            }
            normalCursor();
        }
        return saved;
    }

    public void normalCursor() {
        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(normalCursor);
    }

    public void waitCursor() {
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(hourglassCursor);
    }

    private void openFile() {
        waitCursor();
        File f = selectFile(false);
        if (f != null) {
            try {
                boolean oldFile = false;
                fileName = f.getAbsolutePath();
                FileInputStream fis = new FileInputStream(f);
                hvtInfo = (HVTeachingInfo) xstream.fromXML(fis);
                if (hvtInfo.getVersion() == null) {
                    oldFile = true;
                    fixTeacherCompanionships(hvtInfo.getHtInfo());
                    fixTeacherCompanionships(hvtInfo.getVtInfo());
                    hvtInfo.updateVersion();
                    logger.warn("The file: " + f.getAbsolutePath() + " was saved with a previous version of HVTHelper.");
                    logger.warn("Please choose 'Save' from the 'File' menu to update the file format.");
                }
                populateTree(hvtInfo);
                setDirty(oldFile);
                logger.info("The file: " + f.getAbsolutePath() + " was successfully opened.");
                fis.close();
            } catch (FileNotFoundException ex) {
                logger.error(ex.getMessage());
            } catch (Exception ex) {
                logger.error("There was a problem with the input file. Are you sure it is an XML file that was saved by Home Teaching Helper?");
                logger.error("The error is: " + ex.getMessage());
            }
        }
        normalCursor();
    }

    private void clearAssignments() {
        fileName = "";
        createStartupModel();
        populateTree(hvtInfo);
        expandTree();
        setDirty(false);
    }

    private void exitApp() {
        if (isDirty()) {
            int result = JOptionPane.showConfirmDialog(HVTHelperGUI.this, "You have unsaved changes. Would you like to save them before quiting?", "Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.YES_OPTION && saveFile(false)) {
                System.exit(0);
            } else if (result == JOptionPane.NO_OPTION) System.exit(0);
        } else {
            System.exit(0);
        }
    }

    private static void fixTeacherCompanionships(TeachingAssignmentInfo info) {
        for (Quorum q : info.getQuorums()) {
            for (District d : q.getDistricts()) {
                for (Companionship c : d.getCompanionships()) {
                    if (c.getTeacher1() != null) {
                        c.getTeacher1().addCompanionship(c);
                    }
                    if (c.getTeacher2() != null) {
                        c.getTeacher2().addCompanionship(c);
                    }
                    if (c.getTeacher3() != null) {
                        c.getTeacher3().addCompanionship(c);
                    }
                }
            }
        }
    }
}
