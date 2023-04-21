package de.sonivis.tool.textmining.representation.table;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import de.sonivis.tool.core.datamodel.exceptions.CannotConnectToDatabaseException;

/**
 * This class includes a list of terms and their attributes.
 * The terms are the links of each page. The list is a glazed lists. 
 * Additionally lists for grouping and filtering are implemented.
 * 
 * @author Janette Lehmann
 * @version $Revision$, $Date$
 */
public class LinkTermAttributesList {

    /**
	 * {@link EventList} object including the term list data.
	 */
    private static final EventList<TermAttributes> LIST_EVENT = new BasicEventList<TermAttributes>();

    /**
	 * Instance of this class.
	 */
    private static final LinkTermAttributesList INSTANCE = new LinkTermAttributesList();

    /**
	 * Default Constructor.
	 */
    public LinkTermAttributesList() {
    }

    /**
	 * Returns an instance of this class.
	 * 
	 * @return {@link LinkTermAttributesList} instance
	 */
    public static synchronized LinkTermAttributesList getInstance() {
        return INSTANCE;
    }

    /**
	 * Load terms depending on the filter configuration.
	 * @throws CannotConnectToDatabaseException if persistence store is not available.
	 */
    public final void loadListFromInfoSpace() throws CannotConnectToDatabaseException {
        LIST_EVENT.clear();
        LIST_EVENT.addAll(LinkTermAttributesListLoading.getInstance().getContentLinksData());
    }

    /**
	 * Returns the actual event list.
	 * 
	 * @return {@link EventList} object
	 */
    public final EventList<TermAttributes> getEventList() {
        return LIST_EVENT;
    }
}
