package com.entelience.servlet.risk.excel;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.util.Excel;
import com.entelience.servlet.ExcelServlet;
import com.entelience.soap.soapRiskAssessment;
import com.entelience.objects.risk.Impact;
import com.entelience.objects.risk.RiskControl;
import com.entelience.objects.risk.RiskReviewId;
import com.entelience.objects.risk.RiskId;

/**
 * Provides a set of methods to manipulate Excel spreadsheets.
 *
 */
public final class RiskImpactsRiskControls extends ExcelServlet {

    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        addHeader(response, "RiskImpactsRiskControls.xls");
        ServletOutputStream out = response.getOutputStream();
        Excel x = getExcelObject(peopleId);
        x.setOutputStream(out);
        _logger.info("Start building Excel document ");
        RiskId riskId = null;
        try {
            riskId = new RiskId(getParamInteger(request, "riskId").intValue(), 0);
        } catch (Exception e) {
            riskId = null;
        }
        RiskReviewId reviewId = null;
        try {
            reviewId = new RiskReviewId(getParamInteger(request, "reviewId").intValue(), 0);
        } catch (Exception e) {
            reviewId = null;
        }
        String columnSortImpacts = request.getParameter("order");
        String columnWayImpacts = request.getParameter("way");
        Boolean showDeletedImpacts = getParamBoolean(request, "showDeletedImpacts");
        String columnSortRiskControls = request.getParameter("orderRiskControls");
        String columnWayRiskControls = request.getParameter("wayRiskControls");
        Boolean showDeletedRiskControls = getParamBoolean(request, "showDeletedRiskControls");
        Boolean my = getParamBoolean(request, "my");
        buildDocument(x, peopleId, my, riskId, reviewId, showDeletedImpacts, columnSortImpacts, columnWayImpacts, showDeletedRiskControls, columnSortRiskControls, columnWayRiskControls);
    }

    /**
     * buildDocument
     */
    public static void buildDocument(Excel x, Integer peopleId, Boolean my, RiskId riskId, RiskReviewId reviewId, Boolean showDeletedImpacts, String columnSortImpacts, String columnWayImpacts, Boolean showDeletedRiskControls, String columnSortRiskControls, String columnWayRiskControls) throws Exception {
        x.newWorkBook();
        x.newSheet("Impacts");
        Impact impacts[] = null;
        impacts = getListImpacts(peopleId, my, riskId, reviewId, showDeletedImpacts, columnSortImpacts, columnWayImpacts);
        if (null == impacts || impacts.length == 0) {
            _logger.warn("No impact to process...");
        } else {
            _logger.info("Processing impacts[" + impacts.length + "]");
            x.newRow();
            x.newTitleCell("Title", 20);
            x.newTitleCell("Description", 30);
            x.newTitleCell("Benefic", 10);
            x.newTitleCell("Consequence level", 15);
            x.newTitleCell("Deleted", 10);
            x.newTitleCell("Creation date", 15);
            for (int i = 0; i < impacts.length; ++i) {
                Impact im = impacts[i];
                x.newRow();
                x.newCell(im.getTitle());
                x.newCell(im.getDescription());
                if (im.getBenefic()) {
                    x.newCell("Yes");
                } else {
                    x.newCell("No");
                }
                x.newCell(im.getConsequenceLevel());
                if (im.isDeleted()) {
                    x.newCell("Yes");
                } else {
                    x.newCell("No");
                }
                if (null == im.getCreationDate()) {
                    x.newCell("");
                } else {
                    x.newDateCell(im.getCreationDate());
                }
            }
        }
        x.newSheet("Risk Controls");
        RiskControl riskcontrols[] = null;
        riskcontrols = getListRiskControls(peopleId, my, riskId, reviewId, showDeletedRiskControls, columnSortRiskControls, columnWayRiskControls);
        if (null == riskcontrols || riskcontrols.length == 0) {
            _logger.warn("No risk control to process...");
        } else {
            _logger.info("Processing riskcontrols[" + riskcontrols.length + "]");
            x.newRow();
            x.newTitleCell("Resource requirement", 30);
            x.newTitleCell("Description", 30);
            x.newTitleCell("Status", 15);
            x.newTitleCell("Responsible", 20);
            x.newTitleCell("Deleted", 10);
            x.newTitleCell("Creation date", 15);
            for (int j = 0; j < riskcontrols.length; ++j) {
                RiskControl rc = riskcontrols[j];
                x.newRow();
                x.newCell(rc.getResourceRequirement());
                x.newCell(rc.getDescription());
                x.newCell(rc.getResponsible());
                if (rc.isDeleted()) {
                    x.newCell("Yes");
                } else {
                    x.newCell("No");
                }
                if (null == rc.getCreationDate()) {
                    x.newCell("");
                } else {
                    x.newDateCell(rc.getCreationDate());
                }
            }
        }
        x.writeWorkBook();
    }

    /**
     * getListImpacts
     */
    private static Impact[] getListImpacts(Integer peopleId, Boolean my, RiskId riskId, RiskReviewId reviewId, Boolean showDeletedImpacts, String columnSortImpacts, String columnWayImpacts) throws Exception {
        try {
            soapRiskAssessment ra = new soapRiskAssessment(peopleId);
            return ra.listImpacts(my, riskId, reviewId, showDeletedImpacts, columnSortImpacts, columnWayImpacts, null);
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }

    /**
     * getListRiskControls
     */
    private static RiskControl[] getListRiskControls(Integer peopleId, Boolean my, RiskId riskId, RiskReviewId reviewId, Boolean showDeletedRiskControls, String columnSortRiskControls, String columnWayRiskControls) throws Exception {
        try {
            soapRiskAssessment ra = new soapRiskAssessment(peopleId);
            return ra.listRiskControls(my, riskId, reviewId, showDeletedRiskControls, columnSortRiskControls, columnWayRiskControls, null);
        } catch (Exception e) {
            throw new Exception("Problem while calling webservices method", e);
        }
    }
}
