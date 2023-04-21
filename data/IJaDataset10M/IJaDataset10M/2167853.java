package org.openconcerto.erp.generationDoc;

import org.openconcerto.erp.core.common.element.StyleSQLElement;
import org.openconcerto.openoffice.spreadsheet.Sheet;
import org.openconcerto.openoffice.spreadsheet.SpreadSheet;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SpreadSheetGeneratorCompta extends SpreadSheetGenerator {

    public SpreadSheetGeneratorCompta(SheetInterface sheet, String destFileName, boolean impr, boolean visu) {
        this(sheet, destFileName, impr, visu, true);
    }

    public SpreadSheetGeneratorCompta(SheetInterface sheet, String destFileName, boolean impr, boolean visu, boolean exportPDF) {
        super(sheet, destFileName, impr, visu, exportPDF);
        new Thread(this).start();
    }

    protected File generateWithStyle() throws IOException {
        final SpreadSheet ssheet = loadTemplate();
        if (ssheet == null) {
            return null;
        }
        final Map<String, Map<Integer, String>> mapStyleDef = StyleSQLElement.getMapAllStyle();
        System.err.println("GET first sheet");
        final Sheet sheet = ssheet.getSheet(0);
        System.err.println("get sheet 0, print ranges --> " + sheet.getPrintRanges());
        String s = (sheet.getPrintRanges() == null) ? "" : sheet.getPrintRanges().toString();
        String[] range = s.split(":");
        for (int i = 0; i < range.length; i++) {
            String string = range[i];
            range[i] = string.subSequence(string.indexOf('.') + 1, string.length()).toString();
        }
        int colEnd = -1;
        int rowEnd = -1;
        if (range.length > 1) {
            final Point resolveHint = sheet.resolveHint(range[1]);
            colEnd = resolveHint.x;
            rowEnd = resolveHint.y;
        }
        searchStyle(sheet, mapStyleDef, colEnd, rowEnd);
        System.err.println("Duplicate page Nombre de page : " + this.nbPage + "  nombre de rows par page " + this.nbRowsPerPage);
        if (colEnd > 0) {
            System.err.println("Set Column Count to :: " + (colEnd + 1));
            sheet.setColumnCount(colEnd + 1);
        }
        sheet.duplicateFirstRows(this.nbRowsPerPage, this.nbPage);
        System.err.println("End Duplicate");
        Object printRangeObj = sheet.getPrintRanges();
        if (printRangeObj != null) {
            String[] range2 = printRangeObj.toString().split(":");
            for (int i = 0; i < range2.length; i++) {
                String string = range2[i];
                range2[i] = string.subSequence(string.indexOf('.') + 1, string.length()).toString();
            }
            int end = -1;
            if (range2.length > 1) {
                end = sheet.resolveHint(range2[1]).y + 1;
                long rowEndNew = end * (this.nbPage + 1);
                String sNew = s.replaceAll(String.valueOf(end), String.valueOf(rowEndNew));
                sheet.setPrintRanges(sNew);
                System.err.println(" ******  Replace print ranges; Old:" + end + "--" + s + " New:" + rowEndNew + "--" + sNew);
            }
        } else {
            sheet.removePrintRanges();
        }
        fill(sheet, mapStyleDef);
        System.err.println("SAVE");
        return save(ssheet);
    }
}
