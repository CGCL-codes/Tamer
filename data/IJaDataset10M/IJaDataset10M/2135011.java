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
 * AdminUsers class will export a report of all users (as a report JasperReport).
 */
public class AdminUsers extends ReportServlet {

    protected void buildOutput(Db db, HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        String filter = getParam(request, "filter");
        ReportFiller rep = getReportFillerByName(db, "AdminUsers");
        rep.setDb(db);
        rep.setParameter("paramValue", "-1,year");
        rep.setParameter("peopleId", peopleId);
        rep.setParameter("filter", filter);
        addHeader(response, "users.pdf");
        JasperPrint jasperPrint = rep.fillReport();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
    }
}
