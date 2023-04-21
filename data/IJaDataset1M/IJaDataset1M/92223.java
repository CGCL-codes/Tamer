package org.dbwiki.data.query.xaql;

import org.dbwiki.data.query.condition.AttributeConditionListing;
import org.dbwiki.data.query.condition.Condition;
import org.dbwiki.data.query.handler.QueryNodeSet;
import org.dbwiki.data.time.TimeSequence;

public class WhereCondition implements WhereExpression {

    private Condition _condition;

    private String _variableName;

    public WhereCondition(String variableName, Condition condition) {
        _variableName = variableName;
        _condition = condition;
    }

    public boolean eval(QueryNodeSet nodeSet) {
        return _condition.eval(nodeSet.get(_variableName));
    }

    public TimeSequence evalTimestamp(QueryNodeSet nodeSet) {
        return _condition.evalTimestamp(nodeSet.get(_variableName));
    }

    public void listConditions(AttributeConditionListing listing) {
        _condition.listConditions(listing);
    }
}
