package net.sourceforge.fakesmtp.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.fakesmtp.controller.action.base.BaseAction;
import net.sourceforge.fakesmtp.server.ServersHandler;
import net.sourceforge.fakesmtp.view.ForwardConstants;
import net.sourceforge.fakesmtp.view.KeyConstants;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StartSMTPServerAction extends BaseAction {

    public StartSMTPServerAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int port = Integer.parseInt(request.getParameter(KeyConstants.PORT_KEY));
        ServersHandler.getInstance().getSmtpServer(port).start();
        return mapping.findForward(ForwardConstants.SMTP_STATUS_PAGE);
    }
}
