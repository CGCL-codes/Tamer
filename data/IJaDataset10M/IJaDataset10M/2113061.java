package org.enerj.apache.commons.collections.bidimap;

import java.util.Map;
import java.util.TreeMap;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.enerj.apache.commons.collections.BidiMap;
import org.enerj.apache.commons.collections.OrderedBidiMap;

/**
 * Test class for AbstractOrderedBidiMapDecorator.
 * 
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 */
public class TestAbstractOrderedBidiMapDecorator extends AbstractTestOrderedBidiMap {

    public TestAbstractOrderedBidiMapDecorator(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestAbstractOrderedBidiMapDecorator.class);
    }

    public BidiMap makeEmptyBidiMap() {
        return new TestOrderedBidiMap();
    }

    public Map makeConfirmedMap() {
        return new TreeMap();
    }

    public boolean isAllowNullKey() {
        return false;
    }

    public boolean isAllowNullValue() {
        return false;
    }

    public boolean isSetValueSupported() {
        return true;
    }

    /**
     * Simple class to actually test.
     */
    private static final class TestOrderedBidiMap extends AbstractOrderedBidiMapDecorator {

        private TestOrderedBidiMap inverse = null;

        public TestOrderedBidiMap() {
            super(new DualTreeBidiMap());
        }

        public TestOrderedBidiMap(OrderedBidiMap map) {
            super(map);
        }

        public BidiMap inverseBidiMap() {
            if (inverse == null) {
                inverse = new TestOrderedBidiMap((OrderedBidiMap) getBidiMap().inverseBidiMap());
                inverse.inverse = this;
            }
            return inverse;
        }
    }
}
