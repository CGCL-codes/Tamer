package com.gnizr.db.dao.foruser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import com.gnizr.db.dao.Bookmark;
import com.gnizr.db.dao.DaoResult;
import com.gnizr.db.dao.ForUser;
import com.gnizr.db.dao.GnizrDBTestBase;
import com.gnizr.db.dao.User;
import com.gnizr.db.dao.bookmark.BookmarkDBDao;
import com.gnizr.db.dao.user.UserDBDao;

public class TestForUserDBDao extends GnizrDBTestBase {

    private ForUserDBDao fuDao;

    public TestForUserDBDao(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        fuDao = new ForUserDBDao(getDataSource());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSet(TestForUserDBDao.class.getResourceAsStream("/dbunit/foruserdbdao/TestForUserDBDao1-input.xml"));
    }

    public void testAddForUser() {
        UserDBDao userDao = new UserDBDao(getDataSource());
        User user = userDao.getUser((1));
        assertNotNull(user);
        BookmarkDBDao bmDao = new BookmarkDBDao(getDataSource());
        Bookmark bookmark = bmDao.getBookmark(301);
        assertNotNull(bookmark);
        ForUser forUser = new ForUser(user, bookmark, "This is just a test", new Date());
        int fuid = fuDao.createForUser(forUser);
        assertTrue(fuid > 0);
        int fuid2 = fuDao.createForUser(forUser);
        assertEquals(fuid, fuid2);
    }

    public void testDeleteForUserForUser() {
        ForUser forUser = fuDao.getForUser(401);
        assertNotNull(forUser);
        assertTrue(fuDao.deleteForUser(forUser.getId()));
        assertFalse(fuDao.deleteForUser(forUser.getId()));
        assertFalse(fuDao.deleteForUser(forUser.getId()));
        assertFalse(fuDao.deleteForUser(forUser.getId()));
        assertNull(fuDao.getForUser(forUser.getId()));
    }

    public void testGetForUser() {
        ForUser forUser = fuDao.getForUser(400);
        assertNotNull(forUser);
        assertNotNull(forUser.getBookmark());
        assertNotNull(forUser.getForUser());
        User fuser = forUser.getForUser();
        assertEquals("jsmith", fuser.getUsername());
        Bookmark bm = forUser.getBookmark();
        assertEquals("cnn", bm.getTitle());
        User buser = forUser.getBookmark().getUser();
        assertEquals("jsmith", buser.getUsername());
    }

    public void testPageForUser() {
        UserDBDao userDao = new UserDBDao(getDataSource());
        User user = userDao.getUser((2));
        assertNotNull(user);
        List<ForUser> bookmarks = fuDao.pageForUser(user, 0, 2);
        assertTrue(bookmarks.size() == 2);
        assertNotNull(bookmarks.get(0).getBookmark().getLink().getUrlHash());
        assertNotNull(bookmarks.get(1).getForUser().getFullname());
    }

    public void testUpdateForUser() {
        ForUser forUser = fuDao.getForUser(400);
        forUser.setMessage("I didn't like that last message");
        fuDao.updateForUser(forUser);
        ForUser newForUser = fuDao.getForUser(400);
        assertTrue(forUser.getMessage().equals(newForUser.getMessage()));
    }

    public void testHasForUser() {
        UserDBDao userDao = new UserDBDao(getDataSource());
        User user = userDao.getUser((2));
        assertNotNull(user);
        BookmarkDBDao bmDao = new BookmarkDBDao(getDataSource());
        Bookmark bookmark = bmDao.getBookmark(301);
        assertNotNull(bookmark);
        assertTrue(fuDao.hasForUser(bookmark, user));
    }

    public void testFindForUserFromBookmark() {
        BookmarkDBDao bmDao = new BookmarkDBDao(getDataSource());
        Bookmark bookmark = bmDao.getBookmark(301);
        assertNotNull(bookmark);
        List<ForUser> linksForUsers = fuDao.findForUserFromBookmark(bookmark);
        assertTrue(linksForUsers.size() == 1);
        ForUser forUser = linksForUsers.get(0);
        assertEquals("jsmith", forUser.getForUser().getUsername());
        assertEquals("foo", forUser.getBookmark().getTitle());
        assertEquals("hchen1", forUser.getBookmark().getUser().getUsername());
    }

    public void testGetForUserCount() {
        int count = fuDao.getForUserCount(new User(2));
        assertEquals(count, 3);
    }

    public void testPageForUserInTimeRange() throws Exception {
        Calendar cals = Calendar.getInstance();
        cals.set(2007, 0, 1, 0, 1, 0);
        Calendar cale = Calendar.getInstance();
        cale.set(2007, 0, 19, 0, 1, 0);
        Date start = cals.getTime();
        Date end = cale.getTime();
        List<ForUser> results = fuDao.pageForUser(new User(2), start, end, 0, 10);
        assertEquals(2, results.size());
        cale.set(2008, 0, 19, 0, 1, 0);
        end = cale.getTime();
        results = fuDao.pageForUser(new User(2), start, end, 0, 10);
        assertEquals(3, results.size());
        results = fuDao.pageForUser(new User(2), start, end, 0, 1);
        assertEquals(1, results.size());
    }

    public void testGetForUserInTimeRangeCount() throws Exception {
        Calendar cals = Calendar.getInstance();
        cals.set(2007, 0, 1, 0, 1, 0);
        Calendar cale = Calendar.getInstance();
        cale.set(2007, 0, 19, 0, 1, 0);
        Date start = cals.getTime();
        Date end = cale.getTime();
        int count = fuDao.getForUserCount(new User(2), start, end);
        assertEquals(2, count);
        cale.set(2008, 0, 19, 0, 1, 0);
        end = cale.getTime();
        count = fuDao.getForUserCount(new User(2), start, end);
        assertEquals(3, count);
    }

    public void testPageForUserBySender() throws Exception {
        DaoResult<ForUser> result = fuDao.pageForUser(new User(2), new User(2), 0, 10);
        assertEquals(1, result.getSize());
        ForUser forUser = result.getResult().get(0);
        assertEquals(300, forUser.getBookmark().getId());
        assertEquals("jsmith", forUser.getBookmark().getUser().getUsername());
        assertEquals("jsmith", forUser.getForUser().getUsername());
        result = fuDao.pageForUser(new User(2), new User(1), 0, 10);
        assertEquals(2, result.getSize());
        result = fuDao.pageForUser(new User(2), new User(1), 1, 10);
        assertEquals(2, result.getSize());
        assertEquals(1, result.getResult().size());
        forUser = result.getResult().get(0);
        assertEquals("jsmith", forUser.getForUser().getUsername());
        assertEquals("hchen1", forUser.getBookmark().getUser().getUsername());
    }

    public void testDeleteAllForUser() throws Exception {
        List<ForUser> result = fuDao.pageForUser(new User(2), 0, 100);
        assertEquals(3, result.size());
        boolean okay = fuDao.deleteAllForUser(new User(2));
        assertTrue(okay);
        result = fuDao.pageForUser(new User(2), 0, 100);
        assertEquals(0, result.size());
    }

    public void testDeleteForUserById() throws Exception {
        int[] ids = { 400, 402 };
        List<ForUser> result = fuDao.pageForUser(new User(2), 0, 100);
        assertEquals(3, result.size());
        boolean okay = fuDao.deleteForUser(new User(2), ids);
        assertTrue(okay);
        result = fuDao.pageForUser(new User(2), 0, 100);
        assertEquals(1, result.size());
        assertEquals(401, result.get(0).getId());
    }

    public void testListForUserSenders() throws Exception {
        List<User> result = fuDao.listForUserSenders(new User(2));
        assertEquals(2, result.size());
    }
}
