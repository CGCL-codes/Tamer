package org.kablink.teaming.portlet.administration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.kablink.teaming.ObjectKeys;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.domain.Definition;
import org.kablink.teaming.domain.User;
import org.kablink.teaming.domain.Workspace;
import org.kablink.teaming.portletadapter.MultipartFileSupport;
import org.kablink.teaming.util.AllModulesInjected;
import org.kablink.teaming.util.SZoneConfig;
import org.kablink.teaming.util.ZipEntryStream;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.portlet.SAbstractController;
import org.kablink.teaming.web.tree.TreeHelper;
import org.kablink.teaming.web.util.DefinitionHelper;
import org.kablink.teaming.web.util.PortletRequestUtils;
import org.kablink.teaming.web.util.WebHelper;
import org.kablink.util.Validator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.ModelAndView;

public class ImportDefinitionController extends SAbstractController {

    public void handleActionRequestAfterValidation(ActionRequest request, ActionResponse response) throws Exception {
        User user = RequestContextHolder.getRequestContext().getUser();
        Map formData = request.getParameterMap();
        String operation = PortletRequestUtils.getStringParameter(request, WebKeys.URL_OPERATION);
        Long binderId = null;
        try {
            binderId = PortletRequestUtils.getLongParameter(request, WebKeys.URL_BINDER_ID);
            if (binderId != null) response.setRenderParameter(WebKeys.URL_BINDER_ID, binderId.toString());
        } catch (Exception ex) {
        }
        ;
        if (formData.containsKey("okBtn") && WebKeys.OPERATION_RELOAD.equals(operation) && WebHelper.isMethodPost(request)) {
            java.util.Collection<String> ids = TreeHelper.getCheckedStringIds(formData, "id");
            if (ids.isEmpty()) {
                List currentDefinitions = new ArrayList();
                currentDefinitions = DefinitionHelper.getDefaultDefinitions(this);
                for (Definition def : (List<Definition>) currentDefinitions) ids.add(def.getId());
            }
            getAdminModule().updateDefaultDefinitions(this, RequestContextHolder.getRequestContext().getZoneId(), false, ids);
            getProfileModule().setUserProperty(user.getId(), ObjectKeys.USER_PROPERTY_UPGRADE_DEFINITIONS, "true");
            response.setRenderParameter(WebKeys.URL_ACTION, WebKeys.ACTION_MANAGE_DEFINITIONS);
        } else if (formData.containsKey("okBtn") && request instanceof MultipartFileSupport && WebHelper.isMethodPost(request)) {
            int i = 0;
            Map fileMap = ((MultipartFileSupport) request).getFileMap();
            if (fileMap != null) {
                List errors = new ArrayList();
                List<String> defs = new ArrayList();
                while (++i > 0) {
                    MultipartFile myFile = null;
                    try {
                        myFile = (MultipartFile) fileMap.get("definition" + i);
                        if (myFile == null) break;
                        if (Validator.isNull(myFile.getOriginalFilename())) continue;
                        Boolean replace = PortletRequestUtils.getBooleanParameter(request, "definition" + i + "ck", false);
                        if (myFile.getOriginalFilename().toLowerCase().endsWith(".zip")) {
                            ZipInputStream zipIn = new ZipInputStream(myFile.getInputStream());
                            ZipEntry entry = null;
                            while ((entry = zipIn.getNextEntry()) != null) {
                                defs.add(loadDefinitions(entry.getName(), new ZipEntryStream(zipIn), binderId, replace, errors));
                                zipIn.closeEntry();
                            }
                        } else {
                            defs.add(loadDefinitions(myFile.getOriginalFilename(), myFile.getInputStream(), binderId, replace, errors));
                        }
                        myFile.getInputStream().close();
                    } catch (Exception fe) {
                        errors.add((myFile == null ? "" : myFile.getOriginalFilename()) + " : " + (fe.getLocalizedMessage() == null ? fe.getMessage() : fe.getLocalizedMessage()));
                    }
                }
                for (String id : defs) {
                    if (id != null) getDefinitionModule().updateDefinitionReferences(id);
                }
                if (errors.isEmpty()) {
                    response.setRenderParameter(WebKeys.URL_ACTION, WebKeys.ACTION_MANAGE_DEFINITIONS);
                } else {
                    response.setRenderParameter(WebKeys.ERROR_LIST, (String[]) errors.toArray(new String[0]));
                }
            }
        } else if (formData.containsKey("closeBtn") || formData.containsKey("cancelBtn")) {
            response.setRenderParameter(WebKeys.URL_ACTION, WebKeys.ACTION_MANAGE_DEFINITIONS);
        } else {
            if (WebKeys.OPERATION_RELOAD_CONFIRM.equals(operation)) {
                response.setRenderParameters(formData);
            } else if (WebKeys.OPERATION_RELOAD.equals(operation)) {
                getAdminModule().updateDefaultDefinitions(RequestContextHolder.getRequestContext().getZoneId(), false);
                response.setRenderParameter(WebKeys.URL_ACTION, WebKeys.ACTION_MANAGE_DEFINITIONS);
            } else {
                response.setRenderParameters(formData);
            }
        }
    }

    protected String loadDefinitions(String fileName, InputStream fIn, Long binderId, boolean replace, List errors) {
        try {
            if (binderId == null) {
                return getDefinitionModule().addDefinition(fIn, null, null, null, replace, errors).getId();
            } else {
                return getDefinitionModule().addDefinition(fIn, getBinderModule().getBinder(binderId), null, null, replace, errors).getId();
            }
        } catch (Exception fe) {
            errors.add((fileName == null ? "" : fileName) + " : " + (fe.getLocalizedMessage() == null ? fe.getMessage() : fe.getLocalizedMessage()));
        }
        return null;
    }

    public ModelAndView handleRenderRequestAfterValidation(RenderRequest request, RenderResponse response) throws Exception {
        Long binderId = null;
        Map model = new HashMap();
        try {
            binderId = PortletRequestUtils.getLongParameter(request, WebKeys.URL_BINDER_ID);
            model.put(WebKeys.BINDER, getBinderModule().getBinder(binderId));
        } catch (Exception ex) {
        }
        ;
        String operation = PortletRequestUtils.getStringParameter(request, WebKeys.URL_OPERATION);
        if (WebKeys.OPERATION_RELOAD_CONFIRM.equals(operation)) {
            List currentDefinitions = new ArrayList();
            currentDefinitions = DefinitionHelper.getDefaultDefinitions(this);
            model.put(WebKeys.DOM_TREE, DefinitionHelper.getDefinitionTree(this, null, currentDefinitions));
            return new ModelAndView(WebKeys.VIEW_ADMIN_IMPORT_ALL_DEFINITIONS_CONFIRM, model);
        }
        model.put(WebKeys.ERROR_LIST, request.getParameterValues(WebKeys.ERROR_LIST));
        return new ModelAndView(WebKeys.VIEW_ADMIN_IMPORT_DEFINITIONS, model);
    }
}
