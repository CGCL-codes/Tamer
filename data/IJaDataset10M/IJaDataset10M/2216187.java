package leeon.forpeddy.excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import leeon.forpeddy.htmlparse.GradeHtmlParser;

public class GradeExcelWrapper {

    private WritableWorkbook workbook;

    private WritableSheet sheet;

    public GradeExcelWrapper(File excel) throws WriteException, BiffException, IOException {
        workbook = Workbook.createWorkbook(excel, Workbook.getWorkbook(excel));
        sheet = workbook.getSheet(0);
    }

    public void save() throws IOException, WriteException {
        workbook.write();
        workbook.close();
    }

    public List<String> readStudentNo() {
        Cell[] cells = sheet.getColumn(0);
        List<String> list = new ArrayList<String>();
        for (Cell cell : cells) {
            list.add(cell.getContents());
        }
        return list;
    }

    public void writeStudentInfo(GradeHtmlParser ghp, int row) throws RowsExceededException, WriteException {
        int i = 1;
        sheet.addCell(new Label(i++, row, ghp.getName()));
        sheet.addCell(new Label(i++, row, ghp.getMajor()));
        sheet.addCell(new Label(i++, row, ghp.getInTime()));
        sheet.addCell(new Label(i++, row, ghp.getFinishTime()));
        sheet.addCell(new Label(i++, row, ghp.getScore()));
        sheet.addCell(new Label(i++, row, ghp.getOver70()));
        sheet.addCell(new Label(i++, row, ghp.getEnglish()));
        sheet.addCell(new Label(i++, row, ghp.getCredit()));
        sheet.addCell(new Label(i++, row, ghp.getArticle()));
        sheet.addCell(new Label(i++, row, ghp.getDgreeTest1()));
        sheet.addCell(new Label(i++, row, ghp.getDgreeTest2()));
        sheet.addCell(new Label(i++, row, ghp.getCollageComputer()));
        sheet.addCell(new Label(i++, row, ghp.getCollageEnglish()));
        sheet.addCell(new Label(i++, row, ghp.getCheckResult1()));
        sheet.addCell(new Label(i++, row, ghp.getCheckResult2()));
    }

    public void writeStudentNotExist(int row) throws RowsExceededException, WriteException {
        sheet.addCell(new Label(1, row, "该学生的资料不存在"));
    }
}
