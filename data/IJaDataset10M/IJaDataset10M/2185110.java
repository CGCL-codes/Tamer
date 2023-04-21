package org.gdbms.engine.strategies;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import org.gdbms.engine.data.DataSource;
import org.gdbms.engine.data.driver.DriverException;
import org.gdbms.engine.data.metadata.Metadata;
import org.gdbms.engine.data.persistence.Memento;
import org.gdbms.engine.data.persistence.MementoException;
import org.gdbms.engine.data.persistence.OperationLayerMemento;
import org.gdbms.engine.instruction.IncompatibleTypesException;
import org.gdbms.engine.instruction.SelectAdapter;
import org.gdbms.engine.values.BooleanValue;
import org.gdbms.engine.values.NullValue;
import org.gdbms.engine.values.Value;

/**
 * DOCUMENT ME!
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class OrderedDataSource extends OperationDataSource implements DataSource {

    private OperationDataSource dataSource;

    private int[] fieldIndexes;

    private int[] orders;

    private long[] orderIndexes;

    private Value[][] columnCache;

    /**
     * DOCUMENT ME!
     *
     * @param ret
     * @param fieldNames
     * @param types
     *
     * @throws DriverException
     */
    public OrderedDataSource(OperationDataSource ret, String[] fieldNames, int[] types) throws DriverException {
        this.dataSource = ret;
        fieldIndexes = new int[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            fieldIndexes[i] = dataSource.getFieldIndexByName(fieldNames[i]);
        }
        orders = new int[types.length];
        for (int i = 0; i < types.length; i++) {
            orders[i] = (types[i] == SelectAdapter.ORDER_ASC) ? 1 : -1;
        }
    }

    /**
     * @see org.gdbms.engine.data.DataSource#start()
     */
    public void start() throws DriverException {
        dataSource.start();
    }

    /**
     * @see org.gdbms.engine.data.DataSource#stop()
     */
    public void stop() throws DriverException {
        dataSource.stop();
    }

    /**
     * @see org.gdbms.engine.data.DataSource#getMemento()
     */
    public Memento getMemento() throws MementoException {
        return new OperationLayerMemento(getName(), new Memento[] { dataSource.getMemento() }, getSQL());
    }

    /**
     * @see org.gdbms.engine.data.DataSource#getFieldIndexByName(java.lang.String)
     */
    public int getFieldIndexByName(String fieldName) throws DriverException {
        return dataSource.getFieldIndexByName(fieldName);
    }

    /**
     * @see org.gdbms.engine.data.driver.ReadAccess#getFieldValue(long,
     *      int)
     */
    public Value getFieldValue(long rowIndex, int fieldId) throws DriverException {
        return dataSource.getFieldValue(orderIndexes[(int) rowIndex], fieldId);
    }

    /**
     * @see org.gdbms.engine.data.driver.ReadAccess#getRowCount()
     */
    public long getRowCount() throws DriverException {
        return dataSource.getRowCount();
    }

    /**
     * @throws DriverException
     *
     */
    public void order() throws DriverException {
        int rowCount = (int) dataSource.getRowCount();
        columnCache = new Value[rowCount][fieldIndexes.length];
        for (int field = 0; field < fieldIndexes.length; field++) {
            for (int i = 0; i < rowCount; i++) {
                columnCache[i][field] = dataSource.getFieldValue(i, fieldIndexes[field]);
            }
        }
        TreeSet<Integer> set = new TreeSet<Integer>(new SortComparator());
        for (int i = 0; i < dataSource.getRowCount(); i++) {
            set.add(new Integer(i));
        }
        orderIndexes = new long[(int) dataSource.getRowCount()];
        int index = 0;
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Integer integer = (Integer) it.next();
            orderIndexes[index] = integer.intValue();
            index++;
        }
    }

    public long[] getWhereFilter() throws IOException {
        return orderIndexes;
    }

    /**
     * DOCUMENT ME!
     *
     * @author Fernando Gonz�lez Cort�s
     */
    public class SortComparator implements Comparator<Integer> {

        /**
         * @see java.util.Comparator#compare(java.lang.Object,
         *      java.lang.Object)
         */
        public int compare(Integer o1, Integer o2) {
            try {
                int i1 = ((Integer) o1).intValue();
                int i2 = ((Integer) o2).intValue();
                for (int i = 0; i < orders.length; i++) {
                    Value v1 = columnCache[i1][i];
                    Value v2 = columnCache[i2][i];
                    if (v1 instanceof NullValue) return -1 * orders[i];
                    if (v2 instanceof NullValue) return 1 * orders[i];
                    if (((BooleanValue) v1.less(v2)).getValue()) {
                        return -1 * orders[i];
                    } else if (((BooleanValue) v2.less(v1)).getValue()) {
                        return 1 * orders[i];
                    }
                }
                return -1;
            } catch (IncompatibleTypesException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Metadata getDataSourceMetadata() throws DriverException {
        return dataSource.getDataSourceMetadata();
    }

    public boolean isOpen() {
        return dataSource.isOpen();
    }
}
