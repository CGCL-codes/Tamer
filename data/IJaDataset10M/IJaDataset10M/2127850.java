package org.crappydbms.queries.predicates.simple;

import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.queries.predicates.simple.comparison.ComparisonOperator;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 1, 2008
 * 
 */
public class FieldAttributePredicate extends SimplePredicate {

    protected String attributeName;

    protected Field field;

    public FieldAttributePredicate(Field field, String attributeName, ComparisonOperator comparisonOperator) {
        super(comparisonOperator);
        this.field = field;
        this.attributeName = attributeName;
    }

    @Override
    public Field getFirstValueWith(Tuple tuple) {
        return this.field;
    }

    @Override
    public Field getSecondValueWith(Tuple tuple) throws InvalidAttributeNameException {
        return tuple.getFieldNamed(this.attributeName);
    }

    public String toString() {
        return this.field + " " + this.getComparisonOperator().toString() + " attributeName: " + this.attributeName;
    }
}
