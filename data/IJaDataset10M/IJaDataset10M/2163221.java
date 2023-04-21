package org.kablink.teaming.portlet.binder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.kablink.teaming.ObjectKeys;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.Description;
import org.kablink.teaming.domain.Principal;
import org.kablink.teaming.domain.User;
import org.kablink.teaming.module.mail.MailSentStatus;
import org.kablink.teaming.module.shared.AccessUtils;
import org.kablink.teaming.security.AccessControlException;
import org.kablink.teaming.security.function.WorkArea;
import org.kablink.teaming.util.LongIdUtil;
import org.kablink.teaming.util.NLT;
import org.kablink.teaming.util.Utils;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.portlet.SAbstractController;
import org.kablink.teaming.web.util.MiscUtil;
import org.kablink.teaming.web.util.PermaLinkUtil;
import org.kablink.teaming.web.util.PortletRequestUtils;
import org.kablink.teaming.web.util.WebHelper;
import org.kablink.util.StringUtil;
import org.springframework.web.portlet.ModelAndView;
import javax.mail.Address;

/**
 * @author Janet McCann
 *
 */
public class SendMailController extends SAbstractController {

    @SuppressWarnings("unchecked")
    public void handleActionRequestAfterValidation(ActionRequest request, ActionResponse response) throws Exception {
        Map formData = request.getParameterMap();
        User user = RequestContextHolder.getRequestContext().getUser();
        if (formData.containsKey("okBtn") && WebHelper.isMethodPost(request) && !ObjectKeys.GUEST_USER_INTERNALID.equals(user.getInternalId())) {
            String subject = PortletRequestUtils.getStringParameter(request, "subject", "");
            String[] to = StringUtil.split(PortletRequestUtils.getStringParameter(request, "addresses", ""));
            Set emailAddress = new HashSet();
            for (int i = 0; i < to.length; ++i) {
                emailAddress.add(to[i]);
            }
            String bccEmailAddress = user.getBccEmailAddress();
            if (bccEmailAddress != null && !bccEmailAddress.equals("")) {
                if (!emailAddress.contains(bccEmailAddress.trim())) {
                    emailAddress.add(bccEmailAddress.trim());
                }
            }
            boolean self = PortletRequestUtils.getBooleanParameter(request, "self", false);
            String body = PortletRequestUtils.getStringParameter(request, "mailBody", "");
            Set memberIds = new HashSet();
            if (self) memberIds.add(RequestContextHolder.getRequestContext().getUserId());
            if (formData.containsKey("users")) memberIds.addAll(LongIdUtil.getIdsAsLongSet(request.getParameterValues("users")));
            if (formData.containsKey("groups")) memberIds.addAll(LongIdUtil.getIdsAsLongSet(request.getParameterValues("groups")));
            Set ccIds = new HashSet();
            if (formData.containsKey("ccusers")) ccIds.addAll(LongIdUtil.getIdsAsLongSet(request.getParameterValues("ccusers")));
            if (formData.containsKey("ccgroups")) ccIds.addAll(LongIdUtil.getIdsAsLongSet(request.getParameterValues("ccgroups")));
            Set bccIds = new HashSet();
            if (formData.containsKey("bccusers")) bccIds.addAll(LongIdUtil.getIdsAsLongSet(request.getParameterValues("bccusers")));
            if (formData.containsKey("bccgroups")) bccIds.addAll(LongIdUtil.getIdsAsLongSet(request.getParameterValues("bccgroups")));
            List<String> noAccessPrincipals = new ArrayList();
            String binderId = PortletRequestUtils.getStringParameter(request, WebKeys.URL_BINDER_ID, "");
            if (!binderId.equals("")) {
                Binder binder = getBinderModule().getBinder(Long.valueOf(binderId));
                Set totalIds = new HashSet();
                totalIds.addAll(memberIds);
                totalIds.addAll(ccIds);
                totalIds.addAll(bccIds);
                Set<Principal> totalUsers = getProfileModule().getPrincipals(totalIds);
                for (Principal p : totalUsers) {
                    if (p instanceof User) {
                        try {
                            AccessUtils.readCheck((User) p, (WorkArea) binder);
                        } catch (AccessControlException e) {
                            noAccessPrincipals.add(Utils.getUserTitle(p) + " (" + p.getName() + ")");
                        }
                    }
                }
            }
            Map status = getAdminModule().sendMail(memberIds, null, emailAddress, ccIds, bccIds, subject, new Description(body, Description.FORMAT_HTML));
            MailSentStatus result = (MailSentStatus) status.get(ObjectKeys.SENDMAIL_STATUS);
            if (result != null) {
                response.setRenderParameter(WebKeys.EMAIL_SENT_ADDRESSES, getStringEmail(result.getSentTo()));
                response.setRenderParameter(WebKeys.EMAIL_QUEUED_ADDRESSES, getStringEmail(result.getQueuedToSend()));
                response.setRenderParameter(WebKeys.EMAIL_FAILED_ADDRESSES, getStringEmail(result.getFailedToSend()));
            }
            response.setRenderParameter(WebKeys.EMAIL_FAILED_ACCESS, noAccessPrincipals.toArray(new String[noAccessPrincipals.size()]));
            List errors = (List) status.get(ObjectKeys.SENDMAIL_ERRORS);
            response.setRenderParameter(WebKeys.ERROR_LIST, (String[]) errors.toArray(new String[0]));
            if (formData.containsKey(WebKeys.URL_SEND_MAIL_LOCATION)) response.setRenderParameter(WebKeys.URL_SEND_MAIL_LOCATION, request.getParameter(WebKeys.URL_SEND_MAIL_LOCATION));
            if (formData.containsKey(WebKeys.USER_IDS_TO_ADD)) response.setRenderParameter(WebKeys.USER_IDS_TO_ADD, request.getParameter(WebKeys.USER_IDS_TO_ADD));
        } else if (formData.containsKey("closeBtn") || formData.containsKey("cancelBtn")) {
            response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_CLOSE_WINDOW);
        } else {
            response.setRenderParameters(formData);
        }
    }

    private String[] getStringEmail(Collection<Address> addrs) {
        String addresses[] = new String[addrs.size()];
        int i = 0;
        for (Address email : addrs) addresses[i++] = email.toString();
        return addresses;
    }

    @SuppressWarnings("unchecked")
    public ModelAndView handleRenderRequestAfterValidation(RenderRequest request, RenderResponse response) throws Exception {
        User user = RequestContextHolder.getRequestContext().getUser();
        String[] errors = request.getParameterValues(WebKeys.ERROR_LIST);
        Map model = new HashMap();
        if (ObjectKeys.GUEST_USER_INTERNALID.equals(user.getInternalId())) {
            model.put(WebKeys.ERROR_MESSAGE, NLT.get("errorcode.operation.denied.sessionTimeout"));
            return new ModelAndView(WebKeys.VIEW_ERROR_RETURN, model);
        }
        List userIds = MiscUtil.splitUserIds(request, WebKeys.USER_IDS_TO_ADD, model);
        if (errors != null) {
            if (Utils.canUserOnlySeeCommonGroupMembers()) {
                model.put(WebKeys.ERROR_LIST, errors);
                model.put(WebKeys.EMAIL_SENT_ADDRESSES_COUNT, String.valueOf(request.getParameterValues(WebKeys.EMAIL_SENT_ADDRESSES).length));
                model.put(WebKeys.EMAIL_QUEUED_ADDRESSES_COUNT, String.valueOf(request.getParameterValues(WebKeys.EMAIL_QUEUED_ADDRESSES).length));
                model.put(WebKeys.EMAIL_FAILED_ADDRESSES_COUNT, String.valueOf(request.getParameterValues(WebKeys.EMAIL_FAILED_ADDRESSES).length));
                model.put(WebKeys.EMAIL_FAILED_ACCESS_COUNT, String.valueOf(request.getParameterValues(WebKeys.EMAIL_FAILED_ACCESS).length));
                model.put(WebKeys.URL_SEND_MAIL_LOCATION, request.getParameter(WebKeys.URL_SEND_MAIL_LOCATION));
            } else {
                model.put(WebKeys.ERROR_LIST, errors);
                model.put(WebKeys.EMAIL_SENT_ADDRESSES, request.getParameterValues(WebKeys.EMAIL_SENT_ADDRESSES));
                model.put(WebKeys.EMAIL_QUEUED_ADDRESSES, request.getParameterValues(WebKeys.EMAIL_QUEUED_ADDRESSES));
                model.put(WebKeys.EMAIL_FAILED_ADDRESSES, request.getParameterValues(WebKeys.EMAIL_FAILED_ADDRESSES));
                model.put(WebKeys.EMAIL_FAILED_ACCESS, request.getParameterValues(WebKeys.EMAIL_FAILED_ACCESS));
                model.put(WebKeys.URL_SEND_MAIL_LOCATION, request.getParameter(WebKeys.URL_SEND_MAIL_LOCATION));
            }
            return new ModelAndView(WebKeys.VIEW_SENDMAIL_RESULT, model);
        }
        Long binderId = PortletRequestUtils.getLongParameter(request, WebKeys.URL_BINDER_ID);
        Boolean appendTeamMembers = PortletRequestUtils.getBooleanParameter(request, WebKeys.URL_APPEND_TEAM_MEMBERS, false);
        if (binderId != null) {
            Binder binder = getBinderModule().getBinder(binderId);
            model.put(WebKeys.BINDER, binder);
            model.put("body", "<a href=\"" + PermaLinkUtil.getPermalink(request, binder) + "\">" + binder.getTitle() + "</a>");
        }
        Set users = new HashSet();
        users.addAll(getProfileModule().getUsers(new HashSet(userIds)));
        model.put(WebKeys.USERS, users);
        model.put(WebKeys.URL_APPEND_TEAM_MEMBERS, appendTeamMembers);
        return new ModelAndView(WebKeys.VIEW_BINDER_SENDMAIL, model);
    }
}
