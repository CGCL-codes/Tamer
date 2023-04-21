package com.w20e.socrates.expression;

/**
 * The Expression interface defines the minimum requirements for an
 * expression. These requirements will be shared by all implementing
 * classes like Operation and XObject, so as to be able to evaluate
 * any of these.
 */
public interface Expression {

    /**
   * Evaluate the expression to an XResult, so as to be able to process further,
   * for instance compare with another expression, etc.
   *
   * @return The <code>XObject</code> that is the result of the
   * evaluation.
   */
    XObject eval();

    /**
   * Return the string representation of this expression.
   *
   * @return the string representation of the expression.
   */
    String toString();

    /**
   * Return the result of this expression as boolean value. Usually, for
   * literals like strings and numeric values, this means that for null, "" and
   * 0 'false' should be returned, otherwise 'true'.
   *
   * @return The boolean value of the evaluated expression.
   */
    boolean toBoolean();

    /**
   * Get the type for this expression. In case of, for example,
   * operations, this should return the name of the operation, like
   * 'Multiply', 'And', etc.
   *
   * @return String
   */
    String getType();
}
