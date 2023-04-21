package com.teletalk.jserver.util;

/**
 * The class StringConstraint is a Constraint to be used when filtering String values.
 * 
 * @see Filter
 * @see Constraint
 * 
 * @author Tobias L�fstrand
 * 
 * @since Alpha
 */
public class StringConstraint extends Constraint {

    static final long serialVersionUID = 3784831190772049502L;

    public static final ConstraintOperator EQUALS = new ConstraintOperator("equals", "doesn't equal");

    public static final ConstraintOperator EQUALS_IGNORE_CASE = new ConstraintOperator("equals (ignore case)", "doesn't equal (ignore case)");

    public static final ConstraintOperator CONTAINS = new ConstraintOperator("contains", "doesn't contain");

    public static final ConstraintOperator CONTAINS_IGNORE_CASE = new ConstraintOperator("contains (ignore case)", "doesn't contain (ignore case)");

    public static final ConstraintOperator STARTS_WITH = new ConstraintOperator("starts with", "doesn't start with");

    public static final ConstraintOperator STARTS_WITH_IGNORE_CASE = new ConstraintOperator("starts with (ignore case)", "doesn't start with (ignore case)");

    public static final Object[] constraintOperators = new Object[] { EQUALS, EQUALS_IGNORE_CASE, CONTAINS, CONTAINS_IGNORE_CASE, STARTS_WITH, STARTS_WITH_IGNORE_CASE };

    private String constraintString;

    /**
	 * Constructs a new StringConstraint.
	 * 
	 * @param constraintOperator the operator for this StringConstraint.
	 * @param constraintString the constraintString for this StringConstraint.
	 */
    public StringConstraint(ConstraintOperator constraintOperator, String constraintString) {
        super(constraintOperator, false);
        this.constraintString = constraintString;
    }

    /**
	 * Constructs a new StringConstraint.
	 * 
	 * @param constraintOperator the operator for this StringConstraint.
	 * @param constraintString the constraintString for this StringConstraint.
	 * @param not indicating whether or not and logical NOT should be used in conjunction with the operator when filtering.
	 */
    public StringConstraint(ConstraintOperator constraintOperator, String constraintString, boolean not) {
        super(constraintOperator, not);
        this.constraintString = constraintString;
    }

    /**
	 * Checkes if the object specified in paramterer obj conforms to the
	 * condition specified by constraintOperator and constraintString in
	 * this StringConstraint.
	 * 
	 *  @return true if the object specefied by the parameter obj passes the check,
	 * otherwise false. This method also returns true if the parameter obj isn't
	 * an instance of java.lang.String.
	 */
    public final boolean filter(Object obj) {
        if (obj instanceof String) {
            boolean result;
            if (constraintOperator.equals(EQUALS)) {
                result = ((String) obj).equals(constraintString);
            } else if (constraintOperator.equals(EQUALS_IGNORE_CASE)) {
                result = ((String) obj).equalsIgnoreCase(constraintString);
            } else if (constraintOperator.equals(CONTAINS)) {
                result = (((String) obj).indexOf(constraintString) >= 0);
            } else if (constraintOperator.equals(CONTAINS_IGNORE_CASE)) {
                result = (((String) obj).toUpperCase().indexOf(constraintString.toUpperCase()) >= 0);
            } else if (constraintOperator.equals(STARTS_WITH)) {
                result = ((String) obj).startsWith(constraintString);
            } else if (constraintOperator.equals(STARTS_WITH_IGNORE_CASE)) {
                result = ((String) obj).toUpperCase().startsWith(constraintString.toUpperCase());
            } else result = true;
            if (not) return !result; else return result;
        }
        return true;
    }

    /**
	* Returns a String describing this StringConstraint.
	* 
	* @return a string representation of the StringConstraint.
	*/
    public final String toString() {
        return constraintOperator.toString(not) + " '" + constraintString + "'";
    }

    /**
	 * Returns the available ConstraintOperators in this class. 
	 */
    public static final Object[] getConstraintOperators() {
        return constraintOperators;
    }

    /**
	 * Returns a ConstraintOperator with a specific name. 
	 * 
	 * @param name the name of the operator to look for.
	 * @param notFlag indicating if the specified name is a notName.
	 */
    public static final ConstraintOperator getConstraintOperator(String name, boolean notFlag) {
        Object[] ops = getConstraintOperators();
        for (int i = 0; i < ops.length; i++) {
            if (((ConstraintOperator) ops[i]).toString(notFlag).equals(name)) return ((ConstraintOperator) ops[i]);
        }
        return null;
    }

    /**
	* Compares two StringConstraints.
	* 
	* @return true if the constraintOperands, constraintStrings and notflags in the two StringConstraints are equal.
	*/
    public boolean equals(Object obj) {
        if (obj instanceof StringConstraint) {
            StringConstraint c = (StringConstraint) obj;
            return ((Object) this.constraintString).equals(c.constraintString) && (this.constraintOperator.equals(c.constraintOperator)) && (this.not == c.not);
        } else return false;
    }
}
