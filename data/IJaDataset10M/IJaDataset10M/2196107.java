package uk.ac.bris.portlet.bookmarks.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.ActionSupport;
import uk.ac.bris.portlet.bookmarks.database.BookmarksFacade;
import uk.ac.bris.portlet.bookmarks.xbel.XbelUtils;
import uk.ac.bris.portlet.bookmarks.xbel.model.Bookmark;
import uk.ac.bris.portlet.bookmarks.xbel.model.Folder;
import uk.ac.bris.portlet.bookmarks.xbel.model.Xbel;

public class add extends ActionSupport {

    public static final String GLOBAL_FORWARD_Main = "Main";

    public static final String GLOBAL_FORWARD_AsXbel = "AsXbel";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebApplicationContext ctx = getWebApplicationContext();
        BookmarksFacade facade = (BookmarksFacade) ctx.getBean("bookmarksFacade");
        String pathid = request.getParameter("pathid");
        String type = request.getParameter("type");
        String title = request.getParameter("title");
        String url = request.getParameter("url");
        if (title.equals("")) {
            title = "Blank";
        }
        Xbel bookmarks = facade.getBookmarksXbel(request.getRemoteUser());
        if (pathid.equals("")) {
            if (type.equals("bookmark")) {
                Bookmark bk = XbelUtils.createBookmark(title, url, "");
                bookmarks = XbelUtils.addTopLevelBookmark(bk, bookmarks);
            }
            if (type.equals("folder")) {
                Folder fd = XbelUtils.createFolder(title, "", false);
                bookmarks = XbelUtils.addTopLevelFolder(fd, bookmarks);
            }
        } else {
            int[] paths = XbelUtils.processPathId(pathid);
            Folder fold = XbelUtils.getFolder(paths, bookmarks);
            if (type.equals("bookmark")) {
                Bookmark bk = XbelUtils.createBookmark(title, url, "");
                fold = XbelUtils.addBookmark2Folder(bk, fold);
            }
            if (type.equals("folder")) {
                Folder fd = XbelUtils.createFolder(title, "", false);
                fold = XbelUtils.addFolder2Folder(fd, fold);
            }
        }
        facade.writeBookmarks(request.getRemoteUser(), bookmarks);
        return mapping.findForward(GLOBAL_FORWARD_Main);
    }
}
