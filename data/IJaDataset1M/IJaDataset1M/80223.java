package org.argkit;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestArgumentDecorator extends TestCase {

    public void testEquals() {
        Argument<String> a = new Argument<String>("a");
        Argument<String> da1 = new MyDecorator<String>(new Argument<String>("a"));
        Argument<String> da2 = new MyDecorator<String>(new Argument<String>("a"));
        Assert.assertTrue(da1.equals(da2));
        Assert.assertTrue(da2.equals(da1));
        Assert.assertTrue(a.equals(da1));
        Assert.assertTrue(da2.equals(a));
    }

    public class MyDecorator<C> extends ArgumentDecorator<C> {

        private static final long serialVersionUID = 1L;

        public MyDecorator(Argument<C> argument) {
            super(argument);
        }
    }
}
