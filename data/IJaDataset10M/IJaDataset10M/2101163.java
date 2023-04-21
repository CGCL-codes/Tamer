package newgen.presentation.circulation.ill;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import java.io.StringReader;

/**
 *
 * @author  PIXEL1
 */
public class ListNGLILLLibrariesPanel extends javax.swing.JPanel {

    DefaultTableModel libsTableModel = null;

    private int mode = 0;

    private int NEW = 1;

    private int EDIT = 2;

    private int DELETE = 3;

    newgen.presentation.NewGenMain newGenMain = newgen.presentation.NewGenMain.getAppletInstance();

    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();

    private newgen.presentation.component.ServletConnector servletConnector = newgen.presentation.component.ServletConnector.getInstance();

    private newgen.presentation.component.Utility utility = newgen.presentation.component.Utility.getInstance();

    /** Creates new form ListNGLILLLibrariesPanel */
    public ListNGLILLLibrariesPanel() {
        initComponents();
        setTable();
        getDetails();
    }

    public void setTable() {
        Object[] columns = { " ", newGenMain.getMyResource().getString("Id"), newGenMain.getMyResource().getString("LibraryName") };
        libsTableModel = new DefaultTableModel(columns, 0) {

            public boolean isCellEditable(int r, int c) {
                if (c == 0) return true; else return false;
            }

            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        illLibsTable.setModel(libsTableModel);
        illLibsTable.getColumnModel().getColumn(0).setMinWidth(50);
        illLibsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        TableColumn tc = illLibsTable.getColumnModel().getColumn(0);
        tc.setHeaderRenderer(new CheckBoxHeader(new MyItemListener()));
        illLibsTable.getColumnModel().getColumn(1).setMinWidth(0);
        illLibsTable.getColumnModel().getColumn(1).setMaxWidth(0);
        illLibsTable.getColumnModel().getColumn(2).setMinWidth(50);
        illLibsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        illLibsTable.getTableHeader().setReorderingAllowed(false);
        illLibsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        bnNew = new javax.swing.JButton();
        bnEdit = new javax.swing.JButton();
        bnDelete = new javax.swing.JButton();
        bnRefresh = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        illLibsTable = new JTable() {

            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    try {
                        jc.setToolTipText((String) getValueAt(rowIndex, vColIndex));
                    } catch (Exception e) {
                    }
                }
                return c;
            }
        };
        setLayout(new java.awt.BorderLayout());
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(60, 100));
        bnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/New16.gif")));
        bnNew.setMnemonic('n');
        bnNew.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("New"));
        bnNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(bnNew, gridBagConstraints);
        bnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Edit16.gif")));
        bnEdit.setMnemonic('t');
        bnEdit.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Edit"));
        bnEdit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel1.add(bnEdit, gridBagConstraints);
        bnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Delete16.gif")));
        bnDelete.setMnemonic('d');
        bnDelete.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Delete"));
        bnDelete.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel1.add(bnDelete, gridBagConstraints);
        bnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Refresh16.gif")));
        bnRefresh.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Refresh"));
        bnRefresh.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnRefreshActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel1.add(bnRefresh, gridBagConstraints);
        add(jPanel1, java.awt.BorderLayout.EAST);
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        illLibsTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { " ", "Library name" }) {

            Class[] types = new Class[] { java.lang.Boolean.class, java.lang.Object.class };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jScrollPane1.setViewportView(illLibsTable);
        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }

    private void checkRow() {
        int cnt = 0;
        int rowValue = 0;
        int rc = illLibsTable.getRowCount();
        for (int i = 0; i < rc; i++) {
            if (illLibsTable.getValueAt(i, 0).toString().equalsIgnoreCase("true")) {
                cnt++;
                rowValue = i;
            }
        }
        if (cnt == 1) {
            int val = new javax.swing.JOptionPane().showConfirmDialog(this, "Do you want to delete this library", "Confirm Delete", 0);
            System.out.println("value of OPTION" + val);
            if (val == 0) {
                String otherlib = illLibsTable.getValueAt(rowValue, 1).toString();
                NGLILLLibraryDialog pane = new NGLILLLibraryDialog();
                pane.deleteLibrary(otherlib);
                refresh();
            } else {
            }
        }
        if (cnt == 0) {
            new javax.swing.JOptionPane().showMessageDialog(this, "Select One Record To Delete....");
        } else if (cnt > 1) {
            new javax.swing.JOptionPane().showMessageDialog(this, "Select One Record To Delete....");
        }
    }

    private void bnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        checkRow();
        NGLILLLibraryDialog pane = new NGLILLLibraryDialog();
        pane.setMode(DELETE);
    }

    private void bnRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        refresh();
    }

    public void getDetails() {
        int i = 0;
        String otherLibId = "";
        String othlibname = "";
        Element opId = new Element("OperationId");
        opId.setAttribute("no", "1");
        Element libId = new Element("LibraryID");
        libId.setText(newGenMain.getAppletInstance().getLibraryID().toString());
        opId.addContent(libId);
        String xml = newGenXMLGenerator.buildXMLDocument(opId);
        String response = servletConnector.getInstance().sendRequest("OtherLibraryServlet", xml);
        Element resp = newGenXMLGenerator.getRootElement(response);
        Element othlib = resp.getChild("OtherLibrary");
        java.util.List othList = resp.getChildren();
        for (i = 0; i < othList.size(); i++) {
            java.util.Vector vector = new java.util.Vector();
            Element otherLibrary = (Element) othList.get(i);
            otherLibId = otherLibrary.getChildText("OtherLibraryId");
            othlibname = otherLibrary.getChildText("OtherLibraryName");
            vector.addElement(new Boolean(false));
            vector.addElement(otherLibId);
            vector.addElement(othlibname);
            libsTableModel.addRow(vector);
        }
    }

    public void getRowDetails() {
        int cnt = 0;
        int rowcnt = illLibsTable.getRowCount();
        for (int i = 0; i < rowcnt; i++) {
            String val = illLibsTable.getValueAt(i, 0).toString();
            if (val.equalsIgnoreCase("true")) {
                cnt++;
            }
        }
        if (cnt == 1) {
            int selRow = illLibsTable.getSelectedRow();
            String olibid = illLibsTable.getValueAt(selRow, 1).toString();
            NGLILLLibraryDialog pane = new NGLILLLibraryDialog();
            pane.setMode(EDIT);
            pane.getModifiedDetails(olibid);
        } else if (cnt == 0) {
            new javax.swing.JOptionPane().showMessageDialog(this, "Select One Row.... ");
        } else {
            new javax.swing.JOptionPane().showMessageDialog(this, "Select One Row.... ");
        }
    }

    private void bnEditActionPerformed(java.awt.event.ActionEvent evt) {
        getRowDetails();
        refresh();
    }

    private void bnNewActionPerformed(java.awt.event.ActionEvent evt) {
        NGLILLLibraryDialog pane = new NGLILLLibraryDialog();
        pane.setMode(NEW);
        pane.setVisible(true);
        refresh();
    }

    public void refresh() {
        libsTableModel.setRowCount(0);
        getDetails();
    }

    private javax.swing.JButton bnDelete;

    private javax.swing.JButton bnEdit;

    private javax.swing.JButton bnNew;

    private javax.swing.JButton bnRefresh;

    private javax.swing.JTable illLibsTable;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    class MyItemListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            Object source = e.getSource();
            if (source instanceof AbstractButton == false) return;
            boolean checked = e.getStateChange() == ItemEvent.SELECTED;
            for (int x = 0, y = illLibsTable.getRowCount(); x < y; x++) {
                illLibsTable.setValueAt(new Boolean(checked), x, 0);
            }
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
