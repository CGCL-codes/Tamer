package com.entelience.servlet.admin;

import java.util.Date;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.util.Excel;
import com.entelience.servlet.ExcelServlet;
import com.entelience.soap.soapRaci;
import com.entelience.objects.TwoDate;
import com.entelience.objects.raci.RaciHistory;

/**
 * Export a raci history 
 *
 */
public final class RaciHistoryExcel extends ExcelServlet {

    private String getRaciChange(Boolean b) {
        if (b == null) return "";
        if (b.booleanValue()) {
            return "+";
        } else {
            return "-";
        }
    }

    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        Excel x = getExcelObject(peopleId);
        x.setOutputStream(out);
        _logger.info("Start building Excel document ");
        int raciId = getParamInt(request, "raciObj");
        Date d1 = getParamDate(request, "firstDate");
        Date d2 = getParamDate(request, "lastDate");
        TwoDate td = new TwoDate();
        td.setFirstDate(d1);
        td.setLastDate(d2);
        addHeader(response, "raci_history.xls");
        buildDocument(x, peopleId, raciId, td);
    }

    /**
     * buildDocument
     */
    public void buildDocument(Excel x, Integer peopleId, int raciId, TwoDate td) throws Exception {
        x.newWorkBook();
        x.newSheet("RACI history");
        RaciHistory[] racis = getRaciHistory(peopleId, raciId, td);
        _logger.info("Processing raci history[" + racis.length + "]");
        x.newRow();
        x.newTitleCell("Change date", 15);
        x.newTitleCell("Modifier", 20);
        x.newTitleCell("Stakeholder", 20);
        x.newTitleCell("R", 5);
        x.newTitleCell("A", 5);
        x.newTitleCell("C", 5);
        x.newTitleCell("I", 5);
        for (int i = 0; i < racis.length; i++) {
            x.newRow();
            x.newDateCell(racis[i].getModificationDate());
            x.newCell(racis[i].getModifierName());
            x.newCell(racis[i].getUserName());
            x.newCell(getRaciChange(racis[i].getR()));
            x.newCell(getRaciChange(racis[i].getA()));
            x.newCell(getRaciChange(racis[i].getC()));
            x.newCell(getRaciChange(racis[i].getI()));
        }
        x.writeWorkBook();
    }

    /**
     * get the list of VPV to show
     */
    private RaciHistory[] getRaciHistory(Integer peopleId, int raciId, TwoDate td) throws Exception {
        try {
            soapRaci sr = new soapRaci(peopleId);
            return sr.getRaciHistory(raciId, td, null);
        } catch (Exception e) {
            throw new Exception("Problem while calling webservice method : " + e.getMessage(), e);
        }
    }
}
