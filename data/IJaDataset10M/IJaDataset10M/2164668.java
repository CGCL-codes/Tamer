package net.jotwiki.view;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Vector;
import net.jot.JOTInitializer;
import net.jot.logger.JOTLogger;
import net.jot.utils.JOTConstants;
import net.jot.utils.JOTHTMLUtilities;
import net.jot.utils.JOTUtilities;
import net.jot.web.JOTMainFilter;
import net.jot.web.filebrowser.JOTFileComparators;
import net.jot.web.view.JOTView;
import net.jotwiki.Constants;
import net.jotwiki.PageComment;
import net.jotwiki.PageReader;
import net.jotwiki.Version;
import net.jotwiki.WikiPreferences;
import net.jotwiki.WikiUtilities;
import net.jotwiki.db.PageOptions;
import net.jotwiki.db.WikiPermission;
import net.jotwiki.db.WikiUser;
import net.jotwiki.forms.LoginForm;

/**
 * Main view for a wiki page display.
 * @author tcolar
 */
public class ShowPageView extends JOTView {

    private int nbExtraComments = -1;

    public void prepareViewData() throws Exception {
        String title = WikiPreferences.getInstance().getDefaultedNsString(request, WikiPreferences.NS_TITLE, "My great website");
        String moto = WikiPreferences.getInstance().getDefaultedNsString(request, WikiPreferences.NS_MOTO, "If it isn't broken you are not trying !");
        String sidebar = WikiPreferences.getInstance().getDefaultedNsString(request, WikiPreferences.NS_SIDEBAR, "sidebar");
        String homePage = WikiPreferences.getInstance().getDefaultedNsString(request, WikiPreferences.NS_HOMEPAGE, "home");
        String pageName = request.getParameter(Constants.PAGE_NAME);
        PageInfos infos = (PageInfos) request.getAttribute(Constants.PAGE_INFOS);
        String nameSpace = (String) session.getAttribute(Constants.NAMESPACE);
        addVariable(Constants.SITE_MOTO, moto);
        addVariable(Constants.SITE_TITLE, title);
        addVariable(Constants.LAST_MODIF, request.getAttribute("fileTimestamp"));
        addVariable(Constants.PAGE_INFOS, infos);
        String author = infos.getOptions().getDataAuthor();
        WikiUser authorUser = (WikiUser) WikiUser.getUserByLogin(WikiUser.class, author);
        if (authorUser != null) {
            author = authorUser.getFirstName() + " " + authorUser.getLastName();
        }
        addVariable(Constants.PAGE_AUTHOR, author);
        addVariable(Constants.PAGE_NAME, pageName);
        addVariable(Constants.EDIT_PAGE_LINK, "edit.do?page=" + pageName + "&ns=" + nameSpace);
        addVariable(Constants.OPTIONS_LINK, "options.do?page=" + pageName + "&ns=" + nameSpace);
        addVariable(Constants.LOGIN_LINK, "login.do");
        addVariable(Constants.LOGOUT_LINK, "logout.do");
        addVariable(Constants.SIDEBAR, sidebar);
        addVariable(Constants.EDIT_SIDEBAR_LINK, "edit.do?page=" + sidebar + "&ns=" + nameSpace);
        addVariable(Constants.PRINT_LINK, "print.do?page=" + pageName + "&ns=" + nameSpace);
        addVariable(Constants.HOME_PAGE_LINK, homePage);
        String addThis = WikiPreferences.getInstance().getDefaultedString(WikiPreferences.GLOBAL_ADDTHIS_PUBLISHER, "jotwiki");
        if (addThis.length() < 2) {
            addThis = "jotwiki";
        }
        addVariable(Constants.ADDTHIS_PUBLISHER, addThis);
        String googleUacct = WikiPreferences.getInstance().getNsString(request, WikiPreferences.NS_GOOGLE_UACCT);
        if (googleUacct != null && googleUacct.length() > 4) {
            addVariable(Constants.GoogleUACCT, googleUacct);
        }
        WikiUser user = WikiUser.getCurrentUser(request);
        if (WikiUser.isLoggedIn(user)) {
            addVariable(LoginForm.LOGGED_USER, user);
        }
        if (WikiPermission.canAccessSetupPage(request)) {
            addVariable("canSetup", Boolean.TRUE);
        }
        if (WikiPermission.hasEditPermission(request)) {
            addVariable("canEdit", Boolean.TRUE);
        }
        if (WikiPreferences.getInstance().isIndexingEnabled(nameSpace) && WikiPermission.hasPermission(request, WikiPermission.SEARCH)) {
            addVariable("searchEnabled", Boolean.TRUE);
        }
        LinkedList trace = (LinkedList) session.getAttribute(Constants.SESSION_TRACE);
        addVariable(Constants.SESSION_TRACE, trace);
        addVariable(Constants.VERSION, Version.getVersion());
        addVariable("jotversion", JOTInitializer.VERSION);
    }

    public String getPageContent() {
        return (String) request.getParameter(Constants.PAGE_CONTENT);
    }

    public String getPageLink(String page) {
        String nameSpace = (String) session.getAttribute(Constants.NAMESPACE);
        return getRootUrl(nameSpace) + page;
    }

    public String getTraceLink(String page) {
        String nameSpace = page.substring(0, page.indexOf(":"));
        page = page.substring(page.indexOf(":") + 1, page.length());
        return getRootUrl(nameSpace) + page;
    }

    public String getTraceName(String page) {
        page = page.substring(page.indexOf(":") + 1, page.length());
        return page;
    }

    public String getPageContent(String page) {
        String ns = WikiUtilities.getNamespace(request);
        String result = "Failed to parse: " + page + " !!";
        try {
            String plainPage = PageReader.getPlainPage(request, ns, page);
            result = PageReader.getHtmlPage(plainPage);
        } catch (Exception e) {
        }
        return result;
    }

    private String getRootUrl(String nameSpace) {
        String rootUrl = "";
        if (!nameSpace.equalsIgnoreCase(WikiUtilities.getNamespace(request))) {
            rootUrl = WikiPreferences.getInstance().getString(nameSpace + "." + WikiPreferences.NS_WEBROOT);
            if (rootUrl == null) {
                String ns = nameSpace.length() == 0 ? "" : nameSpace + "/";
                rootUrl = JOTUtilities.endWithForwardSlash(JOTMainFilter.getContextName()) + ns + "/";
            }
        }
        return rootUrl;
    }

    public boolean validatePermissions() {
        return WikiPermission.hasPermission(request, WikiPermission.VIEW_PAGE);
    }

    public boolean getCommentsEnabled() {
        String page = request.getParameter(Constants.PAGE_NAME);
        try {
            PageOptions options = PageReader.getPageOptions(WikiUtilities.getNamespace(request), page);
            return options.getCommentsEnabled().booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canPostComments() {
        String page = request.getParameter(Constants.PAGE_NAME);
        try {
            PageOptions options = PageReader.getPageOptions(WikiUtilities.getNamespace(request), page);
            if (!options.getCommentsEnabled().booleanValue()) {
                return false;
            }
            if (WikiUser.isGuest(WikiUser.getCurrentUser(request)) && !options.getCommentsGuest().booleanValue()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Vector getComments() {
        nbExtraComments = -1;
        boolean allComments = request.getParameter("allComments") != null;
        String page = request.getParameter(Constants.PAGE_NAME);
        String ns = WikiUtilities.getNamespace(request);
        int nb = 5;
        boolean newestFirst = true;
        try {
            PageOptions options = PageReader.getPageOptions(ns, page);
            nb = options.getCommentsNb().intValue();
            newestFirst = WikiPreferences.getInstance().getDefaultedNsBoolean(request, WikiPreferences.NS_COMMENTS_NEWEST_FIRST, Boolean.TRUE).booleanValue();
        } catch (Exception e) {
            JOTLogger.log(JOTLogger.ERROR_LEVEL, this, "Failed to parse page options.");
        }
        if (allComments) {
            nb = 999;
        }
        File folder = new File(JOTUtilities.endWithSlash(WikiPreferences.getInstance().getCommentsFolder(ns)) + page);
        Comparator comp = newestFirst ? JOTFileComparators.NAME_DESC_COMPARATOR : JOTFileComparators.NAME_DESC_COMPARATOR;
        File[] files = folder.listFiles();
        Vector comments = new Vector();
        if (files != null && files.length > 0) {
            Arrays.sort(files, comp);
            Collection coll = Arrays.asList(files);
            Vector filesV = new Vector(coll);
            int i = 0;
            for (i = 0; i != filesV.size() && i < nb; i++) {
                try {
                    PageComment comment = PageComment.parseFromFile((File) filesV.get(i));
                    comment.setText(JOTHTMLUtilities.textToHtml(comment.getText()));
                    comment.setTitle(JOTHTMLUtilities.textToHtml(comment.getTitle()));
                    comment.setAuthor(JOTHTMLUtilities.textToHtml(comment.getAuthor()));
                    comments.add(comment);
                } catch (Exception e) {
                    JOTLogger.logException(JOTLogger.ERROR_LEVEL, this, "Failed to parse comment: ", e);
                }
            }
            if (i < filesV.size()) {
                nbExtraComments = filesV.size() - i;
            }
        }
        return comments;
    }

    public Integer getNbExtraComments() {
        return nbExtraComments <= 0 ? null : new Integer(nbExtraComments);
    }
}
