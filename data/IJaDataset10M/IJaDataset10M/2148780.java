package net.sf.paperclips;

import junit.framework.TestCase;
import org.eclipse.swt.SWT;

public class GridCellTest extends TestCase {

    public void testEquals() {
        GridCell cell = new GridCell(SWT.DEFAULT, SWT.DEFAULT, new PrintStub(0), 1);
        assertTrue(cell.equals(new GridCell(SWT.DEFAULT, SWT.DEFAULT, new PrintStub(0), 1)));
        assertFalse(cell.equals(new GridCell(SWT.CENTER, SWT.DEFAULT, new PrintStub(0), 1)));
        assertFalse(cell.equals(new GridCell(SWT.DEFAULT, SWT.CENTER, new PrintStub(0), 1)));
        assertFalse(cell.equals(new GridCell(SWT.DEFAULT, SWT.DEFAULT, new PrintStub(1), 1)));
        assertFalse(cell.equals(new GridCell(SWT.DEFAULT, SWT.DEFAULT, new PrintStub(0), 2)));
    }
}
