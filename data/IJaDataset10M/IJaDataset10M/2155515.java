package com.vlee.servlet.distribution;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.math.*;
import com.vlee.bean.distribution.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.customer.*;
import com.vlee.util.*;
import com.vlee.ejb.customer.*;
import java.io.*;
import java.lang.*;
import com.lowagie.text.*;
import com.lowagie.text.rtf.*;
import com.lowagie.tools.*;

public class DoOrderPrintingMessageCard extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        String formName = req.getParameter("formName");
        System.out.println("formName :" + formName);
        if (formName.equals("printing")) {
            try {
                HttpSession session = req.getSession();
                PrintMessageCardForm pmcf = (PrintMessageCardForm) session.getAttribute("dist-order-printing-message-card-form");
                if (pmcf == null) {
                    Integer userId = (Integer) session.getAttribute("userId");
                    pmcf = new PrintMessageCardForm(userId);
                    session.setAttribute("dist-order-printing-message-card-form", pmcf);
                }
                System.out.println("Inside printing");
                fnExportToRTF(req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        } else if (formName.equals("processing")) {
            HttpSession session = req.getSession();
            PrintMessageCardForm pmcf = (PrintMessageCardForm) session.getAttribute("dist-order-printing-message-card-form");
            if (pmcf == null) {
                Integer userId = (Integer) session.getAttribute("userId");
                pmcf = new PrintMessageCardForm(userId);
                session.setAttribute("dist-order-printing-message-card-form", pmcf);
            }
            try {
                System.out.println("Inside processing");
                fnGetOrder(req, res);
                fnExportToRTF(req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
    }

    private void fnGetOrder(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        String orderPkid = req.getParameter("pkid");
        try {
            Long lOrder = new Long(orderPkid);
            PrintMessageCardForm pmcf = (PrintMessageCardForm) session.getAttribute("dist-order-printing-message-card-form");
            pmcf.setSalesOrder(lOrder);
        } catch (Exception ex) {
        }
    }

    private void fnExportToRTF(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        try {
            PrintMessageCardForm pmcf = (PrintMessageCardForm) session.getAttribute("dist-order-printing-message-card-form");
            if (pmcf == null) {
                System.out.println("pmcf = null");
                Integer userId = (Integer) session.getAttribute("userId");
                pmcf = new PrintMessageCardForm(userId);
                session.setAttribute("dist-order-printing-message-card-form", pmcf);
            }
            SalesOrderIndexObject soObj = pmcf.getSalesOrder();
            res.setContentType("text/rtf");
            res.setHeader("Content-disposition", "filename=messageCard" + soObj.pkid.toString() + ".rtf");
            if (soObj != null) {
                System.out.println("soObj != null");
                String msgCard = soObj.deliveryTo + " " + soObj.deliveryToName + "\n" + soObj.deliveryMsg1 + "\n" + soObj.deliveryFrom + " " + soObj.deliveryFromName;
                BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
                Document document = new Document();
                RtfWriter2.getInstance(document, outputStream);
                document.open();
                Paragraph headerMsg = new Paragraph(soObj.deliveryTo + " " + soObj.deliveryToName);
                Paragraph bodyMsg = new Paragraph(soObj.deliveryMsg1);
                Paragraph footerMsg = new Paragraph(soObj.deliveryFrom + " " + soObj.deliveryFromName);
                Paragraph orderInfo = new Paragraph("ORDER" + " " + soObj.pkid.toString(), new Font(Font.HELVETICA, 8));
                headerMsg.setAlignment(Element.ALIGN_CENTER);
                bodyMsg.setAlignment(Element.ALIGN_CENTER);
                footerMsg.setAlignment(Element.ALIGN_CENTER);
                orderInfo.setAlignment(Element.ALIGN_CENTER);
                document.add(headerMsg);
                document.add(bodyMsg);
                document.add(footerMsg);
                document.add(new Paragraph(""));
                document.add(orderInfo);
                document.close();
                outputStream.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fnExportToText(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        try {
            PrintMessageCardForm pmcf = (PrintMessageCardForm) session.getAttribute("dist-order-printing-message-card-form");
            if (pmcf == null) {
                Integer userId = (Integer) session.getAttribute("userId");
                pmcf = new PrintMessageCardForm(userId);
                session.setAttribute("dist-order-printing-message-card-form", pmcf);
            }
            SalesOrderIndexObject soObj = pmcf.getSalesOrder();
            if (soObj != null) {
                String msgCard = soObj.deliveryTo + " " + soObj.deliveryToName + "\n" + soObj.deliveryMsg1 + "\n" + soObj.deliveryFrom + " " + soObj.deliveryFromName;
                byte[] bMsgCard = msgCard.getBytes();
                InputStream in = new ByteArrayInputStream(bMsgCard);
                res.setContentType("application/pdf");
                BufferedOutputStream out = new BufferedOutputStream(res.getOutputStream());
                byte by[] = new byte[32768];
                int index = in.read(by, 0, 1000);
                while (index != -1) {
                    out.write(by, 0, index);
                    index = in.read(by, 0, 1000);
                }
                out.flush();
            }
        } catch (Exception ex) {
        }
    }
}
