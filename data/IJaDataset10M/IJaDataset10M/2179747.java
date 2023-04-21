package com.entelience.servlet.admin;

import com.entelience.servlet.ReportServlet;
import com.entelience.export.jasper.ReportFiller;
import com.entelience.sql.Db;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AdminStandards class will export a report of all standards (as a report JasperReport).
 */
public class AdminStandards extends ReportServlet {

    protected void buildOutput(Db db, HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        Integer sourceId = getParamInteger(request, "sourceId");
        String filter = getParam(request, "filter");
        ReportFiller rep = getReportFillerByName(db, "AdminStandards");
        rep.setDb(db);
        rep.setParameter("paramValue", "-1,year");
        rep.setParameter("sourceId", sourceId);
        rep.setParameter("filter", filter);
        rep.setParameter("peopleId", peopleId);
        addHeader(response, "standards.pdf");
        JasperPrint jasperPrint = rep.fillReport();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
    }
}
