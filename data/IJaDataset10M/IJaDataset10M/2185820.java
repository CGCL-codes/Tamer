package com.germinus.xpression.content_editor.action;

import com.germinus.xpression.cms.contents.ContentManager;
import com.germinus.xpression.cms.directory.DirectoryFolder;
import com.germinus.xpression.cms.directory.DirectoryPersister;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.groupware.Authorizator;
import com.germinus.xpression.groupware.CommunityManager;
import com.germinus.xpression.groupware.GroupwareUser;
import com.germinus.xpression.groupware.communities.Community;
import com.germinus.xpression.groupware.util.GroupwareManagerRegistry;
import java.util.Iterator;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import com.germinus.xpression.cms.CMSRuntimeException;
import com.germinus.xpression.cms.CMSUser;
import com.germinus.xpression.cms.action.CMSPortletAction;
import com.germinus.xpression.cms.contents.Content;
import com.germinus.xpression.cms.contents.ContentTypeDefinitions;
import com.germinus.xpression.cms.contents.DynaI18NCMSData;
import com.germinus.xpression.cms.contents.FieldDefinition;
import com.germinus.xpression.cms.contents.FieldGroupDefinition;
import com.germinus.xpression.cms.contents.FieldSetDefinition;
import com.germinus.xpression.cms.model.ContentTypes;
import com.germinus.xpression.cms.worlds.World;
import com.germinus.xpression.content_editor.ContentEditorRequestHelper;
import com.germinus.xpression.content_editor.form.ContentValidatorForm;
import com.germinus.xpression.content_editor.model.EditorAttributes;
import com.germinus.xpression.groupware.NotAuthorizedException;
import com.germinus.xpression.i18n.I18NUtils;

public class CreateContent extends ContentEditorAction {

    private static final Log log = LogFactory.getLog(CreateContent.class);

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        Long fieldSetId = new Long(req.getParameter(EditorAttributes.FIELD_SET_ID));
        EditContent.prepareEdition(getGroupwareUser(req), req, getContentId(req), null, fieldSetId);
        return mapping.findForward(SUCCESS);
    }

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        ActionMessages messages = new ActionMessages();
        ActionMessages errors = new ActionErrors();
        ContentValidatorForm contentForm = (ContentValidatorForm) form;
        GroupwareUser groupwareUser = getGroupwareUser(req);
        ContentEditorRequestHelper requestHelper = new ContentEditorRequestHelper(req);
        try {
            String currentFolderId = calculateCurrentFolderId(req);
            log.debug("Saving content in folder id : " + currentFolderId);
            Content content = contentForm.getContent();
            content.setName(content.getContentData().getName());
            content.setAuthorId(((com.liferay.portal.model.User) req.getAttribute("USER")).getScreenName());
            ContentTypeDefinitions defs = ContentTypes.getDefinitions();
            Long fieldSetId = new Long(req.getParameter(EditorAttributes.FIELD_SET_ID));
            FieldSetDefinition fieldSet = defs.getContentTypeDefinition(content.getContentTypeId()).getFieldSetDefinition(fieldSetId);
            Iterator fieldGroupIterator = fieldSet.getFieldGroups().iterator();
            while (fieldGroupIterator.hasNext()) {
                Iterator fieldIterator = ((FieldGroupDefinition) fieldGroupIterator.next()).getFields().iterator();
                while (fieldIterator.hasNext()) {
                    FieldDefinition fieldDefinition = (FieldDefinition) fieldIterator.next();
                    if (fieldDefinition.isCategoryField()) {
                        populateCategory(content, fieldDefinition, req);
                    }
                }
                Authorizator authorizator = GroupwareManagerRegistry.getAuthorizator();
                CommunityManager communityManager = GroupwareManagerRegistry.getCommunityManager();
                World world = requestHelper.getCurrentWorld();
                content.setAuthorId(groupwareUser.getId());
                Community community = communityManager.getOwnerCommunity(world);
                authorizator.assertAdminAuthorization(groupwareUser, community);
                ContentManager contentManager = ManagerRegistry.getContentManager();
                DirectoryPersister directoryPersister = ManagerRegistry.getDirectoryPersister();
                DirectoryFolder parentFolder = (DirectoryFolder) directoryPersister.getItemByUUIDWorkspace(currentFolderId, null);
                content = contentManager.saveAsDraftContent(content, world, parentFolder);
                log.debug("Created content with id: " + content.getId());
                setContentId(req, content.getId());
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("content.creation.successful"));
                req.setAttribute(CURRENT_FOLDER_ID, currentFolderId);
            }
        } catch (NotAuthorizedException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.creation-not-authorized"));
        } catch (CMSRuntimeException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.creation"));
            log.error("Unexpected error when user " + groupwareUser.getId() + " tried to create " + "a new content", e);
        } finally {
            saveMessages(req, messages);
            saveErrors(req, errors);
        }
    }

    private void setContentId(ActionRequest actionRequest, String contentId) {
        actionRequest.setAttribute(EditorAttributes.CONTENT_ID, contentId);
    }

    private String getContentId(RenderRequest req) {
        return (String) req.getAttribute(EditorAttributes.CONTENT_ID);
    }

    private void populateCategory(Content content, FieldDefinition fd, ActionRequest req) {
        final String threadLocale = I18NUtils.getThreadLocale();
        String[] categoryIds = req.getParameterValues(fd.getId());
        content.removeCategoriesByTopParent(fd.getCategory().getName());
        if (categoryIds != null) {
            addCategories(categoryIds, content);
        } else if (fd.getInternacionalizable().booleanValue()) {
            Object value;
            try {
                value = PropertyUtils.getProperty(content, fd.getId());
                DynaI18NCMSData dynaData = (DynaI18NCMSData) content.getContentData();
                Map localizedMap = dynaData.getI18NMap().buildLocalizedMap(threadLocale);
                localizedMap.put(fd.getId(), value);
            } catch (Exception e) {
                log.warn("Unable to obtain internacionalizable field " + fd.getId() + ". " + e);
            }
            return;
        }
    }

    private void addCategories(String[] categoryIds, Content content) {
        for (int i = 0; i < categoryIds.length; i++) {
            log.debug("Adding category '" + categoryIds[i] + "' to content " + content.getName());
            addCategory(categoryIds[i], content);
        }
    }

    private void addCategory(String categoryId, Content content) throws CMSRuntimeException {
        if (StringUtils.isEmpty(categoryId)) {
            log.warn("Cannot add an empty category. The form is not correct.");
            return;
        }
        content.addCategory(categoryId);
    }
}
