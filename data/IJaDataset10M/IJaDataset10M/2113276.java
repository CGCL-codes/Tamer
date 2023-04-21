package org.sf.alpenstock.hr.web.action;

import java.util.*;
import javax.servlet.http.*;
import org.apache.commons.logging.*;
import org.apache.struts.action.*;
import org.sf.alpenstock.hr.entity.*;
import org.sf.alpenstock.hr.service.*;
import org.sf.alpenstock.util.*;

/**
 * @author lenovo
 * 
 */
public final class DelWorkrecordAction extends Action {

    private static final Log log = LogFactory.getLog(DelWorkrecordAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("step into DelWorkrecordAction");
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute("member");
        ActionForward forward = mapping.findForward("login_fail");
        String recordid = request.getParameter("recordid");
        long rid = Long.parseLong(recordid);
        log.debug("recordid = " + rid);
        WorkrecordService.delWorkrecordById(rid);
        List<Memworkrecord> recordList = (List<Memworkrecord>) session.getAttribute("recordList");
        if (recordList != null) {
            Helper.reElementById(recordList, rid, "recordId");
            session.setAttribute("recordList", recordList);
        }
        forward = mapping.findForward("success");
        log.info("step out AddWorkrecordAction");
        return forward;
    }
}
