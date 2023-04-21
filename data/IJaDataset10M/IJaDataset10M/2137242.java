package com.creawor.hz_market.t_advertisement;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aos.util.UploadFileOne;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import com.creawor.imei.base.BaseAction;

public class UploadZYFileAction extends BaseAction {

    public String doAdd(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadFileForm vo = (UploadFileForm) form;
        FormFile file = vo.getFile();
        String yeah = request.getParameter("year");
        String month = request.getParameter("month");
        if (null != month) {
            if (month.length() < 2) month = "0" + month;
        }
        String date = yeah + "-" + month;
        String rStr = null;
        String xmltype2 = vo.getDoctype2();
        if (file != null && "zy".equals(xmltype2)) {
            String rootFilePath = getServlet().getServletContext().getRealPath("/");
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.ZY_path).toString();
            date += "Rice";
            if (file.getFileSize() != 0) {
                rStr = UploadFileOne.fileUploadOne(file, rootFilePath, date + ".xls");
                request.setAttribute("imageUploadErrors", rStr);
                if (rStr != null) {
                } else {
                }
            }
        }
        if (file != null && "kpi".equals(xmltype2)) {
            String rootFilePath = getServlet().getServletContext().getRealPath("/");
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.ZY_path).toString();
            date += "KPI";
            if (file.getFileSize() != 0) {
                rStr = UploadFileOne.fileUploadOne(file, rootFilePath, "KPI.xls");
                request.setAttribute("imageUploadErrors", rStr);
                if (rStr != null) {
                } else {
                }
            }
        }
        if (file != null && "kehu".equals(xmltype2)) {
            String rootFilePath = getServlet().getServletContext().getRealPath("/");
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.ZY_path).toString();
            date += "Kehu";
            if (file.getFileSize() != 0) {
                rStr = UploadFileOne.fileUploadOne(file, rootFilePath, "Kehu.xls");
                request.setAttribute("imageUploadErrors", rStr);
                if (rStr != null) {
                } else {
                }
            }
        }
        if (file != null && "shouru".equals(xmltype2)) {
            String rootFilePath = getServlet().getServletContext().getRealPath("/");
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.ZY_path).toString();
            date += "Shouru";
            if (file.getFileSize() != 0) {
                rStr = UploadFileOne.fileUploadOne(file, rootFilePath, "Shouru.xls");
                request.setAttribute("imageUploadErrors", rStr);
                if (rStr != null) {
                } else {
                }
            }
        }
        if (file != null && "chengben".equals(xmltype2)) {
            String rootFilePath = getServlet().getServletContext().getRealPath("/");
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.ZY_path).toString();
            date += "Chengben";
            if (file.getFileSize() != 0) {
                rStr = UploadFileOne.fileUploadOne(file, rootFilePath, "Chengben.xls");
                request.setAttribute("imageUploadErrors", rStr);
                if (rStr != null) {
                } else {
                }
            }
        }
        if (file != null && "qita".equals(xmltype2)) {
            String rootFilePath = getServlet().getServletContext().getRealPath("/");
            rootFilePath = (new StringBuilder(String.valueOf(rootFilePath))).append(UploadFileOne.ZY_path).toString();
            date += "Qita";
            if (file.getFileSize() != 0) {
                rStr = UploadFileOne.fileUploadOne(file, rootFilePath, date + ".xls");
                request.setAttribute("imageUploadErrors", rStr);
                if (rStr != null) {
                } else {
                }
            }
        }
        if (rStr == null) rStr = "���������ɣ�";
        request.getSession().setAttribute("operating-status", rStr);
        System.out.println("in the end....");
        return "aftersave";
    }
}
