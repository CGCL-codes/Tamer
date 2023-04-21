package com.quikj.application.communicator.applications.webtalk.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import com.quikj.application.communicator.admin.controller.LinkAttribute;
import com.quikj.application.communicator.admin.model.AccountElement;
import com.quikj.application.communicator.applications.webtalk.model.GroupTable;

/**
 * 
 * @author bhm
 */
public class DropCustomerSelectAction extends Action {

    /** Creates a new instance of UserManagementAction */
    public DropCustomerSelectAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        DropCustomerSelectForm cform = (DropCustomerSelectForm) form;
        ActionErrors errors = new ActionErrors();
        Connection c = (Connection) request.getSession().getAttribute("connection");
        if (c == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.not.logged.in"));
            saveErrors(request, errors);
            return mapping.findForward("logon");
        }
        AccountElement element = (AccountElement) request.getSession().getAttribute("userInfo");
        if (element.isAdminLevel() == false) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.insufficient.privilege"));
            saveErrors(request, errors);
            return mapping.findForward("main_menu");
        }
        String submit = cform.getSubmit();
        if (submit.startsWith("Cancel") == false) {
            String domain = cform.getDomain();
            GroupTable groups = new GroupTable();
            groups.setConnection(c);
            ArrayList domain_list = groups.listDomains();
            if (domain_list != null) {
                ArrayList list = new ArrayList();
                Iterator iter = domain_list.iterator();
                while (iter.hasNext() == true) {
                    String dom = (String) iter.next();
                    if (dom.equals("ace") == false) {
                        list.add(new LabelValueBean(dom, URLEncoder.encode(dom, "UTF-8")));
                    }
                }
                cform.setDomains(list);
            }
            request.setAttribute("domain", new String(domain));
            WebTalkRelatedTasks menu = new WebTalkRelatedTasks();
            request.setAttribute("menu", menu);
            menu.addLink(new LinkAttribute("List all groups", "list_groups"));
            menu.addLink(new LinkAttribute("List all features", "list_features"));
            menu.addLink(new LinkAttribute("Search users", "display_user_search"));
            menu.addLink(new LinkAttribute("Search canned messages", "display_canned_message_search"));
        }
        if (errors.isEmpty() == false) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        return mapping.findForward(submit);
    }
}
