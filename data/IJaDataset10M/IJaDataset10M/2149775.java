package org.neodatis.odb.core.query.criteria;

import java.util.HashSet;
import org.neodatis.odb.core.layers.layer2.meta.AttributeValuesMap;

public class Not extends AbstractExpression {

    private ICriterion criterion;

    public Not(ICriterion criterion) {
        this.criterion = criterion;
    }

    public boolean match(Object object) {
        return !criterion.match(object);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" not ").append(criterion);
        return buffer.toString();
    }

    public HashSet<String> getAllInvolvedFields() {
        return criterion.getAllInvolvedFields();
    }

    public AttributeValuesMap getValues() {
        return new AttributeValuesMap();
    }

    public void ready() {
    }
}
