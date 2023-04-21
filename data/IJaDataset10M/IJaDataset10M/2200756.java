package org.oclc.da.ndiipp.struts.analysis.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oclc.da.ndiipp.struts.analysis.form.ManageCompareForm;
import org.oclc.da.ndiipp.struts.analysis.util.AnalysisBean;
import org.oclc.da.ndiipp.struts.analysis.util.CrawlBean;
import org.oclc.da.ndiipp.struts.analysis.util.CrawlLineDataLayerFacade;
import org.oclc.da.ndiipp.struts.analysis.util.RunDefBean;
import org.oclc.da.ndiipp.struts.analysis.util.SeriesBean;
import org.oclc.da.ndiipp.struts.analysis.util.SeriesDataLayerFacade;
import org.oclc.da.ndiipp.struts.core.action.NDIIPPAction;
import org.oclc.da.ndiipp.struts.core.form.NDIIPPForm;
import org.oclc.da.ndiipp.struts.core.util.NDIIPPDataLayerFacade;
import org.oclc.da.ndiipp.struts.core.util.TreeConst;
import org.oclc.da.ndiipp.struts.domain.util.DomainDataLayerFacade;
import org.oclc.da.ndiipp.struts.packagecontainer.util.PackageContainerDataLayerFacade;
import com.jenkov.prizetags.tree.impl.Tree;
import com.jenkov.prizetags.tree.impl.TreeNode;
import com.jenkov.prizetags.tree.itf.ICollapseListener;
import com.jenkov.prizetags.tree.itf.IExpandListener;
import com.jenkov.prizetags.tree.itf.ITree;
import com.jenkov.prizetags.tree.itf.ITreeNode;

/**
 * This action class gets the initial data to setup the analysis comparison
 * based series tree
 * <P>
 * @author Joe Nelson
 */
public class SetupEditSeriesCompareAction extends NDIIPPAction {

    /**
     * The flag used to determine where to return to when finished with this
     * page.
     */
    protected String editSeries = null;

    /**
     * The relevant data layer facade
     */
    protected CrawlLineDataLayerFacade facade = null;

    /**
     * The struts data form
     */
    protected ManageCompareForm mcf = null;

    /**
     * The user's web session
     */
    protected HttpSession session = null;

    /**
     * (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(
     *      org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ITreeNode crawlNode = null;
        session = request.getSession(false);
        if (session == null) {
            return (mapping.findForward("noSession"));
        }
        mcf = (ManageCompareForm) form;
        String timeoutReached = request.getParameter("timeoutReached");
        if ((timeoutReached == null) || "".equals(timeoutReached)) {
            mcf.setTimeoutReached("false");
        } else {
            mcf.setTimeoutReached(timeoutReached);
        }
        ActionForward action = mapping.findForward("success");
        facade = (CrawlLineDataLayerFacade) session.getAttribute(NDIIPPDataLayerFacade.CRAWL_SERVER);
        if (facade == null) {
            facade = new CrawlLineDataLayerFacade(session);
        }
        DomainDataLayerFacade domainFacade = (DomainDataLayerFacade) session.getAttribute(NDIIPPDataLayerFacade.DOMAIN_SERVER);
        if (domainFacade == null) {
            domainFacade = new DomainDataLayerFacade(session);
        }
        domainFacade.clearEditToken(session);
        PackageContainerDataLayerFacade packageContainerFacade = (PackageContainerDataLayerFacade) session.getAttribute(NDIIPPDataLayerFacade.PACKAGE_CONTAINER_SERVER);
        if (packageContainerFacade == null) {
            packageContainerFacade = new PackageContainerDataLayerFacade(session);
        }
        packageContainerFacade.resetPackageContainers();
        session.setAttribute(NDIIPPDataLayerFacade.PACKAGE_CONTAINER_SERVER, packageContainerFacade);
        session.setAttribute(NDIIPPDataLayerFacade.CRAWL_SERVER, facade);
        session.setAttribute(NDIIPPDataLayerFacade.DOMAIN_SERVER, domainFacade);
        mcf.reset(mapping, request);
        ITree tree = (ITree) session.getAttribute(TreeConst.EDIT_COMPARE_SERIES);
        String expandCrawlLineGUID = request.getParameter("expand");
        session.setAttribute("expandCrawlLineGUID", expandCrawlLineGUID);
        String collapseCrawlLineGUID = request.getParameter("collapse");
        session.setAttribute("collapseCrawlLineGUID", collapseCrawlLineGUID);
        String assignedGUID = request.getParameter("assign");
        String unassignedGUID = request.getParameter("unassign");
        editSeries = request.getParameter("editSeries");
        if (editSeries == null) {
            editSeries = (String) session.getAttribute("EDIT_SERIES");
        } else {
            session.setAttribute("EDIT_SERIES", editSeries);
        }
        SeriesDataLayerFacade seriesFacade = (SeriesDataLayerFacade) session.getAttribute(NDIIPPDataLayerFacade.SERIES_SERVER);
        if (seriesFacade == null) {
            seriesFacade = new SeriesDataLayerFacade(session);
        }
        SeriesBean sb = seriesFacade.getSeries(editSeries);
        session.setAttribute("series", sb);
        session.setAttribute(NDIIPPDataLayerFacade.SERIES_SERVER, seriesFacade);
        AnalysisBean ab = (AnalysisBean) session.getAttribute("analysis");
        mcf.setAnalysis(ab);
        RunDefBean rdbBaseline = (RunDefBean) session.getAttribute(TreeConst.RUN_DEF_BASELINE);
        mcf.setBaselineRun(rdbBaseline);
        mcf.setBaselineStartTime(rdbBaseline.getStartTime());
        mcf.setBaselineStatus(rdbBaseline.getStatus());
        RunDefBean rdbSaved = (RunDefBean) session.getAttribute(TreeConst.RUN_DEF_SAVED);
        mcf.setSavedRun(rdbSaved);
        mcf.setSavedStartTime(rdbSaved.getStartTime());
        mcf.setSavedStatus(rdbSaved.getStatus());
        if (tree != null) {
            if (assignedGUID != null) {
                mcf.setInProcess(NDIIPPForm.TRUE);
                ITreeNode thisNode = tree.findNode(assignedGUID);
                if (thisNode == null) {
                    thisNode = tree.findNode(assignedGUID);
                }
                ITreeNode rootNode = tree.getRoot();
                CrawlBean thisBean = (CrawlBean) thisNode.getObject();
                if (!thisNode.equals(rootNode)) {
                    boolean assignmentFlag = thisBean.isAssigned();
                    thisBean.setAssigned(!assignmentFlag);
                }
                thisBean.setInSeriesPath(true);
                SetupViewTreeAction.assignChildren(thisNode);
            } else if (unassignedGUID != null) {
                mcf.setInProcess(NDIIPPForm.TRUE);
                ITreeNode thisNode = tree.findNode(unassignedGUID);
                if (thisNode == null) {
                    thisNode = tree.findNode(unassignedGUID);
                }
                ITreeNode rootNode = tree.getRoot();
                CrawlBean thisBean = (CrawlBean) thisNode.getObject();
                if (!thisNode.equals(rootNode)) {
                    thisBean.setAssigned(false);
                    thisBean.setSeriesAssignedGUID("");
                }
                thisBean.setInSeriesPath(true);
                SetupViewTreeAction.assignChildren(thisNode);
            }
        } else {
            tree = new Tree();
            tree.setSingleSelectionMode(true);
            if (editSeries != null) {
                mcf.setSeriesGUID(editSeries);
            }
            mcf.setInProcess(NDIIPPForm.FALSE);
            CrawlBean rootBean = facade.getRoot(ab.getGuid(), rdbBaseline.getRunNumber(), rdbSaved.getRunNumber());
            crawlNode = new TreeNode(rootBean.getGuid(), rootBean.getUri());
            crawlNode.setObject(rootBean);
            tree.setRoot(crawlNode);
            tree.addExpandListener(new IExpandListener() {

                /**
                 * This listener will listen for and react to expand events
                 * <P>
                 * @param node The expanded node object
                 * @param localTree The whole tree
                 */
                public void nodeExpanded(ITreeNode node, ITree localTree) {
                    if (node == null) {
                        String nodeGUID = (String) session.getAttribute("expandCrawlLineGUID");
                        if (nodeGUID != null) {
                            localTree.findNode(nodeGUID);
                            localTree.expand(nodeGUID);
                        }
                        return;
                    }
                    if (node.hasChildren()) {
                        node.removeAllChildren();
                        RunDefBean rdbSavedLocal = (RunDefBean) session.getAttribute(TreeConst.RUN_DEF_SAVED);
                        RunDefBean rdbBaselineLocal = (RunDefBean) session.getAttribute(TreeConst.RUN_DEF_BASELINE);
                        CrawlBean currentCrawlLine = (CrawlBean) node.getObject();
                        if (currentCrawlLine.isDummy()) {
                            List children = node.getChildren();
                            for (int ii = 0; ii < children.size(); ii++) {
                                ITreeNode childNode = (ITreeNode) children.get(ii);
                                CrawlBean childCrawlLine = (CrawlBean) childNode.getObject();
                                if (childCrawlLine.isDummy()) {
                                    ArrayList<CrawlBean> childList = facade.getChildren(childCrawlLine, rdbBaselineLocal.getRunNumber(), rdbSavedLocal.getRunNumber());
                                    for (int jj = 0; jj < childList.size(); jj++) {
                                        CrawlBean grandchildCrawlLine = childList.get(jj);
                                        ITreeNode grandchildNode = new TreeNode(grandchildCrawlLine.getGuid(), grandchildCrawlLine.getUri(), childNode);
                                        grandchildCrawlLine.setInSeriesPath(childCrawlLine.isInSeriesPath());
                                        grandchildNode.setObject(grandchildCrawlLine);
                                    }
                                }
                            }
                        }
                    }
                }
            });
            tree.addCollapseListener(new ICollapseListener() {

                /**
                 * This listener will listen for and react to collapse events.
                 * <P>
                 * @param node The collapsed node object
                 * @param localTree The whole tree
                 */
                public void nodeCollapsed(ITreeNode node, ITree localTree) {
                    if (node == null) {
                        String nodeGUID = (String) session.getAttribute("collapseCrawlLineGUID");
                        if (nodeGUID != null) {
                            localTree.collapse(nodeGUID);
                        }
                        return;
                    }
                }
            });
            ArrayList<CrawlBean> childList = facade.getChildren(rootBean, rdbBaseline.getRunNumber(), rdbSaved.getRunNumber());
            for (int ii = 0; ii < childList.size(); ii++) {
                CrawlBean childCrawlLine = childList.get(ii);
                ITreeNode childNode = new TreeNode(childCrawlLine.getGuid(), childCrawlLine.getUri(), crawlNode);
                if (editSeries.equals(childCrawlLine.getSeriesAssignedGUID())) {
                    childCrawlLine.setAssigned(true);
                }
                childNode.setObject(childCrawlLine);
                if (childCrawlLine.isDummy()) {
                    ArrayList<CrawlBean> grandchildList = facade.getChildren(childCrawlLine, rdbBaseline.getRunNumber(), rdbSaved.getRunNumber());
                    for (int jj = 0; jj < grandchildList.size(); jj++) {
                        CrawlBean grandchildCrawlLine = grandchildList.get(jj);
                        ITreeNode grandchildNode = new TreeNode(grandchildCrawlLine.getGuid(), grandchildCrawlLine.getUri(), childNode);
                        if (editSeries.equals(grandchildCrawlLine.getSeriesAssignedGUID())) {
                            grandchildCrawlLine.setAssigned(true);
                        }
                        grandchildNode.setObject(grandchildCrawlLine);
                        if (grandchildCrawlLine.isDummy()) {
                            ArrayList<CrawlBean> greatGrandchildList = facade.getChildren(grandchildCrawlLine, rdbBaseline.getRunNumber(), rdbSaved.getRunNumber());
                            for (int kk = 0; kk < greatGrandchildList.size(); kk++) {
                                CrawlBean greatGrandchildCrawlLine = greatGrandchildList.get(kk);
                                ITreeNode greatGrandchildNode = new TreeNode(greatGrandchildCrawlLine.getGuid(), greatGrandchildCrawlLine.getUri(), grandchildNode);
                                if (editSeries.equals(greatGrandchildCrawlLine.getSeriesAssignedGUID())) {
                                    greatGrandchildCrawlLine.setAssigned(true);
                                }
                                greatGrandchildNode.setObject(greatGrandchildCrawlLine);
                            }
                        }
                    }
                }
            }
            tree.setRoot(crawlNode);
            session.setAttribute(TreeConst.EDIT_COMPARE_SERIES, tree);
            session.setAttribute(TreeConst.EDIT_COMPARE_TREE_VIEW, NDIIPPForm.TRUE);
            session.setAttribute("analysisEditing", NDIIPPForm.TRUE);
            session.setAttribute(BOTTOM_PANE, EDIT_COMPARISON_SERIES);
        }
        return (action);
    }
}
