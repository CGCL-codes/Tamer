package com.google.werkzeugkasten.meta.impl;

import org.junit.Assert;
import org.junit.Test;
import com.google.werkzeugkasten.meta.Chain;

public class AbstractChainContextTest {

    @Test
    public void testExecute() {
        TestContext ctx = new TestContext();
        ctx.add(new Chain<String, TestContext>() {

            public String execute(TestContext context) {
                Assert.assertTrue(true);
                try {
                    return context.execute();
                } catch (IllegalStateException e) {
                    throw new AssertionError();
                }
            }
        });
        final String ok = "test";
        ctx.add(new Chain<String, TestContext>() {

            public String execute(TestContext context) {
                Assert.assertTrue(true);
                try {
                    context.execute();
                    throw new AssertionError();
                } catch (IllegalStateException e) {
                    Assert.assertTrue(true);
                }
                return ok;
            }
        });
        Assert.assertEquals(ok, ctx.execute());
    }

    class TestContext extends AbstractChainContext<String, TestContext> {
    }
}
