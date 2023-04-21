package com.newisys.langschema;

/**
 * Represents a 'while' statement, which executes the given statement while the
 * given pre-condition is true.
 * 
 * @author Trevor Robinson
 */
public interface WhileStatement extends Statement {

    /**
     * Returns the expression that evaluates to the loop pre-condition.
     *
     * @return Expression
     */
    Expression getCondition();

    /**
     * Returns the statement to execute while the pre-condition is true.
     *
     * @return Statement
     */
    Statement getStatement();
}
