package ca.ubc.icapture.genapha.actions;

import ca.ubc.icapture.genapha.beans.SNPInfo;
import ca.ubc.icapture.genapha.forms.AllListForm;
import ca.ubc.icapture.genapha.forms.SearchForm;
import ca.ubc.icapture.genapha.forms.GeneListForm;
import ca.ubc.icapture.genapha.forms.PathwayListForm;
import ca.ubc.icapture.genapha.forms.SnpListForm;
import icapture.SQLMgr;
import icapture.beans.DB.Cohort;
import icapture.beans.DB.Gene;
import icapture.beans.DB.GeneLink;
import icapture.beans.DB.SNP;
import icapture.beans.DB.SNPStatus;
import icapture.beans.KeggGeneBean;
import icapture.beans.KeggPathwayBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SearchAction extends Action {

    static final String noResults = "noResults";

    static final String showAll = "Show All";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward forward = new ActionForward();
        SearchForm searchForm = (SearchForm) form;
        HttpSession session = request.getSession();
        boolean isShowAll = searchForm.getSubmitType().equals(showAll);
        String[] searchText = null;
        if (searchForm.getSearchText() != null && !searchForm.getSearchText().trim().isEmpty()) {
            searchText = searchForm.getSearchText().trim().split("\n");
        }
        String[] snpNames = null;
        if (searchForm.getSnps() != null && !searchForm.getSnps().trim().isEmpty()) {
            snpNames = searchForm.getSnps().trim().split("\n");
        }
        String[] geneNames = null;
        if (searchForm.getGenes() != null && !searchForm.getGenes().trim().isEmpty()) {
            geneNames = searchForm.getGenes().trim().split("\n");
        }
        String[] chromoNames = null;
        if (searchForm.getChromosomes() != null && !searchForm.getChromosomes().trim().isEmpty()) {
            chromoNames = searchForm.getChromosomes().trim().split("\n");
        }
        String[] pathwayNames = null;
        if (searchForm.getPathways() != null && !searchForm.getPathways().trim().isEmpty()) {
            pathwayNames = searchForm.getPathways().trim().split("\n");
        }
        Integer selectedPubli = null;
        if (searchForm.getSelectedPubli() != null && searchForm.getSelectedPubli() != -1) {
            selectedPubli = searchForm.getSelectedPubli();
        }
        Integer selectedPendPubli = null;
        if (searchForm.getSelectedPendPubli() != null && searchForm.getSelectedPendPubli() != -1) {
            selectedPendPubli = searchForm.getSelectedPendPubli();
        }
        switch(searchForm.getSearchSelect()) {
            case 0:
                forward = searchAll(searchText, mapping, request);
                break;
            case 1:
                ArrayList<SNPInfo> snpInfoList = searchSnp(searchText, geneNames, chromoNames, pathwayNames, isShowAll);
                if (!snpInfoList.isEmpty()) {
                    SnpListForm snpListForm = new SnpListForm();
                    snpListForm.setSnpList(snpInfoList);
                    request.getSession().setAttribute("snpListForm", snpListForm);
                    forward = mapping.findForward("snpList");
                } else {
                    forward = mapping.findForward(noResults);
                }
                break;
            case 2:
                ArrayList<Gene> geneList = searchGene(searchText, chromoNames, pathwayNames, selectedPubli, selectedPendPubli, isShowAll);
                if (!geneList.isEmpty()) {
                    GeneListForm geneListForm = new GeneListForm();
                    geneListForm.setGeneList(AddGeneList(geneList));
                    request.getSession().setAttribute("geneListForm", geneListForm);
                    forward = mapping.findForward("geneList");
                } else {
                    forward = mapping.findForward(noResults);
                }
                break;
            case 3:
                ArrayList<KeggPathwayBean> pathwayList = searchPathway(searchText, snpNames, geneNames, isShowAll);
                if (!pathwayList.isEmpty()) {
                    PathwayListForm pathwayListForm = new PathwayListForm();
                    pathwayListForm.setKegPathwayList(pathwayList);
                    request.getSession().setAttribute("pathwayListForm", pathwayListForm);
                    forward = mapping.findForward("pathwayList");
                } else {
                    return mapping.findForward(noResults);
                }
                break;
            default:
                forward = mapping.findForward(noResults);
                break;
        }
        return forward;
    }

    private ActionForward searchAll(String[] searchText, ActionMapping mapping, HttpServletRequest request) {
        ArrayList<SNPInfo> snps = searchSnp(searchText, null, null, null, false);
        ArrayList<Gene> genes = searchGene(searchText, null, null, null, null, false);
        ArrayList<KeggPathwayBean> pathways = searchPathway(searchText, null, null, false);
        if (snps.isEmpty() && genes.isEmpty() && pathways.isEmpty()) {
            return mapping.findForward(noResults);
        }
        AllListForm listForm = new AllListForm();
        listForm.setCohortList(SQLMgr.getCohorts());
        listForm.setSnpList(snps);
        listForm.setGeneList(AddGeneList(genes));
        listForm.setPathwayList(pathways);
        request.getSession().setAttribute("allListForm", listForm);
        return mapping.findForward("allList");
    }

    private ArrayList<SNPInfo> searchSnp(String[] searchText, String[] geneNames, String[] chromoNames, String[] pathwayNames, boolean isShowAll) {
        TreeSet<SNP> snps = new TreeSet<SNP>();
        if (isShowAll) {
            snps.addAll(SQLMgr.getSNPs());
        } else {
            if (searchText != null) {
                for (int i = 0; i < searchText.length; i++) {
                    snps.addAll(SQLMgr.getSNPLike(searchText[i].replaceAll("rs", "")));
                }
            }
            if (geneNames != null) {
                for (int i = 0; i < geneNames.length; i++) {
                    ArrayList<Gene> genes = SQLMgr.getGeneLike(geneNames[i].trim().toUpperCase());
                    for (Gene gene : genes) {
                        snps.addAll(SQLMgr.getSNPs(gene));
                    }
                }
            }
            if (chromoNames != null) {
                for (int i = 0; i < chromoNames.length; i++) {
                    snps.addAll(SQLMgr.getSNPsFromChromosome(chromoNames[i].trim()));
                }
            }
            if (pathwayNames != null) {
                for (int i = 0; i < pathwayNames.length; i++) {
                    ArrayList<KeggPathwayBean> kpbs = SQLMgr.getKeggPathwayLike(pathwayNames[i]);
                    for (KeggPathwayBean kpb : kpbs) {
                        for (KeggGeneBean kgb : kpb.getGeneList()) {
                            snps.addAll(SQLMgr.getSNPs(kgb.getGeneId()));
                        }
                    }
                }
            }
        }
        if (snps.isEmpty()) {
            return new ArrayList<SNPInfo>();
        }
        ArrayList<SNPInfo> snpInfoList = new ArrayList<SNPInfo>();
        ArrayList<Cohort> cohortList = SQLMgr.getCohorts();
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
        return snpInfoList;
    }

    private ArrayList<Gene> searchGene(String[] searchText, String[] chromoNames, String[] pathwayNames, Integer selectedPubli, Integer selectedPendPubli, boolean isShowAll) {
        TreeSet<Gene> genes = new TreeSet<Gene>();
        if (isShowAll) {
            genes.addAll(SQLMgr.getGenes());
        } else {
            if (searchText != null) {
                for (int i = 0; i < searchText.length; i++) {
                    genes.addAll(SQLMgr.getGeneLike(searchText[i].trim().toUpperCase()));
                }
            }
            if (chromoNames != null) {
                for (int i = 0; i < chromoNames.length; i++) {
                    ArrayList<SNP> snpList = SQLMgr.getSNPsFromChromosome(chromoNames[i].trim());
                    for (SNP snp : snpList) {
                        Gene gene = snp.getGene();
                        if (gene != null) {
                            genes.add(gene);
                        }
                    }
                }
            }
            if (pathwayNames != null) {
                for (int i = 0; i < pathwayNames.length; i++) {
                    ArrayList<KeggPathwayBean> kpbs = SQLMgr.getKeggPathwayLike(pathwayNames[i]);
                    for (KeggPathwayBean kpb : kpbs) {
                        for (KeggGeneBean kgb : kpb.getGeneList()) {
                            Gene gene = SQLMgr.getGene(kgb.getGeneId());
                            if (gene != null) {
                                genes.add(gene);
                            }
                        }
                    }
                }
            }
            if (selectedPubli != null) {
                genes.addAll(SQLMgr.getGenes(SQLMgr.getGeneSet(selectedPubli)));
            }
            if (selectedPendPubli != null) {
                genes.addAll(SQLMgr.getGenes(SQLMgr.getGeneSet(selectedPendPubli)));
            }
        }
        return new ArrayList<Gene>(genes);
    }

    private ArrayList<GeneLink> AddGeneList(ArrayList<Gene> geneList) {
        ArrayList<GeneLink> list = new ArrayList<GeneLink>();
        Iterator<Gene> iter = geneList.iterator();
        while (iter.hasNext()) {
            Gene gene = iter.next();
            list.add(SQLMgr.getGeneLink(gene));
        }
        return list;
    }

    private ArrayList<KeggPathwayBean> searchPathway(String[] searchText, String[] snpNames, String[] geneNames, boolean isShowAll) {
        TreeSet<KeggPathwayBean> pathways = new TreeSet<KeggPathwayBean>();
        if (isShowAll) {
            pathways.addAll(SQLMgr.getKeggPathways());
        } else {
            if (searchText != null) {
                for (int i = 0; i < searchText.length; i++) {
                    pathways.addAll(SQLMgr.getKeggPathwayLike(searchText[i]));
                }
            }
            TreeSet<Gene> genes = new TreeSet<Gene>();
            if (snpNames != null) {
                for (int i = 0; i < snpNames.length; i++) {
                    for (SNP snp : SQLMgr.getSNPLike(snpNames[i].trim())) {
                        Gene gene = snp.getGene();
                        if (gene != null) {
                            genes.add(gene);
                        }
                    }
                }
            }
            if (geneNames != null) {
                for (int i = 0; i < geneNames.length; i++) {
                    for (Gene gene : SQLMgr.getGeneLike(geneNames[i].trim().toUpperCase())) {
                        if (gene != null) {
                            genes.add(gene);
                        }
                    }
                }
            }
            for (Gene gene : genes) {
                for (String pathwayName : SQLMgr.getKeggPathways(gene)) {
                    pathways.add(SQLMgr.getKeggPathway(pathwayName));
                }
            }
        }
        return new ArrayList<KeggPathwayBean>(pathways);
    }
}
