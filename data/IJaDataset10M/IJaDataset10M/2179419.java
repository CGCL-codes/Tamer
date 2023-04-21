package alexandria.gui;

import alexandria.core.Config;
import alexandria.core.Item;
import alexandria.core.Store;
import com.sleepycat.je.DatabaseException;
import javax.swing.JFrame;
import junit.framework.JUnit4TestAdapter;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Visual components testing frame
 *
 * @author  neiko
 */
public class TestForm extends JFrame {

    /**
     * Logger instance to be used with this test
     */
    private Logger logger = Logger.getLogger(TestForm.class);

    /**
     * Flag shows if the test is completed
     */
    private boolean completed = false;

    /**
     * Store to be used for this form
     */
    private Store store = new Store(System.getProperty("user.home").concat("/.alexandria/test"));

    /**
     * Item in the store to be used as a tree root
     */
    private Item root;

    /**
     * Creates new form TestFrame
     */
    public TestForm() {
        try {
            store.open();
            root = store.getItemsById().get(1L);
            if (root == null) {
                root = new Item("root");
                store.getItemsById().putNoReturn(root);
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        initComponents();
    }

    private void initComponents() {
        javax.swing.JButton addButton;
        javax.swing.JButton closeButton;
        javax.swing.JScrollPane scroll;
        scroll = new javax.swing.JScrollPane();
        tree = new ItemsTree(store);
        addButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Alexandria Testing Frame");
        scroll.setViewportView(tree);
        addButton.setText("Add item");
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddPutton(evt);
            }
        });
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCloseButton(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, scroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE).add(layout.createSequentialGroup().add(addButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(closeButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(scroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(closeButton).add(addButton)).addContainerGap()));
        pack();
    }

    /**
     * Adds new item to the tree
     */
    private void onAddPutton(java.awt.event.ActionEvent evt) {
        Item item = new Item("sample item");
        Item selected = (Item) tree.getSelectionPath().getLastPathComponent();
        logger.debug("node " + selected.getId() + " is selected");
        ((ItemsTreeModel) tree.getModel()).insertNode(item, selected);
    }

    private void onCloseButton(java.awt.event.ActionEvent evt) {
        dispose();
        store.close();
        setCompleted(true);
    }

    private javax.swing.JTree tree;

    public synchronized boolean isCompleted() {
        return completed;
    }

    public synchronized void setCompleted(boolean aCompleted) {
        completed = aCompleted;
    }

    public synchronized void waitUntilCompleted() throws Exception {
        while (!isCompleted()) wait(200);
    }
}
