package org.happy.collections.tables;

import com.google.common.collect.Table.Cell;

/**
 * simple implementation for cell from guava collections
 * 
 * @param <R> the type of the table row keys
 * @param <C> the type of the table column keys
 * @param <V> the type of the mapped values
 * @author Andreas Hollmann
 *
 */
public class CellImpl_1x2<R, C, V> implements Cell<R, C, V> {

    /**
	 * creates an cell from given cell
	 * @param <R>
	 * @param <C>
	 * @param <V>
	 * @param cell
	 * @return
	 */
    public static <R, C, V> CellImpl_1x2<R, C, V> of(Cell<R, C, V> cell) {
        return new CellImpl_1x2<R, C, V>(cell);
    }

    /**
	 * creates new cell
	 * @param <R>
	 * @param <C>
	 * @param <V>
	 * @param r
	 * @param c
	 * @param v
	 * @return
	 */
    public static <R, C, V> CellImpl_1x2<R, C, V> of(R r, C c, V v) {
        return new CellImpl_1x2<R, C, V>(r, c, v);
    }

    private R r;

    private C c;

    private V v;

    /**
	 * creates an cell from given cell
	 * @param cell
	 */
    public CellImpl_1x2(Cell<R, C, V> cell) {
        this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
    }

    /**
	 * constructor
	 * @param r
	 * @param c
	 * @param v
	 */
    public CellImpl_1x2(R r, C c, V v) {
        super();
        this.r = r;
        this.c = c;
        this.v = v;
    }

    @Override
    public C getColumnKey() {
        return c;
    }

    public void setColumnKey(C c) {
        this.c = c;
    }

    @Override
    public R getRowKey() {
        return r;
    }

    public void setRowKey(R r) {
        this.r = r;
    }

    @Override
    public V getValue() {
        return v;
    }

    public void setValue(V v) {
        this.v = v;
    }
}
