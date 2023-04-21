package net.sf.orcc.ir;

import org.eclipse.emf.ecore.EObject;

/**
 * This interface defines an expression.
 * 
 * @author Matthieu Wipliez
 * @author Jerome Gorin
 * @model abstract="true"
 * 
 */
public interface Expression extends EObject {

    /**
	 * Returns the type of this expression.
	 * 
	 * @return the type of this expression
	 */
    public Type getType();

    /**
	 * Returns true if the expression is a binary expression.
	 * 
	 * @return true if the expression is a binary expression
	 */
    public boolean isExprBinary();

    /**
	 * Returns true if the expression is a boolean expression.
	 * 
	 * @return true if the expression is a boolean expression
	 */
    public boolean isExprBool();

    /**
	 * Returns true if the expression is a float expression.
	 * 
	 * @return true if the expression is a float expression
	 */
    public boolean isExprFloat();

    /**
	 * Returns true if the expression is an integer expression.
	 * 
	 * @return true if the expression is an integer expression
	 */
    public boolean isExprInt();

    /**
	 * Returns true if the expression is a list expression.
	 * 
	 * @return true if the expression is a list expression
	 */
    public boolean isExprList();

    /**
	 * Returns true if the expression is a string expression.
	 * 
	 * @return true if the expression is a string expression
	 */
    public boolean isExprString();

    /**
	 * Returns true if the expression is a unary expression.
	 * 
	 * @return true if the expression is a unary expression
	 */
    public boolean isExprUnary();

    /**
	 * Returns true if the expression is a variable expression.
	 * 
	 * @return true if the expression is a variable expression
	 */
    public boolean isExprVar();
}
