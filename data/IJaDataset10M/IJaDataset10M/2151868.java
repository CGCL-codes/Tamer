package com.quikj.application.communicator.applications.webtalk.controller;

import java.sql.Connection;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.quikj.application.communicator.admin.controller.AdminConfig;
import com.quikj.application.communicator.admin.controller.LinkAttribute;
import com.quikj.application.communicator.admin.model.AccountElement;
import com.quikj.application.communicator.admin.model.AccountsTable;
import com.quikj.application.communicator.applications.webtalk.model.CannedMessageElement;
import com.quikj.application.communicator.applications.webtalk.model.CannedMessageTable;
import com.quikj.application.communicator.applications.webtalk.model.FeatureTable;
import com.quikj.application.communicator.applications.webtalk.model.GroupTable;
import com.quikj.application.communicator.applications.webtalk.model.UserTable;
import com.quikj.server.framework.AceLogger;

/**
 * 
 * @author bhm
 */
public class DisplayDropCustomerAction extends Action {

    /** Creates a new instance of DisplayUserManagementAction */
    public DisplayDropCustomerAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
        String domain = (String) request.getAttribute("domain");
        ((DynaActionForm) form).set("domain", new String(domain));
        UserTable users = new UserTable();
        users.setConnection(c);
        ArrayList operators = users.findMembersOnlyByGroupDomain(domain);
        String operators_error = users.getErrorMessage();
        ArrayList owners = users.findOwnersByGroupDomain(domain);
        String owners_error = users.getErrorMessage();
        if ((operators == null) || (owners == null)) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.customer.delete.finddata"));
            saveErrors(request, errors);
            String msg = "DisplayDropCustomerAction.execute()/by-" + element.getName() + ": Error finding data in domain " + domain + ": ";
            if (operators == null) {
                AceLogger.Instance().log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, msg + "operators - " + operators_error);
            }
            if (owners == null) {
                AceLogger.Instance().log(AceLogger.ERROR, AceLogger.SYSTEM_LOG, msg + "group owners/features - " + owners_error);
            }
            return mapping.findForward("webtalk_main_menu");
        }
        ArrayList stray_users = users.list(domain);
        if (stray_users != null) {
            ArrayList known_users = new ArrayList(operators);
            known_users.addAll(owners);
            stray_users.removeAll(known_users);
            if (stray_users.size() <= 0) {
                stray_users = null;
            }
        }
        if (operators.size() <= 0) {
            operators = null;
        }
        if (owners.size() <= 0) {
            owners = null;
        }
        GroupTable groups = new GroupTable();
        groups.setConnection(c);
        ArrayList all_groups = groups.list(domain);
        if (all_groups != null) {
            if (all_groups.size() <= 0) {
                all_groups = null;
            }
        }
        FeatureTable features = new FeatureTable();
        features.setConnection(c);
        ArrayList all_features = features.list(domain);
        if (all_features != null) {
            if (all_features.size() <= 0) {
                all_features = null;
            }
        }
        CannedMessageTable msgs = new CannedMessageTable();
        msgs.setConnection(c);
        ArrayList all_messages = msgs.search(new CannedMessageElement(), "Group", domain);
        if (all_messages != null) {
            if (all_messages.size() <= 0) {
                all_messages = null;
            }
        }
        AccountsTable accounts = new AccountsTable(AdminConfig.getInstance().getDBParams().getAdminDb());
        accounts.setConnection(c);
        ArrayList system_accounts = accounts.list(domain);
        if (system_accounts != null) {
            if (system_accounts.size() <= 0) {
                system_accounts = null;
            }
        }
        request.setAttribute("operators", operators);
        request.setAttribute("owners", owners);
        request.setAttribute("strayUsers", stray_users);
        request.setAttribute("groups", all_groups);
        request.setAttribute("features", all_features);
        request.setAttribute("cannedMessages", all_messages);
        request.setAttribute("accounts", system_accounts);
        WebTalkRelatedTasks menu = new WebTalkRelatedTasks();
        request.setAttribute("menu", menu);
        menu.addLink(new LinkAttribute("List all groups", "list_groups"));
        menu.addLink(new LinkAttribute("List all features", "list_features"));
        menu.addLink(new LinkAttribute("Search users", "display_user_search"));
        menu.addLink(new LinkAttribute("Search canned messages", "display_canned_message_search"));
        return mapping.getInputForward();
    }
}
