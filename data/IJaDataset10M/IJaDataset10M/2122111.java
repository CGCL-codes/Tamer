package eyeswindle.tools;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * A tool to browse game content by location.  Bif file locations are read
 * from the game's KEY file.
 */
final class ContentBrowser {

    private static final String PREF_LAST_OPENED_FILE = "LastOpenedFile";

    private JFrame frame;

    private JScrollPane tree_container;

    private JPanel table_viewer;

    private JPanel resource_viewer;

    private File current_key_file;

    private Key current_key;

    public static void main(String[] args) {
        Runnable doCreateAndShowGUI = new Runnable() {

            public void run() {
                ContentBrowser browser = new ContentBrowser();
                browser.createAndShowGUI();
            }
        };
        SwingUtilities.invokeLater(doCreateAndShowGUI);
    }

    private void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        frame = new JFrame("Content Browser");
        tree_container = new JScrollPane();
        table_viewer = new JPanel();
        resource_viewer = new JPanel();
        JSplitPane right_side = new JSplitPane(JSplitPane.VERTICAL_SPLIT, table_viewer, resource_viewer);
        right_side.setDividerLocation(200);
        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree_container, right_side);
        jsp.setDividerLocation(200);
        frame.getContentPane().add(jsp, BorderLayout.CENTER);
        createMenuBar();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem fileOpen = new JMenuItem("Open...");
        fileOpen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                fileOpen();
            }
        });
        menu.add(fileOpen);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }

    private void fileOpen() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("KEY files", "key");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        Preferences prefs = Preferences.userNodeForPackage(ContentBrowser.class);
        String last_opened_file = prefs.get(PREF_LAST_OPENED_FILE, null);
        if (last_opened_file != null) {
            File tmpfile = new File(last_opened_file);
            chooser.setCurrentDirectory(tmpfile);
        }
        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File key_file = chooser.getSelectedFile();
            try {
                current_key = KeyReader.readKeyFile(key_file);
                BifEntry[] bif_entries = current_key.getBifEntries();
                Arrays.sort(bif_entries, new Comparator<BifEntry>() {

                    public int compare(BifEntry o1, BifEntry o2) {
                        return o1.getFilename().compareTo(o2.getFilename());
                    }
                });
                current_key_file = key_file;
                JTree tree = createTree(current_key_file.getName(), bif_entries);
                tree_container.setViewportView(tree);
            } catch (IOException e) {
                showExceptionDialog(e);
            } finally {
                prefs.put(PREF_LAST_OPENED_FILE, key_file.getAbsolutePath());
            }
        }
    }

    private JTree createTree(String keyName, BifEntry[] bif_entries) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(keyName);
        for (BifEntry bifEntry : bif_entries) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(bifEntry) {

                public String toString() {
                    BifEntry be = (BifEntry) getUserObject();
                    return be.getFilename();
                }
            };
            top.add(child);
        }
        TreeSelectionListener listener = new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                TreePath treePath = e.getPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                Object obj = node.getUserObject();
                if (obj instanceof BifEntry) {
                    bifSelected((BifEntry) obj);
                }
            }
        };
        JTree result = new JTree(top);
        result.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        result.addTreeSelectionListener(listener);
        return result;
    }

    private void bifSelected(BifEntry be) {
        String bif_name = be.getFilename();
        String key_parent = current_key_file.getParent();
        String bif_filename = Util.lookupIgnoreCase(key_parent, bif_name);
        if (bif_filename == null) {
            JOptionPane.showMessageDialog(frame, "File not found: " + bif_name, "File not found", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Bif bif = null;
        try {
            bif = BifReader.readBif(bif_filename);
        } catch (IOException e) {
            showExceptionDialog(e);
            return;
        }
        String[] columnNames = { "Attribute", "Value" };
        final DefaultTableModel model = new DefaultTableModel(columnNames, 0) {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String qq = "\"";
        model.addRow(new String[] { "Signature", qq + bif.getSignature() + qq });
        model.addRow(new String[] { "Version", qq + bif.getVersion() + qq });
        FileEntry[] files = bif.getFileEntries();
        int bif_index = current_key.getBifIndex(bif_name);
        for (FileEntry fileEntry : files) {
            String res_type = Util.resourceType(fileEntry.getResourceType());
            ResourceEntry re = current_key.lookup(bif_index, fileEntry.getResourceLocator(), res_type);
            Object[] row = new Object[] { "File Entry", re };
            model.addRow(row);
        }
        TilesetEntry[] tilesets = bif.getTilesetEntries();
        for (TilesetEntry tilesetEntry : tilesets) {
            String res_type = Util.resourceType(tilesetEntry.getResourceType());
            ResourceEntry re = current_key.lookup(bif_index, tilesetEntry.getResourceLocator(), res_type);
            String res_name = (re == null) ? "null" : (re.getResRef() + res_type);
            Object[] row = new Object[] { "Tileset Entry", res_name };
            model.addRow(row);
        }
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel ls_model = table.getSelectionModel();
        ls_model.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) event.getSource();
                if (!lsm.isSelectionEmpty()) {
                    int minIndex = lsm.getMinSelectionIndex();
                    int maxIndex = lsm.getMaxSelectionIndex();
                    for (int i = minIndex; i <= maxIndex; i++) {
                        if (lsm.isSelectedIndex(i)) {
                            Object obj = model.getValueAt(i, 1);
                            if (obj != null) {
                                if (obj instanceof ResourceEntry) {
                                    resourceSelected((ResourceEntry) obj);
                                }
                                if (obj instanceof TilesetEntry) {
                                    tilesetSelected((TilesetEntry) obj);
                                }
                            }
                        }
                    }
                }
            }
        });
        JScrollPane scroller = new JScrollPane(table);
        table_viewer.removeAll();
        table_viewer.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        table_viewer.add(scroller, constraints);
        table_viewer.revalidate();
    }

    private void showExceptionDialog(Exception e) {
        JOptionPane.showMessageDialog(frame, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
    }

    private void resourceSelected(ResourceEntry entry) {
    }

    private void tilesetSelected(TilesetEntry entry) {
    }
}
