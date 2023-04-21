package com.ecyrd.jspwiki.project;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mase.system.MaseSystem;
import mase.util.WikipageService;
import mase.wikipage.WikiPage;
import org.apache.log4j.Logger;
import com.ecyrd.jspwiki.filters.RedirectException;

/**
 * @author herbiga
 *
 */
public class WhiteboardDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = -3412061662967951164L;

    Logger log = Logger.getLogger(this.getClass().getName());

    private static MaseSystem ms;

    static {
        try {
            Context ctx = new InitialContext();
            ms = (MaseSystem) ctx.lookup(WikipageService.jndiLookUp);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String showRows = "";
        try {
            if ((req.getParameter("projid") != null)) {
                WikiPage prj = ms.findWikiPage(Long.parseLong(req.getParameter("projid")));
                String author = (String) req.getSession().getAttribute("logon.isDone");
                if ((author != null) && (prj != null)) {
                    if (!ms.checkWritePermission(prj.getFullQualifiedName(), prj.getWritePermission(), author, true)) {
                        throw new RedirectException("You are not allowed to edit anything!", req.getParameter("page"));
                    }
                } else {
                    if (!ms.checkWritePermission(prj.getFullQualifiedName(), prj.getWritePermission(), null, false)) {
                        throw new RedirectException("You are not allowed to edit anything!", req.getParameter("page"));
                    }
                }
            }
            req.getSession();
            req.getSession().removeAttribute("msg");
            res.setContentType("text/html");
            String itemList = req.getParameter("itemList");
            long itemId = -1;
            int itemObj = -1;
            StringTokenizer st = new StringTokenizer(itemList, ",");
            while (st.hasMoreTokens()) {
                itemId = Integer.parseInt(st.nextToken());
                itemObj = Integer.parseInt(st.nextToken());
                if (req.getParameter("displayFlag" + itemId).equalsIgnoreCase("block")) {
                    if (showRows.equalsIgnoreCase("")) {
                        showRows = "" + itemId;
                    } else {
                        showRows = showRows + "," + itemId;
                    }
                }
                if (req.getParameter("checkbox" + itemId) != null) {
                    switch(itemObj) {
                        case 1:
                            {
                                throw new RedirectException("Sorry! Deleting the backlog without deleting the project is not possible!", req.getParameter("page"));
                            }
                        case 2:
                            {
                                if (!ms.setIterationDeleted(itemId, true)) {
                                    throw new RedirectException("Database not reachable!", req.getParameter("page"));
                                }
                                break;
                            }
                        case 3:
                            {
                                if (!ms.setStoryCardDeleted(itemId, true)) {
                                    throw new RedirectException("Database not reachable!", req.getParameter("page"));
                                }
                                break;
                            }
                        case 4:
                            {
                                if (!ms.setTaskDeleted(itemId, true)) {
                                    throw new RedirectException("Database not reachable!", req.getParameter("page"));
                                }
                                break;
                            }
                    }
                }
            }
            req.getSession().setAttribute("whiteboard" + req.getParameter("projid"), showRows);
            res.sendRedirect(req.getParameter("nextpage"));
        } catch (RedirectException e) {
            req.getSession().setAttribute("whiteboard" + req.getParameter("projid"), showRows);
            req.getSession().setAttribute("msg", e.getMessage());
            res.sendRedirect(e.getRedirect());
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        doPost(req, res);
    }
}
