package com.google.gwt.user.cellview.client;

/**
 * A description of how rows are to be styled in a {@link CellTable}.
 *
 * @param <T> the data type of each row
 */
public interface RowStyles<T> {

    /**
   * Get extra style names that should be applied to a row.
   *
   * @param row the data stored in the row.
   * @param rowIndex the zero-based index of the row.
   *
   * @return the extra styles of the given row in a space-separated list, or
   * {@code null} if there are no extra styles for this row.
   */
    String getStyleNames(T row, int rowIndex);
}
