package org.crappydbms.queries.operators.projection;

import java.util.List;
import org.crappydbms.queries.operators.SimpleOperator;
import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Dec 4, 2008
 * 
 */
public class ProjectionOperator extends SimpleOperator {

    protected ProjectedRelationSchema projectedRelationSchema;

    public ProjectionOperator(Relation relation, List<String> removedAttributeNames) {
        super(relation);
        this.setProjectedRelationSchema(new ProjectedRelationSchema(relation.getSchema(), removedAttributeNames));
    }

    @Override
    public RelationSchema getSchema() {
        return this.getProjectedRelationSchema();
    }

    @Override
    public RelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
        return new ProjectedOperatorIterator(this.getRelation().iterator(transaction), this.getProjectedRelationSchema());
    }

    protected ProjectedRelationSchema getProjectedRelationSchema() {
        return this.projectedRelationSchema;
    }

    protected void setProjectedRelationSchema(ProjectedRelationSchema projectedRelationSchema) {
        this.projectedRelationSchema = projectedRelationSchema;
    }
}
