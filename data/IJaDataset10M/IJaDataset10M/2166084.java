package com.completex.objective.components.persistency;

/**
 * @author Gennady Krizhevsky
 */
public interface ColumnFilter {

    public static final NullColumnFilter NULL_COLUMN_FILTER = new NullColumnFilter();

    boolean accept(int columnIndex, String columnName, String columnAlias);

    static class NullColumnFilter implements ColumnFilter {

        public boolean accept(int columnIndex, String columnName, String columnAlias) {
            return true;
        }
    }
}
