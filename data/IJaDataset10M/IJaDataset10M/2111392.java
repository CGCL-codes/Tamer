package org.vramework.mvc;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.vramework.commons.config.VConf;
import org.vramework.commons.datatypes.Dtu;
import org.vramework.commons.datatypes.Triple;
import org.vramework.commons.logging.ICallLevelAwareLog;
import org.vramework.commons.utils.VSert;
import org.vramework.mvc.exceptions.MvcErrors;
import org.vramework.mvc.exceptions.MvcException;
import org.vramework.mvc.web.RequestUtils;

/**
 * 
 * @author thomas.mahringer
 */
public class ActionUtils {

    private static ICallLevelAwareLog _log = VConf.getCallLevelAwareLogger(ActionUtils.class);

    /**
   * Constructs {@link ActionDef}s from the passed parameters.
   * 
   * @param relativeRequestPath
   * @param nextOkAction
   * @param nextErrorAction
   * @param method
   * @param okView
   * @param errorView
   * @param nextOkActionMethod
   * @param nextOkActionOkView
   * @param nextOkActionErrorView
   * @param nextErrorActionMethod
   * @param nextErrorActionOkView
   * @param nextErrorActionErrorView
   * @param messages
   * @param errorHandling
   * @return The action defs.
   */
    public static Triple<ActionDef, ActionDef, ActionDef> createActionDefs(String relativeRequestPath, String nextOkAction, String nextErrorAction, String method, String okView, String errorView, String nextOkActionMethod, String nextOkActionOkView, String nextOkActionErrorView, String nextErrorActionMethod, String nextErrorActionOkView, String nextErrorActionErrorView, List<UserMessage> messages, boolean errorHandling) {
        _log.enter("createActionDefs");
        _log.incCallLevel();
        try {
            if (errorHandling) {
                method = "display";
                nextOkAction = null;
                nextErrorAction = null;
            }
            if (nextOkAction != null && (okView != null)) {
                throw new MvcException(MvcErrors.MustNotSpecifyNextActionAndView);
            }
            if (nextErrorAction != null && (errorView != null)) {
                throw new MvcException(MvcErrors.MustNotSpecifyNextActionAndView);
            }
            if (nextOkAction == null && (nextOkActionMethod != null || nextOkActionOkView != null || nextOkActionErrorView != null)) {
                throw new MvcException(MvcErrors.MustNotSpecifyNextActionAttributesWithoutNextAction);
            }
            if (nextErrorAction == null && (nextErrorActionMethod != null || nextErrorActionOkView != null || nextErrorActionErrorView != null)) {
                throw new MvcException(MvcErrors.MustNotSpecifyNextActionAttributesWithoutNextAction);
            }
            ActionDef actionDef = null;
            ActionDef nextOkActionDef = null;
            ActionDef nextErrorActionDef = null;
            if (method == null) {
                method = "display";
            }
            if (nextOkAction == null) {
                if (okView == null) {
                    okView = "same";
                }
                if (okView.equals("same")) {
                    okView = relativeRequestPath;
                }
            }
            if (nextErrorAction == null) {
                if (errorView == null) {
                    errorView = "same";
                }
                if (errorView.equals("same")) {
                    errorView = relativeRequestPath;
                }
            }
            actionDef = new ActionDef(relativeRequestPath, method, okView, errorView, messages);
            if (_log.isTraceEnabled()) {
                _log.trace("action", new Object[] { "actionDef: ", actionDef.toString() });
            }
            if (nextOkAction != null) {
                if (nextOkActionMethod == null) {
                    nextOkActionMethod = "display";
                }
                if (nextOkActionOkView == null) {
                    nextOkActionOkView = "same";
                }
                if (nextOkActionErrorView == null) {
                    nextOkActionErrorView = "same";
                }
                if (nextOkActionOkView.equals("same")) {
                    nextOkActionOkView = nextOkAction;
                }
                if (nextOkActionErrorView.equals("same")) {
                    nextOkActionErrorView = nextOkAction;
                }
                nextOkActionDef = new ActionDef(nextOkAction, nextOkActionMethod, nextOkActionOkView, nextOkActionErrorView, messages);
                if (_log.isTraceEnabled()) {
                    _log.trace("action", new Object[] { "nextOkActionDef: ", nextOkActionDef.toString() });
                }
            }
            if (nextErrorAction != null) {
                if (nextErrorActionMethod == null) {
                    nextErrorActionMethod = "display";
                }
                if (nextErrorActionOkView == null) {
                    nextErrorActionOkView = "same";
                }
                if (nextErrorActionErrorView == null) {
                    nextErrorActionErrorView = "same";
                }
                if (nextErrorActionOkView.equals("same")) {
                    nextErrorActionOkView = nextErrorAction;
                }
                if (nextErrorActionErrorView.equals("same")) {
                    nextErrorActionErrorView = nextErrorAction;
                }
                nextErrorActionDef = new ActionDef(nextErrorAction, nextErrorActionMethod, nextErrorActionOkView, nextErrorActionErrorView, messages);
                if (_log.isTraceEnabled()) {
                    _log.trace("action", new Object[] { "nextErrorActionDef: ", nextErrorActionDef.toString() });
                }
            }
            Triple<ActionDef, ActionDef, ActionDef> actionDefs = new Triple<ActionDef, ActionDef, ActionDef>(actionDef, nextOkActionDef, nextErrorActionDef);
            return actionDefs;
        } finally {
            _log.decCallLevel();
        }
    }

    /**
   * Constructs {@link ActionDef}s from the passed parameters.
   * 
   * @param relativeRequestPath
   * @param params
   * @param messages
   * @param errorHandling
   * @return The action defs.
   */
    public static Triple<ActionDef, ActionDef, ActionDef> createActionDefs(String relativeRequestPath, Map<String, String[]> params, List<UserMessage> messages, boolean errorHandling) {
        _log.enter("createActionDefs");
        _log.incCallLevel();
        try {
            String method = RequestUtils.extractFirstIndexedParameter(params, "method", false, false);
            String okView = RequestUtils.extractFirstIndexedParameter(params, "okView", false, false);
            String errorView = RequestUtils.extractFirstIndexedParameter(params, "errorView", false, false);
            String nextOkAction = RequestUtils.extractFirstIndexedParameter(params, "nextOkAction", false, false);
            String nextOkActionMethod = RequestUtils.extractFirstIndexedParameter(params, "nextOkActionMethod", false, false);
            String nextOkActionOkView = RequestUtils.extractFirstIndexedParameter(params, "nextOkActionOkView", false, false);
            String nextOkActionErrorView = RequestUtils.extractFirstIndexedParameter(params, "nextOkActionErrorView", false, false);
            String nextErrorAction = RequestUtils.extractFirstIndexedParameter(params, "nextErrorAction", false, false);
            String nextErrorActionMethod = RequestUtils.extractFirstIndexedParameter(params, "nextErrorActionMethod", false, false);
            String nextErrorActionOkView = RequestUtils.extractFirstIndexedParameter(params, "nextErrorActionOkView", false, false);
            String nextErrorActionErrorView = RequestUtils.extractFirstIndexedParameter(params, "nextErrorActionErrorView", false, false);
            return createActionDefs(relativeRequestPath, nextOkAction, nextErrorAction, method, okView, errorView, nextOkActionMethod, nextOkActionOkView, nextOkActionErrorView, nextErrorActionMethod, nextErrorActionOkView, nextErrorActionErrorView, messages, errorHandling);
        } finally {
            _log.decCallLevel();
        }
    }

    /**
   * Constructs {@link ActionDef}s from the passed parameters for multipart requests.
   * 
   * @param relativeRequestPath
   * @param multiPartItems
   * @param errorHandling
   * @return The action defs.
   */
    public static Triple<ActionDef, ActionDef, ActionDef> createActionDefs(String relativeRequestPath, List<FileItem> multiPartItems, boolean errorHandling) {
        _log.enter("createActionDefs");
        _log.incCallLevel();
        try {
            String nextOkAction = getFormFieldValue(multiPartItems, "nextOkAction");
            String nextErrorAction = getFormFieldValue(multiPartItems, "nextErrorAction");
            String method = getFormFieldValue(multiPartItems, "method");
            String okView = getFormFieldValue(multiPartItems, "okView");
            String errorView = getFormFieldValue(multiPartItems, "errorView");
            String nextOkActionMethod = getFormFieldValue(multiPartItems, "nextOkActionMethod");
            String nextOkActionOkView = getFormFieldValue(multiPartItems, "nextOkActionOkView");
            String nextOkActionErrorView = getFormFieldValue(multiPartItems, "nextOkActionErrorView");
            String nextErrorActionMethod = getFormFieldValue(multiPartItems, "nextErrorActionMethod");
            String nextErrorActionOkView = getFormFieldValue(multiPartItems, "nextErrorActionOkView");
            String nextErrorActionErrorView = getFormFieldValue(multiPartItems, "nextErrorActionErrorView");
            return createActionDefs(relativeRequestPath, nextOkAction, nextErrorAction, method, okView, errorView, nextOkActionMethod, nextOkActionOkView, nextOkActionErrorView, nextErrorActionMethod, nextErrorActionOkView, nextErrorActionErrorView, null, errorHandling);
        } finally {
            _log.decCallLevel();
        }
    }

    /**
   * Handles the upload of multipart file requests.
   * 
   * @param action
   * @param maxBytes
   * @param additionalData
   * @return The uploaded files.
   */
    public static List<FileItem> handleFileUpload(IAction action, long maxBytes, Object additionalData) {
        if (additionalData == null || !(additionalData instanceof HttpServletRequest)) {
            throw new MvcException(MvcErrors.ActionWrongAdditionalDataType, new Object[] { action.getClass().getSimpleName(), HttpServletRequest.class, additionalData });
        }
        HttpServletRequest req = (HttpServletRequest) additionalData;
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            String type = req.getContentType();
            throw new MvcException(MvcErrors.ActionWrongMimeTypeType, new Object[] { "multipart", action.getClass().getSimpleName(), type });
        }
        _log.debug("handleFileUpload", new Object[] { "Uploading file(s)" });
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(10000001);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxBytes);
        List<FileItem> uploadedFiles;
        try {
            uploadedFiles = Dtu.cast(upload.parseRequest(req));
            if (_log.isTraceEnabled()) {
                for (FileItem fileItem : uploadedFiles) {
                    if (fileItem.isFormField()) {
                        String str = fileItem.getString();
                        _log.trace("handleFileUpload", new Object[] { "Form field: ", fileItem.getFieldName(), ", ", str });
                    } else {
                        _log.trace("handleFileUpload", new Object[] { "No form field: ", fileItem.getFieldName() });
                    }
                }
            }
        } catch (FileUploadException e) {
            throw new MvcException(e);
        }
        return uploadedFiles;
    }

    /**
   * Retrieves a form field's value from the passed multi part items.
   * 
   * @param multiPartItems
   * @param fieldName
   * @return The value or null if field not found.
   */
    public static String getFormFieldValue(List<FileItem> multiPartItems, String fieldName) {
        VSert.argNotNull("multiPartItems", multiPartItems);
        VSert.argNotEmpty("fieldName", fieldName);
        for (FileItem fileItem : multiPartItems) {
            if (fileItem.isFormField()) {
                if (fileItem.getFieldName().equalsIgnoreCase(fieldName)) {
                    return fileItem.getString();
                }
            }
        }
        return null;
    }
}
