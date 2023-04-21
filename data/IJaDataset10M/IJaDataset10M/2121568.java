package org.base.apps.bean.util;

import org.base.apps.beans.util.Expr;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Expr}.
 * 
 * @author Kevan Simpson
 */
public class ExprTest {

    @Test
    public void testToString() throws Exception {
        Expr expr = Expr.newExpr("foo", "bar");
        Assert.assertNotNull("expr is null", expr);
        Assert.assertEquals("expr wrong", "foo/bar", expr.toString());
    }

    @Test
    public void testStep() throws Exception {
        Expr expr = Expr.newExpr("foo");
        Assert.assertNotNull("expr is null", expr);
        Assert.assertEquals("expr wrong", "foo", expr.toString());
        expr.step("bar");
        Assert.assertEquals("expr wrong", "foo/bar", expr.toString());
    }
}
