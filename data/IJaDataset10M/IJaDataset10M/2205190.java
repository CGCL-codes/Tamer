package org.hironico.dbtool2.wizard.table;

import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.hironico.database.SQLTableColumn;
import org.hironico.database.SQLTablePrimaryKey;
import org.hironico.database.driver.ConnectionPool;
import org.hironico.database.driver.ConnectionPoolManager;

/**
 * Ce panel est la première étape dans la création d'une table via le hironico
 * db tool. Pour commencer, on définit la structure de la table avec les colonnes,
 * leur type, et les règles de défaulting.
 * @author hironico
 * @since 2.1.0
 */
public class CreateTablePanel1 extends javax.swing.JPanel {

    protected static final Logger logger = Logger.getLogger("org.hironico.dbtool2.wizard.table");

    /**
     * Le cell editor pour les types de colonnes SQL.
     * @since 2.1.0
     */
    SQLTypesCellEditor cellEditor = null;

    /** Creates new form CreateTablePanel1 */
    public CreateTablePanel1() {
        initComponents();
        bannerTitle.setEndColor(CreateTablePanel1.this.getBackground());
        updateConnectionNames();
        String connectionName = null;
        if (cmbConnectionName.getItemCount() > 0) {
            connectionName = (String) cmbConnectionName.getSelectedItem();
        }
        cellEditor = new SQLTypesCellEditor(connectionName);
        tableColumnDefinitions.getColumn(2).setCellEditor(cellEditor);
        DefaultTableModel model = (DefaultTableModel) tableColumnDefinitions.getModel();
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    if (e.getColumn() == 0) {
                        Boolean pk = (Boolean) tableColumnDefinitions.getValueAt(e.getFirstRow(), 0);
                        if (pk) {
                            tableColumnDefinitions.setValueAt(Boolean.TRUE, e.getFirstRow(), 6);
                        }
                    }
                }
            }
        });
    }

    protected void updateConnectionNames() {
        cmbConnectionName.removeAllItems();
        Vector<String> names = ConnectionPoolManager.getInstance().getDatabaseNames();
        for (String name : names) {
            cmbConnectionName.addItem(name);
        }
    }

    protected void showPopupMenu(MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            popupCommands.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    /**
     * Permet d'insérer une définition de colonne à l'endroit spécifié par atRow
     * @param atRow numéro d'index où il faut insérer la nouvelle définition de colonne.
     * @since 2.1.0
     */
    protected void insertColumndefinition(int atRow) {
        DefaultTableModel model = (DefaultTableModel) tableColumnDefinitions.getModel();
        if (atRow < 0) {
            atRow = 0;
        }
        if (atRow > model.getRowCount()) {
            atRow = model.getRowCount();
        }
        Object[] data = new Object[model.getColumnCount()];
        data[0] = Boolean.FALSE;
        data[1] = "";
        data[2] = ((JComboBox) (cellEditor.getComponent())).getItemAt(0);
        data[3] = null;
        data[4] = null;
        data[5] = null;
        data[6] = Boolean.FALSE;
        model.insertRow(atRow, data);
        tableColumnDefinitions.editCellAt(atRow, 1);
    }

    /**
     * Permet de savoir si l'utilisateur a saisi des données valides. Par exemple,
     * on va vérifier que tous les champs de noms de colonnes et leur type sont bien remplis.
     * Sinon, on affiche une boite de dialogue pour signifier l'erreur.
     * @return true si tout est correctement rempli et false sinon.
     * @since 2.1.0
     */
    public boolean isValidUserData() {
        if ("".equals(txtTableName.getText().trim())) {
            JOptionPane.showMessageDialog(CreateTablePanel1.this, "You should provide a name to the table to create ...", "Hey !!!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (tableColumnDefinitions.getRowCount() == 0) {
            JOptionPane.showMessageDialog(CreateTablePanel1.this, "The new table should have at least one column...", "Hey!!!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        List<String> definedColumnNames = new ArrayList<String>();
        boolean result = true;
        for (int row = 0; row < tableColumnDefinitions.getRowCount(); row++) {
            String value = (String) tableColumnDefinitions.getValueAt(row, 1);
            if ((value == null) || "".equals(value.trim())) {
                result = false;
                break;
            }
            if (definedColumnNames.contains(value)) {
                result = false;
                break;
            } else {
                definedColumnNames.add(value);
            }
            value = (String) tableColumnDefinitions.getValueAt(row, 2);
            if ((value == null) || "".equals(value.trim())) {
                result = false;
                break;
            }
        }
        if (tableColumnDefinitions.getRowCount() == 0) {
            result = false;
        }
        if (!result) {
            JOptionPane.showMessageDialog(CreateTablePanel1.this, "Incorrect input data.\n" + "Please verify the column names and types.", "Hey !!!", JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    /**
     * Permet de retourner les properties correspondantes aux saisies de
     * l'utilisateur.
     * @return Properties des saisies par rapport aux contantes du CreateTableWizard
     */
    public Properties getUserInputProperties() {
        Properties props = new Properties();
        String tableName = txtTableName.getText().trim();
        String connectionName = (String) cmbConnectionName.getSelectedItem();
        ConnectionPool pool = ConnectionPoolManager.getInstance().getConnectionPool(connectionName);
        props.put(CreateTableWizard.CONNECTION_NAME, connectionName);
        props.put(CreateTableWizard.CREATETABLE_NAME, tableName);
        DefaultTableModel model = (DefaultTableModel) tableColumnDefinitions.getModel();
        List<SQLTableColumn> columnList = new ArrayList<SQLTableColumn>(model.getRowCount());
        List<SQLTablePrimaryKey> primaryKeyList = new ArrayList<SQLTablePrimaryKey>();
        for (int cpt = 0; cpt < model.getRowCount(); cpt++) {
            String columnName = (String) model.getValueAt(cpt, 1);
            SQLTableColumn col = new SQLTableColumn(null, null, columnName, tableName, pool, false);
            col.setColumnDef((String) model.getValueAt(cpt, 5));
            col.setNullable(!(Boolean) model.getValueAt(cpt, 6));
            col.setOrdinalPosition(cpt);
            Integer precision = (Integer) model.getValueAt(cpt, 4);
            if (precision == null) {
                col.setDecimalDigits(-1);
            } else {
                col.setDecimalDigits(precision);
            }
            Integer size = (Integer) model.getValueAt(cpt, 3);
            if (size == null) {
                col.setColumnSize(-1);
            } else {
                col.setColumnSize(size);
            }
            col.setTypeName((String) model.getValueAt(cpt, 2));
            if ((Boolean) model.getValueAt(cpt, 0)) {
                SQLTablePrimaryKey pk = new SQLTablePrimaryKey(null, null, "PK_" + tableName, tableName, pool, false);
                pk.setColumnName(columnName);
                primaryKeyList.add(pk);
            }
            columnList.add(col);
        }
        props.put(CreateTableWizard.CREATETABLE_COLUMNS, columnList);
        props.put(CreateTableWizard.CREATETABLE_PRIMARYKEYS, primaryKeyList);
        return props;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        popupCommands = new javax.swing.JPopupMenu();
        menuInsertColumnAbove = new javax.swing.JMenuItem();
        menuInsertColumnBelow = new javax.swing.JMenuItem();
        menuRemoveColumn = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuchkPrimaryKey = new javax.swing.JCheckBoxMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuchkNotNull = new javax.swing.JCheckBoxMenuItem();
        bannerTitle = new com.jidesoft.dialog.BannerPanel();
        pnlContent = new javax.swing.JPanel();
        lblConnectionName = new javax.swing.JLabel();
        cmbConnectionName = new javax.swing.JComboBox();
        lblTableName = new javax.swing.JLabel();
        txtTableName = new javax.swing.JTextField();
        scrollColumnDefinitions = new javax.swing.JScrollPane();
        tableColumnDefinitions = new org.jdesktop.swingx.JXTable();
        menuInsertColumnAbove.setText("Insert column above");
        menuInsertColumnAbove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInsertColumnAboveActionPerformed(evt);
            }
        });
        popupCommands.add(menuInsertColumnAbove);
        menuInsertColumnBelow.setText("Insert column below");
        menuInsertColumnBelow.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuInsertColumnBelowActionPerformed(evt);
            }
        });
        popupCommands.add(menuInsertColumnBelow);
        menuRemoveColumn.setText("Remove column(s)");
        menuRemoveColumn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveColumnActionPerformed(evt);
            }
        });
        popupCommands.add(menuRemoveColumn);
        popupCommands.add(jSeparator1);
        menuchkPrimaryKey.setText("Primary key");
        menuchkPrimaryKey.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuchkPrimaryKeyActionPerformed(evt);
            }
        });
        popupCommands.add(menuchkPrimaryKey);
        popupCommands.add(jSeparator2);
        menuchkNotNull.setText("Not null");
        menuchkNotNull.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuchkNotNullActionPerformed(evt);
            }
        });
        popupCommands.add(menuchkNotNull);
        setLayout(new java.awt.BorderLayout());
        bannerTitle.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        bannerTitle.setPreferredSize(null);
        bannerTitle.setStartColor(java.awt.Color.white);
        bannerTitle.setSubtitle("This step consist of describing the structure of the table. You must specify the columns names and their data types. Also, you can set some defaulting rules here.");
        bannerTitle.setTitle("Table structure definition");
        add(bannerTitle, java.awt.BorderLayout.NORTH);
        pnlContent.setLayout(new java.awt.GridBagLayout());
        lblConnectionName.setText("Connection name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContent.add(lblConnectionName, gridBagConstraints);
        cmbConnectionName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbConnectionName.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbConnectionNameItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlContent.add(cmbConnectionName, gridBagConstraints);
        lblTableName.setText("Table name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlContent.add(lblTableName, gridBagConstraints);
        txtTableName.setText("NEW_TABLE_NAME");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlContent.add(txtTableName, gridBagConstraints);
        scrollColumnDefinitions.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableColumnDefinitions.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableColumnDefinitions.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "PK?", "Column name", "Data type", "Size", "Scale", "Default", "Not null?" }) {

            Class[] types = new Class[] { java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        tableColumnDefinitions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableColumnDefinitions.setColumnControlVisible(true);
        tableColumnDefinitions.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableColumnDefinitionsMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableColumnDefinitionsMouseReleased(evt);
            }
        });
        scrollColumnDefinitions.setViewportView(tableColumnDefinitions);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContent.add(scrollColumnDefinitions, gridBagConstraints);
        add(pnlContent, java.awt.BorderLayout.CENTER);
    }

    private void cmbConnectionNameItemStateChanged(java.awt.event.ItemEvent evt) {
        if (cmbConnectionName == null) {
            return;
        }
        if (cellEditor == null) {
            return;
        }
        if (evt.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        cellEditor.setConnectionName((String) cmbConnectionName.getSelectedItem());
    }

    private void tableColumnDefinitionsMousePressed(java.awt.event.MouseEvent evt) {
        showPopupMenu(evt);
    }

    private void tableColumnDefinitionsMouseReleased(java.awt.event.MouseEvent evt) {
        showPopupMenu(evt);
    }

    private void menuInsertColumnAboveActionPerformed(java.awt.event.ActionEvent evt) {
        int index = tableColumnDefinitions.getSelectedRow();
        index--;
        insertColumndefinition(index);
    }

    private void menuInsertColumnBelowActionPerformed(java.awt.event.ActionEvent evt) {
        int index = tableColumnDefinitions.getSelectedRow();
        index++;
        insertColumndefinition(index);
    }

    private void menuRemoveColumnActionPerformed(java.awt.event.ActionEvent evt) {
        int index = tableColumnDefinitions.getSelectedRow();
        if (index < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableColumnDefinitions.getModel();
        model.removeRow(index);
    }

    private void menuchkPrimaryKeyActionPerformed(java.awt.event.ActionEvent evt) {
        int index = tableColumnDefinitions.getSelectedRow();
        if (index < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableColumnDefinitions.getModel();
        model.setValueAt(menuchkPrimaryKey.isSelected(), index, 0);
    }

    private void menuchkNotNullActionPerformed(java.awt.event.ActionEvent evt) {
        int index = tableColumnDefinitions.getSelectedRow();
        if (index < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableColumnDefinitions.getModel();
        model.setValueAt(menuchkNotNull.isSelected(), index, 6);
    }

    private com.jidesoft.dialog.BannerPanel bannerTitle;

    private javax.swing.JComboBox cmbConnectionName;

    private javax.swing.JPopupMenu.Separator jSeparator1;

    private javax.swing.JPopupMenu.Separator jSeparator2;

    private javax.swing.JLabel lblConnectionName;

    private javax.swing.JLabel lblTableName;

    private javax.swing.JMenuItem menuInsertColumnAbove;

    private javax.swing.JMenuItem menuInsertColumnBelow;

    private javax.swing.JMenuItem menuRemoveColumn;

    private javax.swing.JCheckBoxMenuItem menuchkNotNull;

    private javax.swing.JCheckBoxMenuItem menuchkPrimaryKey;

    private javax.swing.JPanel pnlContent;

    private javax.swing.JPopupMenu popupCommands;

    private javax.swing.JScrollPane scrollColumnDefinitions;

    private org.jdesktop.swingx.JXTable tableColumnDefinitions;

    private javax.swing.JTextField txtTableName;
}
