package org.mariella.persistence.query;

import java.util.ArrayList;
import java.util.List;

public class FromClause implements Expression {

    private List<TableReference> joinedTableReferences = new ArrayList<TableReference>();

    public List<TableReference> getJoinedTableReferences() {
        return joinedTableReferences;
    }

    public TableReference getTableReference(String alias) {
        for (TableReference tr : joinedTableReferences) {
            if (tr.getAlias().equals(alias)) {
                return tr;
            }
        }
        return null;
    }

    public void printSql(StringBuilder b) {
        b.append("FROM ");
        boolean first = true;
        for (TableReference tableReference : joinedTableReferences) {
            if (first) first = false; else b.append(", ");
            tableReference.printExpression(b);
            b.append(' ');
            tableReference.printSql(b);
        }
    }
}
