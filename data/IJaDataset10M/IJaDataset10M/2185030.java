package mipt.data.store.impl;

import mipt.data.store.PersistentData;

/**
 * Simple extension of mipt.data.impl.DataList: can find PersistentData by id.
 * @author Evdokimov
 */
public class DataList extends mipt.data.impl.DataList {

    /**
 * DataList constructor comment.
 */
    public DataList() {
        super();
    }

    /**
 * DataList constructor comment.
 * @param array mipt.data.Data[]
 */
    public DataList(PersistentData[] array) {
        super(array);
    }

    /**
 * DataList constructor comment.
 * @param approximateSize int
 */
    public DataList(int approximateSize) {
        super(approximateSize);
    }

    /**
 * DataList constructor comment.
 * @param list java.util.List
 */
    public DataList(java.util.List list) {
        super(list);
    }

    /**
 * 
 * @return int
 * @param id int
 */
    public int indexOfDataWithID(int id) {
        if (id == Integer.MIN_VALUE) return -1;
        int n = size();
        for (int i = 0; i < n; i++) {
            PersistentData data = (PersistentData) getData(i);
            if (id == data.getID().toInt()) return i;
        }
        return -1;
    }

    /**
 * 
 * @return int
 * @param id mipt.data.store.DataID
 */
    public int indexOfDataWithID(mipt.data.store.DataID id) {
        int n = size();
        for (int i = 0; i < n; i++) {
            PersistentData data = (PersistentData) getData(i);
            if (id.equals(data.getID())) return i;
        }
        return -1;
    }
}
