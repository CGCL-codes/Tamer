package org.josef.jpa;

import static org.josef.annotations.Status.Stage.TEST;
import static org.josef.annotations.Status.UnitTests.COMPLETE;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.josef.dmc.SqlStatementUtil;
import org.josef.util.CDebug;

/**
 * Abstraction of a JP/QL where clause builder using Reverse Polish Notation.
 * <br>For example: Assume you have to create the following where clause from
 * its individual parts: a = 3 and (b = 7 or (c=8 and d=9))<br>
 * Using this where clause builder the code you need looks like this:<pre><code>
 *     final WhereClauseRpn whereClause = new WhereClauseRpn();
 *     whereClause.push("a=3").push("b=7").push("c=8").push("d=9");
 *     whereClause.and();
 *     whereClause.or();
 *     whereClause.and();
 * </code></pre>
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2840 $
 */
@Status(stage = TEST, unitTests = COMPLETE)
@Review(by = "Kees Schotanus", at = "2009-10-08", reason = "After creation of unit tests")
public class WhereClauseRpn {

    /**
     * The where clause.
     */
    private final Stack<String> stack = new Stack<String>();

    /**
     * The JP/QL parameters.
     */
    private final List<Object> parameters = new ArrayList<Object>();

    /**
     * Pushes the supplied expression on the stack.
     * @param expression The expression to push on the stack.
     * @return This WherelauseRpn.
     * @throws IllegalArgumentException When the supplied expression is empty.
     * @throws NullPointerException When the supplied expression is null.
     */
    public WhereClauseRpn push(final String expression) {
        CDebug.checkParameterNotEmpty(expression, expression);
        stack.push(expression);
        return this;
    }

    /**
     * Pushes a relational expression, constructed from the supplied column,
     * operator and value on the stack.
     * <br>Note: No expression is pushed when the supplied value is null, or is
     * of type String and is empty.
     * @param column The column.
     * @param operator The relational operator to apply.
     * @param value The value.
     * @throws IllegalArgumentException When the supplied column is empty.
     * @throws NullPointerException When either the supplied column or operator
     *  is null.
     */
    public void pushRelationalExpression(final String column, final RelationalOperator operator, final Object value) {
        CDebug.checkParameterNotEmpty(column, "column");
        CDebug.checkParameterNotNull(operator, "operator");
        if (value != null && (!(value instanceof String) || ((String) value).length() > 0)) {
            push(column + operator + createPositionalParameter(value));
        }
    }

    /**
     * Pushes a LIKE expression (where the percent sign is at the end of the
     * expression), created from the supplied column and value on the stack.
     * <br>Note: No expression is added when the supplied value is null or
     * empty.<br>
     * Note: This method has build in protection against the supplied value
     * already containing a wildcard character. In such cases the '%' wildcard
     * character is not added.
     * @param column The column.
     * @param value The value.
     * @throws IllegalArgumentException When the supplied column is empty.
     * @throws NullPointerException When the supplied column is null.
     */
    public void pushStartsWith(final String column, final String value) {
        CDebug.checkParameterNotEmpty(column, "column");
        if (value != null && value.length() != 0) {
            final String parameter = SqlStatementUtil.containsWildcard(value) ? value : value + "%";
            push(column + " LIKE " + createPositionalParameter(parameter));
        }
    }

    /**
     * Pushes a LIKE expression (where the percent sign is at the beginning of
     * the expression), created from the supplied column and value on the stack.
     * <br>Note: No expression is added when the supplied value is null or
     * empty.<br>
     * Note: This method has build in protection against the supplied value
     * already containing a wildcard character. In such cases the '%' wildcard
     * character is not added.
     * @param column The column.
     * @param value The value.
     * @throws IllegalArgumentException When the supplied column is empty.
     * @throws NullPointerException When the supplied column is null.
     */
    public void pushEndsWith(final String column, final String value) {
        CDebug.checkParameterNotEmpty(column, "column");
        if (value != null && value.length() != 0) {
            final String parameter = SqlStatementUtil.containsWildcard(value) ? value : "%" + value;
            push(column + " LIKE " + createPositionalParameter(parameter));
        }
    }

    /**
     * Pushes a BETWEEN expression, created from the supplied column and minimum
     * and maximum values, on the stack.
     * <br>Note: No expression is added when both minimum and maximum are null.
     * @param column The column.
     * @param minimum The minimum value.
     * @param maximum The maximum value.
     * @throws IllegalArgumentException When the supplied column is empty.
     * @throws NullPointerException When the supplied column is null.
     */
    public void pushBetween(final String column, final Object minimum, final Object maximum) {
        CDebug.checkParameterNotEmpty(column, "column");
        if (minimum == null && maximum == null) {
            return;
        }
        if (minimum == null) {
            push(column + "<=" + createPositionalParameter(maximum));
        } else if (maximum == null) {
            push(column + ">=" + createPositionalParameter(minimum));
        } else {
            push(column + " BETWEEN " + createPositionalParameter(minimum) + " AND " + createPositionalParameter(maximum));
        }
    }

    /**
     * Pushes an IS NULL expression, created from the supplied column, on the
     * stack.
     * @param column The column.
     * @throws IllegalArgumentException When the supplied column is empty.
     * @throws NullPointerException When the supplied column is null.
     */
    public void pushIsNull(final String column) {
        CDebug.checkParameterNotEmpty(column, "column");
        push(column + " IS NULL");
    }

    /**
     * Pushes an IS NOT NULL expression, created from the supplied column, on
     * the stack.
     * @param column The column.
     * @throws IllegalArgumentException When the supplied column is empty.
     * @throws NullPointerException When the supplied column is null.
     */
    public void pushIsNotNull(final String column) {
        CDebug.checkParameterNotEmpty(column, "column");
        push(column + " IS NOT NULL");
    }

    /**
     * Performs an AND operation.
     * @return This WherelauseRpn.
     * @throws java.util.EmptyStackException When the stack contains less than
     *  two expressions.
     */
    public WhereClauseRpn and() {
        final String rightHandSide = stack.pop();
        final String leftHandSide = stack.pop();
        stack.push("(" + leftHandSide + " AND " + rightHandSide + ")");
        return this;
    }

    /**
     * Performs an OR operation.
     * @return This WherelauseRpn.
     * @throws java.util.EmptyStackException When the stack contains less than
     *  two expressions.
     */
    public WhereClauseRpn or() {
        final String rightHandSide = stack.pop();
        final String leftHandSide = stack.pop();
        stack.push("(" + leftHandSide + " OR " + rightHandSide + ")");
        return this;
    }

    /**
     * Creates a String representation of this WhereClauseRpn consisting of the
     * where clause (provided the where clause has been build).
     * <br>Note: As long as the where clause has not been build, this method
     * returns the expression at the top of the stack.
     * @return The where clause.
     */
    public String toString() {
        return stack.size() == 0 ? "" : stack.peek();
    }

    /**
     * Creates a positional parameter like ?1 and adds the supplied parameter to
     * the list of jp/ql parameters.
     * @param parameter The parameter corresponding to the returned positional
     *  parameter.
     * @return A positional parameter.
     */
    private String createPositionalParameter(final Object parameter) {
        parameters.add(parameter);
        return "?" + parameters.size();
    }

    /**
     * Gets all the JP/QL parameters.
     * @return The JP/QL parameters.
     *  <br />The returned List may be empty but will never be null!
     */
    public List<Object> getParameters() {
        return parameters;
    }
}
