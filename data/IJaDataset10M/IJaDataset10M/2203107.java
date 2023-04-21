package com.google.code.morphia;

import org.junit.Assert;
import org.junit.Test;
import com.google.code.morphia.testmodel.Rectangle;

/**
 *
 * @author Scott Hernandez
 */
@SuppressWarnings({ "unused" })
public class TestKeyType extends TestBase {

    @Test
    public void testKeyComparisons() throws Exception {
        Rectangle r = new Rectangle(2, 1);
        Key<Rectangle> k1 = new Key<Rectangle>(Rectangle.class, r.getId());
        Key<Rectangle> k2 = this.ds.getKey(r);
        Assert.assertTrue(k1.equals(k2));
        Assert.assertTrue(k2.equals(k1));
    }
}
