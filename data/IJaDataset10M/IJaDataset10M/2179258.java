package edu.psu.citeseerx.myciteseer.web.utils;

import org.apache.commons.collections.comparators.ComparatorChain;
import edu.psu.citeseerx.myciteseer.domain.Paper;

/**
 * Utility functions to work with Papers.
 * @author Juan Pablo Fernandez Ramirez
 * @version $Rev: 876 $ $Date: 2009-01-09 12:52:33 -0500 (Fri, 09 Jan 2009) $
 */
public class PaperUtils {

    public static final String SORT_TIT = "title";

    public static final String SORT_CITE = "cite";

    public static final String SORT_DATE = "date";

    /**
	 * Creates the chain comparator to sort the results
	 * @param sort	Item to be use to sort
	 * @param oType Order direction (asc or desc)
	 * @return a paper ComparatorChain
	 */
    public static ComparatorChain getPaperComparator(String sort, String oType) {
        boolean mainOrder = false;
        if (oType == null || "asc".compareToIgnoreCase(oType) != 0) {
            mainOrder = true;
        }
        ComparatorChain comparator = new ComparatorChain();
        if (sort.equalsIgnoreCase(SORT_DATE)) {
            comparator.addComparator(Paper.dateComparator, mainOrder);
            comparator.addComparator(Paper.titleComparator);
            comparator.addComparator(Paper.citesComparator);
        } else if (sort.equalsIgnoreCase(SORT_CITE)) {
            comparator.addComparator(Paper.citesComparator, mainOrder);
            comparator.addComparator(Paper.titleComparator);
            comparator.addComparator(Paper.dateComparator);
        } else {
            comparator.addComparator(Paper.titleComparator, mainOrder);
            comparator.addComparator(Paper.citesComparator);
            comparator.addComparator(Paper.dateComparator);
        }
        return comparator;
    }
}
