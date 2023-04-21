package net.sf.jdpa.cg.model;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;

/**
 * @author Andreas Nilsson
 */
@RunWith(JMock.class)
public class CastTest {

    private Mockery context = new JUnit4Mockery();

    private Expression expression;

    @Before
    public void setup() {
        expression = context.mock(Expression.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullType() {
        new Cast(null, expression);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullExpression() {
        new Cast("java.lang.String", null);
    }

    @Test
    public void testInitialState() {
        Cast cast = new Cast("java.lang.String", expression);
        assertEquals("java.lang.String", cast.getTargetType());
        assertSame(expression, cast.getExpression());
        assertEquals(ConstructType.CAST, cast.getConstructType());
    }

    @Test
    public void testEquals() {
        Expression exp2 = context.mock(Expression.class, "exp2");
        assertEquals(new Cast("java.lang.String", expression), new Cast("java.lang.String", expression));
        assertFalse(new Cast("java.lang.String", expression).equals(new Cast("java.lang.Object", expression)));
        assertFalse(new Cast("java.lang.String", expression).equals(new Cast("java.lang.String", exp2)));
        assertFalse(new Cast("java.lang.String", expression).equals(null));
        assertFalse(new Cast("java.lang.String", expression).equals("foo"));
    }

    @Test
    public void testHashCode() {
        assertEquals("java.lang.String".hashCode() ^ expression.hashCode(), new Cast("java.lang.String", expression).hashCode());
    }
}
