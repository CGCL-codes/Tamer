package com.entelience.servlet.risk.excel;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.util.Excel;
import com.entelience.servlet.ExcelServlet;
import com.entelience.soap.soapRiskRegister;
import com.entelience.objects.risk.Event;

/**
 * Provides a set of methods to manipulate Excel spreadsheets.
 *
 */
public final class Events extends ExcelServlet {

    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        addHeader(response, "Events.xls");
        ServletOutputStream out = response.getOutputStream();
        Excel x = getExcelObject(peopleId);
        x.setOutputStream(out);
        _logger.info("Start building Excel document ");
        String columnSort = request.getParameter("order");
        String columnWay = request.getParameter("way");
        buildDocument(x, peopleId, columnSort, columnWay);
    }

    /**
     * buildDocument
     */
    public static void buildDocument(Excel x, Integer peopleId, String columnSort, String columnWay) throws Exception {
        x.newWorkBook();
        x.newSheet("Events");
        Event events[] = null;
        events = getListEvents(peopleId, columnSort, columnWay);
        if (null == events || events.length == 0) {
            _logger.warn("No event to process...");
            return;
        }
        _logger.info("Processing events[" + events.length + "]");
        x.newRow();
        x.newTitleCell("Reference", 20);
        x.newTitleCell("Title", 20);
        x.newTitleCell("Description", 30);
        x.newTitleCell("Category", 20);
        x.newTitleCell("Period (years)", 20);
        x.newTitleCell("Likelihood", 15);
        x.newTitleCell("Probability", 10);
        x.newTitleCell("Creation date", 10);
        x.newTitleCell("Last modification date", 10);
        for (int i = 0; i < events.length; ++i) {
            Event e = events[i];
            x.newRow();
            x.newCell(e.getReference());
            x.newCell(e.getTitle());
            x.newCell(e.getDescription());
            x.newCell(e.getCategory());
            x.newNumCell(e.getDefaultPeriod());
            x.newCell(e.getDefaultLikelihood());
            x.newNumCell(e.getDefaultProbability());
            if (null == e.getCreationDate()) {
                x.newCell("");
            } else {
                x.newDateCell(e.getCreationDate());
            }
            if (null == e.getLastModificationDate()) {
                x.newCell("");
            } else {
                x.newDateCell(e.getLastModificationDate());
            }
        }
        x.writeWorkBook();
    }

    /**
     * getListEvents
     */
    private static Event[] getListEvents(Integer peopleId, String columnSort, String columnWay) throws Exception {
        try {
            soapRiskRegister ra = new soapRiskRegister(peopleId);
            return ra.listEvents(columnSort, columnWay, null);
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }
}
