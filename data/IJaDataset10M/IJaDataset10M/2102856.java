package adm.java.excel;

import java.io.*;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import adm.java.librairie.BaseSchema;

public final class ModXls extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BaseSchema application = (BaseSchema) request.getSession().getAttribute("shema");
        if (application == null) {
            synchronized (application.con) {
                application.setServletOk(request, response, this.getServletContext(), this);
                response.addHeader("Content-type", "application/vnd.ms-excel;name=\"test.xls\"");
                response.addHeader("Content-Disposition", "attachment;filename=\"test.xls\"");
                try {
                    HSSFWorkbook hssfworkbook = new HSSFWorkbook();
                    HSSFSheet sheet = hssfworkbook.createSheet("new sheet");
                    HSSFCellStyle cs = hssfworkbook.createCellStyle();
                    HSSFDataFormat df = hssfworkbook.createDataFormat();
                    cs.setDataFormat(df.getFormat("#,##0.0"));
                    HSSFRow row = sheet.createRow((short) 0);
                    HSSFCell cell = row.createCell((short) 0);
                    cell.setCellValue(11111.1);
                    cell.setCellStyle(cs);
                    hssfworkbook.write((ServletOutputStream) application.con.getOut());
                    application.con.closeOut();
                } catch (Exception e) {
                    System.out.println("<tr><td>" + "echec pilote : " + "</td></tr>");
                    System.out.println("echec pilote : " + e);
                    e.printStackTrace();
                }
            }
        }
    }
}
