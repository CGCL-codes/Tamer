package org.querycreator.operator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.SimpleExpression;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Luciano Molinari
 *
 */
public class TestGreaterAndEqualsThanOperator {

    private Operator greaterAndEqualsThanOperator;

    @Before
    public void setUpTest() {
        greaterAndEqualsThanOperator = new GreaterAndEqualsThanOperator();
    }

    @Test
    public void testCreateCriterion() {
        Criterion criterion = greaterAndEqualsThanOperator.createCriterion("salary", 10.0);
        assertTrue(criterion instanceof SimpleExpression);
    }

    @Test
    public void testCreateSql() {
        String sql = greaterAndEqualsThanOperator.createSql("salary");
        assertEquals("salary >= ?", sql);
    }
}
