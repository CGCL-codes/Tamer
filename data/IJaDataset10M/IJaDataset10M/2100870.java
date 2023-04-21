package com.germinus.xpression.content_editor.action;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import com.germinus.xpression.cms.action.CMSPortletAction;

public class DisciplinesCategoryAction extends CMSPortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        DynaValidatorForm contentIdForm = (DynaValidatorForm) form;
        req.setAttribute("contentId", (String) contentIdForm.get("contentId"));
        req.setAttribute("getListItemIdHandler", (String) contentIdForm.get("getListItemIdHandler"));
        req.setAttribute("setListItemsHandler", (String) contentIdForm.get("setListItemsHandler"));
        req.setAttribute("fieldId", (String) contentIdForm.get("fieldId"));
        req.setAttribute("fieldCategory", (String) contentIdForm.get("fieldCategory"));
        return mapping.findForward("success");
    }
}
