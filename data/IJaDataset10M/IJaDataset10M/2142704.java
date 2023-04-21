package com.j2biz.blogunity.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.j2biz.blogunity.exception.BlogunityException;
import com.j2biz.blogunity.i18n.I18N;
import com.j2biz.blogunity.i18n.I18NStatusFactory;
import com.j2biz.blogunity.listener.EntryEvent;
import com.j2biz.blogunity.pojo.Blog;
import com.j2biz.blogunity.pojo.CalendarEntry;
import com.j2biz.blogunity.pojo.Entry;
import com.j2biz.blogunity.pojo.User;
import com.j2biz.blogunity.pojo.Userpic;
import com.j2biz.blogunity.util.HibernateUtil;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 *  
 */
public class EntryDAO extends AbstractDAO {

    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(EntryDAO.class);

    /**
     *  
     */
    public EntryDAO() {
        super();
    }

    public Entry getEntryByID(long id) throws BlogunityException {
        return getEntryByID(new Long(id));
    }

    public Entry getEntryByID(Long id) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        Entry entry = null;
        try {
            entry = (Entry) session.load(Entry.class, id);
        } catch (HibernateException ex) {
            log.error("getEntryByID(Long)", ex);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.NOT_FOUND_BY_ID, "Entry", ex));
        }
        return entry;
    }

    public List getEntiresByUserpic(Userpic pic) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Criteria criteria = session.createCriteria(Entry.class);
            criteria.add(Expression.eq("userpic", pic));
            return criteria.list();
        } catch (HibernateException e) {
            log.error("getPaginatedEntriesByCategory(String, Long, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_BY_USERPIC, "Entry", e));
        }
    }

    public List getPaginatedEntriesByCategory(Long blogId, Long categoryId, int firstResult, int maxResults, boolean includeDraftEntries) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Criteria criteria = session.createCriteria(Entry.class);
            if (!includeDraftEntries) {
                criteria.add(Expression.not(Expression.eq("type", new Integer(Entry.DRAFT))));
            }
            criteria.createCriteria("categories").add(Expression.eq("id", categoryId));
            criteria.createCriteria("blog").add(Expression.eq("id", blogId));
            criteria.setFirstResult(firstResult);
            criteria.setMaxResults(maxResults);
            criteria.setCacheable(true);
            return criteria.list();
        } catch (HibernateException e) {
            log.error("getPaginatedEntriesByCategory(String, Long, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_PAGINATED_LIST_BY_CATEGORY, e));
        }
    }

    public List getPaginatedEntriesByCategory(String blogUrlname, Long categoryId, int firstResult, int maxResults, boolean includeDraftEntries) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Criteria criteria = session.createCriteria(Entry.class);
            if (!includeDraftEntries) {
                criteria.add(Expression.not(Expression.eq("type", new Integer(Entry.DRAFT))));
            }
            criteria.createCriteria("categories").add(Expression.eq("id", categoryId));
            criteria.createCriteria("blog").add(Expression.eq("urlName", blogUrlname));
            criteria.setFirstResult(firstResult);
            criteria.setMaxResults(maxResults);
            criteria.setCacheable(true);
            return criteria.list();
        } catch (HibernateException e) {
            log.error("getPaginatedEntriesByCategory(String, Long, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_PAGINATED_LIST_BY_CATEGORY, e));
        }
    }

    public List getPaginatedEntries(String urlname, int firstResult, int maxResults, boolean includeDraftEntries) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            StringBuffer query = new StringBuffer("from Entry entry where entry.blog.urlName = :blogName");
            if (!includeDraftEntries) {
                query.append(" and entry.type <> ");
                query.append(Entry.DRAFT);
            }
            query.append(" order by entry.createTime desc");
            Query q = session.createQuery(query.toString());
            q.setString("blogName", urlname);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResults);
            return q.list();
        } catch (HibernateException e) {
            log.error("getPaginatedEntries(String, int, int, boolean)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_PAGINATED_LIST, "Entries", e));
        }
    }

    /**
     * Returns last 10 entries for the given blog.
     * 
     * @param urlname
     * @param firstResult
     * @param maxResults
     * @return
     * @throws BlogunityException
     */
    public List getRecentBlogEntries(Blog b, boolean includeDraftEntries) throws BlogunityException {
        return getPaginatedEntries(b.getId(), 0, 10, includeDraftEntries);
    }

    /**
     * @param blogId
     * @param firstResult
     * @param maxResults
     * @return
     * @throws BlogunityException
     */
    public List getPaginatedEntries(Long blogId, int firstResult, int maxResults, boolean includeDraftEntries) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            StringBuffer query = new StringBuffer("from Entry entry where entry.blog.id = :blogId");
            if (!includeDraftEntries) {
                query.append(" and entry.type <> ");
                query.append(Entry.DRAFT);
            }
            query.append(" order by entry.createTime desc");
            Query q = session.createQuery(query.toString());
            q.setLong("blogId", blogId.longValue());
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResults);
            return q.list();
        } catch (HibernateException e) {
            log.error("getPaginatedEntries(Long, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_PAGINATED_LIST, "Entries", e));
        }
    }

    public Entry getEntryByIDorAliasname(String param, String blogname) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Query q = session.createQuery("from Entry entry where entry.blog.urlName = :blogName and entry.aliasname=:aliasParam or entry.id=:idParam");
            q.setString("blogName", blogname);
            q.setString("aliasParam", param);
            long id;
            try {
                id = Long.parseLong(param);
            } catch (NumberFormatException e) {
                id = -1;
            }
            q.setLong("idParam", id);
            return (Entry) q.uniqueResult();
        } catch (HibernateException e) {
            log.error("getPaginatedEntries(Long, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_ENTRY_BY_ALIASNAME, "Entry", e));
        }
    }

    public Entry getEntryByAliasname(String alias, String blogname) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Query q = session.createQuery("from Entry entry where entry.blog.urlName = :blogName and entry.aliasname=:aliasName");
            q.setString("blogName", blogname);
            q.setString("aliasName", alias);
            return (Entry) q.uniqueResult();
        } catch (HibernateException e) {
            log.error("getPaginatedEntries(Long, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_ENTRY_BY_ALIASNAME, "Entries", e));
        }
    }

    public List getPaginatedFriendsTape(User user, int firstResult, int maxResults) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Criteria c = session.createCriteria(Entry.class);
            Set friends = user.getFriends();
            if (friends.size() == 0) return Collections.EMPTY_LIST;
            c.add(Expression.in("author", friends));
            c.addOrder(Order.desc("createTime"));
            c.setFirstResult(firstResult);
            c.setMaxResults(maxResults);
            c.setCacheable(true);
            return c.list();
        } catch (HibernateException e) {
            log.error("getPaginatedFriendsTape(User, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_PAGINATED_FRIENDS_TAPE, e));
        }
    }

    public List getPaginatedFavoriteBlogsTape(User user, int firstResult, int maxResults) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Criteria c = session.createCriteria(Entry.class);
            Set blogs = user.getFavoriteBlogs();
            if (blogs.size() == 0) return Collections.EMPTY_LIST;
            c.add(Expression.in("blog", blogs));
            c.addOrder(Order.desc("createTime"));
            c.setFirstResult(firstResult);
            c.setMaxResults(maxResults);
            c.setCacheable(true);
            return c.list();
        } catch (HibernateException e) {
            log.error("getPaginatedFavoriteBlogsTape(User, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_PAGINATED_FAVORITES_TAPE, e));
        }
    }

    public List getPaginatedCommunityBlogsTape(User user, int firstResult, int maxResults) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Criteria c = session.createCriteria(Entry.class);
            Set blogs = user.getContributedBlogs();
            if (blogs.size() == 0) return Collections.EMPTY_LIST;
            c.add(Expression.in("blog", blogs));
            c.addOrder(Order.desc("createTime"));
            c.setFirstResult(firstResult);
            c.setMaxResults(maxResults);
            c.setCacheable(true);
            return c.list();
        } catch (HibernateException e) {
            log.error("getPaginatedFavoriteBlogsTape(User, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_PAGINATED_COMMUNITY_BLOGS_TAPE, e));
        }
    }

    public List getPaginatedFoundedBlogsTape(User user, int firstResult, int maxResults) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Criteria c = session.createCriteria(Entry.class);
            Set blogs = user.getFoundedBlogs();
            if (blogs.size() == 0) return Collections.EMPTY_LIST;
            c.add(Expression.in("blog", blogs));
            c.addOrder(Order.desc("createTime"));
            c.setFirstResult(firstResult);
            c.setMaxResults(maxResults);
            c.setCacheable(true);
            return c.list();
        } catch (HibernateException e) {
            log.error("getPaginatedFoundedBlogsTape(User, int, int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.FETCH_PAGINATED_FOUNDED_BLOGS_TAPE, e));
        }
    }

    public List getEntriesForDay(String blogName, int year, int month, int day, boolean includeDraftEntries) throws BlogunityException {
        Calendar dayObj = Calendar.getInstance();
        dayObj.set(year, month, day, 0, 0, 0);
        return geEntriesForDay(blogName, dayObj, includeDraftEntries);
    }

    public List geEntriesForDay(String blogName, Calendar day, boolean includeDraftEntries) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Calendar dayEnd = Calendar.getInstance();
            dayEnd.setTimeInMillis(day.getTimeInMillis());
            dayEnd.add(Calendar.DATE, 1);
            StringBuffer query = new StringBuffer("from Entry entry where entry.blog.urlName = :blogName and entry.createTime between :begin and :end");
            if (!includeDraftEntries) {
                query.append(" and entry.type <> ");
                query.append(Entry.DRAFT);
            }
            query.append(" order by entry.createTime desc");
            Query q = session.createQuery(query.toString());
            q.setString("blogName", blogName);
            q.setDate("begin", day.getTime());
            q.setDate("end", dayEnd.getTime());
            q.setCacheable(true);
            List returnList = q.list();
            if (log.isDebugEnabled()) {
                log.debug("getBlogEntriesForDay(String, Calendar) - end");
            }
            return returnList;
        } catch (HibernateException e) {
            log.error("getBlogEntriesForDay(String, Calendar)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.ENTRY_BY_DAY, e));
        }
    }

    public List getEntriesForMonth(String blogName, int year, int month, boolean includeDraftEntries) throws BlogunityException {
        Calendar monthObj = Calendar.getInstance();
        monthObj.set(year, month, 1, 0, 0, 0);
        return getEntriesForMonth(blogName, monthObj, includeDraftEntries);
    }

    public List getEntriesForMonth(String blogName, Calendar month, boolean includeDraftEntries) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Calendar monthEnd = Calendar.getInstance();
            monthEnd.setTimeInMillis(month.getTimeInMillis());
            monthEnd.add(Calendar.MONTH, 1);
            StringBuffer query = new StringBuffer("from Entry entry where entry.blog.urlName = :blogName and entry.createTime between :begin and :end");
            if (!includeDraftEntries) {
                query.append(" and entry.type <> ");
                query.append(Entry.DRAFT);
            }
            query.append(" order by entry.createTime desc");
            Query q = session.createQuery(query.toString());
            q.setString("blogName", blogName);
            q.setDate("begin", month.getTime());
            q.setDate("end", monthEnd.getTime());
            q.setCacheable(true);
            return q.list();
        } catch (HibernateException e) {
            log.error("getBlogEntriesForMonth(String, Calendar)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.ENTRY_BY_MONTH, e));
        }
    }

    public List getEntriesForYear(String blogName, int year, boolean includeDraftEntries) throws BlogunityException {
        Calendar yearObj = Calendar.getInstance();
        yearObj.set(year, 0, 1, 0, 0, 0);
        return getEntriesForYear(blogName, yearObj, includeDraftEntries);
    }

    public List getEntriesForYear(String blogName, Calendar year, boolean includeDraftEntries) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Calendar yearEnd = Calendar.getInstance();
            yearEnd.setTimeInMillis(year.getTimeInMillis());
            yearEnd.add(Calendar.YEAR, 1);
            StringBuffer query = new StringBuffer("from Entry entry where entry.blog.urlName = :blogName and entry.createTime between :begin and :end");
            if (!includeDraftEntries) {
                query.append(" and entry.type <> ");
                query.append(Entry.DRAFT);
            }
            query.append(" order by entry.createTime desc");
            Query q = session.createQuery(query.toString());
            q.setString("blogName", blogName);
            q.setDate("begin", year.getTime());
            q.setDate("end", yearEnd.getTime());
            q.setCacheable(true);
            return q.list();
        } catch (HibernateException e) {
            log.error("getBlogEntriesForYear(String, Calendar)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.ENTRY_BY_YEAR, e));
        }
    }

    public Serializable createEntry(Entry entry) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            Serializable result = session.save(entry);
            if (entry.getType() != Entry.DRAFT) {
                Date d = entry.getCreateTime();
                CalendarEntryDAO calendarDAO = new CalendarEntryDAO();
                CalendarEntry ce = null;
                ce = calendarDAO.getCalendarEntryForBlogByDate(entry.getBlog(), d);
                if (ce == null) {
                    ce = new CalendarEntry();
                    ce.setDate(d);
                    ce.setBlog(entry.getBlog());
                }
                calendarDAO.incrementCalendarEntry(ce);
            }
            BlogDAO blogDAO = new BlogDAO();
            Blog b = entry.getBlog();
            b.setLastModified(new Date());
            blogDAO.updateBlog(b);
            eventService.fireEntryEvent(entry, EntryEvent.ENTRY_CREATED);
            return result;
        } catch (HibernateException e) {
            log.error("createEntry(Entry)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.CREATE, "Entry", e));
        }
    }

    public void deleteEntry(Entry entry) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            session.delete(entry);
            Date d = entry.getCreateTime();
            CalendarEntryDAO calendarDAO = new CalendarEntryDAO();
            CalendarEntry ce = calendarDAO.getCalendarEntryForBlogByDate(entry.getBlog(), d);
            if (ce != null) {
                if (ce.getNumberOfMessages() > 1) {
                    calendarDAO.decrementCalendarEntry(ce);
                } else {
                    calendarDAO.deleteCalendarEntry(ce);
                }
            }
            BlogDAO blogDAO = new BlogDAO();
            Blog b = entry.getBlog();
            b.setLastModified(new Date());
            blogDAO.updateBlog(b);
            eventService.fireEntryEvent(entry, EntryEvent.ENTRY_REMOVED);
        } catch (HibernateException e) {
            log.error("deleteEntry(Entry)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.DELETE, "Entry", e));
        }
    }

    public void updateEntry(Entry entry) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            session.update(entry);
            BlogDAO blogDAO = new BlogDAO();
            Blog b = entry.getBlog();
            b.setLastModified(new Date());
            blogDAO.updateBlog(b);
            eventService.fireEntryEvent(entry, EntryEvent.ENTRY_UPDATED);
        } catch (HibernateException e) {
            log.error("updateEntry(Entry)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.UPDATE, "Entry", e));
        }
    }

    public List getLastPostedEntries(int count) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            StringBuffer query = new StringBuffer("from Entry e where e.type <> ");
            query.append(Entry.DRAFT);
            query.append(" order by e.createTime desc");
            Query q = session.createQuery(query.toString());
            q.setFirstResult(0);
            q.setMaxResults(count);
            return q.list();
        } catch (HibernateException e) {
            log.error("getLastPostedEntries(int)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.ENTRY_LAST_POSTED_LIST, e));
        }
    }

    /**
     * Optimized query for fetching total number of entries for given blog.
     * 
     * @param b
     * @return
     * @throws BlogException
     */
    public int getNumberOfEntries(Blog b) throws BlogunityException {
        Session session = HibernateUtil.getSession();
        try {
            return ((Integer) session.createFilter(b.getEntries(), "select count(*)").iterate().next()).intValue();
        } catch (HibernateException e) {
            log.error("getNumberOfEntries(Blog)", e);
            throw new BlogunityException(I18NStatusFactory.create(I18N.ERRORS.ENTRY_TOTAL_NUMBER, e));
        }
    }
}
