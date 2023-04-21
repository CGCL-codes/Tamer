package org.base.apps.beans.view.swing;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.logging.Log;
import org.base.apps.beans.expr.Evaluator;
import org.base.apps.util.config.LogManager;

/**
 *
 * @author Kevan Simpson
 */
public class BaseTableModel<T> extends AbstractTableModel {

    private static final long serialVersionUID = 1800630934480790282L;

    /** The displayed list of items. */
    private List<T> mItems;

    /** List of column evaluative expressions. */
    private List<ColumnInfo> mColumnsInfo;

    private boolean mSortable;

    private Evaluator mEvaluator;

    private Log mLog;

    /**
     * 
     */
    public BaseTableModel() {
        this(true);
    }

    /**
     * 
     */
    public BaseTableModel(boolean sortable) {
        mLog = LogManager.getLogger(BaseTableModel.class);
        setItems(new ArrayList<T>());
        setColumnsInfo(new ArrayList<ColumnInfo>());
        setEvaluator(new Evaluator());
        setSortable(sortable);
    }

    /**
     * Returns <code>true</code> if this table's data is sortable.
     * @return <code>true</code> if this table's data is sortable, else <code>false</code>..
     */
    public boolean isSortable() {
        return mSortable;
    }

    /**
     * @param sortable the sortable to set
     */
    public void setSortable(boolean sortable) {
        mSortable = sortable;
    }

    /** @see javax.swing.table.TableModel#getRowCount() */
    @Override
    public int getRowCount() {
        return getItems().size();
    }

    /** @see javax.swing.table.TableModel#getColumnCount() */
    @Override
    public int getColumnCount() {
        return getColumnsInfo().size();
    }

    @Override
    public String getColumnName(int column) {
        ColumnInfo info = getColumnInfo(column);
        return (info != null) ? info.getName() : super.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        ColumnInfo info = getColumnInfo(column);
        return (info != null) ? info.getType() : super.getColumnClass(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /** @see javax.swing.table.TableModel#getValueAt(int, int) */
    @Override
    public Object getValueAt(int row, int col) {
        T item = itemAt(row);
        ColumnInfo info = getColumnInfo(col);
        if (item == null || info == null) {
            return null;
        } else {
            Object value = getEvaluator().evaluate(info.getExpr(), item);
            return value;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        T item = itemAt(row);
        ColumnInfo info = getColumnInfo(col);
        if (item != null && info != null) {
            getEvaluator().assignValue(info.getExpr(), value, item);
        }
    }

    /**
     * Returns this model's list of items.
     * @return this model's list of items.
     */
    public List<T> getItems() {
        return mItems;
    }

    /**
     * Returns the item at the given index or <code>null</code> if the index
     * is out of bounds.
     * 
     * @param index The given index.
     * @return An item or <code>null</code>.
     */
    public T itemAt(int index) {
        return (index < 0 || index >= getItems().size()) ? null : getItems().get(index);
    }

    /**
     * Sets this model's list of items.
     * @param items The list of items.
     */
    public void setItems(List<T> items) {
        mItems = (items == null) ? new ArrayList<T>() : items;
    }

    public void addColumnInfo(ColumnInfo info) {
        if (info != null) {
            getColumnsInfo().add(info);
        }
    }

    public ColumnInfo getColumnInfo(int index) {
        return (inRange(index)) ? getColumnsInfo().get(index) : null;
    }

    public ColumnInfo setColumnInfo(int index, ColumnInfo info) {
        if (inRange(index)) {
            if (info == null) {
                return getColumnsInfo().remove(index);
            } else {
                ColumnInfo old = getColumnInfo(index);
                getColumnsInfo().set(index, info);
                return old;
            }
        }
        return null;
    }

    /**
     * @return the columnsInfo
     */
    protected List<ColumnInfo> getColumnsInfo() {
        return mColumnsInfo;
    }

    /**
     * @param columnsInfo the columnsInfo to set
     */
    protected void setColumnsInfo(List<ColumnInfo> columnsInfo) {
        mColumnsInfo = columnsInfo;
    }

    /**
     * @return the evaluator
     */
    protected Evaluator getEvaluator() {
        return mEvaluator;
    }

    /**
     * @param evaluator the evaluator to set
     */
    protected void setEvaluator(Evaluator evaluator) {
        mEvaluator = evaluator;
    }

    protected Log log() {
        return mLog;
    }

    protected boolean inRange(int index) {
        return (index >= 0 && index < getColumnsInfo().size());
    }
}
