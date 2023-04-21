package com.sitescape.team.portlet.forum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.web.servlet.ModelAndView;
import com.sitescape.team.ObjectKeys;
import com.sitescape.team.context.request.RequestContextHolder;
import com.sitescape.team.domain.Binder;
import com.sitescape.team.domain.Description;
import com.sitescape.team.domain.FolderEntry;
import com.sitescape.team.domain.Principal;
import com.sitescape.team.domain.User;
import com.sitescape.team.domain.WorkflowState;
import com.sitescape.team.module.workflow.WorkflowUtils;
import com.sitescape.team.security.AccessControlException;
import com.sitescape.team.util.NLT;
import com.sitescape.team.web.WebKeys;
import com.sitescape.team.web.portlet.SAbstractController;
import com.sitescape.team.web.util.DefinitionHelper;
import com.sitescape.team.web.util.FindIdsHelper;
import com.sitescape.team.web.util.PortletRequestUtils;
import com.sitescape.util.StringUtil;

/**
 * @author Peter Hurley
 *
 */
public class SendEntryMailController extends SAbstractController {

    public void handleActionRequestAfterValidation(ActionRequest request, ActionResponse response) throws Exception {
        Map formData = request.getParameterMap();
        Long folderId = new Long(PortletRequestUtils.getRequiredLongParameter(request, WebKeys.URL_BINDER_ID));
        Long entryId = new Long(PortletRequestUtils.getRequiredLongParameter(request, WebKeys.URL_ENTRY_ID));
        if (formData.containsKey("okBtn")) {
            String subject = PortletRequestUtils.getStringParameter(request, "subject", "");
            String[] to = StringUtil.split(PortletRequestUtils.getStringParameter(request, "addresses", ""));
            Set emailAddress = new HashSet();
            for (int i = 0; i < to.length; ++i) {
                emailAddress.add(to[i]);
            }
            boolean self = PortletRequestUtils.getBooleanParameter(request, "self", false);
            String body = PortletRequestUtils.getStringParameter(request, "mailBody", "");
            Set memberIds = new HashSet();
            if (self) memberIds.add(RequestContextHolder.getRequestContext().getUserId());
            if (formData.containsKey("users")) memberIds.addAll(FindIdsHelper.getIdsAsLongSet(request.getParameterValues("users")));
            if (formData.containsKey("groups")) memberIds.addAll(FindIdsHelper.getIdsAsLongSet(request.getParameterValues("groups")));
            List entries = null;
            if (PortletRequestUtils.getBooleanParameter(request, "attachments", false)) {
                Map folderEntries = getFolderModule().getEntryTree(folderId, entryId);
                entries = (List) folderEntries.get(ObjectKeys.FOLDER_ENTRY_DESCENDANTS);
                entries.add(0, folderEntries.get(ObjectKeys.FOLDER_ENTRY));
            }
            Map status = getAdminModule().sendMail(memberIds, emailAddress, subject, new Description(body, Description.FORMAT_HTML), entries);
            String result = (String) status.get(ObjectKeys.SENDMAIL_STATUS);
            List errors = (List) status.get(ObjectKeys.SENDMAIL_ERRORS);
            List addrs = (List) status.get(ObjectKeys.SENDMAIL_DISTRIBUTION);
            if (ObjectKeys.SENDMAIL_STATUS_SENT.equals(result)) {
                errors.add(0, NLT.get("sendMail.mailSent"));
                response.setRenderParameter(WebKeys.EMAIL_ADDRESSES, (String[]) addrs.toArray(new String[0]));
            } else if (ObjectKeys.SENDMAIL_STATUS_SCHEDULED.equals(result)) {
                errors.add(0, NLT.get("sendMail.mailQueued"));
                response.setRenderParameter(WebKeys.EMAIL_ADDRESSES, (String[]) addrs.toArray(new String[0]));
            } else {
                errors.add(0, NLT.get("sendMail.mailFailed"));
            }
            response.setRenderParameter(WebKeys.ERROR_LIST, (String[]) errors.toArray(new String[0]));
        } else if (formData.containsKey("closeBtn") || formData.containsKey("cancelBtn")) {
            response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_CLOSE_WINDOW);
        } else {
            response.setRenderParameters(formData);
        }
    }

    public ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        Map model = new HashMap();
        String[] errors = request.getParameterValues(WebKeys.ERROR_LIST);
        if (errors != null) {
            model.put(WebKeys.ERROR_LIST, errors);
            model.put(WebKeys.EMAIL_ADDRESSES, request.getParameterValues(WebKeys.EMAIL_ADDRESSES));
            return new ModelAndView(WebKeys.VIEW_BINDER_SENDMAIL, model);
        }
        Long folderId = new Long(PortletRequestUtils.getRequiredLongParameter(request, WebKeys.URL_BINDER_ID));
        Long entryId = new Long(PortletRequestUtils.getRequiredLongParameter(request, WebKeys.URL_ENTRY_ID));
        Map folderEntries = getFolderModule().getEntryTree(folderId, entryId);
        FolderEntry entry = (FolderEntry) folderEntries.get(ObjectKeys.FOLDER_ENTRY);
        Binder folder = entry.getParentFolder();
        model.put(WebKeys.FOLDER_ENTRY_DESCENDANTS, folderEntries.get(ObjectKeys.FOLDER_ENTRY_DESCENDANTS));
        model.put(WebKeys.FOLDER_ENTRY_ANCESTORS, folderEntries.get(ObjectKeys.FOLDER_ENTRY_ANCESTORS));
        model.put(WebKeys.ENTRY, entry);
        model.put(WebKeys.DEFINITION_ENTRY, entry);
        model.put(WebKeys.BINDER, folder);
        model.put(WebKeys.CONFIG_JSP_STYLE, "mail");
        if (DefinitionHelper.getDefinition(entry.getEntryDef(), model, "//item[@name='entryView']") == false) {
            DefinitionHelper.getDefaultEntryView(entry, model);
        }
        List replies = new ArrayList((List) model.get(WebKeys.FOLDER_ENTRY_DESCENDANTS));
        replies.add(entry);
        Map captionMap = new HashMap();
        for (int i = 0; i < replies.size(); i++) {
            FolderEntry reply = (FolderEntry) replies.get(i);
            Set states = reply.getWorkflowStates();
            for (Iterator iter = states.iterator(); iter.hasNext(); ) {
                WorkflowState ws = (WorkflowState) iter.next();
                captionMap.put(ws.getTokenId(), WorkflowUtils.getStateCaption(ws.getDefinition(), ws.getState()));
            }
        }
        model.put(WebKeys.WORKFLOW_CAPTIONS, captionMap);
        List userIds = PortletRequestUtils.getLongListParameters(request, WebKeys.USER_IDS_TO_ADD);
        model.put(WebKeys.USERS, getProfileModule().getUsers(new HashSet(userIds)));
        return new ModelAndView(WebKeys.VIEW_BINDER_SENDMAIL, model);
    }
}
