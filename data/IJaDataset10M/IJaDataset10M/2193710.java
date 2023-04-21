package net.sf.unruly.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Jeff Drost
 */
public class TestUtil {

    private static final Log LOG = LogFactory.getLog(TestUtil.class);

    /**
	 * just remove N objects from a collection.
	 */
    public static void remove(Collection collection, int count) {
        int targetSize = collection.size() - count;
        Assert.isTrue(targetSize >= 0);
        Iterator iterator = collection.iterator();
        while (iterator.hasNext() && collection.size() != targetSize) {
            iterator.next();
            iterator.remove();
        }
        Assert.equals(targetSize, collection.size());
    }

    public static void removeOne(Collection collection) {
        remove(collection, 1);
    }

    /**
	 * TestNG requires a 2 dimentional array of objects for it's
	 * DataProvider functionality.  This helper simply turns a single
	 * dimentional array to a 2 dimentional array of arrays of size 1.
	 */
    public static Object[][] addDimension(Object[] input) {
        Assert.notNull(input);
        Object[][] output = new Object[input.length][1];
        for (int i = 0; i < input.length; i++) {
            output[i][0] = input[i];
        }
        return output;
    }
}
