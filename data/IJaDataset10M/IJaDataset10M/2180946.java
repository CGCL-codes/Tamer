package neuron.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Interface to use Excel spreadsheets as a data storage.
 * 
 * @author Risto
 */
public class ExcelDB {

    private String fileName;

    private List<String> headers;

    private List<Map<String, String>> values;

    public ExcelDB(String fileName) throws IOException, BiffException {
        this.fileName = fileName;
        headers = new ArrayList<String>();
        values = new ArrayList<Map<String, String>>();
        File f = new File(fileName);
        if (!f.exists() || !f.canRead()) throw new IOException("Can't open file! " + f);
        Workbook wb = Workbook.getWorkbook(f);
        Sheet sheet = wb.getSheet(0);
        int col = 0;
        while (true) {
            String v = sheet.getCell(col, 0).getContents();
            if (v != null && v.length() != 0) {
                headers.add(v);
            } else {
                break;
            }
            col++;
            if (col == sheet.getColumns()) break;
        }
        int row = 1;
        while (true) {
            col = 0;
            Map<String, String> data = new HashMap<String, String>();
            for (String hdr : headers) {
                data.put(hdr, sheet.getCell(col, row).getContents());
                col++;
            }
            values.add(data);
            row++;
            if (row == sheet.getRows()) break;
            String next = sheet.getCell(0, row).getContents();
            if (next == null || next.length() == 0) break;
        }
        for (String s : headers) System.out.format("%5s\t", s);
        System.out.println("");
        for (Map<String, String> d : values) {
            for (String s : headers) {
                System.out.format("%5s\t", d.get(s));
            }
            System.out.println("");
        }
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

    public static void appendRow(String fileName, String sheetName, List<String> values, List<String> hdr) throws IOException, BiffException, WriteException {
        File f = new File(fileName);
        WritableWorkbook ww = null;
        if (f.exists()) {
            ww = Workbook.createWorkbook(f, Workbook.getWorkbook(f));
        } else {
            ww = Workbook.createWorkbook(f);
        }
        WritableSheet sheet = ww.getSheet(sheetName);
        if (sheet == null) {
            sheet = ww.createSheet(sheetName, ww.getNumberOfSheets());
            int row = 0, col = 0;
            for (String h : hdr) sheet.addCell(new Label(col++, row, h));
        }
        int row = sheet.getRows();
        int col = 0;
        for (String v : values) {
            sheet.addCell(new Label(col, row, v));
            col++;
        }
        ww.write();
        ww.close();
    }

    public List<String> getHeaders() {
        return headers;
    }
}
