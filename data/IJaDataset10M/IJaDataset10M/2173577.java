package ces.coffice.webmail.ui.action;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import ces.coffice.webmail.datamodel.dao.TrackDao;
import ces.coffice.webmail.datamodel.dao.hibernate.TrackDaoHibernate;
import ces.coffice.webmail.facade.*;
import ces.coffice.webmail.util.*;

public class MailDeleteAction extends Action {

    public MailDeleteAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long userId;
        try {
            userId = new SystemFacade().getLoginUserID(request);
        } catch (Exception ex1) {
            return (mapping.findForward("success"));
        }
        request.setAttribute("messageKey", "webmail.mail.parameter.error");
        String target = "success";
        String mailIdStr = request.getParameter("ids");
        String folder = request.getParameter("boxtype");
        String strQuite = request.getParameter("quite");
        if ("true".equals(request.getParameter("clear"))) {
            EmailFacade mail = new EmailFacade(userId);
            try {
                mail.delMailByBox(folder, Long.parseLong(request.getParameter("nodeId")));
            } catch (Exception ex2) {
                ex2.printStackTrace();
                request.setAttribute("status", Constant.FAILED_TO_UP);
                request.setAttribute("messageKey", "webmail.mail.delete.failed");
                return (mapping.findForward("success"));
            }
            request.setAttribute("status", Constant.SUCCESS_TO_UP);
            request.setAttribute("messageKey", "webmail.mail.delete.success");
            return (mapping.findForward("success"));
        }
        long mailId = 0;
        try {
            if (!(mailIdStr != null && mailIdStr.length() > 0)) {
                request.setAttribute("status", Constant.FAILED_TO_UP);
                request.setAttribute("messageKey", "webmail.mail.delete.failed");
                return (mapping.findForward("success"));
            }
            StringTokenizer token = new StringTokenizer(mailIdStr, ",");
            EmailFacade mail = new EmailFacade(userId);
            while (token.hasMoreTokens()) {
                String id = token.nextToken();
                if (id != null && id.length() > 0) {
                    mailId = Long.parseLong(id);
                    if ("true".equals(strQuite)) {
                        mail.deleteMail(folder, mailId);
                    } else {
                        if (Constant.MAIL_RECYCLE.equals(folder)) {
                            mail.deleteMail(folder, mailId);
                        } else {
                            mail.removeMail(folder, mailId);
                        }
                    }
                }
                if ("sendout".equals(folder)) {
                    TrackDao dao = new TrackDaoHibernate();
                    dao.delByFormMailId(new Long(mailId));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("status", Constant.FAILED_TO_UP);
            request.setAttribute("messageKey", "webmail.mail.delete.failed");
            return (mapping.findForward("success"));
        }
        request.setAttribute("status", Constant.SUCCESS_TO_UP);
        request.setAttribute("messageKey", "webmail.mail.delete.success");
        return (mapping.findForward("success"));
    }
}
