package net.sf.doolin.util.collection;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link FalsePredicate}.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class TestFalsePredicate {

    @Test
    public void test() {
        Assert.assertFalse(new FalsePredicate<MyBean>().evaluate(new MyBean()));
    }
}
