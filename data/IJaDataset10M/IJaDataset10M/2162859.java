package goodservices.database.query;

import goodservices.database.query.filters.DBFilter;
import goodservices.database.query.filters.DbFilterOperator;
import goodservices.database.query.sorts.DBSort;
import goodservices.exceptions.LogicException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DBQuery {

    private Set<DBFilter> filters = new HashSet<DBFilter>();

    private Set<DBSort> sorts = new HashSet<DBSort>();

    public Set<DBSort> getSorts() {
        return sorts;
    }

    public DBQuery addSort(DBSort sort) {
        sorts.add(sort);
        return this;
    }

    public DBQuery addSorts(Set<DBSort> sorts) {
        this.sorts.addAll(sorts);
        return this;
    }

    public Set<DBFilter> getFilters() {
        return filters;
    }

    public DBQuery addFilter(String fieldName, DbFilterOperator operator, Object fieldValue) throws LogicException {
        filters.add(new DBFilter(fieldName, operator, fieldValue));
        return this;
    }

    public DBQuery addFilters(Set<DBFilter> filters) {
        this.filters.addAll(filters);
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
