package tufts.vue.filter;

import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class NodeFilter extends AbstractTableModel {

    public static final int KEY_COL = 0;

    public static final int VALUE_COL = 1;

    public static final int TYPE_COL = 3;

    /** Creates a new instance of NodeFilter */
    private Vector statementVector;

    boolean editable;

    public NodeFilter(boolean editable) {
        this.editable = editable;
        statementVector = new Vector();
    }

    public NodeFilter() {
        this(false);
    }

    public synchronized void removeStatements(Key key) {
        Vector removeStatements = new Vector();
        Iterator i = statementVector.iterator();
        while (i.hasNext()) {
            Statement statement = (Statement) i.next();
            if (((String) statement.getKey().getKey()).equals(key.getKey().toString())) removeStatements.add(statement);
        }
        removeAll(removeStatements);
    }

    public synchronized void add(Statement statement) {
        statementVector.add(statement);
    }

    public synchronized void remove(Statement statement) {
        remove(statementVector.indexOf(statement));
    }

    public synchronized void remove(int i) {
        statementVector.remove(i);
        fireTableRowsDeleted(i, i);
        fireTableDataChanged();
        fireTableStructureChanged();
    }

    public synchronized void addAll(NodeFilter statements) {
        statementVector.addAll(statements.getStatementVector());
    }

    /** nned to fire tableRowsDeleted **/
    public synchronized void removeAll(NodeFilter statements) {
        removeAll(statements.getStatementVector());
    }

    public synchronized void removeAll(Vector statements) {
        Iterator i = statements.iterator();
        while (i.hasNext()) {
            Statement s = (Statement) i.next();
            statementVector.remove(s);
        }
    }

    public synchronized void removeAllElements() {
        statementVector.removeAllElements();
        fireTableRowsDeleted(0, size() - 1);
        fireTableDataChanged();
    }

    public int size() {
        return statementVector.size();
    }

    public Statement get(int i) {
        return (Statement) statementVector.get(i);
    }

    public void setStatementVector(Vector statementVector) {
        this.statementVector = statementVector;
    }

    public Vector getStatementVector() {
        return this.statementVector;
    }

    public boolean add(Object o) {
        throw new RuntimeException(this + " can't add " + o.getClass() + ": " + o);
    }

    public boolean compare(Statement statement) {
        Iterator i = statementVector.iterator();
        while (i.hasNext()) {
            Statement nodeStatement = (Statement) i.next();
            if (nodeStatement.compare(statement)) return true;
        }
        return false;
    }

    public void addStatement(Statement statement) {
        add(statement);
    }

    public boolean isEditable() {
        return editable;
    }

    public String getColumnName(int col) {
        if (col == 0) {
            return "Element Name";
        } else return "Value";
    }

    public int getRowCount() {
        return size();
    }

    public int getColumnCount() {
        return 2;
    }

    /**
     * public Class getColumnClass(int c) {
     * if(getValueAt(0,c) != null)
     * return getValueAt(0, c).getClass();
     * return Object.class;
     * }
     */
    public Object getValueAt(int row, int col) {
        Statement statement = (Statement) get(row);
        if (col == KEY_COL) return statement.getKey().toString(); else return statement.getValue();
    }

    public void setValueAt(Object value, int row, int col) {
        Statement statement = (Statement) get(row);
        Key key = statement.getKey();
        if (col == VALUE_COL) {
            if (statement.getKey().getType().getDisplayName().equals(Type.INTEGER_TYPE)) {
                statement.setValue(new Integer(value.toString()));
            } else {
                statement.setValue(value);
            }
        } else if (col == KEY_COL) {
            statement.setKey((Key) value);
            setValueAt(((Key) value).getDefaultValue(), row, VALUE_COL);
        }
        fireTableCellUpdated(row, col);
        fireTableDataChanged();
    }

    public boolean isCellEditable(int row, int col) {
        if (col == this.VALUE_COL) {
            return true;
        } else return false;
    }
}
