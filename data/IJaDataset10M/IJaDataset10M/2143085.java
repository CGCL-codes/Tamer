package com.gnizr.core.search;

import java.util.List;
import org.apache.log4j.Logger;
import com.gnizr.db.dao.Bookmark;
import com.gnizr.db.dao.DaoResult;
import com.gnizr.db.dao.GnizrDao;
import com.gnizr.db.dao.User;
import com.gnizr.db.dao.bookmark.BookmarkDao;

public class BookmarkSearchResult implements SearchResult<Bookmark> {

    private Logger logger = Logger.getLogger(BookmarkSearchResult.class);

    private BookmarkDao bookmarkDao;

    private User user;

    private String searchQuery;

    private DaoResult<Bookmark> result;

    public BookmarkSearchResult(GnizrDao gnizrDao, String searchQuery) {
        this.searchQuery = searchQuery;
        this.bookmarkDao = gnizrDao.getBookmarkDao();
        doSearch(0, 10);
    }

    public BookmarkSearchResult(GnizrDao gnizrDao, String searchQuery, User user) {
        this.searchQuery = searchQuery;
        this.bookmarkDao = gnizrDao.getBookmarkDao();
        this.user = user;
        doSearch(0, 10);
    }

    private void doSearch(int offset, int count) {
        logger.debug("doSearch: user=" + user + ",offset=" + offset + ",count=" + count);
        if (user == null) {
            result = bookmarkDao.searchCommunityBookmarks(searchQuery, offset, count);
        } else {
            result = bookmarkDao.searchUserBookmarks(searchQuery, user, offset, count);
        }
    }

    public Bookmark getResult(int n) {
        if (n >= result.getResult().size()) {
            doSearch(0, n + 10);
        }
        return result.getResult().get(n);
    }

    public List<Bookmark> getResults(int offset, int count) {
        doSearch(offset, count);
        return result.getResult();
    }

    public int length() {
        return result.getSize();
    }
}
