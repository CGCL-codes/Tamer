package org.thirdway.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

/**
 * Basic interface for wizard form controllers.
 */
public abstract class ThirdwayWizardFormController extends AbstractWizardFormController {

    /** Comment for <code>failForwardView</code> */
    String failForwardView = "";

    /** Comment for <code>successForwardView</code> */
    String successForwardView = "";

    /** Comment for <code>cancelForwardView</code> */
    String cancelForwardView = "";

    /**
     * @param failForwardView
     */
    public void setFailForwardView(String failForwardView) {
        this.failForwardView = failForwardView;
    }

    /**
     * @param successForwardView
     */
    public void setSuccessForwardView(String successForwardView) {
        this.successForwardView = successForwardView;
    }

    /**
     * @return String
     */
    public String getSuccessForwardView() {
        return this.successForwardView;
    }

    /**
     * @param cancelForwardView
     */
    public void setCancelForwardView(String cancelForwardView) {
        this.cancelForwardView = cancelForwardView;
    }

    /**
     * @return String
     */
    public String getCancelForwardView() {
        return this.cancelForwardView;
    }

    /**
     * Kills a user's session.
     * 
     * @param pRequest
     */
    void killSession(HttpServletRequest pRequest) {
        pRequest.getSession().removeAttribute("userSession");
        pRequest.getSession().invalidate();
    }

    /**
     * Method disallows duplicate form submission. Typically used to prevent
     * duplicate insertion of <code>Entity</code>s into the datastore. Shows
     * a new form with an error message.
     * 
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView disallowDuplicateFormSubmission(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BindException errors = new BindException(formBackingObject(request), getCommandName());
        errors.reject("duplicateFormSubmission", "Duplicate form submission");
        return showForm(request, response, errors);
    }
}
