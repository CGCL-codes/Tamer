package org.roller.presentation.website.actions;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.roller.RollerException;
import org.roller.model.UserManager;
import org.roller.pojos.PageData;
import org.roller.pojos.RollerConfig;
import org.roller.pojos.UserData;
import org.roller.pojos.WebsiteData;
import org.roller.presentation.RollerContext;
import org.roller.presentation.RollerRequest;
import org.roller.presentation.pagecache.PageCache;
import org.roller.presentation.velocity.PreviewResourceLoader;
import org.roller.presentation.website.ThemeCache;
import org.roller.presentation.website.formbeans.ThemeEditorForm;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Actions for theme chooser page.
 * 
 * @author llavandowska
 * 
 * @struts.action name="themeEditorForm" path="/themeEditor"
 *    scope="session" parameter="method"
 * 
 * @struts.action-forward name="editTheme.page" path="/website/theme-editor.jsp"
 */
public class ThemeEditorAction extends DispatchAction {

    private static final String SESSION_TEMPLATE = "weblog.template";

    private static final String LAST_THEME = "weblog.prev.theme";

    private static Log mLogger = LogFactory.getFactory().getInstance(ThemeEditorAction.class);

    private ThemeCache themeCache = ThemeCache.getInstance();

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = mapping.findForward("editTheme.page");
        try {
            RollerRequest rreq = RollerRequest.getRollerRequest(request);
            if (rreq.isUserAuthorizedToEdit()) {
                loadThemes(rreq, errors, true);
                ThemeEditorForm teForm = (ThemeEditorForm) form;
                teForm.setThemeName(null);
                if (mLogger.isDebugEnabled()) {
                    mLogger.debug("loaded themes, form=" + teForm);
                }
                clearThemePages(rreq, (String) request.getSession(true).getAttribute(LAST_THEME));
                PageData page = getDefaultPage(rreq);
                teForm.setThemeTemplate(page.getTemplate());
            } else {
                forward = mapping.findForward("access-denied");
            }
        } catch (Exception e) {
            mLogger.error("ERROR in action", e);
            throw new ServletException(e);
        }
        return forward;
    }

    /**
	 * Load the template/theme to be previewed.  The template must be stashed
	 * in PreviewResourceLoader so that PreviewServlet can find it.
     * 
     * @param mapping Struts action mapping.
     * @param form Theme editor form bean.
     * @param request Servlet request.
     * @param response Servlet response.
     * @return Forward to edit-website page.
     * @throws IOException
     * @throws ServletException
	 */
    public ActionForward preview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = mapping.findForward("editTheme.page");
        try {
            RollerRequest rreq = RollerRequest.getRollerRequest(request);
            if (rreq.isUserAuthorizedToEdit()) {
                HttpSession session = request.getSession();
                ThemeEditorForm teForm = (ThemeEditorForm) form;
                String theme = teForm.getThemeName();
                ServletContext ctx = rreq.getServletContext();
                RollerContext rollerContext = RollerContext.getRollerContext(ctx);
                boolean showCustom = false;
                if (!"Custom".equals(theme)) {
                    String sb = this.readTheme(rollerContext, theme);
                    teForm.setThemeTemplate(sb);
                    clearThemePages(rreq, (String) session.getAttribute(LAST_THEME));
                    setThemePages(rreq, theme);
                    session.setAttribute(LAST_THEME, theme);
                } else {
                    showCustom = true;
                    clearThemePages(rreq, (String) session.getAttribute(LAST_THEME));
                    session.removeAttribute(LAST_THEME);
                }
                loadThemes(rreq, errors, showCustom);
                PageData page = getDefaultPage(rreq);
                PreviewResourceLoader.setTemplate(page.getId(), teForm.getThemeTemplate(), rreq.getUser().getUserName());
                session.setAttribute(SESSION_TEMPLATE, teForm.getThemeTemplate());
            } else {
                forward = mapping.findForward("access-denied");
            }
        } catch (Exception e) {
            mLogger.error("ERROR in action", e);
            throw new ServletException(e);
        }
        return forward;
    }

    /**
	 * Save the selected Theme or edited template as the Weblog pages template.
     * 
     * @param mapping Struts action mapping.
     * @param form Theme editor form bean.
     * @param request Servlet request.
     * @param response Servlet response.
     * @return Forward to edit-website page.
     * @throws IOException
     * @throws ServletException
	 */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = mapping.findForward("editTheme.page");
        try {
            RollerRequest rreq = RollerRequest.getRollerRequest(request);
            if (rreq.isUserAuthorizedToEdit()) {
                loadThemes(rreq, errors, true);
                ThemeEditorForm teForm = (ThemeEditorForm) form;
                String theme = teForm.getThemeName();
                ServletContext ctx = rreq.getServletContext();
                RollerContext rollerContext = RollerContext.getRollerContext(ctx);
                String template = "";
                if ("Custom".equals(theme)) {
                    template = teForm.getThemeTemplate();
                } else {
                    String sb = this.readTheme(rollerContext, theme);
                    template = sb;
                }
                UserData ud = rreq.getUser();
                PreviewResourceLoader.clearAllTemplates(ud.getUserName());
                request.getSession().removeAttribute(SESSION_TEMPLATE);
                UserManager mgr = rreq.getRoller().getUserManager();
                PageData page = getDefaultPage(rreq);
                page.setTemplate(template);
                mgr.storePage(page);
                saveThemePages(rreq, theme);
                setThemePages(rreq, theme);
                PageCache.removeFromCache(request, ud);
                teForm.setThemeName("Custom");
            } else {
                forward = mapping.findForward("access-denied");
            }
        } catch (Exception e) {
            mLogger.error("ERROR in action", e);
            throw new ServletException(e);
        }
        return forward;
    }

    /**
     * Cancel choosing of theme.
     * 
	 * @param mapping Struts action mapping.
	 * @param form Theme editor form bean.
	 * @param request Servlet request.
	 * @param response Servlet response.
	 * @return Forward to edit-website page.
	 * @throws IOException
	 * @throws ServletException
	 */
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionForward forward = mapping.findForward("editTheme");
        try {
            RollerRequest rreq = RollerRequest.getRollerRequest(request);
            if (rreq.isUserAuthorizedToEdit()) {
                UserData ud = rreq.getUser();
                PageCache.removeFromCache(request, ud);
                ThemeEditorForm teForm = (ThemeEditorForm) form;
                PreviewResourceLoader.clearAllTemplates(ud.getUserName());
                request.getSession().removeAttribute(SESSION_TEMPLATE);
                teForm.setThemeName("Custom");
            } else {
                forward = mapping.findForward("access-denied");
            }
        } catch (Exception e) {
            mLogger.error("ERROR in action", e);
            throw new ServletException(e);
        }
        return forward;
    }

    /**
	 * Load the Themes from disk ONCE per user session.
     * 
	 * @param rreq
	 * @param errors
	 */
    private void loadThemes(RollerRequest rreq, ActionErrors errors, boolean listCustom) {
        HttpSession session = rreq.getRequest().getSession(false);
        try {
            ServletContext ctx = rreq.getServletContext();
            String[] themes = null;
            if (ctx.getAttribute("themeStore") != null) {
                themes = (String[]) ctx.getAttribute("themeStore");
            } else {
                RollerContext rollerContext = RollerContext.getRollerContext(ctx);
                themes = rollerContext.getThemeNames();
                ctx.setAttribute("themeStore", themes);
            }
            if (listCustom) {
                String[] themes2 = new String[themes.length + 1];
                themes2[0] = "Custom";
                for (int i = 1; i < themes2.length; i++) {
                    themes2[i] = themes[i - 1];
                }
                themes = themes2;
            }
            session.setAttribute("themes", themes);
        } catch (Exception e) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.editing.user", e.toString()));
        }
    }

    /**
	 * Get the Default Page for the website specified by request.
     * 
	 * @param rreq
	 * @return PageData
	 */
    private PageData getDefaultPage(RollerRequest rreq) throws RollerException {
        try {
            UserData ud = rreq.getUser();
            UserManager mgr = rreq.getRoller().getUserManager();
            WebsiteData wd = mgr.getWebsite(ud.getUserName());
            String defaultPageId = wd.getDefaultPageId();
            return mgr.retrievePage(defaultPageId);
        } catch (Exception e) {
            mLogger.error("ERROR in action", e);
            throw new RollerException(e);
        }
    }

    /**
     * Loads theme into preview resource loader.
     * 
     * @param rreq
     * @param theme
     * @throws RollerException
     */
    private void setThemePages(RollerRequest rreq, String theme) throws RollerException {
        RollerContext rollerContext = RollerContext.getRollerContext(rreq.getRequest());
        try {
            UserData ud = rreq.getUser();
            UserManager mgr = rreq.getRoller().getUserManager();
            String username = ud.getUserName();
            HashMap pages = rollerContext.readThemeMacros(theme);
            Iterator iter = pages.keySet().iterator();
            while (iter.hasNext()) {
                String pageName = (String) iter.next();
                String sb = (String) pages.get(pageName);
                PageData page = mgr.getPageByName(rreq.getWebsite(), pageName);
                if (page != null) {
                    PreviewResourceLoader.setTemplate(page.getId(), sb, username);
                } else {
                    PreviewResourceLoader.setTemplate(pageName, sb, username);
                }
            }
        } catch (Exception e) {
            mLogger.error("ERROR in action", e);
            throw new RollerException(e);
        }
    }

    /**
     * Clears users preview theme from the preview resource loader.
     * 
     * @param rreq
     * @param theme
     * @throws RollerException
     */
    private void clearThemePages(RollerRequest rreq, String theme) throws RollerException {
        if (mLogger.isDebugEnabled()) {
            mLogger.debug("theme=" + theme);
        }
        if (theme == null) return;
        RollerContext rollerContext = RollerContext.getRollerContext(rreq.getRequest());
        try {
            UserData ud = rreq.getUser();
            UserManager mgr = rreq.getRoller().getUserManager();
            String username = ud.getUserName();
            String themeDir = rollerContext.getThemePath(theme);
            String[] children = RollerContext.getThemeFilenames(themeDir);
            if (children == null) return;
            for (int i = 0; i < children.length; i++) {
                String pageName = children[i].substring(0, children[i].length() - 3);
                PageData page = mgr.getPageByName(rreq.getWebsite(), pageName);
                if (page != null) {
                    PreviewResourceLoader.clearTemplate(page.getId());
                } else {
                    PreviewResourceLoader.clearTemplate(pageName);
                }
            }
        } catch (Exception e) {
            if (mLogger.isDebugEnabled()) {
                mLogger.debug("clearThemePages error: ", e);
            }
            throw new RollerException(e);
        }
    }

    /**
     * Reads theme pages from disk and saves them as pages in website of
     * the user specified by the RollerRequest. 
     * 
     * @param rreq Request wrapper.
     * @param theme Name of theme to save.
     * @throws RollerException
     */
    private void saveThemePages(RollerRequest rreq, String theme) throws RollerException {
        RollerContext rollerContext = RollerContext.getRollerContext(rreq.getRequest());
        try {
            UserData ud = rreq.getUser();
            UserManager mgr = rreq.getRoller().getUserManager();
            String username = ud.getUserName();
            WebsiteData website = mgr.getWebsite(username);
            HashMap pages = rollerContext.readThemeMacros(theme);
            Iterator iter = pages.keySet().iterator();
            while (iter.hasNext()) {
                String pageName = (String) iter.next();
                String sb = (String) pages.get(pageName);
                PageData page = mgr.getPageByName(rreq.getWebsite(), pageName);
                if (page != null) {
                    page.setTemplate(sb.toString());
                } else {
                    page = new PageData(null, website, pageName, pageName, pageName, sb, new Date());
                    mgr.storePage(page);
                }
            }
            rreq.getRoller().commit();
        } catch (Exception e) {
            mLogger.error("ERROR in action", e);
            throw new RollerException(e);
        }
    }

    /**
     * Read the 'Weblog.vm' file for a theme and return it as a String.
     * 
     * @param ctx Roller context.
     * @param theme Name of theme.
     * @return Theme in the form of a string.
     * @throws RollerException
     */
    public String readTheme(RollerContext ctx, String theme) throws RollerException {
        String fileName = "Weblog.vm";
        if (themeCache.getFromCache(theme, fileName) != null) {
            return themeCache.getFromCache(theme, fileName);
        }
        RollerConfig rollerConfig = ctx.getRollerConfig();
        String themeFile = RollerContext.getServletContext().getRealPath("/" + rollerConfig.getUserThemes() + "/" + theme + "/" + fileName);
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader rdr = new BufferedReader(new FileReader(themeFile));
            String line = null;
            while (null != (line = rdr.readLine())) {
                sb.append(line);
                sb.append("\n");
            }
            themeCache.putIntoCache(theme, fileName, sb.toString());
        } catch (Exception e) {
            System.out.println("themeFile:" + themeFile);
            throw new RollerException(e);
        }
        return sb.toString();
    }
}
