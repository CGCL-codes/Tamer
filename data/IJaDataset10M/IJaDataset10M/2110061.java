package org.project.trunks.user;

import org.project.trunks.utilities.*;
import java.util.*;
import javax.servlet.http.*;
import org.project.trunks.webform.*;
import org.project.trunks.exceptions.*;
import org.project.trunks.navigation.*;
import org.project.trunks.data.*;
import org.project.trunks.action.*;
import java.io.*;
import org.project.trunks.connection.*;

/**
 * <p>Title: GenericUserSession</p>
 * <p>Description: Classe de base d'un utilisateur</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: I</p>
 * @author MFR
 * @version 1.0
 */
public class GenericUserSession implements IGenericUserSession {

    /**
   * Logger
   */
    protected static Log log = new Log();

    protected String sessionID;

    protected String login, password;

    protected String lastName;

    protected String firstName;

    protected boolean isConnected = false;

    protected String rootPath;

    protected String pathGen;

    protected String ID_User;

    protected String profile;

    protected String language;

    protected Date dtLastLogin;

    protected GenericUserData userData;

    protected Translation translation;

    /**
   * Stack to manage a "back" button like a browser
  */
    protected Vector _history = null;

    protected Hashtable hsCustomParams;

    public String getID_User() {
        return ID_User;
    }

    public void setID_User(String s) {
        ID_User = s;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String s) {
        profile = s;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String s) {
        language = s;
    }

    public Translation getTranslation() {
        return translation;
    }

    /**
   * Constructeur par d�faut.
   */
    public GenericUserSession() {
        ID_User = new String("");
        setFirstName(new String(""));
        setLastName(new String(""));
        setLogin(new String(""));
        setPassword(new String(""));
        profile = new String("");
        language = new String("FR");
        translation = new Translation();
        _history = new Stack();
        hsCustomParams = new Hashtable();
    }

    /**
   * Criteria used as filter when parsing xml files
   * @return LabelValueBean[] criteria
   */
    public LabelValueBean[] getCriteria() {
        return new LabelValueBean[] { new LabelValueBean("USER", getLogin()), new LabelValueBean("PROFILE", getProfile()), new LabelValueBean("LANG", getLanguage()) };
    }

    /**
   * Shortcut to get translation
   * @param request HttpServletRequest
   * @param key String
   * @return String
   */
    public static String getTranslation(HttpServletRequest request, String key) {
        String s = null;
        GenericUserSession userSession = null;
        try {
            userSession = (GenericUserSession) SessionManager.getInstance().getSession(request);
        } catch (Exception e) {
        }
        if (userSession != null) s = (String) userSession.getTranslation().getHsTranslation().get(key);
        if (key.equals("LBL_ERROR_OCCURED")) return StringUtilities.getEString(s, "Une erreur est survenue");
        return StringUtilities.getEString(s, key);
    }

    public void initWhenMenu(HttpServletRequest request) {
        resetHistory(request);
    }

    public void resetHistory(HttpServletRequest request) {
        HistoryManager.resetHistory(_history, request);
    }

    public String pop(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return HistoryManager.pop(_history, request, response);
    }

    public WebForm getCurrentWebForm(HttpServletRequest request) {
        return HistoryManager.getCurrentWebForm(_history, request);
    }

    public IPage getCurrentPage(HttpServletRequest request) {
        return HistoryManager.getCurrentPage(_history, request);
    }

    public void gestNavigationForCurrentWebForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HistoryManager.gestNavigationForCurrentWebForm(_history, request, response);
    }

    public void gestNavigationForCurrentWebForm(WebForm webForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HistoryManager.gestNavigationForCurrentWebForm(webForm, _history, request, response);
    }

    public void push(NavigationItem item) {
        HistoryManager.push(_history, item);
    }

    public String peek(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return HistoryManager.peek(_history, request, response);
    }

    public String getHistoryTopPage() {
        return ((NavigationItem) _history.lastElement()).getIPage().getJspPage();
    }

    /**
    * Get Translation
    * Renvoie le libell� correspondant au code
    *
    * @param code Code � rechercher dans la table de hachage
    * @return Libell� correspondant au code
  */
    public String getTranslation(String code) {
        return getTranslation(code, null);
    }

    /**
    * Get Translation
    * Renvoie le libell� correspondant au code
    *
    * @param code Code � rechercher dans la table de hachage
    * @return Libell� correspondant au code
  */
    public String getTranslation(String code, String subsVal) {
        String libelle = (String) translation.getHsTranslation().get(code);
        if (libelle == null) if (subsVal == null) return code; else return subsVal;
        return libelle.replaceAll("#\\[#", "<").replaceAll("#\\]#", ">");
    }

    /**
   * gestAccess
   * @param from String
   * @throws java.lang.Exception
   */
    public void gestAccess(String from) throws Exception {
        if (!this.hasAccess()) {
            log.error("<<<< " + from + " - user : '" + this.getLogin() + "' no permission !");
            throw new Exception(from + " - user : '" + this.getLogin() + "' no permission");
        }
    }

    /**
   * hasAccess
   * @return boolean
   */
    public boolean hasAccess() {
        return true;
    }

    /**
   * hasWebFormAccess
   * @param webForm WebForm
   * @return boolean
   */
    public boolean hasWebFormAccess(HttpServletRequest request, WebForm webForm) {
        if (webForm == null || StringUtilities.getNString(webForm.getProfile()).equals("") || StringUtilities.getNString(this.profile).equals("")) return true; else {
            return StringUtilities.findIn(webForm.getProfile().split(";"), this.profile);
        }
    }

    /**
   * hasWebFormAccess
   * @param request
   * @param webForm
   * @param tRow
   * @param nrSubRow
   * @return boolean
   */
    public boolean hasWebFormAccess(HttpServletRequest request, WebForm webForm, TRow tRow, int nrSubRow) {
        return hasWebFormAccess(request, webForm);
    }

    /**
   * hasWebFormAccessRW
   * @param request HttpServletRequest
   * @param webForm WebForm
   * @return boolean
   */
    public boolean hasWebFormAccessRW(HttpServletRequest request, WebForm webForm) {
        return hasWebFormAccessRW(request, webForm, "");
    }

    public boolean hasWebFormAccessRW(HttpServletRequest request, WebForm webForm, String context) {
        if (webForm == null || StringUtilities.getNString(webForm.getProfile()).equals("") || StringUtilities.getNString(this.profile).equals("")) return true; else {
            return StringUtilities.findIn(webForm.getProfile().split(";"), this.profile);
        }
    }

    /**
   * hasWebFormAccessRW
   * @param request HttpServletRequest
   * @param webForm WebForm
   * @param tRow TRow
   * @return boolean
   */
    public boolean hasWebFormAccessRW(HttpServletRequest request, WebForm webForm, TRow tRow) {
        return hasWebFormAccessRW(request, webForm);
    }

    /**
   * hasWebFormAccessRW
   * @param request HttpServletRequest
   * @param webForm WebForm
   * @param tRow TRow
   * @return boolean
   */
    public boolean hasWebFormAccessRW(HttpServletRequest request, WebForm webForm, TRow tRow, Field field) {
        return hasWebFormAccessRW(request, webForm, tRow);
    }

    /**
   * hasWebFormAccessRW
   * @param request
   * @param webForm
   * @param tRow
   * @param nrSubRow
   * @return boolean
   */
    public boolean hasWebFormAccessRW(HttpServletRequest request, WebForm webForm, TRow tRow, int nrSubRow) {
        return hasWebFormAccessRW(request, webForm, tRow);
    }

    /**
   * hasWebFormAccessRW
   * @param request
   * @param webForm
   * @param tRow
   * @param nrSubRow
   * @param field
   * @return boolean
   */
    public boolean hasWebFormAccessRW(HttpServletRequest request, WebForm webForm, TRow tRow, int nrSubRow, Field field) {
        return hasWebFormAccessRW(request, webForm, tRow);
    }

    public void gestLanguage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestManager rm = new RequestManager(request);
        String newCODE_LANG = rm.getParameter("LANG");
        gestLanguage(request, response, newCODE_LANG);
        String cookieName = ApplicationProperties.getProperty("CODE_PROJET") + ".CODE_LANG";
        Cookie cookie = new Cookie(cookieName, newCODE_LANG);
        int maxAge = 365 * 24 * 60 * 60;
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public void gestLanguage(HttpServletRequest request, HttpServletResponse response, String code_lang) throws Exception {
        RequestManager rm = new RequestManager(request);
        String newCODE_LANG = rm.getParameter("LANG");
        if (!StringUtilities.getNString(newCODE_LANG).equals("") && !this.language.equals(newCODE_LANG)) {
            Log.info("<<<<< UserSession.gestLanguage newCODE_LANG = '" + newCODE_LANG + "'");
            setLanguage(newCODE_LANG);
            try {
                String webAppPath = request.getSession().getServletContext().getRealPath("");
                getTranslation().readXmlFile(FileUtilities.getInputSourceForFile(new File(webAppPath + File.separator + "xml" + File.separator + "translations.xml")), getLanguage());
                getTranslation().readXmlFile(FileUtilities.getInputSourceForFile(new File(webAppPath + File.separator + "xml" + File.separator + "_translations.xml")), getLanguage());
            } catch (Exception e) {
                DefaultAction.displayMessagePage(request, response, "UserSession.gestLanguage", "Une erreur est survenue... lors de la lecture du fichier de traductions");
            }
        }
    }

    public Vector getHistory() {
        return _history;
    }

    public Date getDtLastLogin() {
        return dtLastLogin;
    }

    public void setDtLastLogin(Date dtLastLogin) {
        this.dtLastLogin = dtLastLogin;
    }

    public Hashtable getHsCustomParams() {
        return hsCustomParams;
    }

    public void setHsCustomParams(Hashtable hsCustomParams) {
        this.hsCustomParams = hsCustomParams;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String s) {
        login = s;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String s) {
        password = s;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
