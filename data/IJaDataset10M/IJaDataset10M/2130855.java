package com.vlee.servlet.inventory;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.math.BigDecimal;
import java.io.*;
import com.vlee.servlet.main.*;
import com.vlee.bean.inventory.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.supplier.*;
import com.lowagie.text.*;
import com.lowagie.text.rtf.*;
import com.lowagie.text.pdf.*;
import com.lowagie.tools.*;

public class DoBarcodeToolkitPrintItemcodeSerialnoLabelByGRN extends HttpServlet {

    private String strClassName = "DoBOMCreate";

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        String formName = req.getParameter("formName");
        if (formName.equals("printByGRN")) {
            try {
                fnPrintLabelByGRN(req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
                ex.printStackTrace();
            }
        }
        if (formName.equals("printSingleLabel")) {
            try {
                fnPrintSingleLabel(req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void fnPrintSingleLabel(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "filename=label.pdf");
            Integer numberOfLabel = new Integer(req.getParameter("numberOfLabel"));
            String itemCode = req.getParameter("itemCode");
            String itemEAN = req.getParameter("itemEAN");
            String itemSerial = req.getParameter("itemSerial");
            BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setFixedHeight(80);
            String labelString = "ITEMCODE:" + itemCode;
            labelString += "\nEAN/UPC:" + itemEAN;
            if (itemSerial != null && itemSerial.length() > 3) {
                labelString += "\nSN#:" + itemSerial;
            }
            for (int cnt1 = 0; cnt1 < numberOfLabel.intValue(); cnt1++) {
                try {
                    PdfPCell theCell = new PdfPCell();
                    table.addCell(new Phrase(labelString));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            document.add(table);
            document.close();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fnPrintLabelByGRN(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "filename=label.pdf");
            String strGRN = req.getParameter("grnId");
            GoodsReceivedNoteObject grnObj = GoodsReceivedNoteNut.getObject(new Long(strGRN));
            BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.getDefaultCell().setFixedHeight(80);
            for (int cnt1 = 0; cnt1 < grnObj.vecGRNItems.size(); cnt1++) {
                try {
                    GoodsReceivedNoteItemObject grnItmObj = (GoodsReceivedNoteItemObject) grnObj.vecGRNItems.get(cnt1);
                    ItemObject itemObj = ItemNut.getObject(grnItmObj.mItemId);
                    String labelString = "ITEMCODE:" + itemObj.code;
                    labelString += "\nEAN/UPC:" + itemObj.eanCode;
                    PdfPCell theCell = new PdfPCell();
                    if (itemObj.serialized == false) {
                        Integer nQty = new Integer(CurrencyFormat.strInt(grnItmObj.mTotalQty));
                        for (int cnt2 = 0; cnt2 < nQty.intValue(); cnt2++) ;
                        {
                            table.addCell(new Phrase(labelString));
                        }
                    } else {
                        QueryObject query = new QueryObject(new String[] { SerialNumberDeltaBean.DOC_KEY + " = '" + grnItmObj.mPkid.toString() + "' ", SerialNumberDeltaBean.DOC_TABLE + " = '" + GoodsReceivedNoteItemBean.TABLENAME + "' " });
                        query.setOrder(" ORDER BY " + SerialNumberDeltaBean.SERIAL + " ");
                        Vector vecSN = new Vector(SerialNumberDeltaNut.getObjects(query));
                        for (int cnt5 = 0; cnt5 < vecSN.size(); cnt5++) {
                            SerialNumberDeltaObject sndObj = (SerialNumberDeltaObject) vecSN.get(cnt5);
                            String buffer = labelString + "\nS/N#:" + sndObj.serialNumber;
                            table.addCell(new Phrase(buffer));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            document.add(table);
            document.close();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
