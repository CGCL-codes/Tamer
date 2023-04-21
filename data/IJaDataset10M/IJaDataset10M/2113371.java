package net.sf.excompcel.spreadsheet.impl.poihssf.comparator.compareobject;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import net.sf.excompcel.spreadsheet.ECCellDataCommon;
import net.sf.excompcel.spreadsheet.ECTypeEnum;
import net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject;
import net.sf.excompcel.spreadsheet.comparator.compareobject.ComparePositionBase;
import net.sf.excompcel.spreadsheet.impl.poihssf.PoiECCell;
import net.sf.excompcel.spreadsheet.impl.poihssf.PoiECSheet;
import net.sf.excompcel.spreadsheet.impl.poihssf.BasePoiECTest;
import net.sf.excompcel.spreadsheet.impl.poihssf.PoiECWorkbook;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Detlev Struebig
 *
 */
public class CellCompareObjectTest extends BasePoiECTest {

    /** Logger. */
    private static Logger log = Logger.getLogger(CellCompareObjectTest.class);

    /** The Workbook for Test */
    private static PoiECWorkbook wbPoi;

    /** The Sheet for Test. */
    private static PoiECSheet sheet;

    private int row1 = 0;

    private int row2 = 0;

    private int col1 = 0;

    private int col2 = 1;

    private PoiECCell cell1;

    private PoiECCell cell2;

    private CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> cco1;

    private CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> cco2;

    /**
	 * Constructor.
	 * @throws IOException
	 */
    public CellCompareObjectTest() throws IOException {
        super();
        String filename = model.getFilenameMaster();
        log.debug("FileName=" + filename);
        wbPoi = new PoiECWorkbook(filename);
        assertNotNull(wbPoi);
        sheet = (PoiECSheet) wbPoi.getSheet(0);
        assertNotNull(sheet);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        cell1 = (PoiECCell) sheet.getRow(row1).getCell(col1);
        assertNotNull(cell1);
        cell2 = (PoiECCell) sheet.getRow(row2).getCell(col2);
        assertNotNull(cell2);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#isObjectExists()}.
	 */
    @Test
    public void testIsObjectExists() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row2, col2);
        assertFalse(cco1.isObjectExists());
        assertFalse(cco2.isObjectExists());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(new PoiECCell(null), row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(new PoiECCell(null), row2, col2);
        assertFalse(cco1.isObjectExists());
        assertFalse(cco2.isObjectExists());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertTrue(cco1.isObjectExists());
        assertTrue(cco2.isObjectExists());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#CellCompareObject(net.sf.excompcel.spreadsheet.impl.base.ECCellBase, int, int)}.
	 */
    @Test
    public void testCellCompareObject() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertNotNull(cco1);
        assertNotNull(cco2);
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row2, col2);
        assertNotNull(cco1);
        assertNotNull(cco2);
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(new PoiECCell(null), row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(new PoiECCell(null), row2, col2);
        assertNotNull(cco1);
        assertNotNull(cco2);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#getViewText()}.
	 */
    @Test
    public void testGetViewText() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertEquals("x", cco1.getViewText());
        assertEquals("y", cco2.getViewText());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row2, col2);
        assertEquals("[Null]", cco1.getViewText());
        assertEquals("[Null]", cco2.getViewText());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(new PoiECCell(null), row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(new PoiECCell(null), row2, col2);
        assertEquals("[Null]", cco1.getViewText());
        assertEquals("[Null]", cco2.getViewText());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#getText()}.
	 */
    @Test
    public void testGetText() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertEquals("x", cco1.getText());
        assertEquals("y", cco2.getText());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row2, col2);
        assertEquals("[Null]", cco1.getText());
        assertEquals("[Null]", cco2.getText());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(new PoiECCell(null), row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(new PoiECCell(null), row2, col2);
        assertEquals("[Null]", cco1.getText());
        assertEquals("[Null]", cco2.getText());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#getCellTypeToString()}.
	 */
    @Test
    public void testGetCellTypeToString() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertEquals(ECTypeEnum.CELL_TYPE_STRING.toString(), cco1.getCellTypeToString());
        assertEquals(ECTypeEnum.CELL_TYPE_STRING.toString(), cco2.getCellTypeToString());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row2, col2);
        assertEquals("[Null]", cco1.getCellTypeToString());
        assertEquals("[Null]", cco2.getCellTypeToString());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#getCellTypeToViewText()}.
	 */
    @Test
    public void testGetCellTypeToViewText() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertEquals(ECTypeEnum.CELL_TYPE_STRING.toString(), cco1.getCellTypeToViewText());
        assertEquals(ECTypeEnum.CELL_TYPE_STRING.toString(), cco2.getCellTypeToViewText());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row2, col2);
        assertEquals(ECCellDataCommon.FILL_NULL_TEXT, cco1.getCellTypeToViewText());
        assertEquals(ECCellDataCommon.FILL_NULL_TEXT, cco2.getCellTypeToViewText());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#isFormulaCell()}.
	 */
    @SuppressWarnings("unchecked")
    @Test
    public void testIsFormulaCell_NoOriginalObject() {
        CellCompareObject co = new CellCompareObject(null, 0, 0);
        assertFalse(co.isFormulaCell());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#isFormulaCell()}.
	 */
    @Test
    public void testIsFormulaCell() {
        row2 = 12;
        col2 = 0;
        cell2 = (PoiECCell) sheet.getRow(row2).getCell(col2);
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertFalse(cco1.isFormulaCell());
        assertEquals("A12+1", cco2.getText());
        assertEquals("8.0", cco2.getViewText());
        assertEquals(ECTypeEnum.CELL_TYPE_FORMULA.toString(), cco2.getCellTypeToViewText());
        assertTrue(cco2.isFormulaCell());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#getCell()}.
	 */
    @Test
    public void testGetCell() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        PoiECCell cellTmp = (PoiECCell) cco1.getCell();
        assertNotNull(cellTmp);
        assertTrue(cellTmp.hasOriginalObject());
        cellTmp = (PoiECCell) cco2.getCell();
        assertNotNull(cellTmp);
        assertTrue(cellTmp.hasOriginalObject());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject#toString()}.
	 */
    @Test
    public void testToString() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertEquals(" Row=0 Column=0 Content=x", cco1.toString());
        assertEquals(" Row=0 Column=1 Content=y", cco2.toString());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        assertEquals("NULL", cco1.toString());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.ComparePositionBase#getRowPosition()}.
	 */
    @Test
    public void testGetRowPosition() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertEquals(row1, cco1.getRowPosition());
        assertEquals(row2, cco2.getRowPosition());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row2, col2);
        assertEquals(row1, cco1.getRowPosition());
        assertEquals(row2, cco2.getRowPosition());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.ComparePositionBase#getColPosition()}.
	 */
    @Test
    public void testGetColPosition() {
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertEquals(col1, cco1.getColPosition());
        assertEquals(col2, cco2.getColPosition());
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, row2, col2);
        assertEquals(col1, cco1.getColPosition());
        assertEquals(col2, cco2.getColPosition());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.comparator.compareobject.ComparatorBase#hashCode()}.
	 */
    @Test
    public void testHashCode() {
        Set<ComparePositionBase> holdCompareObjects = new HashSet<ComparePositionBase>();
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, row1, col1);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, row2, col2);
        assertEquals(0, cco1.hashCode());
        assertEquals(1, cco2.hashCode());
        holdCompareObjects.add(cco1);
        holdCompareObjects.add(cco2);
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, 5, 5);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, 15, 15);
        assertEquals(55, cco1.hashCode());
        assertEquals(1515, cco2.hashCode());
        holdCompareObjects.add(cco1);
        holdCompareObjects.add(cco2);
        assertTrue(holdCompareObjects.contains(cco1));
        assertTrue(holdCompareObjects.contains(cco2));
        cco1 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell1, 55, 55);
        cco2 = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(cell2, 25, 25);
        assertFalse(holdCompareObjects.contains(cco1));
        assertFalse(holdCompareObjects.contains(cco2));
    }
}
