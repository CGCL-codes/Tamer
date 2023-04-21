package net.mystrobe.client.connector;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import net.mystrobe.client.IDataBean;

/**
 * @author TVH Group NV
 */
public class IDaoRowList<T extends IDataBean> extends ArrayList<IDAORow<T>> implements Serializable {

    private static final long serialVersionUID = 3718893516583324453L;

    public IDaoRowList(Collection<IDAORow<T>> c) {
        super(c);
    }

    public IDaoRowList() {
        super();
    }

    public IDaoRowList(int size) {
        super(size);
    }

    public IDAORow<T> getRow(String rowId) {
        if (rowId == null) throw new IllegalArgumentException("the [rowId] argument can not be null");
        Iterator<IDAORow<T>> rowIterator = iterator();
        while (rowIterator.hasNext()) {
            IDAORow<T> row = rowIterator.next();
            if (row != null && ((row.getRowData() != null && rowId.equals(row.getRowData().getRowId())) || (row.getBeforeImage() != null && rowId.equals(row.getBeforeImage().getRowId())))) {
                return row;
            }
        }
        return null;
    }

    /**
     * Find position of rowId in current data buffer.<br/>
     * 
     * @param rowId Row id to look for.
     * @return Row position or -1 if not found.
     */
    public int getRowPosition(String rowId) {
        if (rowId == null) throw new IllegalArgumentException("the [rowId] argument can not be null");
        int index = 0;
        Iterator<IDAORow<T>> rowIterator = iterator();
        while (rowIterator.hasNext()) {
            IDAORow<T> row = rowIterator.next();
            if (row != null && ((row.getRowData() != null && rowId.equals(row.getRowData().getRowId())) || (row.getBeforeImage() != null && rowId.equals(row.getBeforeImage().getRowId())))) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Build list of all {@link IDataBean}'s in current list. 
     * 
     * @return Unmodifiable data beans list.
     */
    public List<T> getDataList() {
        List<T> result = new ArrayList<T>(this.size());
        Iterator<IDAORow<T>> rowIterator = iterator();
        while (rowIterator.hasNext()) {
            IDAORow<T> row = rowIterator.next();
            result.add(row.getRowData());
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Create a copy of current data by cloning all objects in the list.
     * 
     * @return Copy of current data.
     */
    public List<IDataBean> getDataListCopy() {
        List<IDataBean> result = new ArrayList<IDataBean>(this.size());
        Iterator<IDAORow<T>> rowIterator = iterator();
        while (rowIterator.hasNext()) {
            IDAORow<T> row = rowIterator.next();
            result.add(row.getRowData());
        }
        return Collections.unmodifiableList(result);
    }
}
