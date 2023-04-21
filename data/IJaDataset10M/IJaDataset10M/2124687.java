package ca.ubc.icapture.genapha.actions;

import ca.ubc.icapture.genapha.beans.SNPInfo;
import ca.ubc.icapture.genapha.forms.GeneSummaryForm;
import icapture.beans.DB.SNPStatus;
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
import icapture.beans.KeggGeneBean;
import icapture.beans.KeggPathwayBean;
import icapture.beans.PageLink;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class GeneSummaryAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ActionForward forward = new ActionForward();
        GeneSummaryForm geneSummaryForm = (GeneSummaryForm) form;
        Gene gene = null;
        TreeSet<SNP> snps = new TreeSet<SNP>();
        if ((geneSummaryForm.getGeneName() == null || geneSummaryForm.getGeneName().trim().isEmpty())) {
            return mapping.findForward("failure");
        } else {
            gene = SQLMgr.getGene(geneSummaryForm.getGeneName().trim());
            snps.addAll(SQLMgr.getSNPs(gene));
        }
        if (!snps.isEmpty()) {
            ArrayList<SNPInfo> snpInfoList = new ArrayList<SNPInfo>();
            geneSummaryForm.setSnpList(snpInfoList);
            ArrayList<Cohort> cohortList = geneSummaryForm.getCohortList();
            for (SNP snp : snps) {
                SNPInfo snpInfo = new SNPInfo();
                snpInfo.setSnp(snp);
                ArrayList<SNPStatus> statusList = new ArrayList<SNPStatus>();
                snpInfo.setStatusList(statusList);
                for (Cohort cohort : cohortList) {
                    SNPStatus snpStatus = SQLMgr.getSNPStatus(snp, cohort);
                    statusList.add(snpStatus);
                }
                snpInfoList.add(snpInfo);
            }
        }
        ArrayList<String> geneAliases = SQLMgr.getGeneAlias(gene);
        StringBuilder aliasBuilder = new StringBuilder();
        if (!geneAliases.isEmpty()) {
            Iterator<String> aliasIter = geneAliases.iterator();
            String firstAlias = aliasIter.next();
            if (firstAlias.equalsIgnoreCase(gene.getName())) {
                if (aliasIter.hasNext()) {
                    aliasBuilder.append(aliasIter.next());
                }
            } else {
                aliasBuilder.append(firstAlias);
            }
            while (aliasIter.hasNext()) {
                String alias = aliasIter.next();
                if (!alias.equalsIgnoreCase(gene.getName())) {
                    aliasBuilder.append(", ").append(alias);
                }
            }
        }
        if (aliasBuilder.length() <= 0) {
            aliasBuilder.append("N/A");
        }
        geneSummaryForm.setGeneAliases(aliasBuilder.toString());
        if (!snps.isEmpty()) {
            geneSummaryForm.setChromosome(snps.first().getChromosome());
        } else {
            geneSummaryForm.setChromosome("N/A");
        }
        geneSummaryForm.setGeneLinks(SQLMgr.getGeneLinks(gene));
        ArrayList<PageLink> pathwayLinks = new ArrayList<PageLink>();
        for (String pathwayName : SQLMgr.getKeggPathways(gene)) {
            for (KeggPathwayBean kpb : SQLMgr.getKeggPathwayLike(pathwayName)) {
                PageLink page = new PageLink();
                page.setName(kpb.getName());
                StringBuilder url = new StringBuilder("http://www.genome.jp/dbget-bin/show_pathway?hsa");
                url.append(kpb.getId());
                for (KeggGeneBean kgb : kpb.getGeneList()) {
                    url.append("+").append(kgb.getGeneId());
                }
                page.setUrl(url.toString());
                Iterator<KeggGeneBean> kgbIter = kpb.getGeneList().iterator();
                StringBuilder title = new StringBuilder(kgbIter.next().getName());
                while (kgbIter.hasNext()) {
                    title.append(", ").append(kgbIter.next().getName());
                }
                page.setTitle(title.toString());
                pathwayLinks.add(page);
            }
        }
        geneSummaryForm.setPathwayLinks(pathwayLinks);
        forward = mapping.findForward("display");
        return forward;
    }
}
