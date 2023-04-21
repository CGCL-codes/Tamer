package org.databene.jdbacl.model.jdbc;

import java.util.regex.Pattern;
import org.databene.commons.Filter;

/**
 * Filters.<br/><br/>
 * Created: 29.01.2012 22:08:43
 * @since 0.8.0
 * @author Volker Bergmann
 */
public class TableNameFilter implements Filter<String> {

    private Pattern tableInclusionPattern;

    private Pattern tableExclusionPattern;

    public TableNameFilter(String tableInclusionPattern, String tableExclusionPattern) {
        this.tableInclusionPattern = Pattern.compile(tableInclusionPattern != null ? tableInclusionPattern : ".*");
        this.tableExclusionPattern = (tableExclusionPattern != null ? Pattern.compile(tableExclusionPattern) : null);
    }

    public boolean accept(String tableName) {
        if (tableName.contains("$") || (tableExclusionPattern != null && tableExclusionPattern.matcher(tableName).matches())) return false;
        return (tableInclusionPattern == null || tableInclusionPattern.matcher(tableName).matches());
    }
}
