package export;

import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import common.statistics.Series;

public class ExcelWriter {

    WritableWorkbook ww;

    WritableSheet curSheet = null;

    int row = 0, col = 0;

    public ExcelWriter(File excel) throws IOException {
        ww = Workbook.createWorkbook(excel);
    }

    public void goToSheet(String name) {
        curSheet = ww.getSheet(name);
        if (curSheet == null) {
            curSheet = ww.createSheet(name, ww.getNumberOfSheets());
        }
    }

    public void write(int value) throws RowsExceededException, WriteException {
        curSheet.addCell(new Number(col++, row, value));
    }

    public void write(double value) throws RowsExceededException, WriteException {
        curSheet.addCell(new Number(col++, row, value));
    }

    public void write(String value) throws RowsExceededException, WriteException {
        curSheet.addCell(new Label(col++, row, value));
    }

    public void write(String[] valueList) throws RowsExceededException, WriteException {
        for (String value : valueList) {
            curSheet.addCell(new Label(col++, row, value));
        }
    }

    public void write(Series value) throws RowsExceededException, WriteException {
        curSheet.addCell(new Number(col++, row, value.mean()));
        curSheet.addCell(new Number(col++, row, value.stdevp()));
    }

    public void newLine() {
        row++;
        col = 0;
    }

    public void close() throws IOException, WriteException {
        ww.write();
        ww.close();
    }
}
