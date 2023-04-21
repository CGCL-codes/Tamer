package com.dvd.djc.gdjc001;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dvd.common.CommonAction;
import com.dvd.dao.dvd.*;
import com.dvd.utils.*;

/**
 * @author sjd
 *
 */
public class GDJC001Action extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommonAction.New(request, this.servlet);
        String dvdId = request.getParameter("dvdId");
        String action = request.getParameter("action");
        if (action == null || action.length() == 0) {
            action = "paging";
        }
        if (action.equals("next")) {
            return this.paging(dvdId, "next", mapping, request);
        } else if (action.equals("pre")) {
            return this.paging(dvdId, "pre", mapping, request);
        } else if (action.equals("paging")) {
            return this.paging(dvdId, null, mapping, request);
        } else if (action.equals("edit")) {
            return this.toEditDvd(dvdId, mapping);
        } else if (action.equals("cancel")) {
            return this.cancelDvd(dvdId, mapping, request);
        }
        return new ActionForward("/error.jsp?error=GDJC001ActionActionError");
    }

    /**
	 * @param dvdId 
	 * @param flag  "pre" "next" or null
	 * @param mapping
	 * @param request
	 * @return mapping
	 * get the information according to the given dvdId and flag 
	 */
    public ActionForward paging(String dvdId, String flag, ActionMapping mapping, HttpServletRequest request) {
        GDJC001Model model = new GDJC001Model();
        TDvd dvd = null;
        dvd = model.searchDvdInfo(dvdId, flag);
        if (dvd == null) {
            List<Message> messages = new ArrayList<Message>();
            Message m = null;
            if (flag.equals("pre")) {
                m = new Message("", "INFO", "NO DVD IN PREVIOUS");
            } else if (flag.equals("next")) {
                m = new Message("", "INFO", "NO DVD IN NEXT");
            } else {
                System.out.println("ERROR:" + "Can't find the DVD Information");
            }
            messages.add(m);
            request.setAttribute("List<Message>", messages);
            return new ActionForward("/GDJC001.do?dvdId=" + dvdId + "&action=paging");
        }
        request.setAttribute("TDvd", dvd);
        return mapping.findForward("GDJC001");
    }

    public ActionForward toEditDvd(String dvdId, ActionMapping mapping) {
        ActionForward forward = new ActionForward();
        forward.setPath(mapping.findForward("GDJC002Action").getPath() + "?dvdId=" + dvdId);
        return new ActionForward("/GDJC002.do?dvdId=" + dvdId + "&action=edit");
    }

    public ActionForward cancelDvd(String dvdId, ActionMapping mapping, HttpServletRequest request) {
        GDJC001Model model = new GDJC001Model();
        String title = null;
        title = model.cancelDvd(dvdId);
        if (title == null) {
            return new ActionForward("/error.jsp?error=GDJC001ActionCancelError");
        } else {
            List<Message> messages = new ArrayList<Message>();
            messages.add(new Message("", "INFO", "DVD情報は成功に削除しました!"));
            request.setAttribute("List<Message>", messages);
            ActionForward forward = new ActionForward("/GDJK001.do?action=paging&pageId=1");
            return forward;
        }
    }
}
