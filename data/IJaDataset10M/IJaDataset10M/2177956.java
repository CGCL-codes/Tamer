package com.entelience.servlet.vrt.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.entelience.objects.DropDown;
import com.entelience.objects.vrt.VulnerabilityAction;
import com.entelience.objects.vrt.VulnerabilityInformation;
import com.entelience.objects.vuln.VulnId;
import com.entelience.servlet.ExcelServlet;
import com.entelience.soap.soapVulnerabilityReview;
import com.entelience.util.Excel;

/**
 * Servlet that generates an excel spreadsheet for vulnerabilities in Action Plan
 *
 * HTTP Parameters :

 * + vuln_id 
 * + show_hidden 
 * + my    
 *
 */
public final class Actions extends ExcelServlet {

    private DropDown[] status;

    private DropDown[] priorities;

    private VulnerabilityInformation vuln;

    private String getPriority(Integer id) throws Exception {
        if (id == null || priorities == null) return "";
        String name = "";
        for (int i = 0; i < priorities.length; i++) {
            if (priorities[i].getData() == id.intValue()) {
                name = priorities[i].getLabel();
                break;
            }
        }
        return name;
    }

    private String getStatus(Integer id) throws Exception {
        if (id == null || status == null) return "";
        String name = "";
        for (int i = 0; i < status.length; i++) {
            if (status[i].getData() == id.intValue()) {
                name = status[i].getLabel();
                break;
            }
        }
        return name;
    }

    public void buildOutput(HttpServletRequest request, HttpServletResponse response, Integer peopleId) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        Boolean my = null;
        Boolean show_hidden = null;
        VulnId vulnId = null;
        my = getParamBoolean(request, "my");
        show_hidden = getParamBoolean(request, "show_hidden");
        try {
            vulnId = new VulnId(getParamInteger(request, "vuln_id").intValue(), 0);
        } catch (Exception e) {
            vulnId = new VulnId(-1, 0);
        }
        Excel x = getExcelObject(peopleId);
        x.setOutputStream(out);
        _logger.info("Start building Excel document ");
        List<VulnerabilityAction> actions = getActionsForVulnerability(vulnId, show_hidden, my, peopleId);
        addHeader(response, vuln.getVuln_name() + ".xls");
        buildDocument(x, actions);
    }

    /**
     * buildDocument
     */
    public void buildDocument(Excel x, List<VulnerabilityAction> actions) throws Exception {
        x.newWorkBook();
        x.newSheet("Actions for " + vuln.getVuln_name());
        _logger.info("Processing actions[" + actions.size() + "]");
        x.newRow();
        x.newTitleCell("Description", 50);
        x.newTitleCell("Change Reference", 25);
        x.newTitleCell("Workload", 50);
        x.newTitleCell("Responsible", 20);
        x.newTitleCell("Priority", 15);
        x.newTitleCell("Status", 15);
        x.newTitleCell("Target Date", 15);
        x.newTitleCell("Hidden", 15);
        Iterator<VulnerabilityAction> it = actions.iterator();
        while (it.hasNext()) {
            VulnerabilityAction va = it.next();
            x.newRow();
            x.newTextCell(va.getDescription(), "center");
            x.newTextCell(va.getChangeref(), "center");
            x.newTextCell(va.getWorkload(), "center");
            x.newTextCell(va.getOwnerName(), "center");
            x.newTextCell(getPriority(va.getPriority()), "center");
            x.newTextCell(getStatus(va.getMav_status()), "center");
            x.newDateCell(va.getTarget_date());
            if (va.getHide_action().booleanValue()) x.newCell("Hidden"); else x.newCell("");
        }
        x.writeWorkBook();
    }

    /**
     * get the list of vulnerabilities to show
     */
    private List<VulnerabilityAction> getActionsForVulnerability(VulnId vulnId, Boolean showHidden, Boolean my, Integer peopleId) throws Exception {
        soapVulnerabilityReview vr = new soapVulnerabilityReview(peopleId);
        List<VulnerabilityAction> vulns = new ArrayList<VulnerabilityAction>();
        VulnerabilityAction[] va = getActionsWS(vulnId, showHidden, my, vr);
        priorities = getPrioritiesWS(vr);
        status = getStatusWS(vr);
        vuln = getVulnInfoWS(vr, vulnId);
        vulns.addAll(Arrays.asList(va));
        return vulns;
    }

    private VulnerabilityInformation getVulnInfoWS(soapVulnerabilityReview vr, VulnId vulnId) throws Exception {
        try {
            return vr.getVulnerabilityInformation(vulnId);
        } catch (Exception e) {
            throw new Exception("Problem while calling webservicemethod : " + e.getMessage(), e);
        }
    }

    private DropDown[] getPrioritiesWS(soapVulnerabilityReview vr) throws Exception {
        try {
            return vr.getListOfPriority();
        } catch (Exception e) {
            throw new Exception("Problem while calling webservicemethod : " + e.getMessage(), e);
        }
    }

    private DropDown[] getStatusWS(soapVulnerabilityReview vr) throws Exception {
        try {
            return vr.getListOfMAVStatus();
        } catch (Exception e) {
            throw new Exception("Problem while calling webservicemethod : " + e.getMessage(), e);
        }
    }

    /**
     * call the Webservice for getting the object
     */
    private VulnerabilityAction[] getActionsWS(VulnId vulnId, Boolean showHidden, Boolean my, soapVulnerabilityReview vr) throws Exception {
        try {
            return vr.getVulnerabilityActions(vulnId, showHidden, my);
        } catch (Exception e) {
            throw new Exception("Problem while calling webservice method : " + e.getMessage(), e);
        }
    }
}
