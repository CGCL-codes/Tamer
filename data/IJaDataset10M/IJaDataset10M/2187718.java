package edge.querybuilder.generic.impl.query;

import edge.querybuilder.generic.*;
import java.util.Iterator;
import java.util.List;

public class UpdateQueryImpl extends AbstractCUQuery implements UpdateQuery {

    private WhereClause whereClause = QueryFactory.whereClause();

    public UpdateQueryImpl(TableSimple table) {
        super(table);
    }

    public String getQuery() {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ").append(table.getName());
        sb.append(" SET ");
        Iterator columnValuesIterator = columnValues.keySet().iterator();
        while (columnValuesIterator.hasNext()) {
            Column column = (Column) columnValuesIterator.next();
            Object value = columnValues.get(column);
            sb.append(column.getName()).append("=");
            if (value instanceof Expression) {
                sb.append(((Expression) value).getExpression());
            } else {
                sb.append(ParametrizedElement.PLACEHOLDER);
            }
            if (columnValuesIterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(" ").append(whereClause.getWhereClause());
        return sb.toString();
    }

    public List getParameters() {
        List params = super.getParameters();
        params.addAll(whereClause.getParameters());
        return params;
    }

    public void addCondition(Condition condition) {
        whereClause.addCondition(condition);
    }
}
