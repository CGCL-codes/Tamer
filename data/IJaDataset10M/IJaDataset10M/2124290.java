package com.germinus.xpression.content_editor.action;

import com.germinus.xpression.cms.action.CMSPortletAction;
import com.germinus.xpression.cms.model.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class ImportFileAction extends CMSPortletAction {

    private static final Log log = LogFactory.getLog(ImportFileAction.class);

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        String currentWorld = ParamUtil.getString(req, "currentWorld");
        String importType = ParamUtil.getString(req, "importType");
        String currentFolderId = ParamUtil.getString(req, "currentFolderId");
        Long contentTypeId = ContentTypes.WEB_FILES;
        if (StringUtils.isNotEmpty(importType)) {
            req.setAttribute("importType", importType);
            if (importType.equals("JClic")) {
                if (log.isDebugEnabled()) log.debug("Import type is JClic");
                contentTypeId = ContentTypes.MULTIMEDIA_ELEMENT;
            }
        } else {
            req.setAttribute("importType", "");
        }
        req.setAttribute("contentTypeId", contentTypeId);
        req.setAttribute("currentWorld", currentWorld);
        req.setAttribute("currentFolderId", currentFolderId);
        return mapping.findForward("success");
    }
}
