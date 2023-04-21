package com.giews.report.servlets;

import com.giews.report.utils.*;

public class ReportManager extends javax.servlet.http.HttpServlet {

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        ActionManager manager = new ActionManager(request, response);
        String action = request.getParameter(Constants.ACTION);
        System.out.println("action = " + action);
        if ("save".equals(action)) {
            manager.prepareSaveAction(false);
        } else if ("saveTemplate".equals(action)) {
            manager.prepareSaveAction(true);
        } else if ("readReport".equals(action) || "viewReport".equals(action) || "readTemplate".equals(action)) {
            manager.prepareReadAction(action);
        } else if ("create".equals(action) || "openReport".equals(action) || "openTemplate".equals(action)) {
            manager.prepareOpenAction(action);
        } else if (action != null && action.indexOf("next") > -1) {
            manager.prepareNextAction();
        } else if ("exportPDF".equals(action)) {
            manager.prepareTemplateToPDF();
        } else if ("exportReportPDF".equals(action) || "viewReports".equals(action)) {
            manager.prepareReportToPDF();
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        System.out.println("-- get --");
        doPost(request, response);
    }
}
