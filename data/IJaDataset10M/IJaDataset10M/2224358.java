package barrywei.igosyncdocs.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import barrywei.igosyncdocs.action.BaseSearchAction;
import barrywei.igosyncdocs.action.CreateNewAction;
import barrywei.igosyncdocs.action.DeleteAction;
import barrywei.igosyncdocs.action.DownloadAction;
import barrywei.igosyncdocs.action.HideAction;
import barrywei.igosyncdocs.action.RefreshAction;
import barrywei.igosyncdocs.action.RenameAction;
import barrywei.igosyncdocs.action.ShowAboutDialogAction;
import barrywei.igosyncdocs.action.ShowPreferenceDialogAction;
import barrywei.igosyncdocs.action.ShowShareDocsDialogAction;
import barrywei.igosyncdocs.action.StarAction;
import barrywei.igosyncdocs.action.SystemTrayAction;
import barrywei.igosyncdocs.action.TrashAction;
import barrywei.igosyncdocs.action.UploadFileAction;
import barrywei.igosyncdocs.action.ViewOnLineAction;
import barrywei.igosyncdocs.bean.ICategory;
import barrywei.igosyncdocs.bean.IGoImageManager;
import barrywei.igosyncdocs.bean.UserConfig;
import barrywei.igosyncdocs.biz.IGoSyncDocsBiz;
import barrywei.igosyncdocs.factory.AbstractFactory;
import barrywei.igosyncdocs.gui.model.IGoSyncDocsCategoryComboboxItem;
import barrywei.igosyncdocs.gui.model.IGoSyncDocsCategoryComboboxModel;
import barrywei.igosyncdocs.gui.model.IGoSyncDocsFolderTreeItem;
import barrywei.igosyncdocs.gui.model.IGoSyncDocsFolderTreeModel;
import barrywei.igosyncdocs.gui.model.IGoSyncDocsRemoteViewTableItem;
import barrywei.igosyncdocs.gui.model.IGoSyncDocsRemoteViewTableModel;
import barrywei.igosyncdocs.gui.renderer.IGoSyncDocsCategoryComboboxRenderer;
import barrywei.igosyncdocs.gui.renderer.IGoSyncDocsRemoteViewTableCellRenderer;
import barrywei.igosyncdocs.gui.renderer.IGoSyncDocsRemoteViewTableHaderRenderer;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Toolkit;

/**
 * 
 * 
 *
 *
 * @author BarryWei
 * @version 1.0, Jul 16, 2010
 * @since JDK1.6
 */
public class IGoSyncDocsMain extends JFrame {

    private static final long serialVersionUID = -132343829434158889L;

    private JPanel pnlMain = new JPanel();

    private JPanel pnlTop;

    private JPanel pnlStatus;

    private JSplitPane pnlLeftSplit;

    private JScrollPane pnlCenter;

    private JScrollPane pnlCategoryScroll;

    private JScrollPane pnlTreeFoldersScroll;

    private JTree treeMyFolders;

    private JList listCategory;

    private JTable tblMainData;

    private JPopupMenu remotePopupMenu = new JPopupMenu();

    private JMenuItem miViewOnLine = new JMenuItem("View Online");

    private JMenuItem miCreateNewDocument = new JMenuItem("Create New Document");

    private JMenuItem miCreateNewPresentation = new JMenuItem("Create New Presentation");

    private JMenuItem miCreateNewSpreadsheet = new JMenuItem("Create New Spreadsheet");

    private JMenuItem miUpload = new JMenuItem("Upload File(s)");

    private JMenuItem miRefresh = new JMenuItem("Refresh All");

    private JMenuItem miDownload = new JMenuItem("Download");

    private JMenuItem miDownloadAsDoc = new JMenuItem("Download Document");

    private JMenuItem miDownloadAsPpt = new JMenuItem("Download Presentation");

    private JMenuItem miDownloadAsXls = new JMenuItem("Download Spreadsheet");

    private JMenuItem miStar = new JMenuItem("Star");

    private JMenuItem miHide = new JMenuItem("Hide");

    private JMenuItem miTrash = new JMenuItem("Trash");

    private JMenuItem miDelete = new JMenuItem("Delete");

    private JMenuItem miRename = new JMenuItem("Rename");

    private JMenu miShare = new JMenu("Share");

    private JMenuItem miShareEmail = new JMenuItem("Email editors/viewers");

    private JMenuItem miShareGroups = new JMenuItem("Google Group");

    private JMenuItem miShareDomain = new JMenuItem("Google Apps domain");

    private JPanel pnlLeftTop = new JPanel();

    private JTextField txtSearch = new JTextField(25);

    private JButton btnSearch = new JButton(" Search ", IGoImageManager.getInstance().getIcon("btn_search.gif"));

    private JLabel lblStatusMessage = new JLabel("Connected to " + UserConfig.Username);

    private JLabel lblTotalDocuments = new JLabel("Total : 0");

    private JProgressBar progressBar = new JProgressBar();

    private IGoSyncDocsBiz biz = null;

    private JMenuBar menuBar = new JMenuBar();

    private JMenu mnFile = new JMenu("File");

    private JMenu mnSettings = new JMenu("Settings");

    private JMenu mnHelp = new JMenu("Help");

    private JMenuItem miAboutiGo = new JMenuItem("About iGoSyncDocs");

    private JMenuItem miCreateDocument = new JMenuItem("New Document");

    private JMenuItem miCreatePresentation = new JMenuItem("New Presentation");

    private JMenuItem miExit = new JMenuItem("Exit");

    private JMenuItem miUploadFiles = new JMenuItem("Upload File(s)");

    private JMenuItem miCreateSpreadsheet = new JMenuItem("New Spreadsheet");

    private JMenuItem miRefreshAll = new JMenuItem("Refresh All");

    private JMenuItem miPreferences = new JMenuItem("<html><b>System Preferences</b></html>");

    private JMenuItem miDocument = new JMenuItem("Document Settings");

    private JMenuItem miPresentation = new JMenuItem("Presentation Settings");

    private JMenuItem miSpreadsheets = new JMenuItem("Spreadsheet Settings");

    public IGoSyncDocsMain() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(IGoSyncDocsMain.class.getResource("/ch/randelshofer/quaqua/images/FileView.computerIcon.png")));
        try {
            this.biz = AbstractFactory.createSyncDocsBizObject();
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage(), "IGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        setJMenuBar(menuBar);
        mnFile.setMnemonic('F');
        menuBar.add(mnFile);
        miCreateDocument.setMnemonic('D');
        miCreateDocument.setAccelerator(KeyStroke.getKeyStroke('D', Event.CTRL_MASK));
        mnFile.add(miCreateDocument);
        miCreatePresentation.setMnemonic('P');
        miCreatePresentation.setAccelerator(KeyStroke.getKeyStroke('P', Event.CTRL_MASK));
        mnFile.add(miCreatePresentation);
        miCreateSpreadsheet.setMnemonic('S');
        miCreateSpreadsheet.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
        mnFile.add(miCreateSpreadsheet);
        miUploadFiles.setMnemonic('U');
        miUploadFiles.setAccelerator(KeyStroke.getKeyStroke('U', Event.CTRL_MASK));
        mnFile.add(miUploadFiles);
        miRefreshAll.setMnemonic('R');
        miRefreshAll.setAccelerator(KeyStroke.getKeyStroke('R', Event.CTRL_MASK));
        mnFile.add(miRefreshAll);
        mnFile.add(new JSeparator());
        miExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure to Exit?", "iGoSyncDocs", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(1);
                }
            }
        });
        mnFile.add(miExit);
        mnSettings.setMnemonic('S');
        miDocument.setAccelerator(KeyStroke.getKeyStroke("F5"));
        mnSettings.add(miDocument);
        miPresentation.setAccelerator(KeyStroke.getKeyStroke("F6"));
        mnSettings.add(miPresentation);
        miSpreadsheets.setAccelerator(KeyStroke.getKeyStroke("F7"));
        mnSettings.add(miSpreadsheets);
        miPreferences.setAccelerator(KeyStroke.getKeyStroke("F8"));
        mnSettings.add(miPreferences);
        mnHelp.setMnemonic('H');
        mnHelp.add(miAboutiGo);
        menuBar.add(mnHelp);
        remotePopupMenu.add(miViewOnLine);
        remotePopupMenu.add(new JSeparator());
        remotePopupMenu.add(miCreateNewDocument);
        remotePopupMenu.add(miCreateNewPresentation);
        remotePopupMenu.add(miCreateNewSpreadsheet);
        remotePopupMenu.add(new JSeparator());
        remotePopupMenu.add(miRefresh);
        remotePopupMenu.add(miUpload);
        remotePopupMenu.add(new JSeparator());
        remotePopupMenu.add(miDownload);
        remotePopupMenu.add(miDownloadAsDoc);
        remotePopupMenu.add(miDownloadAsPpt);
        remotePopupMenu.add(miDownloadAsXls);
        remotePopupMenu.add(new JSeparator());
        remotePopupMenu.add(miStar);
        remotePopupMenu.add(miHide);
        remotePopupMenu.add(new JSeparator());
        remotePopupMenu.add(miTrash);
        remotePopupMenu.add(miDelete);
        remotePopupMenu.add(miRename);
        miShare.add(miShareEmail);
        miShare.add(miShareGroups);
        miShare.add(miShareDomain);
        remotePopupMenu.add(miShare);
        pnlMain.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnlMain.setLayout(new BorderLayout());
        setContentPane(pnlMain);
        pnlTop = new JPanel();
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlTop.setLayout(new BorderLayout(0, 0));
        pnlTop.add(pnlLeftTop, BorderLayout.EAST);
        pnlLeftTop.add(txtSearch);
        pnlLeftTop.add(btnSearch);
        pnlStatus = new JPanel();
        pnlStatus.setPreferredSize(new Dimension(950, 15));
        pnlMain.add(pnlStatus, BorderLayout.SOUTH);
        pnlStatus.setLayout(new BorderLayout(20, 10));
        pnlStatus.add(lblStatusMessage, BorderLayout.WEST);
        pnlStatus.add(lblTotalDocuments, BorderLayout.CENTER);
        pnlStatus.add(progressBar, BorderLayout.EAST);
        progressBar.setVisible(false);
        pnlLeftSplit = new JSplitPane();
        pnlLeftSplit.setPreferredSize(new Dimension(180, 1000));
        pnlLeftSplit.setResizeWeight(0.5);
        pnlLeftSplit.setDividerLocation(250);
        pnlLeftSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pnlMain.add(pnlLeftSplit, BorderLayout.WEST);
        pnlCategoryScroll = new JScrollPane();
        pnlLeftSplit.setLeftComponent(pnlCategoryScroll);
        listCategory = new JList(new IGoSyncDocsCategoryComboboxModel());
        listCategory.setCellRenderer(new IGoSyncDocsCategoryComboboxRenderer());
        pnlCategoryScroll.setViewportView(listCategory);
        listCategory.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        listCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listCategory.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                IGoSyncDocsCategoryComboboxItem item = (IGoSyncDocsCategoryComboboxItem) listCategory.getSelectedValue();
                try {
                    tblMainData.setModel(new IGoSyncDocsRemoteViewTableModel(item.getType()));
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "IGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
                }
                updateRemoteTable();
            }
        });
        pnlTreeFoldersScroll = new JScrollPane();
        pnlLeftSplit.setRightComponent(pnlTreeFoldersScroll);
        treeMyFolders = new JTree(IGoSyncDocsFolderTreeModel.getRoot());
        pnlTreeFoldersScroll.setViewportView(treeMyFolders);
        treeMyFolders.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                int rows = treeMyFolders.getRowForLocation(e.getX(), e.getY());
                if (rows != -1) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMyFolders.getLastSelectedPathComponent();
                    if (node.isRoot()) {
                        try {
                            tblMainData.setModel(new IGoSyncDocsRemoteViewTableModel(ICategory.All));
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "IGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
                        }
                        updateRemoteTable();
                    } else {
                        IGoSyncDocsFolderTreeItem item = (IGoSyncDocsFolderTreeItem) node.getUserObject();
                        try {
                            tblMainData.setModel(new IGoSyncDocsRemoteViewTableModel(item.getEntry().getResourceId()));
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "Server Response:\n" + e1.getMessage(), "iGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
                        }
                        updateRemoteTable();
                    }
                }
            }
        });
        pnlCenter = new JScrollPane();
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        try {
            tblMainData = new JTable(new IGoSyncDocsRemoteViewTableModel(ICategory.All));
        } catch (Exception e3) {
            JOptionPane.showMessageDialog(null, e3.getMessage(), "IGoSyncDocs Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        tblMainData.setToolTipText("Right Click to See More ...");
        updateRemoteTable();
        tblMainData.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int selectedRow = tblMainData.getSelectedRow();
                    if (selectedRow != -1) {
                        IGoSyncDocsRemoteViewTableItem item = ((IGoSyncDocsRemoteViewTableModel) tblMainData.getModel()).getData().get(selectedRow);
                        enableMemnuItem(item);
                        remotePopupMenu.show(tblMainData, e.getX(), e.getY());
                    }
                }
            }
        });
        pnlCenter.setViewportView(tblMainData);
        setSize(new Dimension(950, 650));
        setMinimumSize(new Dimension(950, 650));
        miViewOnLine.addActionListener(new ViewOnLineAction(this));
        miCreateNewDocument.addActionListener(new CreateNewAction(ICategory.Documents, this));
        miCreateDocument.addActionListener(new CreateNewAction(ICategory.Documents, this));
        miCreateNewPresentation.addActionListener(new CreateNewAction(ICategory.Presentations, this));
        miCreatePresentation.addActionListener(new CreateNewAction(ICategory.Presentations, this));
        miCreateNewSpreadsheet.addActionListener(new CreateNewAction(ICategory.SpreadSheets, this));
        miCreateSpreadsheet.addActionListener(new CreateNewAction(ICategory.SpreadSheets, this));
        miRefresh.addActionListener(new RefreshAction(this));
        miRefreshAll.addActionListener(new RefreshAction(this));
        miUpload.addActionListener(new UploadFileAction(this));
        miUploadFiles.addActionListener(new UploadFileAction(this));
        miStar.addActionListener(new StarAction(this));
        miRename.addActionListener(new RenameAction(this));
        miHide.addActionListener(new HideAction(this));
        miTrash.addActionListener(new TrashAction(this));
        miDelete.addActionListener(new DeleteAction(this));
        miDownload.addActionListener(new DownloadAction(this, ICategory.OtherFiles));
        miDownloadAsDoc.addActionListener(new DownloadAction(this, ICategory.Documents));
        miDownloadAsPpt.addActionListener(new DownloadAction(this, ICategory.Presentations));
        miDownloadAsXls.addActionListener(new DownloadAction(this, ICategory.SpreadSheets));
        miShareEmail.addActionListener(new ShowShareDocsDialogAction(this, 1));
        miShareGroups.addActionListener(new ShowShareDocsDialogAction(this, 2));
        miShareDomain.addActionListener(new ShowShareDocsDialogAction(this, 3));
        miAboutiGo.addActionListener(new ShowAboutDialogAction(this));
        btnSearch.addActionListener(new BaseSearchAction(this));
        miPreferences.addActionListener(new ShowPreferenceDialogAction(this));
        addWindowListener(new SystemTrayAction(this));
    }

    private void getItemCountAndShow() {
        try {
            int total = biz.getAllItems().size();
            int trashed = biz.getAllTrashedObject().size();
            int hidden = biz.getAllHiddenObject().size();
            String text = "Total: " + total + "   Trashed: " + trashed + "   Hidden: " + hidden;
            lblTotalDocuments.setText(text);
        } catch (Exception e2) {
            lblTotalDocuments.setText("exception");
        }
    }

    public void enableRemoteTableAndProgressbar() {
        progressBar.setVisible(false);
        tblMainData.setEnabled(true);
        progressBar.setIndeterminate(false);
        tblMainData.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void disableRemoteTableAndProgressbar() {
        progressBar.setVisible(true);
        tblMainData.setEnabled(false);
        progressBar.setIndeterminate(true);
        tblMainData.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void updateRemoteTable() {
        int count = tblMainData.getColumnModel().getColumnCount();
        for (int i = 0; i < count; i++) {
            tblMainData.getColumnModel().getColumn(i).setCellRenderer(new IGoSyncDocsRemoteViewTableCellRenderer());
            if (i == 0) {
                tblMainData.getColumnModel().getColumn(0).setPreferredWidth(25);
                tblMainData.getColumnModel().getColumn(0).setHeaderRenderer(new IGoSyncDocsRemoteViewTableHaderRenderer());
            }
            if (i == 1) {
                tblMainData.getColumnModel().getColumn(1).setPreferredWidth(25);
                tblMainData.getColumnModel().getColumn(1).setHeaderRenderer(new IGoSyncDocsRemoteViewTableHaderRenderer());
            }
            if (i == 2) {
                tblMainData.getColumnModel().getColumn(2).setPreferredWidth(530);
            }
            if (i == 3) {
                tblMainData.getColumnModel().getColumn(3).setPreferredWidth(135);
                tblMainData.getColumnModel().getColumn(3).setHeaderRenderer(new IGoSyncDocsRemoteViewTableHaderRenderer());
            }
        }
        tblMainData.setRowHeight(20);
        tblMainData.setShowGrid(false);
        tblMainData.setDragEnabled(true);
        tblMainData.setAutoCreateColumnsFromModel(true);
        tblMainData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblMainData.getTableHeader().setReorderingAllowed(false);
        tblMainData.add(remotePopupMenu);
        getItemCountAndShow();
    }

    private void enableMemnuItem(IGoSyncDocsRemoteViewTableItem item) {
        if (item.getType().equals("document")) {
            miDownloadAsDoc.setEnabled(true);
            miDownload.setEnabled(false);
            miDownloadAsPpt.setEnabled(false);
            miDownloadAsXls.setEnabled(false);
        } else if (item.getType().equals("spreadsheet")) {
            miDownloadAsDoc.setEnabled(false);
            miDownload.setEnabled(false);
            miDownloadAsPpt.setEnabled(false);
            miDownloadAsXls.setEnabled(true);
        } else if (item.getType().equals("presentation")) {
            miDownloadAsDoc.setEnabled(false);
            miDownload.setEnabled(false);
            miDownloadAsPpt.setEnabled(true);
            miDownloadAsXls.setEnabled(false);
        } else {
            miDownloadAsDoc.setEnabled(false);
            miDownload.setEnabled(true);
            miDownloadAsPpt.setEnabled(false);
            miDownloadAsXls.setEnabled(false);
        }
    }

    public IGoSyncDocsRemoteViewTableItem getRemoteTableSelectedItem() {
        int selectedRow = tblMainData.getSelectedRow();
        if (selectedRow != -1) {
            return ((IGoSyncDocsRemoteViewTableModel) tblMainData.getModel()).getData().get(selectedRow);
        } else return null;
    }

    public IGoSyncDocsRemoteViewTableItem[] getRemoteTableSelectedItems() {
        IGoSyncDocsRemoteViewTableItem[] items = null;
        int[] selectedRows = tblMainData.getSelectedRows();
        if (selectedRows != null && selectedRows.length > 0) {
            items = new IGoSyncDocsRemoteViewTableItem[selectedRows.length];
            for (int i = 0; i < items.length; i++) {
                items[i] = ((IGoSyncDocsRemoteViewTableModel) tblMainData.getModel()).getData().get(i);
            }
        }
        return items;
    }

    public JTable getRemoteTable() {
        return tblMainData;
    }

    public void setRemoteTable(JTable tbl) {
        this.tblMainData = tbl;
    }

    public String getSearchText() {
        return this.txtSearch.getText().trim();
    }
}
