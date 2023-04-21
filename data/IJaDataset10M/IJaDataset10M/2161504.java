package com.dukesoftware.utils.data;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class BitSetTest {

    private BitSet set;

    @Before
    public void setup() {
        set = new BitSet();
    }

    @Test
    public void testTrue() {
        Assert.assertFalse(set.isTrue(0));
    }

    @Test
    public void testFalse() {
        set.setTrue(3);
        Assert.assertTrue(set.isTrue(3));
    }
}
