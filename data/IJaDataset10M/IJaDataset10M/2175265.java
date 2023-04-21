package org.wportal.jspwiki.dbprovider;

import org.wportal.core.ContextManager;
import org.wportal.jspwiki.dbprovider.model.PageComment;
import com.ecyrd.jspwiki.WikiPage;
import com.ecyrd.jspwiki.WikiProvider;

/**
 * User: SimonLei
 * Date: 2004-10-26
 * Time: 19:14:14
 * $Id: TestPageCommentDao.java,v 1.3 2004/10/28 06:21:58 simon_lei Exp $
 */
public class TestPageCommentDao extends TestDatabaseProvider {

    private IPageCommentDao commentDao = (IPageCommentDao) ContextManager.getBean("pageCommentDao");

    public TestPageCommentDao() {
        super("OneRecord");
    }

    public void testGetCommentCount() throws Exception {
        assertEquals(0, commentDao.getCommentCountForPage("Main"));
        assertEquals(1, commentDao.getNextCommentIndex("Main"));
        PageComment pageComment = new PageComment();
        pageComment.setTargetPageName("Main");
        commentDao.putCommentText(pageComment, "Just some test");
        assertEquals(1, commentDao.getCommentCountForPage("Main"));
        assertEquals(2, commentDao.getNextCommentIndex("Main"));
        commentDao.putCommentText(pageComment, "Just some test");
        assertEquals(2, commentDao.getCommentCountForPage("Main"));
        assertEquals(3, commentDao.getNextCommentIndex("Main"));
        provider.deletePage("Main_comments_2");
        assertEquals(1, commentDao.getCommentCountForPage("Main"));
        commentDao.putCommentText(pageComment, "Just some test");
        assertEquals(2, commentDao.getCommentCountForPage("Main"));
        assertEquals(3, commentDao.getNextCommentIndex("Main"));
        provider.deletePage("Main_comments_1");
        assertEquals(1, commentDao.getCommentCountForPage("Main"));
        commentDao.putCommentText(pageComment, "Just some test");
        assertEquals(2, commentDao.getCommentCountForPage("Main"));
        assertEquals(4, commentDao.getNextCommentIndex("Main"));
    }

    public void testCommentAlias() throws Exception {
        PageComment pageComment = new PageComment();
        pageComment.setTargetPageName("ZhongHaoming_blogentry_190804_1");
        commentDao.putCommentText(pageComment, "nono");
        WikiPage page = provider.getPageInfo("ZhongHaoming_comments_190804_1_1", WikiProvider.LATEST_VERSION);
        assertEquals("ZhongHaoming_blogentry_190804_1", page.getAttribute(com.ecyrd.jspwiki.WikiPage.ALIAS));
        pageComment = new PageComment();
        pageComment.setTargetPageName("Main");
        commentDao.putCommentText(pageComment, "nono");
        page = provider.getPageInfo("Main_comments_1", WikiProvider.LATEST_VERSION);
        assertEquals("Main", page.getAttribute(com.ecyrd.jspwiki.WikiPage.ALIAS));
    }
}
