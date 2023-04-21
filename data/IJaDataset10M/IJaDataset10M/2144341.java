package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import riaf.facade.IRowModel;
import riaf.facade.ITableModel;
import riaf.models.RowModel;
import riaf.models.TableModel;
import riafswing.RCellComponent;

public class TableModelBasicTest {

    ITableModel table;

    @Before
    public void init() {
        table = new TableModel(null);
        for (int i = 0; i < 20; i++) {
            IRowModel row = table.appendRow(null);
        }
    }

    @Test
    public void testConstructor() throws Exception {
        assertNotNull(table);
    }

    @Test
    public void testClear() throws Exception {
        table.clear();
        assertEquals(0, table.getRows().size());
        assertEquals(0, table.getNumChildren());
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(20, table.getRows().size());
        assertEquals(20, table.getNumChildren());
    }

    @Test
    public void testSetAndGetHeader() throws Exception {
        IRowModel row = new RowModel(null);
        table.addColumn("row1", "row1");
        table.addColumn("row2", "row2");
        table.addColumn("row3", "row3");
        assertTrue(row == table.getHeader());
    }

    @Test
    public void testSelectionAndGet() throws Exception {
        table.setSelection(3, 5, 6, 8);
        IRowModel[] selected = table.getSelectedRows();
        assertTrue(selected[0] == table.get(3));
        assertTrue(selected[1] == table.get(5));
        assertTrue(selected[2] == table.get(6));
        assertTrue(selected[3] == table.get(8));
    }

    @Test
    public void testRemove() throws Exception {
        for (int i = 0; i < table.getRows().size(); i++) {
        }
        assertEquals(0, table.getNumChildren());
    }

    @Test
    public void testRemove2() throws Exception {
        for (int i = 0; i < table.getRows().size(); i++) {
        }
        assertEquals(0, table.getNumChildren());
    }

    @Test
    public void testInsertRowBefore() throws Exception {
        IRowModel newRow = table.appendRow(null);
        newRow.setContent("test");
        table.insertRowBefore(table.get(5), "newRow");
        assertEquals(newRow.getContent(), table.get(5).getContent());
    }
}
