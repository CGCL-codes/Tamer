package org.opencms.workplace.list;

import java.util.Comparator;
import java.util.Locale;

/**
 * A list item comparator can be set at a column definition to set the sorting method for that column.<p>
 * 
 * @author Michael Moossen 
 * 
 * @version $Revision: 1.7 $ 
 * 
 * @since 6.0.0 
 */
public interface I_CmsListItemComparator {

    /**
     * Returns a new comparator for comparing list items by the given column,
     * and using the given locale.<p>
     * 
     * @param columnId the id of the column to sort by
     * @param locale the current used locale
     * 
     * @return a new comparator
     */
    Comparator getComparator(final String columnId, final Locale locale);
}
