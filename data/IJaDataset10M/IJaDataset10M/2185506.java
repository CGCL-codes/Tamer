package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.DAOProduct;
import model.myutil.myCommon;
import model.myutil.myConnector;
import model.pojo.Product;
import model.pojo.ProductDescription;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 *
 * @author Tidus Le
 */
@WebServlet(name = "bangBaoGiaCtrl", urlPatterns = { "/bangBaoGiaCtrl" })
public class bangBaoGiaCtrl extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            myConnector.open_conect();
            ArrayList arr = new ArrayList();
            arr = DAOProduct.getBangBaoGia();
            myConnector.close_conect();
            HSSFWorkbook wb = new HSSFWorkbook();
            FileOutputStream fileOut = new FileOutputStream(getServletContext().getRealPath("BangBaoGia.xls"));
            HSSFSheet sheet = wb.createSheet("Shoes Online");
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 3500);
            sheet.setColumnWidth(2, 15000);
            sheet.setColumnWidth(3, 5000);
            sheet.setColumnWidth(4, 20000);
            sheet.setColumnWidth(5, 4000);
            HSSFRow row = sheet.createRow(0);
            HSSFCellStyle style = wb.createCellStyle();
            HSSFCellStyle style_title = wb.createCellStyle();
            HSSFCellStyle style_left = wb.createCellStyle();
            HSSFCellStyle style_center = wb.createCellStyle();
            HSSFCellStyle style_right = wb.createCellStyle();
            Font headerFont = wb.createFont();
            HSSFCell cell;
            headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            style_title.setFont(headerFont);
            cell = row.createCell(1);
            cell.setCellValue("Nhóm:");
            cell = row.createCell(2);
            cell.setCellStyle(style_title);
            cell.setCellValue("TaTa Group");
            row = sheet.createRow(1);
            cell = row.createCell(1);
            cell.setCellValue("Thành Viên:");
            cell = row.createCell(2);
            cell.setCellStyle(style_title);
            cell.setCellValue("Lê Minh Tiến - 1041435");
            row = sheet.createRow(2);
            cell = row.createCell(2);
            cell.setCellStyle(style_title);
            cell.setCellValue("Trần Lê Thanh - 1041414");
            row = sheet.createRow(5);
            style.setBorderRight(CellStyle.BORDER_THIN);
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderBottom(CellStyle.BORDER_THIN);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(CellStyle.BORDER_THIN);
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderTop(CellStyle.BORDER_THIN);
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setFont(headerFont);
            cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue("STT");
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue("Mã Sản Phẩm");
            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue("Tên Sản Phẩm");
            cell = row.createCell(3);
            cell.setCellStyle(style);
            cell.setCellValue("Danh Mục");
            cell = row.createCell(4);
            cell.setCellStyle(style);
            cell.setCellValue("Mô Tả");
            cell = row.createCell(5);
            cell.setCellStyle(style);
            cell.setCellValue("Giá Bán (VNĐ)");
            style_left.setBorderRight(CellStyle.BORDER_THIN);
            style_left.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style_left.setBorderBottom(CellStyle.BORDER_THIN);
            style_left.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style_left.setBorderLeft(CellStyle.BORDER_THIN);
            style_left.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style_left.setBorderTop(CellStyle.BORDER_THIN);
            style_left.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style_center.setAlignment(CellStyle.ALIGN_CENTER);
            style_center.setBorderRight(CellStyle.BORDER_THIN);
            style_center.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style_center.setBorderBottom(CellStyle.BORDER_THIN);
            style_center.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style_center.setBorderLeft(CellStyle.BORDER_THIN);
            style_center.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style_center.setBorderTop(CellStyle.BORDER_THIN);
            style_center.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style_right.setAlignment(CellStyle.ALIGN_RIGHT);
            style_right.setBorderRight(CellStyle.BORDER_THIN);
            style_right.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style_right.setBorderBottom(CellStyle.BORDER_THIN);
            style_right.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style_right.setBorderLeft(CellStyle.BORDER_THIN);
            style_right.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style_right.setBorderTop(CellStyle.BORDER_THIN);
            style_right.setTopBorderColor(IndexedColors.BLACK.getIndex());
            NumberFormat num = NumberFormat.getCurrencyInstance();
            for (int i = 0; i < arr.size(); i++) {
                Object[] obj = (Object[]) arr.get(i);
                Product pro = (Product) obj[0];
                ProductDescription prodes = (ProductDescription) obj[1];
                String cate = (String) obj[2];
                row = sheet.createRow(i + 6);
                cell = row.createCell(0);
                cell.setCellStyle(style_center);
                cell.setCellValue(i + 1);
                cell = row.createCell(1);
                cell.setCellStyle(style_center);
                cell.setCellValue(pro.getProductId());
                cell = row.createCell(2);
                cell.setCellStyle(style_left);
                cell.setCellValue(prodes.getName());
                cell = row.createCell(3);
                cell.setCellStyle(style_left);
                cell.setCellValue(cate);
                cell = row.createCell(4);
                cell.setCellStyle(style_left);
                cell.setCellValue(myCommon.trimTagHTML(prodes.getDescription()));
                cell = row.createCell(5);
                cell.setCellStyle(style_right);
                cell.setCellValue(num.format(pro.getPrice()).replace('$', ' '));
            }
            wb.write(fileOut);
            fileOut.close();
            response.sendRedirect("BangBaoGia.xls");
        } finally {
            out.close();
        }
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
