package com.ubb.damate.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ubb.damate.jasper.report.ReportType;
import com.ubb.damate.jasper.report.service.GenerateReportService;

public class GenerateReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String generatedFileName = (String) request.getAttribute("fileName");
        ReportType reportType = (ReportType) request.getAttribute("reportType");
        try {
            new GenerateReportService().generate(reportType, response.getOutputStream());
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + generatedFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
