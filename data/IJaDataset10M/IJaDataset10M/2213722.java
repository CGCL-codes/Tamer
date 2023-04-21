package swingextras;

import org.communications.CommunicationManager;
import org.communications.CommunicationManager.STATUS;
import org.communications.ConnectionInfoListListener;
import org.communications.ConnectionListener;
import org.communications.ConnectionStatusChangeEvent;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import swingextras.icons.IconManager;

/**
 * A panel used to display and edit available connections
 * @param <Manager> the connection manager
 * @param <ConInfo> the connection information
 * @author  Joao Leal
 */
public abstract class AbstractConnectionManagerPanel<Manager extends CommunicationManager<?, ConInfo>, ConInfo> extends javax.swing.JPanel implements ConnectionListener, ConnectionInfoListListener<ConInfo> {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("swingextras/i18n/PGConnection");

    /**
     * The connection manager
     */
    protected final Manager manager;

    /**
     * Creates new form AbstractConnectionManagerPanel
     * @param manager The communication manager
     */
    public AbstractConnectionManagerPanel(Manager manager) {
        this.manager = manager;
        initComponents();
        TableColumn col = jTable.getColumnModel().getColumn(0);
        col.setMaxWidth(64);
        col.setPreferredWidth(32);
        col = jTable.getColumnModel().getColumn(1);
        col.setCellRenderer(new ActiveConnectionCellRenderer());
        col.setMinWidth(32);
        col.setMaxWidth(32);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        manager.support.addConnectionListener(this);
        manager.conListInfo.addConnectionListListener(this);
        jTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateButtons();
                }
            }
        });
        updateButtons();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jButtonMoveUp = new javax.swing.JButton();
        jButtonMoveDown = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonActive = new javax.swing.JButton();
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        setPreferredSize(new java.awt.Dimension(540, 300));
        setLayout(new java.awt.GridBagLayout());
        jScrollPane.setPreferredSize(new java.awt.Dimension(450, 300));
        jTable.setModel(new ConnectionTableModel());
        jScrollPane.setViewportView(jTable);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane, gridBagConstraints);
        jPanel2.setLayout(new java.awt.GridBagLayout());
        jButtonAdd.setIcon(IconManager.getIcon("16x16/actions/edit_add.png"));
        jButtonAdd.setText(bundle.getString("Add..."));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jButtonAdd, gridBagConstraints);
        jButtonRemove.setIcon(IconManager.getIcon("16x16/actions/edit_remove.png"));
        jButtonRemove.setText(bundle.getString("Remove"));
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 10, 5);
        jPanel2.add(jButtonRemove, gridBagConstraints);
        jButtonMoveUp.setIcon(IconManager.getIcon("16x16/actions/1uparrow.png"));
        jButtonMoveUp.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveUpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        jPanel2.add(jButtonMoveUp, gridBagConstraints);
        jButtonMoveDown.setIcon(IconManager.getIcon("16x16/actions/1downarrow.png"));
        jButtonMoveDown.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMoveDownActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jButtonMoveDown, gridBagConstraints);
        jButtonEdit.setText(bundle.getString("Edit..."));
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jButtonEdit, gridBagConstraints);
        jButtonActive.setIcon(IconManager.getIcon("16x16/actions/connect_creating.png"));
        jButtonActive.setText(bundle.getString("Activate"));
        jButtonActive.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jButtonActive, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanel2, gridBagConstraints);
    }

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        int index = jTable.getSelectedRow();
        if (index >= 0) {
            index = jTable.convertRowIndexToModel(index);
            ConInfo ci = manager.conListInfo.get(index);
            if (!manager.conListInfo.getInfo().equals(ci) || manager.getConnectionStatus() != STATUS.CONNECTED) {
                manager.conListInfo.remove(ci);
            }
        }
    }

    private void jButtonMoveUpActionPerformed(java.awt.event.ActionEvent evt) {
        moveConnection(-1);
    }

    private void jButtonMoveDownActionPerformed(java.awt.event.ActionEvent evt) {
        moveConnection(1);
    }

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {
        displayAddDialog();
    }

    /**
     * Display the dialog used to add new connections
     */
    public abstract void displayAddDialog();

    private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {
        int index = jTable.getSelectedRow();
        if (index >= 0) {
            index = jTable.convertRowIndexToModel(index);
            ConInfo ci = manager.conListInfo.get(index);
            displayEditDialog(ci);
        }
    }

    /**
     * Displays the dialog used to edit existing connections
     * @param ci the connection information object
     */
    public abstract void displayEditDialog(ConInfo ci);

    private void jButtonActiveActionPerformed(java.awt.event.ActionEvent evt) {
        int index = jTable.getSelectedRow();
        if (index >= 0) {
            index = jTable.convertRowIndexToModel(index);
            manager.conListInfo.setActive(index);
        }
    }

    private void moveConnection(int moves) {
        int index = jTable.getSelectedRow();
        if (index >= 0) {
            index = jTable.convertRowIndexToModel(index);
            ConInfo ci = manager.conListInfo.get(index);
            manager.conListInfo.move(ci, moves);
            ((AbstractTableModel) jTable.getModel()).fireTableDataChanged();
        }
    }

    private javax.swing.JButton jButtonActive;

    private javax.swing.JButton jButtonAdd;

    private javax.swing.JButton jButtonEdit;

    private javax.swing.JButton jButtonMoveDown;

    private javax.swing.JButton jButtonMoveUp;

    private javax.swing.JButton jButtonRemove;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane;

    protected final javax.swing.JTable jTable = new javax.swing.JTable();

    private void updateButtons() {
        int row = jTable.getSelectedRow();
        if (row >= 0) {
            row = jTable.convertRowIndexToModel(row);
            if ((Boolean) jTable.getModel().getValueAt(row, 1)) {
                jButtonRemove.setEnabled(false);
                jButtonEdit.setEnabled(false);
                jButtonActive.setEnabled(false);
            } else {
                jButtonRemove.setEnabled(true);
                jButtonEdit.setEnabled(true);
                jButtonActive.setEnabled(true);
            }
            if (row > 0) {
                jButtonMoveUp.setEnabled(true);
            } else {
                jButtonMoveUp.setEnabled(false);
            }
            if (row < jTable.getModel().getRowCount() - 1) {
                jButtonMoveDown.setEnabled(true);
            } else {
                jButtonMoveDown.setEnabled(false);
            }
        } else {
            jButtonRemove.setEnabled(false);
            jButtonEdit.setEnabled(false);
            jButtonMoveUp.setEnabled(false);
            jButtonMoveDown.setEnabled(false);
            jButtonActive.setEnabled(false);
        }
    }

    @Override
    public void connectionStateChanged(ConnectionStatusChangeEvent e) {
        ((AbstractTableModel) jTable.getModel()).fireTableDataChanged();
    }

    @Override
    public void connectionInfoListChanged(final ConInfo ci, int index) {
        ((AbstractTableModel) jTable.getModel()).fireTableDataChanged();
        if (index >= 0) {
            int pos = jTable.convertRowIndexToView(index);
            jTable.getSelectionModel().setSelectionInterval(pos, pos);
            System.out.println(pos + "  " + jTable.getSelectedRow());
        }
    }

    /**
     * Removes the listeners from the connection manager when the panel is
     * no longer needed
     */
    public void close() {
        manager.support.removeConnectionListener(this);
        manager.conListInfo.removeConnectionListListener(this);
    }

    /**
     * Returns the column headers to be used in a JTable
     * @return the column headers to be used in a JTable
     */
    public abstract String[] getConnectionInfoTableHeader();

    /**
     * Returns the value for a given table column and connection information
     * @param ci the connection information
     * @param column the column index
     * @return the string for that column
     */
    public abstract Object getConnectionInfoValue(ConInfo ci, int column);

    /**
     * 
     * @author Joao Leal
     */
    @SuppressWarnings("serial")
    class ConnectionTableModel extends AbstractTableModel {

        private Class[] types;

        private String[] header;

        public ConnectionTableModel() {
            super();
            String[] subheader = getConnectionInfoTableHeader();
            header = new String[2 + subheader.length];
            types = new Class[2 + subheader.length];
            header[0] = "";
            types[0] = Integer.class;
            header[1] = "";
            types[1] = Boolean.class;
            for (int subcol = 0; subcol < subheader.length; subcol++) {
                header[subcol + 2] = subheader[subcol];
                types[subcol + 2] = String.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            return header[column];
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public int getRowCount() {
            return manager.conListInfo.size();
        }

        @Override
        public int getColumnCount() {
            return header.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= 0 && rowIndex < manager.conListInfo.size()) {
                ConInfo ci = manager.conListInfo.get(rowIndex);
                switch(columnIndex) {
                    case 0:
                        return rowIndex + 1;
                    case 1:
                        return manager.isConnectionInfoEqual(ci, manager.conListInfo.getInfo()) && manager.getConnectionStatus() == STATUS.CONNECTED;
                    default:
                        return getConnectionInfoValue(ci, columnIndex - 2);
                }
            }
            return null;
        }
    }
}

/**
 * 
 * @author Joao Leal
 */
@SuppressWarnings("serial")
class ActiveConnectionCellRenderer extends org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer {

    private static ImageIcon connectedIcon = IconManager.getIcon("16x16/actions/connect_established.png");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
        if (value instanceof Boolean && ((Boolean) value)) {
            cell.setHorizontalAlignment(JLabel.CENTER);
            cell.setIcon(connectedIcon);
        } else {
            cell.setIcon(null);
        }
        return cell;
    }
}
