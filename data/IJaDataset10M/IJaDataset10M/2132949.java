package net.sf.jdpa.cg.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;

/**
 * @author Andreas Nilsson
 */
@RunWith(JMock.class)
public class CallTest {

    private Mockery context = new JUnit4Mockery();

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArg() {
        new Call(null);
    }

    @Test
    public void testCall() {
        Call call = new Call("foo");
        assertEquals("foo", call.getMethodName());
        assertEquals(ConstructType.INVOCATION, call.getConstructType());
        Expression obj = context.mock(Expression.class, "obj");
        Call newCall = call.of(obj);
        assertSame(call, newCall);
        assertSame(obj, call.getObject());
        Expression exp1 = context.mock(Expression.class, "exp1");
        Expression exp2 = context.mock(Expression.class, "exp2");
        newCall.with(exp1, exp2);
        assertArrayEquals(new Expression[] { exp1, exp2 }, newCall.getArguments());
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleOf() {
        new Call("foo").of(context.mock(Expression.class, "exp1")).of(context.mock(Expression.class, "exp2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleWith() {
        new Call("foo").with(context.mock(Expression.class, "exp1")).with(context.mock(Expression.class, "exp2"));
    }

    @Test
    public void testEquals() {
        Expression exp1 = context.mock(Expression.class, "exp1");
        Expression exp2 = context.mock(Expression.class, "exp2");
        Expression exp3 = context.mock(Expression.class, "exp3");
        Expression exp4 = context.mock(Expression.class, "exp4");
        Expression exp5 = context.mock(Expression.class, "exp5");
        assertEquals(new Call("foo").of(exp1).with(exp2), new Call("foo").of(exp1).with(exp2));
        assertFalse(new Call("bar").of(exp1).with(exp2).equals(new Call("foo").of(exp1).with(exp2)));
        assertFalse(new Call("foo").of(exp3).with(exp2).equals(new Call("foo").of(exp1).with(exp2)));
        assertFalse(new Call("foo").of(exp1).with(exp3).equals(new Call("foo").of(exp1).with(exp2)));
        assertFalse(new Call("foo").of(exp1).with(exp2).equals(null));
        assertFalse(new Call("foo").of(exp1).with(exp2).equals("foo"));
    }
}
