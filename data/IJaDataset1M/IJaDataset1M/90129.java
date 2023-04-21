package com.cell.sql;

/**
 * do not forget implement equals(Object obj)
 * @author WAZA
 * @param <K>
 */
public interface SQLTableRow<K> extends SQLFieldGroup {

    public K getPrimaryKey();

    public boolean equals(Object obj);
}
