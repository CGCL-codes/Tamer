package repast.simphony.data.logging;

/**
 * An interface for objects that will work with columned data. This data is
 * of the form (key, value).  This is used specifically by the Repast logging
 * mechanisms.<br/> 
 * An example of how this would be used is outputting a dataset where the 
 * values to output are based on the column names the StreamAware object has. 
 *
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public interface StreamContainer {

    /**
	 * Adds a column to this stream.
	 * 
	 * @param columnName
	 *            name of the column to add
	 * @return If the column was added (false if it previously was).
	 */
    boolean addColumn(String columnName);

    /**
	 * Adds a set of columns to this stream.
	 * 
	 * @param columns
	 *            columns to add
	 */
    void addColumns(Iterable<String> columns);

    /**
	 * Removes a column from this stream.
	 * 
	 * @param columnName
	 *            name of column to remove
	 * @return If the column was removed (false if it didn't contain the column)
	 */
    boolean removeColumn(String columnName);

    /**
	 * If the column was added to this stream.
	 * 
	 * @see #addColumn(String)
	 * 
	 * @param columnName
	 *            column to check for
	 * @return If the column was added to this stream.
	 */
    boolean containsColumn(String columnName);

    /**
	 * Retrieves a read-only copy of the columns contained in this stream.
	 * 
	 * @return the columns contained in this stream
	 */
    Iterable<String> getColumns();
}
