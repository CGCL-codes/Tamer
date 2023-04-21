package org.cumt.workbench;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.cumt.Messages;
import org.cumt.actions.AddAction;
import org.cumt.actions.DeleteAction;
import org.cumt.misc.Editor;
import org.cumt.misc.ValidationError;
import org.cumt.model.Model;
import org.cumt.view.utils.ActionButtonBar;
import ar.com.da.swing.actions.ActionFactory;
import ar.com.da.swing.actions.ActionMethod;

@SuppressWarnings("serial")
public class VariableMapEditor extends JPanel implements Editor<Map<String, String>> {

    private JTable variablesTable = new JTable();

    private Action add = ActionFactory.create(this, "add");

    private Action delete = ActionFactory.create(this, "delete");

    public VariableMapEditor() {
        setLayout(new BorderLayout());
        add(new JScrollPane(variablesTable), BorderLayout.CENTER);
        variablesTable.setModel(new VariableMapModel());
        ActionButtonBar buttonsPanel = new ActionButtonBar(add, delete);
        checkActions();
        variablesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                checkActions();
            }
        });
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void checkActions() {
        delete.setEnabled(variablesTable.getSelectedRow() != -1);
    }

    @ActionMethod(invokerClass = AddAction.class)
    public void add() {
        getModel().addVariable();
    }

    @ActionMethod(invokerClass = DeleteAction.class)
    public void delete() {
        getModel().deleteVariable(variablesTable.getSelectedRow());
    }

    private VariableMapModel getModel() {
        return (VariableMapModel) variablesTable.getModel();
    }

    public void setModel(Model model) {
    }

    public void set(Map<String, String> collector) {
        variablesTable.setModel(new VariableMapModel(collector));
    }

    public List<ValidationError> validateEdition() {
        return new ArrayList<ValidationError>();
    }

    public void get(Map<String, String> distribute) {
        getModel().applyTo(distribute);
    }

    public JComponent getEditorComponent() {
        return this;
    }

    @SuppressWarnings("unused")
    private class VariableMapModel extends AbstractTableModel {

        private List<Variable> variables = new ArrayList<Variable>();

        public VariableMapModel() {
        }

        public VariableMapModel(Map<String, String> map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Variable variable = new Variable();
                variable.name = entry.getKey();
                variable.value = entry.getValue();
                variables.add(variable);
            }
        }

        public int getRowCount() {
            return variables.size();
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            return Messages.get(column == 0 ? "variableMapEditor.table.variableNameTitle" : "variableMapEditor.table.variableValueTitle");
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                variables.get(rowIndex).name = (String) aValue;
            } else {
                variables.get(rowIndex).value = (String) aValue;
            }
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return columnIndex == 0 ? variables.get(rowIndex).name : variables.get(rowIndex).value;
        }

        public void addVariable() {
            int index = variables.size();
            variables.add(new Variable());
            fireTableRowsInserted(index, index);
        }

        public void deleteVariable(int index) {
            variables.remove(index);
            fireTableRowsDeleted(index, index);
        }

        private Set<String> getVariableNames() {
            Set<String> names = new HashSet<String>();
            for (Variable v : variables) {
                if (v.isValid()) {
                    names.add(v.name);
                }
            }
            return names;
        }

        public void applyTo(Map<String, String> map) {
            map.clear();
            for (Variable v : variables) {
                if (v.isValid()) {
                    map.put(v.name, v.value);
                }
            }
        }
    }

    private class Variable {

        private String name = "";

        private String value = "";

        public boolean isValid() {
            return value != null && !"".equals(value) && name != null && !"".equals(name);
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        variablesTable.setEnabled(enabled);
        add.setEnabled(enabled);
        delete.setEnabled(enabled);
    }
}
