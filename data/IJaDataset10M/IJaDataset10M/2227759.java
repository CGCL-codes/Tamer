package com.bugfree4j.struts.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.bugfree4j.bean.Dict;
import com.bugfree4j.dao.BuginfoDAO;
import com.bugfree4j.dao.DAOConstants;
import com.bugfree4j.domain.Buginfo;
import com.bugfree4j.per.common.web.struts.BaseAction;
import com.bugfree4j.struts.form.BuginfoFormBean;
import com.bugfree4j.tools.DateUtil;

public class ResolveBug extends BaseAction {

    /** 
	 * Method execute
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return ActionForward
	 * @throws Exception
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.getLog().info("****************����ResolveBug");
        BuginfoFormBean fm = (BuginfoFormBean) form;
        ActionErrors errors = new ActionErrors();
        int curBugId = Integer.parseInt(fm.getBugid());
        this.getLog().info("*******resolved bugid:" + curBugId);
        BuginfoDAO buginfoDao = (BuginfoDAO) this.getBean(DAOConstants.BUGINFO_DAO);
        Buginfo buginfo = buginfoDao.get(new Integer(curBugId));
        if (buginfo != null) {
            fm.setProjectid(Long.toString(buginfo.getProjectid()));
            fm.setProjectname(buginfo.getProjectname());
            fm.setModuleid(Integer.toString(buginfo.getModuleid()));
            fm.setModulepath(buginfo.getModulepath());
            fm.setBugtitle(buginfo.getBugtitle());
            fm.setBugtype(buginfo.getBugtype());
            fm.setBugos(buginfo.getBugos());
            fm.setBugseverity(Short.toString(buginfo.getBugseverity()));
            fm.setBugstatus(buginfo.getBugstatus());
            fm.setOpenedby(Dict.getRealname(buginfo.getOpenedby()));
            fm.setOpeneddate(DateUtil.format(buginfo.getOpeneddate(), DateUtil.CN_TIME_STYLE_LONG));
            fm.setOpenedbuild(buginfo.getOpenedbuild());
            fm.setAssignedto(Dict.getRealname(buginfo.getAssignedto()));
            fm.setAssigneddate(DateUtil.format(buginfo.getAssigneddate(), DateUtil.CN_TIME_STYLE_LONG));
            fm.setResolvedby(Dict.getRealname(buginfo.getResolvedby()));
            fm.setResolveddate(DateUtil.format(buginfo.getResolveddate(), DateUtil.CN_TIME_STYLE_LONG));
            fm.setResolvedbuild(buginfo.getResolvedbuild());
            fm.setResolution(buginfo.getResolution());
            fm.setClosedby(Dict.getRealname(buginfo.getClosedby()));
            fm.setCloseddate(DateUtil.format(buginfo.getCloseddate(), DateUtil.CN_TIME_STYLE_LONG));
            fm.setLasteditedby(Dict.getRealname(buginfo.getLasteditedby()));
            fm.setLastediteddate(DateUtil.format(buginfo.getLastediteddate(), DateUtil.CN_TIME_STYLE_LONG));
            fm.setLinkid(buginfo.getLinkid());
        }
        List bugresolutionlist = Dict.getBugresolutionList();
        request.getSession().setAttribute("bugresolutionlist", bugresolutionlist);
        List bughistorylist = Dict.getBughistoryList(new Long(curBugId));
        request.setAttribute("bughistorylist", bughistorylist);
        return mapping.findForward("success");
    }
}
