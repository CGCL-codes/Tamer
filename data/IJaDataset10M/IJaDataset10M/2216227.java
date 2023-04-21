package org.crappydbms.queries.operators.rename;

import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 23, 2009
 * 
 */
public class RenameRelationOperator implements Relation {

    protected Relation relation;

    protected String newName;

    public RenameRelationOperator(Relation relation, String newName) {
        this.setRelation(relation);
        this.setNewName(newName);
    }

    @Override
    public RelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
        return this.getRelation().iterator(transaction);
    }

    @Override
    public String getName() {
        return this.getNewName();
    }

    @Override
    public RelationSchema getSchema() {
        return this.getRelation().getSchema();
    }

    protected Relation getRelation() {
        return this.relation;
    }

    protected void setRelation(Relation relation) {
        this.relation = relation;
    }

    protected String getNewName() {
        return this.newName;
    }

    protected void setNewName(String newName) {
        this.newName = newName;
    }
}
