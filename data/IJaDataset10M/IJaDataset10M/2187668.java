package org.xtophe.jam.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.xtophe.jam.controller.JamController;
import org.xtophe.jam.model.ContentEvent;
import org.xtophe.jam.model.ContentListener;
import org.xtophe.jam.model.Person;

public class PersonTable extends JTable implements ContentListener {

    private static final long serialVersionUID = 1L;

    private PersonTableModel tableModel;

    private JamController<Person> controller;

    public PersonTable(JamController<Person> controller) {
        this.controller = controller;
        this.tableModel = new PersonTableModel(controller, getColumnNames());
        setModel(this.tableModel);
        setColumnRenderers();
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    fireSelectionChanged();
                }
            }
        });
    }

    private String[] getColumnNames() {
        ResourceBundle resource = ResourceBundle.getBundle(getClass().getName());
        String[] columnNames = { resource.getString("uid"), resource.getString("firstName"), resource.getString("lastName"), resource.getString("description"), resource.getString("active") };
        return columnNames;
    }

    private void setColumnRenderers() {
        getColumn(getColumnName(4)).setCellRenderer(getDefaultRenderer(Boolean.class));
    }

    private void fireSelectionChanged() {
        List<Person> selection = new ArrayList<Person>();
        int[] selectedRows = getSelectedRows();
        for (int index : selectedRows) {
            selection.add(controller.getModel().getContent().get(index));
        }
        controller.setSelection(selection);
    }

    @Override
    public void contentChanged(ContentEvent event) {
        tableModel.fireTableDataChanged();
    }

    private static class PersonTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        private JamController<Person> controller;

        private String[] columnNames;

        public PersonTableModel(JamController<Person> controller, String[] columnNames) {
            this.controller = controller;
            this.columnNames = columnNames;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return controller.getModel().getContent().size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object object = controller.getModel().getContent().get(rowIndex);
            Person person = (Person) object;
            switch(columnIndex) {
                case 0:
                    return person.getUid();
                case 1:
                    return person.getFirstName();
                case 2:
                    return person.getLastName();
                case 3:
                    return person.getDescription();
                case 4:
                    return person.isActive();
                default:
                    throw new IllegalArgumentException("Unknow column index " + columnIndex);
            }
        }
    }
}
