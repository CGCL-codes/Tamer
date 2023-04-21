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
public class DisplayCannedMessageSearchAction extends Action {

    /** Creates a new instance of DisplayUserSearchAction */
    public DisplayCannedMessageSearchAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        CannedMessageSearchForm cform = (CannedMessageSearchForm) form;
        ActionErrors errors = new ActionErrors();
        Connection c = (Connection) request.getSession().getAttribute("connection");
        if (c == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.not.logged.in"));
            saveErrors(request, errors);
            return mapping.findForward("logon");
        }
        AccountElement element = (AccountElement) request.getSession().getAttribute("userInfo");
        if ((element.isAdminLevel() == false) && (element.isCustomerLevel() == false)) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.insufficient.privilege"));
            saveErrors(request, errors);
            return mapping.findForward("main_menu");
        }
        GroupTable groups = new GroupTable();
        groups.setConnection(c);
        if (element.isAdminLevel() == true) {
            ArrayList group_list = groups.list();
            if (group_list != null) {
                ArrayList list = new ArrayList();
                Iterator iter = group_list.iterator();
                while (iter.hasNext() == true) {
                    String group = (String) iter.next();
                    list.add(new LabelValueBean(group, URLEncoder.encode(group, "UTF-8")));
                }
                list.add(0, new LabelValueBean("all", URLEncoder.encode("all", "UTF-8")));
                list.add(0, new LabelValueBean("", URLEncoder.encode("", "UTF-8")));
                cform.setUserGroups(list);
            }
        } else {
            ArrayList group_list = groups.list(element.getDomain());
            if (group_list != null) {
                ArrayList list = new ArrayList();
                Iterator iter = group_list.iterator();
                while (iter.hasNext() == true) {
                    String group = (String) iter.next();
                    list.add(new LabelValueBean(group, URLEncoder.encode(group, "UTF-8")));
                }
                list.add(0, new LabelValueBean("", URLEncoder.encode("", "UTF-8")));
                cform.setUserGroups(list);
            }
        }
        WebTalkRelatedTasks menu = new WebTalkRelatedTasks();
        menu.addLink(new LinkAttribute("Administer canned messages", "display_canned_message_management"));
        request.setAttribute("menu", menu);
        return mapping.getInputForward();
    }
}
