package net.sf.bootstrap.framework.web.action;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p>Allows the user to change the display language of the web application.
 * </p>
 *
 * @author Mark Moloney
 *
 * @struts.action path="/changeLanguage" scope="request"
 *  validate="false"
 */
public class ChangeLanguageAction extends Action {

    private final Log log = LogFactory.getLog(getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String lang = request.getParameter("lang");
        log.info("Setting language: " + lang);
        Locale locale = new Locale(lang, "NZ");
        setLocale(request, locale);
        HttpSession session = request.getSession();
        session.setAttribute(Globals.LOCALE_KEY, locale);
        Config.set(session, Config.FMT_LOCALE, locale);
        return mapping.findForward("home");
    }
}
