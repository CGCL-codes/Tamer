package org.objectwiz.metadata.dataset;

import java.util.Iterator;
import junit.framework.TestCase;
import org.objectwiz.metadata.criteria.Criteria;
import org.objectwiz.testapp.TestApplication;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class DataSetTest extends TestCase {

    /**
     * A dataset that throws specific {@link UnsupportedOperationException}s
     * that can be recognized within this {@link TestCase} for checking
     * behavior regarding {@link Criteria}s.
     */
    public class TestDataSet extends UnimplementedDataSet {

        public TestDataSet(String unitName) {
            super(unitName);
        }

        @Override
        public long getRowCount(Criteria criteria) {
            throw new UnsupportedOperationException("Row count: " + criteria);
        }

        @Override
        public Iterator<DatasetRow> getRowIterator(Criteria criteria) {
            throw new UnsupportedOperationException("Row iterator: " + criteria);
        }
    }

    /**
     * Test that the mechanism of attaching/detaching the application proxy
     * behaves well.
     */
    public void testAttachProxy() {
        TestApplication app = TestApplication.sharedInstance();
        DataSet ds = new UnimplementedDataSet(app.getUnit().getName());
        ds.attachApplicationProxy(app.getApplicationProxy());
        assertEquals(app.getProxy(), ds.getUnitProxy());
        ds.detachProxy();
        try {
            ds.getUnitProxy();
            fail("still attached");
        } catch (Exception e) {
        }
    }

    /**
     * Test that {@link DataSet#getRowCount()} redirects correctly to
     * {@link DataSet#getRowCount(Criteria)} with a NULL value.
     */
    public void testGetRowCountWithoutCriteria() {
        try {
            DataSet ds = new TestDataSet("test");
            ds.getRowCount();
            fail("getRowCount does not rely on getRowCount(Criteria)");
        } catch (UnsupportedOperationException e) {
            assertEquals("Row count: null", e.getMessage());
        }
    }

    /**
     * Test that {@link DataSet#getRowIterator()} redirects correctly
     * to {@link DataSet#getRowIterator(Criteria)} with a NULL value.
     */
    public void testGetRowIteratorWithoutCriteria() {
        try {
            DataSet ds = new TestDataSet("test");
            ds.getRowIterator();
            fail("getRowIterator does not rely on getRowIterator(Criteria)");
        } catch (UnsupportedOperationException e) {
            assertEquals("Row iterator: null", e.getMessage());
        }
    }
}
