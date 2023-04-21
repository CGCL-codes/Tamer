package org.plazmaforge.studio.dbmanager.views;

import java.util.Comparator;
import org.eclipse.swt.SWT;
import org.plazmaforge.framework.datawarehouse.IDataSet;
import org.plazmaforge.framework.datawarehouse.IDataSetRow;

public class DataSetComparator implements Comparator {

    protected int[] priorities;

    protected int[] directions;

    public DataSetComparator(IDataSet dataSet) {
        int length = dataSet.getColumnCount();
        priorities = new int[length];
        directions = new int[length];
        for (int i = 0; i < length; i++) {
            directions[i] = SWT.NONE;
            priorities[i] = i;
        }
    }

    /**
     * @param column
     */
    public void setTopPriority(int priority, int direction) {
        if (priority < 0 || priority >= priorities.length) return;
        int index = -1;
        for (int i = 0; i < priorities.length; i++) if (priorities[i] == priority) {
            index = i;
            break;
        }
        if (index == -1) return;
        for (int i = index; i > 0; i--) priorities[i] = priorities[i - 1];
        priorities[0] = priority;
        directions[priority] = direction;
    }

    public int compare(Object e1, Object e2) {
        return compareColumnValue((IDataSetRow) e1, (IDataSetRow) e2, 0);
    }

    private int compareColumnValue(IDataSetRow m1, IDataSetRow m2, int depth) {
        if (depth >= priorities.length) return 0;
        int columnNumber = priorities[depth];
        int direction = directions[columnNumber];
        int result = 0;
        Object o1 = m1.getValue(columnNumber);
        Object o2 = m2.getValue(columnNumber);
        if (o1 == null || o2 == null) {
            if (o1 == null && o2 != null) {
                result = 1;
            } else if (o1 != null && o2 == null) {
                result = -1;
            } else {
                result = 0;
            }
            if (result == 0) {
                return compareColumnValue(m1, m2, depth + 1);
            }
            if (direction == SWT.DOWN) {
                return result * -1;
            }
            return result;
        }
        if (o1 instanceof Comparable && o2 instanceof Comparable) result = ((Comparable) o1).compareTo((Comparable) o2);
        if (result == 0) return compareColumnValue(m1, m2, depth + 1);
        if (direction == SWT.DOWN) {
            return result * -1;
        }
        return result;
    }
}
