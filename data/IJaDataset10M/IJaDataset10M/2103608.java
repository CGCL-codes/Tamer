package org.maverickdbms.database.pgsql;

import java.lang.Comparable;
import org.maverickdbms.basic.Factory;
import org.maverickdbms.basic.SortField;
import org.maverickdbms.basic.MaverickString;

class Record implements Comparable {

    public MaverickString id;

    public MaverickString[] sortValues;

    public int[] sortOrder;

    public Record(Factory factory, int[] sortOrder) {
        id = factory.getString();
        sortValues = new MaverickString[sortOrder.length];
        for (int i = 0; i < sortOrder.length; i++) {
            sortValues[i] = factory.getString();
        }
        this.sortOrder = sortOrder;
    }

    public int compareTo(Object o) {
        Record r = (Record) o;
        for (int i = 0; i < sortOrder.length; i++) {
            int val = sortValues[i].compareTo(r.sortValues[i]);
            if (val != 0) {
                switch(sortOrder[i]) {
                    case SortField.SORT_BY:
                        return -val;
                    case SortField.SORT_BY_DSND:
                        return val;
                    case SortField.SORT_BY_EXP:
                        return -val;
                    case SortField.SORT_BY_EXP_DSND:
                        return val;
                }
            }
        }
        return 0;
    }
}
