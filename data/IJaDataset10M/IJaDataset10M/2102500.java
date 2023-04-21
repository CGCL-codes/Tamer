package nl.burgerweeshuis.ui.web.util.data;

import java.io.Serializable;
import java.util.List;

/**
 * Command for selecting multiple results based on the given queryObject using
 * the given start row and number of rows to load.
 * 
 * @author Eelco Hillenius
 * @param <E>
 */
public interface ISelectListAction<E> extends Serializable {

    /**
	 * Select and return objects based on the given query object, the start row
	 * and the number of rows to load.
	 * 
	 * @param queryObject
	 *            the query object
	 * @param startFromRow
	 *            load from row
	 * @param numberOfRows
	 *            the number of rows to load
	 * @return the result
	 */
    List<E> execute(Object queryObject, int startFromRow, int numberOfRows);
}
