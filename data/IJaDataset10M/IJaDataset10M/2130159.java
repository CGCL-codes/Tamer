package org.vramework.commons.io.rowset;

import org.vramework.commons.io.rowset.events.ISimpleRowSetListener;
import org.vramework.commons.io.rowset.exceptions.SimpleRowSetException;

/**
 * Very simple interface for iterating through a read only row set. <br />
 * Aimed to be used with text files as well as relational DBs. <br />
 * Offers more flexibility in processing the input than JDK 1.4
 * {@link javax.sql.RowSet}.<br />
 * The data in the {@link ISimpleRowSet} can be accessed via its current row. The
 * current row ist set by calling {@link #next()}.<br />
 * The current row is undefined before the first call and after the last valid
 * call to {@link #next()}.<br />
 * The current row can be retrieved via {@link #getCurrentRow()}which must
 * return a reference to the actual current row buffer, not a copy! <br />
 * The current row can be modified by the caller. <br />
 * Implementors must ensure that modifications of the current row <strong>do not
 * </strong> modify the underlying datasource (e.g. JDBC ResultSet oder
 * textfile). <br />
 * Caution: The currentRow will be changed with each call to {@link #next()}.
 * 
 * @author tmahring
 */
public interface ISimpleRowSet {

    /**
   * Moves the current row to the next row. <br />
   * Must inform all listeners of the moval. <br />
   * If the first row is the header row, next() must put the second row into the
   * current row.
   * 
   * @return true: if the the current row is valid; false: if there are no more
   *         rows.
   * @throws SimpleRowSetException
   */
    boolean next() throws SimpleRowSetException;

    /**
   * Follwing contract applies to the current row:
   * <li>It ist null before the first call to {@link #next()}
   * <li>It ist null after the last call to {@link #next()}
   * <li>Each iteration of {@link #next()}creates a new current row and sets
   * its values
   * <li>=> Callers of {@link #getCurrentRow()} can savely store a reference to
   * the retrieved row and be sure that it will not be changed.
   * 
   * @return The current row. It can be modified by the caller. Modifying the
   *         current row does <strong>not </strong> modify the underlying
   *         datasource (e.g. JDBC ResultSet oder textfile).
   * @throws SimpleRowSetException
   *           if there is no current row, i.e. before the first or after the
   *           last {@link #next()}
   */
    Row getCurrentRow() throws SimpleRowSetException;

    /**
   * Returns the value of the column with the given colum index.
   * 
   * @param columnIndex
   * @return the value of the column
   * @throws SimpleRowSetException
   */
    Object getObject(int columnIndex) throws SimpleRowSetException;

    /**
   * Returns the value of the column with the given colum name.
   * 
   * @param columnName
   * @return the value of the column
   * @throws SimpleRowSetException
   */
    Object getObject(String columnName) throws SimpleRowSetException;

    /**
   * @return the row number of the current row: -1 before the first call to
   *         {@link #next()}and numberOfRows after the last valid call to
   *         {@link #next()}.
   */
    int getCurrentRowIndex();

    /**
   * @return The number of handled rows so far. IOW: getCurrentRowIndex() -
   *         skippedRows. It is 0 before the first {@link #next()}.
   */
    public int getHandledRows();

    /**
   * @return The layout of the rowset.
   */
    RowLayout getRowLayout();

    /**
   * Defines the layout of the rowset.
   * 
   * @param layout
   */
    void setRowLayout(RowLayout layout);

    /**
   * Registers the listener. Listeners are informed in the sequence they are
   * registered. Each listener can modify the current row of the rowset. Each
   * following listener will see the modified row.
   * 
   * @param listener
   */
    void registerListener(ISimpleRowSetListener listener);

    /**
   * The number of leading rows to skip.
   * 
   * @param nrRowsToSkip
   */
    public void setSkipLeadingRows(int nrRowsToSkip);

    /**
   * Skips the defined number of rows.
   * 
   * @param nr
   */
    public void skipRows(int nr);

    /**
   * Closes all resources.
   */
    public void close();
}
