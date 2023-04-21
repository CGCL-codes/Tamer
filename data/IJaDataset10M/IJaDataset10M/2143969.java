package net.sf.swinggoodies.components.validator;

import javax.swing.JTabbedPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.swinggoodies.components.textcomponents.SGTextComponent;
import net.sf.swinggoodies.components.windows.SGWindowModel;
import java.awt.Font;

public class SGValidatorTable extends JTabbedPane {

    private static ArrayList<Integer> fieldHashCodeList;

    private static ArrayList<Integer> formHashCodeList;

    private static boolean isOpenedForm;

    private static final long serialVersionUID = 6125253705839693854L;

    private JPanel panel = null;

    private JScrollPane tableScrollPane = null;

    private JTable table = null;

    public static DefaultTableModel tableModel = null;

    /**
	 * This method initializes
	 */
    public SGValidatorTable() {
        super();
        init();
        initialize();
    }

    private void init() {
        fieldHashCodeList = new ArrayList<Integer>();
        formHashCodeList = new ArrayList<Integer>();
        isOpenedForm = true;
    }

    /**
	 * This method initializes this
	 */
    private void initialize() {
        this.setPreferredSize(new Dimension(700, 107));
        this.setFont(new Font("Dialog", Font.BOLD, 11));
        this.addTab("Validações", null, getPanel(), null);
    }

    /**
	 * Informa se existe uma entrada no tree view correspondente ao campo especificado
	 * 
	 * @param fieldName
	 * @return
	 */
    private static int getRow(int hashCode) {
        int row = -1;
        for (int i = 0; i < fieldHashCodeList.size(); i++) {
            if (fieldHashCodeList.get(i) == hashCode) {
                row = i;
            }
        }
        return row;
    }

    /**
	 * Add or remove rows to the grid
	 * 
	 * @param object
	 * @param message
	 * @param type
	 * @param displayMessage
	 */
    public static void update(SGTextComponent component, String message, boolean displayMessage) {
        if (component == null) {
            throw new IllegalArgumentException("Component can't be null");
        }
        int row = getRow(component.hashCode());
        if (displayMessage && row == -1) {
            Container form = component.getParent();
            while (!(form instanceof JRootPane)) {
                form = form.getParent();
            }
            form = form.getParent();
            addFormHashCode(form.hashCode());
            fieldHashCodeList.add(component.hashCode());
            if (isOpenedForm) {
                tableModel.addRow(new Object[] { "", message, ((SGWindowModel) form).getTitle(), component, form });
            }
            isOpenedForm = true;
        } else if (!displayMessage && row != -1) {
            tableModel.removeRow(row);
            fieldHashCodeList.remove(row);
        }
    }

    /**
	 * Check for error entries on the object array of the referenciated form
	 * 
	 * @param fieldHashCode
	 * @param formHashCode
	 */
    public static void check(ArrayList<Object> fieldHashCode, int formHashCode) {
    }

    public static void check(ArrayList<Object> fieldHashCode, int formHashCode, boolean displayMessage) {
    }

    /**
	 * Check for error entries relataded to the referenciated form
	 * 
	 * @param formHashCodeList
	 */
    public static boolean check(SGWindowModel form) {
        for (int i = 0; i < form.getRequiredField().size(); i++) {
            if (!form.getRequiredField().get(i).hasReceivedFocus()) {
                form.getRequiredField().get(i).getValidator().invoke();
            }
        }
        ArrayList<String> requiredFieldName = new ArrayList<String>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 4).hashCode() == form.hashCode()) {
                if (form.getRequiredField().contains((Object) tableModel.getValueAt(i, 3))) {
                    requiredFieldName.add(((SGTextComponent) tableModel.getValueAt(i, 3)).getDisplayName());
                }
            }
        }
        if (requiredFieldName.size() > 0) {
            String message = "<html><b>";
            message += ((requiredFieldName.size() > 1) ? "Os seguintes campos apresentam problemas" : "O seguinte campo apresenta problema");
            message += ":</b></html>\n";
            for (int i = 0; i < requiredFieldName.size(); i++) {
                message += "- " + requiredFieldName.get(i) + "\n";
            }
            JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    public static void check(SGWindowModel form, boolean displayMessage) {
    }

    /**
	 * Add the form hash code to the form hash code array
	 * 
	 * @param hashCode
	 */
    private static void addFormHashCode(int hashCode) {
        if (!formHashCodeList.contains(hashCode)) {
            formHashCodeList.add(hashCode);
        }
    }

    /**
	 * If a form is closed, remove its entries from the table
	 * 
	 * @param hashCode
	 */
    public static void clearClosedFormReference(int hashCode) {
        isOpenedForm = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 3).hashCode() == hashCode) {
                tableModel.removeRow(i);
            }
        }
        formHashCodeList.remove((Object) hashCode);
    }

    /**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getPanel() {
        if (panel == null) {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.gridx = 0;
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.add(getTableScrollPane(), gridBagConstraints);
        }
        return panel;
    }

    /**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getTableScrollPane() {
        if (tableScrollPane == null) {
            tableScrollPane = new JScrollPane();
            tableScrollPane.setViewportView(getTable());
        }
        return tableScrollPane;
    }

    /**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
    private JTable getTable() {
        if (table == null) {
            tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "", "Descrição", "Formulário", "fieldName", "formName" });
            table = new JTable(tableModel) {

                private static final long serialVersionUID = 1L;

                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            table.setRowHeight(16);
            table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {

                private static final long serialVersionUID = 1L;

                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    setIcon(new ImageIcon(getClass().getResource("/net/sf/swinggoodies/icons/cancel2.png")));
                    return this;
                }
            });
            table.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mousePressed(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int row = table.rowAtPoint(e.getPoint());
                        SGWindowModel form = (SGWindowModel) tableModel.getValueAt(row, 4);
                        if (!form.isSelected()) {
                            form.setSelected(true);
                        }
                        JComponent component = (JComponent) tableModel.getValueAt(row, 3);
                        component.grabFocus();
                        if (component instanceof SGTextComponent) {
                            ((SGTextComponent) component).selectAll();
                        }
                    }
                }
            });
            ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
            table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(16);
            table.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(700);
            table.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(300);
            table.getColumnModel().getColumn(3).setMaxWidth(0);
            table.getColumnModel().getColumn(3).setMinWidth(0);
            table.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
            table.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
            table.getColumnModel().getColumn(4).setMaxWidth(0);
            table.getColumnModel().getColumn(4).setMinWidth(0);
            table.getTableHeader().getColumnModel().getColumn(4).setMaxWidth(0);
            table.getTableHeader().getColumnModel().getColumn(4).setMinWidth(0);
        }
        return table;
    }
}
