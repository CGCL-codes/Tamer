package org.ddsteps.dataset.decorator;

import java.util.Iterator;
import junit.framework.TestCase;
import junitx.framework.ArrayAssert;
import junitx.framework.Assert;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.ddsteps.dataset.bean.DataSetBean;
import org.ddsteps.dataset.bean.DataTableBean;
import org.ddsteps.mock.MockUtils;
import org.easymock.MockControl;

/**
 * Unit test for the DataSetDecorator class.
 * 
 * @author Adam Skogman
 * @version $Id: DataSetDecoratorTest.java,v 1.1 2005/12/03 12:51:40 adamskogman
 *          Exp $
 */
public class DataSetDecoratorTest extends TestCase {

    private DataSetBean set;

    private DataTableBean table1;

    private DataTableBean table2;

    public MockControl predControl = MockControl.createControl(Predicate.class);

    private Predicate predMock = (Predicate) predControl.getMock();

    protected void setUp() throws Exception {
        super.setUp();
        table1 = new DataTableBean();
        table1.setName("oneReal");
        table2 = new DataTableBean();
        table2.setName("twoReal");
        set = new DataSetBean();
        set.addTable(table1);
        set.addTable(table2);
    }

    void replay() {
        MockUtils.replay(this);
    }

    void verify() {
        MockUtils.verify(this);
    }

    public void testGetTableNames() {
        DataSetDecorator tested = new DataSetDecorator(set);
        ArrayAssert.assertEquals(set.getTableNames(), tested.getTableNames());
    }

    public void testGetTableNames_filter() {
        predControl.expectAndReturn(predMock.evaluate("oneReal"), true);
        predControl.expectAndReturn(predMock.evaluate("twoReal"), false);
        DataSetDecorator tested = new DataSetDecorator(set);
        tested.setDataTableNamePredicate(predMock);
        replay();
        ArrayAssert.assertEquals(new String[] { "oneReal" }, tested.getTableNames());
        verify();
    }

    public void testTableIterator() {
        predControl.expectAndReturn(predMock.evaluate("oneReal"), true);
        predControl.expectAndReturn(predMock.evaluate("twoReal"), false);
        Transformer dummy = new Transformer() {

            public Object transform(Object arg0) {
                fail("Should not be called");
                return null;
            }
        };
        DataSetDecorator tested = new DataSetDecorator(set);
        tested.setDataTableNamePredicate(predMock);
        tested.setDataValueTransformer(dummy);
        replay();
        Iterator i = tested.tableIterator();
        assertTrue(i.hasNext());
        DataTableDecorator dtd = (DataTableDecorator) i.next();
        assertSame(table1, dtd.dataTable);
        assertSame(dummy, dtd.dataValueTransformer);
        assertFalse(i.hasNext());
        verify();
    }

    public void testGetTable_ok() {
        predControl.expectAndReturn(predMock.evaluate("oneReal"), true);
        Transformer dummy = new Transformer() {

            public Object transform(Object arg0) {
                fail("Should not be called");
                return null;
            }
        };
        DataSetDecorator tested = new DataSetDecorator(set);
        tested.setDataTableNamePredicate(predMock);
        tested.setDataValueTransformer(dummy);
        replay();
        assertNotNull(tested.getTable("oneReal"));
        verify();
    }

    public void testGetTable_backingNull() {
        DataSetDecorator tested = new DataSetDecorator(set);
        replay();
        try {
            assertNull(tested.getTable("noSuchTable"));
        } catch (Exception e) {
            Assert.fail("Should not throw", e);
        }
        verify();
    }

    public void testSetDataValueTransformer() {
    }

    public void testSetDataTableNamePredicate() {
    }
}
