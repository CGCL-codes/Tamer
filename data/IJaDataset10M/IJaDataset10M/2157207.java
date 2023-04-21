package com.scs.base.web.sku;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import com.scs.base.model.sku.Sku;
import com.scs.base.service.sku.SkuService;

public class SkuAction extends DispatchAction {

    private static Log log = LogFactory.getLog(SkuAction.class);

    private SkuService skuService = null;

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'delete' method...");
        }
        String skuId = request.getParameter("sku.id");
        skuService.removeSku(new Long(skuId));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("user.deleted"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'edit' method...");
        }
        DynaValidatorForm userForm = (DynaValidatorForm) form;
        String skuId = request.getParameter("id");
        if (skuId != null) {
            Sku sku = skuService.getSku(new Long(skuId));
            if (sku == null) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("user.missing"));
                saveErrors(request, errors);
                return mapping.findForward("list");
            }
            userForm.set("sku", sku);
        }
        return mapping.findForward("edit");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'list' method...");
        }
        request.getSession().setAttribute("skus", skuService.getSkus());
        return mapping.findForward("list");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'cancel' method...");
        }
        request.getSession().setAttribute("skus", skuService.getSkus());
        return mapping.findForward("list");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'save' method...");
        }
        ActionMessages errors = form.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("edit");
        }
        DynaValidatorForm skuForm = (DynaValidatorForm) form;
        skuService.saveSku((Sku) skuForm.get("sku"));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sku.saved"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return list(mapping, form, request, response);
    }
}
