package org.skunk.dav.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.skunk.config.Configurator;
import org.skunk.dav.client.DAVConstants;
import org.skunk.dav.client.DAVFile;
import org.skunk.dav.client.Lock;
import org.skunk.dav.client.gui.editor.DAVEditor;
import org.skunk.dav.client.gui.editor.EditEvent;
import org.skunk.dav.client.gui.editor.EditListener;
import org.skunk.swing.TreeNodeChooser;

/**
 * An embeddable widget that presents conjoined 
 * tree and table views of a DAV file hierarchy.
 *
 * @author Jacob Smullyan
 * @version $Version$
 */
public class Explorer extends JPanel implements Buffer, EditListener {

    final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    static final long serialVersionUID = 3129234418500414985L;

    private static ArrayList allExplorers;

    private DefaultMutableTreeNode rootNode;

    private DAVTreeModel treeModel;

    private DAVTableModel tableModel;

    private JTable table;

    private JTree tree;

    private DAVTableHeader[] tableHeaders = new DAVTableHeader[] { DAVTableHeader.RESOURCETYPE, DAVTableHeader.LOCK, DAVTableHeader.FILE_NAME, DAVTableHeader.CONTENT_TYPE, DAVTableHeader.CONTENT_LENGTH, DAVTableHeader.LAST_MODIFIED, DAVTableHeader.OWNER };

    private boolean showTableVerticalLines = true;

    private boolean showTableHorizontalLines = true;

    public static final String TABLE_HEADERS_PROPERTY = "tableHeaders";

    static {
        allExplorers = new ArrayList();
    }

    public static synchronized Iterator explorers() {
        return ((ArrayList) allExplorers.clone()).iterator();
    }

    public static int getExplorerCount() {
        return allExplorers.size();
    }

    public static Explorer guessActiveExplorer() {
        for (ListIterator lit = allExplorers.listIterator(); lit.hasNext(); ) {
            Explorer ex = (Explorer) lit.next();
            if (SwingUtilities.findFocusOwner(SwingUtilities.getRoot(ex)) != null) return ex;
        }
        return null;
    }

    public Explorer() {
        super();
        initComponents();
        allExplorers.add(this);
    }

    public String getName() {
        return ResourceManager.getMessage(ResourceManager.EXPLORER_NAME);
    }

    public JComponent getComponent() {
        return this;
    }

    public void docking() {
    }

    public void undocking() {
    }

    public void editPerformed(EditEvent event) {
        int code = event.getEditCode();
        final DAVEditor editor = (DAVEditor) event.getSource();
        Runnable successRunner;
        switch(code) {
            case EditEvent.CLOSE:
                log.trace("received EditEvent.CLOSE");
                break;
            case EditEvent.SAVE:
                log.trace("received EditEvent.SAVE");
                successRunner = new Runnable() {

                    public void run() {
                        editor.setDirty(false);
                    }
                };
                createFile(editor.getResourceName(), editor.getResourceBody(), successRunner);
                editor.setDirty(false);
                break;
            case EditEvent.SAVE_AS:
                log.trace("received EditEvent.SAVE_AS");
                DAVFile oldFile = editor.getDAVFile();
                DAVTreeNode oldParentNode = getNodeForFile(oldFile);
                String title = ResourceManager.getMessage(ResourceManager.SAVE_AS_KEY);
                DAVFileChooser.ChoiceStruct choice = chooseFile(oldParentNode, title, true, oldFile.getFileName());
                if (choice == null) {
                    log.trace("user cancelled save as");
                    return;
                }
                final DAVTreeNode parentNode = choice.getParentNode();
                assert parentNode != null : "parent node is not null";
                final String newFileName = choice.getFilename();
                if (newFileName == null) {
                    log.trace("user didn't select anything");
                    return;
                }
                final String newPath = choice.getFilePath();
                successRunner = new Runnable() {

                    public void run() {
                        log.trace("in runnable");
                        refreshNode(parentNode);
                        DAVFile parentFile = parentNode.getDAVFile();
                        String pathWithoutHost = parentFile.getName() + newFileName;
                        final DAVFile newDAVFile = parentFile.getChildNamed(pathWithoutHost);
                        if (newDAVFile == null) {
                            log.trace("*** ERROR: DAVFile not found with path " + newPath);
                            return;
                        }
                        editor.setDAVFile(newDAVFile);
                        editor.setDirty(false);
                        editor.setResourceName(newDAVFile.getFullName());
                        editor.setName(newFileName);
                        log.trace("editor is {0}, its file is {1}, its dirtiness is {2}, its name is {3}", new Object[] { editor, editor.getDAVFile(), new Boolean(editor.isDirty()), editor.getName() });
                        Explorer.this.lock(parentNode, newDAVFile);
                        ExplorerApp.getViewForBuffer(editor).dock(editor);
                    }
                };
                createFile(newPath, editor.getResourceBody(), successRunner);
                unlock(oldParentNode, oldFile);
                break;
            default:
                assert false : "this default case of the switch on editCode should not be reached";
        }
    }

    private void initComponents() {
        rootNode = new DefaultMutableTreeNode("root");
        treeModel = new DAVTreeModel(rootNode);
        treeModel.addTreeModelListener(new DAVTreeModelListener(this));
        tree = new JTree(treeModel);
        tree.setLargeModel(true);
        tree.setCellRenderer(new DAVTreeCellRenderer());
        ToolTipManager.sharedInstance().registerComponent(tree);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tableModel = new DAVTableModel(tableHeaders);
        tree.addTreeSelectionListener(new DAVTreeSelectionListener(treeModel, tableModel));
        table = new JTable(tableModel);
        addTableColumns();
        table.setShowVerticalLines(getShowHorizontalLines());
        table.setShowHorizontalLines(getShowHorizontalLines());
        TableMouser mouseCatcher = new TableMouser(this);
        table.addMouseListener(mouseCatcher);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Lock.class, new LockRenderer());
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane treePane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Dimension treeD = tree.getPreferredSize();
        treeD.width = Math.max(treeD.width, 200);
        treePane.setPreferredSize(treeD);
        JScrollPane tablePane = new JScrollPane(table);
        tablePane.addMouseListener(mouseCatcher);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, tablePane);
        splitPane.setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    private void addTableColumns() {
        table.createDefaultColumnsFromModel();
        int doneCanAbort = 0;
        for (Enumeration eenum = table.getColumnModel().getColumns(); eenum.hasMoreElements(); ) {
            TableColumn tc = (TableColumn) eenum.nextElement();
            Object identifier = tc.getIdentifier();
            if (identifier.equals(DAVTableHeader.LOCK.toString())) {
                setColumnForIcon(tc, ResourceManager.getImageIcon(ResourceManager.LOCK_ICON, null));
                doneCanAbort++;
            } else if (identifier.equals(DAVTableHeader.CONTENT_LENGTH.toString())) {
                tc.setPreferredWidth(40);
                doneCanAbort++;
            } else if (identifier.equals(DAVTableHeader.RESOURCETYPE.toString())) {
                setColumnForIcon(tc, ResourceManager.getImageIcon(ResourceManager.FOLDER_ICON, null));
                tc.setCellRenderer(new ResourceTypeRenderer());
                doneCanAbort++;
            }
            if (doneCanAbort == 3) return;
        }
    }

    private void setColumnForIcon(TableColumn tc, ImageIcon ii) {
        int width = ii.getIconWidth();
        tc.setMaxWidth(width);
        tc.setPreferredWidth(width);
        tc.setMinWidth(width);
        tc.setResizable(false);
        tc.setHeaderValue("");
    }

    /**
     * @return array of table headers
     */
    public DAVTableHeader[] getTableHeaders() {
        return this.tableHeaders;
    }

    public void setTableHeaders(DAVTableHeader[] tableHeaders) {
        this.tableHeaders = tableHeaders;
        tableModel.setTableHeaders(tableHeaders);
        addTableColumns();
    }

    public boolean getShowVerticalLines() {
        return this.showTableVerticalLines;
    }

    public void setShowVerticalLines(boolean showVerticalLines) {
        this.showTableVerticalLines = showVerticalLines;
        table.setShowVerticalLines(showVerticalLines);
    }

    public boolean getShowHorizontalLines() {
        return this.showTableHorizontalLines;
    }

    public void setShowHorizontalLines(boolean showHorizontalLines) {
        this.showTableHorizontalLines = showHorizontalLines;
        table.setShowHorizontalLines(showHorizontalLines);
    }

    /**
     * tries to connect to the server <code>sd</code> and 
     * displays a corresponding tree node in the explorer.  
     * If the attempt succeeded, the <code>Runnable</code> <code>postRunner</code>
     * will then be executed.
     * @param sd the <code>ServerData</code> representing the target server
     * @param postRunner the <code>Runnable</a> to be executed upon success
     */
    public void addConnectionNode(ServerData sd, Runnable postRunner) {
        selectNode(treeModel.addConnectionNode(sd, postRunner));
    }

    public void removeConnectionNode(String host, int port, String initialPath) {
        treeModel.removeConnectionNode(host, port, initialPath);
        tree.clearSelection();
    }

    public void removeConnectionNode(ServerData sd) {
        treeModel.removeConnectionNode(sd);
        tree.clearSelection();
    }

    public int getConnectedServerCount() {
        return treeModel.getChildCount(rootNode);
    }

    public ServerData[] getConnectedServers() {
        int numServers = getConnectedServerCount();
        ServerData[] servers = new ServerData[numServers];
        for (int i = 0; i < getConnectedServerCount(); i++) {
            Object nodeObj = treeModel.getChild(rootNode, i);
            if (nodeObj instanceof DAVTreeNode) servers[i] = ServerData.getServer(nodeObj.toString());
        }
        return servers;
    }

    public boolean isConnected(ServerData sd) {
        ServerData[] servers = getConnectedServers();
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].equals(sd)) return true;
        }
        return false;
    }

    public byte[] get(DAVFile file) {
        return treeModel.get(file);
    }

    public void delete(DAVTreeNode parentNode, String fileName) {
        treeModel.delete(parentNode, fileName);
        selectNode(parentNode);
    }

    public void lock(DAVTreeNode parentNode, DAVFile file) {
        lock(parentNode, file, null);
    }

    public void lock(DAVTreeNode parentNode, DAVFile file, Runnable runnable) {
        ServerData sd = getConnection(parentNode);
        assert sd != null : MessageFormat.format("node {0} has non-null ServerData", new Object[] { parentNode });
        String owner = sd.getOwner();
        if (owner == null) {
            owner = promptForLockOwner(sd);
            if (owner == null) return;
        }
        treeModel.lock(parentNode, file, owner, runnable);
        selectNode(parentNode);
    }

    private String promptForLockOwner(ServerData sd) {
        String owner;
        String message = ResourceManager.getMessage(ResourceManager.LOCK_OWNER_PROMPT, new Object[] { sd.getHost() });
        String title = ResourceManager.getMessage(ResourceManager.LOCK_OWNER_DIALOG_TITLE);
        while (true) {
            Object result = JOptionPane.showInputDialog(this, message, title, JOptionPane.QUESTION_MESSAGE, null, null, sd.getUsername());
            if (result == null) return null; else {
                owner = result.toString().trim();
                if (owner.equals("")) continue; else break;
            }
        }
        sd.setOwner(owner);
        try {
            ServerData.saveServers();
        } catch (IOException oyVeh) {
            log.error("Exception", oyVeh);
        }
        return owner;
    }

    public void unlock(DAVTreeNode parentNode, DAVFile file) {
        treeModel.unlock(parentNode, file);
        selectNode(parentNode);
    }

    public void stealLock(DAVTreeNode parentNode, DAVFile file) {
        ServerData sd = getConnection(parentNode);
        assert sd != null : MessageFormat.format("node {0} has non-null ServerData", new Object[] { parentNode });
        String owner = sd.getOwner();
        if (owner == null) owner = promptForLockOwner(sd);
        if (owner == null) return;
        treeModel.stealLock(parentNode, file, owner);
        selectNode(parentNode);
    }

    public void proppatch(DAVTreeNode parentNode, DAVFile file, Map propertyValueMap) {
        treeModel.proppatch(parentNode, file, propertyValueMap);
    }

    public void copy(DAVTreeNode parentNode, DAVFile file, String destinationURL) {
        treeModel.copy(parentNode, file, destinationURL);
        int lastDex = destinationURL.lastIndexOf(DAVConstants.DAV_FILE_SEPARATOR) + 1;
        selectNode(treeModel.getNodeMatchingURL(destinationURL.substring(0, lastDex)));
    }

    public void move(DAVTreeNode parentNode, DAVFile file, String destinationURL) {
        treeModel.move(parentNode, file, destinationURL);
        int lastDex = destinationURL.lastIndexOf(DAVConstants.DAV_FILE_SEPARATOR) + 1;
        selectNode(treeModel.getNodeMatchingURL(destinationURL.substring(0, lastDex)));
    }

    public ServerData getConnection(DAVTreeNode node) {
        log.trace("node for getConnection() is {0}", new Object[] { node });
        Object[] path = treeModel.getPathToRoot(node);
        return _getConnection(path);
    }

    private ServerData _getConnection(Object[] path) {
        if (path == null) return null;
        if (path.length > 1) {
            Object serverNodeObj = path[1];
            if (serverNodeObj instanceof DAVTreeNode) {
                DAVTreeNode serverNode = (DAVTreeNode) serverNodeObj;
                ServerData foundSD = ServerData.getServer(serverNode.toString());
                log.trace("ServerData found is " + foundSD);
                return foundSD;
            }
        }
        log.trace("returning null from _getConnection");
        return null;
    }

    public ServerData getSelectedConnection() {
        TreePath teepee = tree.getSelectionPath();
        log.trace("selected TreePath is " + teepee);
        if (teepee != null) {
            Object[] path = teepee.getPath();
            if (log.isDebugEnabled()) {
                StringBuffer sb = new StringBuffer("path returned by treepath:\n");
                for (int i = 0; i < path.length; i++) sb.append("\t").append(path[i]).append("\n");
                log.trace(sb.toString());
            }
            return _getConnection(path);
        }
        log.trace("returning null from getSelectedConnection()");
        return null;
    }

    public void selectLastNode() {
        TreePath path = null;
        if (rootNode.getChildCount() > 0) {
            path = new TreePath(((DefaultMutableTreeNode) rootNode.getLastChild()).getPath());
        } else {
            path = new TreePath(rootNode.getPath());
        }
        log.trace("selecting path " + path);
        tree.setSelectionPath(path);
    }

    public void selectNode(DAVTreeNode node) {
        log.trace("in selectNode with node " + node);
        if (node != null) tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(node)));
    }

    public void displayCollection(String filename) {
        DAVTreeNode parentNode = getSelectedNode();
        if (parentNode != null) displayCollection(parentNode, filename);
    }

    /**
     * displays a given collection in the explorer.
     * @param parentNode the node of the containing collection
     * @param filename the name of the collection resource to display
     */
    public void displayCollection(DAVTreeNode parentNode, String filename) {
        DAVTreeNode childNode = getChildNode(parentNode, filename);
        if (childNode == null) {
            log.trace("filename {0} not found under node {1}", new Object[] { filename, parentNode });
            return;
        }
        TreeNode[] nodeArray = treeModel.getPathToRoot(childNode);
        if (log.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer("path returned by model for node ").append(childNode).append(":\n");
            for (int i = 0; i < nodeArray.length; i++) sb.append("\t").append(nodeArray[i]).append("\n");
            log.trace(sb.toString());
        }
        TreePath newPath = new TreePath(nodeArray);
        tree.makeVisible(newPath);
        tree.scrollPathToVisible(newPath);
        tree.setSelectionPath(newPath);
    }

    protected DAVTreeNode getChildNode(DAVTreeNode parentNode, String filename) {
        for (Enumeration eenum = parentNode.children(); eenum.hasMoreElements(); ) {
            Object nextObj = eenum.nextElement();
            assert nextObj instanceof DAVTreeNode : nextObj + " is a DAVTreeNode";
            DAVTreeNode childNode = (DAVTreeNode) nextObj;
            DAVFile file = childNode.getDAVFile();
            if (file != null && file.isCollection() && filename.equals(file.getFileName())) {
                return childNode;
            }
        }
        return null;
    }

    public DAVTreeNode getNodeForFile(DAVFile file) {
        String fullPath = file.getFullName();
        String directoryPath = (file.isCollection()) ? fullPath : fullPath.substring(0, fullPath.lastIndexOf(DAVConstants.DAV_FILE_SEPARATOR));
        log.trace("directory path: " + directoryPath);
        DAVTreeNode node = treeModel.getNodeMatchingPath(directoryPath);
        log.trace("returning node " + node);
        return node;
    }

    public void createCollection(DAVTreeNode parentNode, String collectionName) {
        treeModel.mkcol(parentNode, collectionName);
        selectNode(parentNode);
    }

    public void createFile(String fullPath, byte[] bodyBytes) {
        createFile(fullPath, bodyBytes, null);
    }

    public void createFile(String fullPath, byte[] bodyBytes, Runnable successRunner) {
        int lastSep = fullPath.lastIndexOf(DAVConstants.DAV_FILE_SEPARATOR);
        String directoryPath = fullPath.substring(0, lastSep);
        String filename = fullPath.substring(lastSep + 1);
        DAVTreeNode node = treeModel.getNodeMatchingPath(directoryPath);
        if (node != null) {
            createFile(node, filename, bodyBytes, successRunner);
        } else {
            log.trace("no node found for path {0}", new Object[] { fullPath });
        }
    }

    public void createFile(DAVTreeNode parentNode, String fileName) {
        createFile(parentNode, fileName, new byte[0], null);
    }

    public void createFile(DAVTreeNode parentNode, String fileName, Runnable successRunner) {
        createFile(parentNode, fileName, new byte[0], successRunner);
    }

    public void createFile(DAVTreeNode parentNode, String fileName, byte[] bodyBytes) {
        createFile(parentNode, fileName, bodyBytes, null);
    }

    public void createFile(DAVTreeNode parentNode, String fileName, byte[] bodyBytes, Runnable successRunner) {
        treeModel.put(parentNode, fileName, bodyBytes, successRunner);
        selectNode(parentNode);
    }

    protected void setTableFile(DAVFile file) {
        tableModel.setDAVFile(file);
        table.clearSelection();
    }

    public DAVFile getCurrentFile() {
        if (table.getSelectedRow() != -1) return getSelectedTableFile();
        DAVTreeNode node = getSelectedNode();
        if (node == null) return null; else return node.getDAVFile();
    }

    public DAVFile getSelectedTableFile() {
        return tableModel.getDAVFile(table.getSelectedRow());
    }

    public DAVFile getFile(DAVTreeNode parentNode, String filename) {
        for (Iterator it = parentNode.getDAVFile().children(); it.hasNext(); ) {
            DAVFile kidFile = (DAVFile) it.next();
            if (kidFile.getFileName().equals(filename)) return kidFile;
        }
        return null;
    }

    public DAVTreeNode getSelectedNode() {
        TreePath currentPath = tree.getSelectionPath();
        if (currentPath == null) return null;
        Object lastObj = currentPath.getLastPathComponent();
        log.trace("current node is {0}", new Object[] { lastObj });
        return (lastObj instanceof DAVTreeNode) ? (DAVTreeNode) lastObj : null;
    }

    public void refreshNode(DAVTreeNode node) {
        assert node != null : "node does not equal null";
        treeModel.refreshNode(node);
        if (node.equals(getSelectedNode())) setTableFile(node.getDAVFile());
    }

    public void refreshNode(DAVTreeNode node, boolean useAllprop) {
        assert node != null : "node does not equal null";
        treeModel.refreshNode(node, useAllprop);
        if (node.equals(getSelectedNode())) setTableFile(node.getDAVFile());
    }

    /**
     * utility method for showing a DAVFileChooser
     * @param node a node on the DAVServer where the user may choose a file
     * @param title the title of the choice dialog
     * @param editable whether the user may freely enter a new filename
     * @param initialFileName the initial contents of the filename entry field
     * @return a struct of information about the chosen file, or null
     *
     */
    public DAVFileChooser.ChoiceStruct chooseFile(DAVTreeNode node, String title, boolean editable, String initialFileName) {
        DAVFileChooser chooser = (node == null) ? getDAVFileChooser() : getDAVFileChooser(node);
        chooser.setSelectionMode(TreeNodeChooser.SelectionMode.LEAF_ONLY);
        if (initialFileName != null) {
            chooser.setEntryFieldText(initialFileName);
            chooser.setEntryFieldTextSticky(true);
        }
        chooser.setEntryFieldEditable(editable);
        int option = JOptionPane.showConfirmDialog(this, chooser, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon());
        if (option != JOptionPane.OK_OPTION) {
            log.trace("user cancelled ");
            return null;
        }
        DAVFileChooser.ChoiceStruct struct = chooser.getChoiceStruct();
        Object[] retStruct = new Object[4];
        Object lastPathComponent = chooser.getSelectedPath().getLastPathComponent();
        DAVTreeNode parentNode = struct.getParentNode();
        struct.setParentNode(treeModel.getNodeMatchingPath(parentNode.getDAVFile().getFullName()));
        return struct;
    }

    public DAVFileChooser getDAVFileChooser(ServerData[] serversToConnect) {
        final DefaultMutableTreeNode chooserRoot = new DefaultMutableTreeNode("chooser");
        final DAVTreeModel chooserModel = new DAVTreeModel(chooserRoot, true);
        final int numServers = serversToConnect.length;
        for (int i = 0; i < numServers; i++) {
            chooserModel.addConnectionNode(serversToConnect[i], true, null);
        }
        DAVFileChooser chooser = new DAVFileChooser(chooserModel);
        Object firstChild = chooserModel.getChild(chooserRoot, 0);
        chooser.setCurrentPath(new TreePath(chooserModel.getPathToRoot((TreeNode) firstChild)));
        return chooser;
    }

    public DAVFileChooser getDAVFileChooser() {
        return getDAVFileChooser(getConnectedServers());
    }

    public DAVFileChooser getDAVFileChooser(DAVTreeNode node) {
        ServerData sd = getConnection(node);
        DAVFileChooser chooser = getDAVFileChooser(new ServerData[] { sd });
        return chooser;
    }

    public void addTableModelListener(TableModelListener tml) {
        tableModel.addTableModelListener(tml);
    }

    public void removeTableModelListener(TableModelListener tml) {
        tableModel.removeTableModelListener(tml);
    }

    public void addTreeModelListener(TreeModelListener tml) {
        treeModel.addTreeModelListener(tml);
    }

    public void removeTreeModelListener(TreeModelListener tml) {
        treeModel.removeTreeModelListener(tml);
    }

    protected void finalize() throws Throwable {
        allExplorers.remove(this);
    }
}
