package org.exist.xmldb;

/**
 * Identifies a compiled representation of an XQuery expression.
 * 
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public interface CompiledExpression {

    /**
	 * Prepare the expression for being reused.
	 *
	 */
    public void reset();
}
