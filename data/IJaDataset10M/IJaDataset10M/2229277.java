package cu.edu.cujae.biowh.gui.component.table;

import cu.edu.cujae.biowh.BioWH2App;
import cu.edu.cujae.biowh.gui.component.filechooser.FileChooser;
import cu.edu.cujae.biowh.gui.component.filechooser.FileFilterGeneric;
import cu.edu.cujae.biowh.gui.component.table.model.ListTableModel;
import cu.edu.cujae.biowh.logger.VerbLogger;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.jdesktop.application.Action;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * This Dialog is the abstract class to visualize a list of Entities returned from a query
 * @param <T> The entity to be visualized
 * @author rvera
 * @version 1.0
 * @since Nov 29, 2011
 */
public abstract class AbstractListViewListTableDialog<T extends Object> extends JDialog {

    /** Creates new form AbstractListViewListTableDialog
     * @param parent the Frame from which the dialog is displayed
     * @param modal specifies whether dialog blocks user input to other top-level windows when shown. If true, the modality type property is set to DEFAULT_MODALITY_TYPE, otherwise the dialog is modeless
     * @param collection The collection with the entity <T> to be visualized
     */
    public AbstractListViewListTableDialog(java.awt.Frame parent, boolean modal, Collection<T> collection) {
        super(parent, modal);
        this.mainFrame = BioWH2App.getApplication().getMainFrame();
        this.collection = collection;
        initComponents();
    }

    /**
     * Return the field to be visualized from the collection
     * @param data The class <T> to extract the field to be visualized
     * @return a List<Object> with the selected fields
     */
    public abstract List<Object> getCollectionColumns(T data);

    /**
     * Get the collection element from the table row
     * @param row The table row
     * @return a <T> object
     */
    public abstract T getCollectionElementFromTableRow(int row);

    /**
     * Create the Table Header
     * @return a String array with the column header names
     */
    public abstract String[] createJTableHeader();

    /**
     * Show the entity visualized class
     */
    public abstract void showEntityClass();

    /**
     * Create the Class array to build the Table
     * @return a Class array with the column class types
     */
    public Class[] createClass() {
        int length = createJTableHeader().length;
        Class[] classes = new Class[length];
        classes[0] = java.lang.Boolean.class;
        for (int i = 1; i < length; i++) {
            classes[i] = java.lang.Object.class;
        }
        return classes;
    }

    /**
     * Create the Edit array
     * @return a Boolean array with the editable column
     */
    public boolean[] createCanEdit() {
        int length = createJTableHeader().length;
        boolean[] canEdit = new boolean[length];
        canEdit[0] = true;
        for (int i = 1; i < length; i++) {
            canEdit[i] = false;
        }
        return canEdit;
    }

    /**
     * Set the table dimensions
     */
    public void setTableDimensions() {
        ListTableModel model = (ListTableModel) jTable.getModel();
        getjTable().getColumnModel().getColumn(0).setMinWidth(30);
        getjTable().getColumnModel().getColumn(0).setPreferredWidth(30);
        getjTable().getColumnModel().getColumn(0).setMaxWidth(30);
        if (model.getColumnCount() > 2) {
            getjTable().getColumnModel().getColumn(1).setMinWidth(120);
            getjTable().getColumnModel().getColumn(1).setPreferredWidth(120);
            getjTable().getColumnModel().getColumn(1).setMaxWidth(120);
        }
    }

    /**
     * The save list method
     */
    @Action
    public void saveList() {
        FileChooser filechooser = new FileChooser(mainFrame, true);
        filechooser.getFileChooser().addChoosableFileFilter(new FileFilterGeneric("txt"));
        filechooser.setVisible(true);
        PrintWriter fileOUT = null;
        String fileName = null;

        if (filechooser.getFile() != null) {
            VerbLogger.println(AbstractListViewListTableDialog.class.getName(), "Writing on file: " + filechooser.getFile());

            if (filechooser.getFileChooser().getFileFilter().getDescription().equals("*.txt")
                    || filechooser.getFileChooser().getFileFilter().getDescription().equals("All Files")) {
                if (filechooser.getFile().toString().endsWith(".txt")) {
                    fileName = filechooser.getFile().toString();
                } else {
                    fileName = filechooser.getFile().toString() + ".txt";
                }
            }

            try {
                fileOUT = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            } catch (IOException e) {
            }

            ListTableModel model = (ListTableModel) getjTable().getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                if ((Boolean) model.getValueAt(i, 0) == true) {
                    for (int j = 1; j < model.getColumnCount(); j++) {
                        if (j < model.getColumnCount() - 1) {
                            fileOUT.print(model.getValueAt(i, j) + "\t");
                        } else {
                            fileOUT.println(model.getValueAt(i, j));
                        }
                    }
                }
            }
            fileOUT.flush();
            fileOUT.close();
        }
    }

    /**
     * Perform the search over the collection selected field
     * @param s The string to be searched
     * @param data The <T> object to perform the search
     * @return True is the collection fields contains the string s
     */
    public boolean collectionFieldContains(String s, T data) {
        for (Object d : getCollectionColumns(data)) {
            if (d instanceof String) {
                if (((String) d).contains(s)) {
                    return true;
                }
            } else if (d instanceof Long) {
                if (((Long) d).toString().contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the collection
     * @return a Collection<T> object
     */
    public Collection<T> getCollection() {
        return collection;
    }

    /**
     * Set the collection
     * @param collection a Collection<T> object
     */
    public void setCollection(Collection<T> collection) {
        this.collection = collection;
    }

    /**
     * Get the close button
     * @return a JButton object
     */
    public JButton getjBClose() {
        return jBClose;
    }

    /**
     * Get the found label
     * @return a JLabel object
     */
    public JLabel getjLFound() {
        return jLFound;
    }

    /**
     * Get the Table
     * @return a JTable object
     */
    public JTable getjTable() {
        return jTable;
    }

    /**
     * Get the list with the selected elements
     * @return a Collection<T> with the selected elements
     */
    public Collection<T> getSelectedElements() {
        Collection<T> result = new ArrayList<>();

        ListTableModel model = (ListTableModel) getjTable().getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((Boolean) model.getValueAt(i, 0) == true) {
                result.add(getCollectionElementFromTableRow(i));
            }
        }

        return result;
    }

    private List<List<Object>> createTableContents() {
        ArrayList<List<Object>> row = new ArrayList<>();
        for (T content : getCollection()) {
            List<Object> column = getCollectionColumns(content);
            row.add(column);
        }
        return row;
    }

    /**
     * Close the windows
     */
    @Action
    public void close() {
        dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLSearch = new javax.swing.JLabel();
        jTSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable =         new javax.swing.JTable() {

            @Override
            public String getToolTipText(java.awt.event.MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);

                if (realColumnIndex != 0) {
                    TableModel model = getModel();

                    Object tipTemp = model.getValueAt(rowIndex, colIndex);
                    if (tipTemp != null) {
                        if (tipTemp instanceof String) {
                            tip = (String) tipTemp;
                        } else {
                            tip = tipTemp.toString();
                        }
                    }

                } else {
                    tip = super.getToolTipText(e);
                }
                return tip;
            }
        };
        jCheckAll = new javax.swing.JCheckBox();
        jLFound = new javax.swing.JLabel();
        jBClose = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMSave = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cu.edu.cujae.biowh.BioWH2App.class).getContext().getResourceMap(AbstractListViewListTableDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jLSearch.setText(resourceMap.getString("jLSearch.text")); // NOI18N
        jLSearch.setName("jLSearch"); // NOI18N

        jTSearch.setName("jTSearch"); // NOI18N
        jTSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTSearchKeyReleased(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable.setModel(new ListTableModel(createJTableHeader(),
            createTableContents(), createClass(), createCanEdit()));
    jTable.setColumnSelectionAllowed(true);
    jTable.setFillsViewportHeight(true);
    jTable.setGridColor(new java.awt.Color(204, 204, 204));
    jTable.setName("jTable"); // NOI18N
    setTableDimensions();
    jTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTableMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(jTable);

    jCheckAll.setText(resourceMap.getString("jCheckAll.text")); // NOI18N
    jCheckAll.setName("jCheckAll"); // NOI18N
    jCheckAll.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jCheckAllMouseClicked(evt);
        }
    });

    jLFound.setText(resourceMap.getString("jLFound.text")); // NOI18N
    jLFound.setName("jLFound"); // NOI18N
    jLFound.setText("Found " + collection.size() + " elements");

    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(cu.edu.cujae.biowh.BioWH2App.class).getContext().getActionMap(AbstractListViewListTableDialog.class, this);
    jBClose.setAction(actionMap.get("close")); // NOI18N
    jBClose.setText(resourceMap.getString("jBClose.text")); // NOI18N
    jBClose.setName("jBClose"); // NOI18N

    jMenuBar1.setName("jMenuBar1"); // NOI18N

    jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
    jMenu1.setName("jMenu1"); // NOI18N

    jMSave.setAction(actionMap.get("saveList")); // NOI18N
    jMSave.setText(resourceMap.getString("jMSave.text")); // NOI18N
    jMSave.setName("jMSave"); // NOI18N
    jMenu1.add(jMSave);

    jMenuBar1.add(jMenu1);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLSearch)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckAll)
                        .addComponent(jLFound, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                    .addComponent(jBClose)))
            .addContainerGap())
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap()))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLSearch)
                .addComponent(jTSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 525, Short.MAX_VALUE)
            .addComponent(jCheckAll)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLFound)
                .addComponent(jBClose))
            .addContainerGap())
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE)))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckAllMouseClicked
        ListTableModel model = (ListTableModel) jTable.getModel();
        if (jCheckAll.isSelected()) {
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(true, i, 0);
            }
        } else {
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(false, i, 0);
            }
        }
    }//GEN-LAST:event_jCheckAllMouseClicked

    private void jTSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTSearchKeyReleased
        ListTableModel model = (ListTableModel) jTable.getModel();
        ArrayList<List<Object>> objects = new ArrayList<>();
        for (T data : collection) {
            if (collectionFieldContains(jTSearch.getText(), data)) {
                objects.add(getCollectionColumns(data));
            }
        }
        jLFound.setText("Found " + objects.size() + " elements");
        model.setContents(objects);
    }//GEN-LAST:event_jTSearchKeyReleased

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        if (getjTable().getSelectedColumn() != 0) {
            if (getjTable().getSelectedRow() >= 0 && getjTable().getSelectedRow() < getCollection().size()) {
                showEntityClass();
            }
        }
    }//GEN-LAST:event_jTableMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBClose;
    private javax.swing.JCheckBox jCheckAll;
    private javax.swing.JLabel jLFound;
    private javax.swing.JLabel jLSearch;
    private javax.swing.JMenuItem jMSave;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTSearch;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
    private Collection<T> collection;
    protected JFrame mainFrame;
}
