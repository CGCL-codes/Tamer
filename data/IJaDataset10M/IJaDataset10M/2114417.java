package org.ddsteps.dataset.excel;

import java.util.Date;
import junit.framework.TestCase;
import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import org.easymock.MockControl;

/**
 * Unit test for the ExcelDataValueAdapter class.
 * 
 * @author Adam Skogman
 * @version $Id: ExcelDataValueAdapterTest.java,v 1.1 2005/12/03 12:51:40 adamskogman Exp $
 */
public class ExcelDataValueAdapterTest extends TestCase {

    private MockControl headerControl = MockControl.createControl(Cell.class);

    private Cell headerMock = (Cell) headerControl.getMock();

    private MockControl dataControl = MockControl.createControl(Cell.class);

    private Cell dataMock = (Cell) dataControl.getMock();

    public void testGetName() {
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, dataMock);
        reset();
        headerControl.expectAndReturn(headerMock.getContents(), "foo");
        replay();
        assertEquals("foo", tested.getName());
        verify();
    }

    private void reset() {
        headerControl.reset();
        dataControl.reset();
    }

    private void replay() {
        headerControl.replay();
        dataControl.replay();
    }

    private void verify() {
        headerControl.verify();
        dataControl.verify();
    }

    public void testGetValue_string() {
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, dataMock);
        reset();
        dataControl.expectAndReturn(dataMock.getType(), CellType.LABEL);
        dataControl.expectAndReturn(dataMock.getContents(), "foo");
        replay();
        assertEquals("foo", tested.getValue());
        verify();
    }

    public void testGetValue_stringFormula() {
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, dataMock);
        reset();
        dataControl.expectAndReturn(dataMock.getType(), CellType.STRING_FORMULA);
        dataControl.expectAndReturn(dataMock.getContents(), "foo");
        replay();
        assertEquals("foo", tested.getValue());
        verify();
    }

    public void testGetValue_number() {
        MockControl control = MockControl.createControl(NumberCell.class);
        NumberCell mock = (NumberCell) control.getMock();
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, mock);
        reset();
        control.reset();
        control.expectAndReturn(mock.getType(), CellType.NUMBER);
        control.expectAndReturn(mock.getValue(), 3.14);
        replay();
        control.replay();
        assertEquals(new Double(3.14), tested.getValue());
        control.verify();
        verify();
    }

    public void testGetValue_numberFormula() {
        MockControl control = MockControl.createControl(NumberCell.class);
        NumberCell mock = (NumberCell) control.getMock();
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, mock);
        reset();
        control.reset();
        control.expectAndReturn(mock.getType(), CellType.NUMBER_FORMULA);
        control.expectAndReturn(mock.getValue(), 3.14);
        replay();
        control.replay();
        assertEquals(new Double(3.14), tested.getValue());
        control.verify();
        verify();
    }

    public void testGetValue_bool() {
        MockControl control = MockControl.createControl(BooleanCell.class);
        BooleanCell mock = (BooleanCell) control.getMock();
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, mock);
        reset();
        control.reset();
        control.expectAndReturn(mock.getType(), CellType.BOOLEAN);
        control.expectAndReturn(mock.getValue(), true);
        replay();
        control.replay();
        assertSame(Boolean.TRUE, tested.getValue());
        control.verify();
        verify();
    }

    public void testGetValue_boolFormula() {
        MockControl control = MockControl.createControl(BooleanCell.class);
        BooleanCell mock = (BooleanCell) control.getMock();
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, mock);
        reset();
        control.reset();
        control.expectAndReturn(mock.getType(), CellType.BOOLEAN_FORMULA);
        control.expectAndReturn(mock.getValue(), true);
        replay();
        control.replay();
        assertSame(Boolean.TRUE, tested.getValue());
        control.verify();
        verify();
    }

    public void testGetValue_date() {
        MockControl control = MockControl.createControl(DateCell.class);
        DateCell mock = (DateCell) control.getMock();
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, mock);
        Date now = new Date();
        reset();
        control.reset();
        control.expectAndReturn(mock.getType(), CellType.DATE);
        control.expectAndReturn(mock.getDate(), now);
        replay();
        control.replay();
        assertSame(now, tested.getValue());
        control.verify();
        verify();
    }

    public void testGetValue_dateFormula() {
        MockControl control = MockControl.createControl(DateCell.class);
        DateCell mock = (DateCell) control.getMock();
        ExcelDataValueAdapter tested = new ExcelDataValueAdapter(headerMock, mock);
        Date now = new Date();
        reset();
        control.reset();
        control.expectAndReturn(mock.getType(), CellType.DATE_FORMULA);
        control.expectAndReturn(mock.getDate(), now);
        replay();
        control.replay();
        assertSame(now, tested.getValue());
        control.verify();
        verify();
    }
}
