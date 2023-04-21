package com.entelience.servlet.admin;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.util.Excel;
import com.entelience.servlet.ExcelServlet;
import com.entelience.soap.soapPreferences;
import com.entelience.objects.directory.Expertise;

/**
 * Export XPs as an excel spreadsheat
 *
 */
public final class ExpertisesExcel extends ExcelServlet {

    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        Excel x = getExcelObject(peopleId);
        x.setOutputStream(out);
        _logger.info("Start building Excel document ");
        addHeader(response, "expertises.xls");
        buildDocument(x, peopleId);
    }

    /**
     * buildDocument
     */
    public void buildDocument(Excel x, Integer peopleId) throws Exception {
        x.newWorkBook();
        x.newSheet("Expertises");
        Expertise[] xps = getMyExpertises(peopleId);
        _logger.info("Processing expertises[" + xps.length + "]");
        x.newRow();
        x.newTitleCell("User name", 25);
        x.newTitleCell("Vendor", 25);
        x.newTitleCell("Product", 25);
        x.newTitleCell("Inherited from group", 25);
        _logger.info("Processing expertises");
        for (int i = 0; i < xps.length; i++) {
            x.newRow();
            x.newCell(xps[i].getUser_name());
            x.newCell(xps[i].getVendor_name());
            x.newCell(xps[i].getProduct_name());
            x.newCell(xps[i].getGroup_name());
        }
        x.writeWorkBook();
    }

    /**
     * get the list of groups to show
     */
    private Expertise[] getMyExpertises(Integer peopleId) throws Exception {
        try {
            return new soapPreferences(peopleId).getMyExpertises(true, true);
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }
}
