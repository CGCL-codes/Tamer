package org.kablink.teaming.portlet.profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.kablink.teaming.IllegalCharacterInNameException;
import org.kablink.teaming.NameMissingException;
import org.kablink.teaming.ObjectKeys;
import org.kablink.teaming.PasswordMismatchException;
import org.kablink.teaming.TextVerificationException;
import org.kablink.teaming.UserExistsException;
import org.kablink.teaming.UserNameMissingException;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.domain.Definition;
import org.kablink.teaming.domain.NoPrincipalByTheNameException;
import org.kablink.teaming.domain.NoUserByTheNameException;
import org.kablink.teaming.domain.ProfileBinder;
import org.kablink.teaming.domain.User;
import org.kablink.teaming.module.binder.impl.WriteEntryDataException;
import org.kablink.teaming.module.file.WriteFilesException;
import org.kablink.teaming.module.shared.InputDataAccessor;
import org.kablink.teaming.module.shared.MapInputData;
import org.kablink.teaming.portletadapter.MultipartFileSupport;
import org.kablink.teaming.portletadapter.portlet.HttpServletRequestReachable;
import org.kablink.teaming.security.AccessControlException;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.portlet.ParamsWrappedActionRequest;
import org.kablink.teaming.web.portlet.SAbstractController;
import org.kablink.teaming.web.util.BinderHelper;
import org.kablink.teaming.web.util.DefinitionHelper;
import org.kablink.teaming.web.util.GwtUIHelper;
import org.kablink.teaming.web.util.PortletRequestUtils;
import org.kablink.teaming.web.util.WebHelper;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author Peter Hurley
 *
 */
public class AddEntryController extends SAbstractController {

    public void handleActionRequestAfterValidation(ActionRequest request, ActionResponse response) throws Exception {
        Long binderId;
        Map formData = request.getParameterMap();
        response.setRenderParameters(formData);
        try {
            binderId = new Long(PortletRequestUtils.getRequiredLongParameter(request, WebKeys.URL_BINDER_ID));
        } catch (Exception ex) {
            ProfileBinder profilesBinder = getProfileModule().getProfileBinder();
            binderId = profilesBinder.getId();
        }
        String context = PortletRequestUtils.getStringParameter(request, WebKeys.URL_CONTEXT, "");
        if (formData.containsKey("okBtn") && WebHelper.isMethodPost(request)) {
            String entryType = PortletRequestUtils.getStringParameter(request, WebKeys.URL_ENTRY_TYPE, "");
            Map fileMap = null;
            if (request instanceof MultipartFileSupport) {
                fileMap = ((MultipartFileSupport) request).getFileMap();
            } else {
                fileMap = new HashMap();
            }
            MapInputData inputData = new MapInputData(formData);
            String name = inputData.getSingleValue(WebKeys.USER_PROFILE_NAME);
            if (name == null || name.equals("")) throw new UserNameMissingException();
            if (!BinderHelper.isBinderNameLegal(name)) throw new IllegalCharacterInNameException("errorcode.illegalCharacterInName");
            try {
                User user = getProfileModule().getUserDeadOrAlive(name);
                throw new UserExistsException();
            } catch (NoPrincipalByTheNameException nue) {
            } catch (NoUserByTheNameException ex) {
            }
            String password = inputData.getSingleValue(WebKeys.USER_PROFILE_PASSWORD);
            String password2 = inputData.getSingleValue(WebKeys.USER_PROFILE_PASSWORD2);
            if (password == null || password.equals("")) throw new PasswordMismatchException("errorcode.password.cannotBeNull");
            if (!password.equals(password2)) {
                throw new PasswordMismatchException("errorcode.password.mismatch");
            }
            String operation = PortletRequestUtils.getStringParameter(request, WebKeys.URL_OPERATION, "");
            if (operation.equals(WebKeys.OPERATION_RELOAD_OPENER)) {
                response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_RELOAD_OPENER);
                response.setRenderParameter(WebKeys.URL_BINDER_ID, "");
            } else {
                if (isGuestUser()) {
                    String kaptchaResponse;
                    String kaptchaExpected;
                    ActionRequest actionRequest;
                    HttpServletRequest httpServletRequest;
                    if (request instanceof ParamsWrappedActionRequest) {
                        actionRequest = ((ParamsWrappedActionRequest) request).getActionRequest();
                        if (actionRequest instanceof HttpServletRequestReachable) {
                            httpServletRequest = ((HttpServletRequestReachable) actionRequest).getHttpServletRequest();
                            kaptchaExpected = (String) httpServletRequest.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
                            kaptchaResponse = inputData.getSingleValue(WebKeys.TEXT_VERIFICATION_RESPONSE);
                            if (kaptchaExpected == null || kaptchaResponse == null || !kaptchaExpected.equalsIgnoreCase(kaptchaResponse)) {
                                throw new TextVerificationException();
                            }
                        }
                    }
                }
                User newUser = addUser(request, response, entryType, inputData, fileMap, null);
                if (GwtUIHelper.isGwtUIActive(request) && isGuestUser()) {
                    response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_CLOSE_WINDOW);
                } else {
                    if (context.equals("adminMenu")) {
                        setupShowSuccess(response, binderId, newUser.getId());
                    } else {
                        setupReloadBinder(response, binderId);
                        response.setRenderParameter(WebKeys.RELOAD_URL_FORCED, "");
                    }
                }
            }
        } else if (formData.containsKey("cancelBtn")) {
            response.setRenderParameter(WebKeys.URL_BINDER_ID, binderId.toString());
            response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_PROFILE_LISTING);
            response.setRenderParameter(WebKeys.URL_OPERATION, WebKeys.OPERATION_RELOAD_LISTING);
        }
    }

    private void setupShowSuccess(ActionResponse response, Long binderId, Long userId) {
        response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_VIEW_SUCCESS);
    }

    private void setupReloadBinder(ActionResponse response, Long folderId) {
        response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_RELOAD_BINDER);
        response.setRenderParameter(WebKeys.URL_BINDER_ID, folderId.toString());
    }

    private void setupReloadOpener(ActionResponse response, Long binderId) {
        response.setRenderParameter(WebKeys.ACTION, WebKeys.ACTION_RELOAD_OPENER);
        response.setRenderParameter(WebKeys.URL_BINDER_ID, binderId.toString());
    }

    public ModelAndView handleRenderRequestAfterValidation(RenderRequest request, RenderResponse response) throws Exception {
        String context = PortletRequestUtils.getStringParameter(request, WebKeys.URL_CONTEXT, "");
        Map model = new HashMap();
        try {
            Map folderEntryDefs = getProfileModule().getProfileBinderEntryDefsAsMap();
            String entryType = PortletRequestUtils.getStringParameter(request, WebKeys.URL_ENTRY_TYPE, "");
            if (entryType.equals("")) {
                ProfileBinder profilesBinder = getProfileModule().getProfileBinder();
                List defaultEntryDefinitions = profilesBinder.getEntryDefinitions();
                if (!defaultEntryDefinitions.isEmpty()) {
                    Definition def = (Definition) defaultEntryDefinitions.get(0);
                    entryType = def.getId();
                }
                model.put(WebKeys.FOLDER, profilesBinder);
            }
            model.put(WebKeys.ENTRY_TYPE, entryType);
            model.put(WebKeys.BINDER_ID, getProfileModule().getProfileBinderId());
            model.put(WebKeys.ENTRY_DEFINITION_MAP, folderEntryDefs);
            model.put(WebKeys.CONFIG_JSP_STYLE, Definition.JSP_STYLE_FORM);
            if (isGuestUser()) {
                model.put(WebKeys.URL_DO_TEXT_VERIFICATION, "true");
            }
            if (folderEntryDefs.containsKey(entryType)) {
                DefinitionHelper.getDefinition(getDefinitionModule().getDefinition(entryType), model, "//item[@type='form']");
            } else {
                DefinitionHelper.getDefinition((Document) null, model, "//item[@name='profileEntryForm']");
            }
        } catch (AccessControlException e) {
        }
        if (context.equals("adminMenu")) {
            return new ModelAndView(WebKeys.VIEW_ADD_USER_ACCOUNT, model);
        } else {
            return new ModelAndView(WebKeys.VIEW_ADD_ENTRY, model);
        }
    }

    /**
	 * This method will determine if the user is the guest user.
	 */
    private boolean isGuestUser() {
        boolean guestUser = false;
        if (RequestContextHolder.getRequestContext() != null) {
            User user;
            user = RequestContextHolder.getRequestContext().getUser();
            if (user != null) {
                if (ObjectKeys.GUEST_USER_INTERNALID.equals(user.getInternalId())) guestUser = true;
            }
        }
        return guestUser;
    }

    protected User addUser(ActionRequest request, ActionResponse response, String definitionId, InputDataAccessor inputData, Map fileItems, Map options) throws AccessControlException, WriteFilesException, WriteEntryDataException {
        return getProfileModule().addUser(definitionId, inputData, fileItems, options);
    }
}
