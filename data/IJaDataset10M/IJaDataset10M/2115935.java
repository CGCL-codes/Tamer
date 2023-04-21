package ca.ubc.icapture.genapha.actions;

import icapture.SQLMgr;
import org.biomoby.services.ncbi.NCBIServices;
import icapture.beans.DB.Cohort;
import icapture.beans.DB.Ethnicity;
import icapture.beans.DB.Phenotypes;
import icapture.beans.DB.SNP;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AnalysisAjaxAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        StringBuilder output = new StringBuilder();
        String requestData = request.getParameter("request");
        if (requestData.equalsIgnoreCase("ethnicity")) {
            String[] cohortIds = request.getParameterValues("selectedCohort[]");
            ArrayList<Cohort> cohorts = new ArrayList<Cohort>();
            for (int i = 0; i < cohortIds.length; i++) {
                cohorts.add(SQLMgr.getCohort(Integer.parseInt(cohortIds[i])));
            }
            List<Ethnicity> ethnicities = SQLMgr.getEthnicitys(cohorts);
            for (Ethnicity ethnicity : ethnicities) {
                output.append("<option value=\"").append(ethnicity.getEthnicityID()).append("\">");
                output.append(ethnicity.getEthnicity().trim());
                output.append("</option>\n");
            }
        } else if (requestData.equalsIgnoreCase("phenotype")) {
            List<Phenotypes> phenotypes = SQLMgr.getBinaryPhenotypes(Integer.parseInt(request.getParameter("selectedCohort")));
            for (Phenotypes phenotype : phenotypes) {
                output.append("<option value=\"").append(phenotype.getPhenotypeID()).append("\">");
                output.append(phenotype.getName().trim());
                output.append("</option>\n");
            }
        } else if (requestData.equalsIgnoreCase("snps")) {
            String[] geneIds = request.getParameterValues("selectedGene[]");
            ArrayList<SNP> snps = new ArrayList<SNP>();
            for (int i = 0; i < geneIds.length; i++) {
                snps.addAll(SQLMgr.getSNPs(SQLMgr.getGene(Integer.parseInt(geneIds[i]))));
            }
            Iterator<SNP> snpIter = snps.iterator();
            SNP snp = snpIter.next();
            output.append(snp.getRsNumber());
            output.append("|").append(snp.getGene().getName());
            output.append("|").append("<input type=\"checkbox\" name=\"selectedSnps\" value=\"" + snp.getRsNumber() + "\" />");
            while (snpIter.hasNext()) {
                snp = snpIter.next();
                output.append("||").append(snp.getRsNumber());
                output.append("|").append(snp.getGene().getName());
                output.append("|").append("<input type=\"checkbox\" name=\"selectedSnps\" value=\"" + snp.getRsNumber() + "\" />");
            }
        } else if (requestData.equalsIgnoreCase("isBinary")) {
            Phenotypes phenotype = SQLMgr.getPhenotype(Integer.valueOf(request.getParameter("selectedPhenotype")));
            output.append((phenotype.getType() == Phenotypes.BINARY));
        }
        try {
            PrintWriter writer = response.getWriter();
            writer.print(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
