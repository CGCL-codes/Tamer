package ca.ubc.icapture.genapha.external.actions;

import ca.ubc.icapture.genapha.external.beans.Login;
import ca.ubc.icapture.genapha.external.forms.ExportForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import icapture.SQLMgr;
import icapture.beans.DB.Cohort;
import icapture.beans.DB.Gene;
import icapture.beans.DB.SNP;
import icapture.beans.DB.User;
import icapture.beans.DBExportOptions;
import icapture.beans.Format;
import icapture.genapha.GenotypeToolsManager;
import java.util.*;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: btripp
 * Date: Sep 13, 2006
 * Time: 9:38:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class ExportAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ActionForward forward = new ActionForward();
        HttpSession session = request.getSession();
        final ExportForm exportForm = (ExportForm) form;
        DBExportOptions options = new DBExportOptions();
        try {
            forward = mapping.findForward("success");
            GenotypeToolsManager gtm = new GenotypeToolsManager();
            final String[] cohortids = exportForm.getSelectedCohort();
            final Integer phenotypeid = new Integer(exportForm.getSelectedPhenotype());
            final String[] geneIDList = exportForm.getSelectedGenes();
            final Format format;
            if (exportForm.getFormat().equalsIgnoreCase("SAGE")) {
                format = gtm.SAGE_FORMAT;
            } else if (exportForm.getFormat().equalsIgnoreCase("UNPHASED")) {
                format = gtm.UNPHASED_FORMAT;
            } else {
                format = gtm.FBAT_FORMAT;
            }
            options.setPhenotypeID(phenotypeid);
            options.setCohortIds(cohortids);
            options.setRemoveMZTwins(exportForm.isRemoveMzTwins());
            options.setRemoveDups(exportForm.isRemoveDuplicateSubjects());
            options.setGeneIDs(geneIDList);
            options.setEthnicityIDs(exportForm.getSelectedEthnicity());
            options.setFormat(format);
            options.setFilePerGene(exportForm.isFilePerGene());
            options.setCohortPopulationIDs(exportForm.getSelectedSubCohort());
            options.setZeroIncomplete(exportForm.isZeroIncomplete());
            options.setZeroControls(exportForm.isZeroControls());
            options.setHyperControls(exportForm.isHyperControls());
            options.setCaseControl(exportForm.isCaseControl());
            options.setRsNumbers(exportForm.getSelectedSnps());
            options.setUser(Login.getUser(session));
            options.setCovariates(exportForm.getSelectedCovariates());
            ArrayList<User> ownerList = new ArrayList<User>();
            StringBuilder cohortString = new StringBuilder();
            for (String cohortId : options.getCohortIds()) {
                Cohort cohort = SQLMgr.getCohort(Integer.parseInt(cohortId));
                cohortString.append(cohort.getName() + ", ");
                ownerList.addAll(SQLMgr.getCohortOwners(cohort));
            }
            cohortString.delete(cohortString.length() - 2, cohortString.length() - 1);
            if (options.getUser().getSupervisor() != null) {
                ownerList.add(SQLMgr.getUser(options.getUser().getSupervisor()));
            }
            TreeSet<SNP> snpSet = new TreeSet<SNP>();
            for (String geneId : options.getGeneIDs()) {
                Gene gene = SQLMgr.getGene(Integer.parseInt(geneId));
                if (gene != null) {
                    snpSet.addAll(SQLMgr.getSNPs(gene));
                }
            }
            if (options.getRsNumbers() != null) {
                for (String rsNumber : options.getRsNumbers()) {
                    SNP snp = SQLMgr.getSNP(rsNumber);
                    if (snp != null) {
                        snpSet.add(snp);
                    }
                }
            }
            StringBuilder snpString = new StringBuilder();
            for (SNP snp : snpSet) {
                snpString.append(snp.getRsNumber() + ", ");
                ownerList.addAll(SQLMgr.getSnpOwners(snp));
            }
            snpString.delete(snpString.length() - 2, snpString.length() - 1);
            StringBuilder varString = new StringBuilder();
            varString.append(SQLMgr.getPhenotype(options.getPhenotypeID()).getName());
            if (options.getCovariates() != null) {
                for (String cov : options.getCovariates()) {
                    varString.append(", " + SQLMgr.getPhenotype(Integer.parseInt(cov)).getName());
                }
            }
            try {
                int optionsid = SQLMgr.insertExportOptions(options);
                optionsid = 1;
                int requestid = SQLMgr.insertDataRequest(optionsid, cohortString.substring(0, (cohortString.length() > 40) ? 40 : cohortString.length() - 1), snpString.substring(0, (snpString.length() > 40) ? 40 : snpString.length() - 1), varString.substring(0, (varString.length() > 40) ? 40 : varString.length() - 1), options.getUser());
                TreeSet<User> ownerSet = new TreeSet(ownerList);
                for (User user : ownerSet) {
                    SQLMgr.insertDataRequestApproval(requestid, user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            forward = mapping.findForward("error");
        }
        return forward;
    }
}
