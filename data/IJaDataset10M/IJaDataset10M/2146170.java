package org.crappydbms.queries.operators.join.natural;

import java.util.ArrayList;
import java.util.List;
import org.crappydbms.queries.operators.Operator;
import org.crappydbms.queries.operators.join.CompositeOperator;
import org.crappydbms.queries.operators.join.RelationsWithSameNameException;
import org.crappydbms.queries.operators.join.cartesianproduct.JoinOperator;
import org.crappydbms.queries.operators.projection.ProjectionOperator;
import org.crappydbms.queries.operators.selection.SelectionOperator;
import org.crappydbms.queries.predicates.Predicate;
import org.crappydbms.queries.predicates.TruePredicate;
import org.crappydbms.queries.predicates.simple.factories.EqualsPredicateFactory;
import org.crappydbms.relations.Relation;
import org.crappydbms.relations.RelationIterator;
import org.crappydbms.relations.schemas.RelationSchema;
import org.crappydbms.relations.schemas.attributes.Attribute;
import org.crappydbms.relations.tuples.Tuple;
import org.crappydbms.transactions.Transaction;
import org.crappydbms.transactions.TransactionAbortedException;

/**
 * @author Facundo Manuel Quiroga
 * Dec 11, 2008
 * 
 */
public class NaturalJoinOperator extends CompositeOperator {

    protected Operator innerOperator;

    public NaturalJoinOperator(Relation firstRelation, Relation secondRelation) throws MatchingNonEqualAttributesException, RelationsWithSameNameException {
        JoinOperator joinOperator = new JoinOperator(firstRelation, secondRelation);
        this.setFirstRelation(firstRelation);
        this.setSecondRelation(secondRelation);
        List<Attribute> commonAttributes = this.getCommonAttributesBetween(firstRelation, secondRelation);
        Predicate joinPredicate = this.createNaturalJoinPredicate(commonAttributes, firstRelation.getName(), secondRelation.getName());
        SelectionOperator selectionOperator = new SelectionOperator(joinOperator, joinPredicate);
        List<String> commonAttributeNamesOfSecondRelation = this.attributeNamesWithRelationNameAppended(commonAttributes, secondRelation.getName());
        ProjectionOperator projectionOperator = new ProjectionOperator(selectionOperator, commonAttributeNamesOfSecondRelation);
        this.setInnerOperator(projectionOperator);
    }

    /**
	 * @param commonAttributes
	 * @return
	 */
    private List<String> attributeNamesWithRelationNameAppended(List<Attribute> commonAttributes, String relationName) {
        List<String> commonAttributeNames = new ArrayList<String>();
        for (Attribute attribute : commonAttributes) {
            commonAttributeNames.add(relationName + "." + attribute.getName());
        }
        return commonAttributeNames;
    }

    /**
	 * @param firstRelation
	 * @param secondRelation
	 * @return
	 * @throws MatchingNonEqualAttributesException if there
	 */
    protected List<Attribute> getCommonAttributesBetween(Relation firstRelation, Relation secondRelation) throws MatchingNonEqualAttributesException {
        List<Attribute> matchingAttributes = this.getMatchingAttributesFrom(firstRelation.getSchema(), secondRelation.getSchema());
        List<Attribute> equalAttributes = this.getEqualAttributesFrom(firstRelation.getSchema(), secondRelation.getSchema());
        if (!matchingAttributes.equals(equalAttributes)) {
            throw new MatchingNonEqualAttributesException();
        }
        return equalAttributes;
    }

    /**
	 * @return a list of attributes, so that the name of the attribute is the same for both relations 
	 */
    protected List<Attribute> getMatchingAttributesFrom(RelationSchema firstRelationSchema, RelationSchema secondRelationSchema) {
        List<Attribute> commonAttributes = new ArrayList<Attribute>();
        for (Attribute attribute : firstRelationSchema.getAttributes()) {
            for (Attribute secondAttribute : secondRelationSchema.getAttributes()) {
                if (attribute.matches(secondAttribute)) {
                    commonAttributes.add(attribute);
                }
            }
        }
        return commonAttributes;
    }

    protected ArrayList<Attribute> getEqualAttributesFrom(RelationSchema firstRelationSchema, RelationSchema secondRelationSchema) {
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        for (Attribute attribute : firstRelationSchema.getAttributes()) {
            for (Attribute secondAttribute : secondRelationSchema.getAttributes()) {
                if (attribute.equals(secondAttribute)) {
                    attributes.add(attribute);
                }
            }
        }
        return attributes;
    }

    /**
	 * @param commonAttributes
	 * @param string2 
	 * @param string 
	 * @return
	 */
    protected Predicate createNaturalJoinPredicate(List<Attribute> commonAttributes, String firstRelationName, String secondRelationName) {
        if (commonAttributes.size() == 0) {
            return new TruePredicate();
        }
        String attributeName = commonAttributes.get(0).getName();
        String firstAttributeName = firstRelationName + "." + attributeName;
        String secondAttributeName = secondRelationName + "." + attributeName;
        Predicate predicate = EqualsPredicateFactory.newPredicate(firstAttributeName, secondAttributeName);
        for (int i = 1; i < commonAttributes.size(); i++) {
            attributeName = commonAttributes.get(0).getName();
            firstAttributeName = firstRelationName + "." + attributeName;
            secondAttributeName = secondRelationName + "." + attributeName;
            predicate = predicate.and(EqualsPredicateFactory.newPredicate(firstAttributeName, secondAttributeName));
        }
        return predicate;
    }

    /**
	 * @param firstRelationSchema
	 * @param secondRelationSchema
	 * @return
	 */
    @Override
    public String getName() {
        String firstRelationName = this.getFirstRelation().getName();
        String secondRelationName = this.getSecondRelation().getName();
        return "( " + firstRelationName + ") NaturalJoin (" + secondRelationName + " )";
    }

    @Override
    public RelationSchema getSchema() {
        return this.getInnerOperator().getSchema();
    }

    @Override
    public RelationIterator<Tuple> iterator(Transaction transaction) throws TransactionAbortedException {
        return this.getInnerOperator().iterator(transaction);
    }

    protected Operator getInnerOperator() {
        return this.innerOperator;
    }

    protected void setInnerOperator(Operator innerOperator) {
        this.innerOperator = innerOperator;
    }
}
