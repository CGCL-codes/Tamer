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
import uk.ac.bris.portlet.bookmarks.xbel.model.XbelChoice;
import uk.ac.bris.portlet.bookmarks.xbel.model.types.FolderFoldedType;

public class moveToFolder extends ActionSupport {

    public static final String GLOBAL_FORWARD_Main = "Main";

    public static final String GLOBAL_FORWARD_AsXbel = "AsXbel";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebApplicationContext ctx = getWebApplicationContext();
        BookmarksFacade facade = (BookmarksFacade) ctx.getBean("bookmarksFacade");
        String source = request.getParameter("move");
        String destination = request.getParameter("pathid");
        int[] sourcePath = XbelUtils.processPathId(source);
        int sourceTokensNo = sourcePath.length;
        int[] destPath;
        int destTokensNo;
        boolean topLevelDestination = (destination.equals("zero"));
        boolean doNotProceed;
        doNotProceed = source.equals(destination);
        if (!(topLevelDestination)) {
            destPath = XbelUtils.processPathId(destination);
            destTokensNo = destPath.length;
            if (sourceTokensNo < destTokensNo) {
                int matches = 0;
                for (int i = 0; i < sourceTokensNo; i++) {
                    if (sourcePath[i] == destPath[i]) {
                        matches++;
                    }
                }
                if (matches == sourceTokensNo) {
                    doNotProceed = true;
                }
            }
        }
        if (!(doNotProceed)) {
            Xbel bookmarks = facade.getBookmarksXbel(request.getRemoteUser());
            int digit = sourcePath[sourceTokensNo - 1];
            boolean done = false;
            if (sourceTokensNo == 1) {
                if (XbelUtils.isBookmark(sourcePath, bookmarks)) {
                    Bookmark bk = XbelUtils.copyBookmark(XbelUtils.getBookmark(sourcePath, bookmarks));
                    if (topLevelDestination) {
                        XbelChoice xbc = bookmarks.removeXbelChoice(digit - 1);
                        bookmarks = XbelUtils.addTopLevelBookmark(bk, bookmarks);
                        done = true;
                    } else {
                        destPath = XbelUtils.processPathId(destination);
                        Folder destFd = XbelUtils.getFolder(destPath, bookmarks);
                        XbelChoice xbc = bookmarks.removeXbelChoice(digit - 1);
                        destFd = XbelUtils.addBookmark2Folder(bk, destFd);
                        destFd.setFolded(FolderFoldedType.NO);
                        done = true;
                    }
                }
                if (!done) {
                    if (XbelUtils.isFolder(sourcePath, bookmarks)) {
                        Folder fd = XbelUtils.copyFolderWithChildren(XbelUtils.getFolder(sourcePath, bookmarks));
                        if (topLevelDestination) {
                            XbelChoice xbc = bookmarks.removeXbelChoice(digit - 1);
                            bookmarks = XbelUtils.addTopLevelFolder(fd, bookmarks);
                        } else {
                            destPath = XbelUtils.processPathId(destination);
                            Folder destFd = XbelUtils.getFolder(destPath, bookmarks);
                            XbelChoice xbc = bookmarks.removeXbelChoice(digit - 1);
                            destFd = XbelUtils.addFolder2Folder(fd, destFd);
                            destFd.setFolded(FolderFoldedType.NO);
                        }
                    }
                }
            } else {
                if (XbelUtils.isBookmark(sourcePath, bookmarks)) {
                    Bookmark bk = XbelUtils.copyBookmark(XbelUtils.getBookmark(sourcePath, bookmarks));
                    if (topLevelDestination) {
                        bookmarks = XbelUtils.deleteFolderNode(sourcePath, bookmarks);
                        bookmarks = XbelUtils.addTopLevelBookmark(bk, bookmarks);
                        done = true;
                    } else {
                        destPath = XbelUtils.processPathId(destination);
                        Folder destFd = XbelUtils.getFolder(destPath, bookmarks);
                        bookmarks = XbelUtils.deleteFolderNode(sourcePath, bookmarks);
                        destFd = XbelUtils.addBookmark2Folder(bk, destFd);
                        destFd.setFolded(FolderFoldedType.NO);
                        done = true;
                    }
                }
                if (!done) {
                    if (XbelUtils.isFolder(sourcePath, bookmarks)) {
                        Folder fd = XbelUtils.copyFolderWithChildren(XbelUtils.getFolder(sourcePath, bookmarks));
                        if (topLevelDestination) {
                            bookmarks = XbelUtils.deleteFolderNode(sourcePath, bookmarks);
                            bookmarks = XbelUtils.addTopLevelFolder(fd, bookmarks);
                        } else {
                            destPath = XbelUtils.processPathId(destination);
                            Folder destFd = XbelUtils.getFolder(destPath, bookmarks);
                            bookmarks = XbelUtils.deleteFolderNode(sourcePath, bookmarks);
                            destFd = XbelUtils.addFolder2Folder(fd, destFd);
                            destFd.setFolded(FolderFoldedType.NO);
                        }
                    }
                }
            }
            facade.writeBookmarks(request.getRemoteUser(), bookmarks);
        }
        return mapping.findForward(GLOBAL_FORWARD_Main);
    }
}
