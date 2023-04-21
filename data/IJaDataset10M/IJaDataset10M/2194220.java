package com.liferay.portlet.bookmarks.model.impl;

import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.model.BookmarksFolder;
import com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="BookmarksEntryImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class BookmarksEntryImpl extends BookmarksEntryModelImpl implements BookmarksEntry {

    public BookmarksEntryImpl() {
    }

    public BookmarksFolder getFolder() {
        BookmarksFolder folder = null;
        try {
            folder = BookmarksFolderLocalServiceUtil.getFolder(getFolderId());
        } catch (Exception e) {
            folder = new BookmarksFolderImpl();
            _log.error(e);
        }
        return folder;
    }

    private static Log _log = LogFactory.getLog(BookmarksEntryImpl.class);
}
