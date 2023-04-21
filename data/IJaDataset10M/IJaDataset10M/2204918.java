package com.liferay.portlet.addressbook.action;

import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.Constants;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.Recipient;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.util.comparator.RecipientComparator;
import com.liferay.portlet.ActionRequestImpl;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.addressbook.service.spring.ABContactServiceUtil;
import com.liferay.portlet.addressbook.service.spring.ABListServiceUtil;
import com.liferay.portlet.mail.model.Message;
import com.liferay.portlet.mail.util.MailUtil;
import com.liferay.util.JS;
import com.liferay.util.StringPool;
import com.liferay.util.StringUtil;
import com.liferay.util.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.servlet.jsp.PageContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="ComposeMessageToAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ComposeMessageToAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        try {
            PortletSession ses = req.getPortletSession();
            List recipients = new ArrayList();
            String[] ids = req.getParameterValues("contact_ids");
            for (int i = 0; ids != null && i < ids.length; i++) {
                recipients.add(ABContactServiceUtil.getContact(ids[i]));
            }
            ids = req.getParameterValues("list_ids");
            for (int i = 0; ids != null && i < ids.length; i++) {
                recipients.add(ABListServiceUtil.getList(ids[i]));
            }
            Collections.sort(recipients, new RecipientComparator());
            PortletPreferences prefs = MailUtil.getPreferences(req);
            Message msg = new Message(req, prefs);
            if (recipients.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < recipients.size(); i++) {
                    Recipient recipient = (Recipient) recipients.get(i);
                    sb.append(recipient.getRecipientInternetAddress());
                    if ((i + 1) != recipients.size()) {
                        sb.append(", ");
                    }
                }
                msg.setTo(sb.toString());
            }
            StringBuffer msgBody = new StringBuffer();
            String signature = prefs.getValue("signature", StringPool.BLANK);
            if (Validator.isNotNull(signature)) {
                signature = JS.decodeURIComponent(signature);
                if (msg.isHTMLFormat()) {
                    msgBody.append("<br><br>");
                    msgBody.append(StringUtil.replace(signature, "\n", "<br>"));
                } else {
                    msgBody.append("\n\n\n");
                    msgBody.append(signature);
                }
            }
            msg.setBody(msgBody.toString());
            ses.setAttribute(WebKeys.MAIL_MESSAGE_MODEL, msg, PortletSession.APPLICATION_SCOPE);
            List layouts = (List) req.getAttribute(WebKeys.LAYOUTS);
            String layoutId = PortalUtil.getLayoutIdWithPortletId(layouts, PortletKeys.MAIL);
            PortletURLImpl portletURLImpl = new PortletURLImpl((ActionRequestImpl) req, PortletKeys.MAIL, layoutId, true);
            portletURLImpl.setWindowState(WindowState.MAXIMIZED);
            portletURLImpl.setPortletMode(PortletMode.VIEW);
            portletURLImpl.setParameter("struts_action", "/mail/compose_message");
            res.sendRedirect(portletURLImpl.toString());
        } catch (Exception e) {
            req.setAttribute(PageContext.EXCEPTION, e);
            setForward(req, Constants.COMMON_ERROR);
        }
    }
}
