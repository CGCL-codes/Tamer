package net.sourceforge.xconf.toolbox.spring.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * This controller implements a redirect-after-post workflow, where all form submissions result in a redirect.
 */
public class RedirectAfterSubmitFormController extends AbstractFormController {

    private String formViewName;

    private String successRedirectUrl;

    public void setFormViewName(String formViewName) {
        this.formViewName = formViewName;
    }

    public String getFormViewName() {
        return formViewName;
    }

    public void setSuccessRedirectUrl(String successRedirectUrl) {
        this.successRedirectUrl = successRedirectUrl;
    }

    public String getSuccessRedirectUrl() {
        return successRedirectUrl;
    }

    /**
     * To be overwritten by implementing classes.
     * This implementation shows the configured form view.
     *
     * @see #setFormViewName(String)
     */
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        return new ModelAndView(getFormViewName());
    }

    /**
     * This method delegates to {@link #onSubmit(HttpServletRequest, Object, BindException)} and creates a
     * {@link RedirectView} from the returned {@link Redirect}. If the delegate method returns <code>null</code>,
     * this method will also return <code>null</code>.
     */
    protected final ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Redirect redirect = onSubmit(request, command, errors);
        if (redirect == null) {
            return null;
        }
        RedirectView view = new RedirectView(redirect.getUrl(), redirect.isContextRelative());
        return new ModelAndView(view, redirect.getQueryParameters());
    }

    /**
     * To be overwritten by implementing classes.
     * This implementation delegates to {@link #onSubmit(Object, BindException)}.
     */
    protected Redirect onSubmit(HttpServletRequest request, Object command, BindException errors) throws Exception {
        return onSubmit(command, errors);
    }

    /**
     * To be overwritten by implementing classes.
     * This implementation delegates to {@link #onSubmit(Object)}.
     */
    protected Redirect onSubmit(Object command, BindException errors) throws Exception {
        return onSubmit(command);
    }

    /**
     * To be overwritten by implementing classes.
     * This implementation returns a redirect to the configured success url.
     *
     * @see #setSuccessRedirectUrl(String)
     */
    protected Redirect onSubmit(Object command) throws Exception {
        return new Redirect(getSuccessRedirectUrl());
    }
}
