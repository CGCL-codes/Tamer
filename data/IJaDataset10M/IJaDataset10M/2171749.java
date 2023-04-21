package middlegen.predicates.column;

import middlegen.predicate.Not;
import org.apache.commons.collections.Predicate;

/**
 * Describe what this class does
 *
 * @author Aslak Helles�y
 * @created 21. august 2002
 * @todo-javadoc Write javadocs
 */
public class Basic extends Not {

    /**
    * @todo-javadoc Describe the field
    */
    private static final Predicate _instance = new Basic();

    /**
    * Describe what the MandatoryBasic constructor does
    *
    * @todo-javadoc Write javadocs for constructor
    */
    public Basic() {
        add(ForeignKey.getInstance());
    }

    /**
    * Gets the Instance attribute of the MandatoryBasic class
    *
    * @return The Instance value
    */
    public static Predicate getInstance() {
        return _instance;
    }
}
