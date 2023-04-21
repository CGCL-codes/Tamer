package ca.ubc.icapture.genapha.external.actions;

import ca.ubc.icapture.genapha.external.forms.SnpGeneForm;
import icapture.genapha.GenaphaTools;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jxl.read.biff.BiffException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import jxl.*;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.biomoby.services.ncbi.NCBIJobManager;
import org.biomoby.services.ncbi.jobs.EntrezGeneIdBySNPPosFromXML;
import org.biomoby.services.ncbi.jobs.GeneInformationByEntrezGeneIdFromXML;
import org.biomoby.services.ncbi.jobs.SNPPosBySnpFromXML;
import org.biomoby.services.ncbi.jobs.SnpInformationBySnpFromXML;

/**
 *
 * @author ATan1
 */
public class SnpGeneAction extends Action {

    private HashMap<String, HashMap<String, String>> snpGeneList = new HashMap();

    private int offset = 0;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward();
        HttpSession session = request.getSession();
        final SnpGeneForm snpGeneForm = (SnpGeneForm) form;
        snpGeneList.clear();
        offset = snpGeneForm.getStreamOffset() * 1000;
        if (snpGeneForm.getSubmitType().isEmpty()) {
            return mapping.findForward("display");
        }
        if (snpGeneForm.getUseTextarea().equals("text")) {
            ByteArrayInputStream bais = new ByteArrayInputStream(snpGeneForm.getTextInput().getBytes());
            ArrayList<String> inputContents = GenaphaTools.readFile(bais);
            try {
                readCSVFile(inputContents);
            } catch (Exception ex) {
                Logger.getLogger(SnpGeneAction.class.getName()).log(Level.SEVERE, null, ex);
                errors.add("snpGene", new ActionMessage("snpGene.error.errorReadingText"));
                forward = mapping.findForward("error");
                saveErrors(request, errors);
                return forward;
            }
            forward = mapping.findForward("next");
        } else if (snpGeneForm.getFile() != null) {
            try {
                readExcelFile(snpGeneForm.getFile().getInputStream());
            } catch (Exception e) {
                try {
                    ArrayList<String> fileContents = GenaphaTools.readFile(snpGeneForm.getFile().getInputStream());
                    readCSVFile(fileContents);
                } catch (Exception ex) {
                    Logger.getLogger(SnpGeneAction.class.getName()).log(Level.SEVERE, null, ex);
                    errors.add("snpGene", new ActionMessage("geneSnp.error.readingFile"));
                    forward = mapping.findForward("error");
                    saveErrors(request, errors);
                    return forward;
                }
            }
            forward = mapping.findForward("next");
        }
        if (snpGeneList.size() > 0) {
            snpGeneForm.setSnpGeneHash(snpGeneList);
            request.getSession().setAttribute("snpGeneForm", snpGeneForm);
        }
        if (snpGeneForm.getExcel()) {
            forward = mapping.findForward("snpGeneExcel");
        }
        return forward;
    }

    private void readExcelFile(InputStream in) throws IOException, BiffException {
        Workbook wb = Workbook.getWorkbook(in);
        Sheet sheet = wb.getSheet(0);
        ArrayList<String> rsList = new ArrayList<String>();
        Cell[] col = sheet.getColumn(0);
        for (int i = 0; i < col.length; ++i) {
            rsList.add(col[i].getContents().trim());
        }
        try {
            snpCallMan(rsList);
        } catch (Exception e) {
        }
    }

    private void readCSVFile(ArrayList<String> fileContents) throws Exception {
        ArrayList<String> rsList = new ArrayList<String>();
        for (String line : fileContents) {
            rsList.add(line.trim());
        }
        snpCallMan(rsList);
    }

    private void snpCallMan(ArrayList<String> rsList) throws Exception {
        if (offset != 0) {
            getSnpToGeneWOffset(rsList);
        } else {
            getSnpToGene(rsList);
        }
    }

    public void getSnpToGene(ArrayList<String> rsList) throws Exception {
        HashMap<String, String> noGeneFound = new HashMap();
        ArrayList tmpArr;
        SnpInformationBySnpFromXML sNPPosBySnpFromXML = new SnpInformationBySnpFromXML();
        sNPPosBySnpFromXML.setSnpList(rsList);
        NCBIJobManager.runJob(sNPPosBySnpFromXML);
        noGeneFound.put("No Gene Found", "-");
        for (String rs : rsList) {
            String rsNumber = rs.substring(rs.toLowerCase().indexOf("rs") + 2);
            Object[] geneID = sNPPosBySnpFromXML.getSnpGeneMap().get(rsNumber);
            if (geneID == null || ((HashMap<String, String>) geneID[0]).isEmpty()) {
                snpGeneList.put(rs, noGeneFound);
            } else {
                snpGeneList.put(rs, (HashMap<String, String>) geneID[0]);
            }
        }
    }

    private void getSnpToGeneWOffset(ArrayList<String> rsList) {
        HashMap<String, String> noGeneFound = new HashMap();
        HashMap<String, String> geneIDToName = new HashMap();
        SnpInformationBySnpFromXML sNPPosBySnpFromXML = new SnpInformationBySnpFromXML();
        sNPPosBySnpFromXML.setSnpList(rsList);
        NCBIJobManager.runJob(sNPPosBySnpFromXML);
        noGeneFound.put("No Gene Found", "-");
        EntrezGeneIdBySNPPosFromXML egib = new EntrezGeneIdBySNPPosFromXML();
        egib.setSnpInfo(sNPPosBySnpFromXML.getSnpGeneMap());
        egib.setStreamOffset(offset);
        NCBIJobManager.runJob(egib);
        HashMap<String, ArrayList> snpToGeneID = egib.getSnpMap();
        ArrayList<String> tmp = egib.getAllGeneID();
        GeneInformationByEntrezGeneIdFromXML geneInformationByEntrezGeneIdFromXML = new GeneInformationByEntrezGeneIdFromXML();
        geneInformationByEntrezGeneIdFromXML.setGeneIdList(tmp);
        NCBIJobManager.runJob(geneInformationByEntrezGeneIdFromXML);
        for (String id : geneInformationByEntrezGeneIdFromXML.getGeneMap().keySet()) {
            String[] geneInfo = geneInformationByEntrezGeneIdFromXML.getGeneMap().get(id);
            geneIDToName.put(id, geneInfo[0]);
        }
        ArrayList<String> tmpArr;
        for (String rs : rsList) {
            String rsNumber = rs.substring(rs.toLowerCase().indexOf("rs") + 2);
            HashMap<String, String> snpGeneIDName = new HashMap<String, String>();
            tmpArr = snpToGeneID.get(rsNumber);
            if (tmpArr == null || tmpArr.isEmpty()) {
                snpGeneList.put(rsNumber, noGeneFound);
            } else {
                for (String geneIDs : tmpArr) {
                    snpGeneIDName.put(geneIDs, geneIDToName.get(geneIDs));
                }
                snpGeneList.put(rs, snpGeneIDName);
            }
        }
    }
}
