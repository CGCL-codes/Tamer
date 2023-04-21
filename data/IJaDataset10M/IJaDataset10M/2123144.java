package com.alianzamedica.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.objectsearch.web.tools.Converter;
import com.alianzamedica.businessobject.SalesByMedicObject;
import com.alianzamedica.models.ReportModel;

/**
 * 
 * @author Carlos
 * 
 */
public class SalesByMedic extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7038259034595154826L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet hoja1 = excel.createSheet("hoja1");
        Integer doctorId = Converter.string2Integer(request.getParameter("medicId"));
        ReportModel model = new ReportModel();
        OutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=Report.xls");
        try {
            ArrayList<SalesByMedicObject> list = model.getMedicSalesReport(doctorId);
            Iterator<SalesByMedicObject> iterator = list.iterator();
            HSSFRow row0 = hoja1.createRow(0);
            row0.createCell(0).setCellValue("Nombre");
            row0.createCell(1).setCellValue("Cantidad");
            int rowNum = 2;
            while (iterator.hasNext()) {
                SalesByMedicObject salesByMedicObject = (SalesByMedicObject) iterator.next();
                HSSFRow row = hoja1.createRow(rowNum);
                row.createCell(0).setCellValue(object2String(salesByMedicObject.getDrugName()));
                row.createCell(1).setCellValue(object2String(salesByMedicObject.getQuantitySold()));
                rowNum++;
            }
            excel.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    private String object2String(Object object) {
        if (object == null) {
            return "";
        } else {
            return object.toString();
        }
    }
}
