package com.amwebexpert.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import com.amwebexpert.tags.workbook.model.InMemoryWorkbookBin;
import com.amwebexpert.tags.workbook.model.InMemoryWorkbookXlsx;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

public class WorkbookPdfConverterTest extends TestCase {

    public static final String FOLDER = "C:/DE311/solution_workspace/WorkbookTaglib/WorkbookTagDemoWebapp/src/main/resources/workbooks/";

    public void _testConvertSingleXlsxToPDF() throws Exception {
        String xlsFilename = "C:/DE311/solution_workspace/WorkbookTaglib/WorkbookTagDemoWebapp/src/main/resources/workbooks-xlsx/ResourceTimesheets.xlsx";
        WorkbookPdfConverter converter = new WorkbookPdfConverter();
        converter.setWorksheetToDisplay(2);
        File xlsFile = new File(xlsFilename);
        try {
            InMemoryWorkbookXlsx wkb = new InMemoryWorkbookXlsx(xlsFile.getAbsolutePath());
            String filename = "c:/temp/" + StringUtils.remove(xlsFile.getName(), ".xlsx");
            Document document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, new FileOutputStream(new File(filename + ".pdf")));
            document.open();
            converter.convert(document, wkb);
            document.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + ": " + xlsFile.getName());
        }
    }

    public void testConvertSingleToPDF() throws Exception {
        WorkbookPdfConverter converter = new WorkbookPdfConverter();
        converter.setScaleFactor(0.5f);
        converter.setCellsToDisplay("0!B6:L38");
        File xlsFile = new File("C:/DE311/solution_workspace/WorkbookTaglib/WorkbookTagDemoWebapp/src/main/resources/workbooks/demo09_merged_cells.xls");
        try {
            InMemoryWorkbookBin wkb = new InMemoryWorkbookBin(xlsFile.getAbsolutePath());
            wkb.removeEmptyWorksheets();
            String filename = "c:/temp/" + StringUtils.remove(xlsFile.getName(), ".xls");
            Document document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, new FileOutputStream(new File(filename + ".pdf")));
            document.open();
            converter.convert(document, wkb);
            document.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + ": " + xlsFile.getName());
        }
    }

    public void _testConvertToPDF() throws Exception {
        WorkbookPdfConverter converter = new WorkbookPdfConverter();
        converter.setScaleFactor(0.7f);
        File xlsDirectory = new File(FOLDER);
        File[] xlsFiles = xlsDirectory.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".xls");
            }
        });
        for (int i = 0; i < xlsFiles.length; i++) {
            File xlsFile = xlsFiles[i];
            InMemoryWorkbookBin wkb;
            try {
                wkb = new InMemoryWorkbookBin(xlsFile.getAbsolutePath());
                wkb.removeEmptyWorksheets();
                String filename = "c:/temp/" + StringUtils.remove(xlsFile.getName(), ".xls");
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(new File(filename + ".pdf")));
                if (xlsFile.getName().equals("demo02.xls")) {
                    converter.setCellsToDisplay("0!A6:G48");
                } else {
                    converter.removeBoundaries();
                }
                document.open();
                converter.convert(document, wkb);
                document.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ": " + xlsFile.getName());
            }
        }
    }

    public void _testConvertToRTF() throws Exception {
        WorkbookPdfConverter converter = new WorkbookPdfConverter();
        converter.setScaleFactor(0.8f);
        File xlsDirectory = new File(FOLDER);
        File[] xlsFiles = xlsDirectory.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".xls");
            }
        });
        for (int i = 0; i < xlsFiles.length; i++) {
            File xlsFile = xlsFiles[i];
            InMemoryWorkbookBin wkb;
            try {
                wkb = new InMemoryWorkbookBin(xlsFile.getAbsolutePath());
                wkb.removeEmptyWorksheets();
                String filename = "c:/temp/" + StringUtils.remove(xlsFile.getName(), ".xls");
                Document document = new Document();
                RtfWriter2.getInstance(document, new FileOutputStream(new File(filename + ".rtf")));
                if (xlsFile.getName().equals("demo02.xls")) {
                    converter.setCellsToDisplay("0!A6:G48");
                } else {
                    converter.removeBoundaries();
                }
                document.open();
                converter.convert(document, wkb);
                document.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ": " + xlsFile.getName());
            }
        }
    }
}
