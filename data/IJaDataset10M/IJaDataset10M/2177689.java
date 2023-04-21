package org.kablink.teaming.portlet.binder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.Definition;
import org.kablink.teaming.domain.NoUserByTheNameException;
import org.kablink.teaming.domain.SimpleName;
import org.kablink.teaming.domain.TemplateBinder;
import org.kablink.teaming.domain.User;
import org.kablink.teaming.domain.EntityIdentifier.EntityType;
import org.kablink.teaming.module.admin.AdminModule.AdminOperation;
import org.kablink.teaming.module.binder.BinderModule.BinderOperation;
import org.kablink.teaming.smtp.SMTPManager;
import org.kablink.teaming.util.AllModulesInjected;
import org.kablink.teaming.util.SPropsUtil;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.util.BinderHelper;
import org.kablink.teaming.web.util.DefinitionHelper;
import org.kablink.teaming.web.util.FixupFolderDefsException;
import org.kablink.teaming.web.util.PortletRequestUtils;
import org.kablink.teaming.web.util.WebHelper;
import org.kablink.teaming.web.util.WebUrlUtil;
import org.kablink.util.Validator;
import org.springframework.web.portlet.ModelAndView;

/**
 *
 */
public class ConfigureController extends AbstractBinderController {

    private SMTPManager smtpService;

    public void setSmtpService(SMTPManager smtpService) {
        this.smtpService = smtpService;
    }

    public SMTPManager getSmtpService() {
        return smtpService;
    }

    public void handleActionRequestAfterValidation(ActionRequest request, ActionResponse response) throws Exception {
        User user = RequestContextHolder.getRequestContext().getUser();
        Map formData = request.getParameterMap();
        Long binderId = new Long(PortletRequestUtils.getRequiredLongParameter(request, WebKeys.URL_BINDER_ID));
        Binder binder = getBinderModule().getBinder(binderId);
        String binderType = PortletRequestUtils.getRequiredStringParameter(request, WebKeys.URL_BINDER_TYPE);
        response.setRenderParameters(request.getParameterMap());
        if (formData.containsKey("okBtn") && WebHelper.isMethodPost(request)) {
            List definitions = new ArrayList();
            Map workflowAssociations = new HashMap();
            getDefinitions(request, definitions, workflowAssociations);
            getBinderModule().setDefinitions(binderId, definitions, workflowAssociations);
            response.setRenderParameter(WebKeys.URL_BINDER_ID, binderId.toString());
        } else if (formData.containsKey("updateEmailButton") && WebHelper.isMethodPost(request)) {
            getBinderModule().setPostingEnabled(binderId, formData.containsKey("allow_simple_email"));
        } else if (formData.containsKey("addUrlBtn") && WebHelper.isMethodPost(request)) {
            String[] globalKeywords = SPropsUtil.getStringArray("simpleUrl.globalKeywords", ",");
            String prefix = PortletRequestUtils.getStringParameter(request, "prefix", "").trim();
            boolean prefixIsGlobalKeyword = false;
            for (int i = 0; i < globalKeywords.length; i++) {
                if (globalKeywords[i].trim().equals(prefix)) {
                    prefixIsGlobalKeyword = true;
                    break;
                }
            }
            if (!prefix.equals("") && !prefixIsGlobalKeyword && !prefix.toLowerCase().equals(user.getUrlSafeName().toLowerCase())) {
                response.setRenderParameter(WebKeys.SIMPLE_URL_NAME_NOT_ALLOWED_ERROR, "true");
                return;
            }
            String name = PortletRequestUtils.getStringParameter(request, "name", "").trim();
            if (name.startsWith("/")) {
                response.setRenderParameter(WebKeys.SIMPLE_URL_NAME_NOT_ALLOWED_ERROR, "true");
                return;
            }
            if (name.indexOf("/") >= 0) {
                String n1 = name.substring(0, name.indexOf("/"));
                User u = null;
                try {
                    u = getProfileModule().findUserByName(n1);
                } catch (NoUserByTheNameException e) {
                }
                if (!n1.equals("") && u != null && !n1.toLowerCase().equals(user.getName().toLowerCase()) && !getAdminModule().testAccess(AdminOperation.manageFunction)) {
                    response.setRenderParameter(WebKeys.SIMPLE_URL_NAME_NOT_ALLOWED_ERROR, "true");
                    return;
                }
            }
            if (!name.equals("")) {
                if (!prefix.equals("")) {
                    name = prefix + "/" + name;
                } else if (prefix.equals("") && !getAdminModule().testAccess(AdminOperation.manageFunction)) {
                    name = user.getName() + "/" + name;
                }
                Pattern p = Pattern.compile("^([a-z0-9$_.+!*'(),-]*)/?([a-z0-9$_.+!*'(),-]*)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(name);
                if (!m.find()) {
                    response.setRenderParameter(WebKeys.SIMPLE_URL_INVALID_CHARACTERS, "true");
                    return;
                }
                SimpleName simpleUrl = getBinderModule().getSimpleName(name);
                if (simpleUrl == null) {
                    getBinderModule().addSimpleName(name, binderId, binder.getEntityType().name());
                } else if (!simpleUrl.getBinderId().equals(binderId)) {
                    response.setRenderParameter(WebKeys.SIMPLE_URL_NAME_EXISTS_ERROR, "true");
                }
            } else {
                if (prefix.toLowerCase().equals(user.getUrlSafeName().toLowerCase())) {
                    SimpleName simpleUrl = getBinderModule().getSimpleName(prefix);
                    if (simpleUrl == null) {
                        getBinderModule().addSimpleName(prefix, binderId, binder.getEntityType().name());
                    } else if (simpleUrl.getBinderId().equals(binderId)) {
                        response.setRenderParameter(WebKeys.SIMPLE_URL_NAME_EXISTS_ERROR, "true");
                    } else {
                        response.setRenderParameter(WebKeys.SIMPLE_URL_NAME_NOT_ALLOWED_ERROR, "true");
                    }
                } else {
                    response.setRenderParameter(WebKeys.SIMPLE_URL_NAME_NOT_ALLOWED_ERROR, "true");
                }
            }
        } else if (formData.containsKey("deleteUrlBtn") && WebHelper.isMethodPost(request)) {
            Set<String> deleteNames = new HashSet();
            for (Iterator iter = formData.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry e = (Map.Entry) iter.next();
                String key = (String) e.getKey();
                if (key.startsWith("delete_")) {
                    deleteNames.add(key.substring(7));
                }
            }
            for (String urlName : deleteNames) {
                SimpleName simpleName = getBinderModule().getSimpleName(urlName);
                if (simpleName != null && simpleName.getBinderId().equals(binderId)) if (getBinderModule().testAccess(binder, BinderOperation.manageSimpleName)) getBinderModule().deleteSimpleName(urlName);
            }
        } else if (formData.containsKey("inheritanceBtn") && WebHelper.isMethodPost(request)) {
            boolean inherit = PortletRequestUtils.getBooleanParameter(request, "inherit", false);
            getBinderModule().setDefinitionsInherited(binderId, inherit);
            response.setRenderParameter(WebKeys.URL_BINDER_ID, binderId.toString());
        } else if (formData.containsKey("folderDefinitionFixupsBtn") && WebHelper.isMethodPost(request)) {
            boolean folderFixups = PortletRequestUtils.getBooleanParameter(request, "folderFixups", false);
            boolean entryFixups = PortletRequestUtils.getBooleanParameter(request, "entryFixups", false);
            if (folderFixups || entryFixups) {
                String entryDefinition = null;
                if (entryFixups) {
                    entryDefinition = PortletRequestUtils.getStringParameter(request, "entryFixupDefinitions");
                }
                try {
                    BinderHelper.fixupFolderAndEntryDefinitions(request, this, binderId, folderFixups, entryFixups, entryDefinition);
                } catch (FixupFolderDefsException ffde) {
                    FixupFolderDefsException.FixupExceptionCause fec = ffde.getExceptionCause();
                    String cause;
                    if (FixupFolderDefsException.FixupExceptionCause.EXCEPTION_PREVIOUS_THREAD_BUSY == fec) cause = "busy"; else if (FixupFolderDefsException.FixupExceptionCause.EXCEPTION_CANT_CREATE_THREAD == fec) cause = "cantStart"; else cause = "unknown";
                    response.setRenderParameter(WebKeys.FIXUP_THREAD_STATUS, cause);
                }
            }
        } else if (formData.containsKey("cancelBtn") || formData.containsKey("closeBtn")) {
            if (binder instanceof TemplateBinder) {
                response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_CONFIGURATION);
                response.setRenderParameter(WebKeys.URL_BINDER_ID, binderId.toString());
            } else setupViewBinder(response, binderId, binderType);
        } else response.setRenderParameters(formData);
    }

    public ModelAndView handleRenderRequestAfterValidation(RenderRequest request, RenderResponse response) throws Exception {
        Map model = new HashMap();
        User user = RequestContextHolder.getRequestContext().getUser();
        model.put(WebKeys.USER_PRINCIPAL, user);
        model.put(WebKeys.USER_PROPERTIES, getProfileModule().getUserProperties(user.getId()));
        String op = PortletRequestUtils.getStringParameter(request, WebKeys.URL_OPERATION, "");
        model.put(WebKeys.OPERATION, op);
        Long binderId = PortletRequestUtils.getLongParameter(request, WebKeys.URL_BINDER_ID);
        if (binderId != null) {
            Binder binder = getBinderModule().getBinder(binderId);
            model.put(WebKeys.BINDER, binder);
            setupDefinitions(binder, model);
            model.put(WebKeys.DEFINITION_ENTRY, binder);
            BinderHelper.buildNavigationLinkBeans(this, binder, model);
            BinderHelper.buildSimpleUrlBeans(this, request, binder, model);
            List<SimpleName> simpleNames = (List) model.get(WebKeys.SIMPLE_URL_NAMES);
            model.put(WebKeys.SIMPLE_EMAIL_ENABLED, getSmtpService().isEnabled() && binder.getEntityType().equals(EntityType.folder) && simpleNames.size() > 0);
        }
        String fixFolderDefsStatus = PortletRequestUtils.getStringParameter(request, WebKeys.FIXUP_THREAD_STATUS);
        if ((null == fixFolderDefsStatus) || (0 == fixFolderDefsStatus.length())) {
            if (BinderHelper.isFolderFixupReady(request)) {
                fixFolderDefsStatus = "ready";
            } else if (BinderHelper.isFolderFixupInProgress(request)) {
                fixFolderDefsStatus = "running";
            }
        }
        model.put(WebKeys.FIXUP_THREAD_STATUS, fixFolderDefsStatus);
        model.put(WebKeys.SIMPLE_URL_NAME_EXISTS_ERROR, PortletRequestUtils.getStringParameter(request, WebKeys.SIMPLE_URL_NAME_EXISTS_ERROR));
        model.put(WebKeys.SIMPLE_URL_NAME_NOT_ALLOWED_ERROR, PortletRequestUtils.getStringParameter(request, WebKeys.SIMPLE_URL_NAME_NOT_ALLOWED_ERROR));
        model.put(WebKeys.SIMPLE_URL_INVALID_CHARACTERS, PortletRequestUtils.getStringParameter(request, WebKeys.SIMPLE_URL_INVALID_CHARACTERS));
        return new ModelAndView(WebKeys.VIEW_CONFIGURE, model);
    }

    protected void getDefinitions(ActionRequest request, List definitions, Map workflowAssociations) {
        String defBinderId = PortletRequestUtils.getStringParameter(request, "binderDefinition", "");
        String[] defBinderIds = PortletRequestUtils.getStringParameters(request, "binderDefinitions");
        if (!Validator.isNull(defBinderId)) {
            if (defBinderIds != null) {
                definitions.add(defBinderId);
            }
        }
        if (defBinderIds != null) {
            for (int i = 0; i < defBinderIds.length; i++) {
                String defId = defBinderIds[i];
                if (!Validator.isNull(defId) && !defId.toString().equals(defBinderId.toString())) {
                    definitions.add(defId);
                }
            }
        }
        defBinderIds = PortletRequestUtils.getStringParameters(request, "workflowDefinition");
        if (defBinderIds != null) {
            for (int i = 0; i < defBinderIds.length; i++) {
                String defId = defBinderIds[i];
                if (!Validator.isNull(defId)) {
                    definitions.add(defId);
                }
            }
        }
        String[] defEntryIds = PortletRequestUtils.getStringParameters(request, "entryDefinition");
        Map tempEntryDefMap = new HashMap<String, Definition>();
        for (int i = 0; i < defEntryIds.length; i++) {
            tempEntryDefMap.put(defEntryIds[i], DefinitionHelper.getDefinition(defEntryIds[i]));
        }
        Map replyDefMap = DefinitionHelper.getReplyDefinitions(tempEntryDefMap.values());
        Object[] replyIdsArray = replyDefMap.keySet().toArray();
        if (defEntryIds != null) {
            for (int i = 0; i < defEntryIds.length; i++) {
                String defId = defEntryIds[i];
                if (!Validator.isNull(defId)) {
                    definitions.add(defId);
                    String wfDefId = PortletRequestUtils.getStringParameter(request, "workflow_" + defId, "");
                    if (!wfDefId.equals("")) workflowAssociations.put(defId, wfDefId);
                }
            }
        }
        if (replyIdsArray != null) {
            for (int i = 0; i < replyIdsArray.length; i++) {
                String defId = (String) replyIdsArray[i];
                if (!Validator.isNull(defId)) {
                    String wfDefId = PortletRequestUtils.getStringParameter(request, "workflow_" + defId, "");
                    if (!wfDefId.equals("")) workflowAssociations.put(defId, wfDefId);
                }
            }
        }
    }

    protected void setupDefinitions(Binder binder, Map model) {
        model.put(WebKeys.BINDER, binder);
        model.put(WebKeys.CONFIG_JSP_STYLE, Definition.JSP_STYLE_VIEW);
        EntityType binderType = binder.getEntityType();
        if (binderType.equals(EntityType.workspace)) {
            if ((binder.getDefinitionType() != null) && (binder.getDefinitionType().intValue() == Definition.USER_WORKSPACE_VIEW)) {
                model.put(WebKeys.ALL_BINDER_DEFINITIONS, DefinitionHelper.getAvailableDefinitions(null, Definition.USER_WORKSPACE_VIEW));
            } else {
                model.put(WebKeys.ALL_BINDER_DEFINITIONS, DefinitionHelper.getAvailableDefinitions(binder.getId(), Definition.WORKSPACE_VIEW));
            }
        } else if (binderType.equals(EntityType.profiles)) {
            model.put(WebKeys.ALL_BINDER_DEFINITIONS, DefinitionHelper.getAvailableDefinitions(null, Definition.PROFILE_VIEW));
            model.put(WebKeys.ALL_ENTRY_DEFINITIONS, DefinitionHelper.getAvailableDefinitions(null, Definition.PROFILE_ENTRY_VIEW));
        } else {
            model.put(WebKeys.ALL_BINDER_DEFINITIONS, DefinitionHelper.getAvailableDefinitions(binder.getId(), Definition.FOLDER_VIEW));
            model.put(WebKeys.ALL_ENTRY_DEFINITIONS, DefinitionHelper.getAvailableDefinitions(binder.getId(), Definition.FOLDER_ENTRY));
            model.put(WebKeys.ALL_WORKFLOW_DEFINITIONS, DefinitionHelper.getAvailableDefinitions(binder.getId(), Definition.WORKFLOW));
            model.put(WebKeys.REPLY_DEFINITION_MAP, DefinitionHelper.getReplyDefinitions(binder.getEntryDefinitions()));
        }
    }
}
