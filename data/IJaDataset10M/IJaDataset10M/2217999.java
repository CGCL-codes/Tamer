package com.ivata.cms.struts;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;
import org.sourceforge.clientsession.ClientSession;
import com.ivata.cms.Library;
import com.ivata.cms.article.ArticleConstants;
import com.ivata.cms.article.ArticleDO;
import com.ivata.cms.faq.category.FAQCategoryDO;
import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.persistence.PersistenceManager;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.StringHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.RewriteHandling;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;

/**
 * <p><code>Action</code> invoked whenever an article is
 * displayed.</p>
 *
 * @since 2003-02-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.6 $
 */
public class DisplayAction extends MaskAction {

    /**
     * Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(DisplayAction.class);

    /**
     * Size of the page context retrieved in <code>execute</code>.
     */
    private static final int PAGE_CONTEXT_SIZE = 512;

    /**
     * <copyDoc>Refer to {@link DisplayAction()}.</copyDoc>
     */
    private Library library;

    /**
     * <copyDoc>Refer to {@link DisplayAction()}.</copyDoc>
     */
    private PersistenceManager persistenceManager;

    /**
     * Construct the display action. This is usually only every called by
     * <code>PicoContainer</code>.
     *
     * @param libraryParam System library interface. Used to locate articles.
     * @param persistenceManagerParam Used to remove articles.
     * @param maskFactory {@inheritDoc}
     * @param authenticator {@inheritDoc}
     */
    public DisplayAction(final Library libraryParam, final PersistenceManager persistenceManagerParam, final MaskFactory maskFactory, final MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
        this.library = libraryParam;
        this.persistenceManager = persistenceManagerParam;
    }

    /**
     * <p>Set information to display the article type correcly.</p>
     *
     * @param articleForm the fom which should be set up fo the given
     * type.
     */
    public void chooseItemType(final ArticleForm articleForm) {
        if (logger.isDebugEnabled()) {
            logger.debug("chooseItemType(ArticleForm articleForm = " + articleForm + ") - start");
        }
        ArticleDO article = articleForm.getArticle();
        articleForm.setDeleteKey("display.alert.delete");
        Integer articleType = article.getType();
        assert (articleType != null);
        if (articleType.equals(ArticleConstants.ARTICLE_MEETING)) {
            articleForm.setThemeName("meeting");
            articleForm.setSummaryThemeName("meetingSummary");
            articleForm.setDeleteKey("display.alert.delete.isMeeting");
            articleForm.setDisplayIncludePage("/displayMeeting.jsp");
        } else if (articleType.equals(ArticleConstants.ARTICLE_NOTE)) {
            articleForm.setThemeName("note");
            articleForm.setSummaryThemeName("note");
            articleForm.setDisplayIncludePage(null);
            articleForm.setDisplayIncludePage(null);
        } else if (articleType.equals(ArticleConstants.ARTICLE_FAQ)) {
            articleForm.setDisplayIncludePage("/displayFAQ.jsp");
            articleForm.setThemeName("fAQ");
            articleForm.setSummaryThemeName("fAQ");
            articleForm.setDisplayIncludePage("/displayFAQ.jsp");
        } else {
            articleForm.setDisplayIncludePage("/displayMeeting.jsp");
            articleForm.setThemeName("document");
            articleForm.setSummaryThemeName("documentSummary");
            articleForm.setDisplayIncludePage("/displayDocument.jsp");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("chooseItemType(ArticleForm) - end");
        }
    }

    /**
     * <p>
     * Overridden to proved flow for <code>onEdit</code> and
     * <code>onPreview</code>.
     * </p>
     *
     * @param mapping {@inheritDoc}
     * @param form {@inheritDoc}
     * @param request {@inheritDoc}
     * @param response {@inheritDoc}
     * @param session {@inheritDoc}
     * @param clientSession {@inheritDoc}
     * @return &quot;displayStandalone&quot; if the request parameter
     * &quot;standalone&quot; is set, &quot;displayXDoc&quot; if the request
     * parameter &quot;XDoc&quot; is set, &quot;cmsSubmitAction&quot; if the
     * edit button has been pressed, otherwise <code>null</code>.
     * @throws SystemException If the article cannot be found.
     */
    public String execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response, final HttpSession session, final ClientSession clientSession) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("execute(ActionMapping mapping = " + mapping + ", ActionForm form = " + form + ", HttpServletRequest request = " + request + ", HttpServletResponse response = " + response + ", HttpSession session = " + session + ", ClientSession clientSession = " + clientSession + ") - start");
        }
        ArticleForm articleForm = (ArticleForm) form;
        if (!StringHandling.isNullOrEmpty(articleForm.getEdit())) {
            request.setAttribute("cmdArticleForm", articleForm);
            if (logger.isDebugEnabled()) {
                logger.debug("execute - end - return value = cmsSubmitAction");
            }
            return "cmsSubmitAction";
        }
        boolean standalone = (request.getParameter("standalone") != null);
        boolean xDoc = (request.getParameter("XDoc") != null);
        boolean print = (request.getParameter("print") != null);
        if (xDoc || standalone || print) {
            articleForm.setPreview("preview");
        }
        if (articleForm == null) {
            logger.warn("null article form passed to display action.");
            return "cmsIndex";
        }
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        findArticleFromRequest(securitySession, request, articleForm);
        ArticleDO article = articleForm.getArticle();
        MessageResources libraryResources = getResources(request, "cms");
        Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        JspFactory factory = JspFactory.getDefaultFactory();
        PageContext pageContext = factory.getPageContext(getServlet(), request, response, "", true, PAGE_CONTEXT_SIZE, true);
        session.removeAttribute("cmsArticleForm");
        chooseItemType(articleForm);
        setPageNumber("/display.action", request, response, articleForm);
        HashMap printParameters = new HashMap();
        TagUtils tagUtils = TagUtils.getInstance();
        printParameters.put("id", article.getId().toString());
        printParameters.put("print", "true");
        String pageLink1 = "";
        try {
            pageLink1 = tagUtils.computeURL(pageContext, null, null, "/display.action", null, null, printParameters, null, true);
        } catch (MalformedURLException e) {
            logger.error("Malformed URL on computing the URL for " + "page '" + "/display.action" + "', parameters '" + printParameters + "'.", e);
            throw new SystemException(e);
        }
        StringBuffer newLinks = new StringBuffer();
        if (StringHandling.isNullOrEmpty(articleForm.getPreview())) {
            newLinks.append(articleForm.getPageLinks());
            newLinks.append("<a href='");
            newLinks.append(pageLink1);
            newLinks.append("' target='_blank'><img class='printer' src='");
            newLinks.append(RewriteHandling.getContextPath(request));
            newLinks.append("/images/printer.gif' border='0' alt='");
            newLinks.append(libraryResources.getMessage(locale, "displayArticle.label.print"));
            newLinks.append("' title='");
            newLinks.append(libraryResources.getMessage(locale, "displayArticle.label.print"));
            newLinks.append("' width='32' height='32'/></a>");
        }
        articleForm.setPageLinks(newLinks.toString());
        String returnValue = null;
        if (standalone || print) {
            returnValue = "displayStandalone";
        } else if (xDoc) {
            returnValue = "displayXDoc";
        }
        if (logger.isDebugEnabled()) {
            logger.debug("execute - end - return value = " + returnValue);
        }
        return returnValue;
    }

    /**
     * Look up the current article using an id field in the request (if
     * present).
     *
     * @param securitySession Used for user authentication.
     * @param request Current request we are processing.
     * @param articleForm Form we are populating. Article will be set here.
     * @throws SystemException If the article cannot be retrieved.
     */
    protected void findArticleFromRequest(final SecuritySession securitySession, final HttpServletRequest request, final ArticleForm articleForm) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("findArticleFromRequest(SecuritySession securitySession = " + securitySession + ", HttpServletRequest request = " + request + ", ArticleForm articleForm = " + articleForm + ") - start");
        }
        Integer requestId = StringHandling.integerValue(request.getParameter("id"));
        if (requestId != null) {
            articleForm.setArticle(library.findArticleByPrimaryKey(securitySession, requestId));
            request.setAttribute("cmsArticleForm", articleForm);
            articleForm.setEdit(null);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("findArticleFromRequest - end");
        }
    }

    /**
     * Deletes the article in the current form. Called when the delete button
     * is pressed, and the user confirms.
     *
     * @param mapping {@inheritDoc}
     * @param form {@inheritDoc}
     * @param request {@inheritDoc}
     * @param response {@inheritDoc}
     * @param session {@inheritDoc}
     * @param clientSession {@inheritDoc}
     * @param defaultForward {@inheritDoc}
     * @return Always returns &quot;cmsIndexAction&quot;.
     * @throws SystemException If the article cannot be deleted for any reason.
     */
    public String onDelete(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response, final HttpSession session, final ClientSession clientSession, final String defaultForward) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("onDelete(ActionMapping mapping = " + mapping + ", ActionForm form = " + form + ", HttpServletRequest request = " + request + ", HttpServletResponse response = " + response + ", HttpSession session = " + session + ", ClientSession clientSession = " + clientSession + ", String defaultForward = " + defaultForward + ") - start");
        }
        Integer requestId = StringHandling.integerValue(request.getParameter("id"));
        assert (requestId != null) : "You must set a parameter called 'id' in the request.";
        SecuritySession securitySession = (SecuritySession) session.getAttribute("securitySession");
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            persistenceManager.remove(persistenceSession, ArticleDO.class, requestId);
        } catch (Exception e) {
            logger.error("Exception removing the article", e);
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("onDelete - end - return value = cmsIndexAction");
        }
        return "cmsIndexAction";
    }

    /**
     * Overridden to retrieve the article to be deleted.
     *
     * @param mappingParam {@inheritDoc}
     * @param formParam {@inheritDoc}
     * @param requestParam {@inheritDoc}
     * @param responseParam {@inheritDoc}
     * @param sessionParam {@inheritDoc}
     * @param defaultForwardParam {@inheritDoc}
     * @return {@inheritDoc}
     * @throws SystemException IF the article cannot be found for any reason.
     */
    public String onDeleteWarn(final ActionMapping mappingParam, final ActionForm formParam, final HttpServletRequest requestParam, final HttpServletResponse responseParam, final HttpSession sessionParam, final String defaultForwardParam) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("onDeleteWarn(ActionMapping mappingParam = " + mappingParam + ", ActionForm formParam = " + formParam + ", HttpServletRequest requestParam = " + requestParam + ", HttpServletResponse responseParam = " + responseParam + ", HttpSession sessionParam = " + sessionParam + ", String defaultForwardParam = " + defaultForwardParam + ") - start");
        }
        ArticleForm articleForm = (ArticleForm) formParam;
        SecuritySession securitySession = (SecuritySession) sessionParam.getAttribute("securitySession");
        findArticleFromRequest(securitySession, requestParam, articleForm);
        String returnString = super.onDeleteWarn(mappingParam, formParam, requestParam, responseParam, sessionParam, defaultForwardParam);
        if (logger.isDebugEnabled()) {
            logger.debug("onDeleteWarn - end - return value = " + returnString);
        }
        return returnString;
    }

    /**
     * <p>Implementation of
     * <code>setPageNumber</code> for document types.</p>
     *
     * @param pageContext Current page context we are processing. Used to
     * create links.
     * @param linkPage The page to link the page numbers to.
     * @param request Current request to check for a 'page' parameter
     * and to create links.
     * @param articleForm Form to set page number <code>displayPage</code>.
     * @throws SystemException If there is any exception creating
     * the URLs.
     */
    protected void setDocumentPageNumber(final PageContext pageContext, final String linkPage, final HttpServletRequest request, final ArticleForm articleForm) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setDocumentPageNumber(PageContext pageContext = " + pageContext + ", String linkPage = " + linkPage + ", HttpServletRequest request = " + request + ", ArticleForm articleForm = " + articleForm + ") - start");
        }
        int pageInt = articleForm.getDisplayPage();
        ArticleDO article = articleForm.getArticle();
        StringBuffer links = new StringBuffer();
        if (article.getPages().size() > 1) {
            HashMap pageLinkParameters = new HashMap();
            pageLinkParameters.put("id", article.getId().toString());
            int numberOfPages = article.getPages().size();
            for (int i = 0; i < numberOfPages; ++i) {
                if (i == pageInt) {
                    links.append("&nbsp;");
                    links.append(i + 1);
                } else {
                    pageLinkParameters.put("page", new Integer(i + 1).toString());
                    String pageLink;
                    try {
                        pageLink = TagUtils.getInstance().computeURL(pageContext, null, null, linkPage, null, null, pageLinkParameters, null, true);
                    } catch (MalformedURLException e) {
                        logger.error("Malformed URL on computing the URL for " + "page '" + linkPage + "', parameters '" + pageLinkParameters + "'.", e);
                        throw new SystemException(e);
                    }
                    links.append("&nbsp;<a href='");
                    links.append(pageLink);
                    links.append("'>");
                    links.append(i + 1);
                    links.append("</a>");
                }
            }
        }
        articleForm.setPageLinks(links.toString());
        if (logger.isDebugEnabled()) {
            logger.debug("setDocumentPageNumber - end");
        }
    }

    /**
     * <p>Implementation of
     * <code>setPageNumber</code> for FAQ types.</p>
     *
     * @param pageContext Current page context we are processing. Used to create
     * links.
     * @param linkPage The page to link the page numbers to.
     * @param request current request. Used to check for a 'page' parameter
     * and to create links.
     * @param articleForm Form to set page number <code>displayPage</code>.
     * @throws SystemException If there is any exception creating
     * the URLs.
     */
    protected void setFaqPageNumber(final PageContext pageContext, final String linkPage, final HttpServletRequest request, final ArticleForm articleForm) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setFaqPageNumber(PageContext pageContext = " + pageContext + ", String linkPage = " + linkPage + ", HttpServletRequest request = " + request + ", ArticleForm articleForm = " + articleForm + ") - start");
        }
        int pageInt = articleForm.getDisplayPage();
        ArticleDO article = articleForm.getArticle();
        MessageResources libraryResources = getResources(request, "cms");
        Locale locale = (Locale) request.getSession().getAttribute(Globals.LOCALE_KEY);
        String link;
        HashMap linkParameters = new HashMap();
        linkParameters.put("id", article.getId().toString());
        StringBuffer links = new StringBuffer();
        if (pageInt == 0) {
            links = new StringBuffer(libraryResources.getMessage(locale, "displayArticle.label.contents"));
        } else {
            linkParameters.put("page", "0");
            try {
                link = TagUtils.getInstance().computeURL(pageContext, null, null, linkPage, null, null, linkParameters, null, true);
            } catch (MalformedURLException e) {
                logger.error("Malformed URL for page '" + linkPage + "', with parameters '" + linkParameters + "'.", e);
                throw new SystemException(e);
            }
            links = new StringBuffer("<a href='");
            links.append(link);
            links.append("'>");
            links.append(libraryResources.getMessage(locale, "displayArticle.label.contents"));
            links.append("</a>");
        }
        int categoryLinkNumber = 0;
        for (Iterator i = article.getFAQCategories().iterator(); i.hasNext(); ) {
            FAQCategoryDO category = (FAQCategoryDO) i.next();
            if (++categoryLinkNumber == articleForm.getDisplayPage()) {
                links.append("&nbsp;");
                links.append(categoryLinkNumber);
            } else {
                linkParameters.put("page", new Integer(categoryLinkNumber).toString());
                try {
                    link = TagUtils.getInstance().computeURL(pageContext, null, null, linkPage, null, null, linkParameters, null, true);
                } catch (MalformedURLException e) {
                    logger.error("Malformed URL for page '" + linkPage + "', with parameters '" + linkParameters + "'.", e);
                    throw new SystemException(e);
                }
                links.append("&nbsp;<a href='");
                links.append(link);
                links.append("' title='");
                links.append(category.getName());
                links.append("'>");
                links.append(categoryLinkNumber);
                links.append("</a>");
            }
        }
        articleForm.setPageLinks(links.toString());
        if (logger.isDebugEnabled()) {
            logger.debug("setFaqPageNumber - end");
        }
    }

    /**
     * <p>Implementation of
     * <code>setPageNumber</code> for meeting types.</p>
     *
     * @param pageContext Current page context we are processing. Used to create
     * links.
     * @param linkPage The page to link the page numbers to.
     * @param request current request. Used to check for a 'page' parameter
     * and to create links.
     * @param articleForm Form to set page number <code>displayPage</code>.
     * @throws SystemException If there is any exception creating
     * the URLs.
     */
    protected void setMeetingPageNumber(final PageContext pageContext, final String linkPage, final HttpServletRequest request, final ArticleForm articleForm) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setMeetingPageNumber(PageContext pageContext = " + pageContext + ", String linkPage = " + linkPage + ", HttpServletRequest request = " + request + ", ArticleForm articleForm = " + articleForm + ") - start");
        }
        int pageInt = articleForm.getDisplayPage();
        ArticleDO article = articleForm.getArticle();
        MessageResources libraryResources = getResources(request, "cms");
        Locale locale = (Locale) request.getSession().getAttribute(Globals.LOCALE_KEY);
        String agendaLink;
        HashMap agendaLinkParameters = new HashMap();
        agendaLinkParameters.put("id", article.getId().toString());
        StringBuffer links = new StringBuffer();
        if (pageInt == 0) {
            links = new StringBuffer(libraryResources.getMessage(locale, "displayArticle.label.agenda"));
        } else {
            agendaLinkParameters.put("page", "0");
            try {
                agendaLink = TagUtils.getInstance().computeURL(pageContext, null, null, linkPage, null, null, agendaLinkParameters, null, true);
            } catch (MalformedURLException e) {
                logger.error("Malformed URL on computing the URL for " + "page '" + linkPage + "', parameters '" + agendaLinkParameters + "'.", e);
                throw new SystemException(e);
            }
            links = new StringBuffer("<a href='");
            links.append(agendaLink);
            links.append("'>");
            links.append(libraryResources.getMessage(locale, "displayArticle.label.agenda"));
            links.append("</a>");
        }
        articleForm.setPageLinks(links.toString());
        if (logger.isDebugEnabled()) {
            logger.debug("setMeetingPageNumber - end");
        }
    }

    /**
     * <p>Set the current page number from the request or form to the
     * <code>displayPage</code> attribute on the form.</p>
     *
     * <p>This method also sets the appropriate page links.</p>
     *
     * @param linkPage the page to link the page numbers to.
     * @param request current request to check for a 'page' parameter
     * and to create links.
     * @param response used to create links.
     * @param articleForm form to set page number <code>displayPage</code>.
     * @throws SystemException if there is any exception creating
     * the <code>URL</code>s.
     */
    public final void setPageNumber(final String linkPage, final HttpServletRequest request, final HttpServletResponse response, final ArticleForm articleForm) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("setPageNumber(String linkPage = " + linkPage + ", HttpServletRequest request = " + request + ", HttpServletResponse response = " + response + ", ArticleForm articleForm = " + articleForm + ") - start");
        }
        ArticleDO article = articleForm.getArticle();
        JspFactory factory = JspFactory.getDefaultFactory();
        PageContext pageContext = factory.getPageContext(getServlet(), request, response, "", true, PAGE_CONTEXT_SIZE, true);
        articleForm.setLinkPage(linkPage);
        int lastPage;
        int offset;
        Integer itemType = article.getType();
        assert (itemType != null);
        if (itemType.equals(ArticleConstants.ARTICLE_FAQ)) {
            lastPage = article.getFAQCategories().size();
            offset = 0;
        } else {
            lastPage = article.getPages().size() - 1;
            offset = 1;
        }
        int pageInt = -1;
        String requestPage = request.getParameter("page");
        if (!StringHandling.isNullOrEmpty(requestPage)) {
            try {
                pageInt = new Integer(requestPage).intValue() - offset;
            } catch (NumberFormatException e) {
                logger.error("NumberFormatException parsing the page number: ", e);
            }
        } else {
            pageInt = articleForm.getDisplayPage();
        }
        if (pageInt > lastPage) {
            pageInt = lastPage;
        }
        if (pageInt < 0) {
            pageInt = 0;
        }
        articleForm.setDisplayPage(pageInt);
        if (itemType.equals(ArticleConstants.ARTICLE_MEETING)) {
            setMeetingPageNumber(pageContext, linkPage, request, articleForm);
        } else if (itemType.equals(ArticleConstants.ARTICLE_FAQ)) {
            setFaqPageNumber(pageContext, linkPage, request, articleForm);
        } else {
            setDocumentPageNumber(pageContext, linkPage, request, articleForm);
        }
        HashMap displayDocumentParameters = new HashMap();
        StringBuffer previousPageLink = new StringBuffer();
        StringBuffer nextPageLink = new StringBuffer();
        MessageResources libraryResources = getResources(request, "cms");
        Locale locale = (Locale) request.getSession().getAttribute(Globals.LOCALE_KEY);
        if (pageInt > 0) {
            displayDocumentParameters.put("id", article.getId().toString());
            displayDocumentParameters.put("page", new Integer((pageInt + offset) - 1).toString());
            previousPageLink.append("<a href='");
            try {
                previousPageLink.append(TagUtils.getInstance().computeURL(pageContext, null, null, linkPage, null, null, displayDocumentParameters, null, true));
            } catch (MalformedURLException e) {
                logger.error("Malformed URL on computing the URL for " + "page '" + linkPage + "', parameters '" + displayDocumentParameters + "'.", e);
                throw new SystemException(e);
            }
            previousPageLink.append("'>");
            previousPageLink.append(libraryResources.getMessage(locale, "displayDocument.link.previousPage"));
            previousPageLink.append("</a>");
        }
        articleForm.setPreviousPageLink(previousPageLink.toString());
        if (pageInt < lastPage) {
            displayDocumentParameters.put("id", article.getId().toString());
            displayDocumentParameters.put("page", new Integer(pageInt + offset + 1).toString());
            nextPageLink.append("<a href='");
            try {
                nextPageLink.append(TagUtils.getInstance().computeURL(pageContext, null, null, linkPage, null, null, displayDocumentParameters, null, true));
            } catch (MalformedURLException e) {
                logger.error("Malformed URL on computing the URL for " + "page '" + linkPage + "', parameters '" + displayDocumentParameters + "'.", e);
                throw new SystemException(e);
            }
            nextPageLink.append("'>");
            nextPageLink.append(libraryResources.getMessage(locale, "displayDocument.link.nextPage"));
            nextPageLink.append("</a>");
        }
        articleForm.setNextPageLink(nextPageLink.toString());
        if (logger.isDebugEnabled()) {
            logger.debug("setPageNumber - end");
        }
    }
}
