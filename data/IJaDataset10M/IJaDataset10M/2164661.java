package net.sourceforge.nrl.parser.ast.constraints;

import net.sourceforge.nrl.parser.ast.IVariable;
import net.sourceforge.nrl.parser.model.IModelElement;

/**
 * A "global" exists statement. This tests for the existence or absence of an
 * object - rather than of an attribute or a member of a collection, like the
 * exists statement.
 * 
 * <p>
 * Abstract syntax reference:
 * <code>thereIs ("no")? element:ModelReference (Variable)? (constraint:Constraint)?</code>
 * 
 * @author Christian Nentwich
 */
public interface IGlobalExistsStatement extends IConstraint {

    /**
	 * Return the constraint to check relative to the object being iterated
	 * over. Use {@link #hasConstraint()} to check if it exists first.
	 * 
	 * @return the constraint or null
	 */
    public IConstraint getConstraint();

    /**
	 * Return the cardinality constraint if there is one. This will <b>always</b>
	 * return one or zero for the time being, zero indicating 'none'.
	 * <p>
	 * It never returns null.
	 * 
	 * @return the cardinality constraint, as a number
	 */
    public int getCount();

    /**
	 * Return the model element being checked for existence. Never returns
	 * null.
	 * 
	 * @return the model element
	 */
    public IModelElement getElement();

    /**
	 * If the statement is used to check for the existence (rather than absence)
	 * of an element, the element can be assigned to a variable if found. If
	 * a variable is assigned, it is returned here.
	 * 
	 * @return the variable or null if none was assigned
	 */
    public IVariable getVariable();

    /**
	 * Return true if there is a constraint attached that is to be executed
	 * relative to the model element.
	 * 
	 * @return true if a constraint is attached
	 */
    public boolean hasConstraint();
}
