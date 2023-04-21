package magoffin.matt.tidbits.biz;

import magoffin.matt.tidbits.domain.PaginationCriteria;
import magoffin.matt.tidbits.domain.Tidbit;

/**
 * Search criteria API.
 * 
 * @author matt.magoffin
 * @version $Revision: 1.2 $ $Date: 2006/07/09 03:12:52 $
 */
public interface TidbitSearchCriteria {

    /** A Tidbit search type. */
    public static enum TidbitSearchType {

        /** Search for Tidbits matching an index key. */
        FOR_INDEX_KEY, /** Search for Tidbits matching the Tidbit template. */
        FOR_TEMPLATE, /** Search for Tidbits matching a query string. */
        FOR_QUERY
    }

    /**
	 * Get the search type.
	 * @return search type
	 */
    TidbitSearchType getSearchType();

    /**
	 * Get the pagination criteria.
	 * @return the pagination criteria
	 */
    PaginationCriteria getPaginationCriteria();

    /**
	 * Get a Tidbit instance to use as a search template.
	 * @return the Tidbit template
	 */
    Tidbit getTidbitTemplate();

    /**
	 * Get a query string to search by.
	 * @return the query string
	 */
    String getQuery();
}
