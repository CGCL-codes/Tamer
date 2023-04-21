package pt.igeo.snig.mig.editor.ui.recordEditor.xmlView;

import javax.swing.tree.TreeModel;

/**
 * TreeTableModel is the model used by a JTreeTable. It extends TreeModel to add methods for getting information about
 * the set of columns each node in the TreeTableModel may have. Each column, like a column in a TableModel, has a name
 * and a type associated with it. Each node in the TreeTableModel can return a value for each of the columns and set
 * that value if isCellEditable() returns true.
 * 
 * @author Philip Milne
 * @author Scott Violet
 */
public interface TreeTableModel extends TreeModel {

    /**
	 * Returns the number of available columns.
	 * 
	 * @return number of available columns
	 */
    public int getColumnCount();

    /**
	 * Returns the name for column number <code>column</code>.
	 * 
	 * @param column number
	 * @return column name
	 */
    public String getColumnName(int column);

    /**
	 * Returns the type for column number <code>column</code>.
	 * 
	 * @param column number
	 * @return column type
	 */
    @SuppressWarnings("unchecked")
    public Class getColumnClass(int column);

    /**
	 * Returns the value to be displayed for node <code>node</code>, at column number <code>column</code>.
	 * 
	 * @param node node
	 * @param column column number
	 * @return value
	 */
    public Object getValueAt(Object node, int column);

    /**
	 * Indicates whether the the value for node <code>node</code>, at column number <code>column</code> is
	 * editable.
	 * 
	 * @param node node
	 * @param column number
	 * @return true - editable; false - not editable
	 */
    public boolean isCellEditable(Object node, int column);

    /**
	 * Sets the value for node <code>node</code>, at column number <code>column</code>.
	 * 
	 * @param aValue value to set
	 * @param node node to be setted
	 * @param column column number
	 */
    public void setValueAt(Object aValue, Object node, int column);
}
