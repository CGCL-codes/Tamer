package com.scs.base.web.common;

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
import com.scs.base.model.common.Language;
import com.scs.base.service.common.LanguageService;

public class LanguageAction extends DispatchAction {

    private static Log log = LogFactory.getLog(LanguageAction.class);

    private LanguageService languageService = null;

    public void setLanguageService(LanguageService languageService) {
        this.languageService = languageService;
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'delete' method...");
        }
        String languageId = request.getParameter("language.id");
        languageService.removeLanguage(new Long(languageId));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("language.deleted"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'edit' method...");
        }
        DynaValidatorForm languageForm = (DynaValidatorForm) form;
        String languageId = request.getParameter("id");
        if (languageId != null) {
            Language language = languageService.getLanguage(new Long(languageId));
            if (language == null) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("language.missing"));
                saveErrors(request, errors);
                return mapping.findForward("list");
            }
            languageForm.set("language", language);
        }
        return mapping.findForward("edit");
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'list' method...");
        }
        request.setAttribute("languages", languageService.getLanguages());
        return mapping.findForward("list");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'cancel' method...");
        }
        request.setAttribute("languages", languageService.getLanguages());
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
        DynaValidatorForm languageForm = (DynaValidatorForm) form;
        languageService.saveLanguage((Language) languageForm.get("language"));
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("language.saved"));
        saveMessages(request, messages);
        return list(mapping, form, request, response);
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return list(mapping, form, request, response);
    }
}
