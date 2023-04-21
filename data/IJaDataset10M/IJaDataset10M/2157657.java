package net.sf.excompcel.spreadsheet.impl.poihssf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.sf.excompcel.model.AppPreferences;
import net.sf.excompcel.model.MainModel;
import net.sf.excompcel.spreadsheet.comparator.compareobject.CellCompareObject;
import net.sf.excompcel.spreadsheet.comparator.compareobject.CompareResultObject;
import net.sf.excompcel.spreadsheet.impl.base.ECReportSheetBase;
import net.sf.excompcel.spreadsheet.reportsheet.DiffSheet;
import net.sf.excompcel.spreadsheet.reportsheet.FontSheet;
import net.sf.excompcel.spreadsheet.reportsheet.FormatSheet;
import net.sf.excompcel.spreadsheet.reportsheet.SignSheet;
import net.sf.excompcel.spreadsheet.reportsheet.TypeSheet;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

/**
 * @author Detlev Struebig
 * @since v0.8
 *
 */
public class PoiECReportTest extends BasePoiECTest {

    /** Logger. */
    private static Logger log = Logger.getLogger(PoiECReportTest.class);

    /**
	 * 
	 */
    public PoiECReportTest() throws IOException {
        super();
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#getModel()}.
	 */
    @Test
    public void testGetSetModel() {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
        assertTrue(model == report.getModel());
        MainModel modelTmp = new MainModel();
        report.setModel(modelTmp);
        assertFalse(model == report.getModel());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#PoiECReport(net.sf.excompcel.model.MainModel)}.
	 */
    @Test
    public void testPoiECReportModel() {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#PoiECReport(net.sf.excompcel.model.MainModel, HSSFWorkbook)}.
	 */
    @Test
    public void testPoiECReportModelWorkbook() {
        HSSFWorkbook wbReport = new HSSFWorkbook();
        PoiECReport report = new PoiECReport(model, wbReport);
        assertNotNull(report);
    }

    @Test
    public void testPoiECReportModelAppPreferencesWorkbook() {
        HSSFWorkbook wbReport = new HSSFWorkbook();
        AppPreferences prefs = AppPreferences.getInstance();
        PoiECReport report = new PoiECReport(model, prefs, wbReport);
        assertNotNull(report);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#getReportFilename()}.
	 */
    @Test
    public void testGetReportFile() {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
        String name = report.getReportFilename();
        assertNotNull(name);
        assertTrue(name.endsWith(".xls"));
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#getSheetByType(java.lang.Class)}.
	 */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSheetByType() {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
        ECReportSheetBase sheet = report.getSheetByType(FormatSheet.class);
        assertNotNull(sheet);
        assertTrue(sheet instanceof FormatSheet);
        sheet = report.getSheetByType(FontSheet.class);
        assertNotNull(sheet);
        assertTrue(sheet instanceof FontSheet);
        sheet = report.getSheetByType(TypeSheet.class);
        assertNotNull(sheet);
        assertTrue(sheet instanceof TypeSheet);
        sheet = report.getSheetByType(SignSheet.class);
        assertNotNull(sheet);
        assertTrue(sheet instanceof SignSheet);
        sheet = report.getSheetByType(DiffSheet.class);
        assertNotNull(sheet);
        assertTrue(sheet instanceof DiffSheet);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#save()}.
	 * @throws IOException 
	 */
    @Test
    public void testSave() throws IOException {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
        String name = report.save();
        log.debug(name);
        assertNotNull(name);
        assertTrue(new File(name).delete());
        assertFalse(new File(name).exists());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#save(java.lang.String)}.
	 * @throws IOException 
	 */
    @Test
    public void testSaveString() throws IOException {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
        String filename = "reportXX.xls";
        String name = report.save(filename);
        log.debug(name);
        assertNotNull(name);
        assertTrue(name.endsWith(filename));
        assertTrue(new File(name).delete());
        assertFalse(new File(name).exists());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#save(java.io.File)}.
	 * @throws IOException 
	 */
    @Test
    public void testSaveFile() throws IOException {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
        File filename = new File("reportXX.xls");
        String name = report.save(filename);
        log.debug(name);
        assertNotNull(name);
        assertEquals(name, filename.getAbsolutePath());
        assertTrue(new File(name).delete());
        assertFalse(new File(name).exists());
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#setReportCell(net.sf.excompcel.poi.sheet.comparator.compareobject.CellCompareObject, net.sf.excompcel.poi.sheet.comparator.compareobject.CellCompareObject, net.sf.excompcel.poi.sheet.comparator.compareobject.CompareResultObject)}.
	 */
    @Test
    public void testSetReportCell() {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
        CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> master = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, 0, 0);
        CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString> slave = new CellCompareObject<HSSFCell, HSSFCellStyle, HSSFFont, HSSFRichTextString>(null, 0, 0);
        CompareResultObject result = new CompareResultObject();
        report.setReportCell(master, slave, result);
    }

    /**
	 * Test method for {@link net.sf.excompcel.spreadsheet.impl.poihssf.PoiECReport#getListSheets()}.
	 */
    @Test
    public void testGetListSheets() {
        PoiECReport report = new PoiECReport(model);
        assertNotNull(report);
        List<ECReportSheetBase<HSSFWorkbook, HSSFSheet, HSSFRow, HSSFCell, HSSFFont, HSSFCellStyle, HSSFRichTextString>> list = report.getListSheets();
        assertNotNull(list);
        assertTrue(list.size() > 0);
        assertEquals(6, list.size());
    }
}
