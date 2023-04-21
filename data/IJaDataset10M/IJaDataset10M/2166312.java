package net.sf.jdpa.cg.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import static org.junit.Assert.*;

/**
 * @author Andreas Nilsson
 */
@RunWith(JMock.class)
public class ReturnTest {

    private Mockery context = new JUnit4Mockery();

    @Test
    public void testInitialState() {
        Return ret = new Return();
        assertNull(ret.getReturnValue());
        Expression exp = context.mock(Expression.class);
        ret = new Return(exp);
        assertSame(exp, ret.getReturnValue());
        assertEquals(ConstructType.RETURN, ret.getConstructType());
    }

    @Test
    public void testEquals() {
        Expression exp1 = context.mock(Expression.class, "exp1");
        Expression exp2 = context.mock(Expression.class, "exp2");
        assertEquals(new Return(), new Return());
        assertFalse(new Return(exp1).equals(new Return()));
        assertFalse(new Return(exp1).equals(new Return(exp2)));
        assertFalse(new Return(exp1).equals(null));
        assertFalse(new Return(exp1).equals("foo"));
    }
}
