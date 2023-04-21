package ecom.ibeer.client.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import sun.awt.VerticalBagLayout;

public abstract class TabPanel<T_OBJ> extends JPanel {

    protected JButton ajouter;

    protected JButton supprimer;

    protected JButton editer;

    protected JTable dataTable;

    protected List<T_OBJ> dataList;

    protected IbeerAbstractTableModel model;

    /**
	 * 
	 *
	 */
    public TabPanel() {
        this.setLayout(new BorderLayout());
        initComponent();
        this.setVisible(true);
    }

    private void initComponent() {
        ajouter = new JButton("Ajouter");
        ajouter.setAction(new ajouterAction());
        supprimer = new JButton("Supprimer");
        supprimer.setAction(new supprimerAction());
        editer = new JButton("Editer");
        editer.setAction(new modifierAction());
        JPanel buttons = new JPanel();
        buttons.add(ajouter);
        buttons.add(supprimer);
        buttons.add(editer);
        this.add(buttons, BorderLayout.SOUTH);
        initDataTable();
        dataTable = new JTable(model);
        dataTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    protected abstract void initDataTable();

    public abstract class IbeerAbstractTableModel extends AbstractTableModel {

        protected String[] columnNames = new String[1];

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public int getRowCount() {
            return dataList.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public void rowsRemoved(TableModelEvent event) {
            fireTableChanged(event);
        }

        public void removeRow(int row) {
            dataList.remove(row);
            fireTableRowsDeleted(row, row);
        }

        public void newRowsAdded(TableModelEvent e) {
            fireTableChanged(e);
        }
    }

    public class supprimerAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public supprimerAction() {
            super("Supprimer");
        }

        public void actionPerformed(ActionEvent e) {
            supprimerActionPerformed(dataTable.getSelectedRow());
            AdminFrame.getInstance().updateAllPane();
        }
    }

    protected void supprimerActionPerformed(int selectedRow) {
        supprimer(dataList.get(selectedRow));
    }

    protected abstract void supprimer(T_OBJ obj);

    public class modifierAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public modifierAction() {
            super("Modifier");
        }

        public void actionPerformed(ActionEvent e) {
            modifierActionPerformed(dataTable.getSelectedRow());
            AdminFrame.getInstance().updateAllPane();
        }
    }

    protected void modifierActionPerformed(int selectedRow) {
        dialog = new ElementDialog(AdminFrame.getInstance(), dataList.get(selectedRow));
        dialog.setVisible(true);
    }

    protected abstract void modifier(T_OBJ obj);

    public class ajouterAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ajouterAction() {
            super("Ajouter");
        }

        public void actionPerformed(ActionEvent e) {
            ajouterActionPerformed();
            AdminFrame.getInstance().updateAllPane();
        }
    }

    protected void ajouterActionPerformed() {
        dialog = new ElementDialog(AdminFrame.getInstance(), null);
        dialog.setVisible(true);
    }

    protected abstract void ajouter();

    protected ElementDialog dialog;

    protected class ElementDialog extends JDialog {

        private static final long serialVersionUID = 1L;

        public ElementDialog(JFrame owner, T_OBJ selectedObj) {
            super(owner, "Category Dialog", true);
            this.setLayout(new VerticalBagLayout());
            setDialogComponents(this, selectedObj);
            JPanel buttons = new JPanel();
            JButton ok = new JButton(new okAction(selectedObj));
            buttons.add(ok);
            JButton annuler = new JButton(new annulerAction());
            buttons.add(annuler);
            this.add(buttons);
            this.setSize(300, 200);
        }
    }

    public class okAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        T_OBJ selectedObj;

        public okAction(T_OBJ selectedObj) {
            super("Ok");
            this.selectedObj = selectedObj;
        }

        public void actionPerformed(ActionEvent e) {
            if (selectedObj == null) {
                ajouter();
            } else {
                modifier(selectedObj);
            }
            dialog.dispose();
        }
    }

    public class annulerAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public annulerAction() {
            super("Annuler");
        }

        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    }

    protected abstract void setDialogComponents(ElementDialog dialog, T_OBJ selectedObj);

    public void update() {
        initDataTable();
        model.fireTableDataChanged();
        this.repaint();
    }
}
