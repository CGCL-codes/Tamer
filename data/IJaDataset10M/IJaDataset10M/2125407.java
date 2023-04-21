package oracle.toplink.essentials.internal.expressions;

import oracle.toplink.essentials.exceptions.*;
import oracle.toplink.essentials.queryframework.*;
import oracle.toplink.essentials.expressions.*;
import oracle.toplink.essentials.internal.sessions.AbstractRecord;
import oracle.toplink.essentials.internal.sessions.AbstractSession;
import oracle.toplink.essentials.descriptors.ClassDescriptor;

/**
 * Used for logical AND and OR.  This is not used by NOT.
 */
public class LogicalExpression extends CompoundExpression {

    /**
     * LogicalExpression constructor comment.
     */
    public LogicalExpression() {
        super();
    }

    /**
     * INTERNAL:
     * Used for debug printing.
     */
    public String descriptionOfNodeType() {
        return "Logical";
    }

    /**
     * INTERNAL:
     * Check if the object conforms to the expression in memory.
     * This is used for in-memory querying.
     * If the expression in not able to determine if the object conform throw a not supported exception.
     */
    public boolean doesConform(Object object, AbstractSession session, AbstractRecord translationRow, InMemoryQueryIndirectionPolicy valueHolderPolicy, boolean objectIsUnregistered) {
        if (getOperator().getSelector() == ExpressionOperator.And) {
            return getFirstChild().doesConform(object, session, translationRow, valueHolderPolicy, objectIsUnregistered) && getSecondChild().doesConform(object, session, translationRow, valueHolderPolicy, objectIsUnregistered);
        } else if (getOperator().getSelector() == ExpressionOperator.Or) {
            return getFirstChild().doesConform(object, session, translationRow, valueHolderPolicy, objectIsUnregistered) || getSecondChild().doesConform(object, session, translationRow, valueHolderPolicy, objectIsUnregistered);
        }
        throw QueryException.cannotConformExpression();
    }

    /**
     * INTERNAL:
     * Extract the primary key from the expression into the row.
     * Ensure that the query is quering the exact primary key.
     * Return false if not on the primary key.
     */
    public boolean extractPrimaryKeyValues(boolean requireExactMatch, ClassDescriptor descriptor, AbstractRecord primaryKeyRow, AbstractRecord translationRow) {
        if (getOperator().getSelector() != ExpressionOperator.And) {
            if (requireExactMatch || (getOperator().getSelector() != ExpressionOperator.Or)) {
                return false;
            }
        }
        boolean validExpression = getFirstChild().extractPrimaryKeyValues(requireExactMatch, descriptor, primaryKeyRow, translationRow);
        if (requireExactMatch && (!validExpression)) {
            return false;
        }
        return getSecondChild().extractPrimaryKeyValues(requireExactMatch, descriptor, primaryKeyRow, translationRow);
    }

    /**
     * INTERNAL:
     */
    public boolean isLogicalExpression() {
        return true;
    }
}
