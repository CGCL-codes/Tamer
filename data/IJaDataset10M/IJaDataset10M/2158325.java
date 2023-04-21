package org.dspace.app.webui.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.browse.Browse;
import org.dspace.browse.BrowseInfo;
import org.dspace.browse.BrowseScope;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.handle.HandleManager;

/**
 * Servlet for browsing through indices. This can be used to browse authors,
 * items by date, or items by title. In the deployment description, the initial
 * parameter "browse" should be set to one of these values:
 * <p>
 * <ul>
 * <lI><code>titles</code>- for browsing items by title (the default)</li>
 * <lI><code>authors</code>- for browsing authors</li>
 * <lI><code>dates</code>- for browsing items by date</li>
 * </ul>
 * <p>
 * Hence there should be three instances of this servlet, one for each type of
 * browse.
 * 
 * @author Robert Tansley
 * @version $Revision: 1624 $
 */
public class BrowseServlet extends DSpaceServlet {

    /** log4j category */
    private static Logger log = Logger.getLogger(BrowseServlet.class);

    /** Is this servlet for browsing authors? */
    private boolean browseAuthors;

    /** Is this servlet for browsing items by title? */
    private boolean browseTitles;

    /** Is this servlet for browsing items by date? */
    private boolean browseDates;

    /** Is this servlet for browsing items by subject? */
    private boolean browseSubjects;

    public void init() {
        String browseWhat = getInitParameter("browse");
        browseAuthors = ((browseWhat != null) && browseWhat.equalsIgnoreCase("authors"));
        browseDates = ((browseWhat != null) && browseWhat.equalsIgnoreCase("dates"));
        browseSubjects = ((browseWhat != null) && browseWhat.equalsIgnoreCase("subjects"));
        browseTitles = ((!browseAuthors && !browseDates) && !browseSubjects);
    }

    protected void doDSGet(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        BrowseScope scope = new BrowseScope(context);
        boolean highlight = false;
        String logInfo = "";
        String flipOrderingQuery = "";
        String focus = request.getParameter("focus");
        String startsWith = request.getParameter("starts_with");
        String top = request.getParameter("top");
        String bottom = request.getParameter("bottom");
        String month = request.getParameter("month");
        String year = request.getParameter("year");
        String order = request.getParameter("order");
        boolean oldestFirst = false;
        if ((order != null) && order.equalsIgnoreCase("oldestfirst")) {
            oldestFirst = true;
        }
        if (browseDates && (year != null) && !year.equals("") && ((startsWith == null) || startsWith.equals(""))) {
            startsWith = year;
            if ((month != null) & !month.equals("-1")) {
                if (month.length() == 1) {
                    month = "0" + month;
                }
                startsWith = year + "-" + month;
            }
        }
        if (focus != null) {
            if (browseAuthors || browseSubjects) {
                scope.setFocus(focus);
            } else {
                Item item = (Item) HandleManager.resolveToObject(context, focus);
                if (item == null) {
                    JSPManager.showInvalidIDError(request, response, focus, Constants.ITEM);
                    return;
                }
                scope.setFocus(item);
            }
            highlight = true;
            logInfo = "focus=" + focus + ",";
            if (browseDates) {
                flipOrderingQuery = "focus=" + URLEncoder.encode(focus, Constants.DEFAULT_ENCODING) + "&amp;";
            }
        } else if (startsWith != null) {
            if (browseDates) {
                flipOrderingQuery = "starts_with=" + URLEncoder.encode(startsWith, Constants.DEFAULT_ENCODING) + "&amp;";
                if (!oldestFirst) {
                    startsWith = startsWith + "-32";
                }
            }
            scope.setFocus(startsWith);
            highlight = true;
            logInfo = "starts_with=" + startsWith + ",";
        } else if ((top != null) || (bottom != null)) {
            String val = bottom;
            boolean isTop = false;
            if (top != null) {
                val = top;
                isTop = true;
            }
            if (browseAuthors || browseSubjects) {
                scope.setFocus(val);
            } else {
                Item item = (Item) HandleManager.resolveToObject(context, val);
                if (item == null) {
                    JSPManager.showInvalidIDError(request, response, focus, Constants.ITEM);
                    return;
                }
                scope.setFocus(item);
            }
            scope.setNumberBefore(isTop ? 0 : 20);
            logInfo = (isTop ? "top" : "bottom") + "=" + val + ",";
            if (browseDates) {
                if (top != null) {
                    flipOrderingQuery = "bottom=" + URLEncoder.encode(top, Constants.DEFAULT_ENCODING) + "&amp;";
                } else {
                    flipOrderingQuery = "top=" + URLEncoder.encode(bottom, Constants.DEFAULT_ENCODING) + "&amp;";
                }
            }
        }
        Community community = UIUtil.getCommunityLocation(request);
        Collection collection = UIUtil.getCollectionLocation(request);
        if (collection != null) {
            logInfo = logInfo + ",collection_id=" + collection.getID() + ",";
            scope.setScope(collection);
        } else if (community != null) {
            logInfo = logInfo + ",community_id=" + community.getID() + ",";
            scope.setScope(community);
        }
        BrowseInfo browseInfo;
        try {
            if (browseAuthors) {
                browseInfo = Browse.getAuthors(scope);
            } else if (browseDates) {
                browseInfo = Browse.getItemsByDate(scope, oldestFirst);
            } else if (browseSubjects) {
                browseInfo = Browse.getSubjects(scope);
            } else {
                browseInfo = Browse.getItemsByTitle(scope);
            }
        } catch (SQLException sqle) {
            JSPManager.showIntegrityError(request, response);
            return;
        }
        String what = "title";
        if (browseAuthors) {
            what = "author";
        } else if (browseSubjects) {
            what = "subject";
        } else if (browseDates) {
            what = "date";
        }
        log.info(LogManager.getHeader(context, "browse_" + what, logInfo + "results=" + browseInfo.getResultCount()));
        if (browseInfo.getResultCount() == 0) {
            request.setAttribute("community", community);
            request.setAttribute("collection", collection);
            JSPManager.showJSP(request, response, "/browse/no-results.jsp");
        } else {
            if (!browseInfo.isFirst()) {
                String s;
                if (browseAuthors || browseSubjects) {
                    s = (browseInfo.getStringResults())[0];
                } else {
                    Item firstItem = (browseInfo.getItemResults())[0];
                    s = firstItem.getHandle();
                }
                if (browseDates && oldestFirst) {
                    request.setAttribute("previous.query", "order=oldestfirst&amp;bottom=" + URLEncoder.encode(s, Constants.DEFAULT_ENCODING));
                } else {
                    request.setAttribute("previous.query", "bottom=" + URLEncoder.encode(s, Constants.DEFAULT_ENCODING));
                }
            }
            if (!browseInfo.isLast()) {
                String s;
                if (browseAuthors) {
                    String[] authors = browseInfo.getStringResults();
                    s = authors[authors.length - 1];
                } else if (browseSubjects) {
                    String[] subjects = browseInfo.getStringResults();
                    s = subjects[subjects.length - 1];
                } else {
                    Item[] items = browseInfo.getItemResults();
                    Item lastItem = items[items.length - 1];
                    s = lastItem.getHandle();
                }
                if (browseDates && oldestFirst) {
                    request.setAttribute("next.query", "order=oldestfirst&amp;top=" + URLEncoder.encode(s, Constants.DEFAULT_ENCODING));
                } else {
                    request.setAttribute("next.query", "top=" + URLEncoder.encode(s, Constants.DEFAULT_ENCODING));
                }
            }
            request.setAttribute("community", community);
            request.setAttribute("collection", collection);
            request.setAttribute("browse.info", browseInfo);
            request.setAttribute("highlight", new Boolean(highlight));
            if (browseAuthors) {
                JSPManager.showJSP(request, response, "/browse/authors.jsp");
            } else if (browseSubjects) {
                JSPManager.showJSP(request, response, "/browse/subjects.jsp");
            } else if (browseDates) {
                request.setAttribute("oldest.first", new Boolean(oldestFirst));
                request.setAttribute("flip.ordering.query", flipOrderingQuery);
                JSPManager.showJSP(request, response, "/browse/items-by-date.jsp");
            } else {
                JSPManager.showJSP(request, response, "/browse/items-by-title.jsp");
            }
        }
    }
}
