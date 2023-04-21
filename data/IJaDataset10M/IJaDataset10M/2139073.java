package org.objectstyle.cayenne.unit.jira;

import junit.framework.TestCase;
import org.objectstyle.cayenne.exp.Expression;

/**
 * @author Andrei Adamchik
 */
public class CAY_10062004_ExpTst extends TestCase {

    public void testDeepCopy() throws Exception {
        Expression parsed = Expression.fromString("(a = 1 and a = 2) or (a != 1 and a != 2)");
        Expression finalExpression = parsed.deepCopy();
        assertEquals(parsed, finalExpression);
        assertEquals(parsed.toString(), finalExpression.toString());
    }

    public void testAndExpOrExp() throws Exception {
        Expression parsed = Expression.fromString("(a = 1 and a = 2) or (a != 1 and a != 2)");
        Expression first = Expression.fromString("a = 1");
        Expression second = Expression.fromString("a = 2");
        Expression third = Expression.fromString("a != 1");
        Expression fourth = Expression.fromString("a != 2");
        Expression firstAndSecond = first.andExp(second);
        Expression thirdAndFourth = third.andExp(fourth);
        Expression finalExpression = firstAndSecond.orExp(thirdAndFourth);
        assertEquals(parsed, finalExpression);
        assertEquals(parsed.toString(), finalExpression.toString());
    }
}
