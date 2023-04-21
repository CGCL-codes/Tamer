package org.crappydbms.queries.operators.rename;

import java.util.ArrayList;
import org.crappydbms.exceptions.InvalidAttributeNameException;
import org.crappydbms.relations.fields.Field;
import org.crappydbms.relations.schemas.InvalidTupleForRelationSchemaException;
import org.crappydbms.relations.tuples.AbstractTuple;
import org.crappydbms.relations.tuples.InvalidFieldPositionException;
import org.crappydbms.relations.tuples.Tuple;

/**
 * @author Facundo Manuel Quiroga
 * Dec 12, 2008
 * 
 */
public class RenamedAttributesTuple extends AbstractTuple implements Tuple {

    protected Tuple tuple;

    protected RenamedAttributesRelationSchema relationSchema;

    /**
	 * @param tuple
	 * @param relationSchema
	 */
    public RenamedAttributesTuple(Tuple tuple, RenamedAttributesRelationSchema relationSchema) {
        this.setTuple(tuple);
        this.setRelationSchema(relationSchema);
    }

    @Override
    public Field getFieldAtPosition(int n) throws InvalidFieldPositionException {
        return this.getFields().get(n);
    }

    @Override
    public Field getFieldNamed(String attributeName) throws InvalidAttributeNameException {
        int position = this.getRelationSchema().getPositionOfAttributeNamed(attributeName);
        try {
            return this.getFieldAtPosition(position);
        } catch (InvalidFieldPositionException e) {
            throw new InvalidTupleForRelationSchemaException("Attribute name" + attributeName + " has a non-existant position");
        }
    }

    @Override
    public ArrayList<Field> getFields() {
        return this.getTuple().getFields();
    }

    @Override
    public RenamedAttributesRelationSchema getRelationSchema() {
        return this.relationSchema;
    }

    protected Tuple getTuple() {
        return this.tuple;
    }

    protected void setTuple(Tuple tuple) {
        this.tuple = tuple;
    }

    protected void setRelationSchema(RenamedAttributesRelationSchema relationSchema) {
        this.relationSchema = relationSchema;
    }
}
