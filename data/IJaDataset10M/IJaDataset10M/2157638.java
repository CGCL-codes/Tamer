package com.c2b2.ipoint.model.test;

import com.c2b2.ipoint.model.*;
import java.util.Date;
import java.util.GregorianCalendar;
import junit.framework.TestCase;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

public class NewsItemTestCase extends TestCase {

    public NewsItemTestCase(String sTestName) {
        super(sTestName);
    }

    public void testCreate() throws PersistentModelException {
        String title = "title";
        String category = "category";
        Date date = new Date();
        User user = User.getGuestUser();
        NewsItem item = NewsItem.createNewsItem(title, date, category, user);
        assertNotNull(item);
        assertEquals(category, item.getCategory());
        assertNotNull(item.getContent());
        assertEquals(user, item.getContent().getAuthor());
        assertEquals(date, item.getItemDate());
        assertEquals(item, NewsItem.findItem(item.getID()));
        assertTrue(NewsItem.findItemsForCategory(category).contains(item));
        assertFalse(NewsItem.findItemsForCategory("duffCategory").contains(item));
    }

    public void testDelete() throws PersistentModelException {
        String title = "title";
        String category = "category";
        Date date = new Date();
        User user = User.getGuestUser();
        NewsItem item = NewsItem.createNewsItem(title, date, category, user);
        assertNotNull(item);
        long itemID = item.getID();
        long contentID = item.getContent().getID();
        item.delete();
        assertFalse(NewsItem.findItemsForCategory(category).contains(item));
        try {
            NewsItem.findItem(itemID);
            fail("An Exception should be thrown");
        } catch (Exception ex) {
        }
        try {
            Content.getContent(contentID);
            fail("An Exception should be thrown");
        } catch (Exception ex) {
        }
    }

    public void testCategoryFind() throws PersistentModelException {
        String title = "title";
        Date date = new Date();
        User user = User.getGuestUser();
        NewsItem item1 = NewsItem.createNewsItem(title, date, "category1", user);
        NewsItem item2 = NewsItem.createNewsItem(title, date, "category2", user);
        assertTrue(NewsItem.findItemsForCategory("category1").contains(item1));
        assertFalse(NewsItem.findItemsForCategory("category1").contains(item2));
        assertTrue(NewsItem.findItemsForCategory("category2").contains(item2));
        assertFalse(NewsItem.findItemsForCategory("category2").contains(item1));
        assertFalse(NewsItem.findItemsForCategory("category3").contains(item1));
        assertFalse(NewsItem.findItemsForCategory("category3").contains(item2));
    }

    public void testFindPublishableItems() throws PersistentModelException, HibernateException {
        String title = "title";
        User user = User.getGuestUser();
        String category = "testFindPunishable";
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, -1);
        NewsItem item = NewsItem.createNewsItem(title, calendar.getTime(), category, user);
        assertTrue(NewsItem.findPublishableItemsForCategory(category).contains(item));
        calendar.add(calendar.DATE, 2);
        item.getContent().getPublishedVersion().setValidFrom(calendar.getTime());
        assertFalse(NewsItem.findPublishableItemsForCategory(category).contains(item));
    }
}
