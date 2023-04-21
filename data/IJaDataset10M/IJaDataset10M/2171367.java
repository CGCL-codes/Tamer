package com.entelience.servlet.audit.excel;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import com.entelience.util.Excel;
import com.entelience.servlet.ExcelServlet;
import com.entelience.soap.soapAudit;
import com.entelience.objects.audit.Audit;

/**
 * Provides a set of methods to manipulate Excel spreadsheets.
 */
public final class Audits extends ExcelServlet {

    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        Boolean my;
        Integer origin;
        Integer topic;
        boolean noReports;
        my = getParamBoolean(request, "my");
        String statusMatchAsString = getParam(request, "statusMatch");
        String statusNoMatchAsString = getParam(request, "statusNoMatch");
        List<Number> statusMatch = toArray(statusMatchAsString);
        List<Number> statusNoMatch = toArray(statusNoMatchAsString);
        origin = getParamInteger(request, "origin");
        topic = getParamInteger(request, "topic");
        try {
            noReports = getParamBoolean(request, "noReports").booleanValue();
        } catch (Exception e) {
            noReports = false;
        }
        addHeader(response, "Audits.xls");
        Excel x = getExcelObject(peopleId);
        x.setOutputStream(out);
        _logger.info("Start building Excel document ");
        List<Audit> audits = getAudits(peopleId, my, statusMatch, statusNoMatch, origin, topic, noReports);
        buildDocument(x, audits);
    }

    /**
     * buildDocument
     */
    public static void buildDocument(Excel x, List<Audit> audits) throws Exception {
        x.newWorkBook();
        x.newSheet("Audits");
        _logger.info("Processing audits[" + audits.size() + "]");
        x.newRow();
        x.newTitleCell("Reference", 20);
        x.newTitleCell("Responsible", 20);
        x.newTitleCell("Status", 20);
        x.newTitleCell("Confidentiality", 20);
        x.newTitleCell("Origin", 20);
        x.newTitleCell("Topic", 20);
        x.newTitleCell("Standard", 20);
        x.newTitleCell("Creation", 20);
        x.newTitleCell("Start date", 20);
        x.newTitleCell("End date", 20);
        Iterator<Audit> it = audits.iterator();
        while (it.hasNext()) {
            Audit a = it.next();
            x.newRow();
            x.newCell(a.getReference());
            x.newCell(a.getOwner());
            x.newCell(a.getStatus());
            x.newCell(a.getConfidentiality());
            x.newCell(a.getOrigin());
            x.newCell(a.getTopic());
            x.newCell(a.getStandardName());
            x.newDateCell(a.getCreationDate());
            x.newDateCell(a.getStartDate());
            x.newDateCell(a.getEndDate());
        }
        x.writeWorkBook();
    }

    /**
     * getListReports
     */
    private static List<Audit> getAudits(Integer peopleId, Boolean my, List<Number> statusMatch, List<Number> statusNoMatch, Integer origin, Integer topic, boolean noReports) throws Exception {
        try {
            soapAudit au = new soapAudit(peopleId);
            Audit[] ret = au.getAudits(my, statusMatch, statusNoMatch, origin, topic, noReports, null, null, null);
            List<Audit> audits = new ArrayList<Audit>();
            audits.addAll(Arrays.asList(ret));
            return audits;
        } catch (Exception e) {
            throw new Exception("Problem while calling webservice method : " + e.getMessage(), e);
        }
    }
}
