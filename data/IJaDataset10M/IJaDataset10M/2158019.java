package test;

import java.io.IOException;
import java.util.Map;
import bean.Point;
import jxl.Sheet;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import service.ExcelService;

public class writeExcel {

    /**
	 * @param args
	 * @throws IOException
	 * @throws BiffException
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
    public static void main(String[] args) throws BiffException, IOException, RowsExceededException, WriteException {
        int colBegin = 0;
        int colEnd, rowEnd;
        int index = 0;
        int rowBegin = 0;
        String filename = "d:/user/sfhq083/����/���ж��˵�/2011.5�����ж��˵�.xls";
        String target = "E:/���ж��˵�/2011/5.xls";
        ExcelService eService = new ExcelService();
        Sheet rs = eService.getSheet(filename, index);
        rowEnd = rs.getRows();
        colEnd = rs.getColumns();
        Map<Point, String> result = eService.readExcel(rs, rowBegin, rowEnd, colBegin, colEnd);
        eService.writeExcel(result, target, rowBegin, rowEnd, colBegin, colEnd);
    }
}
