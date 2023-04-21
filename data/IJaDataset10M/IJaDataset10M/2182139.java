package org.kumenya.api.reference;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jean Morissette
 */
public class ErrorMessage {

    private ErrorMessage() {
    }

    public static final Map<String, String> ref = new HashMap<String, String>();

    static {
        ref.put("42X03", "Column name '%s' is in more than one table in the FROM list.");
        ref.put("42X04", "Column '%s' is not in any table in the FROM list.");
        ref.put("42X05", "Table '%s' does not exist.");
        ref.put("42X09", "The table or alias name '%s' is used more than once in the FROM list.");
        ref.put("42X14", "'%1s' is not a column in tuple '%2s'.");
        ref.put("42X40", "A NOT statement has an operand that is not boolean.");
        ref.put("42X79", "Column name '%s' appears more than once in the result of the query expression.");
        ref.put("42818", "Comparisons between '%1s' and '%2s' are not supported.");
        ref.put("42X25", "The '<functionName>' function is not allowed on the '<type>' type.");
        ref.put("42X36", "The '%1s' operator is not allowed to take a '%2s' parameter as an operand.");
        ref.put("42X38", "'SELECT *' only allowed in EXISTS and NOT EXISTS subqueries.");
        ref.put("42X39", "Subquery is only allowed to return a single column.");
        ref.put("42X44", "Invalid length '<length>' in column specification.");
        ref.put("42X53", "The LIKE predicate can only have 'CHAR' or 'VARCHAR' operands. Type '<type>' is not permitted.");
        ref.put("42X80", "VALUES clause must contain at least one element. Empty elements are not allowed.");
        ref.put("42Y22", "Aggregate '%1' cannot operate on type '%2'.");
        ref.put("42Y30", "The SELECT list of a grouped query contains at least one invalid expression. " + "If a SELECT list has a GROUP BY, the list may only contain grouping columns and valid aggregate expressions.");
        ref.put("42Y94", "An AND or OR has a non-boolean operand. The operands of AND and OR must evaluate to TRUE, FALSE, or UNKNOWN.");
        ref.put("42803", "An expression containing the column '%s' appears in the SELECT list and is not part of a GROUP BY clause.");
        ref.put("42622", "The name '<name>' is too long. The maximum length is '<maximumLength>'.");
        ref.put("60X00", "The having clause is not of type boolean.");
    }
}
