package net.sf.echobinding.table;

import java.util.List;
import net.sf.echobinding.binding.BindingContext;

/**
 *
 */
public interface DetailsHandler {

    /**
	 * Handles the details for a bound collection. Enables a
	 * master-detail-relationship between tables. The BoundTableCollectionCellRenderer
	 * renders each bound collection (to-many associations) as a clickable
	 * button that the user can click to see the details of this collection.
	 * 
	 * The onToManyContext is the binding context you may have provided for the
	 * property adapter which points to the collection. Use this binding context
	 * to show the collections details in another (Detail) table.
	 * 
	 * 
	 * @param adapterId
	 *            The adpater id of the selected column
	 * @param list
	 *            The elements of the collection as a list
	 * @param oneToManyContext
	 *            The binding context for the collection. Can be null, if not
	 *            specified in the cofiguration.
	 */
    void handleDetails(String adapterId, List list, BindingContext oneToManyContext);
}
