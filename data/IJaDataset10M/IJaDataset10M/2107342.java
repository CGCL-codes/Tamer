package org.enjavers.jethro.dspi.metasql;

import java.io.Serializable;

/**
 * Each SQLExpression instance is a piece of an sql query or, more generally, a
 * syntattical correct sql expression. Note that this top-class, like all the classes 
 * of this package, is not intended as a precise, complete and correct sql representation;
 * the metasql package purpose is to serve as a container of the dinamically-generated
 * query. In this sense, they provides an sql object representation to a 'coarse grain' detail level:
 * only statement are represented as objects, whereby 'WHERE' conditions are not.
 * 
 * @author Alessandro Lombardi
 */
public abstract class SQLExpression implements Serializable {

    /**
	 * indicates if the sql expression is "clean", i.e. in the same state
	 * after the invocation of clearExpression();
	 */
    protected boolean _clearedExpression = true;

    protected StringBuffer _expression;

    public SQLExpression() {
        clearExpression();
    }

    /**
	 * Clear the expression string, bringing it to an "erased" state;
	 * if redefine this method, be aware to set the _clearedExpression flag. 
	 */
    public void clearExpression() {
        _clearedExpression = true;
    }

    public StringBuffer getExpression() {
        return _expression;
    }

    /**
	 * Append the param to the sql expression.
	 * @deprecated If possible, use specific method of the subclasses.
	 * @param i_expression any expression.
	 */
    public void appendExpression(String i_expression) {
        _expression.append(i_expression);
        _clearedExpression = false;
    }

    public boolean isCleared() {
        return _clearedExpression;
    }

    public String toString() {
        if (_clearedExpression) return "";
        return _expression.toString();
    }
}
