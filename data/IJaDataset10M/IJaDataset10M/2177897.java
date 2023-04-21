package il.co.entrypoint.export;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.io.FileOutputStream;
import java.io.OutputStream;
import il.co.entrypoint.model.*;

/**
 * Converts export from DB to MsExcel xls format.<br>
 * <b>Apache POI HSSF project used .<br>
 * <u>Attention!!! One instance, one report. If you attempt to create several
 * summaries from one instance it is not safe.</u></b>
 *
 * <code>main()</code> with console application uses this converter exist.
 *
 * @author Grinfeld Igor, Entrypoint Ltd. igorg@entrypoint.co.il
  * @version 1.0
 */
public class ExcelSummary extends Summary implements Converter {

    private HSSFWorkbook buf;

    private static final int FIRST_ROW = 2;

    private HSSFCellStyle dateStyle;

    private HSSFCellStyle timeStyle;

    private HSSFCellStyle daysStyle;

    public byte[] createSummary(Date startDate, Date endDate) {
        buf = new HSSFWorkbook();
        dateStyle = buf.createCellStyle();
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        timeStyle = buf.createCellStyle();
        timeStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm"));
        daysStyle = buf.createCellStyle();
        daysStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        super.createSummary(startDate, endDate);
        return buf.getBytes();
    }

    protected void addReports(String titleStr, String columns, Report[] reports) {
        HSSFSheet sheet = buf.createSheet(titleStr);
        HSSFRow title = sheet.createRow(0);
        HSSFRow colTitles = sheet.createRow(1);
        HSSFCell titleCell;
        HSSFCell colTitleCell;
        String[] titles = convertTitles(columns);
        titleCell = title.createCell((short) 0);
        titleCell.setCellValue(new HSSFRichTextString(titleStr));
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (titles.length - 1)));
        for (short colIndex = 0; colIndex < titles.length; colIndex++) {
            colTitleCell = colTitles.createCell(colIndex);
            colTitleCell.setCellValue(new HSSFRichTextString(titles[colIndex]));
        }
        for (int rowIndex = 0; rowIndex < reports.length; rowIndex++) {
            addReport(rowIndex, sheet, reports[rowIndex]);
        }
    }

    /**
     * Add hours report to result document
     * @param sheet excel table from summary where report added
     * @param report hours report added to summary
     * @param rowIndex index index of row  to add report
     */
    protected void addReport(int rowIndex, HSSFSheet sheet, Report report) {
        HSSFRow values;
        values = sheet.createRow(rowIndex + FIRST_ROW);
        if (report instanceof Activity) {
            addHours(values, (Activity) report);
        } else if (report instanceof Task) {
            addTask(values, (Task) report);
        } else if (report instanceof Issue) {
            addIssue(values, (Issue) report);
        }
    }

    protected void addHours(HSSFRow values, Activity report) {
        HSSFCell cell;
        cell = values.createCell((short) Activity.DATE_POS);
        cell.setCellValue(report.getDate());
        cell.setCellStyle(dateStyle);
        cell = values.createCell((short) Activity.TIME_START_POS);
        cell.setCellValue(report.getTimeStart());
        cell.setCellStyle(timeStyle);
        cell = values.createCell((short) Activity.TIME_END_POS);
        cell.setCellValue(report.getTimeStart());
        cell.setCellStyle(timeStyle);
        BusinessUnit bu = report.getBusinessUnit();
        String val = bu != null ? bu.getFullName() : "unknown";
        cell = values.createCell((short) Activity.BUSINESS_UNIT_POS);
        cell.setCellValue(new HSSFRichTextString(val));
        cell = values.createCell((short) Activity.TYPE_POS);
        cell.setCellValue(new HSSFRichTextString(report.getType()));
        cell = values.createCell((short) Activity.DESCR_POS);
        cell.setCellValue(new HSSFRichTextString(report.getDescription()));
        Employe user = report.getEmploye();
        cell = values.createCell((short) Activity.EMPLOYE_POS);
        val = user != null ? user.getUsername() : "unknown";
        cell.setCellValue(new HSSFRichTextString(val));
        cell = values.createCell((short) Activity.ID_POS);
        cell.setCellValue(report.getId());
    }

    /**
     * Add task report to result document
     * @param report task report to add
     * @param values  row of table to add report
     */
    protected void addTask(HSSFRow values, Task report) {
        HSSFCell cell;
        cell = values.createCell((short) Task.DATE_POS);
        cell.setCellValue(report.getDate());
        cell.setCellStyle(dateStyle);
        cell = values.createCell((short) Task.DESCR_POS);
        cell.setCellValue(new HSSFRichTextString(report.getDescription()));
        Employe user = report.getEmploye();
        cell = values.createCell((short) Task.EMPLOYE_POS);
        String val = user != null ? user.getUsername() : "unknown";
        cell.setCellValue(new HSSFRichTextString(val));
        float days = report.getDayDuration();
        if (report.isPartOfDay()) {
            days += .5;
        }
        cell = values.createCell((short) Task.DAYS_POS);
        cell.setCellValue(days);
        cell.setCellStyle(daysStyle);
        cell = values.createCell((short) Task.ID_POS);
        cell.setCellValue(report.getId());
    }

    /**
     * Add issue report to result document
     * @param report issue report to add
     * @param values  row of table to add report
     */
    protected void addIssue(HSSFRow values, Issue report) {
        HSSFCell cell;
        cell = values.createCell((short) Issue.DATE_POS);
        cell.setCellValue(report.getDate());
        cell.setCellStyle(dateStyle);
        cell = values.createCell((short) Issue.DESCR_POS);
        cell.setCellValue(new HSSFRichTextString(report.getDescription()));
        Employe user = report.getEmploye();
        cell = values.createCell((short) Issue.EMPLOYE_POS);
        String val = user != null ? user.getUsername() : "unknown";
        cell.setCellValue(new HSSFRichTextString(val));
        if (report instanceof TechIssue) {
            val = "TechIssue";
        } else {
            val = "AdministrativeIssue";
        }
        cell = values.createCell((short) Issue.TYPE_POS);
        cell.setCellValue(new HSSFRichTextString(val));
        cell = values.createCell((short) Issue.ID_POS);
        cell.setCellValue(report.getId());
    }

    /**
     * You can print rtf or pdf summary into file
     * @param args console arguments
     **/
    public static void main(String[] args) {
        Converter summary = new ExcelSummary();
        DateFormat format = new SimpleDateFormat("dd/MM/yy");
        try {
            byte[] content = summary.createSummary(format.parse(args[0]), format.parse(args[1]));
            OutputStream out = new FileOutputStream("reports.xls");
            out.write(content);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Usage: ExcelSummary <startDate> <endDate>\n" + " File with name reports.xls will be created.");
        }
    }
}
