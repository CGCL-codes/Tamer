package jaxlib.col.table;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.List;
import jaxlib.col.XList;
import jaxlib.col.restricted.RestrictedXList;
import jaxlib.jaxlib_private.CheckArg;
import jaxlib.jaxlib_private.col.CheckMatrixArg;
import jaxlib.util.AccessTypeSet;
import jaxlib.util.CheckBounds;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: DenseTable.java 2818 2010-07-03 01:18:46Z joerg_wassmer $
 */
public class DenseTable<E> extends AbstractTable<E> implements Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 2L;

    private final XList<E> list;

    transient int columnCount;

    transient int rowCount;

    public DenseTable(XList<E> list, int columnCount, int rowCount) {
        super();
        CheckArg.notNull(list, "list");
        CheckArg.notNegative(columnCount, "columnCount");
        CheckArg.notNegative(rowCount, "rowCount");
        CheckArg.equals(list.size(), columnCount * rowCount, "list.size()", "columnCount * rowCount");
        if ((columnCount == 0) && (rowCount != 0)) throw new IllegalArgumentException("columnCount == 0 and rowCount != 0");
        this.list = list;
        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }

    public DenseTable(XList<E> list, Table<? extends E> src) {
        super();
        CheckArg.notNull(list, "list");
        CheckArg.notNull(src, "src");
        if (!list.isEmpty()) throw new IllegalArgumentException("cell list is not empty");
        list.addAll(src.cells());
        this.list = list;
        this.columnCount = src.getColumnCount();
        this.rowCount = src.getRowCount();
        if (list.size() != (this.rowCount * this.columnCount)) throw new ConcurrentModificationException();
    }

    public DenseTable(XList<E> list, E[][] src, boolean elementIsRow) {
        this(list, 0, 0);
        if (!list.isEmpty()) throw new IllegalArgumentException("cell list is not empty");
        if (elementIsRow) {
            int rowCount = src.length;
            if (rowCount > 0) {
                int colCount = src[0].length;
                list.ensureCapacity(rowCount * colCount);
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    E[] row = src[rowIndex];
                    if (colCount != row.length) {
                        throw new IllegalArgumentException("row at index " + rowIndex + " is of different length (" + row.length + ") than the first row (" + colCount + ")");
                    }
                    list.addAll(row, 0, colCount);
                }
                this.columnCount = colCount;
                this.rowCount = rowCount;
            }
        } else {
            int colCount = src.length;
            int rowCount = (src.length == 0) ? 0 : src[0].length;
            for (int colIndex = 1; colIndex < colCount; colIndex++) {
                if (rowCount != src[colIndex].length) {
                    throw new IllegalArgumentException("column at index " + colIndex + " is of different length (" + src[colIndex].length + ") than the first column (" + rowCount + ")");
                }
            }
            list.ensureCapacity(rowCount * colCount);
            if (colCount == 1) {
                this.list.addAll(src[0], 0, rowCount);
            } else {
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    for (int colIndex = 0; colIndex < colCount; colIndex++) {
                        list.add(src[colIndex][rowIndex]);
                    }
                }
            }
            this.columnCount = colCount;
            this.rowCount = rowCount;
        }
    }

    public DenseTable(XList<E> list, List<? extends List<? extends E>> src, boolean elementIsRow) {
        this(list, 0, 0);
        if (!list.isEmpty()) throw new IllegalArgumentException("cell list is not empty");
        if (elementIsRow) {
            int rowCount = src.size();
            if (rowCount > 0) {
                int colCount = src.get(0).size();
                list.ensureCapacity(rowCount * colCount);
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    List<? extends E> row = src.get(rowIndex);
                    if (colCount != row.size()) {
                        throw new IllegalArgumentException("row at index " + rowIndex + " is of different length (" + row.size() + ") than the first row (" + colCount + ")");
                    }
                    list.addAll(row, 0, colCount);
                }
                this.columnCount = colCount;
                this.rowCount = rowCount;
            }
        } else {
            int colCount = src.size();
            int rowCount = (colCount == 0) ? 0 : src.get(0).size();
            for (int colIndex = 1; colIndex < colCount; colIndex++) {
                if (rowCount != src.get(colIndex).size()) {
                    throw new IllegalArgumentException("column at index " + colIndex + " is of different length (" + src.get(colIndex).size() + ") than the first column (" + rowCount + ")");
                }
            }
            list.ensureCapacity(rowCount * colCount);
            if (colCount == 1) {
                list.addAll(src.get(0), 0, rowCount);
            } else {
                for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                    for (int colIndex = 0; colIndex < colCount; colIndex++) {
                        list.add(src.get(colIndex).get(rowIndex));
                    }
                }
            }
            this.columnCount = colCount;
            this.rowCount = rowCount;
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        this.columnCount = in.readInt();
        this.rowCount = in.readInt();
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        final int modCount = this.modCount;
        try {
            out.defaultWriteObject();
            out.writeInt(this.columnCount);
            out.writeInt(this.rowCount);
        } finally {
            if (modCount != this.modCount) throw new ConcurrentModificationException();
        }
    }

    final XList<E> list() {
        return this.list;
    }

    @Override
    public AccessTypeSet accessTypes() {
        return this.list.accessTypes();
    }

    @Override
    public void addColumns(int columnIndex, int count) {
        int columnCount = this.columnCount;
        CheckArg.rangeForAdding(columnCount, columnIndex);
        CheckArg.count(count);
        if (count != 0) {
            for (int rowIndex = 0, rowCount = this.rowCount, offs = 0; rowIndex < rowCount; rowIndex++) {
                this.list.add(offs + columnIndex, null);
                offs += columnCount;
            }
            this.modCount++;
            this.columnCount = columnCount + count;
            addColumnMeta(columnIndex, count);
        }
    }

    @Override
    public void addRows(int rowIndex, int count) {
        int rowCount = this.rowCount;
        CheckArg.rangeForAdding(rowCount, rowIndex);
        if (count != 0) {
            CheckArg.count(count);
            int columnCount = this.columnCount;
            if (columnCount <= 0) throw new IllegalStateException("no columns");
            this.list.addCount(rowIndex * columnCount, null, count * columnCount);
            this.modCount++;
            this.rowCount = rowCount + count;
        }
    }

    @Override
    public int capacity() {
        return this.list.capacity();
    }

    @Override
    public XList<E> cells() {
        if (this.cellList == null) this.cellList = RestrictedXList.fixedSize(this.list);
        return this.cellList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true; else if (!(o instanceof Table)) return false; else {
            Table b = (Table) o;
            int colCount = getColumnCount();
            if (colCount != b.getColumnCount()) {
                return false;
            } else {
                int rowCount = getRowCount();
                if (rowCount != b.getRowCount()) {
                    return false;
                } else {
                    return this.list.equals(b.cells());
                }
            }
        }
    }

    @Override
    public int freeCapacity() {
        return this.list.freeCapacity();
    }

    @Override
    public E get(int rowIndex, int colIndex) {
        return this.list.get(CheckMatrixArg.indexAt(this.rowCount, this.columnCount, rowIndex, colIndex));
    }

    @Override
    public final int getColumnCount() {
        return this.columnCount;
    }

    @Override
    public final int getRowCount() {
        return this.rowCount;
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public final boolean isEmpty() {
        return (this.rowCount == 0) || (this.columnCount == 0);
    }

    @Override
    public void removeColumns(int fromColumnIndex, int toColumnIndex) {
        int columnCount = this.columnCount;
        CheckBounds.range(columnCount, fromColumnIndex, toColumnIndex);
        if (fromColumnIndex != toColumnIndex) {
            if ((fromColumnIndex == 0) && (toColumnIndex == columnCount)) {
                this.list.clear();
                this.columnClasses = null;
                this.columnNames = null;
                this.modCount++;
                this.columnCount = 0;
                this.rowCount = 0;
            } else {
                for (int rowIndex = this.rowCount; --rowIndex >= 0; ) {
                    int offs = rowIndex * columnCount;
                    this.list.clear(offs + fromColumnIndex, offs + toColumnIndex);
                }
                this.modCount++;
                this.columnCount = columnCount -= (toColumnIndex - fromColumnIndex);
                if (columnCount == 0) this.rowCount = 0;
                removeColumnMeta(fromColumnIndex, toColumnIndex);
            }
        }
    }

    @Override
    public void removeRows(int fromRowIndex, int toRowIndex) {
        int rowCount = this.rowCount;
        CheckBounds.range(rowCount, fromRowIndex, toRowIndex);
        if (fromRowIndex != toRowIndex) {
            int fromIndex = fromRowIndex * this.columnCount;
            int toIndex = toRowIndex * this.columnCount;
            this.list.clear(fromIndex, toIndex);
            this.modCount++;
            this.rowCount = rowCount - (toRowIndex - fromRowIndex);
        }
    }

    @Override
    public void set(int rowIndex, int colIndex, E e) {
        this.list.at(CheckMatrixArg.indexAt(this.rowCount, this.columnCount, rowIndex, colIndex), e);
    }

    @Override
    public void set(int rowIndex1, int columnIndex1, int rowIndex2, int columnIndex2, E e) {
        final int rowCount = this.rowCount;
        final int colCount = this.columnCount;
        CheckMatrixArg.pointFromTo(rowCount, colCount, rowIndex1, columnIndex1, rowIndex2, columnIndex2);
        final int colDiff = (columnIndex2 - columnIndex1) + 1;
        if (colDiff == colCount) {
            this.list.set(rowIndex1 * colCount, (rowIndex2 * colCount) + colCount, e);
        } else if ((rowIndex1 != rowIndex2) || (columnIndex1 != columnIndex2)) {
            final XList<E> list = this.list;
            if (colDiff == 1) {
                int i = (rowIndex1 * colCount) + columnIndex1;
                for (int r = rowIndex1; r <= rowIndex2; r++) {
                    list.at(i, e);
                    i += colCount;
                }
            } else {
                for (int r = rowIndex1; r <= rowIndex2; r++) {
                    int fromIndex = (r * colCount) + columnIndex1;
                    list.set(fromIndex, fromIndex + colDiff, e);
                }
            }
        }
    }

    @Override
    public final int size() {
        return this.rowCount * this.columnCount;
    }

    @Override
    public void swapCells(final int rowIndex1, final int columnIndex1, final int rowIndex2, final int columnIndex2) {
        this.list.swap(CheckMatrixArg.indexAt(this.rowCount, this.columnCount, rowIndex1, columnIndex1), CheckMatrixArg.indexAt(this.rowCount, this.columnCount, rowIndex2, columnIndex2));
    }

    @Override
    public void swapColumns(final int columnIndex1, final int columnIndex2) {
        final int columnCount = this.columnCount;
        CheckMatrixArg.colIndex(columnCount, columnIndex1);
        if (columnIndex1 != columnIndex2) {
            CheckMatrixArg.colIndex(columnCount, columnIndex2);
            final XList<E> list = this.list;
            int index1 = columnIndex1;
            int index2 = columnIndex2;
            for (int rowIndex = 0, rowCount = this.rowCount; rowIndex < rowCount; rowIndex++) {
                list.swap(index1, index2);
                index1 += columnCount;
                index2 += columnCount;
            }
            swapColumnMeta(columnIndex1, columnIndex2);
        }
    }

    @Override
    public void swapRows(final int rowIndex1, final int rowIndex2) {
        final int rowCount = this.rowCount;
        CheckMatrixArg.rowIndex(rowCount, rowIndex1);
        if (rowIndex1 != rowIndex2) {
            CheckMatrixArg.rowIndex(rowCount, rowIndex2);
            final int columnCount = this.columnCount;
            this.list.swapRanges(rowIndex1 * columnCount, rowIndex2 * columnCount, columnCount);
        }
    }

    @Override
    public Object[] toArray() {
        return this.list.toArray();
    }

    @Override
    public void toArray(final int rowIndex1, final int columnIndex1, final int rowIndex2, final int columnIndex2, final Object[] dest, int destIndex) {
        final int rowCount = this.rowCount;
        final int colCount = this.columnCount;
        CheckMatrixArg.pointFromTo(rowCount, colCount, rowIndex1, columnIndex1, rowIndex2, columnIndex2);
        final int colDiff = (columnIndex2 - columnIndex1) + 1;
        if (colDiff == colCount) {
            this.list.toArray(rowIndex1 * colCount, (rowIndex2 * colCount) + colCount, dest, destIndex);
        } else if ((rowIndex1 != rowIndex2) || (columnIndex1 != columnIndex2)) {
            final XList<E> list = this.list;
            if (colDiff == 1) {
                int i = (rowIndex1 * colCount) + columnIndex1;
                for (int r = rowIndex1; r <= rowIndex2; r++) {
                    dest[destIndex++] = list.get(i);
                    i += colCount;
                }
            } else {
                for (int r = rowIndex1; r <= rowIndex2; r++) {
                    int fromIndex = (r * colCount) + columnIndex1;
                    list.toArray(fromIndex, fromIndex + colDiff, dest, destIndex);
                    destIndex += colDiff;
                }
            }
        }
    }

    @Override
    public int trimCapacity(final int newCapacity) {
        return this.list.trimCapacity(newCapacity);
    }
}
