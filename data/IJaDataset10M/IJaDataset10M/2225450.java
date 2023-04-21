package org.ddsteps.dataset.bean;

import java.util.Iterator;
import org.ddsteps.dataset.DataTable;
import org.ddsteps.dataset.bean.DataSetBean;
import org.ddsteps.mock.MockUtils;
import org.easymock.MockControl;
import junit.framework.TestCase;

/**
 * Unit test for the DataSetBean class.
 * 
 * @author Adam Skogman
 * @version $Id: DataSetBeanTest.java,v 1.1 2005/12/03 12:51:41 adamskogman Exp $
 */
public class DataSetBeanTest extends TestCase {

    private DataSetBean tested = new DataSetBean();

    public MockControl tableControl = MockControl.createControl(DataTable.class);

    private DataTable tableMock = (DataTable) tableControl.getMock();

    public MockControl table2Control = MockControl.createControl(DataTable.class);

    private DataTable table2Mock = (DataTable) table2Control.getMock();

    public void testGetTableNames_empty() {
        assertNotNull(tested.getTableNames());
        assertEquals(0, tested.getTableNames().length);
    }

    public void testGetTableNames_notEmpty() {
        tableControl.expectAndReturn(tableMock.getName(), "foo", MockControl.ONE);
        replay();
        tested.addTable(tableMock);
        assertNotNull(tested.getTableNames());
        assertEquals(1, tested.getTableNames().length);
        assertEquals("foo", tested.getTableNames()[0]);
        verify();
    }

    private void replay() {
        MockUtils.replay(this);
    }

    private void verify() {
        MockUtils.verify(this);
    }

    public void testGetTableNames_order() {
        tableControl.expectAndReturn(tableMock.getName(), "foo", MockControl.ONE);
        tableControl.expectAndReturn(tableMock.getName(), "bar", MockControl.ONE);
        replay();
        tested.addTable(tableMock);
        tested.addTable(tableMock);
        assertNotNull(tested.getTableNames());
        assertEquals(2, tested.getTableNames().length);
        assertEquals("foo", tested.getTableNames()[0]);
        assertEquals("bar", tested.getTableNames()[1]);
        verify();
    }

    public void testGetTableNames_replaceOrder() {
        tableControl.expectAndReturn(tableMock.getName(), "foo", MockControl.ONE);
        tableControl.expectAndReturn(tableMock.getName(), "bar", MockControl.ONE);
        tableControl.expectAndReturn(tableMock.getName(), "foo", MockControl.ONE);
        MockUtils.replay(this);
        tested.addTable(tableMock);
        assertNotNull(tested.getTableNames());
        assertEquals(1, tested.getTableNames().length);
        assertEquals("foo", tested.getTableNames()[0]);
        tested.addTable(tableMock);
        assertNotNull(tested.getTableNames());
        assertEquals(2, tested.getTableNames().length);
        assertEquals("foo", tested.getTableNames()[0]);
        assertEquals("bar", tested.getTableNames()[1]);
        tested.addTable(tableMock);
        assertNotNull(tested.getTableNames());
        assertEquals(2, tested.getTableNames().length);
        assertEquals("foo", tested.getTableNames()[0]);
        assertEquals("bar", tested.getTableNames()[1]);
        MockUtils.verify(this);
    }

    public void testTableIterator_0() {
        assertNotNull(tested.tableIterator());
        assertFalse(tested.tableIterator().hasNext());
    }

    public void testTableIterator_2() {
        assertNotNull(tested.tableIterator());
        assertFalse(tested.tableIterator().hasNext());
        tableControl.expectAndReturn(tableMock.getName(), "foo", MockControl.ONE);
        table2Control.expectAndReturn(table2Mock.getName(), "bar", MockControl.ONE);
        MockUtils.replay(this);
        tested.addTable(tableMock);
        tested.addTable(table2Mock);
        Iterator i2 = tested.tableIterator();
        assertTrue(i2.hasNext());
        assertSame(tableMock, i2.next());
        assertTrue(i2.hasNext());
        assertSame(table2Mock, i2.next());
        assertFalse(i2.hasNext());
        MockUtils.verify(this);
    }

    public void testTableIterator_1() {
        assertNotNull(tested.tableIterator());
        assertFalse(tested.tableIterator().hasNext());
        tableControl.expectAndReturn(tableMock.getName(), "foo", MockControl.ONE);
        MockUtils.replay(this);
        tested.addTable(tableMock);
        Iterator i1 = tested.tableIterator();
        assertTrue(i1.hasNext());
        assertSame(tableMock, i1.next());
        assertFalse(i1.hasNext());
        MockUtils.verify(this);
    }

    public void testGetTable() {
        assertNull(tested.getTable("foo"));
        tableControl.expectAndReturn(tableMock.getName(), "foo", MockControl.ONE);
        MockUtils.replay(this);
        tested.addTable(tableMock);
        assertSame(tableMock, tested.getTable("foo"));
        MockUtils.verify(this);
    }
}
