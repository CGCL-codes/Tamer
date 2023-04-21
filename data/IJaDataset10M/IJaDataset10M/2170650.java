package net.sf.webwarp.reports.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

public class XLSTest {

    @Test
    public void testWorkbookCreation() throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        wb.createSheet("empty sheet");
        wb.write(new FileOutputStream(new File("test1.xls")));
        FileUtils.writeByteArrayToFile(new File("test2.xls"), wb.getBytes());
    }
}
