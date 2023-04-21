package com.smartwish.documentburster.unit.documentation.userguide.howtodo;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import com.smartwish.documentburster._helpers.ExcelTestUtils;
import com.smartwish.documentburster._helpers.TestBursterFactory;
import com.smartwish.documentburster.engine.AbstractBurster;
import com.smartwish.documentburster.variables.Variables;

public class CustomConfigurationTest {

    private static final String EXCEL_BURST_BY_DISTINCT_COLUMN_VALUES_COMPLEX = "src/test/resources/input/unit/excel/extra/burst-by-distinct-column-values-complex.xls";

    private static final String PDF_CUSTOM_SETTINGS_SHORT = "src/test/resources/input/unit/pdf/custom-settings-short.pdf";

    private static final String PDF_CUSTOM_SETTINGS_LONG = "src/test/resources/input/unit/pdf/custom-settings-long.pdf";

    @Test
    public final void pdfCustomSettingsShort() throws Exception {
        pdfCustomSettings(PDF_CUSTOM_SETTINGS_SHORT);
    }

    @Test
    public final void pdfCustomSettingsLong() throws Exception {
        pdfCustomSettings(PDF_CUSTOM_SETTINGS_LONG);
    }

    private final void pdfCustomSettings(String filePath) throws Exception {
        AbstractBurster burster = TestBursterFactory.createPdfBurster();
        burster.burst(filePath, false);
        String outputFolder = burster.getCtx().outputFolder + "/";
        assertEquals(2, new File(outputFolder).listFiles().length);
        String path = burster.getCtx().outputFolder + "/John Lehnon-March.pdf";
        File outputReport = new File(path);
        assertTrue(outputReport.exists());
        path = burster.getCtx().outputFolder + "/Paul McCartney-June.pdf";
        outputReport = new File(path);
        assertTrue(outputReport.exists());
    }

    @Test
    public void excelBurstTokensAndVariablesAndCustomSettings() throws Exception {
        AbstractBurster burster = TestBursterFactory.createPoiExcelBurster();
        burster.burst(EXCEL_BURST_BY_DISTINCT_COLUMN_VALUES_COMPLEX, false);
        String outputFolder = burster.getCtx().outputFolder + "/";
        assertEquals(2, new File(outputFolder).listFiles().length);
        for (String token : Arrays.asList("Germany", "USA")) {
            String var0 = burster.getCtx().variables.getUserVariables(token).get("var0").toString();
            String var1 = burster.getCtx().variables.getUserVariables(token).get("var1").toString();
            String fileName = var0 + "-" + var1 + ".xls";
            File outputReport = new File(outputFolder + fileName);
            assertTrue(outputReport.exists());
            InputStream input = new FileInputStream(outputReport);
            Workbook workBook = WorkbookFactory.create(input);
            assertEquals(1, workBook.getNumberOfSheets());
            assertNotNull(workBook.getSheet("Customer List"));
            assertTrue(ExcelTestUtils.isWorkbookDataValid(workBook, token));
            input.close();
        }
        String skip = burster.getCtx().variables.getUserVariables("Germany").get(Variables.SKIP).toString();
        assertFalse(Boolean.valueOf(skip).booleanValue());
        skip = burster.getCtx().variables.getUserVariables("USA").get(Variables.SKIP).toString();
        assertTrue(Boolean.valueOf(skip).booleanValue());
    }
}

;
