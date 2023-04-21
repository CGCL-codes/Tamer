package ca.ubc.icapture.genapha.external.actions;

import ca.ubc.icapture.genapha.beans.AlleleCounts;
import ca.ubc.icapture.genapha.beans.AssociationResultBean;
import ca.ubc.icapture.genapha.beans.PopulationDiversityBean;
import ca.ubc.icapture.genapha.external.beans.Login;
import ca.ubc.icapture.genapha.external.forms.SnpSummaryForm;
import icapture.beans.DB.EthnicityFrequency;
import icapture.beans.DB.SNPStatus;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.Action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import icapture.SQLMgr;
import icapture.beans.DB.Cohort;
import icapture.beans.DB.SNP;
import icapture.beans.DB.Values;
import icapture.beans.KeggGeneBean;
import icapture.beans.KeggPathwayBean;
import icapture.beans.PageLink;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: btripp
 * Date: Sep 13, 2006
 * Time: 9:39:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class SnpSummaryAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        SnpSummaryForm snpSummaryForm = (SnpSummaryForm) form;
        SNP snp = null;
        try {
            snp = SQLMgr.getCachedSnp(snpSummaryForm.getRsNumber());
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.findForward("home");
        }
        Boolean isVisible = Login.isSnpVisible(session, snp);
        if (isVisible == null) {
            return mapping.findForward("login");
        } else if (!isVisible) {
            return mapping.findForward("home");
        }
        generalInfo(snp, snpSummaryForm);
        genoStatus(snp, snpSummaryForm);
        assocResults(snp, snpSummaryForm);
        popDiversity(snp, snpSummaryForm);
        links(snp, snpSummaryForm);
        return mapping.findForward("display");
    }

    /**
     * Generate general information section for this SNP and add it to the form
     * 
     * @param snp The SNP of this page
     * @param form The ActionForm object used to generate this page
     */
    private void generalInfo(SNP snp, SnpSummaryForm form) {
        form.setSnp(snp);
        form.setGeneSnpList(SQLMgr.getSNPs(snp.getGene()));
        ArrayList<String> aliases = SQLMgr.getGeneAlias(snp.getGene());
        Collections.sort(aliases);
        StringBuilder aliasBuilder = new StringBuilder();
        if (!aliases.isEmpty()) {
            Iterator<String> aliasIter = aliases.iterator();
            String firstAlias = aliasIter.next();
            if (firstAlias.equalsIgnoreCase(snp.getGene().getName())) {
                if (aliasIter.hasNext()) {
                    aliasBuilder.append(aliasIter.next());
                }
            } else {
                aliasBuilder.append(firstAlias);
            }
            while (aliasIter.hasNext()) {
                String alias = aliasIter.next();
                if (!alias.equalsIgnoreCase(snp.getGene().getName())) {
                    aliasBuilder.append(", ").append(alias);
                }
            }
        }
        form.setGeneAlias(aliasBuilder.toString());
        aliases = SQLMgr.getSNPAlias(snp);
        Collections.sort(aliases);
        aliasBuilder = new StringBuilder();
        if (!aliases.isEmpty()) {
            Iterator<String> aliasIter = aliases.iterator();
            String firstAlias = aliasIter.next();
            if (firstAlias.equalsIgnoreCase(snp.getRsNumber())) {
                if (aliasIter.hasNext()) {
                    aliasBuilder.append(aliasIter.next());
                }
            } else {
                aliasBuilder.append(firstAlias);
            }
            while (aliasIter.hasNext()) {
                String alias = aliasIter.next();
                if (!alias.equalsIgnoreCase(snp.getRsNumber())) {
                    aliasBuilder.append(", ").append(alias);
                }
            }
        }
        form.setSnpAlias(aliasBuilder.toString());
        form.setPrevAssociations(SQLMgr.isSnpInSet(snp, 0));
        if (form.getPrevAssociations()) {
            form.setReferences(SQLMgr.getReferences(snp));
        }
    }

    /**
     * Generate genotyping status section for this SNP and add it to the form
     * 
     * @param snp The SNP of this page
     * @param form The ActionForm object used to generate this page
     */
    private void genoStatus(SNP snp, SnpSummaryForm form) {
        ArrayList<SNPStatus> snpStatusList = new ArrayList<SNPStatus>();
        ArrayList<Cohort> cohortList = SQLMgr.getCohorts();
        for (Cohort cohort : cohortList) {
            SNPStatus snpStatus = SQLMgr.getSNPStatus(snp, cohort);
            snpStatusList.add(snpStatus);
        }
        form.setCohortList(cohortList);
        form.setSnpStatusList(snpStatusList);
    }

    /**
     * Generate association results section for this SNP and add it to the form
     * 
     * @param snp The SNP of this page
     * @param form The ActionForm object used to generate this page
     */
    private void assocResults(SNP snp, SnpSummaryForm form) {
        HashMap<String, AssociationResultBean> arbMap = new HashMap<String, AssociationResultBean>();
        ArrayList<Values> vals = SQLMgr.getValues(snp);
        for (Values val : vals) {
            String key = val.getCohort().getCohortID() + "-" + val.getPhenotype().getPhenotypeID();
            AssociationResultBean arb = arbMap.get(key);
            if (arb == null) {
                arb = new AssociationResultBean();
                arbMap.put(key, arb);
            }
            if (val.getExperiment().getExperimentID() == 0) {
                arb.setCombinedValues(val);
            } else if (val.getExperiment().getExperimentID() == 1) {
                arb.setCaucasianValues(val);
            } else if (val.getExperiment().getExperimentID() == 2) {
                arb.setNonCauacasianValues(val);
            }
        }
        HashMap<String, ArrayList<AssociationResultBean>> snpPValueMap = new HashMap<String, ArrayList<AssociationResultBean>>();
        for (AssociationResultBean arb : arbMap.values()) {
            String key = arb.getPhenotype().getName();
            ArrayList<AssociationResultBean> arbList = snpPValueMap.get(key);
            if (arbList == null) {
                arbList = new ArrayList<AssociationResultBean>();
                snpPValueMap.put(key, arbList);
            }
            arbList.add(arb);
        }
        for (ArrayList<AssociationResultBean> list : snpPValueMap.values()) {
            Collections.sort(list);
        }
        form.setSnpPValueMap(snpPValueMap);
    }

    /**
     * Generate population diversity section for this SNP and add it to the form
     * 
     * @param snp The SNP of this page
     * @param form The ActionForm object used to generate this page
     */
    private void popDiversity(SNP snp, SnpSummaryForm form) {
        ArrayList<PopulationDiversityBean> pdbList = new ArrayList<PopulationDiversityBean>();
        form.setPopulationDiversityList(pdbList);
        ArrayList<PopulationDiversityBean> caseControlPdbList = new ArrayList<PopulationDiversityBean>();
        form.setCaseControlDiversityList(caseControlPdbList);
        PopulationDiversityBean pdb = null;
        ArrayList<EthnicityFrequency> efList = SQLMgr.getEthnicityFrequency(snp);
        HashMap<String, PopulationDiversityBean> familyPDMap = new HashMap<String, PopulationDiversityBean>();
        HashMap<String, PopulationDiversityBean> caseControlPDMap = new HashMap<String, PopulationDiversityBean>();
        for (EthnicityFrequency ef : efList) {
            String key = ef.getCohort().getCohortID() + "-" + ef.getEthnicity().getEthnicityID();
            if (ef.getCode() == 1 || ef.getCode() == 3) {
                pdb = familyPDMap.get(key);
                if (pdb == null) {
                    pdb = new PopulationDiversityBean();
                    pdb.setCohort(ef.getCohort());
                    pdb.setEthnicity(ef.getEthnicity());
                    familyPDMap.put(key, pdb);
                    pdbList.add(pdb);
                }
            } else {
                pdb = caseControlPDMap.get(key);
                if (pdb == null) {
                    pdb = new PopulationDiversityBean();
                    pdb.setCohort(ef.getCohort());
                    pdb.setEthnicity(ef.getEthnicity());
                    caseControlPDMap.put(key, pdb);
                    caseControlPdbList.add(pdb);
                }
            }
            if (ef.getCode() == 1 || ef.getCode() == 4) {
                AlleleCounts child = pdb.getChild();
                if (child == null) {
                    child = new AlleleCounts();
                    pdb.setChild(child);
                }
                if (ef.getAllele().equalsIgnoreCase(snp.getMajorAllele())) {
                    child.setAllele1Count(ef.getN());
                } else {
                    child.setAllele2Count(ef.getN());
                }
            } else if (ef.getCode() == 3 || ef.getCode() == 5) {
                AlleleCounts parent = pdb.getParent();
                if (parent == null) {
                    parent = new AlleleCounts();
                    pdb.setParent(parent);
                }
                if (ef.getAllele().equalsIgnoreCase(snp.getMajorAllele())) {
                    parent.setAllele1Count(ef.getN());
                } else {
                    parent.setAllele2Count(ef.getN());
                }
            }
        }
        for (PopulationDiversityBean pd : pdbList) {
            AlleleCounts child = pd.getChild();
            if (child != null) {
                child.calculate();
            }
            AlleleCounts parent = pd.getParent();
            if (parent != null) {
                parent.calculate();
            }
        }
        for (PopulationDiversityBean pd : caseControlPdbList) {
            AlleleCounts child = pd.getChild();
            if (child != null) {
                child.calculate();
            }
            AlleleCounts parent = pd.getParent();
            if (parent != null) {
                parent.calculate();
            }
        }
    }

    /**
     * Generate links section for this SNP and add it to the form
     * 
     * @param snp The SNP of this page
     * @param form The ActionForm object used to generate this page
     */
    private void links(SNP snp, SnpSummaryForm form) {
        PageLink snpLink = new PageLink();
        snpLink.setName("dbSNP");
        snpLink.setUrl("http://www.ncbi.nlm.nih.gov/SNP/snp_ref.cgi?rs=" + snp.getRsNumber());
        ArrayList<PageLink> snpLinks = new ArrayList<PageLink>();
        snpLinks.add(snpLink);
        form.setSnpLinks(snpLinks);
        ArrayList<PageLink> geneLinks = SQLMgr.getGeneLinks(snp.getGene());
        form.setGeneLinks(geneLinks);
        ArrayList<PageLink> pathwayLinks = new ArrayList<PageLink>();
        for (String pathwayName : SQLMgr.getKeggPathways(snp.getGene())) {
            for (KeggPathwayBean kpb : SQLMgr.getKeggPathwayLike(pathwayName)) {
                PageLink page = new PageLink();
                page.setName(kpb.getName());
                StringBuilder url = new StringBuilder("http://www.genome.jp/dbget-bin/show_pathway?hsa");
                url.append(kpb.getId());
                for (KeggGeneBean kgb : kpb.getGeneList()) {
                    url.append("+").append(kgb.getGeneId());
                }
                page.setUrl(url.toString());
                ArrayList<KeggGeneBean> kgbs = kpb.getGeneList();
                Collections.sort(kgbs);
                Iterator<KeggGeneBean> kgbIter = kgbs.iterator();
                StringBuilder title = new StringBuilder(kgbIter.next().getName());
                while (kgbIter.hasNext()) {
                    title.append(", ").append(kgbIter.next().getName());
                }
                page.setTitle(title.toString());
                pathwayLinks.add(page);
            }
        }
        form.setPathwayLinks(pathwayLinks);
    }
}
