package net.inator.qb2;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tempoc
 * Date: 10/28/11
 * Time: 3:05 PM
 * @since 1.1
 */
public class ValidateTest {

    @Test
    public void collectionTest() {
        List<Integer> list = null;
        Assert.assertFalse("Should not accept null", Q.validate(list));
        list = new ArrayList<Integer>();
        Assert.assertFalse("Should not accept an empty collection", Q.validate(list));
        list.add(1);
        Assert.assertTrue("Should accept non-empty collection", Q.validate(list));
    }

    @Test
    public void booleanTest() {
        Boolean b = null;
        Assert.assertFalse("Should not accept null", Q.validate(b));
        b = false;
        Assert.assertFalse("Should not accept false", Q.validate(b));
        b = true;
        Assert.assertTrue("Should accept true", Q.validate(b));
    }

    @Test
    public void objectTest() {
        Object o = null;
        Assert.assertFalse("Should not accept null", Q.validate(o));
        o = "something";
        Assert.assertTrue("Should accept a non-null object", Q.validate(o));
    }
}
