package net.sf.excompcel.spreadsheet.reportsheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.sf.excompcel.spreadsheet.ECCell;
import net.sf.excompcel.spreadsheet.ECReport;
import net.sf.excompcel.spreadsheet.ECRow;
import net.sf.excompcel.spreadsheet.ECSheet;
import net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject;
import net.sf.excompcel.spreadsheet.comparator.compareobject.CompareResultObject;
import net.sf.excompcel.spreadsheet.impl.BaseECImplTest;
import net.sf.excompcel.spreadsheet.reportsheet.FontSheet;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Detlev Struebig
 *
 */
public abstract class BaseFontSheetTest extends BaseECImplTest {

    /** Logger. */
    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BaseFontSheetTest.class);

    @SuppressWarnings("rawtypes")
    protected FontSheet sheet;

    @SuppressWarnings("rawtypes")
    protected ECCell cellNull;

    protected int iRow = 0;

    protected int iCol = 0;

    @SuppressWarnings("rawtypes")
    protected ECReport report;

    /**
	 * 
	 * @throws Exception
	 */
    public static void setUpBeforeClassTest(SpreadSheetTestType spreadSheetTestType) throws Exception {
        init(spreadSheetTestType, "commonMaster", "commonSlave");
        setUpBeforeClassBase(spreadSheetTestType);
    }

    @Before
    public void setBefore() throws Exception {
        iCol = 0;
        iRow = 0;
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.reportsheet.FontSheet#FontSheet(net.sf.excompcel.spreadsheet.impl.base.ECReportBase, net.sf.excompcel.model.MainModel)}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testSignSheetECReportBase() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.reportsheet.FontSheet#setCell(net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject, net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject, net.sf.excompcel.spreadsheet.comparator.compareobject.CompareResultObject)}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testSetCell_Null_Diff() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        ECCell cellMaster = cellNull;
        ECCell cellslave = cellNull;
        CellCompareObject master = new CellCompareObject(cellMaster);
        CellCompareObject slave = new CellCompareObject(cellslave);
        CompareResultObject result = new CompareResultObject();
        result.setEqual(false);
        sheet.setCell(master, slave, result);
        ECCell cellReport = sheet.getReportCell(iRow, iCol);
        assertEquals("", cellReport.getTextView());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.base.ECReportSheetBase#getReport()}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testGetReport() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        assertTrue(sheet instanceof FontSheet);
        assertNotNull(sheet.getReport());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.base.ECReportSheetBase#isReportRowExists(int)}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testIsReportRowExists() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        assertEquals("Font", sheet.getSheet().getSheetName());
        switch(spreadSheetTestType) {
            case XLS:
            case XLSX:
                assertFalse(sheet.isReportRowExists(0));
                assertFalse(sheet.isReportRowExists(10));
                break;
            case ODS:
                assertTrue(sheet.isReportRowExists(0));
                assertFalse(sheet.isReportRowExists(10));
                break;
        }
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.base.ECReportSheetBase#isReportCellExists(int, int)}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testIsReportCellExists() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        switch(spreadSheetTestType) {
            case XLS:
            case XLSX:
                assertFalse(sheet.isReportCellExists(0, 0));
                assertFalse(sheet.isReportCellExists(10, 10));
                break;
            case ODS:
                assertTrue(sheet.isReportRowExists(0));
                assertFalse(sheet.isReportRowExists(10));
                break;
        }
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.base.ECReportSheetBase#getReportRow(int)}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testGetReportRow() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        ECRow row = sheet.getReportRow(0);
        assertNotNull(row);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.base.ECReportSheetBase#getReportCell(int, int)}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testGetReportCell() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        ECCell cell = sheet.getReportCell(0, 0);
        assertNotNull(cell);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.base.ECReportSheetBase#getSheet()}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testGetSheet() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        ECSheet s = sheet.getSheet();
        assertNotNull(s);
        assertTrue(s.hasOriginalObject());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.base.ECReportSheetBase#getConvert()}.
	 * @throws Exception 
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testGetConvert() throws Exception {
        assertNotNull(report);
        sheet = (FontSheet) report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        assertNull(sheet.getConvert());
    }
}
