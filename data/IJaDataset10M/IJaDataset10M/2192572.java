package eu.popeye.middleware.dataSharing.test.plugin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageProducer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import com.jgoodies.looks.plastic.*;
import eu.popeye.middleware.dataSearch.SearchEngine;
import eu.popeye.middleware.dataSearch.SearchPanel;
import eu.popeye.middleware.dataSharing.*;
import eu.popeye.middleware.dataSharing.sharedDataImpls.ObjectSharedData;
import eu.popeye.middleware.dataSharing.sharedDataImpls.SharedFile;
import eu.popeye.middleware.dataSharing.dataSharingExceptions.*;
import eu.popeye.middleware.workspacemanagement.WorkspaceProfile;

public class XMLTree extends JPanel implements SharedSpaceObserver, TreeSelectionListener, ActionListener, MouseListener {

    /** */
    JScrollPane treeView;

    JTabbedPane tabbedPane;

    JEditorPane htmlPane;

    MetaDataEditor metaDataEditor;

    HashMap<String, myTreeNode> myTreeNodeMap = new HashMap<String, myTreeNode>();

    /** Shared Space attached to the plug-in view */
    private SharedSpace sharedSpace;

    /** Search Engine attached to the plug-in view */
    private SearchEngine searchEngine;

    /** Shared Space list */
    private Document doc;

    /** */
    private DefaultMutableTreeNode rootNode;

    /** */
    private myTreeNode currentNode;

    private DefaultTreeModel treeModel;

    /** */
    private JTree tree;

    /** plug-in tool bar */
    private JToolBar toolBar;

    /** popup menu for leaves */
    private JPopupMenu popupMenuFile;

    /** popup menu for leaves */
    private JPopupMenu popupMenuDir;

    private int positionX = 0;

    private int positionY = 0;

    /** home directory where the plugin will store their accessed documents */
    private File homeDir;

    public XMLTree(SharedSpace sharedSpace, SearchEngine searchEngine) {
        super(new BorderLayout());
        try {
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Exception e) {
        }
        this.sharedSpace = sharedSpace;
        this.searchEngine = searchEngine;
        this.homeDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "POPEYE_FS_" + this.sharedSpace.getName());
        if (!this.homeDir.exists()) this.homeDir.mkdirs();
        rootNode = new DefaultMutableTreeNode(this.sharedSpace.getName());
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setEditable(false);
        tree.setLayout(new GridLayout(1, 0));
        tree.setShowsRootHandles(true);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(this);
        this.refreshTreeView();
        treeView = new JScrollPane(tree);
        Dimension minimumSize = new Dimension(100, 50);
        treeView.setMinimumSize(minimumSize);
        createTabPane();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(tabbedPane);
        splitPane.setDividerLocation(200);
        splitPane.setPreferredSize(new Dimension(500, 600));
        createToolBar();
        add(toolBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        this.sharedSpace.registerForNotification(this);
    }

    /**
	 * create File Sharing Toolbar 
	 */
    private void createToolBar() {
        toolBar = new JToolBar();
        toolBar.setRollover(true);
        ImageIcon buttonIcon = new ImageIcon(loadImage("images/expand.jpg"));
        JButton button = new JButton(buttonIcon);
        button.setToolTipText("Expand All");
        button.setActionCommand("expand");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/collapse.jpg"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Collapse All");
        button.setActionCommand("collapse");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/refresh.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Refresh Tree");
        button.setActionCommand("refresh");
        button.addActionListener(this);
        toolBar.add(button);
        toolBar.addSeparator();
        buttonIcon = new ImageIcon(loadImage("images/add3.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Share Data");
        button.setActionCommand("Share Data");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/folder3.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Create Directory");
        button.setActionCommand("Create Directory");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/delete.gif"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Withdraw Data / Remove Directory");
        button.setActionCommand("Withdraw");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/read.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Data Access-Read");
        button.setActionCommand("Data Access-Read");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/readWrite.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Data Access-Write");
        button.setActionCommand("Data Access-Write");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/commit.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Commit");
        button.setActionCommand("Commit");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/download3.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Snapshot");
        button.setActionCommand("Snapshot");
        button.addActionListener(this);
        toolBar.add(button);
        buttonIcon = new ImageIcon(loadImage("images/info3.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("Get Metadata");
        button.setActionCommand("Get Metadata");
        button.addActionListener(this);
        toolBar.add(button);
        toolBar.addSeparator();
        buttonIcon = new ImageIcon(loadImage("images/about3.png"));
        button = new JButton(buttonIcon);
        button.setToolTipText("About");
        button.setActionCommand("About");
        button.addActionListener(this);
        toolBar.add(button);
        toolBar.addSeparator();
        button = new JButton("DEBUG");
        button.setActionCommand("DEBUG");
        button.addActionListener(this);
        toolBar.add(button);
        toolBar.setFloatable(false);
    }

    /**
	 * 
	 */
    public void createTabPane() {
        tabbedPane = new JTabbedPane();
        if (this.searchEngine != null) tabbedPane.addTab("Search", null, new SearchPanel(searchEngine), "Search options");
        metaDataEditor = new MetaDataEditor(this.sharedSpace);
        tabbedPane.addTab("MetaData Editor", null, metaDataEditor, "Metadata info");
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        JScrollPane htmlView = new JScrollPane(htmlPane);
        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        tabbedPane.addTab("Console", null, htmlView, "File-Sharing Plug-in messages");
    }

    /**
	 * recurse an xml tree from a given jdom element
	 */
    private void recurseXml(Element element, DefaultMutableTreeNode parentNode, String absolutePath) {
        List elementList = element.getChildren();
        Iterator listIterator = elementList.iterator();
        while (listIterator.hasNext()) {
            Element currentElement = (Element) listIterator.next();
            int type = myTreeNode.DOC_NORMAL;
            if (currentElement.getName().equals("directory")) {
                type = myTreeNode.DIR;
            }
            String label = currentElement.getAttribute("alias").getValue();
            if (!label.equals("plugin")) {
                myTreeNode newNode = new myTreeNode(currentElement.getAttribute("alias").getValue(), type);
                treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
                if (this.myTreeNodeMap.containsKey(absolutePath + "/" + currentElement.getAttribute("alias").getValue())) {
                    type = this.myTreeNodeMap.get(absolutePath + "/" + currentElement.getAttribute("alias").getValue()).getType();
                    newNode.setType(type);
                }
                this.myTreeNodeMap.put(absolutePath + "/" + currentElement.getAttribute("alias").getValue(), newNode);
                recurseXml(currentElement, newNode, absolutePath + "/" + currentElement.getAttribute("alias").getValue());
            }
        }
    }

    private void refreshTreeView() {
        this.doc = sharedSpace.listData();
        IconRenderer myRenderer = new IconRenderer();
        tree.setCellRenderer(myRenderer);
        Element rootElement = doc.getRootElement();
        rootNode = new myTreeNode(this.sharedSpace.getName(), myTreeNode.DIR);
        this.treeModel.setRoot(rootNode);
        if (rootElement != null) {
            recurseXml(rootElement, rootNode, "");
        }
        this.expandAll();
    }

    public void actionPerformed(ActionEvent e) {
        if ("expand".equals(e.getActionCommand())) {
            this.expandAll();
        } else if ("collapse".equals(e.getActionCommand())) {
            this.collapseAll();
        } else if ("refresh".equals(e.getActionCommand())) {
            this.refreshTreeView();
        } else if ("Share Data".equals(e.getActionCommand())) {
            this.shareData();
        } else if ("Create Directory".equals(e.getActionCommand())) {
            this.createDirectory();
        } else if ("Data Access-Read".equals(e.getActionCommand())) {
            this.accessRead(FileViewer.DEFAULT_VIEWER);
        } else if ("Data Access-Read With...".equals(e.getActionCommand())) {
            this.accessRead(FileViewer.CHOSEN_VIEWER);
        } else if ("Data Access-Write".equals(e.getActionCommand())) {
            this.accessWrite(FileViewer.DEFAULT_VIEWER);
        } else if ("Data Access-Write With...".equals(e.getActionCommand())) {
            this.accessWrite(FileViewer.CHOSEN_VIEWER);
        } else if ("Commit".equals(e.getActionCommand())) {
            this.commit();
        } else if ("Snapshot".equals(e.getActionCommand())) {
            this.snapshot();
        } else if ("Get Metadata".equals(e.getActionCommand())) {
            this.tabbedPane.setSelectedIndex(1);
            this.readMetadata();
        } else if ("Withdraw".equals(e.getActionCommand())) {
            this.withdrawData();
        } else if ("About".equals(e.getActionCommand())) {
            AboutFileSharing.display(this);
        } else if ("DEBUG".equals(e.getActionCommand())) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                XMLOutputter outputter = new XMLOutputter();
                outputter.output(this.doc, out);
                displayInfo(out.toString());
                out.close();
                this.tabbedPane.setSelectedIndex(2);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else System.out.println("Not handled event");
    }

    /**
	 * expand all tree nodes
	 */
    public void expandAll() {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    /**
	 * collapse all tree nodes
	 */
    public void collapseAll() {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }

    /**
	 * 
	 *
	 */
    private void shareData() {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String fullPath = "/";
            if (currentNode != null) {
                Object[] path = currentNode.getUserObjectPath();
                for (int i = 1; i < path.length; i++) {
                    fullPath += (String) path[i] + "/";
                }
            }
            fullPath += file.getName();
            Object[] options = OptionsDialog.getOptions(fullPath, "To be set");
            if (options != null) {
                try {
                    SharedFile data = new SharedFile();
                    data.retrieve(file.getAbsolutePath());
                    this.sharedSpace.shareData(data, (String) options[0], (SharingMode) options[1], false);
                    this.refreshTreeView();
                } catch (DataAlreadyExistException e) {
                    JOptionPane.showMessageDialog(this, "Invalid Path: " + fullPath, "DataAlreadyExistException", JOptionPane.ERROR_MESSAGE);
                } catch (InvalidPathException e) {
                    JOptionPane.showMessageDialog(this, "Invalid Path: " + fullPath, "InvalidPathException", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
	 * Create a new directory in the shared space
	 *
	 */
    private void createDirectory() {
        String fullPath = this.getFullPath(this.currentNode) + "/New Folder";
        fullPath = (String) JOptionPane.showInputDialog(null, "Select new directory path:", "", JOptionPane.PLAIN_MESSAGE, null, null, fullPath);
        if (fullPath == null) return;
        try {
            this.sharedSpace.shareData(null, fullPath, SharingMode.SWMR, true);
        } catch (DataAlreadyExistException e) {
            JOptionPane.showMessageDialog(this, "Invalid Path: " + fullPath, "DataAlreadyExistException", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidPathException e) {
            JOptionPane.showMessageDialog(this, "Invalid Path: " + fullPath, "InvalidPathException", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void accessRead(int option) {
        if (currentNode != null) {
            if (currentNode.isDirectory()) return;
            String pathInTS = "";
            try {
                pathInTS = getFullPath(currentNode);
                SharedData sharedData = this.sharedSpace.accessRead(pathInTS);
                if (sharedData instanceof SharedFile) {
                    SharedFile data = (SharedFile) this.sharedSpace.accessRead(pathInTS);
                    String fullPath = this.homeDir.getAbsolutePath() + pathInTS;
                    data.save(fullPath);
                    System.out.println("XMLTRee saving in :" + fullPath);
                    FileViewer fileViewer = FileViewerFactory.getFileViewer();
                    if (fileViewer != null) fileViewer.viewFile(data.getLocation(), option);
                } else if (sharedData instanceof ObjectSharedData) {
                    ObjectSharedData data = (ObjectSharedData) this.sharedSpace.accessRead(pathInTS);
                    Object payload = data.getPayload();
                    JTextArea textArea = new JTextArea();
                    textArea.setText(payload.toString());
                    textArea.setEditable(false);
                    JFrame frame = new JFrame("" + pathInTS);
                    frame.setContentPane(new JScrollPane(textArea));
                    frame.setPreferredSize(new Dimension(300, 200));
                    frame.setSize(new Dimension(300, 200));
                    frame.setLocation(positionX + 30, positionY + 30);
                    frame.pack();
                    frame.setVisible(true);
                }
            } catch (DataDoesNotExistException e) {
                JOptionPane.showMessageDialog(this, "Data doesn't exist: " + pathInTS);
            } catch (CouldNotGetLastVersionException e) {
                JOptionPane.showMessageDialog(this, "Couldn't get the last version: " + pathInTS);
            } catch (CouldNotGetException e) {
                JOptionPane.showMessageDialog(this, "Couldn't get : " + pathInTS);
            } catch (FileViewerException e) {
                JOptionPane.showMessageDialog(this, e.toString());
            }
        }
    }

    private void accessWrite(int option) {
        if (currentNode != null) {
            if (currentNode.isDirectory()) return;
            String pathInTS = getFullPath(currentNode);
            AccessWriteThread thread = new AccessWriteThread(this, pathInTS, option);
            thread.start();
        }
    }

    /**
	 * Class used to adquire data in access write mode. The thread is needed so the application doesn't
	 * block in case the data lock is not currently available 
	 * @author Francisco Antonio Bas Esparza
	 *
	 */
    private class AccessWriteThread extends Thread {

        private JComponent parent;

        private String pathInTS;

        private int viewerOption;

        public AccessWriteThread(JComponent parent, String pathInTS, int viewerOption) {
            this.parent = parent;
            this.pathInTS = pathInTS;
            this.viewerOption = viewerOption;
        }

        public void run() {
            try {
                myTreeNode myNode = myTreeNodeMap.get(pathInTS);
                myNode.setType(myTreeNode.DOC_WAITING_LOCK);
                treeModel.reload(myTreeNodeMap.get(pathInTS));
                SharedFile data = (SharedFile) sharedSpace.accessWrite(pathInTS);
                myNode.setType(myTreeNode.DOC_LOCKED);
                treeModel.reload(myTreeNodeMap.get(pathInTS));
                String fullPath = homeDir.getAbsolutePath() + pathInTS;
                data.save(fullPath);
                FileViewer fileViewer = FileViewerFactory.getFileViewer();
                if (fileViewer != null) fileViewer.viewFile(data.getLocation(), viewerOption);
                return;
            } catch (DataDoesNotExistException e) {
                JOptionPane.showMessageDialog(parent, "Data doesn't exist: " + pathInTS);
            } catch (CouldNotGetLastVersionException e) {
                JOptionPane.showMessageDialog(parent, "Couldn't get the last version: " + pathInTS);
            } catch (CouldNotGetException e) {
                JOptionPane.showMessageDialog(parent, "Couldn't get : " + pathInTS);
            } catch (NotAllowedException e) {
                JOptionPane.showMessageDialog(parent, "Not allowed to modify " + pathInTS);
            } catch (FileViewerException e) {
                JOptionPane.showMessageDialog(parent, e.toString());
            }
            myTreeNode myNode = myTreeNodeMap.get(pathInTS);
            myNode.setType(myTreeNode.DOC_NORMAL);
            treeModel.reload(myTreeNodeMap.get(pathInTS));
        }
    }

    public void snapshot() {
        if (currentNode == null) {
            return;
        }
        String pathInTS = "";
        pathInTS = getFullPath(currentNode);
        JFileChooser chooser = new JFileChooser("Select snapshot destination");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            this.sharedSpace.snapshot(pathInTS, selectedFile.getAbsolutePath());
        }
    }

    private void commit() {
        if (currentNode != null) {
            if (currentNode.isDirectory()) return;
            String pathInTS = "";
            try {
                pathInTS = getFullPath(currentNode);
                myTreeNode myNode = myTreeNodeMap.get(pathInTS);
                if (myNode.getType() == myTreeNode.DOC_LOCKED) {
                    String fullPath = this.homeDir.getAbsolutePath() + pathInTS;
                    SharedFile data = new SharedFile();
                    data.retrieve(fullPath);
                    this.sharedSpace.commit(pathInTS, data);
                    myNode.setType(myTreeNode.DOC_NORMAL);
                    treeModel.reload(myTreeNodeMap.get(pathInTS));
                }
            } catch (DataDoesNotExistException e) {
                JOptionPane.showMessageDialog(this, "Data doesn't exist: " + pathInTS);
            } catch (CouldNotGetLastVersionException e) {
                JOptionPane.showMessageDialog(this, "Couldn't get the last version: " + pathInTS);
            } catch (CouldNotGetException e) {
                JOptionPane.showMessageDialog(this, "Couldn't get : " + pathInTS);
            } catch (NotAllowedException e) {
                JOptionPane.showMessageDialog(this, "Not allow to modify " + pathInTS);
            }
        }
    }

    private void readMetadata() {
        if (currentNode != null) {
            String fullPath = "";
            try {
                fullPath = getFullPath(currentNode);
                Document metadata = (Document) this.sharedSpace.readMetadata(fullPath).getMetadata();
                metaDataEditor.update(getFullPath(currentNode), metadata);
            } catch (InvalidPathException e) {
                JOptionPane.showMessageDialog(this, "Invalid Path: " + fullPath, "InvalidPathRemoteException", JOptionPane.ERROR_MESSAGE);
            } catch (DataDoesNotExistException e) {
                JOptionPane.showMessageDialog(this, "Data doesn't exist: " + fullPath);
            } catch (CouldNotGetException e) {
                JOptionPane.showMessageDialog(this, "Couldn't get the requested data: " + fullPath, "CouldNotGetException", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void withdrawData() {
        if (currentNode != null) {
            String fullPath = "";
            try {
                fullPath = getFullPath(currentNode);
                System.out.println("Trying to remove: " + fullPath);
                this.sharedSpace.withdrawData(fullPath);
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(this, "Communication Exception", "RemoteException", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (DataDoesNotExistException e) {
                JOptionPane.showMessageDialog(this, "Data doesn't exist: " + fullPath);
            } catch (NotAllowedException e) {
                JOptionPane.showMessageDialog(this, "Error when trying to remove:\n - If it is a file: check permissions\n - If it is a directory:  make sure it is empty");
            }
        }
    }

    private String getFullPath(DefaultMutableTreeNode node) {
        String fullPath = "";
        if (node != null) {
            Object[] path = node.getUserObjectPath();
            for (int i = 1; i < path.length; i++) {
                fullPath += "/" + (String) path[i];
            }
        }
        return fullPath;
    }

    private void displayInfo(String info) {
        htmlPane.setText(info);
    }

    private JPopupMenu getPopupMenuFile() {
        if (popupMenuFile == null) {
            popupMenuFile = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem("Open (read only Mode)");
            menuItem.setActionCommand("Data Access-Read");
            menuItem.addActionListener(this);
            popupMenuFile.add(menuItem);
            menuItem = new JMenuItem("Open With... (read only Mode)");
            menuItem.setActionCommand("Data Access-Read With...");
            menuItem.addActionListener(this);
            popupMenuFile.add(menuItem);
            popupMenuFile.add(new JSeparator(JSeparator.HORIZONTAL));
            menuItem = new JMenuItem("Open (write Mode)");
            menuItem.setActionCommand("Data Access-Write");
            menuItem.addActionListener(this);
            popupMenuFile.add(menuItem);
            menuItem = new JMenuItem("Open With... (write Mode)");
            menuItem.setActionCommand("Data Access-Write With...");
            menuItem.addActionListener(this);
            popupMenuFile.add(menuItem);
            menuItem = new JMenuItem("Commit");
            menuItem.setActionCommand("Commit");
            menuItem.addActionListener(this);
            popupMenuFile.add(menuItem);
            menuItem = new JMenuItem("Snapshot");
            menuItem.setActionCommand("Snapshot");
            menuItem.addActionListener(this);
            popupMenuFile.add(menuItem);
            popupMenuFile.add(new JSeparator(JSeparator.HORIZONTAL));
            menuItem = new JMenuItem("Get Metadata");
            menuItem.setActionCommand("Get Metadata");
            menuItem.addActionListener(this);
            popupMenuFile.add(menuItem);
            menuItem = new JMenuItem("Delete");
            menuItem.setActionCommand("Withdraw");
            menuItem.addActionListener(this);
            popupMenuFile.add(menuItem);
            menuItem = new JMenuItem("Move");
            menuItem.setActionCommand("Move");
            menuItem.addActionListener(this);
            menuItem.setEnabled(false);
            popupMenuFile.add(menuItem);
        }
        return popupMenuFile;
    }

    private JPopupMenu getPopupMenuDir() {
        if (popupMenuDir == null) {
            popupMenuDir = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem("New File");
            menuItem.setActionCommand("Share Data");
            menuItem.addActionListener(this);
            popupMenuDir.add(menuItem);
            menuItem = new JMenuItem("New Directory");
            menuItem.setActionCommand("Create Directory");
            menuItem.addActionListener(this);
            popupMenuDir.add(menuItem);
            menuItem = new JMenuItem("Delete");
            menuItem.setActionCommand("Withdraw");
            menuItem.addActionListener(this);
            popupMenuDir.add(menuItem);
            menuItem = new JMenuItem("Move");
            menuItem.setActionCommand("Move");
            menuItem.addActionListener(this);
            menuItem.setEnabled(false);
            popupMenuDir.add(menuItem);
            popupMenuDir.add(new JSeparator());
            menuItem = new JMenuItem("Copy Branch");
            menuItem.setActionCommand("Copy Branch");
            menuItem.addActionListener(this);
            menuItem.setEnabled(false);
            popupMenuDir.add(menuItem);
            menuItem = new JMenuItem("Snapshot");
            menuItem.setActionCommand("Snapshot");
            menuItem.addActionListener(this);
            menuItem.setEnabled(true);
            popupMenuDir.add(menuItem);
        }
        return popupMenuDir;
    }

    private void showAboutWindow() {
        JOptionPane.showMessageDialog(null, "POPEYE File-Shring plug-in\n\n" + "Version: 0.2 (October, 2007)\n" + "Developed by Francisco Antonio Bas Esparza\n" + "(c) Copyright Popeye contributors\n" + "------------------------------------------\n" + "ToDo:\n" + " - Data names with spaces does not work in Linux\n" + " - Queries including wilcard characters and capital letters do not work", "About File-Sharing Plug-in", JOptionPane.INFORMATION_MESSAGE, null);
    }

    public void valueChanged(TreeSelectionEvent e) {
        currentNode = (myTreeNode) tree.getLastSelectedPathComponent();
        if (currentNode == null) return;
        positionX = MouseInfo.getPointerInfo().getLocation().x;
        positionY = MouseInfo.getPointerInfo().getLocation().y;
        String nodeName = (String) currentNode.getUserObject();
        if (currentNode.isDirectory()) {
            displayInfo(nodeName + " is a directory");
        }
    }

    public void mousePressed(MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path != null) {
            tree.setSelectionPath(path);
        }
        if (e.getClickCount() == 2) this.accessRead(FileViewer.DEFAULT_VIEWER);
    }

    public void mouseReleased(MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path == null) {
            return;
        }
        boolean showMenu = (e.isPopupTrigger() || (e.getButton() == 3));
        myTreeNode node = (myTreeNode) path.getLastPathComponent();
        if (node != null && showMenu && !node.isDirectory()) {
            this.getPopupMenuFile().show(tree, e.getX() + 30, e.getY());
        } else if (node != null && showMenu) {
            this.getPopupMenuDir().show(tree, e.getX() + 30, e.getY());
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void treeStructureChanged(SharedSpaceEvent event) {
        this.refreshTreeView();
    }

    private Image loadImage(String name) {
        try {
            URL url = this.getClass().getResource(name);
            Image image = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
            return image;
        } catch (IOException e) {
            System.err.println("Image not found: " + name);
            return null;
        }
    }
}
