package org.dbunit.dataset.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter exposes only allowed tables from the filtered dataset. This
 * implementation do not modify the original table sequence from the filtered
 * dataset and support duplicate table names.
 *
 * @author Manuel Laflamme
 * @since Mar 7, 2003
 * @version $Revision: 942 $
 */
public class IncludeTableFilter extends AbstractTableFilter implements ITableFilter {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(IncludeTableFilter.class);

    private final PatternMatcher _patternMatcher = new PatternMatcher();

    /**
     * Create a new empty IncludeTableFilter. Use {@link #includeTable} to allow
     * access to some tables.
     */
    public IncludeTableFilter() {
    }

    /**
     * Create a new IncludeTableFilter which allow access to specified tables.
     */
    public IncludeTableFilter(String[] tableNames) {
        for (int i = 0; i < tableNames.length; i++) {
            String tableName = tableNames[i];
            includeTable(tableName);
        }
    }

    /**
     * Add a new accepted table name pattern.
     * The following wildcard characters are supported:
     * '*' matches zero or more characters,
     * '?' matches one character.
     */
    public void includeTable(String patternName) {
        logger.debug("includeTable(patternName={} - start", patternName);
        _patternMatcher.addPattern(patternName);
    }

    public boolean isEmpty() {
        logger.debug("isEmpty() - start");
        return _patternMatcher.isEmpty();
    }

    public boolean isValidName(String tableName) {
        logger.debug("isValidName(tableName={}) - start", tableName);
        return _patternMatcher.accept(tableName);
    }
}
