package org.tonguetied.datatransfer.exporting;

import java.util.List;
import java.util.Map;
import org.tonguetied.keywordmanagement.Translation;

/**
 * Data post processor that performs transformations / formatting of data after
 * it has been retrieved from persistence.
 * 
 * @author bsion
 * 
 */
public interface ExportDataPostProcessor {

    /**
     * Using an existing list of {@link Translation}s, transform this list into
     * another list containing a different view of the same data in
     * <code>translations</code>.
     * 
     * @param translations the list of {@link Translation}s to transform
     * @return List of objects that may or may not be of type
     *         {@link Translation} that is a different representation of the
     *         <code>translations</code>
     */
    List<?> transformData(List<Translation> translations);

    /**
     * If any data needs to be added specifically passed to the templating 
     * engine for this export type, then add it here.
     * 
     * @param root the data object
     */
    void addItems(Map<String, Object> root);
}
