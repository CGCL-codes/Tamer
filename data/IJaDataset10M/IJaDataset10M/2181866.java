package middlegen.predicates.relation;

import middlegen.RelationshipRole;
import org.apache.commons.collections.Predicate;

/**
 * Base class for column based predicates.
 *
 * @author Aslak Helles�y
 * @created 14. juli 2002
 */
public class Mandatory extends RelationshipRolePredicate {

    /**
    * @todo-javadoc Describe the field
    */
    private static final Predicate _instance = new Mandatory();

    /**
    * Describe what the method does
    *
    * @todo-javadoc Write javadocs for method
    * @todo-javadoc Write javadocs for method parameter
    * @todo-javadoc Write javadocs for return value
    * @param role Describe what the parameter does
    * @return Describe the return value
    */
    public boolean evaluate(RelationshipRole role) {
        return role.isMandatory();
    }

    /**
    * Gets the Instance attribute of the Mandatory class
    *
    * @return The Instance value
    */
    public static Predicate getInstance() {
        return _instance;
    }
}
