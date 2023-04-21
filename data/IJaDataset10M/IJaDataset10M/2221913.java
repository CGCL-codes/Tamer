package com.vgkk.hula.jsp.controller;

import com.vgkk.hula.jsp.model.Model;
import com.vgkk.hula.jsp.Authorization;
import com.vgkk.hula.util.UrlUtil;
import com.vgkk.hula.i18n.PropertiesLocalizer;
import com.vgkk.hula.config.Config;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.util.Locale;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;

public abstract class Controller {

    public static final String PAGE_PERMISSION_ERROR = "/permission_err.jsp";

    public static final String PAGE_LOGIN = "/login.jsp";

    public static final String ERR_MSG_NAME = "ErrorMessage";

    public static final String STAUS_MSG_NAME = "StatusMessage";

    public static final String AUTH_NONE = "NONE";

    public static final String AUTH_LOGIN = "LOGIN";

    public static final String AUTH_ADMIN = Authorization.AUTH_ADMIN;

    public static final String AUTH_ROOT = Authorization.AUTH_ROOT;

    private static final String SESS_ATTRIB_LOGIN = "Hula_Login";

    private static final String SESS_ATTRIB_LOCALE = "Hula_Locale";

    private PageContext pageContext;

    private String errorMessage = "";

    private String statusMessage = "";

    private Set<Model> models = new HashSet<Model>();

    private RequestParams params;

    /**
     * Called when the JSP is requested from another page or when the
     * user types a URL directly into the broweser. Subcalsses can use
     * this method to initalize the state of the controler by setting
     * Models, creating objects and such.
     * 
     * <P>Although the superclass permits subclasses to throw any 
     * Exception subclass, in practice developers will wish to restrict
     * the kinds of errors that can be thrown. See the Hula Tutorial for
     * information on Exception arcihtecture and centralized exception
     * handling.
     * @param params the request parameters from this request
     * @throws Exception 
     * @author (2004-Oct-29) Tim Romero CR: Velin Doychinov
     */
    protected abstract void onExternalReference(RequestParams params) throws Exception;

    /**
     * When the JSP is requested from the same page. This almost
     * always indicates an editing command or a change or state of
     * the models that were set up on the first external reference.
     * 
     * <P>Although the superclass permits subclasses to throw any 
     * Exception subclass, in practice developers will wish to restrict
     * the kinds of errors that can be thrown. See the Hula Tutorial for
     * information on Exception arcihtecture and centralized exception
     * handling.
     * @param params the request parameters from this request
     * @throws Exception 
     * @author (2004-Oct-19) Tim Romero CR: Velin Doychinov
     */
    protected abstract void onSelfReference(RequestParams params) throws Exception;

    /**
     * Subclasses can override this method to ensure that the session
     *  has at least the specfied authorization levels before
     * the contoller is executed. By default all controlers
     * require  AUTH_LOGIN.
     *
     * <p>This method has no effect on the actual authorizatioin level
     * of the current session.
     * @return authLevel the level of authorization required
     * @author (2004-Dec-27) Tim Romero CR: Daniel Leuck
     */
    protected String getAuthorizationLevel() {
        return AUTH_LOGIN;
    }

    /**
     * Returns the last PageContext passed to the service
     * mehtod.
     * @return the most recent pageContext
     * @author (2004-Oct-29) Tim Romero CR: Velin Doychinov
     */
    protected PageContext getPageContext() {
        return pageContext;
    }

    /**
     * Makes sure the Authorization has the required permissions, and redirects
     * the session to another page if they do not.
     *
     * <P>If an authorization level of AUTH_LOGIN is set and not met
     * by the session, the user will be redirected to PAGE_LOGIN. For
     * other authrization failures the user will be redirected to
     * PAGE_PERMISSION_ERROR.
     *
     * <p>If the session has an authorization level not defined
     * in the Authorization interface, this method will return true
     * and final authenticatioin will be left up to the program.
     * @return true if the Authorization has the specified permission level
     * @author (2004-Oct-19) Tim Romero CR: Velin Doychinov
     */
    private boolean validateUserAuthorization() {
        Authorization login = getAuthorization();
        String authLevel = getAuthorizationLevel();
        if (authLevel.equals(AUTH_NONE)) {
            return true;
        } else if (login == null) {
            setRedirect(PAGE_LOGIN);
            return false;
        } else if (authLevel.equals(AUTH_LOGIN)) {
            return true;
        } else if (authLevel.equals(AUTH_ADMIN)) {
            if (login.getAuthorizationLevel().equals(Authorization.AUTH_USER)) {
                setRedirect(PAGE_PERMISSION_ERROR);
                return false;
            }
        } else if (authLevel.equals(AUTH_ROOT)) {
            if (!login.getAuthorizationLevel().equals(Authorization.AUTH_ROOT)) {
                setRedirect(PAGE_PERMISSION_ERROR);
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the locale of the current session.
     * @param locale the locale id of the current session.
     * @author (2005-Jul-11) Daniel Leuck CR: Tim Romero
     */
    public void setLocale(Locale locale) {
        pageContext.getSession().setAttribute(SESS_ATTRIB_LOCALE, locale);
    }

    /**
    * Retrieves the session locale
    * @return the session locale
    * @author (2005-Jul-11) Daniel Leuck CR: Tim Romero
    */
    public Locale getLocale() {
        Locale locale = (Locale) pageContext.getSession().getAttribute(SESS_ATTRIB_LOCALE);
        if (locale == null) {
            locale = Config.getInstance().getDefaultLocale();
        }
        return locale;
    }

    /**
     * Returns the current authorization of the session or null if it has not been set.
     * Authorizatioin is usually implemnted by a Login or User object.
     * @return the session Autorization or null
     * @author (2004-Oct-24) Tim Romero CR: Velin Doychinov
     */
    public Authorization getAuthorization() {
        return (Authorization) pageContext.getSession().getAttribute(SESS_ATTRIB_LOGIN);
    }

    /**
     * Sets the current authorization of the session. Since Authorization
     * is usually implemented by a Login or User object, this method is
     * used to login a particular user for this session.
     *
     * <p>Passing null to this method effectivly logs a user
     * out of the session.
     * @param authorization the autorization for this session.
     * @author (2004-Oct-24) Tim Romero CR: Velin Doychinov
     */
    public void setAuthorization(Authorization authorization) {
        pageContext.getSession().setAttribute(SESS_ATTRIB_LOGIN, authorization);
    }

    /**
     * Models are the primary way Controllers set information to be displayed
     * in the UI. Adding a model of the same name as one that is already set
     * by the controller will overwrite the previous one.
     * @param model the model to be displaed to the user
     * @author (2004-Oct-24) Tim Romero CR: Velin Doychinov
     */
    protected void addModel(Model model) {
        if (model.getModelName() == null) {
            throw new IllegalArgumentException("Anonymous models cannot be added to the session");
        }
        models.remove(model);
        models.add(model);
    }

    /**
     * Returns the specificed model or null if the model does not
     * exist
     * @param modelName the name of the model
     * @author (2005-Jul-25) Daniel Leuck CR: Daniel Leuck
     * 
     * ?? TODO test
     */
    protected Model getModel(String modelName) {
        for (Model model : models) {
            if (model.getModelName().equals(modelName)) {
                return model;
            }
        }
        return null;
    }

    /**                               TODO test case retriveal from properties
     * Creates a model to display a localized error message. If the message
     * is a key that can be founf in the properties file the laclaized
     * message will be displayed. Otherwise the input string will be
     * displayed as is.
     *
     * <p>Display an error message on the resulting
     * The error message is cleared before the onDisplay or onSubmit methods
     * are called. If the subclasses set an error message during the execution
     * of either of these methods.
     *
     * @param msg the localization key or the text of the message to be displayed
     * @author (2004-Oct-30) Tim Romero  CR: Velin Doychinov
     */
    public void setErrorMessage(String errorMessage) {
        String localMsg = getProperty(errorMessage);
        this.errorMessage = localMsg != null ? localMsg : errorMessage;
    }

    /**                            TODO test case retriveal from properties
      * Creates a model to display a localized status message. If the message
      * is a key that can be founf in the properties file the laclaized
      * message will be displayed. Otherwise the input string will be
      * displayed as is.
     *
     * <p>Display an error message on the resulting
     * The status message is cleared before every invocation of the controller.
     *
      * @param msg the localization key or the text of the message to be displayed
     * @author (2004-Oct-30) Tim Romero  CR: Velin Doychinov
     */
    public void setStatusMessage(String statusMessage) {
        String localMsg = getProperty(statusMessage);
        this.statusMessage = localMsg != null ? localMsg : statusMessage;
    }

    /**
     * Instructs the system to redirect to the specified url immediately
     * after execution of the onSubmot or onDisplay methods.
     * @param   url a relative or absolute URL
     * @throws  IOException if the URL is invlad or the Contoller
     * has not been initalized.
     * @author (24-Oct-2004) Tim Romero  CR: Velin Doychinov
     */
    protected void setRedirect(String url) {
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        String encodedURL = response.encodeRedirectURL(url);
        try {
            response.sendRedirect(encodedURL);
        } catch (IOException e) {
            throw new FailedRedirectException(e);
        }
    }

    /**
     * Returns a property localized into the current session language
     * @param key property name
     * @return a property localized into the current session language
     * @author (24-Oct-2004) Tim Romero  CR: Velin Doychinov
     */
    protected String getProperty(String key) {
        return PropertiesLocalizer.getInstance().getValue(getLocale(), key);
    }

    /**
     * Called before the serice method to initalize the basic parameters
     * and to update the state of any models that are in the session.
     * @param pageContext The pageContext of the JSP request
     * @throws IOException if an error occurs setting session parameters
     * @author (25-Jul-2005) Daniel Leuck  CR: ??
     */
    public synchronized void initalizeService(PageContext pageContext) throws IOException {
        this.pageContext = pageContext;
        pageContext.getRequest().setCharacterEncoding("UTF-8");
        params = new RequestParams(pageContext.getRequest());
        for (Model model : models) {
            model.updateState(params);
        }
        statusMessage = "";
        errorMessage = "";
    }

    /**
     * Returns the request parameters.
     * NOTE: Use this method in subclasses to get request parameters instead of creating
     * a new RequestParams, because if the parameters are  multipart encoded then
     * the input stream of the request can be read only once.
     * @return the request parameters.
     * @author (2005-Mar-23) Velin Doychinov  CR: Daniel Leuck
     */
    public RequestParams getRequestParams() {
        return params;
    }

    /**
	 * This method is invoked from method doStartTag() of PageTag.
	 * Validates user authorization. Adds models to the page context.
	 * 
	 * <p>Although the superclass permits subclasses to throw any 
     * Exception subclass, in practice developers will wish to restrict
     * the kinds of errors that can be thrown. See the Hula Tutorial for
     * information on Exception arcihtecture and centralized exception
     * handling.
	 * @throws Exception
     * @author (25-Jul-2005) Daniel Leuck  CR: ??
	 */
    public synchronized void service() throws Exception {
        if (!validateUserAuthorization()) {
            return;
        }
        pageContext.getResponse().setCharacterEncoding("UTF8");
        if (isSelfReference()) {
            onSelfReference(params);
        } else {
            onExternalReference(params);
        }
        Locale locale = getLocale();
        for (Model model : models) {
            model.setLocale(locale);
            pageContext.setAttribute(model.getModelName(), model);
        }
        pageContext.setAttribute(ERR_MSG_NAME, errorMessage);
        pageContext.setAttribute(STAUS_MSG_NAME, statusMessage);
    }

    private boolean isSelfReference() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String referer = request.getHeader("referer");
        if (referer == null) {
            referer = request.getParameter("hula_page_referrer");
        }
        if (referer == null) {
            return false;
        } else {
            String trimedRequstURI = UrlUtil.getPagePath(request.getRequestURI());
            return UrlUtil.getPagePath(referer).equals(trimedRequstURI);
        }
    }
}
