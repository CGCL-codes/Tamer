package org.databene.commons.condition;

import org.databene.commons.ArrayUtil;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link ComparationCondition}.<br/><br/>
 * Created at 29.04.2008 18:18:45
 * @since 0.4.2
 * @author Volker Bergmann
 */
public class ComparationConditionTest {

    @Test
    public void testEqual() {
        ComparationCondition<Integer> condition = new ComparationCondition<Integer>(ComparationCondition.EQUAL);
        assertTrue(condition.evaluate(ArrayUtil.toArray(1, 1)));
        assertFalse(condition.evaluate(ArrayUtil.toArray(1, 2)));
    }

    @Test
    public void testNotEqual() {
        ComparationCondition<Integer> condition = new ComparationCondition<Integer>(ComparationCondition.NOT_EQUAL);
        assertFalse(condition.evaluate(ArrayUtil.toArray(1, 1)));
        assertTrue(condition.evaluate(ArrayUtil.toArray(1, 2)));
    }

    @Test
    public void testGreaterOrEqual() {
        ComparationCondition<Integer> condition = new ComparationCondition<Integer>(ComparationCondition.GREATER_OR_EQUAL);
        assertTrue(condition.evaluate(ArrayUtil.toArray(1, 1)));
        assertFalse(condition.evaluate(ArrayUtil.toArray(1, 2)));
        assertTrue(condition.evaluate(ArrayUtil.toArray(2, 1)));
    }

    @Test
    public void testGreater() {
        ComparationCondition<Integer> condition = new ComparationCondition<Integer>(ComparationCondition.GREATER);
        assertFalse(condition.evaluate(ArrayUtil.toArray(1, 1)));
        assertFalse(condition.evaluate(ArrayUtil.toArray(1, 2)));
        assertTrue(condition.evaluate(ArrayUtil.toArray(2, 1)));
    }

    @Test
    public void testLessOrEqual() {
        ComparationCondition<Integer> condition = new ComparationCondition<Integer>(ComparationCondition.LESS_OR_EQUAL);
        assertTrue(condition.evaluate(ArrayUtil.toArray(1, 1)));
        assertTrue(condition.evaluate(ArrayUtil.toArray(1, 2)));
        assertFalse(condition.evaluate(ArrayUtil.toArray(2, 1)));
    }

    @Test
    public void testLess() {
        ComparationCondition<Integer> condition = new ComparationCondition<Integer>(ComparationCondition.LESS);
        assertFalse(condition.evaluate(ArrayUtil.toArray(1, 1)));
        assertTrue(condition.evaluate(ArrayUtil.toArray(1, 2)));
        assertFalse(condition.evaluate(ArrayUtil.toArray(2, 1)));
    }
}
