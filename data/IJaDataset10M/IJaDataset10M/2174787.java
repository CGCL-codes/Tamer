package middlegen.predicates.relation;

import middlegen.predicate.AttributedPredicate;
import middlegen.RelationshipRole;

/**
 * Base class for column based predicates.
 *
 * @author Aslak Helles�y
 * @created 14. juli 2002
 */
public abstract class RelationshipRolePredicate extends AttributedPredicate {

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param o Describe what the parameter does
    * @return Describe the return value
    */
    public final boolean evaluate(Object o) {
        return evaluate((RelationshipRole) o);
    }

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param column Describe what the parameter does
    * @return Describe the return value
    */
    public abstract boolean evaluate(RelationshipRole column);
}
