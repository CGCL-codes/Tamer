package net.sf.jmedcat.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 * @author Peter Butkovic
 *
 */
public class CollectionUtilTest {

    @Test
    public void isEmptyForNull() {
        Assert.assertTrue(CollectionUtil.isEmpty(null));
    }

    @Test
    public void isEmptyForEmpty() {
        Assert.assertTrue(CollectionUtil.isEmpty(new ArrayList<Object>()));
        Assert.assertTrue(CollectionUtil.isEmpty(new HashSet<Object>()));
        Assert.assertTrue(CollectionUtil.isEmpty(new HashSet<Object>(10)));
    }

    @Test
    public void isNonEmpty() {
        Assert.assertFalse(CollectionUtil.isEmpty(Arrays.asList("a", "b")));
        Assert.assertFalse(CollectionUtil.isEmpty(Arrays.asList("")));
        Assert.assertFalse(CollectionUtil.isEmpty(Arrays.asList("", "")));
    }
}
