package org.kopsox.spreadsheet.data.common;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.kopsox.spreadsheet.SpreadsheetFactory;
import org.kopsox.spreadsheet.SpreadsheetFactory.SpreadsheetType;
import org.kopsox.spreadsheet.data.Sheet;
import org.kopsox.spreadsheet.data.Workbook;

public abstract class AbstractWorkbookTest extends TestCase {

    private final String SHEET_NAME = "SHEET_NAME";

    protected Workbook workbook;

    protected Integer numberOfSelectedSheet;

    protected ArrayList<String> sheetNames;

    @Override
    public abstract void setUp() throws Exception;

    @Test
    public void testCreateNewSheet() {
        checkProperties();
        int numberOfSheets = this.workbook.getNumberOfSheets();
        Sheet newSheet = this.workbook.createNewSheet();
        Assert.assertNotNull(newSheet);
        Assert.assertTrue(numberOfSheets < this.workbook.getNumberOfSheets());
        this.sheetNames.add(newSheet.getSheetIndex(), newSheet.getSheetName());
    }

    @Test
    public void testCreateNewSheetString() {
        checkProperties();
        int numberOfSheets = this.workbook.getNumberOfSheets();
        Sheet newSheet = this.workbook.createNewSheet(SHEET_NAME);
        Assert.assertNotNull(newSheet);
        Assert.assertEquals(SHEET_NAME, newSheet.getSheetName());
        Assert.assertTrue(numberOfSheets < this.workbook.getNumberOfSheets());
        this.sheetNames.add(newSheet.getSheetIndex(), newSheet.getSheetName());
    }

    @Test
    public void testGetNumberOfSheets() {
        checkProperties();
        Assert.assertEquals(this.sheetNames.size(), this.workbook.getNumberOfSheets());
    }

    @Test
    public void testGetSelectedSheetIndex() {
        checkProperties();
        Assert.assertEquals(this.numberOfSelectedSheet.intValue(), this.workbook.getSelectedSheetIndex());
    }

    @Test
    public void testGetSheetByName() {
        checkProperties();
        Sheet sheetWithIndex0 = this.workbook.getSheetByIndex(0);
        Assert.assertNotNull(sheetWithIndex0);
        Sheet sheet = this.workbook.getSheetByName(sheetWithIndex0.getSheetName());
        Assert.assertNotNull(sheet);
        Assert.assertEquals(sheetWithIndex0, sheet);
        Assert.assertEquals(this.sheetNames.get(0), sheet.getSheetName());
    }

    @Test
    public void testGetSheetByIndex() {
        checkProperties();
        Sheet sheetWithIndex0 = this.workbook.getSheetByIndex(0);
        Assert.assertNotNull(sheetWithIndex0);
        Assert.assertEquals(this.sheetNames.get(0), sheetWithIndex0.getSheetName());
    }

    @Test
    public void testSetSelectedSheet() {
        checkProperties();
        int newSelection = 0;
        int oldSelection = this.numberOfSelectedSheet.intValue();
        if (oldSelection == 0) {
            if (this.workbook.getNumberOfSheets() == 1) {
                this.workbook.createNewSheet();
            }
            newSelection++;
        }
        this.workbook.setSelectedSheet(newSelection);
        Assert.assertEquals(newSelection, this.workbook.getSelectedSheetIndex());
        Assert.assertFalse(oldSelection == this.workbook.getSelectedSheetIndex());
        this.workbook.setSelectedSheet(oldSelection);
        Assert.assertEquals(oldSelection, this.workbook.getSelectedSheetIndex());
        Assert.assertFalse(newSelection == this.workbook.getSelectedSheetIndex());
    }

    public abstract void testSave();

    public abstract SpreadsheetType getSpreadsheetType();

    @Test
    public void testEquals() {
        checkProperties();
        Assert.assertTrue(this.workbook.equals(this.workbook));
        try {
            Assert.assertFalse(this.workbook.equals(SpreadsheetFactory.createWorkbook("otherName", getSpreadsheetType())));
        } catch (Exception e) {
            Assert.assertTrue("Exception thrown:" + e, false);
        }
    }

    @Test
    public void testHashCode() {
        checkProperties();
        Assert.assertEquals(this.workbook.hashCode(), this.workbook.hashCode());
        try {
            Assert.assertTrue(this.workbook.hashCode() != SpreadsheetFactory.createWorkbook("otherName", getSpreadsheetType()).hashCode());
        } catch (Exception e) {
            Assert.assertTrue("Exception thrown:" + e, false);
        }
    }

    private final void checkProperties() {
        Assert.assertNotNull(this.workbook);
        Assert.assertNotNull(this.numberOfSelectedSheet);
        Assert.assertNotNull(this.sheetNames);
    }
}
