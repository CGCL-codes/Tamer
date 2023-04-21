package org.ujac.util.exi;

import org.ujac.util.exi.ExpressionException;

/**
 * Name: ConditionTest<br>
 * Description: A class testing conditional execution.
 * 
 * Example:
 * <code>
 *  ${${a isDefined} ? a : ''}
 * </code>
 * 
 * @author lauerc
 */
public class ConditionTest extends BaseExpressionTest {

    /**
   * @see junit.framework.TestCase#setUp()
   */
    protected void setUp() throws Exception {
        super.setUp();
        properties.put("NULL", null);
        properties.put("negativeInt", new Integer(-13));
        properties.put("negativeFloat", new Float(-13.0F));
        properties.put("negativeDouble", new Double(-13.0));
        properties.put("byte1", new Byte((byte) 8));
        properties.put("byte2", new Byte((byte) 15));
    }

    /**
   * Tests type casts.
   * @throws ExpressionException In case an expression evaluation fails.
   */
    public void testCondition() throws ExpressionException {
        assertEquals(exi.evalInt("${${negativeInt < 0} ? negativeInt : ${negativeInt * -1}}", properties, formatHelper), -13);
        assertEquals(exi.evalInt("${negativeInt < 0 ? negativeInt : negativeInt * -1}", properties, formatHelper), -13);
        assertEquals(exi.evalInt("${${NULL isDefined}?1:0}", properties, formatHelper), 0);
        assertEquals(exi.evalInt("${${NULL notDefined}?0:1}", properties, formatHelper), 0);
    }
}
