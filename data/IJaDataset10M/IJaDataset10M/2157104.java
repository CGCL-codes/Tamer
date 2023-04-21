package org.hsqldb.lib;

import java.lang.reflect.*;
import java.util.Hashtable;

/**
 * Provides a reflection-based abstraction of Java array objects, allowing
 * table-like access to and manipulation of both primitive and object array
 * types through a single interface.
 *
 * @author tony_lai@users
 * @version 1.7.2
 * @since 1.7.2
 */
public class UnifiedTable {

    private static Hashtable classCodeMap = new Hashtable(37, 1);

    static final int PRIM_CLASS_CODE_BYTE = 101;

    static final int PRIM_CLASS_CODE_CHAR = 102;

    static final int PRIM_CLASS_CODE_SHORT = 103;

    static final int PRIM_CLASS_CODE_INT = 104;

    static final int PRIM_CLASS_CODE_LONG = 105;

    static final int PRIM_CLASS_CODE_FLOAT = 106;

    static final int PRIM_CLASS_CODE_DOUBLE = 107;

    static final int OBJ_CLASS_CODE_OBJECT = (new Object()).getClass().hashCode();

    static {
        classCodeMap.put(byte.class, new Integer(PRIM_CLASS_CODE_BYTE));
        classCodeMap.put(char.class, new Integer(PRIM_CLASS_CODE_SHORT));
        classCodeMap.put(short.class, new Integer(PRIM_CLASS_CODE_SHORT));
        classCodeMap.put(int.class, new Integer(PRIM_CLASS_CODE_INT));
        classCodeMap.put(long.class, new Integer(PRIM_CLASS_CODE_LONG));
        classCodeMap.put(float.class, new Integer(PRIM_CLASS_CODE_FLOAT));
        classCodeMap.put(double.class, new Integer(PRIM_CLASS_CODE_DOUBLE));
        classCodeMap.put(Object.class, new Integer(OBJ_CLASS_CODE_OBJECT));
    }

    protected SingleCellComparator getSingleCellComparator(int targetColumn) {
        switch(cellTypeCode) {
            case PRIM_CLASS_CODE_BYTE:
                return new PrimByteCellComparator(targetColumn);
            case PRIM_CLASS_CODE_CHAR:
                return new PrimCharCellComparator(targetColumn);
            case PRIM_CLASS_CODE_SHORT:
                return new PrimShortCellComparator(targetColumn);
            case PRIM_CLASS_CODE_INT:
                return new PrimIntCellComparator(targetColumn);
            case PRIM_CLASS_CODE_LONG:
                return new PrimLongCellComparator(targetColumn);
            case PRIM_CLASS_CODE_FLOAT:
                return new PrimFloatCellComparator(targetColumn);
            case PRIM_CLASS_CODE_DOUBLE:
                return new PrimDoubleCellComparator(targetColumn);
            default:
                if (comparatorArray[targetColumn] == null) {
                    comparatorArray[targetColumn] = new PrimObjectCellComparator(targetColumn, new DefaultStringComparator());
                }
                return comparatorArray[targetColumn];
        }
    }

    private Class cellType;

    private int cellTypeCode;

    private int columns;

    private int initRows;

    private int growth;

    private Object tableData;

    private int rowAvailable = 0;

    private int rowCount = 0;

    private RowComparator rowComparator;

    private boolean ascending;

    private PrimObjectCellComparator[] comparatorArray;

    public UnifiedTable(Class cellType, int columns) {
        this(cellType, columns, 128);
    }

    public UnifiedTable(Class cellType, int columns, int initRows) {
        this(cellType, columns, initRows, 128);
    }

    public UnifiedTable(Class cellType, int columns, int initRows, int growth) {
        this.cellType = cellType;
        cellTypeCode = ((Integer) classCodeMap.get(cellType)).intValue();
        this.columns = columns;
        this.growth = growth;
        tableData = Array.newInstance(cellType, initRows * columns);
        rowAvailable = initRows;
        if (cellTypeCode == OBJ_CLASS_CODE_OBJECT) {
            comparatorArray = new PrimObjectCellComparator[columns];
        }
    }

    public void SetComparator(ObjectComparator objectCompare, int column) {
        if (cellTypeCode == OBJ_CLASS_CODE_OBJECT) {
            if (comparatorArray[column] == null) {
                comparatorArray[column] = new PrimObjectCellComparator(column, objectCompare);
            } else {
            }
        }
    }

    public void addRow(Object rowData) {
        int dataIndex = makeRoom(rowCount, 1);
        System.arraycopy(rowData, 0, tableData, dataIndex, columns);
    }

    public void removeRow(int rowIndex) {
        makeRoom(rowIndex, -1);
    }

    public void clear() {
        rowCount = 0;
    }

    public void setCount(int count) {
        rowCount = count;
    }

    public void setCell(int rowIndex, int colIndex, Object cellData) {
        Array.set(tableData, rowIndex * columns + colIndex, cellData);
    }

    public void setRow(int rowIndex, Object rowData) {
        System.arraycopy(rowData, 0, tableData, rowIndex * columns, columns);
    }

    public void moveRows(int fromIndex, int toIndex, int rows) {
        System.arraycopy(tableData, fromIndex * columns, tableData, toIndex * columns, rows * columns);
    }

    public Object getRow(int rowIndex) {
        Object row = Array.newInstance(cellType, columns);
        System.arraycopy(tableData, rowIndex * columns, row, 0, columns);
        return row;
    }

    public void sort(int targetColumn, boolean ascending) {
        rowComparator = getSingleCellComparator(targetColumn);
        rowComparator.reset();
        this.ascending = ascending;
        fastQuickSort();
    }

    public void sort(int[] targetColumns, boolean ascending) {
        rowComparator = new MultiCellsComparator(targetColumns);
        rowComparator.reset();
        this.ascending = ascending;
        fastQuickSort();
    }

    public int search(byte value) {
        if (rowComparator == null) {
            throw new IllegalArgumentException("Table is not sorted");
        }
        try {
            ((PrimByteCellComparator) rowComparator).setSearchTarget(value);
        } catch (ClassCastException ccx) {
            throw new IllegalArgumentException("Invalid search target: " + value);
        }
        return binarySearch();
    }

    public int search(char value) {
        if (rowComparator == null) {
            throw new IllegalArgumentException("Table is not sorted");
        }
        try {
            ((PrimCharCellComparator) rowComparator).setSearchTarget(value);
        } catch (ClassCastException ccx) {
            throw new IllegalArgumentException("Invalid search target: " + value);
        }
        return binarySearch();
    }

    public int search(short value) {
        if (rowComparator == null) {
            throw new IllegalArgumentException("Table is not sorted");
        }
        try {
            ((PrimShortCellComparator) rowComparator).setSearchTarget(value);
        } catch (ClassCastException ccx) {
            throw new IllegalArgumentException("Invalid search target: " + value);
        }
        return binarySearch();
    }

    public int search(int value) {
        if (rowComparator == null) {
            throw new IllegalArgumentException("Table is not sorted");
        }
        try {
            ((PrimIntCellComparator) rowComparator).setSearchTarget(value);
        } catch (ClassCastException ccx) {
            throw new IllegalArgumentException("Invalid search target: " + value);
        }
        return binarySearch();
    }

    public int search(long value) {
        if (rowComparator == null) {
            throw new IllegalArgumentException("Table is not sorted");
        }
        try {
            ((PrimLongCellComparator) rowComparator).setSearchTarget(value);
        } catch (ClassCastException ccx) {
            throw new IllegalArgumentException("Invalid search target: " + value);
        }
        return binarySearch();
    }

    public int search(float value) {
        if (rowComparator == null) {
            throw new IllegalArgumentException("Table is not sorted");
        }
        try {
            ((PrimFloatCellComparator) rowComparator).setSearchTarget(value);
        } catch (ClassCastException ccx) {
            throw new IllegalArgumentException("Invalid search target: " + value);
        }
        return binarySearch();
    }

    public int search(double value) {
        if (rowComparator == null) {
            throw new IllegalArgumentException("Table is not sorted");
        }
        try {
            ((PrimDoubleCellComparator) rowComparator).setSearchTarget(value);
        } catch (ClassCastException ccx) {
            throw new IllegalArgumentException("Invalid search target: " + value);
        }
        return binarySearch();
    }

    public int search(String value) {
        if (rowComparator == null) {
            throw new IllegalArgumentException("Table is not sorted");
        }
        try {
            rowComparator.setSearchTarget(value);
        } catch (ClassCastException ccx) {
            throw new IllegalArgumentException("Invalid search target: " + value);
        }
        return binarySearch();
    }

    /**
     * Swaps the values for row indexed i and j.
     */
    public void swap(int i, int j) {
        Object rowI = getRow(i);
        System.arraycopy(tableData, j * columns, tableData, i * columns, columns);
        System.arraycopy(rowI, 0, tableData, j * columns, columns);
    }

    public Object getCell(int rowIndex, int colIndex) {
        return Array.get(tableData, rowIndex * columns + colIndex);
    }

    public byte getByteCell(int rowIndex, int colIndex) {
        return Array.getByte(tableData, rowIndex * columns + colIndex);
    }

    public char getCharCell(int rowIndex, int colIndex) {
        return Array.getChar(tableData, rowIndex * columns + colIndex);
    }

    public short getShortCell(int rowIndex, int colIndex) {
        return Array.getShort(tableData, rowIndex * columns + colIndex);
    }

    public int getIntCell(int rowIndex, int colIndex) {
        return Array.getInt(tableData, rowIndex * columns + colIndex);
    }

    public long getLongCell(int rowIndex, int colIndex) {
        return Array.getInt(tableData, rowIndex * columns + colIndex);
    }

    public float getFloatCell(int rowIndex, int colIndex) {
        return Array.getFloat(tableData, rowIndex * columns + colIndex);
    }

    public double getDoubleCell(int rowIndex, int colIndex) {
        return Array.getDouble(tableData, rowIndex * columns + colIndex);
    }

    public int size() {
        return rowCount;
    }

    void clearArray(int from, int to) {
        switch(cellTypeCode) {
            case PRIM_CLASS_CODE_BYTE:
                {
                    byte[] array = (byte[]) tableData;
                    while (--to >= from) {
                        array[to] = 0;
                    }
                    return;
                }
            case PRIM_CLASS_CODE_CHAR:
                {
                    byte[] array = (byte[]) tableData;
                    while (--to >= from) {
                        array[to] = 0;
                    }
                    return;
                }
            case PRIM_CLASS_CODE_SHORT:
                {
                    short[] array = (short[]) tableData;
                    while (--to >= from) {
                        array[to] = 0;
                    }
                    return;
                }
            case PRIM_CLASS_CODE_INT:
                {
                    int[] array = (int[]) tableData;
                    while (--to >= from) {
                        array[to] = 0;
                    }
                    return;
                }
            case PRIM_CLASS_CODE_LONG:
                {
                    long[] array = (long[]) tableData;
                    while (--to >= from) {
                        array[to] = 0;
                    }
                    return;
                }
            case PRIM_CLASS_CODE_FLOAT:
                {
                    float[] array = (float[]) tableData;
                    while (--to >= from) {
                        array[to] = 0;
                    }
                    return;
                }
            case PRIM_CLASS_CODE_DOUBLE:
                {
                    double[] array = (double[]) tableData;
                    while (--to >= from) {
                        array[to] = 0;
                    }
                    return;
                }
            default:
                {
                    Object[] array = (Object[]) tableData;
                    while (--to >= from) {
                        array[to] = null;
                    }
                    return;
                }
        }
    }

    /**
     * Handles both addition and removal of rows
     */
    protected int makeRoom(int rowIndex, int rows) {
        int newCount = rowCount + rows;
        Object data = tableData;
        if (newCount > rowAvailable) {
            rowAvailable += growth;
            data = Array.newInstance(cellType, rowAvailable * columns);
            System.arraycopy(tableData, 0, data, 0, rowIndex * columns);
        }
        if (rowIndex < rowCount) {
            int source;
            int target;
            int size;
            if (rows >= 0) {
                source = rowIndex * columns;
                target = (rowIndex + rows) * columns;
                size = (rowCount - rowIndex) * columns;
            } else {
                source = (rowIndex - rows) * columns;
                target = rowIndex * columns;
                size = (rowCount - rowIndex + rows) * columns;
            }
            if (size > 0) {
                System.arraycopy(tableData, source, data, target, size);
            }
            if (rows < 0 && cellTypeCode == OBJ_CLASS_CODE_OBJECT) {
                clearArray(newCount * columns, rowCount * columns);
            }
        }
        tableData = data;
        rowCount = newCount;
        return rowIndex * columns;
    }

    private int binarySearch() {
        int low = 0;
        int high = rowCount;
        int mid = 0;
        while (low < high) {
            mid = (low + high) / 2;
            if (rowComparator.greaterThan(mid)) {
                high = mid;
            } else {
                if (rowComparator.lessThan(mid)) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            }
        }
        return -1;
    }

    private void fastQuickSort() {
        quickSort(0, size() - 1);
        insertionSort(0, size() - 1);
    }

    private void quickSort(int l, int r) {
        int M = 4;
        int i;
        int j;
        int v;
        if ((r - l) > M) {
            i = (r + l) / 2;
            if (lessThan(i, l)) {
                swap(l, i);
            }
            if (lessThan(r, l)) {
                swap(l, r);
            }
            if (lessThan(r, i)) {
                swap(i, r);
            }
            j = r - 1;
            swap(i, j);
            i = l;
            v = j;
            for (; ; ) {
                while (lessThan(++i, v)) ;
                while (lessThan(v, --j)) ;
                if (j < i) {
                    break;
                }
                swap(i, j);
            }
            swap(i, r - 1);
            quickSort(l, j);
            quickSort(i + 1, r);
        }
    }

    private void insertionSort(int lo0, int hi0) {
        int i;
        int j;
        for (i = lo0 + 1; i <= hi0; i++) {
            j = i;
            while ((j > lo0) && lessThan(i, j - 1)) {
                j--;
            }
            if (i != j) {
                Object row = getRow(i);
                moveRows(j, j + 1, i - j);
                setRow(j, row);
            }
        }
    }

    /**
     * Check if row indexed i is less than row indexed j
     */
    private boolean lessThan(int i, int j) {
        return ascending ? rowComparator.lessThan(i, j) : rowComparator.lessThan(j, i);
    }

    /**
     * Check if targeted column value in the row indexed i is less than the
     * search target object.
     */
    private boolean lessThan(int i) {
        return ascending ? rowComparator.lessThan(i) : rowComparator.greaterThan(i);
    }

    /**
     * Check if targeted column value in the row indexed i is greater than the
     * search target object.
     * @see setSearchTarget(Object)
     */
    private boolean greaterThan(int i) {
        return ascending ? rowComparator.greaterThan(i) : rowComparator.lessThan(i);
    }

    interface RowComparator {

        /**
         * called when the array is sorted to update the information
         */
        void reset();

        /**
         * Check if row indexed i is less than row indexed j.
         */
        abstract boolean lessThan(int i, int j);

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        abstract boolean lessThan(int i);

        /**
         * Check if targeted column value in the row indexed i is greater than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        abstract boolean greaterThan(int i);

        /**
         * Sets the target object in a search operation.
         */
        abstract void setSearchTarget(Object target);
    }

    abstract class SingleCellComparator implements RowComparator {

        protected int targetColumn;

        SingleCellComparator(int targetColumn) {
            this.targetColumn = targetColumn;
        }
    }

    class PrimByteCellComparator extends SingleCellComparator {

        private byte[] myTableData;

        private byte mySearchTarget;

        PrimByteCellComparator(int targetColumn) {
            super(targetColumn);
        }

        public void reset() {
            myTableData = (byte[]) tableData;
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            return myTableData[i * columns + targetColumn] < myTableData[j * columns + targetColumn];
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            return myTableData[i * columns + targetColumn] < mySearchTarget;
        }

        /**
         * Check if targeted column value in the row indexed i is greater
         * than the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            return myTableData[i * columns + targetColumn] > mySearchTarget;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object target) {
            mySearchTarget = ((Number) target).byteValue();
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(byte target) {
            mySearchTarget = target;
        }
    }

    class PrimCharCellComparator extends SingleCellComparator {

        private char[] myTableData;

        private char mySearchTarget;

        PrimCharCellComparator(int targetColumn) {
            super(targetColumn);
        }

        public void reset() {
            myTableData = (char[]) tableData;
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            return myTableData[i * columns + targetColumn] < myTableData[j * columns + targetColumn];
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            return myTableData[i * columns + targetColumn] < mySearchTarget;
        }

        /**
         * Check if targeted column value in the row indexed i is greater
         * than the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            return myTableData[i * columns + targetColumn] > mySearchTarget;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object target) {
            mySearchTarget = (char) (((Number) target).intValue() & Character.MAX_VALUE);
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(char target) {
            mySearchTarget = target;
        }
    }

    class PrimShortCellComparator extends SingleCellComparator {

        private short[] myTableData;

        private short mySearchTarget;

        PrimShortCellComparator(int targetColumn) {
            super(targetColumn);
        }

        public void reset() {
            myTableData = (short[]) tableData;
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            return myTableData[i * columns + targetColumn] < myTableData[j * columns + targetColumn];
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            return myTableData[i * columns + targetColumn] < mySearchTarget;
        }

        /**
         * Check if targeted column value in the row indexed i is greater than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            return myTableData[i * columns + targetColumn] > mySearchTarget;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object target) {
            mySearchTarget = ((Number) target).shortValue();
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(short target) {
            mySearchTarget = target;
        }
    }

    class PrimIntCellComparator extends SingleCellComparator {

        private int[] myTableData;

        private int mySearchTarget;

        PrimIntCellComparator(int targetColumn) {
            super(targetColumn);
        }

        public void reset() {
            myTableData = (int[]) tableData;
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            return myTableData[i * columns + targetColumn] < myTableData[j * columns + targetColumn];
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            return myTableData[i * columns + targetColumn] < mySearchTarget;
        }

        /**
         * Check if targeted column value in the row indexed i is greater than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            return myTableData[i * columns + targetColumn] > mySearchTarget;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object target) {
            mySearchTarget = ((Number) target).intValue();
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(int target) {
            mySearchTarget = target;
        }
    }

    class PrimLongCellComparator extends SingleCellComparator {

        private long[] myTableData;

        private long mySearchTarget;

        PrimLongCellComparator(int targetColumn) {
            super(targetColumn);
        }

        public void reset() {
            myTableData = (long[]) tableData;
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            return myTableData[i * columns + targetColumn] < myTableData[j * columns + targetColumn];
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            return myTableData[i * columns + targetColumn] < mySearchTarget;
        }

        /**
         * Check if targeted column value in the row indexed i is greater than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            return myTableData[i * columns + targetColumn] > mySearchTarget;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object target) {
            mySearchTarget = ((Number) target).longValue();
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(long target) {
            mySearchTarget = target;
        }
    }

    class PrimFloatCellComparator extends SingleCellComparator {

        private float[] myTableData;

        private float mySearchTarget;

        PrimFloatCellComparator(int targetColumn) {
            super(targetColumn);
        }

        public void reset() {
            myTableData = (float[]) tableData;
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            return myTableData[i * columns + targetColumn] < myTableData[j * columns + targetColumn];
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            return myTableData[i * columns + targetColumn] < mySearchTarget;
        }

        /**
         * Check if targeted column value in the row indexed i is greater than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            return myTableData[i * columns + targetColumn] > mySearchTarget;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object target) {
            mySearchTarget = ((Number) target).floatValue();
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(float target) {
            mySearchTarget = target;
        }
    }

    class PrimDoubleCellComparator extends SingleCellComparator {

        private double[] myTableData;

        private double mySearchTarget;

        PrimDoubleCellComparator(int targetColumn) {
            super(targetColumn);
        }

        public void reset() {
            myTableData = (double[]) tableData;
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            return myTableData[i * columns + targetColumn] < myTableData[j * columns + targetColumn];
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            return myTableData[i * columns + targetColumn] < mySearchTarget;
        }

        /**
         * Check if targeted column value in the row indexed i is greater than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            return myTableData[i * columns + targetColumn] > mySearchTarget;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object target) {
            mySearchTarget = ((Number) target).doubleValue();
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(double target) {
            mySearchTarget = target;
        }
    }

    class PrimObjectCellComparator extends SingleCellComparator {

        private Object[] myTableData;

        private Object mySearchTarget;

        ObjectComparator objectCompare;

        PrimObjectCellComparator(int targetColumn) {
            super(targetColumn);
            objectCompare = new DefaultStringComparator();
        }

        PrimObjectCellComparator(int targetColumn, ObjectComparator objectCompare) {
            super(targetColumn);
            myTableData = (Object[]) tableData;
            this.objectCompare = objectCompare;
        }

        void setObjectComparator(ObjectComparator objectCompare) {
            this.objectCompare = objectCompare;
        }

        public void reset() {
            myTableData = (Object[]) tableData;
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            return objectCompare.compare(myTableData[i * columns + targetColumn], myTableData[j * columns + targetColumn]) < 0;
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            return objectCompare.compare(myTableData[i * columns + targetColumn], mySearchTarget) < 0;
        }

        /**
         * Check if targeted column value in the row indexed i is greater
         * than the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            return objectCompare.compare(myTableData[i * columns + targetColumn], mySearchTarget) > 0;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object target) {
            mySearchTarget = target;
        }
    }

    class MultiCellsComparator implements RowComparator {

        private SingleCellComparator[] cellComparators;

        MultiCellsComparator(int[] targetColumns) {
            cellComparators = new SingleCellComparator[targetColumns.length];
            for (int i = 0; i < targetColumns.length; i++) {
                cellComparators[targetColumns[i]] = getSingleCellComparator(targetColumns[i]);
            }
        }

        public void reset() {
            for (int i = 0; i < cellComparators.length; i++) {
                if (cellComparators[i] != null) cellComparators[i].reset();
            }
        }

        /**
         * Check if row indexed i is less than row indexed j
         */
        public boolean lessThan(int i, int j) {
            for (int c = 0; c < cellComparators.length; c++) {
                if (cellComparators[c].lessThan(i, j)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Check if targeted column value in the row indexed i is less than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean lessThan(int i) {
            for (int c = 0; c < cellComparators.length; c++) {
                if (cellComparators[c].lessThan(i)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Check if targeted column value in the row indexed i is greater than
         * the search target object.
         * @see setSearchTarget(Object)
         */
        public boolean greaterThan(int i) {
            for (int c = 0; c < cellComparators.length; c++) {
                if (cellComparators[c].greaterThan(i)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Sets the target object in a search operation.
         */
        public void setSearchTarget(Object targets) {
            for (int c = 0; c < cellComparators.length; c++) {
                cellComparators[c].setSearchTarget(Array.get(targets, c));
            }
        }
    }

    /**
     * This interface is used for the compare method for Objects
     * stored in a column.
     */
    public class DefaultStringComparator implements ObjectComparator {

        public int compare(Object a, Object b) {
            if (a == b) {
                return 0;
            }
            if (a == null) {
                if (b == null) {
                    return 0;
                }
                return -1;
            }
            if (b == null) {
                return 1;
            }
            return a.toString().compareTo(b);
        }
    }

    static int[] outputRow(int index, int[] row) {
        return row;
    }

    public static void main(String[] args) {
        StopWatch sw = new StopWatch();
        sw.start();
        UnifiedTable table = new UnifiedTable(int.class, 2, 0x20000, 0x20000);
        for (int i = 0; i < 0x100000; i++) {
            table.addRow(outputRow(i, new int[] { (int) (Math.random() * Integer.MAX_VALUE), (int) (Math.random() * Integer.MAX_VALUE) }));
        }
        System.out.println("Create time: " + sw.elapsedTime());
        int size = table.size();
        sw.zero();
        table.sort(0, true);
        System.out.println("Sort time: " + sw.elapsedTime() + " size: " + size);
        sw.zero();
        for (int i = 1; i < size; i++) {
            if (table.getIntCell(i - 1, 0) > table.getIntCell(i, 0)) {
                System.out.println("Sort failed on Row: " + i + " column 1=" + table.getIntCell(i, 0) + " column 2=" + table.getCell(i, 1));
            }
        }
        System.out.println("Access time: " + sw.elapsedTime() + " size: " + size);
        sw.zero();
        for (int i = 1; i < size; i++) {
            int targetRow = (int) (Math.random() * (size - 1));
            int targetValue = table.getIntCell(targetRow, 0);
            int resultValue = table.getIntCell(table.search(targetValue), 0);
            if (targetValue != resultValue) {
                System.out.println("Search failed on Row: " + targetRow + " column 1=" + table.getIntCell(targetRow, 0) + " column 2=" + table.getCell(targetRow, 1));
            }
        }
        System.out.println("Search time: " + sw.elapsedTime() + " size: " + size);
        int[] longarr = new int[0x100000];
        for (int i = 0; i < longarr.length; i++) {
            longarr[i] = (int) Math.random();
        }
        sw.zero();
        java.util.Arrays.sort(longarr);
        System.out.println("Sort time: " + sw.elapsedTime() + " size: " + size);
        longarr[50000] = (int) Math.random();
        sw.zero();
        java.util.Arrays.sort(longarr);
        System.out.println("Re-sort time: " + sw.elapsedTime() + " size: " + size);
    }
}
