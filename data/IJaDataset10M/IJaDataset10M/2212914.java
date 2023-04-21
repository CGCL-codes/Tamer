package org.cantaloop.jiomask.testing.factory.javacode;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import junit.framework.TestCase;
import org.cantaloop.jiomask.factory.javacode.layout.CellDescriptor;
import org.cantaloop.jiomask.factory.javacode.layout.CellIterator;
import org.cantaloop.jiomask.factory.javacode.layout.RowDescriptor;

public class CellIteratorUTest extends TestCase {

    public CellIteratorUTest(String arg0) {
        super(arg0);
    }

    public void testEmpty() {
        CellIterator cit = new CellIterator(new ArrayList());
        assertTrue(!cit.hasNext());
        try {
            cit.next();
            fail("exception expected.");
        } catch (NoSuchElementException e) {
        }
    }

    public void testIteration() {
        CellDescriptor cell1 = new CellDescriptor();
        CellDescriptor cell2 = new CellDescriptor();
        CellDescriptor cell3 = new CellDescriptor();
        CellDescriptor cell4 = new CellDescriptor();
        List rows = new ArrayList();
        RowDescriptor row1 = new RowDescriptor();
        RowDescriptor row2 = new RowDescriptor();
        RowDescriptor row3 = new RowDescriptor();
        RowDescriptor row4 = new RowDescriptor();
        RowDescriptor row5 = new RowDescriptor();
        RowDescriptor row6 = new RowDescriptor();
        row2.addCell(cell1);
        row2.addCell(cell2);
        row3.addCell(cell3);
        row5.addCell(cell4);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
        rows.add(row6);
        CellIterator cit = new CellIterator(rows);
        assertSame(cell1, cit.next());
        assertSame(cell2, cit.next());
        assertSame(cell3, cit.next());
        assertSame(cell4, cit.next());
        assertTrue(!cit.hasNext());
    }
}
